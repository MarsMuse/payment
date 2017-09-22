package com.zhph.channel.zj;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.zhph.api.entity.ConstantPay;
import com.zhph.base.redis.ConstantByCache;
import com.zhph.base.utils.XmlConvertHandler;
import com.zhph.entity.BatchPaymentResult;
import com.zhph.payment.charge.entity.CommonChargeInfo;
import com.zhph.payment.charge.entity.ZjBatchChargeBackInfo;
import com.zhph.payment.charge.service.impl.RedisCacheServiceImp;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import payment.api.vo.GatheringItem;

import com.zhph.api.entity.ChargeOperationException;
import com.zhph.channel.PaymentChannel;
import com.zhph.entity.PaymentResult;
import com.zhph.payment.charge.entity.ChargeDetailInfo;
import com.zhph.utils.AccurateCalculation;


/**
 *
 * Author: Zou Yao
 * Description: (中金渠道视图层，该层作用旨在封装第三方渠道所需参数，与解析第三方渠道返回参数)
 * Time: 2017/7/24 11:52
 *
**/
@Service(value = "zjPaymentView")
public class ZjPaymentView implements PaymentChannel {

    //获取到日志打印对象
    private Logger log = LoggerFactory.getLogger(ZjPaymentView.class);

    //注入中金渠道支付交互工具（加密/解密--发送/接收数据）
    @Resource(name = "zjPaymentUtil")
    private ZjPaymentUtil zjPaymentUtil;
    //获取到缓存服务
    @Resource
    private RedisCacheServiceImp redisCache;

    /**
     *
     * Author: zou yao
     * Description: {单笔扣款操作}
     * Date: 2017/7/24 11:52
     *
    **/
    @Override
    public PaymentResult singlePayment(ChargeDetailInfo ChargeInfo) throws Exception {
        log.debug("中金渠道视图层--开始执行单扣，交易号： {} ", ChargeInfo.getChargeNo());
        return  zjPaymentUtil.singlePayment(ChargeInfo);
    }

    /**
     *
     * Author: zou yao
     * Description: {批量扣款操作}
     * Date: 2017/7/24 11:53
     *
     **/
    @Override
    public PaymentResult batchPayment(List<ChargeDetailInfo> ChargeInfoList,  String batchNo,String platformCode) throws Exception {
        PaymentResult paymentResult  =  new PaymentResult();
        paymentResult.setPlatformCode(platformCode);
        BatchPaymentResult batchPaymentResult  = paymentResult.getBatchPaymentResult();
        batchPaymentResult.setPaymentChannel(ConstantPay.ZJ_CODE);

        log.debug("中金渠道视图层--开始执行批扣，批次号："+batchNo);
        //验证数据有效性
        if(ChargeInfoList == null || ChargeInfoList.isEmpty() || batchNo == null ||
                "".equals(batchNo.trim()) || paymentResult == null){
            log.error("中金渠道视图层--数据异常");
            throw new ChargeOperationException("2","数据异常");
        }

        //新建数据集合
        ArrayList<GatheringItem>  dataList = new ArrayList<>();
        //总共金额
        long totalAmount = 0;
        //获取到中金的银行卡对应银行编号--Map(此处应当在Redis缓存中获取到Map)
        Map<String , String >   keyCache = redisCache.getBankCodeCache(ConstantByCache.ZJ_BANK_CODE);
        //判定缓存信息
        if(keyCache == null){
            //更新缓存
            keyCache = redisCache.setBankCodeCache(ConstantByCache.ZJ_CHANNEL_NO , ConstantByCache.ZJ_BANK_CODE);
        }

        for(ChargeDetailInfo tempInfo: ChargeInfoList){
            //新建一个数据传输对象
            GatheringItem transData = new GatheringItem();
            //获取到当前金额（转化为分）
            long nowAmount = AccurateCalculation.mul(tempInfo.getAmount(),100d).longValue();
            //获取到总的金额数（计算）
            totalAmount +=nowAmount;

            //银行代码
            String bankId = keyCache.get(tempInfo.getBankKey());
            //判断如果银行ID不存在
            if(bankId == null || "".equals(bankId)){
                //更新缓存
                keyCache = redisCache.refreshBankCodeCache(ConstantByCache.ZJ_CHANNEL_NO , ConstantByCache.ZJ_BANK_CODE);
                //再次获取
                bankId =  keyCache.get(tempInfo.getBankKey());
            }

            //设置传值参数
            transData.setAmount(nowAmount);//金额
            transData.setBankID(bankId);//银行卡编码
            transData.setItemNo(tempInfo.getChargeNo());
            transData.setAccountNumber(tempInfo.getAccountNumber());//银行卡号
            transData.setAccountName(tempInfo.getLoanName());//客户姓名
            transData.setIdentificationNumber(tempInfo.getLoanIdCard());//身份证号码
            //常量配置
            transData.setAccountType(11);//个人账户
            transData.setIdentificationType("0");//证件类型：身份证
            transData.setSettlementFlag("0001");//结算类型：0001

            //添加到集合之中
            dataList.add(transData);
        }

        //获取到返回值
         zjPaymentUtil.batchPayment(dataList ,paymentResult ,batchNo ,totalAmount);
        return paymentResult;
    }


