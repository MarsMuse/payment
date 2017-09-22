package com.zhph.entity;

import java.io.Serializable;

import com.zhph.payment.charge.entity.ChargeDetailInfo;

/**
 * 业务平台传参  * 表示必须 
 * @Description: 请求参数实体
 */
public class ParamsEntity extends ChargeDetailInfo implements Serializable{

 
	private static final long serialVersionUID = 1L;
	//第三方交易号
	private Object otherObj;
	/**
	 * 1.单扣
	 * 2.批扣
	 * 3.单扣查询
	 */
	private int chargeType;//判断类型
	
	//第三方平台交易号
	private String platformCode;
	 
	//有效标识位
	private String flag;
	/**
	 * 扣款渠道
	 */
	private String paymentChannel;
	/**
	 * 第三方银行对应code
	 */
	private String bankCode;
	/**
	 * 批扣号  只在批扣时使用。单扣为空
	 */
	private String batchCode;



	/**
	 * 是否切分
	 *  为null 未切分。不为空表示已切分
	 *  合同号加当前时间
	 */
	private String cutNo=null;

	/**
	 * 原始金额
	 */
	private String arginiAmount;

	
	
	
	
	public String getArginiAmount() {
		return arginiAmount;
	}

	public void setArginiAmount(String arginiAmount) {
		this.arginiAmount = arginiAmount;
	}

	public String getCutNo() {
		return cutNo;
	}

	public void setCutNo(String cutNo) {
		this.cutNo = cutNo;
	}
	public String getBatchCode() {
		return batchCode;
	}
	public void setBatchCode(String batchCode) {
		this.batchCode = batchCode;
	}
	public String getBankCode() {
		return bankCode;
	}
	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}
	public String getPaymentChannel() {
		return paymentChannel;
	}
	public void setPaymentChannel(String paymentChannel) {
		this.paymentChannel = paymentChannel;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public String getPlatformCode() {
		return platformCode;
	}
	public void setPlatformCode(String platformCode) {
		this.platformCode = platformCode;
	}
	public int getChargeType() {
		return chargeType;
	}
	public void setChargeType(int chargeType) {
		this.chargeType = chargeType;
	}
	public Object getOtherObj() {
		return otherObj;
	}
	public void setOtherObj(Object otherObj) {
		this.otherObj = otherObj;
	}
	
	
	 
	
}
