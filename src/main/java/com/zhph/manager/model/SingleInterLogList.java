package com.zhph.manager.model;

/**
 * Created by ZHPH on 2017/9/14.
 */
public class SingleInterLogList {

    private String loanContractNo; //'合同编号';
    private String paymentChannel; //'扣款渠道';
    private String terminalId; //'平台id';
    private String requestNo; //'请求编号(1:扣款发送报文;2:扣款接收报文;3:查询发送报文;4:查询接收报文)';
    private String transNo; //'流水号';
    private String createTime; //'创建时间';
    private String chargeType; //'标识：1表示单批，2表示批扣，3，表示单扣查询，4，表示批扣查询';
    private String isLoop; //'标识：0表示正常，2表示批扣循环单扣操作';

    private String interfaceContent; //'报文内容';
    private String priNumber; //'唯一标识';

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

    public String getPaymentChannel() {
        return paymentChannel;
    }

    public void setPaymentChannel(String paymentChannel) {
        this.paymentChannel = paymentChannel;
    }

    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    public String getRequestNo() {
        return requestNo;
    }

    public void setRequestNo(String requestNo) {
        this.requestNo = requestNo;
    }

    public String getTransNo() {
        return transNo;
    }

    public void setTransNo(String transNo) {
        this.transNo = transNo;
    }

    public String getInterfaceContent() {
        return interfaceContent;
    }

    public void setInterfaceContent(String interfaceContent) {
        this.interfaceContent = interfaceContent;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getChargeType() {
        return chargeType;
    }

    public void setChargeType(String chargeType) {
        this.chargeType = chargeType;
    }

    public String getIsLoop() {
        return isLoop;
    }

    public void setIsLoop(String isLoop) {
        this.isLoop = isLoop;
    }
}
