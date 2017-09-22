package com.zhph.manager.service;

import com.zhph.payment.charge.entity.ChargeRecordDetail;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by zhph on 2017/8/7.
 */
public interface ChargeService {
    //查询所有
    Map<String,Object>   getChargeRecored(Map<String,Object> detail);

    int updateChargeRecored(String localNo);
}
