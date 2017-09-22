package com.zhph.manager.model;

import java.io.Serializable;
import java.util.Date;

public class RoleBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String roleId;//角色编号
	private String roleName;//角色名称
	private Date createTime;//创建时间
	private int status;//状态 0 停用 1启用
	
	
	public String getRoleId() {
		return roleId;
	}
	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	
	
	
}
