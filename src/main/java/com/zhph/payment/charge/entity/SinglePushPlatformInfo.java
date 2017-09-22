package com.zhph.payment.charge.entity;

/**
 * Created by zhph on 2017/8/11.
 */
public class SinglePushPlatformInfo implements java.io.Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//平台代码
    private String platformCode;

    //交易流水号Id
    private String singleNo;

    //扣款平台
    private String chargeChannelCode;

    //证书路径
    private String certificatePath;

    //证书名称
    private String certificateName;

    //回调路径
    private String callBackPath;



    public SinglePushPlatformInfo(String platformCode, String single_no, String chargeChannelCode, String certificatePath, String certificateName, String callBackPath, String realAmount) {
        this.platformCode = platformCode;
        this.singleNo = single_no;
        this.chargeChannelCode = chargeChannelCode;
        this.certificatePath = certificatePath;
        this.certificateName = certificateName;
        this.callBackPath = callBackPath;
    }

    public String getPlatformCode() {
        return platformCode;
    }

    public void setPlatformCode(String platformCode) {
        this.platformCode = platformCode;
    }

    public String getSingleNo() {
        return singleNo;
    }

    public void setSingle_no(String single_no) {
        this.singleNo = single_no;
    }

    public String getChargeChannelCode() {
        return chargeChannelCode;
    }

    public void setChargeChannelCode(String chargeChannelCode) {
        this.chargeChannelCode = chargeChannelCode;
    }

    public String getCertificatePath() {
        return certificatePath;
    }

    public void setCertificatePath(String certificatePath) {
        this.certificatePath = certificatePath;
    }

    public String getCertificateName() {
        return certificateName;
    }

    public void setCertificateName(String certificateName) {
        this.certificateName = certificateName;
    }

    public String getCallBackPath() {
        return callBackPath;
    }

    public void setCallBackPath(String callBackPath) {
        this.callBackPath = callBackPath;
    }


}