    /**
     *
     * Author: zou yao
     * Description: {单笔扣款查询}
     * Date: 2017/7/24 11:52
     *
    **/
    @Override
    public PaymentResult singlePaymentQuery(ChargeDetailInfo ChargeInfo) throws Exception {
        log.debug("中金渠道视图层--开始执行单扣查询，交易号："+ChargeInfo.getChargeNo());

        return zjPaymentUtil.singlePaymentQuery(ChargeInfo.getChargeNo());
    }



    /**
     *
     * Author: zou yao
     * Description: {批量扣款查询，将数据从xml转换成对象并且获取到需要更新的公共对象集合}
     * Date: 2017/7/24 11:53
     *
    **/
    @Override
    public PaymentResult batchPaymentQuery(String batchNo,String mainBody) throws Exception {
        log.debug("中金渠道视图层--开始执行中金批扣查询，批次号为： {}" ,batchNo);
        if(batchNo == null || "".equals(batchNo.trim())){
            log.error("中金渠道视图层--数据异常");
            throw new ChargeOperationException("2","数据异常");
        }
        PaymentResult  result = zjPaymentUtil.batchPaymentQuery(batchNo);
        result.getBatchPaymentResult().setPaymentChannel(ConstantPay.ZJ_CODE);
        //获取到响应报文
        String responseContent = result.getRespContent();
        Document doc = DocumentHelper.parseText(responseContent);
        Element rootElement = doc.getRootElement();
        String code = (String) rootElement.element("Head").element("Code").getData();
        List<ZjBatchChargeBackInfo>  batchBackInfoList = null;
        //如果响应为成功
        if("2000".equals(code)){
            //将Xml字符串转换成对象集合
            batchBackInfoList = XmlConvertHandler.domConvert(ZjBatchChargeBackInfo.class ,responseContent ,"Item");
        }else{
            log.error("中金渠道视图层--中金批次号{} 在查询数据异常，异常代码为：{}",batchNo , code);
        }
        //如果存在批扣数据
        if(batchBackInfoList != null && (!batchBackInfoList.isEmpty())){
            log.debug("中金渠道视图层--中金批扣号：{} 查询到扣款结果总条数为： {}" ,batchNo,batchBackInfoList.size());
            //已经扣款完成的公共对象集合
            List<CommonChargeInfo>  commonList = new ArrayList<>();
            for(ZjBatchChargeBackInfo backInfo : batchBackInfoList){
                //获取到已经扣款完成的数据
                if("40".equals(backInfo.getStatus()) || "30".equals(backInfo.getStatus())){
                    //40扣款失败30扣款成功
                    String chargeStatus = "40".equals(backInfo.getStatus())?"2":"1";
                    CommonChargeInfo chargeInfo = new CommonChargeInfo(backInfo.getItemNo() ,chargeStatus ,
                            backInfo.getResponseMessage() ,backInfo.getBankTxTime() ,batchNo);
                    commonList.add(chargeInfo);
                }
            }
            //如果不为空,传入上层
            if(!commonList.isEmpty()){
                log.debug("中金渠道视图层--中金批扣号：{} 查询到已扣款条数为： {}" ,batchNo,commonList.size());
                result.setResultBySelf(commonList);
            }
        }
        return result;
    }
}
