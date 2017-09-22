package com.zhph.manager.controller;


import com.alibaba.fastjson.JSON;
import com.zhph.base.utils.RequestUtil;
import com.zhph.manager.model.Menu;
import com.zhph.manager.model.UserBean;
import com.zhph.manager.service.ManageService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 *
 * Author: Zou Yao
 * Description: (管理模块)
 * Time: 2017/8/3 14:47
 *
**/
@Controller
@RequestMapping("/manage")
public class ManageController {

    @Resource
    private ManageService manageService;


    /**
     *
     * Author: zou yao
     * Description: {获取到菜单列表}
     * Date: 2017/8/4 10:37
     *
    **/
    @RequestMapping("/menulist")
    @ResponseBody
    public List<Menu>  getUserMenuList(HttpServletRequest request){
        List<Menu> result = null;
        UserBean user = (UserBean) request.getSession().getAttribute("user");
        result = manageService.getUserMenuList(user);
        return result;
    }


    /**
     *
     * Author: zou yao
     * Description: {获取到用户信息管理视图}
     * Date: 2017/8/4 14:58
     *
    **/
    @RequestMapping("/usermanage")
    public String menuManageView(){
        return "manage/userManage";
    }


    /**
     *
     * Author: zou yao
     * Description: {获取到用户信息列表}
     * Date: 2017/8/4 14:59
     *
    **/
    @ResponseBody
    @RequestMapping("/userlist")
    public Map<String , Object> getListForUserInfo(HttpServletRequest request){
        Map<String , Object> result;
        //获取到请求参数
        Map<String , Object> parameter = RequestUtil.getParameter(request);
        result = manageService.getListForUserInfo(parameter);
        return result;
    }


    /**
     *
     * Author: zou yao
     * Description: {验证用户名}
     * Date: 2017/8/4 16:27
     *
    **/
    @ResponseBody
    @RequestMapping("/verifyaccount")
    public String verifyAccount(HttpServletRequest request){
        String result ;
        //获取到登录名
        String loginCode = request.getParameter("loginCode");
        if(loginCode == null || "".equals(loginCode)){
            result = "-1";
        }else{
            result = manageService.verifyAccount(loginCode);
        }
        return result;
    }

    /**
     *
     * Author: zou yao
     * Description: {新增用户}
     * Date: 2017/8/4 16:27
     *
     **/
    @ResponseBody
    @RequestMapping("/adduser")
    public String addUser(HttpServletRequest request){
        String result ;
        //获取到登录名
        String data = request.getParameter("data");
        //转换成对象
        UserBean user = JSON.parseObject(data , UserBean.class);
        result= manageService.addUser(user);
        return result;
    }


    /**
     *
     * Author: zou yao
     * Description: {更新锁定信息}
     * Date: 2017/8/7 11:20
     *
    **/
    @ResponseBody
    @RequestMapping("/updatelock")
    public String updateLock(HttpServletRequest request){
        String result ;
        //获取到登录名
        String id = request.getParameter("id");
        String lock = request.getParameter("lock");
        //转换成对象
        result = manageService.updateLock(id , lock);
        return result;
    }
    
    
    /**
     *
     * Author: zou yao
     * Description: {描述}
     * Date: 2017/8/7 11:22
     * 
    **/
    @ResponseBody
    @RequestMapping("/phydelete")
    public String physicalDelete(HttpServletRequest request){
        String result ;
        //获取到登录名
        String id = request.getParameter("id");
        result = manageService.physicalDelete(id);
        return result;
    }
}
