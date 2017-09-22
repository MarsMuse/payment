package com.zhph.manager.model;

import java.io.Serializable;
import java.sql.Timestamp;

public class UserBean implements Serializable{
    
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    private String userId;// 用户编号
    
    private String userName;// 用户名称
    
    private String userPass;// 用户密码
    
    private String mobile;// 手机号
    
    private String eMail;// 用户邮箱
    
    private int errorTimes;// 密码错误次数
    
    private String isLock;// 是否锁定用户
    
    private Timestamp createTime;// 创建时间

    private Timestamp lastTime;// 最后一次登录时间

    private String status;// 状态

    private String loginCode;// 登录编码

    private String userCode;// 用户编码
    
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

	public Timestamp getLastTime() {
		return lastTime;
	}

	public void setLastTime(Timestamp lastTime) {
		this.lastTime = lastTime;
	}

	public String getUserCode() {
        return userCode;
    }
    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }
    
    public String getLoginCode() {
        return loginCode;
    }
    public void setLoginCode(String loginCode) {
        this.loginCode = loginCode;
    }
    public String getUserPass() {
        return userPass;
    }
    public void setUserPass(String userPass) {
        this.userPass = userPass;
    }
    public String getMobile() {
        return mobile;
    }
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
    public String geteMail() {
        return eMail;
    }
    public void seteMail(String eMail) {
        this.eMail = eMail;
    }
    public int getErrorTimes() {
        return errorTimes;
    }
    public void setErrorTimes(int errorTimes) {
        this.errorTimes = errorTimes;
    }
    public String getIsLock() {
        return isLock;
    }
    public void setIsLock(String isLock) {
        this.isLock = isLock;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public static long getSerialversionuid() {
        return serialVersionUID;
    }
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
}
