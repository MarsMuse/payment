package com.zhph.payment.charge.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName：BatchRecord
 * @Description：批扣记录
 */
public class BatchRecord implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /** OID */
    private String priNumber;
    
    /** 批次号 */
    private String batchNo;
    
    /** 发送人数 */
    private Long sendCount;
    
    /** 查询更新人数(默认为0) */
    private Long returnCount;
    
    /** 状态1、启用，2、失效 */
    private String status;
    
    /** 创建时间 */
    private Date createDate;
    
    /** 创建人 */
    private String createName;
    
    /** 来源平台 */
    private String sourcePlatform;
    
    /** 批次扣款总金额 */
    private Double amount;

    public BatchRecord() {
        
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
    
    public Long getSendCount() {
        return sendCount;
    }
    
    public void setSendCount(Long sendCount) {
        this.sendCount = sendCount;
    }
    
    public Long getReturnCount() {
        return returnCount;
    }
    
    public void setReturnCount(Long returnCount) {
        this.returnCount = returnCount;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
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
    
    public String getSourcePlatform() {
        return sourcePlatform;
    }
    
    public void setSourcePlatform(String sourcePlatform) {
        this.sourcePlatform = sourcePlatform;
    }
    
    public Double getAmount() {
        return amount;
    }
    
    public void setAmount(Double amount) {
        this.amount = amount;
    }

}
