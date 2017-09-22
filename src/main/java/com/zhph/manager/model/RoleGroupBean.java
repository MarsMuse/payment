package com.zhph.manager.model;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;

public class RoleGroupBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String roleId;//角色编号
	private String groupId;//用户组编号
	private Date createTime;//创建时间

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
