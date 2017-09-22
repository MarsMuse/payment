package com.zhph.payment.charge.service;


import com.zhph.payment.charge.entity.BatchPushPlatformInfo;
import com.zhph.payment.charge.entity.PushBatchChargeInfo;
import com.zhph.payment.charge.entity.SingleChargeInfo;
import com.zhph.payment.charge.entity.SinglePushPlatformInfo;

import java.util.List;

/**
 *
 * Author: Zou Yao
 * Description: (信息推送服务)
 * Time: 2017/7/21 12:59
 *
**/
public interface PushChargeInfoService {



    /**
     * 单扣推送数据
     */
    void synPushSingleChargeInfo();
    /**
     * 单扣查询正在扣款中的数据
     * @author likang
     * @date 2017-8-1上午10:40:40
     */
    void synQueryPayingData();
    /**
     *
     * Author: Zou Yao
     * Description: (推送批扣数据)
     * Time: 2017/7/21 13:07
     *
    **/
    void asynPushBatchChargeInfo();


    /**
     *
     * Author: zou yao
     * Description: {同步推送}
     * Date: 2017/7/21 13:29
     *
    **/
    void synPushBatchChargeInfo();
    
    
    /**
     * 单扣操作推送操作
     * @author likang
     * @date 2017-7-28上午10:33:54
     */
    void pushSingleOperation(List<SingleChargeInfo>  pushList , BatchPushPlatformInfo platformInfo);


    /**
     *
     * Author: zou yao
     * Description: {批量推送操作}
     * Date: 2017/7/21 13:27
     **/
    void pushBatchOperation(List<PushBatchChargeInfo>  pushList , BatchPushPlatformInfo platformInfo);


    
    /**
     *
     * Author: zou yao
     * Description: {发送数据至业务平台并返回结果}
     * Date: 2017/7/21 13:43
     *
    **/
    String sendMessageToWorkPlatform(String cipher , String callBackPath) throws  Exception;



    /**
     *
     * Author: zou yao
     * Description: {推送数据的加密发送与收到数据候的解密}
     * Date: 2017/7/26 12:14
     *
    **/
    String pushDataEncryptAndDecrypt(String sourceData , BatchPushPlatformInfo platformInfo) throws Exception;
}
