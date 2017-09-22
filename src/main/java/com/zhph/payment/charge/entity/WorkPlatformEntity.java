package com.zhph.payment.charge.entity;


import java.util.Date;

/**
 *
 * @Author: Zou Yao
 * @Description: (业务平台信息)
 * @Time: 2017/7/17 17:58
 *
**/
public class WorkPlatformEntity {
    //ID
    private String id;

    //平台编码
    private String platformCode;

    //平台名称
    private String platformName;

    //证书路径
    private String certificatePath;

    //证书名称
    private String certificateName;

    //创建人
    private String createBy;

    //创建时间
    private Date createTime;

    //更新人
    private String updateBy;

    //更新时间
    private Date updateTime;

    //是否锁定
    private String locked;

    //是否有效
    private String flag;

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

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    @Override
    public String toString() {
        return "WorkPlatformEntity{" +
                "id='" + id + '\'' +
                ", platformCode='" + platformCode + '\'' +
                ", platformName='" + platformName + '\'' +
                ", certificatePath='" + certificatePath + '\'' +
                ", certificateName='" + certificateName + '\'' +
                ", createBy='" + createBy + '\'' +
                ", createTime=" + createTime +
                ", updateBy='" + updateBy + '\'' +
                ", updateTime=" + updateTime +
                ", locked='" + locked + '\'' +
                ", flag='" + flag + '\'' +
                '}';
    }
}
