package com.zhph.payment.charge.task;


import javax.annotation.Resource;

import com.zhph.payment.charge.service.PushChargeInfoService;

/**
 *
 * Author: Zou Yao
 * Description: (批量推送任务)
 * Time: 2017/7/26 14:01
 *
**/
public class BatchPushTask  {

    @Resource
    private PushChargeInfoService pushChargeInfoService;
    /**
     * 批扣
     * @author likang
     * @date 2017-7-31下午6:01:47
     */
    public void pushBatch(){
        pushChargeInfoService.synPushBatchChargeInfo();
    }




}
