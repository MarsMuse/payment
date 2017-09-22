package com.zhph.payment.charge.dao;

import com.zhph.payment.charge.entity.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 *
 * Author: Zou Yao
 * Description: (批量扣款数据库访问对象)
 * Time: 2017/7/20 11:51
 *
**/
@Repository
public interface BatchChargeDao {


    /**
     *
     * Author: zou yao
     * Description: {插入批量扣款基本信息，在数据库中利用数据库的事务隔离机制，使用乐观锁对数据进行处理}
     * Date: 2017/7/20 12:10
     *
    **/
    int insertBatchBasicInfo(@Param("batchInfo") BatchChargeBasicInfo batchInfo);
    
    
    /**
    *
    * Description: {插入批量扣款基本信息，在数据库中利用数据库的事务隔离机制，使用乐观锁对数据进行处理}
    * Date: 2017/7/20 12:10
    *
   **/
   int insertChargeRecordInfo(@Param("batchInfo") ChargeRecordDetail changeInfo);




    /**
     *
     * Author: zou yao
     * Description: {插入批量扣款无效数据基本信息，在数据库中利用数据库的事务隔离机制，使用乐观锁对数据进行处理}
     * Date: 2017/7/20 15:36
     *
    **/
    int insertInvalidBatchBasicInfo(@Param("batchInfo") BatchChargeBasicInfo batchInfo);



    /**
     *
     * Author: zou yao
     * Description: {记录有效扣款数据}
     * Date: 2017/7/20 12:13
     *
    **/
    void insertValidBatchChargeInfo(@Param("validList")List<ChargeDetailInfo> validList ,@Param("batchInfo") BatchChargeBasicInfo batchInfo);



    /**
     *
     * Author: zou yao
     * Description: {记录无效扣款数据}
     * Date: 2017/7/20 12:13
     *
    **/
    void insertInvalidBatchChargeInfo(@Param("invalidList")List<ChargeDetailInfo> invalidList ,@Param("batchInfo") BatchChargeBasicInfo batchInfo);


    /**
     *
     * Author: zou yao
     * Description: {获取到需要做推送任务的平台列表}
     * Date: 2017/7/21 10:39
     *
    **/
    List<BatchPushPlatformInfo>  getNeedPushPlatformInfo();


    /**
     *
     * Author: zou yao
     * Description: {获取到需要推送的扣款信息}
     * Date: 2017/7/21 11:29
     *
    **/
    List<PushBatchChargeInfo>  getPushChargeInfo(@Param("platformInfo") BatchPushPlatformInfo platformInfo);



    /**
     *
     * Author: zou yao
     * Description: {更新推送失败信息}
     * Date: 2017/7/21 14:02
     *
    **/
    void updatePushErrorInfo(@Param("pushList") List<PushBatchChargeInfo>  pushList);


    /**
     *
     * Author: zou yao
     * Description: {更新批量推送消息}
     * Date: 2017/7/26 10:58
     *
    **/
    void updateBatchPushInfo(@Param("pushBackInfo") List<ChargePushBackInfo>  pushBackInfo);
    
    
    /**
     *
     * Author: zou yao
     * Description: {描述}
     * Date: 2017/7/25 9:48
     * 
    **/
    int updateBatchSendFlag(@Param("batchNo") String batchNo , @Param("flag") String flag);

    int updateBatchSendEndFlag(@Param("batchNo") String batchNo , @Param("flag") String flag);


    /**
     *
     * Author: zou yao
     * Description: {更新通讯异常数据}
     * Date: 2017/7/25 9:49
     *
    **/
    int batchUpdateCommunicationErrorCharge(@Param("batchNo")String batchNo);


    /**
     *
     * Author: zou yao
     * Description: {验证该批次号是否存在，是否需要更新}
     * Date: 2017/7/25 13:28
     *
    **/
    Integer verifyBatchNoNeedUpdate(@Param("batchNo") String batchNo);


    /**
     *
     * Author: zou yao
     * Description: {获取到已经完成更新的批量扣款数据}
     * Date: 2017/7/25 13:53
     *
    **/
    List<String> getFinishUpdateBatchChargeNo(@Param("batchNo") String batchNo);


    /**
     *
     * Author: zou yao
     * Description: {更新批量扣款返回数据}
     * Date: 2017/7/25 14:41
     *
    **/
    int updateBatchChargeInfo(@Param("info") CommonChargeInfo info);


    /**
     *
     * Author: zou yao
     * Description: {更新已扣款数据}
     * Date: 2017/7/25 14:43
     *
    **/
    int updateBatchBasicInfoReplyCount(@Param("batchNo")String batchNo , @Param("updateCount")Integer updateCount);



    /**
     *
     * Author: zou yao
     * Description: {将通过判断更新该批次是否完成}
     * Date: 2017/7/25 14:44
     *
    **/
    int updateBatchBasicInfoEndFlag(@Param("batchNo")String batchNo);



    /**
     *
     * Author: zou yao
     * Description: {获取到需要同步的批扣数据}
     * Date: 2017/7/28 14:15
     *
    **/
    List<BatchChargeBasicInfo>  getNeedSynchronizedBatchInfo();


    List<ChargeRecordDetail> getChinagPaying(@Param("batchNo")String batchNo);

 /**
  * 取批扣号下成功条数
  * @param batchNo
  * @return
  */
  int getCountByBatchDetail(@Param("batchNo")String batchNo);

 /**
  * 更新批扣号实始化状态
  * @param batchNo 批扣号
  * @param send_flag 发送状态
  * @param secuessCount 成功条数
  */
 int updateBatchSendEndFlags(@Param("batchNo") String batchNo,@Param("sendFlag") String send_flag,@Param("secuessCount")  String secuessCount);
}
