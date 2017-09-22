package com.zhph.manager.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

public class RoleResourceBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String roleId;//角色编号
	private String resourceId;//资源编号
	private Date createTime;//创建时间
	public String getRoleId() {
		return roleId;
	}
	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}
	public String getResourceId() {
		return resourceId;
	}
	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	
}
