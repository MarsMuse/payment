package com.zhph.channel.an;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeMap;

import com.zhph.entity.*;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.jnewsdk.connection.client.HttpClient;
import com.jnewsdk.connection.client.HttpSSLClient;
import com.jnewsdk.tools.log.LogFactory;
import com.jnewsdk.util.Base64;
import com.jnewsdk.util.SignUtil;
import com.zhph.base.common.DateUtil;
import com.zhph.base.common.MapToXMLString;
import com.zhph.base.common.ObjectUtil;
import com.zhph.base.common.StringOperator;
import com.zhph.channel.PaymentChannel;
import com.zhph.payment.charge.entity.ChargeDetailInfo;

import javax.annotation.Resource;

/**
 * 第三方爱农支付工具
 *     爱农请示第三方处理功能 
 * 
 * @author likang
 * 
 */
@Service("chinagPaymentView")
public class ChinagPaymentView implements PaymentChannel {
	private static Logger logger = LoggerFactory.getLogger(ChinagPaymentView.class);

	@Resource
	private ChinagPayUtil chinagPayUtil;

	// 需内容做Base64加密
	private static final String[] base64Keys = new String[] { "subject", "body", "remark" };
	// 需内容做Base64加密,并所有子域采用json数据格式
	private static  final String[] base64JsonKeys = new String[] { "customerInfo", "accResv", "riskRateInfo", "billQueryInfo","billDetailInfo" };

	private static final Set<String> base64Key = new HashSet<String>();

	public ChinagPaymentView() {
		base64Key.add("subject");
		base64Key.add("body");
		base64Key.add("remark");
		base64Key.add("customerInfo");
		base64Key.add("accResv");
		base64Key.add("riskRateInfo");
		base64Key.add("billpQueryInfo");
		base64Key.add("billDetailInfo");
		base64Key.add("respMsg");
		base64Key.add("resv");
	}

	@Override
	public PaymentResult singlePayment(ChargeDetailInfo value) throws Exception {
		ParamsEntity params = (ParamsEntity) value;
		PaymentResult paymentResult = new PaymentResult();
		String msg;
		try {
			msg = sendSingleRequest(params, paymentResult);
			handMsg(paymentResult,msg);
		} catch (Exception e) {
			logger.error("爱农扣款异常：ChinagpayPayDsUtil 类 【ChinagpayPayDsSingle】方法");
			throw e ;
		}
		return paymentResult;
	}

	/**
	 * 处理返回消息值
	 * @param paymentResult
	 * @param msg
	 */
	private void handMsg(PaymentResult paymentResult, String msg) {
		BaseResult result = paymentResult.getBfResult();
		SinglePaymentResult singlePaymentResult = paymentResult.getSinglePaymentResult();
		if (StringUtils.isEmpty(msg)) {
            singlePaymentResult.setPaymentCode("3");
            singlePaymentResult.setPaymentDesc("报文发送失败或应答消息为空");
        } else {
            Map<String,String> resMap = parseMsg(msg);
            result.setOrderSeq(resMap.get("merOrderId"));
            // 将返回报文转换为xml格式并记录
            String respXmlStr = MapToXMLString.converterStr(resMap,"result");
            paymentResult.setRespContent(respXmlStr);
            HanderService(singlePaymentResult, resMap);
        }
        paymentResult.setBfResult(result);
		paymentResult.setSinglePaymentResult(singlePaymentResult);
	}

	@Override
	public PaymentResult singlePaymentQuery(ChargeDetailInfo params) throws Exception {
		PaymentResult paymentResult = new PaymentResult();
		String msg ;
		try {
			msg = sendSingleQueryReqeuset((ParamsEntity)params, paymentResult);
			handMsg(paymentResult,   msg);
		} catch (Exception e) {
			logger.error("爱农扣款查询异常：{}",e.getMessage());
			throw e ;
		}
		return paymentResult;
	}
	/**
	 * 把返回状态 处理成，公司需要业务状态
	 * @author likang
	 * @date 2017-8-1下午4:59:47
	 */
	private void HanderService(SinglePaymentResult singlePaymentResult,Map<String, String> resMap) {
		if ("1001".equals(resMap.get("respCode"))) {// 交易成功
			singlePaymentResult.setPaymentCode("1");
			singlePaymentResult.setPaymentDesc("扣款成功");
			singlePaymentResult.setPaymentAmount(StringOperator.div(ObjectUtil.clearDoubleNull(resMap.get("txnAmt")),100, 2));
			singlePaymentResult.setPaymentTime(DateUtil.convertStringToDateString(resMap.get("succTime").toString(), "yyyyMMddHHmmss","yyyy-MM-dd HH:mm:ss"));
		} else if ("1111".equals(resMap.get("respCode"))) {
			singlePaymentResult.setPaymentCode("2");
			singlePaymentResult.setPaymentDesc("扣款中,请稍后查询");
		} else {
			singlePaymentResult.setPaymentCode("3");
			singlePaymentResult.setPaymentDesc(resMap.get("respMsg"));
		}
	}

