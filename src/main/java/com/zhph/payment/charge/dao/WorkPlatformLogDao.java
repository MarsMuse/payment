package com.zhph.payment.charge.dao;

import com.zhph.api.entity.PlatformCodeAndData;
import com.zhph.api.entity.ResultInformation;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;


/**
 *
 * Author: Zou Yao
 * Description: (业务平台日志数据库访问对象)
 * Time: 2017/8/4 11:57
 *
**/
@Repository
public interface WorkPlatformLogDao {


    /**
     *
     * Author: zou yao
     * Description: {插入业务平台扣款记录}
     * Date: 2017/8/4 11:58
     *
    **/
    int insetWorkPlatformChargeLog(@Param("pcd") PlatformCodeAndData pcd , @Param("ri") ResultInformation ri , @Param("chargeType") String chargeType);
}
