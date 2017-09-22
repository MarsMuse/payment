package com.zhph.payment.proxy.paylogger;

import com.zhph.entity.PaymentResult;
import com.zhph.payment.charge.entity.ChargeDetailInfo;
import org.apache.ibatis.annotations.Param;

/**
 * 支付日志
 */
public interface PayLogService {
	
	
	/**
	 *  所有方法      <p/>只处理请求报文记录日志 
	 * 			 <p/>保存请求参数
	 * @param params 请求参数 
	 * @param resultParsms 返回参数
	 * @param className 具体实现类
	 */
	void SingleRequestPayHandllog(ChargeDetailInfo params, PaymentResult resultParsms, String className) ;
	
	/**
	 * 所有方法   <p/>处理  在支付结束后报文日志
	 * 			<p/>另外更新业务库
	 * 		  <p/> 保存处理后的参数
	 * @param params 请求参数 
	 * @param resultParsms 返回参数
	 */
	void SingleResponsePayHandlog(ChargeDetailInfo params, PaymentResult resultParsms, String className);
 
	/**
	 * 批扣保存日志 <p/> 请求报文、接收报文
	 * @param params 请求参数 
	 * @param resultParsms 返回参数
	 */
	void BatchResponsePayHandlog(Object params, PaymentResult resultParsms, String batchNo, String className, int req_state);
	/**
	 * 所有方法  <p/>支付异常日志 
	 * 		 <p/>保存请求发生异常参数 
	 * @param params 请求参数 
	 * @param className 具体实现类
	 */
	void ExceptionPaylog(ChargeDetailInfo params, Throwable e, String className);
	
	/**
	 * 更新日志
	 * @author likang
	 * @date 2017-7-20下午2:14:52
	 */
	void updatePaylog(ChargeDetailInfo params, PaymentResult resultParsms, String className);

	/**
	 * 更新业务库状态
	 * @param charge_status
	 * @param charge_messgae
	 * @param update_flag
	 * @param single_no
	 * @param amount
	 * @param send_flag
	 */
	void updateReqChargeRecordInfo( String charge_status,
									   String charge_messgae,
									  String update_flag,
									   String single_no,
									  String amount,
									   String send_flag);
}
