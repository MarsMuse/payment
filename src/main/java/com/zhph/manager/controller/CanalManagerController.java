package com.zhph.manager.controller;

import com.alibaba.fastjson.JSON;
import com.zhph.base.utils.RequestUtil;
import com.zhph.manager.model.UserBean;
import com.zhph.manager.service.BankNormalLimitService;
import com.zhph.payment.charge.entity.BankNormalLimit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Created by zhph on 2017/8/4.
 * 渠道管理页面
 */
@Controller
@RequestMapping("/cannalManager")
public class CanalManagerController {
    private final static Logger logger = LoggerFactory.getLogger(CanalManagerController.class);
    @Resource
    private BankNormalLimitService bankNormalService;
    /**
     * 初始页面
     * @return
     */
    @RequestMapping("/cannalView")
    public String CannalView(){
        return "cannalManager/cannalView";
    }


    /**
     *
     * Author: zou yao
     * Description: {获取到密钥列表}
     * Date: 2017/8/3 16:22
     *
     **/
    @ResponseBody
    @RequestMapping("/getCannallist")
    public Map<String , Object> getListForCipherKey(HttpServletRequest request){
        Map<String , Object> parameter = RequestUtil.getParameter(request);
        Map<String , Object> result =  bankNormalService.getListForBankNormalList(parameter);
        return result;
    }

    @ResponseBody
    @RequestMapping("/updateCannal")
    public String UpdateCannal(HttpServletRequest request){
        String data1 = request.getParameter("data");  //转换成对象
        BankNormalLimit bankNormalLimit = JSON.parseObject(data1 , BankNormalLimit.class);
        UserBean user = (UserBean) request.getSession().getAttribute("user");
        int count = bankNormalService.updateBankNormal(bankNormalLimit,user);
        logger.info("更新成功");
        return count+"";
    }
    @ResponseBody
    @RequestMapping("/updateCannalState")
    public String updateCannalState(HttpServletRequest request){
        String id = request.getParameter("id");  //转换成对象
        String isEnable =  request.getParameter("isEnable");
        UserBean user = (UserBean) request.getSession().getAttribute("user");
        int count = bankNormalService.updateBankNormalState(id,isEnable,user);
        logger.info("更新成功");
        return count+"";
    }
    @ResponseBody
    @RequestMapping("/addCannal")
    public String addCannal(HttpServletRequest request){
        //获取到登录名
        String data1 = request.getParameter("data");  //转换成对象
        BankNormalLimit bankNormalLimit = JSON.parseObject(data1 , BankNormalLimit.class);
        UserBean user = (UserBean) request.getSession().getAttribute("user");
        int count = bankNormalService.addCannal(bankNormalLimit,user);
        return count+"";
    }

    @ResponseBody
    @RequestMapping("/deleteCannal")
    public String deleteCannal(HttpServletRequest request){
        String id = request.getParameter("id");  //转换成对象
        UserBean user = (UserBean) request.getSession().getAttribute("user");
        int count = bankNormalService.deleteById(id,user);
        logger.info("更新成功");
        return count+"";
    }


}
