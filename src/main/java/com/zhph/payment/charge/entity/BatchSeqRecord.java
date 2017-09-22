package com.zhph.payment.charge.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName：BatchSeqRecord
 * @Description：批扣流水记录
 */
public class BatchSeqRecord implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /** OID */
    private String priNumber;
    
    /** 批次号 */
    private String batchNo;
    
    /** 合同号 */
    private String loanContractNo;
    
    /** 流水号 */
    private String seq;
    
    /** 是否导出 */
    private String isExport;
    
    /** 创建时间 */
    private Date createDate;
    
    /** 创建人 */
    private String createName;
    
    /** 姓名 */
    private String loanName;
    
    /** 还款额 */
    private Double amount;
    
    /** 还款银行 */
    private String repayBank;
    
    /** 还款银行卡号 */
    private String repayBankNo;
    
    /** 状态 */
    private String status; // 20=正在处理,30=代扣成功,40=代扣失败
    
    /** 扣款时间 */
    private Date deductionTime;
    
    /** 响应信息(用于保存扣款失败原因) */
    private String responseMessage;

    public BatchSeqRecord() {
        
    }
    
    public String getPriNumber() {
        return priNumber;
    }
    
    public void setPriNumber(String priNumber) {
        this.priNumber = priNumber;
    }
    
    public String getBatchNo() {
        return batchNo;
    }
    
    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }
    
    public String getLoanContractNo() {
        return loanContractNo;
    }
    
    public void setLoanContractNo(String loanContractNo) {
        this.loanContractNo = loanContractNo;
    }
    
    public String getSeq() {
        return seq;
    }
    
    public void setSeq(String seq) {
        this.seq = seq;
    }
    
    public String getIsExport() {
        return isExport;
    }
    
    public void setIsExport(String isExport) {
        this.isExport = isExport;
    }
    
    public Date getCreateDate() {
        return createDate;
    }
    
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    
    public String getCreateName() {
        return createName;
    }
    
    public void setCreateName(String createName) {
        this.createName = createName;
    }
    
    public String getLoanName() {
        return loanName;
    }
    
    public void setLoanName(String loanName) {
        this.loanName = loanName;
    }
    
    public Double getAmount() {
        return amount;
    }
    
    public void setAmount(Double amount) {
        this.amount = amount;
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
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public Date getDeductionTime() {
        return deductionTime;
    }
    
    public void setDeductionTime(Date deductionTime) {
        this.deductionTime = deductionTime;
    }
    
    public String getResponseMessage() {
        return responseMessage;
    }
    
    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }
}
