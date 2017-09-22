package com.zhph.base.entity;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;



/**
 *
 * Author: Zou Yao
 * Description: (用户实体类)
 * Time: 2017/8/3 9:32
 *
**/
public class User implements Serializable{
    public static final String SESSION_USER_ATTR = "paySysCurrentUser";

	private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(User.class);
    //用户名
	private String id;
	//登录名
	private String account;
	//姓名
    private String name;
    //密码
	private String password;
	//盐值
    private String salt;
    //性别
    private Integer gender;
    //邮箱
    private String email;
    //电话
    private String phone;
    //是否锁定
    private Integer locked;


    public static User getCurrentUser(){
        Subject sbj = SecurityUtils.getSubject();
        if(sbj == null){
            return null;
        }
        if(!sbj.isAuthenticated()){
            return null;
        }
        Session session = sbj.getSession();
        User originalUser = (User)session.getAttribute(SESSION_USER_ATTR);
        User currentUser = new User();
        try {
            BeanUtils.copyProperties(currentUser, originalUser);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return null;
        }
        currentUser.setPassword("");
        currentUser.setSalt("");
        return currentUser;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getLocked() {
        return locked;
    }

    public void setLocked(Integer locked) {
        this.locked = locked;
    }
}
