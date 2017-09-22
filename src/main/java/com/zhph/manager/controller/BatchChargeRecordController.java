package com.zhph.manager.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.zhph.base.utils.ExeclUtil;
import com.zhph.base.utils.ListToArray;
import com.zhph.base.utils.RequestUtil;
import com.zhph.manager.service.BatchChargeRecordService;

/**
 * 批扣记录Controller
 * Created by lidongkui on 2017/8/7.
 */
@Controller
@RequestMapping("/batchChargeRecord")
public class BatchChargeRecordController {
	private static Logger logger = Logger.getLogger(BatchChargeRecordController.class);
	@Resource
	private BatchChargeRecordService batchChargeRecordService;
	
    /**
     * 显示批扣信息记录列表页面
     */
    @RequestMapping("batchChargeRecordList")
    public String batchChargeRecordList(){
    	return "/batchChargeRecord/batchChargeRecordList";
    }
    
    /**
     * 获取页面查询下拉框数据
     */
    @RequestMapping("getDropdownList")
    @ResponseBody
    public Map<String, Object> getDropdownList() throws Exception {
    	logger.info("获取页面查询下拉框数据!");
    	Map<String, Object> map = new HashMap<String, Object>();
        try{
		    //获取平台下拉框集合
		    List<JSONObject> platformList = this.batchChargeRecordService.getPlatformList();
		    map.put("platformList", platformList);
		    //获取渠道下拉框集合
		    List<JSONObject> channelList = this.batchChargeRecordService.getChannelList();
		    map.put("channelList", channelList);
        }catch (Exception e){
			logger.error("获取页面查询下拉框数据异常！在方法[getDropdownList()]中", e);
			map.put("flag",false);
			map.put("errorStr",e.getMessage());
        }
        return map;
    }
    
    /**
     * 获取批扣信息记录列表页面数据
     */
    @RequestMapping("batchChargeRecordData")
    @ResponseBody
    public Map<String, Object> batchChargeRecordData(HttpServletRequest request) throws Exception {
    	logger.info("批扣信息记录列表页面查询!");
    	Map<String, Object> result = new HashMap<String, Object>();
        try{
		    //获取到前台请求信息
		    Map<String, Object> params = RequestUtil.getParameter(request);
		    //获取到查询数据
		    result = this.batchChargeRecordService.getBathChargeRecordList(params);
        }catch (Exception e){
			logger.error("批扣信息记录列表页面查询异常！在方法[batchChargeRecordData(HttpServletRequest request)]中", e);
			throw new RuntimeException(e.getMessage());
        }
        return result;
    }
    
    /**
     * 显示批扣信息记录详情页面
     */
    @RequestMapping("batchChargeRecordDetail")
    public String batchChargeRecordDetail(HttpServletRequest request, Model model) throws Exception {
    	logger.info("显示批扣信息记录详情页面!");
    	try {
        	//获取列表页面的批口号
        	String batchNo = request.getParameter("batchNo");
            if(null == batchNo || "".equals(batchNo)){
                throw new NullPointerException("批口号为空！");
            }
            model.addAttribute("batchNo", batchNo);
		} catch (Exception e) {
			logger.error("显示批扣信息记录详情页面异常！在方法[batchChargeRecordData(HttpServletRequest request)]中", e);
			throw new RuntimeException(e.getMessage());
		}
    	return "/batchChargeRecord/batchChargeRecordDetail";
    }
    
    /**
     * 获取批扣信息记录详情页面数据
     */
    @RequestMapping("batchChargeRecordDetailData")
    @ResponseBody
    public Map<String, Object> batchChargeRecordDetailData(HttpServletRequest request) throws Exception {
    	logger.info("批扣信息记录详情页面查询!");
    	Map<String, Object> result = new HashMap<String, Object>();
        try{
		    //获取到前台请求信息
		    Map<String, Object> params = RequestUtil.getParameter(request);
		    //获取到查询数据
		    result = this.batchChargeRecordService.getBatchChargeRecordDetail(params);
        }catch (Exception e){
			logger.error("批扣信息记录详情页面查询异常！在方法[batchChargeRecordDetailData(HttpServletRequest request)]中", e);
			result.put("flag",false);
			result.put("errorStr",e.getMessage());
        }
        return result;
    }
    
