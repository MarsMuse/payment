package com.zhph.payment.charge.service;


import com.zhph.payment.charge.entity.CommonChargeInfo;

import java.util.List;

/**
 *
 * Author: Zou Yao
 * Description: (第三方渠道返回数据持久化服务)
 * Time: 2017/7/25 16:57
 *
**/
public interface ChargeInfoPersistenceService {



    /**
     *
     * Author: zou yao
     * Description: {验证该批扣号是否需要同步数据}
     * Date: 2017/7/25 17:13
     *
    **/
    Integer verifyBatchNoNeedUpdate(String batchNo);
    /**
     *
     * Author: zou yao
     * Description: {批量扣款发送标志位更新操作}
     * Date: 2017/7/25 17:01
     *
    **/
    void updateBatchSendFlag(String batchNo , String sendFlag) throws Exception;


    /**
     *
     * Author: zou yao
     * Description: {批量扣款扣款信息更新操作}
     * Date: 2017/7/25 17:02
     *
    **/
    void updateBatchChargeDiskInfo(List<CommonChargeInfo> chargeInfoList , String batchNo) throws Exception;


    /**
     *
     * Author: zou yao
     * Description: {验证该交易号是否需要同步数据}
     * Date: 2017/7/25 17:38
     *
    **/
    Integer verifyChargeNoNeedUpdate(String chargeNo);


    /**
     *
     * Author: zou yao
     * Description: {单扣发送标志位更新操作}
     * Date: 2017/7/25 17:39
     *
    **/
    void updateSingleSendFlag(String batchNo , String sendFlag) throws Exception;


    /**
     *
     * Author: zou yao
     * Description: {单扣扣款信息更新操作}
     * Date: 2017/7/25 17:40
     *
    **/
    void updateSingleChargeDiskInfo(CommonChargeInfo chargeInfo)throws Exception;

}
