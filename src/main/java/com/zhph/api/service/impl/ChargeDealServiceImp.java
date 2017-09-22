package com.zhph.api.service.impl;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.zhph.api.entity.ChargeOperationException;
import com.zhph.api.entity.ResultInformation;
import com.zhph.api.service.ChargeDealService;
import com.zhph.payment.charge.service.BatchChargeService;
import com.zhph.payment.charge.service.SinglePayRoundService;

/**
 * 
 * @Author: Zou Yao
 * @Description: (扣款处理接口服务实现类)
 * @Time: 2017/7/17 9:36
 * 
 **/
@Component("chargeDealServiceImp")
public class ChargeDealServiceImp implements ChargeDealService {
    // 日志打印对象
    private final Logger log = Logger.getLogger(ChargeDealServiceImp.class);

    @Resource
    private BatchChargeService batchChargeService;

    // 单扣抽象
    @Resource
    private SinglePayRoundService singlePayRoundService;


    /**
     **/
    @Override
    public ResultInformation singleChargeOperation(String message)  {
        // 调用批扣服务
        ResultInformation ri;
        try {
            ri =singlePayRoundService.singlePayService(message); // 调用单扣方法
        } catch (Exception e) {
            if (e instanceof ChargeOperationException) {
                ChargeOperationException ce = (ChargeOperationException) e;
                ri = new ResultInformation(ce.getCode(), ce.getMessage());
            } else {
                log.error("扣款处理服务--调用单扣服务出现非预知异常");
                ri = new ResultInformation("2", "调用单扣服务异常");
            }
            log.error("扣款处理服务--调用单扣服务出现异常",e);
        }
        return ri;


    }

    /**
     * 单扣查询扣款中的状态
     * @param message
     * @return
     */
    @Override
    public ResultInformation singleQueryCharge(String message) {
        // 调用单扣查询服务
        ResultInformation ri;
        try {
            ri = singlePayRoundService.singleQueryService(message);
        } catch (Exception e) {
            if (e instanceof ChargeOperationException) {
                ChargeOperationException ce = (ChargeOperationException) e;
                ri = new ResultInformation(ce.getCode(), ce.getMessage());
            } else {
                log.error("扣款处理服务--调用单扣查询服务异常");
                ri = new ResultInformation("2", "调用单扣查询服务异常");
            }
            e.printStackTrace();
            log.error("扣款处理服务--调用单扣查询出现异常",e);
        }
        return ri;
    }

    /**
     * 
     * @Author: zou yao
     * @Description: {单笔扣款操作接口}
     * @Date: 2017/7/17 9:39
     * @Param:
     * 
     **/
    @Override
    public ResultInformation batchChargeOperation(String message) {
        // 调用批扣服务
        ResultInformation ri;
        try {
            ri = batchChargeService.batchChargeOperation(message);
        } catch (Exception e) {
            if (e instanceof ChargeOperationException) {
                ChargeOperationException ce = (ChargeOperationException) e;
                ri = new ResultInformation(ce.getCode(), ce.getMessage());
            } else {
                log.error("扣款处理服务--调用批扣服务异常");
                ri = new ResultInformation("2", "调用批扣服务异常");
            }
            log.error("扣款处理服务--调用批扣服务出现异常",e);
        }
        return ri;
    }


}
