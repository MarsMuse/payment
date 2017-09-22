package com.zhph.channel.baofu;

import com.baofoo.util.*;
import com.zhph.api.entity.ChargeOperationException;
import com.zhph.api.entity.ConstantPay;
import com.zhph.base.common.ObjectUtil;
import com.zhph.base.common.StringOperator;
import com.zhph.channel.PaymentChannel;
import com.zhph.channel.baofu.util.JXMConvertUtilBatch;
import com.zhph.channel.baofu.util.MapToXml;
import com.zhph.channel.baofu.util.RsaCodingUtil;
import com.zhph.channel.baofu.util.ZipUtils;
import com.zhph.entity.*;
import com.zhph.payment.charge.entity.ChargeDetailInfo;
import com.zhph.payment.charge.entity.CommonChargeInfo;
import com.zhph.payment.charge.util.UUIDUtil;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 第三方宝付实现接口
 * @author likang
 */
@Service("baofooPaymentView")
public class BaofooPaymentView implements PaymentChannel {
	//获取到日志打印对象
	private Logger log = LoggerFactory.getLogger(BaofooPaymentView.class);
	@Resource
	private BaofooPayUtil baofooPayUtil;



	@Override
	public PaymentResult singlePayment(ChargeDetailInfo params) throws Exception {
		ParamsEntity value = (ParamsEntity)params;
		Date nowDate = new Date();
		Properties properties = baofooPayUtil.getSingleProperties();
		Map<String,String> headPostParam = BaofooPayUtil.getHeadPostParamByMainBody(params.getMainBody(), properties,"txn_sub_type_pay");
		// 设置加密数据
		String biz_type = properties.get("biz_type").toString().trim();
		String trade_date = new SimpleDateFormat("yyyyMMddHHmmss").format(nowDate);// 交易日期
		Map<String, String> XMLArray = new HashMap<>();

		XMLArray.put("txn_sub_type", properties.get("txn_sub_type_pay").toString());
		XMLArray.put("biz_type", biz_type);
		XMLArray.put("terminal_id", headPostParam.get("terminal_id"));
		XMLArray.put("member_id", headPostParam.get("member_id"));
		XMLArray.put("trans_serial_no", params.getChargeNo());
		XMLArray.put("trade_date", trade_date);
		XMLArray.put("additional_info", "附加信息");
		XMLArray.put("req_reserved", "保留");

		// 交易信息
		String txn_amt = String.valueOf(StringOperator.mul(Double.valueOf(params.getAmount()), 100d));// 支付金额转换成分
		XMLArray.put("pay_code", value.getBankCode()); // // 银行卡编码 M
		XMLArray.put("pay_cm", properties.get("pay_cm").toString());
		XMLArray.put("id_card_type",properties.get("id_card_type").toString());
		XMLArray.put("acc_no", params.getAccountNumber()); // 银行卡卡号 M
		XMLArray.put("id_card", params.getLoanIdCard());// 身份证号码 M
		XMLArray.put("id_holder", params.getLoanName());// 姓名 M
		XMLArray.put("mobile", params.getPhoneNumber()); // 手机号
		XMLArray.put("valid_date", null); // 信用卡有效期
		XMLArray.put("valid_no", null); // 信用卡安全码
		XMLArray.put("trans_id", params.getChargeNo()); // 商户订单号 M

		if (params.getMainBody().equalsIgnoreCase(ConstantPay.ZH_COMPANY)) {
			XMLArray.put("txn_amt", txn_amt); // 交易金额M
		}else if(params.getMainBody().equalsIgnoreCase(ConstantPay.HT_COMPANY)){
			XMLArray.put("txn_amt",new BigDecimal(txn_amt.toString()).toString()); // 交易金额M
		}
		PaymentResult paymentResults =  sendSingleRequest(headPostParam, XMLArray, params.getMainBody(),properties);
		return paymentResults;
	}

