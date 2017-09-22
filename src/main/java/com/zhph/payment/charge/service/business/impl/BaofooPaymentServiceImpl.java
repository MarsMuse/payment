package com.zhph.payment.charge.service.business.impl;

import com.baofoo.util.JXMConvertUtil;
import com.zhph.api.entity.ChargeOperationException;
import com.zhph.api.entity.ConstantPay;
import com.zhph.base.redis.ConstantByCache;
import com.zhph.base.redis.JedisLock;
import com.zhph.base.redis.RedisUtil;
import com.zhph.channel.baofu.BaofooPaymentView;
import com.zhph.entity.ParamsEntity;
import com.zhph.entity.PaymentResult;
import com.zhph.entity.SinglePaymentResult;
import com.zhph.payment.charge.entity.ChargeDetailInfo;
import com.zhph.payment.charge.entity.CommonChargeInfo;
import com.zhph.payment.charge.service.ChargeInfoPersistenceService;
import com.zhph.payment.charge.service.business.PaymentService;
import com.zhph.payment.charge.service.impl.RedisCacheServiceImp;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 宝付扣款逻辑层    只对宝付第三方数据进行封装
 */
@Service("baofooPaymentService")
public class BaofooPaymentServiceImpl implements PaymentService {
	//日志打印对象
	private org.slf4j.Logger log = LoggerFactory.getLogger(BaofooPaymentServiceImpl.class);

	@Resource(name="baofooPaymentView")
	private BaofooPaymentView baofooPaymentView;

	@Resource
	private RedisCacheServiceImp redisCache;//获取到缓存服务

	@Resource
	private ChargeInfoPersistenceService chargeInfoPersistenceService;

	/**
	 * 根据银行键取银行对应编码
	 * @param bankKey
	 * @return
	 */
	public  String  getBankCode(String bankKey){
		Map<String , String > keyCache = redisCache.getBankCodeCache(ConstantByCache.BF_BANK_CODE);
		if(keyCache == null){//更新缓存
			keyCache = redisCache.setBankCodeCache(ConstantByCache.BF_CHANNEL_NO , ConstantByCache.BF_BANK_CODE);
		}
		String bankId = keyCache.get(bankKey);
		if(bankId == null || "".equals(bankId)) {    //判断如果银行ID不存在
			keyCache = redisCache.refreshBankCodeCache(ConstantByCache.BF_CHANNEL_NO , ConstantByCache.BF_BANK_CODE);//更新缓存
			bankId =  keyCache.get(bankId);//再次获取
		}
		return bankId;
	}

	@Override
	public void singlePayment(ChargeDetailInfo value)throws Exception {
		ParamsEntity params  = (ParamsEntity)value;
		//通过银行编码，查询对应的扣款限制信息
		//singePaymentResult = QueryBankKey(params) ;
		/*Map<String , String > keyCache = redisCache.getBankCodeCache(ConstantByCache.BF_BANK_CODE);
		if(keyCache == null){//更新缓存
			keyCache = redisCache.setBankCodeCache(ConstantByCache.BF_CHANNEL_NO , ConstantByCache.BF_BANK_CODE);
		}
		String bankId = keyCache.get(params.getBankKey());
		if(bankId == null || "".equals(bankId)){	//判断如果银行ID不存在
			keyCache = redisCache.refreshBankCodeCache(ConstantByCache.BF_CHANNEL_NO , ConstantByCache.BF_BANK_CODE);//更新缓存
			bankId =  keyCache.get(params.getBankKey());//再次获取
		}
		params.setBankCode(bankId);*/

		params.setBankCode(this.getBankCode(params.getBankKey()));
		PaymentResult paymentResults = baofooPaymentView.singlePayment(params);
		SinglePaymentResult singePaymentResult = paymentResults.getSinglePaymentResult();
		log.info(singePaymentResult.toString());
	}
	@Override
	public void singlePaymentQuery(ChargeDetailInfo chargeDetailInfo)throws Exception {
		ParamsEntity params = (ParamsEntity)chargeDetailInfo;
    	params.setChargeType(ConstantPay.SINGLESTATE_QUERY);
    	params.setPaymentChannel(ConstantPay.BAOFOO_CODE);
		PaymentResult result = this.baofooPaymentView.singlePaymentQuery(chargeDetailInfo);
		log.info(result.toString());
	}

