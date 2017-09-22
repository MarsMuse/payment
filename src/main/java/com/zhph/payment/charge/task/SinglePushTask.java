package com.zhph.payment.charge.task;

import javax.annotation.Resource;

import com.zhph.payment.charge.service.PushChargeInfoService;

/**
 * 单扣推送服务
 */
public class SinglePushTask {
	@Resource
	private PushChargeInfoService pushChargeInfoService;

	/**
	 * 单扣
	 * 
	 * @author likang
	 * @date 2017-7-31下午6:01:40
	 */
	public void pushSingle() {
		pushChargeInfoService.synPushSingleChargeInfo();
	}

	/**
	 * 定时处理向第三方发送所有扣款中的合同 查看第三方是否已更新了状态。如果已更新在，操作更新本地业务库
	 * 
	 * @author likang
	 * @date 2017-7-31下午5:59:48
	 */
	public void queryPaying() {
		pushChargeInfoService.synQueryPayingData();
	}
}