	/**
	 * 处理返回给业务平台的数据
	 * @param paymentResults 封装结果
	 */
	private void HandlerServiceResult(PaymentResult paymentResults) {
		BaseResult result = paymentResults.getBfResult();
		SinglePaymentResult singlePaymentResult =  paymentResults.getSinglePaymentResult();
		if (baofooPayUtil.getSingleProperties().get("success_code").toString().contains(result.getCode())) {// 扣款成功
			singlePaymentResult.setPaymentCode("1");
			singlePaymentResult.setPaymentDesc("扣款成功");
			//如果成功这个金额没有值 并且 result 没有值就重新赋值
			if(singlePaymentResult.getPaymentAmount() <= 0.0  && result.getAmount() > 0 )
				singlePaymentResult.setPaymentAmount(result.getAmount());
		} else if (baofooPayUtil.getSingleProperties().get("dealing_code").toString().contains(result.getCode())) {
			singlePaymentResult.setPaymentCode("2");// 扣款状态未知，处于口款中
			singlePaymentResult.setPaymentDesc(result.getMsg());
		} else {
			singlePaymentResult.setPaymentCode("3");//  扣款状态未知，参数有问题
			singlePaymentResult.setPaymentDesc(result.getMsg());
		}
		paymentResults.setSinglePaymentResult(singlePaymentResult);
	}

	/**
	 * 根据请求头类型转换
	 * @param paramsMap
	 * @param headPostParam
	 * @return
	 */
	private String switchDataType(Map<String, String> paramsMap, Map<String, String> headPostParam) {
		String XmlOrJson;
		Map<Object, Object> ArrayToObj = new HashMap<>();
		if (headPostParam.get("data_type").equals("xml")) {
			ArrayToObj.putAll(paramsMap);
			XmlOrJson = MapToXMLString.converter(ArrayToObj, "data_content");
		} else {
			JSONObject jsonObjectFromMap = JSONObject.fromObject(paramsMap);
			XmlOrJson = jsonObjectFromMap.toString();
		}
		return XmlOrJson;
	}