	/**
	 * 异步调用单扣
	 * @param params
	 */
	private synchronized void anysSinglePayment(final ParamsEntity params){
		JedisLock lock =  RedisUtil.getMethodLock(false);
		 try {
			 if(lock.acquire()){
				 this.singlePayment(params);//调用单扣
			 }
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			lock.release();
		}
	}
 
	@Override
	public void batchPayment(List<ChargeDetailInfo> chargeDetailInfoList,String batchNo,String platformCode ,String mainBody) throws Exception {
		String sendFlag = "1";
		try {
			PaymentResult paymentResult = baofooPaymentView.batchPayment(chargeDetailInfoList , batchNo,platformCode);
			String response = paymentResult.getRespContent();
			response = JXMConvertUtil.XmlConvertJson(response);
			Map<String, Object> arrayDataString = JXMConvertUtil.JsonConvertHashMap(response);
			if(arrayDataString.get("resp_code").equals( "0000")){//受理成功
				//Thread.sleep(10000);//让当前线程睡眠1000毫秒
				//baofooPaymentView.QueryBatchOther(batchNo,mainBody);//调用当前批扣查询
				log.info("批扣处理成功 ！！！{}",batchNo);
			}else{
				sendFlag = "2";
				log.info("{}:批扣宝付请求受理失败,{}",batchNo,arrayDataString.get("resp_msg"));
			}
		}catch (Exception e) {
			if(e instanceof ChargeOperationException){
				ChargeOperationException ce = (ChargeOperationException) e;
				if("2".equals(ce.getCode())){//通讯异常
					log.error("宝付批扣发送出现异常，批扣号为：{}" ,batchNo);
					sendFlag = "2";
				}
			}
			e.printStackTrace();
		}finally {
			try {
				chargeInfoPersistenceService.updateBatchSendFlag(batchNo , sendFlag);//持久化数据
			} catch (Exception e) {
				e.printStackTrace();
				log.error("批扣更新发送标志位出现异常，批扣号为：{}" ,batchNo);
			}
		}
	}

 



	@Override
	public void batchPaymentQuery(String batchNo ,String mainBody) throws Exception {
		log.info("宝付查询开始,批扣号{}",batchNo);
		Integer batchNoNeedUpdate = chargeInfoPersistenceService.verifyBatchNoNeedUpdate(batchNo);
		if(batchNoNeedUpdate != null && batchNoNeedUpdate == 1){
			//支付结果
			PaymentResult  result = null;
			//获取到支付结果
			try {
				result = baofooPaymentView.batchPaymentQuery(batchNo,mainBody);
			} catch (Exception e) {
				if(e instanceof ChargeOperationException){
					ChargeOperationException ce = (ChargeOperationException) e;//通讯异常
					if("2".equals(ce.getCode())){
						log.error("批扣查询发送出现异常，批扣号为：{}" ,batchNo );
					}
				}
				e.printStackTrace();
			}
			//处理结果
			if(result != null && result.getResultBySelf() != null&& result.getResultBySelf() instanceof  List){
				List<CommonChargeInfo> chargeInfoList = (List<CommonChargeInfo>) result.getResultBySelf();//向下造型
				if(chargeInfoList != null && (!chargeInfoList.isEmpty())){
					//更新磁盘数据
					try {
						log.debug("批次号：{} 查询到批扣结果条数为：{}" ,batchNo,chargeInfoList.size());
						//持久化数据
						chargeInfoPersistenceService.updateBatchChargeDiskInfo(chargeInfoList ,batchNo);
					} catch (Exception e) {
						log.error("批扣更新扣款数据出现异常，批扣号为：{}" ,batchNo);
						e.printStackTrace();
					}
				}
			}
		}

	}
  
 
}
