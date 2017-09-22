package com.zhph.payment.charge.entity;

import java.io.Serializable;
import java.sql.Timestamp;

public class BatchChargeBasicInfo implements Serializable{

    private static final long serialVersionUID = 1L;
    private String id ;

    private String platformCode;
    
    private String platformName;

    private String batchNo;

    private int sendInfoCount;

    private int replyInfoCount;

    private Timestamp createTime;

    private Timestamp updateTime;

    private String endFlag;
    //业务平台批扣编码
    private String workBatchNo;
    //扣款渠道编号
    private String channelNo;
    
    private String channelName;
    
    private String sendFlag;

    private String mainBody;

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

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
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

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public Timestamp getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}

	public String getEndFlag() {
		return endFlag;
	}

	public void setEndFlag(String endFlag) {
		this.endFlag = endFlag;
	}

	public String getWorkBatchNo() {
		return workBatchNo;
	}

	public void setWorkBatchNo(String workBatchNo) {
		this.workBatchNo = workBatchNo;
	}

	public String getChannelNo() {
		return channelNo;
	}

	public void setChannelNo(String channelNo) {
		this.channelNo = channelNo;
	}

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public String getSendFlag() {
		return sendFlag;
	}

	public void setSendFlag(String sendFlag) {
		this.sendFlag = sendFlag;
	}

	public String getMainBody() {
		return mainBody;
	}

	public void setMainBody(String mainBody) {
		this.mainBody = mainBody;
	}

	public BatchChargeBasicInfo() {
    }

	public BatchChargeBasicInfo(String id, String platformCode, String platformName, String batchNo, int sendInfoCount, int replyInfoCount, Timestamp createTime, Timestamp updateTime, String endFlag, String workBatchNo, String channelNo, String channelName, String sendFlag, String mainBody) {
		this.id = id;
		this.platformCode = platformCode;
		this.platformName = platformName;
		this.batchNo = batchNo;
		this.sendInfoCount = sendInfoCount;
		this.replyInfoCount = replyInfoCount;
		this.createTime = createTime;
		this.updateTime = updateTime;
		this.endFlag = endFlag;
		this.workBatchNo = workBatchNo;
		this.channelNo = channelNo;
		this.channelName = channelName;
		this.sendFlag = sendFlag;
		this.mainBody = mainBody;
	}

	public BatchChargeBasicInfo(String id, String platformCode, String batchNo, int sendInfoCount, int replyInfoCount, Timestamp createTime, Timestamp updateTime, String endFlag) {
        this.id = id;
        this.platformCode = platformCode;
        this.batchNo = batchNo;
        this.sendInfoCount = sendInfoCount;
        this.replyInfoCount = replyInfoCount;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.endFlag = endFlag;
    }

    public BatchChargeBasicInfo(String platformCode, String batchNo, int sendInfoCount) {
        this.platformCode = platformCode;
        this.batchNo = batchNo;
        this.sendInfoCount = sendInfoCount;
    }

    public BatchChargeBasicInfo(String platformCode, int sendInfoCount) {
        this.platformCode = platformCode;
        this.sendInfoCount = sendInfoCount;
    }

    public BatchChargeBasicInfo(String platformCode, int sendInfoCount, String workBatchNo) {
        this.platformCode = platformCode;
        this.sendInfoCount = sendInfoCount;
        this.workBatchNo = workBatchNo;
    }

    public BatchChargeBasicInfo(String platformCode, int sendInfoCount, String workBatchNo, String channelNo) {
        this.platformCode = platformCode;
        this.sendInfoCount = sendInfoCount;
        this.workBatchNo = workBatchNo;
        this.channelNo = channelNo;
    }

    public BatchChargeBasicInfo(String platformCode, String workBatchNo, String channelNo) {
        this.platformCode = platformCode;
        this.workBatchNo = workBatchNo;
        this.channelNo = channelNo;
    }
}
