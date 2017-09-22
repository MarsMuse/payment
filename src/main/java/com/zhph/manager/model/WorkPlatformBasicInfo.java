package com.zhph.manager.model;


import java.util.Date;

/**
 *
 * Author: Zou Yao
 * Description: (业务平台基本信息)
 * Time: 2017/8/3 16:11
 *
**/
public class WorkPlatformBasicInfo {

    private String id;
    private String platformCode;
    private String platformName;
    private String certificatePath;
    private String certificateName;
    private String createBy;
    private Date createTime;
    private String updateBy;
    private Date updateTime;
    private String locked;
    private String signCallBackPath;
    private String batchCallBackPath;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPlatformCode() {
        return platformCode;
    }

    public void setPlatformCode(String platformCode) {
        this.platformCode = platformCode;
    }

    public String getPlatformName() {
        return platformName;
    }

    public void setPlatformName(String platformName) {
        this.platformName = platformName;
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

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getLocked() {
        return locked;
    }

    public void setLocked(String locked) {
        this.locked = locked;
    }

    public String getSignCallBackPath() {
        return signCallBackPath;
    }

    public void setSignCallBackPath(String signCallBackPath) {
        this.signCallBackPath = signCallBackPath;
    }

    public String getBatchCallBackPath() {
        return batchCallBackPath;
    }

    public void setBatchCallBackPath(String batchCallBackPath) {
        this.batchCallBackPath = batchCallBackPath;
    }
}
