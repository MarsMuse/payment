package com.zhph.channel.lianlian;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import com.zhph.payment.charge.entity.ChargeDetailInfo;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lianpay.api.util.TraderRSAUtil;
import com.mysql.jdbc.StringUtils;
import com.zhph.channel.lianlian.entry.BankCardAgreeBean;
import com.zhph.channel.lianlian.entry.BankCardPayBean;
import com.zhph.channel.lianlian.entry.Instalmentsign;
import com.zhph.channel.lianlian.utils.LLPayUtil;
import com.zhph.channel.lianlian.utils.Md5Algorithm;
import com.zhph.channel.lianlian.utils.YTHttpHandler;
import com.zhph.entity.PaymentResult;
import com.zhph.entity.SinglePaymentResult;

public class LianLianPayUtil {
	private static Logger log = Logger.getLogger(LianLianPayUtil.class);
	private static Properties pro;
	static {
		try {
			Properties prop = new Properties();
			prop.load(LianLianPayUtil.class.getClassLoader().getResourceAsStream("lianlianPay.properties"));
			pro = prop;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		ChargeDetailInfo params = new ChargeDetailInfo();
		params.setAmount("10.00");

		params.setLoanIdCard("513027196702060017");
		params.setAccountNumber("6212262318002852824");
		params.setLoanNo("201608101001022519");
		params.setChargeNo("6212262318002852824");
		params.setLoanName("安斌");
		params.setPhoneNumber("18682708126");
		LianLianPayUtil.instalmentsign(params);
	}

	/**
	 * 签约
	 *
	 * @return
	 */
	public static String agreenoauthapply(ChargeDetailInfo params) {
		String date_time = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
		BankCardAgreeBean bankCardAgreeBean = new BankCardAgreeBean();
		bankCardAgreeBean.setOid_partner(params.getLoanNo());
		// 商户用户id
		bankCardAgreeBean.setOid_partner(params.getChargeNo());
		bankCardAgreeBean.setSign_type("RSA");
		bankCardAgreeBean.setApi_version("1.0");
		bankCardAgreeBean.setUser_id(params.getLoanIdCard());
		bankCardAgreeBean.setCard_no(params.getAccountNumber());
		bankCardAgreeBean.setRepayment_no(params.getLoanIdCard());
		String date_val = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		// 分期计划时间需要传当天或者之后的日期
		bankCardAgreeBean.setRepayment_plan(
				"{\"repaymentPlan\":[{\"date\":\"" + date_val + "\",\"amount\":\"" + params.getAmount() + "\"}]}");
		// 短信参数字段, contract_type 商户名称 , contact_way 商户联系电话
		bankCardAgreeBean.setSms_param("{\"contract_type\":\"小额借款测试\",\"contact_way\":\"400-702-6677\"}");
		bankCardAgreeBean.setPay_type("D");
		// 调用签约接口后返回的协议号,和 用户id
		bankCardAgreeBean.setNo_agree(date_time);// "2015121625627086"
		bankCardAgreeBean.setSign(genSign(JSON.parseObject(JSON.toJSONString(bankCardAgreeBean))));
		String reqJson = JSON.toJSONString(bankCardAgreeBean);
		System.out.println(reqJson.toString());
//		HttpRequestSimple httpclent = new HttpRequestSimple();
//		String resJson = httpclent.postSendHttp(SERVER, reqJson);
		String resJson = YTHttpHandler.getInstance().doRequestPostString(reqJson, pro.getProperty("instalmentsign"));
		System.out.println("结果报文为:" + resJson);
		return null;
	}

	/*
	 * 签约授权
	 */
	public static String instalmentsign(ChargeDetailInfo params) {

		//RSA("RSA", "RSA签名"),
		//MD5("MD5", "MD5签名");
		final String enumvluae = "RSA";


		String date_time = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
		String date_val = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		Instalmentsign instalmentsign = new Instalmentsign();
		instalmentsign.setVersion("1.0");
		instalmentsign.setOid_partner(params.getChargeNo());
		instalmentsign.setUser_id(params.getLoanIdCard());
		instalmentsign.setTimestamp(date_time);
		instalmentsign.setSign_type(enumvluae);
		instalmentsign.setUrl_return(pro.getProperty("resultTest"));
		String item = "{\"frms_ware_category\":\"2009\",\"user_info_mercht_userno\":\"" + params.getLoanName() + "\","
				+ "\"user_info_bind_phone\":\"" + params.getPhoneNumber() + "\",\"user_info_dt_register\":\"20131030122130\"}";
		instalmentsign.setRisk_item(item);
		instalmentsign.setId_no(params.getLoanIdCard());
		instalmentsign.setAcct_name(params.getLoanName());
		instalmentsign.setCard_no(params.getAccountNumber());
		String repayment_plan ="{\"repaymentPlan\":[{\"date\":\""+date_val+"\",\"amount\":\""+params.getAmount()+"\"}]}";
		instalmentsign.setRepayment_plan(repayment_plan);
		instalmentsign.setSms_param("{\"contract_type\":\"小额借款测试\",\"contact_way\":\"400-702-6677\"}");

		String sign = LLPayUtil.addSign(JSON.parseObject(JSON.toJSONString(instalmentsign)),
				pro.getProperty("prikeyvalue"), enumvluae);
		instalmentsign.setSign(sign);
		//instalmentsign.setSign(genSign(JSON.parseObject(JSON.toJSONString(instalmentsign))));
		String reqJson = JSON.toJSONString(instalmentsign);
		String resJson = YTHttpHandler.getInstance().doClientPost(reqJson, pro.getProperty("sign"));
		//String resJson = YTHttpHandler.getInstance().doRequestPostString(reqJson, pro.getProperty("sign"));
		System.out.println("结果报文为:" + resJson);
		return resJson;
	}

	/**
	 * 连连扣款(单扣)
	 *
	 * @param params
	 *            扣款参数
	 * @return 代收结果
	 */
	public static PaymentResult singlePayment(ChargeDetailInfo params) {
		PaymentResult result = new PaymentResult();
		SinglePaymentResult singlePaymentResult = new SinglePaymentResult();
		String msg = agreenoauthapply(params);
		if (StringUtils.isNullOrEmpty(msg))
			return null;
		JSONObject agreeJson;
		try {
			agreeJson = JSONObject.parseObject(msg);
		} catch (Exception e) {
			singlePaymentResult.setPaymentCode("3");
			singlePaymentResult.setPaymentDesc("转换失败");
			return null;
		}

		if (agreeJson.get("result_sign").equals("SUCCESS")) {// 成功
			reqbuildservice(params, agreeJson, result, singlePaymentResult);
		} else if (agreeJson.get("result_sign").equals("PROCESSING")) {// 银行签约处理中
			singlePaymentResult.setPaymentCode("2");
			singlePaymentResult.setPaymentDesc("银行签约处理中");
		} else {// FAILURE 失败
			singlePaymentResult.setPaymentCode("3");
			singlePaymentResult.setPaymentDesc((String) agreeJson.get("ret_msg"));
		}

		return result;
	}

	/**
	 * 请求绑定代扣
	 *
	 * @param params
	 *            请求参
	 * @param jsonArgeen
	 *            签约返回参数
	 * @param result
	 *            返回结果
	 * @param singlePaymentResult
	 *            单扣处理结果
	 */
	private static void reqbuildservice(ChargeDetailInfo params, JSONObject jsonArgeen, PaymentResult result,
										SinglePaymentResult singlePaymentResult) {
		BankCardPayBean bank = buildBankParams(params, jsonArgeen);
		bank.setSign_type("RSA");
		bank.setSign(genSign(JSON.parseObject(JSON.toJSONString(bank))));
		String reqJson = JSON.toJSONString(bank);

		log.info("请求报文为:" + reqJson);
		result.setReqContent(reqJson);// 请求报文头
		// 发送请求，并返回结果
		String resp = YTHttpHandler.getInstance().doRequestPostString(reqJson, pro.getProperty("payment"));
		result.setRespContent(resp);// 返回结果报文参数

		JSONObject respJson = JSONObject.parseObject(resp);// 解析json
		singlePaymentResult.setTransNo(respJson.getString("no_order"));// 交易流水号
		// 连连支付定单号
		singlePaymentResult.setPaymentTime(respJson.getString("settle_date"));// 清算时间
		if (respJson.getString("ret_msg").equals("交易成功")) {
			singlePaymentResult.setPaymentCode("1");// 扣款结果
			singlePaymentResult.setPaymentDesc(respJson.getString("ret_msg"));// 描述
			singlePaymentResult.setPaymentAmount(respJson.getDoubleValue("money_order"));// 交易金额
		} else {
			singlePaymentResult.setPaymentCode("3");
			singlePaymentResult.setPaymentDesc((String) respJson.get("info_order"));
		}
		result.setSinglePaymentResult(singlePaymentResult);// 单扣结果
	}

	/**
	 * 绑定参数
	 *
	 * @param params
	 *            请求参数
	 * @param jsonArgeen
	 *            签约结果
	 * @return
	 */
	private static BankCardPayBean buildBankParams(ChargeDetailInfo params, JSONObject jsonArgeen) {
		BankCardPayBean bank = new BankCardPayBean();
		bank.setPlatform("zhphfinance.com");
		bank.setUser_id(params.getLoanIdCard());// 身份证Id
		bank.setOid_partner(params.getChargeNo());// 交易结算商户编号
		bank.setBusi_partner("101001");// 2009
		bank.setApi_version("1.0");
		bank.setNo_order("");// 商户唯一定单号
		bank.setDt_order(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
		bank.setName_goods("银行代扣");

		bank.setMoney_order(params.getAmount().toString());
		bank.setNotify_url("http://www.zhphfinance.com/");
		/**
		 * frms_ware_category 商品类目 user_info_mercht_userno 商户用户唯一标识 ?? "
		 * user_info_bind_phone user_info_full_name user_info_id_no
		 * user_info_dt_register 用户在平台注册时间 ??
		 */
		String item = "{\"frms_ware_category\":\"2009\",\"user_info_mercht_userno\":\"" + params.getLoanName() + "\","
				+ "\"user_info_bind_phone\":\"" + params.getPhoneNumber()+ "\",\"user_info_dt_register\":\"\"}";
		bank.setRisk_item(item);// 风险控制参数
		bank.setSchedule_repayment_date(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));// 还款日期
		bank.setRepayment_no(jsonArgeen.getString("repayment_no"));// 还款计划编号
		bank.setPay_type("D");// 认证支付
		bank.setNo_agree(params.getLoanNo());// 签约协议号

		bank.setValid_order("");// 可选
		bank.setInfo_order("");// 描述

		// private String terminalId; //扣款平台id
		// private String loanContractNo;//合同号
		bank.setOid_partner(params.getLoanNo());
		// private String transNo;// 商户订单号 M
		bank.setUser_id(params.getChargeNo());
		// private String bankCardNo; // 银行卡号
		// private String idCard;// 身份证号码 M
		// private String name;// 姓名 M
		// private String payPhone; // 手机号
		// private Double amount;//交易金额
		// private String payCode;//第三方银行卡请求编码 M
		// private String bankCode; //银行卡编码
		// private String mainBody; //主体：ZH 正合 HT 鸿特
		// private String financingChannel; // 扣款渠道
		return bank;
	}

	/**
	 * 连连扣款查询
	 *
	 * @param transNo
	 *            交易流水号
	 * @return 单扣结果
	 * @throws Exception
	 *             扣款异常
	 */
	public static PaymentResult singlePaymentQuery(String transNo) throws Exception {
		return null;
	}

	private static String genSign(JSONObject reqObj) {
		String sign = reqObj.getString("sign");
		String sign_type = reqObj.getString("sign_type");
		// 生成待签名串
		String sign_src = genSignData(reqObj);
		log.info("商户[" + reqObj.getString("oid_partner") + "]待签名原串" + sign_src);
		log.info("商户[" + reqObj.getString("oid_partner") + "]签名串" + sign);

		if ("MD5".equals(sign_type)) {
			sign_src += "&key=" + pro.getProperty("TRADER_MD5_KEY");
			return signMD5(sign_src);
		}
		if ("RSA".equals(sign_type)) {
			return getSignRSA(sign_src);
		}
		return null;
	}

	private static String signMD5(String signSrc) {
		try {
			return Md5Algorithm.getInstance().md5Digest(signSrc.getBytes("utf-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * RSA签名验证
	 *
	 * @param
	 * @return
	 */
	public static String getSignRSA(String sign_src) {
		// return null;
		return TraderRSAUtil.sign(pro.getProperty("prikeyvalue"), sign_src);

	}

	/**
	 * 生成待签名串
	 *
	 * @param
	 * @return
	 */
	public static String genSignData(JSONObject jsonObject) {
		StringBuffer content = new StringBuffer();

		// 按照key做首字母升序排列
		List<String> keys = new ArrayList<String>(jsonObject.keySet());
		Collections.sort(keys, String.CASE_INSENSITIVE_ORDER);
		for (int i = 0; i < keys.size(); i++) {
			String key = (String) keys.get(i);
			// sign 和ip_client 不参与签名
			if ("sign".equals(key)) {
				continue;
			}
			String value = (String) jsonObject.getString(key);
			// 空串不参与签名
			if (null == value) {
				continue;
			}
			content.append((i == 0 ? "" : "&") + key + "=" + value);

		}
		String signSrc = content.toString();
		if (signSrc.startsWith("&")) {
			signSrc = signSrc.replaceFirst("&", "");
		}
		return signSrc;
	}
}