package com.zhph.manager.model;

import java.io.Serializable;
import java.sql.Date;

public class UserGroupBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String groupId;//用户组编号
	private String groupName;//用户组名称
	private Date createTime;//创建时间
	private String status;// 状态 0 停用 1启用
	private String roleIds;// 组id对应角色组合

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getRoleIds() {
		return roleIds;
	}

	public void setRoleIds(String roleIds) {
		this.roleIds = roleIds;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
