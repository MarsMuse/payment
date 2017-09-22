package com.zhph.payment.charge.task;

import com.zhph.payment.charge.service.BatchChargeService;

import javax.annotation.Resource;

public class BatchChargeDataSynchronized {
    @Resource
    private BatchChargeService batchChargeService;
    //数据同步
    public void dataSynchronized(){
        batchChargeService.batchChargeDataSynchronize();
    }
}
