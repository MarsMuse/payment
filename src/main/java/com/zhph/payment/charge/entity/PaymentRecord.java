package com.zhph.payment.charge.entity;

import java.util.Date;

/**
 * 单扣表数据库-对应字段-
 */
public class PaymentRecord {
    private String priNumber;
    private String terminalId;
    private String name;
    private String bankCard;
    private String transNo;
    private String batchNo;
    private String thirdTransNo;
    private String thirdBatchNo;
    private String transType;
    private String loanContractNo;
    private String paymentChannel;
    private String bankCode;
    private Double transAmount;
    private String paymentCode;
    private String paymentDesc;
    private Date paymentTime;
    private Date createTime;
    private Date updateTime;
    
    public String getPriNumber() {
        return priNumber;
    }

    
    public void setPriNumber(String priNumber) {
        this.priNumber = priNumber == null ? null : priNumber.trim();
    }

    
    public String getTerminalId() {
        return terminalId;
    }

    
    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId == null ? null : terminalId.trim();
    }

    
    public String getName() {
        return name;
    }

    
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    
    public String getBankCard() {
        return bankCard;
    }

    
    public void setBankCard(String bankCard) {
        this.bankCard = bankCard == null ? null : bankCard.trim();
    }

    
    public String getTransNo() {
        return transNo;
    }

    
    public void setTransNo(String transNo) {
        this.transNo = transNo == null ? null : transNo.trim();
    }

    
    public String getBatchNo() {
        return batchNo;
    }

    
    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo == null ? null : batchNo.trim();
    }

    
    public String getThirdTransNo() {
        return thirdTransNo;
    }

    
    public void setThirdTransNo(String thirdTransNo) {
        this.thirdTransNo = thirdTransNo == null ? null : thirdTransNo.trim();
    }

    
    public String getThirdBatchNo() {
        return thirdBatchNo;
    }

    
    public void setThirdBatchNo(String thirdBatchNo) {
        this.thirdBatchNo = thirdBatchNo == null ? null : thirdBatchNo.trim();
    }

    
    public String getTransType() {
        return transType;
    }

    
    public void setTransType(String transType) {
        this.transType = transType == null ? null : transType.trim();
    }

    
    public String getLoanContractNo() {
        return loanContractNo;
    }

    
    public void setLoanContractNo(String loanContractNo) {
        this.loanContractNo = loanContractNo == null ? null : loanContractNo.trim();
    }

    
    public String getPaymentChannel() {
        return paymentChannel;
    }

    
    public void setPaymentChannel(String paymentChannel) {
        this.paymentChannel = paymentChannel == null ? null : paymentChannel.trim();
    }

    
    public String getBankCode() {
        return bankCode;
    }

    
    public void setBankCode(String bankCode) {
        this.bankCode = bankCode == null ? null : bankCode.trim();
    }

    public Double getTransAmount() {
        return transAmount;
    }

    public void setTransAmount(Double transAmount) {
        this.transAmount = transAmount;
    }

    public String getPaymentCode() {
        return paymentCode;
    }

    
    public void setPaymentCode(String paymentCode) {
        this.paymentCode = paymentCode == null ? null : paymentCode.trim();
    }

    
    public String getPaymentDesc() {
        return paymentDesc;
    }

    
    public void setPaymentDesc(String paymentDesc) {
        this.paymentDesc = paymentDesc == null ? null : paymentDesc.trim();
    }

    
    public Date getPaymentTime() {
        return paymentTime;
    }

    
    public void setPaymentTime(Date paymentTime) {
        this.paymentTime = paymentTime;
    }

    
    public Date getCreateTime() {
        return createTime;
    }

    
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    
    public Date getUpdateTime() {
        return updateTime;
    }

    
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}