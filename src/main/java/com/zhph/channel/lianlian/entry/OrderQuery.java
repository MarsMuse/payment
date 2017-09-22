package com.zhph.channel.lianlian.entry;

/**
 * 支付结果查询交易bean
 * @author 
 * @date:2013-5-20 上午11:51:41
 * @version :1.0
 * 
 */
public class OrderQuery {
    private static final long serialVersionUID = 1L;
    
    //** 表示必须设置参数
    private String            oid_partner;	//** 商户编号，是在连连支付平台上开设的商户编号，为18位数字
    private String            sign_type;	//** 签名方式，RSA | MD5
    private String            sign;			//** RSA加密签名，见安全签名机制
    private String            no_order;		//** 商户订单号，合同号。。
    private String            dt_order;		//** 商户订单时间  YYYYMMDDH24MISS 14位
    private String            oid_paybill;	// 连连支付单号
    private String            query_version;// 查询版本号，默认1.0，1.1新增memo字段，银行名称bank_name字段 
    
    
    
    
    
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
	public String getOid_paybill() {
		return oid_paybill;
	}
	public void setOid_paybill(String oid_paybill) {
		this.oid_paybill = oid_paybill;
	}
	public String getQuery_version() {
		return query_version;
	}
	public void setQuery_version(String query_version) {
		this.query_version = query_version;
	}
    
}
