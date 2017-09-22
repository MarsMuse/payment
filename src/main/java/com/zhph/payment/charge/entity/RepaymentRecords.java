package com.zhph.payment.charge.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName：RepaymentRecords
 * @Description：单扣记录
 * @company:zhph
 * @author Even
 * @date 2016年8月18日 上午11:00:49
 */
public class RepaymentRecords implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /** OID */
    private String priNumber;
    
    /** 合同号 */
    private String loanContractNo;
    
    /** 姓名 */
    private String loanName;
    
    /** 还款类型 */
    private String repayType;
    
    /** 流水号 */
    private String orderSeq;
    
    /** 还款银行 */
    private String repayBank;
    
    /** 还款银行卡号 */
    private String repayBankNo;
    
    /** 还款额 */
    private Double amount;
    
    /** 操作人ID */
    private String operationId;
    
    /** 操作人 */
    private String operationUser;
    
    /** 操作时间 */
    private Date operationTime;
    
    /** 扣款时间 */
    private Date deductionTime;
    
    /** 创建时间 */
    private Date createTime;
    
    /** 状态 */
    private String status; // 20=正在处理,30=代扣成功,40=代扣失败
    
    /** 来源平台 */
    private String sourcePlatform;
    
    /** 响应信息(用于保存扣款失败原因) */
    private String responseMessage;

    public RepaymentRecords() {
        
    }
    
    public String getPriNumber() {
        return priNumber;
    }
    
    public void setPriNumber(String priNumber) {
        this.priNumber = priNumber;
    }
    
    public String getLoanContractNo() {
        return loanContractNo;
    }
    
    public void setLoanContractNo(String loanContractNo) {
        this.loanContractNo = loanContractNo;
    }
    
    public String getLoanName() {
        return loanName;
    }
    
    public void setLoanName(String loanName) {
        this.loanName = loanName;
    }
    
    public String getRepayType() {
        return repayType;
    }
    
    public void setRepayType(String repayType) {
        this.repayType = repayType;
    }
    
    public String getOrderSeq() {
        return orderSeq;
    }
    
    public void setOrderSeq(String orderSeq) {
        this.orderSeq = orderSeq;
    }
    
    public String getRepayBank() {
        return repayBank;
    }
    
    public void setRepayBank(String repayBank) {
        this.repayBank = repayBank;
    }
    
    public String getRepayBankNo() {
        return repayBankNo;
    }
    
    public void setRepayBankNo(String repayBankNo) {
        this.repayBankNo = repayBankNo;
    }
    
    public Double getAmount() {
        return amount;
    }
    
    public void setAmount(Double amount) {
        this.amount = amount;
    }
    
    public String getOperationId() {
        return operationId;
    }
    
    public void setOperationId(String operationId) {
        this.operationId = operationId;
    }
    
    public Date getOperationTime() {
        return operationTime;
    }
    
    public void setOperationTime(Date operationTime) {
        this.operationTime = operationTime;
    }
    
    public Date getDeductionTime() {
        return deductionTime;
    }
    
    public void setDeductionTime(Date deductionTime) {
        this.deductionTime = deductionTime;
    }
    
    public Date getCreateTime() {
        return createTime;
    }
    
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getSourcePlatform() {
        return sourcePlatform;
    }
    
    public void setSourcePlatform(String sourcePlatform) {
        this.sourcePlatform = sourcePlatform;
    }
    
    public String getOperationUser() {
        return operationUser;
    }
    
    public void setOperationUser(String operationUser) {
        this.operationUser = operationUser;
    }
    
    public String getResponseMessage() {
        return responseMessage;
    }
    
    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }
}
