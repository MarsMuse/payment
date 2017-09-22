package com.zhph.manager.service;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

/**
 * 批扣记录信息Service接口
 * Created by lidongkui on 2017/8/7.
 */
public interface BatchChargeRecordService {
	
    /**
     * 获取全部批扣记录信息
     */
    Map<String, Object> getBathChargeRecordList(Map<String,Object> params) throws Exception;
    
    /**
     * 获取全部批扣记录详情信息
     */
    Map<String, Object> getBatchChargeRecordDetail(Map<String,Object> params) throws Exception;
    
    /**
     * 获取平台下拉框集合
     */
    List<JSONObject> getPlatformList() throws Exception;
    
    /**
     * 获取渠道下拉框集合
     */
    List<JSONObject> getChannelList() throws Exception;
    
}
