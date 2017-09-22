package com.zhph.entity;

import java.util.List;

/**
 * 批扣实体
 */
public class BatchPaymentResult {
    private String batchNo; //批次号
    private String paymentChannel; //扣款渠道
    private List<SinglePaymentResult> paymentResults; //批扣结果

    public String getPaymentChannel() {
        return paymentChannel;
    }

    public void setPaymentChannel(String paymentChannel) {
        this.paymentChannel = paymentChannel;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public List<SinglePaymentResult> getPaymentResults() {
        return paymentResults;
    }

    public void setPaymentResults(List<SinglePaymentResult> paymentResults) {
        this.paymentResults = paymentResults;
    }
}
