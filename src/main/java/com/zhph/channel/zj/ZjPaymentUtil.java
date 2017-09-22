package com.zhph.channel.zj;

import java.util.ArrayList;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import payment.api.system.TxMessenger;
import payment.api.tx.gatheringbatch.Tx1610Request;
import payment.api.tx.gatheringbatch.Tx1620Request;
import payment.api.tx.realgathering.Tx2011Request;
import payment.api.tx.realgathering.Tx2020Request;
import payment.api.vo.GatheringItem;
import payment.tools.system.CodeException;
import payment.tools.util.Base64;

import com.zhph.api.entity.ChargeOperationException;
import com.zhph.api.entity.ConstantPay;
import com.zhph.base.common.DateUtil;
import com.zhph.entity.BatchPaymentResult;
import com.zhph.entity.ParamsEntity;
import com.zhph.entity.PaymentResult;
import com.zhph.entity.SinglePaymentResult;
import com.zhph.payment.charge.entity.ChargeDetailInfo;
import com.zhph.utils.AccurateCalculation;


/**
 *
 * Author: Zou Yao
 * Description: (中金数据传输层，该层旨在加密发送数据，接收解密返回数据)
 * Time: 2017/7/24 11:57
 *
**/
@Component(value = "zjPaymentUtil")
public class ZjPaymentUtil {

    private  static  final  String  DEFAULT_CHAR_SET = "UTF-8";

    //获取到日志打印对象
    private Logger log = LoggerFactory.getLogger(ZjPaymentUtil.class);


    /**
     *
     * Author: zou yao
     * Description: {单笔扣款操作}
     * Date: 2017/7/25 16:16
     *
    **/
    public PaymentResult singlePayment(ChargeDetailInfo  value) throws Exception{
        log.info("中金数据传输层--单笔扣款操作");
    	ParamsEntity chargeInfo = (ParamsEntity) value;
        //返回结果
        PaymentResult  result = new PaymentResult();
        //新建单扣请求
        Tx2011Request singleRequest = new Tx2011Request();

        //配置参数（常量）
        singleRequest.setInstitutionID("001926");//机构编码
        singleRequest.setAccountType(11);//个人账户
        singleRequest.setIdentificationType("0");//证件类型
        singleRequest.setSettlementFlag("0001");//交易类型
        //变量
        //获取到当前金额（转化为分）
        long nowAmount;
        try {
            nowAmount = AccurateCalculation.mul(chargeInfo.getAmount(), 100d).longValue();
        }catch (Exception e){
            throw new ChargeOperationException("2" , "金额转换失败");
        }
        //银行代码
        //String bankId = keyCache.get(chargeInfo.getBankKey());
        singleRequest.setBankID(chargeInfo.getBankCode());//银行编码
        singleRequest.setAmount(nowAmount);//金额
        singleRequest.setAccountNumber(chargeInfo.getAccountNumber());//银行卡号
        singleRequest.setAccountName(chargeInfo.getLoanName());//用户名
        singleRequest.setIdentificationNumber(chargeInfo.getLoanIdCard());//身份证号
        singleRequest.setTxSN(chargeInfo.getChargeNo());//交易号
        //加密过程
        try {
            singleRequest.process();
        }catch (Exception e){
            e.printStackTrace();
            throw new ChargeOperationException("2","中金加密单扣请求报文异常");
        }
        //获取到请求报文字符串
        String requestXmlContent = singleRequest.getRequestPlainText();
        result.setReqContent(requestXmlContent);
        //获取到信息
        String message = singleRequest.getRequestMessage();
        //获取签名
        String signature = singleRequest.getRequestSignature();
        //获取到信息处理工具
        TxMessenger messageUtil = new TxMessenger();
        //获取到被base64加密后的数据
        //获取到返回信息
        String[] responseMessage = this.dealSendInfo(messageUtil ,message ,  signature );
        if(responseMessage != null) {
            //获取到响应内容
            String responseContent = new String(Base64.decode(responseMessage[0]), DEFAULT_CHAR_SET);
            //设置返回结果
            result.setRespContent(responseContent);
            
            addSinglePaymentResult(result, responseContent);
        }

        return result;
    }

