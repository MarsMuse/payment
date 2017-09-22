package com.zhph.manager.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.ibatis.session.RowBounds;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.zhph.base.pagination.BindedRowBounds;
import com.zhph.base.pagination.Page;
import com.zhph.manager.dao.BatchChargeRecordMapper;
import com.zhph.manager.service.BatchChargeRecordService;
import com.zhph.payment.charge.entity.BatchChargeBasicInfo;

/**
 * 批扣信息Service实现类
 * Created by lidongkui on 2017/8/7.
 */
@Service("batchChargeRecordService")
public class BatchChargeRecordServiceImpl implements BatchChargeRecordService {
    private static Logger logger = Logger.getLogger(BatchChargeRecordServiceImpl.class);
    @Resource
    private BatchChargeRecordMapper batchChargeRecordMapper; 
    
	@Override
	public Map<String, Object> getBathChargeRecordList(Map<String, Object> params) throws Exception {
		logger.info("获取批扣信息记录分页数据！");
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			if(params.get("currentPage") != null && params.get("limit") != null){
				//获取到当前页
		        int currentPage = Integer.parseInt(params.get("currentPage").toString());
		        //获取到每页数据量
		        int limit = Integer.parseInt(params.get("limit").toString());
		        //载入分页数据
		        Page page = new Page(currentPage, limit);
		        //将分页数据载入分页接口中
		        BindedRowBounds rowBounds = new BindedRowBounds(page);
		        List<BatchChargeBasicInfo> dataList = this.batchChargeRecordMapper.getListForBathChargeRecordList(params, rowBounds);
		        result.put("result", dataList);
		        result.put("totalCount", rowBounds.getPage().getTotalRecords());
			}else {
				//不分页
				List<BatchChargeBasicInfo> dataList = this.batchChargeRecordMapper.getListForBathChargeRecordList(params, RowBounds.DEFAULT);
		        result.put("result", dataList);
			}
		}catch (Exception e) {
			logger.error("获取批扣信息记录分页数据异常！在方法[getBathChargeRecordList(Map<String, Object> params)]中", e);
			throw new RuntimeException(e.getMessage());
        }
		return result;
	}

	@Override
	public Map<String, Object> getBatchChargeRecordDetail(Map<String, Object> params) throws Exception {
		logger.info("获取批扣信息记录详情分页数据！");
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			if(params.get("currentPage") != null && params.get("limit") != null){
				//获取到当前页
		        int currentPage = Integer.parseInt(params.get("currentPage").toString());
		        //获取到每页数据量
		        int limit = Integer.parseInt(params.get("limit").toString());
		        //载入分页数据
		        Page page = new Page(currentPage, limit);
		        //将分页数据载入分页接口中
		        BindedRowBounds rowBounds = new BindedRowBounds(page);
		        List<BatchChargeBasicInfo> dataList = this.batchChargeRecordMapper.getListForBathChargeRecordDetail(params, rowBounds);
		        result.put("result", dataList);
		        result.put("totalCount", rowBounds.getPage().getTotalRecords());
			}else {
				//不分页
				List<BatchChargeBasicInfo> dataList = this.batchChargeRecordMapper.getListForBathChargeRecordDetail(params, RowBounds.DEFAULT);
		        result.put("result", dataList);
			}
		}catch (Exception e) {
			logger.error("获取批扣信息记录详情分页数据异常！在方法[getBatchChargeRecordDetail(Map<String, Object> params)]中", e);
			throw new RuntimeException(e.getMessage());
        }
		return result;
	}

	@Override
	public List<JSONObject> getPlatformList() throws Exception {
		return this.batchChargeRecordMapper.getPlatformList();
	}

	@Override
	public List<JSONObject> getChannelList() throws Exception {
		return this.batchChargeRecordMapper.getChannelList();
	}

}