	/**
	 * 宝付批扣
	 * @param paramsEntitys 扣款请求参数
	 * @param batchNo  批扣号
	 * @param platformCode  平台号
	 * @return  第三方响应内容
	 * @throws Exception
	 */
	@Override
	public PaymentResult batchPayment(List<ChargeDetailInfo> paramsEntitys, String batchNo,String platformCode) throws Exception {
		PaymentResult result  = new PaymentResult();
		if(paramsEntitys == null && paramsEntitys.size() > 5000){
			result.getBfResult().setMsg("数据异常"+batchNo);
			log.error("数据异常{}",batchNo);
			throw new ChargeOperationException("2","数据异常");
		}
		result.setPlatformCode(platformCode);
		BatchPaymentResult batchPaymentResult  = result.getBatchPaymentResult();
		batchPaymentResult.setPaymentChannel(ConstantPay.BAOFOO_CODE);

		Map<String,List<ChargeDetailInfo>> mainbodyMap = new HashMap<>();
		for (ChargeDetailInfo info : paramsEntitys){
			if(info.getMainBody().equalsIgnoreCase(ConstantPay.HT_COMPANY)){//鸿特账户
				if(!mainbodyMap.containsKey(ConstantPay.HT_COMPANY))
					mainbodyMap.put(ConstantPay.HT_COMPANY,new ArrayList<ChargeDetailInfo>());
				mainbodyMap.get(ConstantPay.HT_COMPANY).add(info);
			}else if(info.getMainBody().equalsIgnoreCase(ConstantPay.ZH_COMPANY)){//正合账户
				if(!mainbodyMap.containsKey(ConstantPay.ZH_COMPANY))
					mainbodyMap.put(ConstantPay.ZH_COMPANY,new ArrayList<ChargeDetailInfo>());
				mainbodyMap.get(ConstantPay.ZH_COMPANY).add(info);
			}
		}
		if(mainbodyMap.size()>0){
			for(Map.Entry<String,List<ChargeDetailInfo>> entry : mainbodyMap.entrySet()){
				String mainBody = entry.getKey();
				List<ChargeDetailInfo> detailList =  entry.getValue();
				if(detailList != null && detailList.size()>0){
					Properties pros = baofooPayUtil.getBatchProperties();//取配置文件
					List<Map<Object, Object>> listParams = new ArrayList<>();//用于封装给第三方的请求参数
					Map<String, String> headPostParam =baofooPayUtil.getHeadPostParamByMainBodyBatch(mainBody,pros,"txn_sub_type_batch");
					// 设置加密数据
					String tradeDateStr = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());// 交易日期
					Map<String, Object> xmlArray = new HashMap<>();
					xmlArray.put("terminal_id", headPostParam.get("terminal_id"));
					xmlArray.put("member_id", headPostParam.get("member_id"));
					xmlArray.put("txn_sub_type", headPostParam.get("txn_sub_type"));
					xmlArray.put("trans_serial_no", batchNo); // 商户批次号
					xmlArray.put("trans_batch_id", batchNo);//流水号
					String id_card_type = pros.get("id_card_type").toString();
					for (ChargeDetailInfo chargeDetailInfo : detailList){
						Map<Object, Object> actionParams = new HashMap<>();
						String transId =chargeDetailInfo.getChargeNo();//交易号 商户订单号
						String acc_no = chargeDetailInfo.getAccountNumber(); //卡号
						String id_holder = chargeDetailInfo.getLoanName(); // 持卡人姓名
						String id_card = chargeDetailInfo.getLoanIdCard();  //身份证号
						String mobile = chargeDetailInfo.getPhoneNumber();	//电话号码
						String txnAmt = String.valueOf(StringOperator.getDimeConvertPenny(Double.valueOf(chargeDetailInfo.getAmount())));// 支付金额转换成分
						String txn_amt = new BigDecimal(txnAmt.toString()).toString();
						String req_reserved = "";
						String tempStr = transId + "#" + tradeDateStr + "#" + acc_no + "#" + id_holder + "#" + id_card_type + "#" + id_card
										 +"#" + mobile + "#" + txn_amt + "#" + req_reserved;
						actionParams.put("info", tempStr);
						listParams.add(actionParams);
					}
					xmlArray.put("actual_info", listParams);// XML样式
					try{
						Map<String, Object> resultParam = sendRequestBatch(headPostParam, xmlArray,pros,mainBody,result,false);
						log.info(resultParam.toString());
						if(resultParam.get("resp_code").equals( "0000")){//受理成功
							log.info("{}:批扣宝付请求成功,{}",batchNo,resultParam.get("resp_code"));
							//Thread.sleep(10000);//让当前线程睡眠1000毫秒
							//this.QueryBatchOther(batchNo,mainBody);//调用当前批扣查询
						}else{
							log.info("{}:批扣宝付请求受理失败,{}",batchNo,resultParam.get("resp_msg"));
						}
					}catch (Exception e){
						result.getBfResult().setMsg("批扣宝付请求第三方封装异常");
						log.info("批扣宝付请求第三方封装异常,{}",e.getMessage());
						e.printStackTrace();
					}finally {
						//直接返回。。因为业务平台返回来的数据只能有一个公司，只有正合。与鸿特
						return result;
					}
				}
			}
		}
		return result;
	}


	@Override
	public PaymentResult batchPaymentQuery(String batchNo,String mainBody) throws Exception {
		return  QueryBatchOther(batchNo,mainBody);
	}

	/**
	 * 查询第三方批扣结果
	 * @param batchNo
	 */
	public  PaymentResult QueryBatchOther(String batchNo,String mainBody) throws  Exception {
		PaymentResult paymentResults = new PaymentResult();
		paymentResults.getBatchPaymentResult().setPaymentChannel(ConstantPay.BAOFOO_CODE);
		Date curDate = new Date();
		Properties pros = baofooPayUtil.getBatchProperties();//取配置文件
		Map<String, String> headPostParam =baofooPayUtil.getHeadPostParamByMainBodyBatch(mainBody,pros,"txn_sub_type_batch_query");
		String transSerialNo = curDate.getTime() + "-" + (int)(Math.random() * 1000000); // 商户流水号
		Map<String, Object> xmlArray = new HashMap<>();
		xmlArray.put("terminal_id", headPostParam.get("terminal_id"));
		xmlArray.put("member_id", headPostParam.get("member_id"));
		xmlArray.put("txn_sub_type", headPostParam.get("txn_sub_type"));
		xmlArray.put("trans_serial_no", transSerialNo);
		xmlArray.put("trans_batch_id", batchNo);
		xmlArray.put("batch_id", "");
		log.info("{}批扣号请求查询前",batchNo);
		Map<String, Object>  mapResult = sendRequestBatch(headPostParam,xmlArray,pros,mainBody,paymentResults,true);
		log.info("{}批扣号请求查询后,处理返回信息开始",batchNo);
		List<CommonChargeInfo> results = HandlerBatchQueryResult(mapResult,batchNo);//得到每个交易号
		log.info("{}批扣号处理返回信息结束",batchNo);
		if(results.size()>0)
			paymentResults.setResultBySelf(results);
		return  paymentResults;
	}

	/**
	 * 批扣请求
	 * @param headPostParam 请求头部
	 * @param encryptParam 需要加密数据
	 * @param pro  配置文件
	 * @param  paymentResults 封装处理结果
	 * @param  isQuery 是否是查询  true 为查询 false 批扣
	 * @return 第三方返回结果
	 * @throws Exception
	 */
	public  Map<String, Object>  sendRequestBatch(Map<String, String> headPostParam, Map<String, Object> encryptParam,Properties pro,
												  String mainBody,PaymentResult paymentResults,Boolean isQuery ) throws Exception{
		String requestUrl = pro.get("url_batch").toString();// 测试地址
		String password = null;
		String pfx_name = null;
		String cer_name = null;
		if(mainBody.equalsIgnoreCase(ConstantPay.HT_COMPANY)){
			password = pro.get("pfx_pwd_ht").toString();
			pfx_name = pro.get("pfx_name_ht").toString();
			cer_name = pro.get("cer_name_ht").toString();
		}else if(mainBody.equalsIgnoreCase(ConstantPay.ZH_COMPANY)){
			password = pro.get("pfx_pwd").toString();
			pfx_name = pro.get("pfx_name").toString();
			cer_name = pro.get("cer_name").toString();
		}
		String pfxpath =PathUtil.getRootClassPath() + pro.get("pfx_path")+ pfx_name;// 商户私钥
		String cerpath =PathUtil.getRootClassPath() + pro.get("cer_path")+cer_name;// 宝付公钥
		Map<Object, Object> arrayToObj = new HashMap<>();// 根据配置的加密数据类型，转换请求参数为xml或json格式
		String xmlOrJson = "";
		if (headPostParam.get("data_type").equals("xml")) {
			arrayToObj.putAll(encryptParam);
			xmlOrJson = MapToXml.Coverter(arrayToObj, "data_content");
		}else {
			JSONObject jsonObjectFromMap = JSONObject.fromObject(encryptParam);
			xmlOrJson = jsonObjectFromMap.toString();
		}
		paymentResults.setReqContent(xmlOrJson);//记录请求报文
		byte[] gZipStr = ZipUtils.gZip(xmlOrJson.getBytes("UTF-8"));// 先压缩数据
		String data_content = RsaCodingUtil.encryptByPriPfxFile(gZipStr, pfxpath, password); // 再RSA
		headPostParam.put("data_content", data_content);
		String postString = HttpUtil.RequestForm(requestUrl, headPostParam);
		if (postString.isEmpty() || (null == postString)) {
			throw new ChargeOperationException("数据返回为空值");
		}
		byte[] PostStringByte = RsaCodingUtil.decryptByPubCerFileByte(postString, cerpath);
		if (postString.isEmpty()) {// 判断解密是否正确。如果为空则宝付公钥不正确
			throw new ChargeOperationException("宝付公钥不正确");
		}
		postString = new String(ZipUtils.unZip(PostStringByte), "UTF-8");
		paymentResults.setRespContent(postString);//记录招接收报文
		log.info("=====返回查询数据解密结果:{}",postString);
		if (headPostParam.get("data_type").equals("xml")) {
			postString = JXMConvertUtil.XmlConvertJson(postString);
		}
		Map<String, Object> arrayDataString = null;
		if(isQuery){
			arrayDataString = JXMConvertUtilBatch.JsonConvertHashMap(postString);
		}else{
			arrayDataString = JXMConvertUtil.JsonConvertHashMap(postString);
		}
		BaseResult result = paymentResults.getBfResult();
		result.setCode(arrayDataString.get("resp_code").toString());
		result.setMsg(arrayDataString.get("resp_msg").toString());
		paymentResults.setBfResult(result);
		return arrayDataString;
	}

	/**
	 * 处理批扣查询  返回结果
	 * @param arrayDataString 第三方返回的结果实体
	 * @param batchNo      当前操作批次号
	 * @return
	 */
	private List<CommonChargeInfo> HandlerBatchQueryResult(Map<String, Object>  arrayDataString,String batchNo) {
		  if(arrayDataString == null)
		  		return  null;
			if (!arrayDataString.containsKey("resp_code")) {
				// return "返回参数异常！XML解析参数[resp_code]不存在";
			}
			List<CommonChargeInfo> listBf = new ArrayList<>();
			if (arrayDataString.get("resp_code").toString().equals("0000")) {
				// ReturnStr = "状态码：" + arrayDataString.get("resp_code") + ",Msg:" + arrayDataString.get("resp_msg");
				if (!arrayDataString.containsKey("actual_info")) {
					log.error("返回参数异常！XML解析参数[actual_info]不存在");
					// return "返回参数异常！XML解析参数[actual_info]不存在";
				}
				List<Map<String, String>> tmplist = (List)arrayDataString.get("actual_info");
				for(int i=0;tmplist != null && i<tmplist.size();i++){
					Map<String, String> map = tmplist.get(i);
 					String[] valList = map.get("actual_info").split("#");
					// key值：info---------------------value:20014969063070408966-1499767109601-69#S#0000#交易成功#201707110110000402902199#4111.37#
					// [20014969063070408966-1499767109601-69, S, 0000, 交易成功, 201707110110000402902199, 4111.37]
					String bankTxTime = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
					//0-0  第一个值是合同号。第二个值是交易流水号
					//String []firstNo  = valList[0].split("-");
					String transId = "";
					if(valList[0].indexOf("-") != -1){//包含
						transId = valList[0].split("-")[1];
					}else{
						transId = valList[0];
					}
					CommonChargeInfo info = new CommonChargeInfo();
					info.setBatchNo(batchNo);//批扣号
					info.setChargeNo(transId);//交易流水号
					info.setChargeMessage(valList[3]);//当前返回信息
					if (valList[1].equalsIgnoreCase("S")) {
						info.setChargeStatus("1");//当前状态 处理成功
						info.setChargeAmount(valList[5]);//已扣金额
						info.setChargeTime(valList[4].substring(0,14));
					}
					if (valList[1].equalsIgnoreCase("I")) {
						continue;//不记录
						//info.setChargeStatus("0");//当前状态 处理中
					}
					if (valList[1].equalsIgnoreCase("F")) {
						info.setChargeStatus("2");//当前状态 处理失败
						info.setChargeTime("");//不记录交易时间
					}
					listBf.add(info);
				}
			}else{
				log.error("请求未成功，返回的消息{}",arrayDataString.get("resp_code"));//请求未成功
			}
			return listBf;
	}

	/**
	 * 发送请求 根据账户的主体不同区分发送请求
	 * @param headPostParam
	 *            请求报文
	 * @param encryptParam
	 *            需要加密数据
	 */
	public  PaymentResult sendSingleRequest(Map<String, String> headPostParam, Map<String, String> encryptParam, String mainBody, Properties pro) throws Exception {
		PaymentResult paymentResults = new PaymentResult();
		String pfxpath = null;
		String cerpath = null;
		String pfx_pwd = null;
		String request_url =pro.get("url").toString().trim();// 测试地址
		if (mainBody.equalsIgnoreCase(ConstantPay.ZH_COMPANY)) {	// 获取本地保存的宝付私钥和公钥路径// 商户私钥
			 pfxpath = PathUtil.getRootClassPath()+ pro.get("pfx_path").toString().trim()	+ pro.get("pfx_name").toString().trim();
			 cerpath = PathUtil.getRootClassPath()+ pro.get("cer_path").toString().trim()+ pro.get("cer_name").toString().trim();//  宝付公钥
			 pfx_pwd = pro.get("pfx_pwd").toString().trim();
		}else if (mainBody.equalsIgnoreCase(ConstantPay.HT_COMPANY)) {// 获取本地保存的宝付私钥和公钥路径
			 pfxpath = PathUtil.getRootClassPath()+pro.get("pfx_path").toString().trim()+pro.get("pfx_name_ht").toString().trim();// 商户私钥
			 cerpath = PathUtil.getRootClassPath()+pro.get("cer_path").toString().trim()+pro.get("cer_name_ht").toString().trim();// 宝付公钥
			 pfx_pwd = pro.get("pfx_pwd_ht").toString().trim();
		}
		String XmlOrJson = switchDataType(encryptParam,headPostParam);
		paymentResults.setReqContent(XmlOrJson);// 记录请求报文
		// 将请求参数进行base64加密
		String base64str = SecurityUtil.Base64Encode(XmlOrJson);
		// 根据私钥文件对请求参数进行加密
		String data_content = RsaCodingUtil.encryptByPriPfxFile(base64str,pfxpath,pfx_pwd);
		// 将加密内容放入
		headPostParam.put("data_content", data_content);
		String PostString = HttpUtil.RequestForm(request_url, headPostParam);
		// 指定公钥路径解密,获取到base64加密的报文
		PostString = RsaCodingUtil.decryptByPubCerFile(PostString, cerpath);
		if (PostString.isEmpty()) {// 判断解密是否正确。如果为空则宝付公钥不正确
			throw new Exception("宝付公钥不正确");
		}
		// 使用Base64解码
		PostString = SecurityUtil.Base64Decode(PostString);
		// 将解码后的报文设置为result的报文内容
		paymentResults.setRespContent(PostString);//记录接收报文
		HandleOtherResult(headPostParam,PostString,paymentResults);
		HandlerServiceResult(paymentResults);//处理返回给业务平台结果
		return paymentResults;
	}

	/**
	 * 处理第三方返回值信息
	 * @param headPostParam 请求头
	 * @param postString 第三方返回信息
	 * @param paymentResults 处理的返回结果
	 * @throws Exception
	 */
	private void HandleOtherResult(Map<String, String> headPostParam, String postString, PaymentResult paymentResults) throws Exception {
		BaseResult result = new BaseResult();
		SinglePaymentResult singlePaymentResult = new SinglePaymentResult();
		if (headPostParam.get("data_type").equals("xml")) {
			postString = JXMConvertUtil.XmlConvertJson(postString);
		}
		// 将JSON转化为Map对象。
		Map<String, Object> ArrayDataString = JXMConvertUtil.JsonConvertHashMap(postString);
		result.setCode(ArrayDataString.get("resp_code").toString());
		result.setMsg(ArrayDataString.get("resp_msg").toString());
		if (headPostParam.get("txn_sub_type").equalsIgnoreCase("13")) {
			if(ArrayDataString.get("trade_date") != null)
				result.setTradeTime(ArrayDataString.get("trade_date").toString());
				//result.setTradeTime(convertStringToDateString(ArrayDataString.get("trade_date").toString(), "yyyyMMddHHmmss","yyyy-MM-dd HH:mm:ss"));
		}
		if (headPostParam.get("txn_sub_type").equalsIgnoreCase("31")) {
			if(ArrayDataString.get("orig_trade_date") != null)
				result.setTradeTime(ArrayDataString.get("orig_trade_date").toString());
				//result.setTradeTime(convertStringToDateString(ArrayDataString.get("orig_trade_date").toString(), "yyyyMMddHHmmss","yyyy-MM-dd HH:mm:ss"));
		}
		//感觉这里有多余。。但是目前不敢改  这里 singlePaymentResult 这个值不应该在这里贬值  逻辑不清楚
		singlePaymentResult.setPaymentCode(ArrayDataString.get("resp_code").toString());
		singlePaymentResult.setPaymentDesc(ArrayDataString.get("resp_msg").toString());
		if(ArrayDataString.get("trade_date") != null){
			singlePaymentResult.setPaymentTime(ArrayDataString.get("trade_date").toString());
			//singlePaymentResult.setPaymentTime(convertStringToDateString(ArrayDataString.get("trade_date").toString(),"yyyyMMddHHmmss", "yyyy-MM-dd HH:mm:ss"));
			singlePaymentResult.setPaymentAmount(StringOperator.div(ObjectUtil.clearDoubleNull(ArrayDataString.get("succ_amt")),100, 2));
		}
		paymentResults.setSinglePaymentResult(singlePaymentResult);
		// 金额返回单位为分,将返回金额转换为元
		result.setAmount(StringOperator.div(ObjectUtil.clearDoubleNull(ArrayDataString.get("succ_amt")),100, 2));
		paymentResults.setBfResult(result);
	}

	/**
	 * 转换日期格式
	 * @param dateStr 日期
	 * @param oldFormat 旧格式
	 * @param newFormat 新格式
	 * @return
	 */
	public static String convertStringToDateString(String dateStr,String oldFormat, String newFormat) {
		if (!StringUtils.isEmpty(dateStr)) {
			SimpleDateFormat format = new SimpleDateFormat(oldFormat);
			try {
				Date date = format.parse(dateStr);
				format = new SimpleDateFormat(newFormat);
				return format.format(date);
			} catch (ParseException e) {
				e.printStackTrace();
			}finally {
				return null;
			}
		}
		return null;
	}

	@SuppressWarnings("static-access")
	@Override
	public PaymentResult singlePaymentQuery(ChargeDetailInfo params) throws Exception {
		PaymentResult paymentResults;
		String mainBody = params.getMainBody();
		String transId = params.getChargeNo();
		String loanNo = params.getLoanNo();
		Properties properties = baofooPayUtil.getSingleProperties();
		Map<String, String> headPostParam = baofooPayUtil.getHeadPostParamByMainBody(mainBody, properties,"txn_sub_type_query");
		// 设置加密数据
        String biz_type = properties.get("biz_type").toString();
        /**
         * 避免transId出现空的情况 Long.valueOf在装箱时出现异常 解决方案 使用 parseLong替换valueOf
         */
		String timevalue = "1"+ transId.replace(loanNo,"");
        Date tradeDate = new Date(Long.valueOf(timevalue));
        String trade_date = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());// 交易日期
        Map<String, String> XMLArray = new HashMap<>();
        XMLArray.put("txn_sub_type", headPostParam.get("txn_sub_type"));
        XMLArray.put("biz_type", biz_type);
        XMLArray.put("terminal_id", headPostParam.get("terminal_id"));
        XMLArray.put("member_id", headPostParam.get("member_id"));
        XMLArray.put("trans_serial_no", loanNo+ (int)(Math.random() * 100));
        XMLArray.put("orig_trade_date", trade_date);
         XMLArray.put("additional_info", "附加信息");
        XMLArray.put("req_reserved", "保留");
        XMLArray.put("orig_trans_id", transId);// 商户订单号(查询)
        // 发送请求并获取扣款结果
        paymentResults= sendSingleRequest(headPostParam, XMLArray, mainBody,properties);
		return paymentResults;
	}




	 

	 
}

