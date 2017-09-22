package com.zhph.payment.charge.entity;



/**
 *
 * Author: Zou Yao
 * Description: (中金批扣返回数据对象)
 * Time: 2017/7/25 15:03
 *
**/
public class ZjBatchChargeBackInfo {
    //交易号
    private  String  itemNo;
    //状态
    private  String  status;
    //扣款时间
    private  String  bankTxTime;
    //扣款结果
    private  String  responseMessage;

    public ZjBatchChargeBackInfo() {
    }

    public String getItemNo() {
        return itemNo;
    }

    public void setItemNo(String itemNo) {
        this.itemNo = itemNo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBankTxTime() {
        return bankTxTime;
    }

    public void setBankTxTime(String bankTxTime) {
        this.bankTxTime = bankTxTime;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }
}
