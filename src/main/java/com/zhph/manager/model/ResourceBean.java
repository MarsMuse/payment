package com.zhph.manager.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

public class ResourceBean implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String resourceId;//资源编号
	private String resourceName;//资源名称
	private String resourceUrl;//资源URL
	private String parentId;//父资源URL
	private Date createTime;//创建时间
	private int status;//状态 0 停用 1启用
	private int sort;//排序
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getResourceId() {
		return resourceId;
	}
	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}
	public String getResourceName() {
		return resourceName;
	}
	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}
	public String getResourceUrl() {
		return resourceUrl;
	}
	public void setResourceUrl(String resourceUrl) {
		this.resourceUrl = resourceUrl;
	}
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public int getSort() {
		return sort;
	}
	public void setSort(int sort) {
		this.sort = sort;
	}
	
	
}
