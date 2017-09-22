package com.zhph.payment.charge.service.business.impl;

import com.zhph.api.entity.ConstantPay;
import com.zhph.base.redis.ConstantByCache;
import com.zhph.channel.an.ChinagPaymentView;
import com.zhph.entity.ParamsEntity;
import com.zhph.entity.PaymentResult;
import com.zhph.payment.charge.dao.BatchChargeDao;
import com.zhph.payment.charge.entity.ChargeDetailInfo;
import com.zhph.payment.charge.entity.ChargeRecordDetail;
import com.zhph.payment.charge.service.business.PaymentService;
import com.zhph.payment.charge.service.impl.RedisCacheServiceImp;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 爱农扣款逻辑层
 * Created by zhph on 2017/2/5.
 */
@Service("chinagPayPaymentService")
public class ChinagPayPaymentServiceImpl implements PaymentService{
    //日志打印对象
    private org.slf4j.Logger log = LoggerFactory.getLogger(ChinagPayPaymentServiceImpl.class);

	@Resource(name="chinagPaymentView")
	private ChinagPaymentView chinagPaymentView;

	@Resource
	private RedisCacheServiceImp redisCache;//获取到缓存服务
    @Resource
    private BatchChargeDao batchChargeDao;  //批扣数据库访问对象


	/*@Resource
	private ChargeInfoPersistenceService chargeInfoPersistenceService;*/

	public  String  getBankCode(String bankKey){
		Map<String , String > keyCache = redisCache.getBankCodeCache(ConstantByCache.AN_BANK_CODE);
		if(keyCache == null){//更新缓存
			keyCache = redisCache.setBankCodeCache(ConstantByCache.AN_CHANNEL_NO , ConstantByCache.AN_BANK_CODE);
		}
		String bankId = keyCache.get(bankKey);
		if(bankId == null || "".equals(bankId)) {    //判断如果银行ID不存在
			keyCache = redisCache.refreshBankCodeCache(ConstantByCache.AN_CHANNEL_NO , ConstantByCache.AN_BANK_CODE);//更新缓存
			bankId =  keyCache.get(bankId);//再次获取
		}
		return bankId;
	}

	@Override
	public void singlePayment(ChargeDetailInfo values)throws Exception {
		ParamsEntity params = (ParamsEntity)values;
	/*	Map<String , String > keyCache = redisCache.getBankCodeCache(ConstantByCache.AN_BANK_CODE);
		if(keyCache == null){//更新缓存
			keyCache = redisCache.setBankCodeCache(ConstantByCache.AN_CHANNEL_NO , ConstantByCache.AN_BANK_CODE);
		}
		String bankId = keyCache.get(values.getBankKey());
		if(bankId == null || "".equals(bankId)){	//判断如果银行ID不存在
			keyCache = redisCache.refreshBankCodeCache(ConstantByCache.AN_CHANNEL_NO , ConstantByCache.AN_BANK_CODE);//更新缓存
			bankId =  keyCache.get(values.getBankKey());//再次获取
		}
		params.setBankCode(bankId);*/
		params.setBankCode(this.getBankCode(params.getBankKey()));
        //调用爱农单扣，获取扣款结果
        PaymentResult result = chinagPaymentView.singlePayment(params);
        log.info("爱农单扣请求完成"+result);
       // SinglePaymentResult singlePaymentResult = result.getSinglePaymentResult();
       // return singlePaymentResult;
	}
	
	

	@Override
	public void singlePaymentQuery(ChargeDetailInfo values)throws Exception {
		ParamsEntity params = (ParamsEntity)values;
		params.setChargeType(ConstantPay.SINGLESTATE_QUERY);
		params.setPaymentChannel(ConstantPay.CHINAG_CODE);
		PaymentResult result = chinagPaymentView.singlePaymentQuery(params);
		log.info("爱农单扣请求查询完成"+result);
	}

	@Override
	public void batchPayment(List<ChargeDetailInfo> chargeDetailInfoList,String batchNo,String platformCode ,String mainBody) throws Exception {
	/*	Map<String , String > keyCache = redisCache.getBankCodeCache(ConstantByCache.AN_BANK_CODE);
		if(keyCache == null){//更新缓存
			keyCache = redisCache.setBankCodeCache(ConstantByCache.AN_CHANNEL_NO , ConstantByCache.AN_BANK_CODE);
		}*/
        for (ChargeDetailInfo values : chargeDetailInfoList ) {
        	 ParamsEntity params  = (ParamsEntity)values;
        	 params.setChargeType(ConstantPay.BATCHSTATE);//表示批扣
        	 params.setCutNo(batchNo);//表示拆分批扣号
        	 params.setBatchCode(batchNo);
        	 params.setPaymentChannel(ConstantPay.CHINAG_CODE);
        	 params.setPlatformCode(platformCode);
			/*String bankId = keyCache.get(values.getBankKey());
				if(bankId == null || "".equals(bankId)){	//判断如果银行ID不存在
					keyCache = redisCache.refreshBankCodeCache(ConstantByCache.AN_CHANNEL_NO , ConstantByCache.AN_BANK_CODE);//更新缓存
					bankId =  keyCache.get(values.getBankKey());//再次获取
				}
				params.setBankCode(bankId);*/
        	this.singlePayment(params);
			batchChargeDao.updateBatchBasicInfoReplyCount(batchNo,1);
        }
		updateBatchTableState(batchNo);
	}

	/**
	 * 更新批扣基础表状态
	 * @param batchNo
	 */
	private void updateBatchTableState(String batchNo) {
		//当前线程休息5秒。。
		try {
			Thread.sleep(5000);                 //5000 毫秒，也就是5秒.
		} catch(InterruptedException ex) {
			Thread.currentThread().interrupt();
		}
		try {
            int secuessCount = batchChargeDao.getCountByBatchDetail(batchNo);//取批扣号下明细成功条数
            //int updateFlag = batchChargeDao.updateBatchSendEndFlag(batchNo ,"1"); //持久化数据
            int updateFlag = batchChargeDao.updateBatchSendEndFlags(batchNo,"1",secuessCount+"");
            log.info("批扣爱农{}更新状态{}",batchNo,updateFlag);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("批扣更新发送标志位出现异常，批扣号为：{}" ,batchNo);
        }
	}

	@Override
	public void batchPaymentQuery(String batchNo ,String mainBody) throws Exception {
		//只处理扣款中的数据
		List<ChargeRecordDetail> listVal  =  batchChargeDao.getChinagPaying(batchNo);
		if(listVal == null || listVal.size()<1)
			return;
		for (ChargeRecordDetail crd : listVal){
			ParamsEntity params = new ParamsEntity();
			params.setMainBody(mainBody);
			params.setChargeNo(crd.getCharge_no());
			params.setLoanNo(crd.getLoan_no());
			params.setChargeType(ConstantPay.SINGLESTATE_QUERY);
			params.setPaymentChannel(ConstantPay.CHINAG_CODE);
			PaymentResult result = chinagPaymentView.singlePaymentQuery(params);
			log.info("爱农循环单扣请求查询完成"+result);
		}
		updateBatchTableState(batchNo);
	}

 
 
 
    
}
