package com.zhph.payment.charge.service;

import com.zhph.api.entity.ResultInformation;

/**
 * 抽象单扣环绕
 * 		扣款逻辑  
 * @author likang
 */
public interface SinglePayRoundService {
	/**
	 * 单扣服务
	 * 	   处理单扣前的预判断。与第三方平台选择与金额的拆分
	 * @author likang
	 * @date 2017-7-25上午11:39:34
	 */
	ResultInformation singlePayService(String params);

	/**
	 * 单扣查询服务
	 * @return
	 */
	ResultInformation singleQueryService(String params);
}
