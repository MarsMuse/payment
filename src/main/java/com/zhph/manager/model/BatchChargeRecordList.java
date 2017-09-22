package com.zhph.manager.model;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * 批扣记录列表显示
 * Created by lidongkui on 2017/8/9.
 */
public class BatchChargeRecordList{

	private String rn;

    private String platformName;

    private String batchNo;

    private int sendInfoCount;

    private int replyInfoCount;
    
    private String pushCount;
    
    private Timestamp createTime;

    private Timestamp updateTime;

    private String endFlag;
    
    private String channelName;
    
    private String sendFlag;
    
    private String mainBody;

	public String getRn() {
		return rn;
	}

	public void setRn(String rn) {
		this.rn = rn;
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
	
	public String getPushCount() {
		return pushCount;
	}

	public void setPushCount(String pushCount) {
		this.pushCount = pushCount;
	}

	public String getCreateTime() {
	    DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    try {
	        if (null != createTime) {
	            return sdf.format(createTime);
	        }
	    } catch (Exception e) {
	        throw new RuntimeException("TimeStamp转换String格式出错", e);
	    }
	    return null;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public String getUpdateTime() {
		DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    try {
	        if (null != updateTime) {
	            return sdf.format(updateTime);
	        }
	    } catch (Exception e) {
	        throw new RuntimeException("TimeStamp转换String格式出错", e);
	    }
	    return null;
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
    
}
