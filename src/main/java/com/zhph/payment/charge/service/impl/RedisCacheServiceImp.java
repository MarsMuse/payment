package com.zhph.payment.charge.service.impl;

import com.zhph.base.redis.RedisUtil;
import com.zhph.payment.charge.dao.RedisCacheDao;
import com.zhph.payment.charge.entity.BankCodeCacheEntity;
import com.zhph.payment.charge.service.RedisCacheService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 *
 * Author: Zou Yao
 * Description: (Redis缓存服务实现)
 * Time: 2017/8/7 14:47
 *
**/
@Service
public class RedisCacheServiceImp implements RedisCacheService {

    private Logger log = LoggerFactory.getLogger(RedisCacheServiceImp.class);

    @Resource
    private RedisCacheDao redisCacheDao;
    /**
     *
     * Author: zou yao
     * Description: {将银行编码加入缓存}
     * Date: 2017/8/7 14:47
     *
    **/
    @Override
    public Map<String, String> setBankCodeCache(String channelNo, String cacheKey) {
        log.info("将银行编码加入缓存 渠道号 {} , 缓存键值 {}" ,channelNo ,cacheKey);
        Map<String, String> result;
        //先获取到缓存
        Object temp= null;
        try {
            temp = RedisUtil.getObject(cacheKey);
        } catch (Exception e) {
            log.error("Redis缓存服务--获取缓存信息出现异常,渠道号： {}" ,channelNo,e );
        }
        if(temp  == null){
            result = getBankCodeListConvertMap(channelNo);
            try {
                RedisUtil.setObject(cacheKey ,result );
            } catch (Exception e) {
                e.printStackTrace();
                log.error("Redis缓存服务--设置渠道号： {} 的银行卡编码缓存出现错误" ,channelNo ,e);
            }
        }else{
            result = (Map<String, String>) temp;
        }
        return result;
    }


    /**
     *
     * Author: zou yao
     * Description: {获取到银行信息缓存}
     * Date: 2017/8/7 14:48
     *
    **/
    @Override
    public Map<String, String> getBankCodeCache(String cacheKey) {
        log.info("Redis缓存服务--获取到银行信息缓存");
        Map<String, String> result;
        try {
            result =  (Map<String, String>) RedisUtil.getObject(cacheKey);
        } catch (Exception e) {
            result = null;
            log.error("Redis缓存服务--获取键值为 {} 的银行卡编码缓存出现错误" ,cacheKey , e);
        }
        return result;
    }


    /**
     *
     * Author: zou yao
     * Description: {刷新银行信息缓存,暂时直接使用加入的接口}
     * Date: 2017/8/7 14:48
     *
    **/
    @Override
    public Map<String, String> refreshBankCodeCache(String channelNo, String cacheKey) {
        log.info("Redis缓存服务--刷新银行信息缓存,暂时直接使用加入的接口");
        Map<String, String> result = getBankCodeListConvertMap(channelNo);
        try {
            RedisUtil.setObject(cacheKey ,result );
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Redis缓存服务--设置渠道号为 {} 的银行卡编码缓存出现错误" ,channelNo ,e );
        }
        return result;
    }



    /**
     *
     * Author: zou yao
     * Description: {获取银行编码信息数据并且将其转化为Map}
     * Date: 2017/8/7 14:51
     *
    **/
    private Map<String , String>  getBankCodeListConvertMap(String channelNo){
        log.info("Redis缓存服务--获取银行编码信息数据并且将其转化为Map");
        //在并发条件下课使用的Map
        Map<String , String>  result = new ConcurrentHashMap<>();
        //获取到银行编码
        List<BankCodeCacheEntity> bankCodeList = redisCacheDao.getBankCodeByChannelNo(channelNo);
        //循环赋值
        for(BankCodeCacheEntity cache : bankCodeList){
            result.put(cache.getBankCodeKey() , cache.getBankCodeValue());
        }
        return result;
    }
}
