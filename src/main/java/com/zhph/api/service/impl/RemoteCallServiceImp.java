package com.zhph.api.service.impl;

import com.zhph.api.service.RemoteCallService;
import com.zhph.base.redis.JedisLock;
import com.zhph.base.redis.RedisUtil;
import com.zhph.payment.charge.service.BatchChargeService;
import com.zhph.payment.charge.service.PushChargeInfoService;
import com.zhph.payment.charge.util.BatchChargeExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.Executor;


/**
 *
 * Author: Zou Yao
 * Description: (远程调用接口服务)
 * Time: 2017/9/14 14:18
 *
**/
@Component("remoteCallServiceImp")
public class RemoteCallServiceImp implements RemoteCallService {

    private Logger log = LoggerFactory.getLogger(RemoteCallServiceImp.class);

    @Resource
    private PushChargeInfoService pushService;
    @Resource
    private BatchChargeService batchChargeService;



    /**
     *
     * Author: zou yao
     * Description: {推送批量扣款服务}
     * Date: 2017/8/7 16:05
     *
    **/
    @Override
    public String pushBatchInfo() {
        String result = "1";
        try {
            pushService.asynPushBatchChargeInfo();
        }catch (Exception e){
            result = "2";
            log.error("远程调用--远程调用推送批量扣款信息出现异常",e);
        }
        return result;
    }


    /**
     *
     * Author: zou yao
     * Description: {更新批量扣款服务}
     * Date: 2017/8/7 16:05
     *
    **/
    @Override
    public String updateBatchCharge() {
        String result = "1";
        try {
            batchChargeService.batchChargeDataSynchronize();
        }catch (Exception e){
            result = "2";
            log.error("远程调用--远程调用同步批量扣款信息出现异常",e);
        }
        return result;
    }

    /**
     * 定时单扣推送服务
     * @return
     */
    @Override
    public String singlePush() {
        String result = "1";
        try {
            pushService.synPushSingleChargeInfo();
        }catch (Exception e){
            result = "2";
            log.error("远程调用--远程调用推送单扣扣款信息出现异常",e);
        }
        return result;
    }


    /**
     * 定时处理向第三方发送所有‘扣款中’的合同 查看第三方是否已更新了状态。
     * <p/>如果已更新，操作更新本地业务库
     *
     * @author likang
     * @date 2017-7-31下午5:59:48
     */
    @Override
    public String updateSingleCharge() {
        String result = "1";
        try {
            pushService.synQueryPayingData();
        }catch (Exception e){
            result = "2";
            log.error("远程调用--远程调用同步单扣扣款信息出现异常",e);
        }
        return result;
    }

    @Override
    public String test() throws Exception{

        Executor exe = BatchChargeExecutor.getExecutor();
        for(int i = 0 ; i <1000000; i ++) {

            final int j = i;
            exe.execute(new Runnable() {
                @Override
                public void run() {
                    JedisLock lock = RedisUtil.getSinglePaymentLock("test-mm-000", false);
                    try {
                        if (lock.acquire()) {
                            Thread.sleep(10);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        lock.release();
                    }
                }
            });
        }
        return "成功";
    }
}
