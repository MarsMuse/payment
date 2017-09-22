package com.zhph.manager.dao;

import java.util.List;

import com.zhph.manager.model.UserBean;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMapper {
	
	int findUserCount();
	
    UserBean login(@Param("loginCode") String loginCode);
	
	void saveUser(UserBean user);
	
	void deleteUser(String userId);
	
	UserBean findUserById(String userId);
	
	void updateUserInfo(UserBean user);
	
	List<UserBean> getPageUser(@Param("start") int start, @Param("end") int end);
	
	void updateUserPartInfo(UserBean user);
	
	UserBean getUserByLoginCode(String loginCode);
}
