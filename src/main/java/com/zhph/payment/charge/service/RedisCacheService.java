package com.zhph.payment.charge.service;

import java.util.Map;

public interface RedisCacheService {


    /**
     *
     * Author: zou yao
     * Description: {加入银行编码缓存信息}
     * Date: 2017/8/7 14:44
     *
    **/
    Map<String , String> setBankCodeCache(String channelNo , String cacheKey);


    /**
     *
     * Author: zou yao
     * Description: {获取到银行编码缓存信息}
     * Date: 2017/8/7 14:45
     *
    **/
    Map<String , String> getBankCodeCache(String cacheKey);


    /**
     *
     * Author: zou yao
     * Description: {刷新银行编码缓存}
     * Date: 2017/8/7 14:46
     *
    **/
    Map<String , String> refreshBankCodeCache(String channelNo , String cacheKey);
}
