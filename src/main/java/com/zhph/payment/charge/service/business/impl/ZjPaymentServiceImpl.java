package com.zhph.payment.charge.service.business.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.zhph.base.redis.ConstantByCache;
import com.zhph.payment.charge.service.impl.RedisCacheServiceImp;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.zhph.api.entity.ChargeOperationException;
import com.zhph.api.entity.ConstantPay;
import com.zhph.channel.PaymentChannel;
import com.zhph.entity.ParamsEntity;
import com.zhph.entity.PaymentResult;
import com.zhph.payment.charge.dao.SingleChargeDao;
import com.zhph.payment.charge.entity.ChargeDetailInfo;
import com.zhph.payment.charge.entity.CommonChargeInfo;
import com.zhph.payment.charge.service.ChargeInfoPersistenceService;
import com.zhph.payment.charge.service.business.PaymentService;


/**
 *
 * Author: Zou Yao
 * Description: (中金服务实现)
 * Time: 2017/7/20 9:48
 *
**/
@Service("zjPaymentService")
public class ZjPaymentServiceImpl implements PaymentService{

    //日志打印对象
    private Logger log = LoggerFactory.getLogger(ZjPaymentServiceImpl.class);
    //获取到中金的视图信息
    @Resource(name = "zjPaymentView")
    private PaymentChannel zjPaymentView;

    //批扣数据库访问对象
    @Resource
    private ChargeInfoPersistenceService chargeInfoPersistenceService;
	@Resource
	private SingleChargeDao singleChargeDao;

    @Resource
    private RedisCacheServiceImp redisCache;//获取到缓存服务
    /**
     *
     * Author: zou yao
     * Description: {单扣接口，提供单笔扣款服务。}
     * Date: 2017/7/21 17:57
     *
    **/
    @Override
    public void singlePayment(ChargeDetailInfo chargeDetailInfo) throws Exception {
        log.info("中金扣款服务服务--单扣方法被调用");
        ParamsEntity params = (ParamsEntity)chargeDetailInfo;
        Map<String , String > keyCache = redisCache.getBankCodeCache(ConstantByCache.ZJ_BANK_CODE);
        if(keyCache == null){//更新缓存
            keyCache = redisCache.setBankCodeCache(ConstantByCache.ZJ_CHANNEL_NO , ConstantByCache.ZJ_BANK_CODE);
        }
        String bankId = keyCache.get(params.getBankKey());
        if(bankId == null || "".equals(bankId)){	//判断如果银行ID不存在
            keyCache = redisCache.refreshBankCodeCache(ConstantByCache.ZJ_CHANNEL_NO , ConstantByCache.ZJ_BANK_CODE);//更新缓存
            bankId =  keyCache.get(params.getBankKey());//再次获取
        }
        params.setBankCode(bankId);
        zjPaymentView.singlePayment(params);
    }


    /**
     *
     * Author: zou yao
     * Description: {单笔扣款查询接口}
     * Date: 2017/7/21 17:58
     *
    **/
    @Override
    public void singlePaymentQuery(ChargeDetailInfo chargeDetailInfo) throws Exception {
        log.info("中金扣款服务服务--单笔扣款查询接口");
    	ParamsEntity params = (ParamsEntity)chargeDetailInfo;
    	params.setChargeType(ConstantPay.SINGLESTATE_QUERY);
    	params.setPaymentChannel(ConstantPay.ZJ_CODE);
        zjPaymentView.singlePaymentQuery(chargeDetailInfo);
    }



    /**
     *
     * Author: zou yao
     * Description: {批扣接口，提供批量扣款服务}
     * Date: 2017/7/21 17:58
     *
    **/
    @Override
    public void batchPayment(List<ChargeDetailInfo> chargeDetailInfoList, String batchNo,String platformCode,String mainBody) {
        log.info("中金扣款服务服务--批扣接口，提供批量扣款服务");
        //扣款结果对象
        String sendFlag = "1";
        try {
            PaymentResult paymentResult = zjPaymentView.batchPayment(chargeDetailInfoList , batchNo,platformCode);
            String response = paymentResult.getRespContent();
            Document doc = DocumentHelper.parseText(response);
            Element rootElement = doc.getRootElement();
            String code = (String) rootElement.element("Head").element("Code").getData();
            //返回编码不为2000则是发送失败
            if(!"2000".equals(code)){
                sendFlag = "2";
            }
        }catch (Exception e) {
            if(e instanceof ChargeOperationException){
                ChargeOperationException ce = (ChargeOperationException) e;
                //通讯异常
                if("2".equals(ce.getCode())){
                    log.error("批扣发送出现异常，批扣号为：{}" ,batchNo);
                    sendFlag = "2";
                }
            }
            e.printStackTrace();
            log.error("批扣发送出现异常，批扣号为：{}" ,batchNo , e);
        }finally {
            try {
                //持久化数据
                chargeInfoPersistenceService.updateBatchSendFlag(batchNo , sendFlag);
            } catch (Exception e) {
                e.printStackTrace();
                log.error("批扣更新发送标志位出现异常，批扣号为：{}" ,batchNo , e);
            }
        }
    }


    /**
     *
     * Author: zou yao
     * Description: {批量扣款查询接口}
     * Date: 2017/7/21 17:59
     *
    **/
    @Override
    public void batchPaymentQuery(String batchNo,String mainBody){
        log.info("中金扣款服务服务--批量扣款查询接口");
        Integer batchNoNeedUpdate = chargeInfoPersistenceService.verifyBatchNoNeedUpdate(batchNo);
        if(batchNoNeedUpdate != null && batchNoNeedUpdate == 1){
            //支付结果
            PaymentResult  result = null;
            //获取到支付结果
            try {
                result = zjPaymentView.batchPaymentQuery(batchNo,mainBody);
            } catch (Exception e) {
                if(e instanceof ChargeOperationException){
                    ChargeOperationException ce = (ChargeOperationException) e;
                    //通讯异常
                    if("2".equals(ce.getCode())){
                        log.error("批扣查询发送出现异常，批扣号为：{}" ,batchNo );
                    }
                }
                e.printStackTrace();
                log.error("批扣查询发送出现异常",e);
            }
            //处理结果
            if(result != null && result.getResultBySelf() != null
                    && result.getResultBySelf() instanceof  List){
                //向下造型
                List<CommonChargeInfo> chargeInfoList = (List<CommonChargeInfo>) result.getResultBySelf();
                if(chargeInfoList != null && (!chargeInfoList.isEmpty())){
                    //更新磁盘数据
                    try {
                        log.debug("批次号：{} 查询到批扣结果条数为：{}" ,batchNo,chargeInfoList.size());
                        //持久化数据
                        chargeInfoPersistenceService.updateBatchChargeDiskInfo(chargeInfoList ,batchNo);
                    } catch (Exception e) {
                        log.error("批扣更新扣款数据出现异常，批扣号为：{}" ,batchNo);
                        e.printStackTrace();
                        log.error("批扣查询接收出现异常",e);
                    }
                }
            }
        }
    }








}
