package com.zhph.payment.charge.service.impl;


import java.util.List;
import java.util.concurrent.Executor;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.zhph.api.entity.ChargeOperationException;
import com.zhph.api.entity.PlatformCodeAndData;
import com.zhph.api.entity.ResultInformation;
import com.zhph.base.utils.DateUtil;
import com.zhph.base.utils.PushHttpClientUtil;
import com.zhph.entity.ParamsEntity;
import com.zhph.payment.charge.dao.BatchChargeDao;
import com.zhph.payment.charge.dao.SingleChargeDao;
import com.zhph.payment.charge.entity.BatchPushPlatformInfo;
import com.zhph.payment.charge.entity.ChargePushBackInfo;
import com.zhph.payment.charge.entity.ChargeRecordDetail;
import com.zhph.payment.charge.entity.PushBatchChargeInfo;
import com.zhph.payment.charge.entity.SingleChargeInfo;
import com.zhph.payment.charge.service.PlatformSecurityService;
import com.zhph.payment.charge.service.PushChargeInfoService;
import com.zhph.payment.charge.service.business.PaymentService;
import com.zhph.payment.charge.util.BatchChargePushExecutor;

/**
 *
 * Author: Zou Yao
 * Description: (扣款信息推送服务)
 * Time: 2017/7/21 12:58
 *
**/
@Service
@Scope("prototype")//开启多实例  每次调用都开启新的实例
public class PushChargeInfoServiceImp implements PushChargeInfoService {

    //日志打印对象
    private Logger log = LoggerFactory.getLogger(PushChargeInfoServiceImp.class);
    //注入平台安全服务
    @Resource
    private PlatformSecurityService platformSecurityService;
    //注入操作批扣信息数据库访问对象
    @Resource
    private BatchChargeDao batchChargeDao;

    @Resource
    private SingleChargeDao singleChargeDao;
    
    @Resource
    private ChannelBeanServiceImp beanServiceImp;

    /**
     * 定时查询正在扣款中的数据
     *  <p/> 拆分两种情况 1.请求发送金额未拆分。2.请求发送金额已拆分
     * author likang
     * date 2017-7-31下午6:06:41
     */
    public void synQueryPayingData(){
        // select * from zh_charge_record_detail tt where tt.charge_status = 0 and   tt.CHARGE_TYPE = 1  
        List<ChargeRecordDetail>  listPaying = singleChargeDao.getPayingChargeInfo();
        for (int i = 0; listPaying != null &&i < listPaying.size(); i++) {
             ChargeRecordDetail crd = listPaying.get(i);
             PaymentService service = beanServiceImp.getInstance(crd.getCharge_channel_code() ,crd.getMian_body());
             if(service == null){
            	 log.error("找不到bean服务商。。。");
            	 continue;
             }
             ParamsEntity info = new ParamsEntity();
             info.setChargeNo(crd.getCharge_no());
             info.setPlatformCode(crd.getPlatform_code()); 
             info.setLoanNo(crd.getLoan_no());
             info.setMainBody(crd.getMian_body());
             try {
                service.singlePaymentQuery(info);
            } catch (Exception e) {
                e.printStackTrace();
                 log.error("推送异常 ", e);
            }
        }


    }

    /**
     * 单扣推送
     */
    @Override
    public void synPushSingleChargeInfo() {
        log.info("推送服务--单扣服务");
        //获取到需要推送的平台信息
        List<BatchPushPlatformInfo> platformInfoList = singleChargeDao.getNeedPushPlatformInfo();
        for(BatchPushPlatformInfo platformInfo : platformInfoList){
            //获取到
            List<SingleChargeInfo>  pushList = singleChargeDao.getPushChargeInfo(platformInfo);
            //实现异步推送
            if(pushList.size()>0)
                this.asynSinglePushServiceInvoke(this,pushList ,platformInfo);
        }
    }
    
    

    /**
     *
     * Description: {异步调用单扣推送服务}
    **/
    private void asynSinglePushServiceInvoke(final PushChargeInfoService pushChargeInfoService ,
                                                    final List<SingleChargeInfo>  pushList ,
                                                    final BatchPushPlatformInfo platformInfo){
        log.info("推送服务--异步调用单扣推送服务");
        //获取到异步操作
        Executor exe = BatchChargePushExecutor.getExecutor();
        //开启多线程进行推送
        exe.execute(new Runnable() {
            @Override
            public void run() {
                pushChargeInfoService.pushSingleOperation(pushList , platformInfo);
            }
        });
    }
    
