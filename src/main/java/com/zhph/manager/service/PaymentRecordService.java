package com.zhph.manager.service;

import java.util.Map;

/**
 * Created by zhph on 2017/8/7.
 */
public interface PaymentRecordService {
    //查询所有
    Map<String,Object>  getPaymentRecord(Map<String, Object> detail);

}
