package com.zhph.payment.charge.dao;


import com.zhph.payment.charge.entity.WorkPlatformEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 *
 * @Author: Zou Yao
 * @Description: (业务平台信息数据库访问对象)
 * @Time: 2017/7/17 18:04
 *
**/
@Repository
public interface WorkPlatformInfoDao {



    /**
     *
     * @Author: zou yao
     * @Description: {根据平台编码获取到平台信息}
     * @Date: 2017/7/17 18:06
     * @Param: No such property: code for class: Script1
     *
    **/
    List<WorkPlatformEntity> getPlatformInforByCode(@Param("platformCode") String platformCode);
}