    /**
    *
    * Author: zou yao
    * Description: {推送单扣扣款操作,在此处理逻辑推送}
    * Date: 2017/7/21 13:33
    *
   **/
   @Override
   public synchronized void pushSingleOperation(List<SingleChargeInfo> pushList, BatchPushPlatformInfo platformInfo) {
       String sourceData = JSON.toJSONString(pushList); //明文
       log.info("推送平台￥￥￥￥￥￥￥￥￥￥￥￥￥￥￥￥￥￥"+platformInfo.getPlatformCode());
       //获取到返回结果
       String result = null;
       try {
           result = pushDataEncryptAndDecrypt(sourceData ,platformInfo );
       }catch (Exception e) {
           log.error("推送异常 ", e);
           if(e instanceof ChargeOperationException){
               log.error("更新推送错误次数开始！！！{}平台",platformInfo.getPlatformCode());
               this.singleChargeDao.updatePushErrorInfo(pushList); //记录失败次数
               log.error("更新推送错误次数结束！！！{}平台",platformInfo.getPlatformCode());
           }
           log.error(platformInfo.getPlatformCode()+"平台，请求失败"+e.getMessage());
       }finally {
           log.info(DateUtil.getNowTime()+"平台{}@@@@,推送返回业务平台接口返回数据{}",platformInfo.getPlatformCode(),result);
           if(result != null ){
               List<ChargePushBackInfo>  pushBackInfo = JSON.parseArray(result ,ChargePushBackInfo.class );  //转换为集合
               if(pushBackInfo!= null && (!pushBackInfo.isEmpty())){
                   log.info("转换数据正常，平台{}推送返回数据更新数据！！！，返回结果{}",platformInfo.getPlatformCode(),result);
                   this.singleChargeDao.updateSinglePushInfo(pushBackInfo); //批量更新
               }else{
                   log.error("转换数据为空！！！{}平台，返回结果{}",platformInfo.getPlatformCode(),result);
               }
           }else{
               log.error("未检查到返回数据！！！{}平台，返回结果{}",platformInfo.getPlatformCode(),result);
           }
       }
   }



    /**
     *
     * Author: zou yao
     * Description: {推送批扣信息操作}
     * Date: 2017/7/21 13:08
     *
    **/
    @Override
    public void asynPushBatchChargeInfo() {
        log.info("推送服务--推送批扣信息操作");
        //获取到需要推送的平台信息
        List<BatchPushPlatformInfo> platformInfoList = this.batchChargeDao.getNeedPushPlatformInfo();
        for(BatchPushPlatformInfo platformInfo : platformInfoList){
            //获取到
            List<PushBatchChargeInfo>  pushList = this.batchChargeDao.getPushChargeInfo(platformInfo);
            //实现异步推送
            this.asynchronousInvokeBatchPushService(this,pushList ,platformInfo);
        }
    }


