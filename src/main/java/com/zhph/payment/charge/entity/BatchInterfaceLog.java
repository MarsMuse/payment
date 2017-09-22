package com.zhph.payment.charge.entity;

import java.util.Date;
/**
 * 批扣日志记录
 * @author likang
 *
 */
public class BatchInterfaceLog {
    
    private String priNumber;

    
    private String batchNo;

    
    private String requestNo;

    
    private String terminalId;

    
    private String paymentChannel;

    
    private Date createTime;

    
    private String interfaceContent;

    
    public String getPriNumber() {
        return priNumber;
    }

    
    public void setPriNumber(String priNumber) {
        this.priNumber = priNumber == null ? null : priNumber.trim();
    }

    
    public String getBatchNo() {
        return batchNo;
    }

    
    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo == null ? null : batchNo.trim();
    }

    
    public String getRequestNo() {
        return requestNo;
    }

    
    public void setRequestNo(String requestNo) {
        this.requestNo = requestNo == null ? null : requestNo.trim();
    }

    
    public String getTerminalId() {
        return terminalId;
    }

    
    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId == null ? null : terminalId.trim();
    }

    
    public String getPaymentChannel() {
        return paymentChannel;
    }

    
    public void setPaymentChannel(String paymentChannel) {
        this.paymentChannel = paymentChannel == null ? null : paymentChannel.trim();
    }

    
    public Date getCreateTime() {
        return createTime;
    }

    
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    
    public String getInterfaceContent() {
        return interfaceContent;
    }

    
    public void setInterfaceContent(String interfaceContent) {
        this.interfaceContent = interfaceContent == null ? null : interfaceContent.trim();
    }
}