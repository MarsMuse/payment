package com.zhph.entity;

/**
 * 单扣结果
 */
public class SinglePaymentResult {
    private String transNo; //交易流水
    private String paymentCode; //扣款结果(1:成功;2:扣款�?;3扣款失败)
    private String paymentDesc; //扣款描述
    private double paymentAmount; //扣款金额
    private String paymentTime; //扣款时间
    private String paymentChannel; //扣款渠道
    @Override
	public String toString() {
		return "SinglePaymentResult [transNo=" + transNo + ", paymentCode="
				+ paymentCode + ", paymentDesc=" + paymentDesc
				+ ", paymentAmount=" + paymentAmount + ", paymentTime="
				+ paymentTime + ", paymentChannel=" + paymentChannel + "]";
	}

	public String getPaymentChannel() {
        return paymentChannel;
    }

    public void setPaymentChannel(String paymentChannel) {
        this.paymentChannel = paymentChannel;
    }

    public String getTransNo() {
        return transNo;
    }

    public void setTransNo(String transNo) {
        this.transNo = transNo;
    }

    public String getPaymentCode() {
        return paymentCode;
    }

    public void setPaymentCode(String paymentCode) {
        this.paymentCode = paymentCode;
    }

    public String getPaymentDesc() {
        return paymentDesc;
    }

    public void setPaymentDesc(String paymentDesc) {
        this.paymentDesc = paymentDesc;
    }

    public double getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(double paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public String getPaymentTime() {
        return paymentTime;
    }

    public void setPaymentTime(String paymentTime) {
        this.paymentTime = paymentTime;
    }
}
