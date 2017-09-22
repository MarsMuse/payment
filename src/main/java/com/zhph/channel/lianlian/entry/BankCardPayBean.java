package com.zhph.channel.lianlian.entry;

public class BankCardPayBean {

    private static final long serialVersionUID = 1L;
    
    //** 表示必须设置参数
    private String            platform;             // 平台来源标示
    private String            user_id;              //** 商户唯一ID
    private String            oid_partner;          //** 商户编号
    private String            sign_type;            //** 签名方式  RSA
    private String            sign;					//** RSA加密签名
    private String            busi_partner;         //** 商户业务类型
    private String            api_version;          //** 版本标识
    private String            no_order;             //** 商户唯一订单号
    private String            dt_order;             //** 商户订单时间
    private String            name_goods;           //** 商品名称
    private String            info_order;           // 订单描述
    private String            money_order;          //** 交易金额 单位元
    private String            notify_url;           //** 服务器异步通知地址
    private String            valid_order;          // 订单有效期
    private String            risk_item;            //** 风控参数
    private String         schedule_repayment_date; //** 计划还款日期
    private String            repayment_no;         //** 计划还款编号
    private String            pay_type;             //** 支付方式
    private String            no_agree;             //** 银通协议编号
    
    
    
    /*private String            card_no;              // 银行卡号
    private String            bank_code;            // 银行编号
    private String            acct_name;            // 银行账号姓名
    private String            bind_mob;             // 绑定手机号
    private String            vali_date;            // 信用卡有效期
    private String            cvv2;                 // 信用卡CVV2
    private String            id_type;              // 证件类型
    private String            id_no;                // 证件号码
    private String            verify_code;          // 短信验证码
    private String            req_ip;               // 请求IP
    private String            oid_paybill;          // 支付单号
    private String            result_pay;           // 支付结果
    private String            settle_date;          // 清算日期
    private String            oid_userno;           // 用户编号
    private String            token;                // 授权码
    private String            times_errmsg;         // 验证码输错次数
    private String            agreementno;          // 协议编号
    private String            item_product;         // 商品条目
    private String            sms_flag;             // 0 不发送短信 1发送短信
    private String            user_login;
    private String            flag_card_bind;       // 银行卡绑定标识
    private String            flag_smssend;         //是否需要发送短信验证码 标识
    private String            flag_sign;            //签约标识
    private String            flag_sms_verify;     //短信验证标识
*/
    
    
    
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
	public String getOid_partner() {
		return oid_partner;
	}
	public void setOid_partner(String oid_partner) {
		this.oid_partner = oid_partner;
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
	public String getBusi_partner() {
		return busi_partner;
	}
	public void setBusi_partner(String busi_partner) {
		this.busi_partner = busi_partner;
	}
	public String getApi_version() {
		return api_version;
	}
	public void setApi_version(String api_version) {
		this.api_version = api_version;
	}
	public String getNo_order() {
		return no_order;
	}
	public void setNo_order(String no_order) {
		this.no_order = no_order;
	}
	public String getDt_order() {
		return dt_order;
	}
	public void setDt_order(String dt_order) {
		this.dt_order = dt_order;
	}
	public String getName_goods() {
		return name_goods;
	}
	public void setName_goods(String name_goods) {
		this.name_goods = name_goods;
	}
	public String getInfo_order() {
		return info_order;
	}
	public void setInfo_order(String info_order) {
		this.info_order = info_order;
	}
	public String getMoney_order() {
		return money_order;
	}
	public void setMoney_order(String money_order) {
		this.money_order = money_order;
	}
	public String getNotify_url() {
		return notify_url;
	}
	public void setNotify_url(String notify_url) {
		this.notify_url = notify_url;
	}
	public String getValid_order() {
		return valid_order;
	}
	public void setValid_order(String valid_order) {
		this.valid_order = valid_order;
	}
	public String getRisk_item() {
		return risk_item;
	}
	public void setRisk_item(String risk_item) {
		this.risk_item = risk_item;
	}
	public String getSchedule_repayment_date() {
		return schedule_repayment_date;
	}
	public void setSchedule_repayment_date(String schedule_repayment_date) {
		this.schedule_repayment_date = schedule_repayment_date;
	}
	public String getRepayment_no() {
		return repayment_no;
	}
	public void setRepayment_no(String repayment_no) {
		this.repayment_no = repayment_no;
	}
	public String getPay_type() {
		return pay_type;
	}
	public void setPay_type(String pay_type) {
		this.pay_type = pay_type;
	}
	public String getNo_agree() {
		return no_agree;
	}
	public void setNo_agree(String no_agree) {
		this.no_agree = no_agree;
	}
    
    
}