    /**
     *
     * Author: zou yao
     * Description: {同步推送批量扣款信息}
     * Date: 2017/7/21 13:31
     *
    **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void synPushBatchChargeInfo() {
        log.info("推送服务--同步推送批量扣款信息");
        //获取到需要推送的平台信息
        List<BatchPushPlatformInfo> platformInfoList = this.batchChargeDao.getNeedPushPlatformInfo();
        for(BatchPushPlatformInfo platformInfo : platformInfoList){
            //获取到
            List<PushBatchChargeInfo>  pushList = this.batchChargeDao.getPushChargeInfo(platformInfo);
            this.pushBatchOperation(pushList , platformInfo);
        }
    }


    /**
     *
     * Author: zou yao
     * Description: {推送批量扣款操作,在此处理逻辑推送}
     * Date: 2017/7/21 13:33
     *
    **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public  void pushBatchOperation(List<PushBatchChargeInfo> pushList, BatchPushPlatformInfo platformInfo) {
        log.info("推送服务--推送批量扣款操作,在此处理逻辑推送");
        //明文
        String sourceData = JSON.toJSONString(pushList);
        //获取到返回结果
        String result = null;
        try {
            result = pushDataEncryptAndDecrypt(sourceData ,platformInfo );
        }catch (Exception e) {
            if(e instanceof ChargeOperationException){
                //记录失败次数
                this.batchChargeDao.updatePushErrorInfo(pushList);
                log.error("推送批量扣款信息出现异常！，已将失败记录更新至数据库");
                log.error("推送异常 ", e);
            }
            e.printStackTrace();
        }
        if(result != null ){
            //转换为集合
            List<ChargePushBackInfo>  pushBackInfo = JSON.parseArray(result ,ChargePushBackInfo.class );
            if(pushBackInfo!= null && (!pushBackInfo.isEmpty())){
                //批量更新
                batchChargeDao.updateBatchPushInfo(pushBackInfo);
                log.info("推送批量扣款信息成功！，已将成功更新至数据库");
            }
        }
    }


    /**
     *
     * Author: zou yao
     * Description: {推送功能数据发送加密与接收返回信息解密}
     * Date: 2017/7/26 18:28
     *
    **/
    @Override
    public String pushDataEncryptAndDecrypt(String sourceData , BatchPushPlatformInfo platformInfo) throws Exception{

        String content = null;
        String cipherData = null;
        try {
            cipherData = platformSecurityService.encryptPushInfo(sourceData , platformInfo.getCertificatePath() ,platformInfo.getCertificateName());
        } catch (Exception e) {
            e.printStackTrace();
            log.error("推送异常 ", e);
            log.error("平台代码：{}，在扣款数据推送数据加密出现异常，业务平台批扣号{}",platformInfo.getPlatformCode() , platformInfo.getWorkBatchNo());
        }
        //获取到返回结果
        String result = sendMessageToWorkPlatform(cipherData ,platformInfo.getCallBackPath());
        //验证数据有效性
        if(result != null ){
            ResultInformation backInfo = null;
            try {
                backInfo = platformSecurityService.verifyRequestInfo(result);
            } catch (Exception e) {
                e.printStackTrace();
                log.error("推送异常 ", e);
                log.error("平台代码：{}，在扣款数据推送返回数据验证出现异常，业务平台批扣号{},",platformInfo.getPlatformCode() , platformInfo.getWorkBatchNo());
            }
            if(backInfo != null && "1".equals(backInfo.getCode())){
                String platform = backInfo.getMessage();
                PlatformCodeAndData data = JSON.parseObject(platform , PlatformCodeAndData.class);
                content = data.getData();
            }
            else{
                log.error("平台代码：{}，在扣款数据推送返回数据验证出现异常，业务平台号{}",platformInfo.getPlatformCode() , platformInfo.getWorkBatchNo());
            }
        }

        return content;
    }

     

    /**
     *
     * Author: zou yao
     * Description: {发送数据至业务平台}
     * Date: 2017/7/21 13:44
     *
    **/
    @SuppressWarnings("deprecation")
    @Override
    public String sendMessageToWorkPlatform(String cipher, String callBackPath) throws Exception{

        if(cipher == null || "".equals(cipher.trim()) || callBackPath==null || "".equals(callBackPath.trim())){
            throw new ChargeOperationException("内容异常");
        }
        log.info("推送地址为{} ", callBackPath);
        String resultContent ;
        try {
            resultContent = PushHttpClientUtil.httpPost(cipher , callBackPath);
        }catch (Exception e){
            log.error("推送出现异常",e);
            if(e instanceof ChargeOperationException){
                throw e;
            }else{
                throw new ChargeOperationException("推送出现连接异常");
            }
        }
        return resultContent;
    }


    /**
     *
     * Author: zou yao
     * Description: {异步调用批量推送服务}
     * Date: 2017/7/21 13:12
     *
    **/
    private void asynchronousInvokeBatchPushService(final PushChargeInfoService pushChargeInfoService ,
                                                    final List<PushBatchChargeInfo>  pushList ,
                                                    final BatchPushPlatformInfo platformInfo){
        //获取到异步操作
        Executor exe = BatchChargePushExecutor.getExecutor();
        //开启多线程进行推送
        exe.execute(new Runnable() {
            @Override
            public void run() {
                pushChargeInfoService.pushBatchOperation(pushList , platformInfo);
            }
        });
    }
}
