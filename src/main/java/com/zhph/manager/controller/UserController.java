package com.zhph.manager.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zhph.base.encrypt.util.MessageDigestUtil;
import com.zhph.base.utils.DataUtil;
import com.zhph.base.utils.ValidateCode;
import com.zhph.manager.model.PageBean;
import com.zhph.manager.model.UserBean;
import com.zhph.manager.service.IRoleGroup;
import com.zhph.manager.service.IUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

@RequestMapping("/usermanager")
@Controller
public class UserController {

    private Logger log = LoggerFactory.getLogger(UserController.class);

    @Resource
    private IUser iUser;
    @Resource
    private IRoleGroup roleGroup;

    @RequestMapping(value = "/login" , method = RequestMethod.POST)
    @ResponseBody
    public JSONObject login( HttpServletRequest request) {
        String loginCode  =  request.getParameter("loginCode");
        log.info("{} 开始登录.........." , loginCode );
        String userPass  =  request.getParameter("userPass");
        String validateCode  =  request.getParameter("validateCode");
        int num = 0;
        boolean flag = false;
        JSONObject json = new JSONObject();
        String vCode = (String) request.getSession().getAttribute("vCode");
        if(vCode == null || !vCode.equals(validateCode.trim())){
            num = 1;// 验证码错误
        }else{
            UserBean user = iUser.login(loginCode);
            if (user != null) {
                if(MessageDigestUtil.digestToHexString(userPass).equals(user.getUserPass())){
                    user.setErrorTimes(0);
                    flag = true;
                    List<Map> userExtInfoList = roleGroup.getRoleRelationByLoginCode(loginCode);
                    request.getSession().setAttribute("user",user);
                    request.getSession().setAttribute("userExtInfoList", userExtInfoList);
                }else{
                    user.setErrorTimes(user.getErrorTimes() + 1);
                    num = 3;// 密码错误
                }
                iUser.updateUserPartInfo(user);
            } else {
                num = 4;// 用户名不存在或账户已锁定！
            }
        }

        json.put("success", flag);
        json.put("result", num);
        return json;
    }

    @RequestMapping("/addUser")
    public String addUser() {
        return "addUser";
    }

    @RequestMapping("/loginOut")
    public String loginOut(HttpServletRequest request){
        request.getSession().removeAttribute("vCode");
        request.getSession().removeAttribute("user");
        request.getSession().removeAttribute("userExtInfoList");
        return "login/login";
    }
    
    @RequestMapping("/validate")
    @ResponseBody
    public void getValidateCode(HttpServletRequest request,
            HttpServletResponse response) {
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "No-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType("image/jpeg");
        ValidateCode vc = new ValidateCode();
        try {
            String validateCode = vc.drawPicture(response.getOutputStream());
            request.getSession().setAttribute("vCode", validateCode);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    
    @RequestMapping("/main")
    public String mainPage(){
        return "index";
    }
    
    @RequestMapping("/top")
    public String topPage(){
        return "top";
    }

    @RequestMapping("/addUserPage")
    public String addUserPage(ModelMap model){
        return "addUser";
    }
    
    @RequestMapping("/loginview")
    public String toLoginPage(){
        return "login/login";
    }
    
    @RequestMapping("/saveUser")
    @ResponseBody
    public JSONObject saveUser(UserBean user, HttpServletResponse response) {
        JSONObject json = new JSONObject();
        user.setUserId("23132");
        user.setUserPass(MessageDigestUtil.digestToHexString(user.getUserPass()));
        //user.setCreateTime(new Date());
        user.setStatus(DataUtil.userStateUse);
        user.setIsLock(DataUtil.unLock);
        String saveOrNot = iUser.saveUser(user);
        json.put("flag", saveOrNot);
        return json;
    }
    
    @RequestMapping("/userList")
    public String userList(ModelMap modelMap,PageBean page){
        int userCount = iUser.findUserCount();
        page.setTotalPage((int)Math.ceil(Integer.valueOf(userCount).doubleValue()/Integer.valueOf(page.getPerPageCount()).doubleValue()));
        List<UserBean> userPageList = iUser.getPageUser(((page.getCurrentPage()-1)*page.getPerPageCount())+1,page.getCurrentPage()*page.getPerPageCount());
        page.setTotalNum(userCount);
        modelMap.addAttribute("pageBean", page);
        modelMap.addAttribute("userList", userPageList);
        return "userList";
    }
    
    @RequestMapping("/deleteUser")
    public String deleteUser(@RequestParam String userId){
        iUser.deleteUser(userId);
        return "redirect:/userList.do";
    }
    
    @RequestMapping("/updateUser")
    public String updateUser(@RequestParam String userId,ModelMap modelMap){
        UserBean user = iUser.findUserById(userId);
        user.setUserPass(MessageDigestUtil.digestToHexString(user.getUserPass()));
        modelMap.addAttribute("userBean", user);
        return addUserPage(modelMap);
    }
    
    @RequestMapping("/updateUserInfo")
    public String updateUserInfo(UserBean userBean){
        userBean.setUserPass(MessageDigestUtil.digestToHexString(userBean.getUserPass()));
        iUser.updateUserInfo(userBean);
        return "redirect:/userList.do";
    }
    
    @RequestMapping("/getCompanyById")
    public void getCompanyById(@RequestParam String companyId,HttpServletResponse response){
        StringBuilder sb = new StringBuilder();
        PrintWriter pw = null;
        try {
            pw = response.getWriter();
        } catch (Exception e) {
            e.printStackTrace();
        }
        response.setCharacterEncoding("utf-8");
        pw.write(sb.toString());
        pw.flush();
        pw.close();
    }
    
    @RequestMapping("/changePassword")
    @ResponseBody
    public JSONObject changePassword(@RequestParam String oldPassword,@RequestParam String newPassword,@RequestParam String userId){
        JSONObject json = new JSONObject();
        json.put("status", "1");
        UserBean user = iUser.findUserById(userId);
        if(MessageDigestUtil.digestToHexString(oldPassword).equals(user.getUserPass())){
            user.setUserPass(MessageDigestUtil.digestToHexString(newPassword));
            iUser.updateUserPartInfo(user);
            json.put("status", "2");
        }
        return json;
    }
}
