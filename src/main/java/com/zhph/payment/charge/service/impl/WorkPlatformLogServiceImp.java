package com.zhph.payment.charge.service.impl;

import com.alibaba.fastjson.JSON;
import com.zhph.api.entity.ChargeOperationException;
import com.zhph.api.entity.PlatformCodeAndData;
import com.zhph.api.entity.ResultInformation;
import com.zhph.payment.charge.dao.WorkPlatformLogDao;
import com.zhph.payment.charge.service.WorkPlatformLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


/**
 *
 * Author: Zou Yao
 * Description: (业务平台日志服务)
 * Time: 2017/8/4 11:56
 *
**/
@Service
public class WorkPlatformLogServiceImp implements WorkPlatformLogService {

    //日志打印对象
    private Logger log = LoggerFactory.getLogger(WorkPlatformLogServiceImp.class);

    @Resource
    private WorkPlatformLogDao workPlatformLogDao;
    /**
     *
     * Author: zou yao
     * Description: {正常返回信息}
     * Date: 2017/8/4 11:44
     *
    **/
    @Override
    public void insertWorkPlatformLog(String content, String chargeType , Object returnObject) {
        log.error("业务平台日志服务--插入正常返回信息");
        ResultInformation ri = null;
        PlatformCodeAndData pcd = null;
        try {
            //平台代码与数据
            pcd = JSON.parseObject(content, PlatformCodeAndData.class);
        }catch (Exception e){
            log.error("业务平台日志服务--业务平台扣款日志对象转换异常",e);
        }
        if(returnObject instanceof  ResultInformation){
            ri = (ResultInformation) returnObject;
        }else{
            log.error("业务平台日志服务--返回结果不是类ResultInformation");
        }

        if(pcd != null && ri != null && chargeType != null) {
            int num = workPlatformLogDao.insetWorkPlatformChargeLog(pcd, ri, chargeType);
            if(num ==  1){
                log.info("业务平台日志服务--业务平台扣款日志插入成功");
            }else{
                log.error("业务平台日志服务--业务平台扣款日志插入失败");
            }
        }
    }


    /**
     *
     * Author: zou yao
     * Description: {异常返回信息}
     * Date: 2017/8/4 11:44
     *
    **/
    @Override
    public void insertWorkPlatformLog(String content, String chargeType , Throwable e) {
        log.error("业务平台日志服务--异常返回信息");
        ResultInformation ri ;

        PlatformCodeAndData pcd = null;
        try {
            //平台代码与数据
            pcd = JSON.parseObject(content, PlatformCodeAndData.class);
        }catch (Exception te){
            log.error("业务平台日志服务--业务平台扣款日志对象转换异常",te);
        }
        //如果是自定义的异常
        if(e instanceof ChargeOperationException){
            ChargeOperationException ce = (ChargeOperationException) e;
            ri = new ResultInformation("2",ce.getMessage());
        }else{
            ri = new ResultInformation("2","请求出现异常");
        }
        if(pcd != null && chargeType != null) {
            int num = workPlatformLogDao.insetWorkPlatformChargeLog(pcd, ri, chargeType);
            if(num ==  1){
                log.info("业务平台日志服务--业务平台扣款日志插入成功");
            }else{
                log.info("业务平台日志服务--业务平台扣款日志插入失败");
            }
        }
    }
}
