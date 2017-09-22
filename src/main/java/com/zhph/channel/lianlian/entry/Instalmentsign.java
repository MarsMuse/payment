package com.zhph.channel.lianlian.entry;

/**
 * 签约授权
 * 
 * @author zhph
 */
public class Instalmentsign extends TranBaseBean {
	
	private static final long serialVersionUID = 1L;
	private String version; // 版本号
	private String oid_partner; // 支付交易商户编 号
	private String platform; // 平台来源标示
	private String user_id; // 商户用户唯一编 号
	private String timestamp; // 时间戳
	private String sign_type; // 签名方式
	private String sign; // 签名
	private String url_return; // 签约结束回显
	private String risk_item; // 风险控制参数
	private String id_type; // 证件类型
	private String id_no; // 证件号码
	private String acct_name; // 银行账号姓名
	private String card_no; // 银行卡号
	private String back_url; // 返回修改信息地
	private String repayment_plan; // 还款计划
	private String repayment_no; // 还款计划编号
	private String sms_param; // 短信参数
	
	

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getOid_partner() {
		return oid_partner;
	}

	public void setOid_partner(String oid_partner) {
		this.oid_partner = oid_partner;
	}

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getSign_type() {
		return sign_type;
	}

	public void setSign_type(String sign_type) {
		this.sign_type = sign_type;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getUrl_return() {
		return url_return;
	}

	public void setUrl_return(String url_return) {
		this.url_return = url_return;
	}

	public String getRisk_item() {
		return risk_item;
	}

	public void setRisk_item(String risk_item) {
		this.risk_item = risk_item;
	}

	public String getId_type() {
		return id_type;
	}

	public void setId_type(String id_type) {
		this.id_type = id_type;
	}

	public String getId_no() {
		return id_no;
	}

	public void setId_no(String id_no) {
		this.id_no = id_no;
	}

	public String getAcct_name() {
		return acct_name;
	}

	public void setAcct_name(String acct_name) {
		this.acct_name = acct_name;
	}

	public String getCard_no() {
		return card_no;
	}

	public void setCard_no(String card_no) {
		this.card_no = card_no;
	}

	public String getBack_url() {
		return back_url;
	}

	public void setBack_url(String back_url) {
		this.back_url = back_url;
	}

	public String getRepayment_plan() {
		return repayment_plan;
	}

	public void setRepayment_plan(String repayment_plan) {
		this.repayment_plan = repayment_plan;
	}

	public String getRepayment_no() {
		return repayment_no;
	}

	public void setRepayment_no(String repayment_no) {
		this.repayment_no = repayment_no;
	}

	public String getSms_param() {
		return sms_param;
	}

	public void setSms_param(String sms_param) {
		this.sms_param = sms_param;
	}

}
