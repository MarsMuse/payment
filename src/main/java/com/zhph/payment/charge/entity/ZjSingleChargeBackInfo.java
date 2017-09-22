package com.zhph.payment.charge.entity;



/**
 *
 * Author: Zou Yao
 * Description: (描述)
 * Time: 2017/7/25 18:01
 *
**/ 
public class ZjSingleChargeBackInfo {

    private  String bankTxTime;//扣款时间
    
    private  String status;//扣款状态
    
    private  String responseMessage;//扣款返回信息
    
    private  String txSN;//交易号

    public ZjSingleChargeBackInfo() {
    }

    public String getBankTxTime() {
        return bankTxTime;
    }

    public void setBankTxTime(String bankTxTime) {
        this.bankTxTime = bankTxTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public String getTxSN() {
        return txSN;
    }

    public void setTxSN(String txSN) {
        this.txSN = txSN;
    }
}
