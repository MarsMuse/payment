package com.zhph.manager.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import com.alibaba.fastjson.JSONObject;
import com.zhph.payment.charge.entity.BatchChargeBasicInfo;

/**
 * 批扣记录信息Dao接口
 * Created by lidongkui on 2017/8/7.
 */
public interface BatchChargeRecordMapper {
   /**
    * 获取批扣信息
    */
	List<BatchChargeBasicInfo> getListForBathChargeRecordList(@Param("params")Map<String, Object> params, @Param("rowBounds")RowBounds rowBounds);
   
   /**
    * 获取批扣记录详细信息
    */
   List<BatchChargeBasicInfo> getListForBathChargeRecordDetail(@Param("params")Map<String, Object> params, @Param("rowBounds")RowBounds rowBounds);
   
   /**
    * 获取平台下拉框集合
    */
   List<JSONObject> getPlatformList() throws Exception;
   
   /**
    * 获取渠道下拉框集合
    */
   List<JSONObject> getChannelList() throws Exception;
}
