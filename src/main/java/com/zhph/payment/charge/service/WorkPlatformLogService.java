package com.zhph.payment.charge.service;


/**
 *
 * Author: Zou Yao
 * Description: (平台日志处理服务)
 * Time: 2017/8/4 11:39
 *
**/
public interface WorkPlatformLogService {


    /**
     *
     * Author: zou yao
     * Description: {正常返回信息}
     * Date: 2017/8/4 11:44
     *
    **/
    void insertWorkPlatformLog(String content, String chargeType , Object returnObject);


    /**
     *
     * Author: zou yao
     * Description: {异常返回信息}
     * Date: 2017/8/4 11:44
     *
    **/
    void insertWorkPlatformLog(String content , String chargeType , Throwable e);
}
