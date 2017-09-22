package com.zhph.payment.charge.entity;



/**
 *
 * Author: Zou Yao
 * Description: (需要做推送任务的平台)
 * Time: 2017/7/21 10:37
 *
**/
public class BatchPushPlatformInfo {

    //平台代码
    private String platformCode;
    //业务平台批扣代码
    private String workBatchNo;

    //证书路径
    private String certificatePath;

    //证书名称
    private String certificateName;

    //回调路径
    private String callBackPath;

    public String getPlatformCode() {
        return platformCode;
    }

    public void setPlatformCode(String platformCode) {
        this.platformCode = platformCode;
    }

    public String getWorkBatchNo() {
        return workBatchNo;
    }

    public void setWorkBatchNo(String workBatchNo) {
        this.workBatchNo = workBatchNo;
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

    public BatchPushPlatformInfo(String platformCode, String workBatchNo, String certificatePath, String certificateName, String callBackPath) {
        this.platformCode = platformCode;
        this.workBatchNo = workBatchNo;
        this.certificatePath = certificatePath;
        this.certificateName = certificateName;
        this.callBackPath = callBackPath;
    }

    public BatchPushPlatformInfo() {
    }
}
