package com.zhph.manager.controller;


import com.zhph.base.utils.RequestUtil;
import com.zhph.manager.model.UserBean;
import com.zhph.manager.model.WorkPlatformBasicInfo;
import com.zhph.manager.service.WorkPlatformService;
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
 * Description: (业务平台控制器)
 * Time: 2017/8/3 16:04
 *
**/
@Controller
@RequestMapping("/workplatform")
public class WorkPlatformController {

    @Resource
    private WorkPlatformService workPlatformService;

    /**
     *
     * Author: zou yao
     * Description: {密钥管理列表视图}
     * Date: 2017/8/3 16:20
     *
    **/
    @RequestMapping("/cipherlist")
    public String cipherKeyView(){
        return "workplatform/platformInfo";
    }


    /**
     *
     * Author: zou yao
     * Description: {获取到密钥列表}
     * Date: 2017/8/3 16:22
     *
    **/
    @ResponseBody
    @RequestMapping("/cipherkeylist")
    public Map<String , Object> getListForCipherKey(HttpServletRequest request){
        Map<String , Object> result = null;
        Map<String , Object> parameter = RequestUtil.getParameter(request);
        result = workPlatformService.getListForCipherKey(parameter);

        return result;
    }


    /**
     *
     * Author: zou yao
     * Description: {更新密钥信息}
     * Date: 2017/8/3 19:32
     *
    **/
    @ResponseBody
    @RequestMapping("/updatekey")
    public String updateCipherKeyInfo(HttpServletRequest request){
        String result = null;
        //获取到当前用户
        UserBean user = (UserBean) request.getSession().getAttribute("user");
        Map<String , Object> parameter = RequestUtil.getParameter(request);
        //设置更改人登录账号
        parameter.put("userCode" , user.getLoginCode());
        result = workPlatformService.updateCipherKeyInfo(parameter);
        return result;
    }
}