    /**
     * 添加单扣返回值
     * @author likang
     * @date 2017-7-26下午2:33:33
     */
	private void addSinglePaymentResult(PaymentResult result,String responseContent) throws DocumentException {
        log.info("中金数据传输层--添加单扣返回值");
		SinglePaymentResult singlePaymentResult =   new SinglePaymentResult();
		//解析报文
		Document doc = DocumentHelper.parseText(responseContent);
		Element rootElt = doc.getRootElement();
		String code = (String) rootElt.element("Head").element("Code").getData();
		if(code.equals("2000")){
		    Element bodyElement = rootElt.element("Body");
		    String amountString = (String) bodyElement.element("Amount").getData();
		    String status = (String) bodyElement.element("Status").getData();
		    String returnTimeString = (String) bodyElement.element("BankTxTime").getData();
		    if("30".equals(status)){
		        singlePaymentResult.setPaymentCode("1");
		        singlePaymentResult.setPaymentDesc("扣款成功");
		        singlePaymentResult.setPaymentAmount(Double.valueOf(amountString) / 100);
		        singlePaymentResult.setPaymentTime(DateUtil.convertStringToDateString(returnTimeString, "yyyyMMddHHmmss", "yyyy-MM-dd HH:mm:ss"));
		    }else if("20".equals(status)){
		        singlePaymentResult.setPaymentCode("2");
		        singlePaymentResult.setPaymentDesc("扣款中,请稍后查询");
		    } else if("40".equals(status)){
		        singlePaymentResult.setPaymentCode("3");
		        singlePaymentResult.setPaymentDesc((String)bodyElement.element("ResponseMessage").getData());
		    }
		}else{
		    Element bodyElement = rootElt.element("Head");
		    singlePaymentResult.setPaymentCode("3");
		    singlePaymentResult.setPaymentDesc((String)bodyElement.element("Message").getData());
		}
		result.setSinglePaymentResult(singlePaymentResult);
	}
 
    /**
     *
     * Author: zou yao
     * Description: {单扣查询}
     * Date: 2017/7/25 18:09
     *
    **/
    public PaymentResult singlePaymentQuery(String chargeNo) throws Exception{
        log.info("中金数据传输层--单扣查询");
        PaymentResult result = new PaymentResult();
        //单扣查询对象
        Tx2020Request queryRequest = new Tx2020Request();
        queryRequest.setInstitutionID("001926");
        queryRequest.setTxSN(chargeNo);

        try {
            queryRequest.process();
        } catch (Exception e) {
            e.printStackTrace();
            throw new ChargeOperationException("2","中金加密单扣查询请求报文异常");
        }

        //获取到请求报文字符串
        String requestXmlContent = queryRequest.getRequestPlainText();
        result.setReqContent(requestXmlContent);
        //获取到信息
        String message = queryRequest.getRequestMessage();
        //获取签名
        String signature = queryRequest.getRequestSignature();
        //获取到信息处理工具
        TxMessenger messageUtil = new TxMessenger();
        //获取到被base64加密后的数据
        //获取到返回信息
        String[] responseMessage = this.dealSendInfo(messageUtil ,message ,  signature );
        if(responseMessage != null) {
            //获取到响应内容
            String responseContent = new String(Base64.decode(responseMessage[0]), DEFAULT_CHAR_SET);
            //设置返回结果
            result.setRespContent(responseContent);
            addSinglePaymentResult(result, responseContent);
        }

        return result;

    }


