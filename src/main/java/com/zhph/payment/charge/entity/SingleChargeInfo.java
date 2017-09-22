package com.zhph.payment.charge.entity;



/**
 *
 * Description: (推送单扣扣款信息实体)
 * Time: 2017/7/21 11:21
 *
**/
public class SingleChargeInfo {

    //平台批扣对象
    private String platformCode;
    //扣款号
    private String chargeNo;
    //金额拆分 分组号 如果没有表示未拆分
    private String groupChargeNo;
    //合同号
    private String loanNo;
    //扣款时间
    private String chargeTime;
    //扣款返回信息
    private String chargeMessage;
    //扣款状态
    private String chargeStatus;
    //实际扣款金额
    private String realAmount;
    //扣款平台
    private String chargeChannelCode;
    //操作人
    private String operateName;

    public String getOperateName() {
        return operateName;
    }
    public void setOperateName(String operateName) {
        this.operateName = operateName;
    }
    public String getGroupChargeNo() {
        return groupChargeNo;
    }

    public void setGroupChargeNo(String groupChargeNo) {
        this.groupChargeNo = groupChargeNo;
    }

    public String getChargeChannelCode() {
        return chargeChannelCode;
    }

    public void setChargeChannelCode(String chargeChannelCode) {
        this.chargeChannelCode = chargeChannelCode;
    }

    public String getPlatformCode() {
        return platformCode;
    }

    public void setPlatformCode(String platformCode) {
        this.platformCode = platformCode;
    }

    public String getRealAmount() {
        return realAmount;
    }

    public void setRealAmount(String realAmount) {
        this.realAmount = realAmount;
    }

    public String getLoanNo() {
        return loanNo;
    }

    public void setLoanNo(String loanNo) {
        this.loanNo = loanNo;
    }

    public String getChargeTime() {
        return chargeTime;
    }

    public void setChargeTime(String chargeTime) {
        this.chargeTime = chargeTime;
    }

    public String getChargeMessage() {
        return chargeMessage;
    }

    public void setChargeMessage(String chargeMessage) {
        this.chargeMessage = chargeMessage;
    }

    public String getChargeStatus() {
        return chargeStatus;
    }

    public void setChargeStatus(String chargeStatus) {
        this.chargeStatus = chargeStatus;
    }

    public String getChargeNo() {
        return chargeNo;
    }

    public void setChargeNo(String chargeNo) {
        this.chargeNo = chargeNo;
    }

    public SingleChargeInfo() {
    }

}
