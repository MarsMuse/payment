package com.zhph.manager.model;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * 批扣记录详情列表显示
 * Created by lidongkui on 2017/8/9.
 */
public class BatchChargeRecordDetail{
	
	/**
	 * 序号
	 */
	private String rn;
	
	/**
	 * 平台名称
	 */
	private String platformName;
	
	/**
	 * 扣款方式（1：单笔扣款  2：批量扣款）
	 */
	private String chargeType;
	
	/**
	 * 扣款渠道名称
	 */
	private String chargeChannelName;
	
	/**
	 * 扣款编号
	 */
	private String chargeNo;
	
	/**
	 * 分公司名称
	 */
	private String branchOrgName;
	
	/**
	 * 合同号
	 */
	private String loanNo;
	
	/**
	 * 客户姓名
	 */
	private String loanName;
	
	/**
	 * 客户身份证号
	 */
	private String loanIdCard;
	
	/**
	 * 客户手机号
	 */
	private String phoneNumber;
	
	/**
	 * 账单日
	 */
	private String paymentDate;
	
	/**
	 * 还款期数
	 */
	private String billTerm;
	
	/**
	 * 客户银行卡号
	 */
	private String accountNumber;
	
	/**
	 * 扣款金额
	 */
	private String amount;
	
	/**
	 * 操作人姓名
	 */
	private String operateName;
	
	/**
	 * 操作时间
	 */
	private String operateTime;
	
	/**
	 * 扣款日期
	 */
	private String chargeTime;
	
	/**
	 * 扣款返回信息
	 */
	private String chargeMessage;
	
	/**
	 * 扣款状态（0：扣款中1：扣款成功2：扣款失败，默认为0）
	 */
	private String chargeStatus;
	
	/**
	 * 推送标志位（0：未推送，1：推送成功，2：尝试推送，但是推送失败）
	 */
	private String pushFlag;
	
	/**
	 * 推送次数（每次推送自增1）
	 */
	private String pushCount;
	
	/**
	 * 最近推送时间（每次推送都更新）
	 */
	private Timestamp pushTime;
	
	/**
	 * 主体信息（ZH：正合HT：鸿特）
	 */
	private String mianBody;
	
	/**
	 * 银行卡键值
	 */
	private String bankKey;
	
	/**
	 * 创建时间
	 */
	private Timestamp createTime;
	
	/**
	 * 更新时间
	 */
	private Timestamp updateTime;
	
	/**
	 * 是否更新（0：未更新1：已更新，默认为未更新）
	 */
	private String updateFlag;


	public String getRn() {
		return rn;
	}

	public void setRn(String rn) {
		this.rn = rn;
	}

	public String getPlatformName() {
		return platformName;
	}

	public void setPlatformName(String platformName) {
		this.platformName = platformName;
	}

	public String getChargeType() {
		return chargeType;
	}

	public void setChargeType(String chargeType) {
		this.chargeType = chargeType;
	}

	public String getChargeChannelName() {
		return chargeChannelName;
	}

	public void setChargeChannelName(String chargeChannelName) {
		this.chargeChannelName = chargeChannelName;
	}

	public String getChargeNo() {
		return chargeNo;
	}

	public void setChargeNo(String chargeNo) {
		this.chargeNo = chargeNo;
	}

	public String getBranchOrgName() {
		return branchOrgName;
	}

	public void setBranchOrgName(String branchOrgName) {
		this.branchOrgName = branchOrgName;
	}

	public String getLoanNo() {
		return loanNo;
	}

	public void setLoanNo(String loanNo) {
		this.loanNo = loanNo;
	}

	public String getLoanName() {
		return loanName;
	}

	public void setLoanName(String loanName) {
		this.loanName = loanName;
	}

	public String getLoanIdCard() {
		return loanIdCard;
	}

	public void setLoanIdCard(String loanIdCard) {
		this.loanIdCard = loanIdCard;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(String paymentDate) {
		this.paymentDate = paymentDate;
	}

	public String getBillTerm() {
		return billTerm;
	}

	public void setBillTerm(String billTerm) {
		this.billTerm = billTerm;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getOperateName() {
		return operateName;
	}

	public void setOperateName(String operateName) {
		this.operateName = operateName;
	}

	public String getOperateTime() {
	    return operateTime;
	}

	public void setOperateTime(String operateTime) {
		this.operateTime = operateTime;
	}

	public String getChargeTime() {
	    return chargeTime;
	}

	public void setChargeTime(String chargeTime) {
		this.chargeTime = chargeTime;
	}

	public String getChargeMessage() {
		return chargeMessage;
	}

	public void setChargeMessage(String chargeMessage) {
		this.chargeMessage = chargeMessage;
	}

	public String getChargeStatus() {
		return chargeStatus;
	}

	public void setChargeStatus(String chargeStatus) {
		this.chargeStatus = chargeStatus;
	}

	public String getPushFlag() {
		return pushFlag;
	}

	public void setPushFlag(String pushFlag) {
		this.pushFlag = pushFlag;
	}

	public String getPushCount() {
		return pushCount;
	}

	public void setPushCount(String pushCount) {
		this.pushCount = pushCount;
	}
	
	public String getPushTime() {
		DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    try {
	        if (null != pushTime) {
	            return sdf.format(pushTime);
	        }
	    } catch (Exception e) {
	        throw new RuntimeException("TimeStamp转换String格式出错", e);
	    }
	    return null;
	}

	public void setPushTime(Timestamp pushTime) {
		this.pushTime = pushTime;
	}

	public String getMianBody() {
		return mianBody;
	}

	public void setMianBody(String mianBody) {
		this.mianBody = mianBody;
	}

	public String getBankKey() {
		return bankKey;
	}

	public void setBankKey(String bankKey) {
		this.bankKey = bankKey;
	}

	public String getCreateTime() {
	    DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    try {
	        if (null != createTime) {
	            return sdf.format(createTime);
	        }
	    } catch (Exception e) {
	        throw new RuntimeException("TimeStamp转换String格式出错", e);
	    }
	    return null;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public String getUpdateTime() {
	    DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    try {
	        if (null != updateTime) {
	            return sdf.format(updateTime);
	        }
	    } catch (Exception e) {
	        throw new RuntimeException("TimeStamp转换String格式出错", e);
	    }
	    return null;
	}

	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}

	public String getUpdateFlag() {
		return updateFlag;
	}

	public void setUpdateFlag(String updateFlag) {
		this.updateFlag = updateFlag;
	}
}
