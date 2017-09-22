package com.zhph.payment.charge.entity;

import java.io.Serializable;

/**
 * zh_charge_record_detail  表字段
 * @author likang
 */
public class ChargeRecordDetail implements Serializable {
	private static final long serialVersionUID = 1L;
	private String id                   ;            //唯一主键，uuid存储
	private String loan_no               ;           //y合同号
	private String platform_code        ;            //平台编码，加入索引
	private String charge_channel_code  ;            //y扣款渠道编码
	private String single_no              ;           //y批口号，在批扣时使用 交易流水号
	private String branch_org_name       ;           //y分公司名称
	private String loan_name             ;           //y客户姓名
	private String loan_id_card          ;           //y客户身份证号
	private String phone_number          ;           //y客户手机号
	private String payment_date          ;           //y账单日
	private String bill_term             ;           //y还款期数
	private String account_number        ;           //y客户银行卡号
	private String real_amount;						//单扣切分的时候实扣金额
	private String amount                ;           //y金额
	private String operate_name          ;           //y操作人姓名
	private String operate_time          ;           //y操作时间
	private String charge_time           ;           //y扣款日期
	private String charge_message        ;           //y扣款返回信息
	private String charge_status         ;           //扣款状态（0：扣款中1：扣款成功2：扣款失败，默认为0）
	private String push_flag             ;           //推送标志位（0：未推送，1：推送成功，2：尝试推送，但是推送失败）
	private String push_count            ;           //推送次数（每次推送自增1）
	private String flag                  ;           //有效标志位（0：无效1：有效，默认为1）
	private String mian_body             ;           //y主体信息（zh：正合ht：鸿特）加入索引
	private String bank_key              ;           //y银行卡键值
	private String update_flag           ;           //是否更新（0：未更新1：已更新，默认为未更新）
	private String push_time             ;           //y最近推送时间（每次推送都更新）
	private String send_flag             ;           //提供给单扣操作使用，批扣忽略 发送至第三方标志位（0：发送中1：发送成功2：发送失败，3：忽略此字段 默认为0）
	private String create_time           ;           //y创建时间
	private String update_time           ;           //y更新时间
	private String cut_no;							 //单扣是否切分

	private String charge_type          ;            //扣款方式（1：单笔扣款  2：批量扣款）
	private String charge_no			  ;          //y扣款编号，加入索引



	public String getCut_no() {
		return cut_no;
	}

	public void setCut_no(String cut_no) {
		this.cut_no = cut_no;
	}

	public String getReal_amount() {
		return real_amount;
	}

	public void setReal_amount(String real_amount) {
		this.real_amount = real_amount;
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPlatform_code() {
		return platform_code;
	}
	public void setPlatform_code(String platform_code) {
		this.platform_code = platform_code;
	}
	public String getCharge_type() {
		return charge_type;
	}
	public void setCharge_type(String charge_type) {
		this.charge_type = charge_type;
	}
	public String getCharge_channel_code() {
		return charge_channel_code;
	}
	public void setCharge_channel_code(String charge_channel_code) {
		this.charge_channel_code = charge_channel_code;
	}
	public String getCharge_no() {
		return charge_no;
	}
	public void setCharge_no(String charge_no) {
		this.charge_no = charge_no;
	}
	public String getBranch_org_name() {
		return branch_org_name;
	}
	public void setBranch_org_name(String branch_org_name) {
		this.branch_org_name = branch_org_name;
	}
	public String getLoan_no() {
		return loan_no;
	}
	public void setLoan_no(String loan_no) {
		this.loan_no = loan_no;
	}
	public String getLoan_name() {
		return loan_name;
	}
	public void setLoan_name(String loan_name) {
		this.loan_name = loan_name;
	}
	public String getLoan_id_card() {
		return loan_id_card;
	}
	public void setLoan_id_card(String loan_id_card) {
		this.loan_id_card = loan_id_card;
	}
	public String getPhone_number() {
		return phone_number;
	}
	public void setPhone_number(String phone_number) {
		this.phone_number = phone_number;
	}
	public String getPayment_date() {
		return payment_date;
	}
	public void setPayment_date(String payment_date) {
		this.payment_date = payment_date;
	}
	public String getBill_term() {
		return bill_term;
	}
	public void setBill_term(String bill_term) {
		this.bill_term = bill_term;
	}
	public String getAccount_number() {
		return account_number;
	}
	public void setAccount_number(String account_number) {
		this.account_number = account_number;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getOperate_name() {
		return operate_name;
	}
	public void setOperate_name(String operate_name) {
		this.operate_name = operate_name;
	}
	public String getOperate_time() {
		return operate_time;
	}
	public void setOperate_time(String operate_time) {
		this.operate_time = operate_time;
	}
	public String getCharge_time() {
		return charge_time;
	}
	public void setCharge_time(String charge_time) {
		this.charge_time = charge_time;
	}
	public String getCharge_message() {
		return charge_message;
	}
	public void setCharge_message(String charge_message) {
		this.charge_message = charge_message;
	}
	public String getCharge_status() {
		return charge_status;
	}
	public void setCharge_status(String charge_status) {
		this.charge_status = charge_status;
	}
	public String getPush_flag() {
		return push_flag;
	}
	public void setPush_flag(String push_flag) {
		this.push_flag = push_flag;
	}
	public String getPush_count() {
		return push_count;
	}
	public void setPush_count(String push_count) {
		this.push_count = push_count;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public String getMian_body() {
		return mian_body;
	}
	public void setMian_body(String mian_body) {
		this.mian_body = mian_body;
	}
	public String getBank_key() {
		return bank_key;
	}
	public void setBank_key(String bank_key) {
		this.bank_key = bank_key;
	}
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
	public String getUpdate_time() {
		return update_time;
	}
	public void setUpdate_time(String update_time) {
		this.update_time = update_time;
	}
	public String getUpdate_flag() {
		return update_flag;
	}
	public void setUpdate_flag(String update_flag) {
		this.update_flag = update_flag;
	}
	public String getPush_time() {
		return push_time;
	}
	public void setPush_time(String push_time) {
		this.push_time = push_time;
	}
	public String getSend_flag() {
		return send_flag;
	}
	public void setSend_flag(String send_flag) {
		this.send_flag = send_flag;
	}
	public String getSingle_no() {
		return single_no;
	}
	public void setSingle_no(String single_no) {
		this.single_no = single_no;
	}
	 
	
	
}
