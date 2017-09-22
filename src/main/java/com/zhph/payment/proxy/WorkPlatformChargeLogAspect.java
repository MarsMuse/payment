package com.zhph.payment.proxy;


import com.zhph.payment.charge.service.WorkPlatformLogService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * Author: Zou Yao
 * Description: (业务平台扣款请求日志信息)
 * Time: 2017/8/4 10:55
 *
**/
@Aspect
@Component
public class WorkPlatformChargeLogAspect {

    //日志打印对象
    private Logger log = LoggerFactory.getLogger(WorkPlatformChargeLogAspect.class);
    //业务平台扣款日志服务
    @Resource
    private WorkPlatformLogService workPlatformLogService;
    /**
     *
     * Author: zou yao
     * Description: {设置切入点}
     * Date: 2017/8/4 11:15
     *
    **/
    @Pointcut("execution(public * com.zhph.api.service.ChargeDealService.*(..))")
    public void workPlatformLogAspect(){

    }


    /**
     *
     * Author: zou yao
     * Description: {前置任务}
     * Date: 2017/8/4 11:18
     *
    **/
    @AfterReturning(pointcut ="workPlatformLogAspect()",returning="obj")
    public void behindAspect(JoinPoint joinPoint ,Object obj){
        //获取到扣款类型
        String chargeType = getChargeType(joinPoint);
        if("1".equals(chargeType) || "2".equals(chargeType)){
            String message = getMessage(joinPoint);
            if(message != null){
                workPlatformLogService.insertWorkPlatformLog(message , chargeType ,obj);
            }
        }

    }


    /**
     *
     * Author: zou yao
     * Description: {异常日志记录}
     * Date: 2017/8/4 11:35
     *
    **/
    @AfterThrowing(pointcut = "workPlatformLogAspect()", throwing = "e")
    public void exceptionAspect(JoinPoint joinPoint,Throwable e){
        log.error("业务平台请求出现异常，异常将记录至表ZHPH_WORK_PLATFORM_CHARGE_LOG", e);
        //获取到扣款类型
        String chargeType = getChargeType(joinPoint);
        if("1".equals(chargeType) || "2".equals(chargeType)){
            String message = getMessage(joinPoint);
            if(message != null){
                workPlatformLogService.insertWorkPlatformLog(message , chargeType ,e);
            }
        }
    }




    /**
     *
     * Author: zou yao
     * Description: {获取到参数列表}
     * Date: 2017/8/4 11:32
     *
    **/
    private List<Object> getParameterList(JoinPoint joinPoint){
        List<Object> result = null;

        //获取到参数数组
        Object[] paramArray = joinPoint.getArgs();
        if(paramArray != null && paramArray.length >0){
            result = new ArrayList<>();
            Collections.addAll(result, paramArray);
        }
        return result;
    }


    /**
     *
     * Author: zou yao
     * Description: {获取到扣款信息}
     * Date: 2017/8/4 11:53
     *
    **/
    private String getMessage(JoinPoint joinPoint){
        String result = null;
        //获取到参数列表
        List<Object>  paramList = getParameterList(joinPoint);
        if(paramList != null && paramList.size()>0){
            Object object=paramList.get(0);
            if(object instanceof String){
                result = (String) object;
            }
        }
        return result;
    }


    /**
     *
     * Author: zou yao
     * Description: {获取到扣款类型}
     * Date: 2017/8/4 13:59
     *
    **/
    private String getChargeType(JoinPoint joinPoint){
        String result = null;
        String methodName = joinPoint.getSignature().getName();
        //单扣返回1
        if("singleChargeOperation".equals(methodName)){
            result = "1";
        }else if("batchChargeOperation".equals(methodName)){
            result = "2";
        }

        return result;
    }

}
