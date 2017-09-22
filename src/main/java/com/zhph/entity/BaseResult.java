package com.zhph.entity;

/**
 * 返回实体基本类
 */
public class BaseResult {

    private String code; // 响应码

    private String msg; // 响应信息

    private Double amount; // 实际扣款金额

    private String interfaceContent; // 报文内容

    private String orderSeq; // 交易订单号

    private String tradeTime; // 交易时间

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getInterfaceContent() {
        return interfaceContent;
    }

    public void setInterfaceContent(String interfaceContent) {
        this.interfaceContent = interfaceContent;
    }

    public String getOrderSeq() {
        return orderSeq;
    }

    public void setOrderSeq(String orderSeq) {
        this.orderSeq = orderSeq;
    }

    public String getTradeTime() {
        return tradeTime;
    }

    public void setTradeTime(String tradeTime) {
        this.tradeTime = tradeTime;
    }
}