    /**
     *
     * Author: zou yao
     * Description: {中金批扣工具类}
     * Date: 2017/7/24 15:13
     *
    **/
    public String batchPayment(ArrayList<GatheringItem> dataList , PaymentResult paymentResult, String batchNo , long totalAmount) throws Exception{
        log.info("中金批扣工具类--中金批扣工具类");
        //验证数据有效性
        if(dataList == null || dataList.isEmpty() || paymentResult ==null ||  batchNo ==null || "".equals(batchNo.trim()) || totalAmount ==0){
            log.error("中金批扣工具类--数据异常");
            throw new ChargeOperationException("2","数据异常");
        }
        BatchPaymentResult batchResult =  new BatchPaymentResult();
        //新建一个请求对象
        Tx1610Request  zjRequest =  new Tx1610Request();
        //设置参数
        zjRequest.setInstitutionID("001926");//机构码
        zjRequest.setBatchNo(batchNo);//批次号
        zjRequest.setTotalAmount(totalAmount);//总金额
        zjRequest.setTotalCount(dataList.size());//设置总条数
        zjRequest.setGatheringItemList(dataList);//设置总数据
        try{
            //拼接成请求报文
            zjRequest.process();
        } catch (Exception e) {
            e.printStackTrace();
            throw new ChargeOperationException("2","中金加密批扣查询请求报文异常");
        }
        //获取到请求报文字符串
        String requestXmlContent = zjRequest.getRequestPlainText();
        //存入对象中
        paymentResult.setReqContent(requestXmlContent);
        batchResult.setBatchNo(batchNo);
        batchResult.setPaymentChannel(ConstantPay.ZJ_CODE);
        //信息处理对象
        TxMessenger messageUtil = new TxMessenger();
        //获取到请求信息
        String message =  zjRequest.getRequestMessage();
        //获取到签名
        String signature = zjRequest.getRequestSignature();
        //获取到被base64加密后的数据
        //获取到返回信息
        String[] responseMessage = this.dealSendInfo(messageUtil ,message ,  signature );
        //响应内容
        String responseContent = null;
        if(responseMessage != null) {
            //获取到响应的字符串内容
            responseContent = new String(Base64.decode(responseMessage[0]), DEFAULT_CHAR_SET);
            //设置响应内容
            paymentResult.setRespContent(responseContent);
        }
        paymentResult.setBatchPaymentResult(batchResult);
        return responseContent;
    }



    /**
     *
     * Author: zou yao
     * Description: {批量扣款查询查询}
     * Date: 2017/7/25 11:49
     *
    **/
    public PaymentResult batchPaymentQuery(String batchNo) throws Exception{
        //新建一个返回对象
        PaymentResult result = new PaymentResult();

        //获取到请求对象
        Tx1620Request queryRequest = new Tx1620Request();

        //设置参数
        queryRequest.setInstitutionID("001926");
        queryRequest.setBatchNo(batchNo);
        //生成报文
        try {
            queryRequest.process();
        } catch (Exception e) {
            e.printStackTrace();
            throw new ChargeOperationException("2","中金加密批扣查询请求报文异常");
        }

        //获取到请求报文字符串
        String requestXmlContent = queryRequest.getRequestPlainText();
        result.setReqContent(requestXmlContent);
        //获取到信息
        String message = queryRequest.getRequestMessage();
        //获取签名
        String signature = queryRequest.getRequestSignature();
        //获取到信息处理工具
        TxMessenger messageUtil = new TxMessenger();
        //获取到被base64加密后的数据
        //获取到返回信息
        String[] responseMessage = this.dealSendInfo(messageUtil ,message ,  signature );
        if(responseMessage != null) {
            //获取到响应内容
            String responseContent = new String(Base64.decode(responseMessage[0]), DEFAULT_CHAR_SET);
            //设置返回结果
            result.setRespContent(responseContent);
        }
        return result;
    }



    /**
     *
     * Author: zou yao
     * Description: {处理请求返回信息与异常}
     * Date: 2017/7/25 12:28
     *
    **/
    private String[] dealSendInfo(TxMessenger messageUtil , String message , String signature)throws Exception{
        String[] responseMessage = null;
        try {
            responseMessage = messageUtil.send(message, signature);
        }catch (Exception e){
            log.error("中金批扣工具类--处理请求返回信息与异常" ,e);
            if(e instanceof CodeException){
                CodeException ce = (CodeException) e;
                //通讯异常
                if("280001".equals(ce.getCode())){
                    log.error("中金批扣工具类--数据发送出现通讯异常");
                    throw new ChargeOperationException("2","数据发送出现通讯异常");
                }
            }else {
                throw e;
            }
        }
        return responseMessage;
    }
}
