package com.zhph.payment.charge.entity;

import java.io.Serializable;
import java.util.Date;
/**
 * 扣款银行限制表 属性 
 * 缓存
 * @author kang
 * 2017年7月14日上午9:50:22
 */
public class BankNormalLimit implements Serializable {
	private static final long serialVersionUID = 1L;

	private String priNumber;
    
    private String financingChannel;

    private String channelName;
    
    private String bankKey;
    
    private String bankCode;

    private String bankName;
    
    private Double singleAmountLimit;
    
    private Double dayAmountLimit;
    
    private Long dayCountLimit;
    
    private Long deductLevel;

    private String isEnable;
    
    private String createUserId;
    
    private String createTime;
    
    private String updateUserId;
    
    private String updateTime;
    
    private String mainBody;
    
    private Double singleAmountLimitBatch;
    
    private Double dayAmountLimitBatch;



    public String getPriNumber() {
        return priNumber;
    }

    public String getFinancingChannel() {
        return financingChannel;
    }

    public String getChannelName() {
        return channelName;
    }

    public String getBankKey() {
        return bankKey;
    }

    public String getBankCode() {
        return bankCode;
    }

    public String getBankName() {
        return bankName;
    }

    public Double getSingleAmountLimit() {
        return singleAmountLimit;
    }

    public Double getDayAmountLimit() {
        return dayAmountLimit;
    }

    public Long getDayCountLimit() {
        return dayCountLimit;
    }

    public Long getDeductLevel() {
        return deductLevel;
    }

    public String getIsEnable() {
        return isEnable;
    }

    public String getCreateUserId() {
        return createUserId;
    }


    public String getUpdateUserId() {
        return updateUserId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public String getMainBody() {
        return mainBody;
    }

    public Double getSingleAmountLimitBatch() {
        return singleAmountLimitBatch;
    }

    public Double getDayAmountLimitBatch() {
        return dayAmountLimitBatch;
    }

    public void setFinancingChannel(String financingChannel) {
        this.financingChannel = financingChannel;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public void setBankKey(String bankKey) {
        this.bankKey = bankKey;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public void setSingleAmountLimit(Double singleAmountLimit) {
        this.singleAmountLimit = singleAmountLimit;
    }

    public void setDayAmountLimit(Double dayAmountLimit) {
        this.dayAmountLimit = dayAmountLimit;
    }

    public void setDayCountLimit(Long dayCountLimit) {
        this.dayCountLimit = dayCountLimit;
    }

    public void setDeductLevel(Long deductLevel) {
        this.deductLevel = deductLevel;
    }

    public void setIsEnable(String isEnable) {
        this.isEnable = isEnable;
    }

    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }


    public void setMainBody(String mainBody) {
        this.mainBody = mainBody;
    }

    public void setSingleAmountLimitBatch(Double singleAmountLimitBatch) {
        this.singleAmountLimitBatch = singleAmountLimitBatch;
    }

    public void setDayAmountLimitBatch(Double dayAmountLimitBatch) {
        this.dayAmountLimitBatch = dayAmountLimitBatch;
    }

    public void setUpdateUserId(String updateUserId) {
        this.updateUserId = updateUserId == null ? null : updateUserId.trim();
    }

    public void setPriNumber(String priNumber) {
        this.priNumber = priNumber;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
}