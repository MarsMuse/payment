package com.zhph.payment.charge.service;


import com.zhph.api.entity.ResultInformation;

/**
 *
 * @Author: Zou Yao
 * @Description: (扣款接口信息验证服务)
 * @Time: 2017/7/17 11:14
 *
**/
public interface ChargeVerifyService {



    /**
     *
     * @Author: zou yao
     * @Description: {请求信息验证}
     * @Date: 2017/7/17 11:21
     * @Param: tis
     *
    **/
    ResultInformation verifyRequestInfo(String info);
}
