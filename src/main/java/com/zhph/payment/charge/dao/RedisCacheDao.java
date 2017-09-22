package com.zhph.payment.charge.dao;


import com.zhph.payment.charge.entity.BankCodeCacheEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 *
 * Author: Zou Yao
 * Description: (Redis缓存数据库访问对象)
 * Time: 2017/8/7 15:00
 *
**/
@Repository
public interface RedisCacheDao {


    /**
     *
     * Author: zou yao
     * Description: {通过渠道号获取到银行卡编码信息}
     * Date: 2017/8/7 15:01
     *
    **/
    List<BankCodeCacheEntity>   getBankCodeByChannelNo(@Param("channelNo") String channelNo);
}