    /**
     * 导出批扣记录列表数据到Excel
     */
    @RequestMapping("exportBatchChargeRecordList")
    @ResponseBody
    public void exportBatchChargeRecordList(HttpServletRequest request, HttpServletResponse response) throws Exception {
    	logger.info("批扣记录列表数据导出");
    	Map<String, Object> result = new HashMap<String, Object>();
    	try {
    		//获取到前台请求信息
    	    Map<String, Object> params = RequestUtil.getParameter(request);
    		//表头
    		String[] title = new String[] { "序号", "平台名称", "批口号", "发送批扣数据量", "收到扣款回复条数", "成功推送条数", "创建时间", "最近更新时间", "标志位状态", "渠道", "发送状态", "主体", };
    		//长度
    		short[] colWidthAry = { 50,140,140,140,140,140,140,140,140,140,140,140 };
    		//numcol
    		int[][] numColAry = {};
    		//获取到导出的数据
    		result = this.batchChargeRecordService.getBathChargeRecordList(params);
    		String[][] expDao = new String[0][];
    		if(((List<?>) result.get("result")).size()>0){
    			//将list集合转化成二维数组
    			expDao = ListToArray.listToArrayWay((List<?>) result.get("result"));
    		}
    		ExeclUtil eu = new ExeclUtil();
    		@SuppressWarnings("deprecation")
    		String filePath = request.getRealPath("/").replace("\\", "/") + "fileTemp/批扣记录信息报表";
    		String reportName = "批扣记录信息报表";
    		eu.downloadExecl(title, colWidthAry, expDao, numColAry, response, filePath, reportName);
    		eu = null;
		} catch (Exception e) {
			logger.error("导出批扣记录列表数据到Excel异常！在方法[exportBatchChargeRecordList(HttpServletRequest request, HttpServletResponse response)]中", e);
			throw new RuntimeException(e.getMessage());
		}
    }
    
    /**
     * 导出批扣记录详情列表数据到Excel
     */
    @RequestMapping("exportBatchChargeRecordDetail")
    @ResponseBody
    public void exportBatchChargeRecordDetail(HttpServletRequest request, HttpServletResponse response) throws Exception {
    	logger.info("批扣记录详情列表数据导出");
    	Map<String, Object> result = new HashMap<String, Object>();
    	try {
    		//获取到前台请求信息
    	    Map<String, Object> params = RequestUtil.getParameter(request);
    		//表头
    		String[] title = new String[] { "序号", "平台名称", "扣款方式", "渠道", "扣款编号", "分公司名称", "合同号", "客户姓名", "客户身份证号", "客户手机号", "账单日", "还款期数", "客户银行卡号", "扣款金额", "操作人姓名", "操作时间", "扣款时间", "扣款返回信息", "扣款状态", "推送标志位", "推送次数", "最近推送时间", "主体", "银行卡键值", "创建时间", "更新时间", "是否更新", };
    		//长度
    		short[] colWidthAry = { 50,140,140,140,140,140,140,140,140,140,140,140,140,140,140,140,140,140,140,140,140,140,140,140,140,140,140,140 };
    		//numcol
    		int[][] numColAry = {{ 13,5 }};//设置为货币格式
    		//获取到导出的数据
    		result = this.batchChargeRecordService.getBatchChargeRecordDetail(params);
    		String[][] expDao = new String[0][];
    		if(((List<?>) result.get("result")).size()>0){
    			//将list集合转化成二维数组
    			expDao = ListToArray.listToArrayWay((List<?>) result.get("result"));
    		}
    		ExeclUtil eu = new ExeclUtil();
    		@SuppressWarnings("deprecation")
    		String filePath = request.getRealPath("/").replace("\\", "/") + "fileTemp/批扣记录详情信息报表";
    		String reportName = "批扣记录详情信息报表";
    		eu.downloadExecl(title, colWidthAry, expDao, numColAry, response, filePath, reportName);
    		eu = null;
		} catch (Exception e) {
			logger.error("导出批扣记录详情列表数据到Excel异常！在方法[exportBatchChargeRecordDetail(HttpServletRequest request, HttpServletResponse response)]中", e);
			throw new RuntimeException(e.getMessage());
		}
    }
}
