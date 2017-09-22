package com.zhph.manager.service.impl;

import java.util.List;

import com.zhph.manager.dao.UserMapper;
import com.zhph.manager.model.UserBean;
import com.zhph.manager.service.IUser;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


@Service
public class UserImpl implements IUser {
    
    @Resource
    private UserMapper userMapper;
    
    public int findUserCount() {
        return userMapper.findUserCount();
    }
    
    @Override
    public UserBean login(String loginCode) {
        return userMapper.login(loginCode);
    }
    
    @Override
    public String saveUser(UserBean user) {
        UserBean userBean = userMapper.getUserByLoginCode(user.getLoginCode());
        if (userBean == null) {
            userMapper.saveUser(user);
            return "1";
        } else {
            return "2";
        }
    }
    
    @Override
    public void deleteUser(String userId) {
        userMapper.deleteUser(userId);
    }
    
    @Override
    public UserBean findUserById(String userId) {
        return userMapper.findUserById(userId);
    }
    
    @Override
    public void updateUserInfo(UserBean user) {
        userMapper.updateUserInfo(user);
    }
    
    @Override
    public List<UserBean> getPageUser(int start, int end) {
        return userMapper.getPageUser(start, end);
    }
    
    @Override
    public void updateUserPartInfo(UserBean user) {
        userMapper.updateUserPartInfo(user);
    }
    
    @Override
    public UserBean getUserByLoginCode(String loginCode) {
        return userMapper.getUserByLoginCode(loginCode);
    }
    
}
