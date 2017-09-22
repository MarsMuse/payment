package com.zhph.payment.charge.service;

import com.zhph.payment.charge.service.business.PaymentService;



/**
 *
 * @Author: Zou Yao
 * @Description: (获取到渠道服务Bean接口)
 * @Time: 2017/7/19 13:39
 *
**/
public interface ChannelBeanService {



    /**
     *
     * @Author: zou yao
     * @Description: {获取到渠道服务对象}
     * @Date: 2017/7/19 13:41
     * @Param: No such property: code for class: Script1
     *
    **/
    PaymentService  getInstance(String channelNo ,String mainBody);
}
