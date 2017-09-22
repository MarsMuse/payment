package com.zhph.manager.service;

import com.zhph.manager.model.UserBean;
import org.springframework.stereotype.Repository;

import java.util.List;


public interface IUser {
	
	int findUserCount();
	
	UserBean login(String loginCode);
	
    String saveUser(UserBean user);
	
	void deleteUser(String userId);
	
	UserBean findUserById(String userId);
	
	void updateUserInfo(UserBean user);
	
	List<UserBean> getPageUser(int start, int end);
	
	void updateUserPartInfo(UserBean user);
	
	UserBean getUserByLoginCode(String loginCode);
}
