package com.zhph.payment.charge.entity;



/**
 *
 * Author: Zou Yao
 * Description: (公共的第三方扣款返回结果)
 * Time: 2017/7/25 13:38
 *
**/
public class CommonChargeInfo {

    private String chargeNo; // 交易号

    private String chargeStatus;//扣款状态

    private String chargeMessage;//扣款返回信息

    private String chargeTime;//扣款时间

    private String batchNo;//批次号（批扣使用）

    private String chargeAmount;//扣款金额(单扣使用）

    public String getChargeNo() {
        return chargeNo;
    }

    public void setChargeNo(String chargeNo) {
        this.chargeNo = chargeNo;
    }

    public String getChargeStatus() {
        return chargeStatus;
    }

    public void setChargeStatus(String chargeStatus) {
        this.chargeStatus = chargeStatus;
    }

    public String getChargeMessage() {
        return chargeMessage;
    }

    public void setChargeMessage(String chargeMessage) {
        this.chargeMessage = chargeMessage;
    }

    public String getChargeTime() {
        return chargeTime;
    }

    public void setChargeTime(String chargeTime) {
        this.chargeTime = chargeTime;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public String getChargeAmount() {
        return chargeAmount;
    }

    public void setChargeAmount(String chargeAmount) {
        this.chargeAmount = chargeAmount;
    }

    public CommonChargeInfo() {
    }

    public CommonChargeInfo(String chargeNo, String chargeStatus, String chargeMessage, String chargeTime, String batchNo) {
        this.chargeNo = chargeNo;
        this.chargeStatus = chargeStatus;
        this.chargeMessage = chargeMessage;
        this.chargeTime = chargeTime;
        this.batchNo = batchNo;
    }

    public CommonChargeInfo(String chargeNo, String chargeStatus, String chargeMessage, String chargeTime) {
        this.chargeNo = chargeNo;
        this.chargeStatus = chargeStatus;
        this.chargeMessage = chargeMessage;
        this.chargeTime = chargeTime;
    }


}
