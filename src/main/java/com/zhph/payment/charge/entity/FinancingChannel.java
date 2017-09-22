package com.zhph.payment.charge.entity;

import java.io.Serializable;
import java.util.Date;



public class FinancingChannel implements Serializable {
	
	private static final long serialVersionUID = 4614131735502998520L;

	private String id;
	
    private String channelNumber; // 类型编号
	
	private String channelName;//类型名称
	
	private String isSupportImport;//是否支持二级导入，0否，1是
	
	private String updateUserId;//修改人ID
	
	private String updateUserName;//修改人姓名
	
	private Date updateTime;//修改时间
	
	private Date createTime;//创建时间
	
    private String isUsing; // 启用状态(0:禁用;1:启用)

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getChannelNumber() {
		return channelNumber;
	}

	public void setChannelNumber(String channelNumber) {
		this.channelNumber = channelNumber;
	}

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public String getIsSupportImport() {
		return isSupportImport;
	}

	public void setIsSupportImport(String isSupportImport) {
		this.isSupportImport = isSupportImport;
	}

	public String getUpdateUserId() {
		return updateUserId;
	}

	public void setUpdateUserId(String updateUserId) {
		this.updateUserId = updateUserId;
	}

	public String getUpdateUserName() {
		return updateUserName;
	}

	public void setUpdateUserName(String updateUserName) {
		this.updateUserName = updateUserName;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getIsUsing() {
		return isUsing;
	}

	public void setIsUsing(String isUsing) {
		this.isUsing = isUsing;
	}
    
    
}
