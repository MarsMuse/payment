package com.zhph.payment.charge.entity;

import java.util.Date;

/**
 * 单扣基础信息
 */
public class SingleChargeBasicInfo {


    private String id ;

    private String platformCode;

    private String singleNo;

    private int sendInfoCount;

    private int replyInfoCount;

    private Date createTime;

    private Date updateTime;

    private String endFlag;
    //业务平台批扣编码
    private String worksingleNo;
    //扣款渠道编号
    private String channelNo;




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
 
    public String getSingleNo() {
		return singleNo;
	}

	public void setSingleNo(String singleNo) {
		this.singleNo = singleNo;
	}

	public int getSendInfoCount() {
        return sendInfoCount;
    }

    public void setSendInfoCount(int sendInfoCount) {
        this.sendInfoCount = sendInfoCount;
    }

    public int getReplyInfoCount() {
        return replyInfoCount;
    }

    public void setReplyInfoCount(int replyInfoCount) {
        this.replyInfoCount = replyInfoCount;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getEndFlag() {
        return endFlag;
    }

    public void setEndFlag(String endFlag) {
        this.endFlag = endFlag;
    }

    public String getWorksingleNo() {
        return worksingleNo;
    }

    public void setWorksingleNo(String worksingleNo) {
        this.worksingleNo = worksingleNo;
    }

    public String getChannelNo() {
        return channelNo;
    }

    public void setChannelNo(String channelNo) {
        this.channelNo = channelNo;
    }

    public SingleChargeBasicInfo() {
    }

    public SingleChargeBasicInfo(String id, String platformCode, String singleNo, int sendInfoCount, int replyInfoCount, Date createTime, Date updateTime, String endFlag) {
        this.id = id;
        this.platformCode = platformCode;
        this.singleNo = singleNo;
        this.sendInfoCount = sendInfoCount;
        this.replyInfoCount = replyInfoCount;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.endFlag = endFlag;
    }

    public SingleChargeBasicInfo(String platformCode, String singleNo, int sendInfoCount) {
        this.platformCode = platformCode;
        this.singleNo = singleNo;
        this.sendInfoCount = sendInfoCount;
    }

    public SingleChargeBasicInfo(String platformCode, int sendInfoCount) {
        this.platformCode = platformCode;
        this.sendInfoCount = sendInfoCount;
    }

    public SingleChargeBasicInfo(String platformCode, int sendInfoCount, String worksingleNo) {
        this.platformCode = platformCode;
        this.sendInfoCount = sendInfoCount;
        this.worksingleNo = worksingleNo;
    }

    public SingleChargeBasicInfo(String platformCode, int sendInfoCount, String worksingleNo, String channelNo) {
        this.platformCode = platformCode;
        this.sendInfoCount = sendInfoCount;
        this.worksingleNo = worksingleNo;
        this.channelNo = channelNo;
    }

    public SingleChargeBasicInfo(String platformCode, String worksingleNo, String channelNo) {
        this.platformCode = platformCode;
        this.worksingleNo = worksingleNo;
        this.channelNo = channelNo;
    }
}
