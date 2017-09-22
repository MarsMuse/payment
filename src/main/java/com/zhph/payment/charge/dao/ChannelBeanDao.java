package com.zhph.payment.charge.dao;


import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 *
 * @Author: Zou Yao
 * @Description: (渠道Bean数据库访问对象)
 * @Time: 2017/7/19 14:24
 *
**/
@Repository
public interface ChannelBeanDao {


    /**
     *
     * @Author: zou yao
     * @Description: {根据渠道编号获取到对应的Bean名称}
     * @Date: 2017/7/19 14:25
     * @Param: No such property: code for class: Script1
     *
    **/
    List<String> getBeanNameByChannelNo(@Param("channelNo") String channelNo  , @Param("mainBody") String mainBody);



    /**
     *
     * Author: zou yao
     * Description: {获取到渠道批量扣款数量限制}
     * Date: 2017/7/20 13:44
     *
    **/
    List<Integer>  getChannelBatchLimit(@Param("channelNo") String channelNo  ,@Param("mainBody") String mainBody);
}