	/**
	 * 转换报文格式及特殊字段base64解码
	 * 
	 * @param msg
	 *            机密的数据
	 * @return base64解码的数据
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static Map<String, String> parseMsg(String msg) {
		Map<String, String> map = SignUtil.parseResponse(msg);
		// 特殊字段base64解码
		for (Iterator iterator = base64Key.iterator(); iterator.hasNext();) {
			String key = (String) iterator.next();
			String value = map.get(key);
			if (StringUtils.isNotEmpty(value)) {
				try {
					String text = new String(Base64.decode(value.toCharArray()), "UTF-8");
					map.put(key, text);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return map;
	}
	/**
	 * 单扣查询请求 封装参数
	 * @author likang
	 * @throws Exception 
	 * @date 2017-8-1下午4:45:31
	 */
	private  String sendSingleQueryReqeuset(ParamsEntity params,PaymentResult paymentResult) throws Exception{
		  Map<String, String> map = new TreeMap<String, String>(
		   	        	 new Comparator<String>() {
		   	             public int compare(String obj1, String obj2) {
		   	             return obj1.compareTo(obj2);
		   	           }
		   	        }
		   	        );
		Properties properties = chinagPayUtil.getSingleProperties();
		map.put("version", properties.getProperty("version").toString());	//*版本号
		map.put("signMethod",properties.getProperty("signMethod").toString());   //*签名类型
		map.put("txnType", properties.getProperty("txnTypeQuery").toString());		//*查询交易码
		map.put("txnSubType", properties.getProperty("txnSubTypeQuery").toString());    //*交易子类型
		map.put("merId", properties.getProperty("merId").toString());  //商户号-测试环境统一商户号
		//map.put("bizType", properties.getProperty("bizType").toString()); 	//*产品类型
		map.put("merOrderId",params.getChargeNo());  //商户订单号
		
		// 设置签名
		chinagPayUtil.setSignature(map,properties.getProperty("key"));
        //报文明文
        String plain = SignUtil.getURLParam(map, false, null);
        logger.debug("报文明文：{}",plain);
       // 特殊字段数据转换
        converData(map);
        //请求报文
        String reqMsg = SignUtil.getURLParam(map, false, null);
        logger.debug("请求报文--->{} ",reqMsg);
        		
        return sendMsg(properties.getProperty("url").toString(), map,paymentResult);
	}
	/**
	 * 单扣请求
	 * @author likang
	 * @date 2017-8-1下午4:45:26
	 */
	private String sendSingleRequest(ParamsEntity params,PaymentResult paymentResult) throws Exception{
		Properties properties = chinagPayUtil.getSingleProperties();
		 JSONObject customerInfoJson = new JSONObject();
		 // 用户个人信息参数及交易参数
        customerInfoJson.put("certifTp", properties.getProperty("certifTp")); // 证件类型
        customerInfoJson.put("certify_id", params.getLoanIdCard()); // 证件号
        customerInfoJson.put("customerNm", params.getLoanName()); // 姓名
        customerInfoJson.put("cvv2", "");
        customerInfoJson.put("expired", "");
        customerInfoJson.put("phoneNo", params.getPhoneNumber()); // 电话
		 
		Map<String, String> map = new TreeMap<>(
	         new Comparator<String>() {
	             public int compare(String obj1, String obj2) {
	             return obj1.compareTo(obj2);
	           }
	        });
		map.put("version",properties.getProperty("version").toString());	//*版本号
		map.put("signMethod", properties.getProperty("signMethod").toString()); //*签名类型
		map.put("txnType", properties.getProperty("txnType").toString());  		//*交易类型
		map.put("txnSubType", properties.getProperty("txnSubType").toString());	//*交易子类型				
		map.put("bizType", properties.getProperty("bizType").toString()); 	//*产品类型
		map.put("accessType", properties.getProperty("accessType").toString());  	//*接入类型
		map.put("accessMode", properties.getProperty("accessMode").toString());	//*接入方式
		map.put("merId",properties.getProperty("merId").toString());  //商户号-测试环境统一商户号
		map.put("merOrderId",params.getChargeNo());  	//商户号订单号-最小6位最大64位
		map.put("accType",properties.getProperty("accType").toString());     //账户类型 01：借记卡 02：贷记卡 03：存折 04：公司账号
		map.put("accNo", params.getAccountNumber());  		//卡号-交易卡号			

		map.put("customerInfo", customerInfoJson.toString());
		map.put("txnTime", new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));  		//订单发送时间
		map.put("txnAmt",(new BigDecimal(StringOperator.mul(Double.valueOf(params.getAmount()), 100.0)).longValue())	+ "");		//交易金额(分)
		map.put("currency", properties.getProperty("currency").toString());		//*交易币种
		//map.put("backUrl",properties.getProperty("backUrl").toString());     	//后台通知地址-商户接收异步通知的地址
		map.put("payType", properties.getProperty("payType").toString());		//*支付方式
		map.put("bankId", params.getBankCode());		//银行编号
		map.put("ppFlag", properties.getProperty("ppFlag").toString());   //对公对私标志     00：对公  01：对私			
       // 设置签名
		chinagPayUtil.setSignature(map,properties.getProperty("key"));
        //报文明文
        String plain = SignUtil.getURLParam(map, false, null);
        logger.debug("报文明文---> "+plain);
       // 特殊字段数据转换
        converData(map);
        //请求报文
        String reqMsg = SignUtil.getURLParam(map, false, null);
        logger.debug("请求报文---> "+reqMsg);
				//测试地址
		// 发送代扣信息至指定地址，并获取返回代扣结果
		return sendMsg(properties.getProperty("url").toString(), map, paymentResult);
	}

	/**
	 * 往渠道发送数据
	 * 
	 * @param url
	 *            通讯地址
	 * @param paramMap
	 *            发送参数
	 * @return 应答消息
	 */
	private  String sendMsg(String url, Map<String, String> paramMap,PaymentResult paymentResult) throws Exception {
		try {
			HttpClient http = new HttpSSLClient(url, "60000");
			http.setRequestMethod("POST");
			// http.addRequestProperty("Content-Type",
			// "application/x-www-form-urlencoded");
			http.connect();
			// 转换参数格式
			String webForm = chinagPayUtil.getWebForm(paramMap);
			// 将请求报文转换为xml格式,并记录
			String reqXmlStr = MapToXMLString.converterStr(paramMap,"data_content");
			paymentResult.setReqContent(reqXmlStr);
			// 发送请求
			http.send(webForm.getBytes());
			byte[] rspMsg = http.getRcvData();
			return new String(rspMsg, "utf-8");
		} catch (Exception e) {
			LogFactory.getLog().error(e, e.getMessage());
			throw e;
		}
	}

	/**
	 * 验签
	 * 
	 * @param paramMap
	 * @return
	 */
	public boolean verifySign(Map paramMap,String key) {
		// 计算签名
		Set<String> removeKey = new HashSet<String>();
		removeKey.add("signMethod");
		removeKey.add("signature");
		String signedMsg = SignUtil.getSignMsg(paramMap, removeKey);
		String signMethod = (String) paramMap.get("signMethod");
		String signature = (String) paramMap.get("signature");
		return SignUtil.verifySign(signMethod, signedMsg, signature, key, "UTF-8");
	}

	/**
	 * 转换特殊字段
	 * @param paramMap
	 *            参数
	 */

	private static void converData(Map<String, String> paramMap) {
		for (int i = 0; i < base64Keys.length; i++) {
			String key = base64Keys[i];
			String value = (String) paramMap.get(key);
			if (StringUtils.isNotEmpty(value)) {
				try {
					String text = new String(Base64.encode(value.getBytes("UTF-8")));
					// 更新请求参数
					paramMap.put(key, text);
				} catch (Exception e) {
				}
			}
		}
		for (int i = 0; i < base64JsonKeys.length; i++) {
			String key = base64JsonKeys[i];
			String value = (String) paramMap.get(key);
			if (StringUtils.isNotEmpty(value)) {
				try {
					String text = new String(Base64.encode(value.getBytes("UTF-8")));
					// 更新请求参数
					paramMap.put(key, text);
				} catch (Exception e) {
				}
			}
		}
	}


	@Override
	public PaymentResult batchPayment(List<ChargeDetailInfo> paramsEntitys, String batchNo,String platformCode) throws Exception {
		logger.info("暂时未开启。。。。等待第三方平台提供开发接口。此批扣。在业务层实现循环单扣");
	 	return null;
	}

	@Override
	public PaymentResult batchPaymentQuery(String batchNo,String mainBody) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
