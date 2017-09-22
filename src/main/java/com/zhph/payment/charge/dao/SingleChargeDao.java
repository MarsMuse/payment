package com.zhph.payment.charge.dao;

import com.zhph.payment.charge.entity.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.zhph.entity.ParamsEntity;

import java.util.List;


/**
 *
 * Description: (单扣扣款数据库访问对象)
 * Time: 2017/7/20 11:51
 *
 **/
@Repository
public interface SingleChargeDao{

	/**
	 * 单扣基础数据 用于推送标识
	 * @author likang
	 * @date 2017-8-1下午4:08:50
	 */
    int insertSingleBasicInfo(@Param("batchInfo") SingleChargeBasicInfo changeInfo);

    /**
     *  插入业务表数据 用于更新当前数据状态
     **/
    int insertChargeRecordInfo(@Param("batchInfo") ParamsEntity changeInfo);

    /**
     * 请求了第三方平台，，表示处于更新中
     * @author likang
     * @date 2017-7-25下午1:59:02
     * @param charge_status 扣款状态（0：扣款中1：扣款成功2：扣款失败，默认为0）
     * @param charge_messgae 扣款返回信息
     * @param update_flag	是否更新（0：未更新1：已更新，默认为未更新）
     * @param single_no  	单扣号，在单扣时使用
     * @param amount   		实际扣款金额
     * @param send_flag    提供给单扣操作使用，发送至第三方标志位（0：发送中1：发送成功2：发送失败，3：忽略此字段 默认为0）
     */
    int updateReqChargeRecordInfo(@Param("charge_status")String charge_status,
                                  @Param("charge_messgae")String charge_messgae,
                                  @Param("update_flag")String update_flag,
                                  @Param("single_no")String single_no,
                                  @Param("amount")String amount,
                                  @Param("send_flag")String send_flag);

    int updateLastState(@Param("single_no") String single_no);
    /**
     * 查询需要发送给业务平台的数据
     */
    List<BatchPushPlatformInfo>  getNeedPushPlatformInfo();

	/**
	 * 根据平台号查询需要的回推送地址
	 * @param platform_code
	 * @return
	 */
	BatchPushPlatformInfo getNeedPushPlatformInfoByCode(@Param("platform_code") String platform_code);
    
    /**
     * 获取要推送的数据
     * @author likang
     * @date 2017-7-28下午2:02:42
     */
    List<SingleChargeInfo> getPushChargeInfo(@Param("platformInfo") BatchPushPlatformInfo platformInfo);

    /***
     * 查询所有单扣正在扣款中的数据
     * @author likang
     * @date 2017-8-1上午10:09:52
     */
    List<ChargeRecordDetail> getPayingChargeInfo();


	/***
	 * 查询所有单扣正在扣款中的数据
	 * @author likang
	 * @date 2017-8-1上午10:09:52
	 */
	List<ChargeRecordDetail> getPayingChargeInfoById(@Param("charge_no") String charge_no);
	/**
	 * 指定交易号查询扣款中的数据，
	 *   并且在未推送的状态
	 * @param charge_no 交易号
	 * @return
	 */
	List<ChargeRecordDetail> getSingleQueryPayingChargeInfo(@Param("charge_no") String charge_no);
    /**
     * 记录失败次数
     * @author likang
     * @date 2017-7-28上午10:36:58
     */
	void updatePushErrorInfo(@Param("pushList") List<SingleChargeInfo> pushList);

	/**
	 * 批量更新
	 * @author likang
	 * @date 2017-7-28上午10:37:38
	 */
	void updateSinglePushInfo(@Param("pushSingleInfo") List<ChargePushBackInfo> pushBackInfo);
	
	
	/**
	 * 在拆分数据后。更新数据返回结果次数
	 * @author likang
	 * @date 2017-8-2上午11:34:42
	 */
	void updateSingleBatchReplyCount(@Param("single_no")String single_no,@Param("updateCount")Integer updateCount);
	/**
	 * 在拆分数据后。更新发送数
	 * @author likang
	 * @date 2017-8-29下午3:04:50
	 */
	void updateSinlgeBatchSendCount(@Param("single_no")String single_no,@Param("updateCount")Integer updateCount);
	/**
	 * 更新处理完成的数据
	 * @author likang
	 * @date 2017-8-2上午11:38:54
	 */
   int updateSingleEndFlag(@Param("single_no") String single_no);

	/**
	 * 查询当前交易号拆分了几单
	 * @param chargeNo 交易号
	 * @return
	 */
	Integer getSingleQueryPayingSize(@Param("single_no") String chargeNo);
}
