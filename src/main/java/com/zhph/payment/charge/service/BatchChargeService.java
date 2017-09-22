package com.zhph.payment.charge.service;


import com.zhph.api.entity.ResultInformation;

/**
 *
 * Author: Zou Yao
 * Description: (批量扣款服务)
 * Time: 2017/7/19 9:07
 *
**/
public interface BatchChargeService {



    /**
     *
     * Author: zou yao
     * Description: {中金扣款操作服务}
     * Date: 2017/7/19 9:16
     *
    **/
    ResultInformation  batchChargeOperation(String  content) throws  Exception;



    /**
     *
     * Author: zou yao
     * Description: {批量扣款数据同步服务}
     * Date: 2017/7/28 14:08
     *
    **/
    void batchChargeDataSynchronize();


}
