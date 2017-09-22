package com.zhph.manager.service;

import com.zhph.manager.model.Menu;
import com.zhph.manager.model.UserBean;

import java.util.List;
import java.util.Map;

public interface ManageService {


    /**
     *
     * Author: zou yao
     * Description: {获取到用户菜单}
     * Date: 2017/8/3 15:27
     *
    **/
    List<Menu> getUserMenuList(UserBean user);


    /**
     *
     * Author: zou yao
     * Description: {获取到用户信息列表}
     * Date: 2017/8/4 15:00
     *
    **/
    Map<String , Object> getListForUserInfo(Map<String , Object> parameter);


    /**
     *
     * Author: zou yao
     * Description: {验证用户名是否存在}
     * Date: 2017/8/4 16:31
     *
    **/
    String verifyAccount(String loginCode);


    /**
     *
     * Author: zou yao
     * Description: {新增用户}
     * Date: 2017/8/4 17:31
     *
    **/
    String addUser(UserBean user);


    /**
     *
     * Author: zou yao
     * Description: {修改锁定状态}
     * Date: 2017/8/7 10:04
     *
    **/
    String updateLock(String id , String lock);


    /**
     *
     * Author: zou yao
     * Description: {物理删除}
     * Date: 2017/8/7 11:23
     *
    **/
    String physicalDelete(String id);
}
