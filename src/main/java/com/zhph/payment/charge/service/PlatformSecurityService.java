package com.zhph.payment.charge.service;


import com.zhph.api.entity.ResultInformation;

/**
 *
 * @Author: Zou Yao
 * @Description: (扣款接口信息验证服务)
 * @Time: 2017/7/17 11:14
 *
**/
public interface PlatformSecurityService {



    /**
     *
     * @Author: zou yao
     * @Description: {请求信息验证}
     * @Date: 2017/7/17 11:21
     * @Param: tis
     *
    **/
    ResultInformation verifyRequestInfo(String info) throws Exception;



    /**
     *
     * Author: zou yao
     * Description: {加密推送信息}
     * Date: 2017/7/21 11:51
     *
    **/
    String encryptPushInfo(String  content  ,String  cerPath , String cerName) throws Exception;
}
