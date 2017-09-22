package com.zhph.payment.charge.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executor;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.zhph.api.entity.ChargeOperationException;
import com.zhph.api.entity.PlatformCodeAndData;
import com.zhph.api.entity.ResultInformation;
import com.zhph.entity.ParamsEntity;
import com.zhph.payment.charge.dao.BatchChargeDao;
import com.zhph.payment.charge.dao.ChannelBeanDao;
import com.zhph.payment.charge.entity.BatchChargeBasicInfo;
import com.zhph.payment.charge.entity.ChargeDetailInfo;
import com.zhph.payment.charge.service.BatchChargeService;
import com.zhph.payment.charge.service.ChannelBeanService;
import com.zhph.payment.charge.service.business.PaymentService;
import com.zhph.payment.charge.util.BatchChargeExecutor;
import com.zhph.payment.charge.util.ChargeDataVerify;
import com.zhph.payment.charge.util.UUIDUtil;


/**
 *
 * Author: Zou Yao
 * Description: (支付平台批扣服务实现)
 * Time: 2017/7/19 11:14
 *
**/
@Service
public class BatchChargeServiceImp implements BatchChargeService {



    //日志打印对象
    private final Logger log = LoggerFactory.getLogger(BatchChargeServiceImp.class);

    //注入渠道Bean获取服务
    @Resource
    private ChannelBeanService channelBeanService;

    //注入渠道信息获取Dao
    @Resource
    private ChannelBeanDao channelBeanDao;

    //注入操作批扣信息数据库访问对象
    @Resource
    private BatchChargeDao batchChargeDao;
    /**
     *
     * Author: zou yao
     * Description: {批量扣款操作接口}
     * Date: 2017/7/19 11:14
     * Param:
     *
    **/
    @Transactional(rollbackFor = Exception.class)
    public ResultInformation batchChargeOperation(String content) throws  Exception{
        //预设返回结果
        ResultInformation  resultInfo = new ResultInformation("1" ,"请求成功");
        //获取到平台信息及数据
        PlatformCodeAndData pcd  = JSON.parseObject(content , PlatformCodeAndData.class);

        //获取到扣款渠道
        PaymentService paymentService = channelBeanService.getInstance(pcd.getChannelNo() ,pcd.getMainBody());
        //如果为获取到服务
        if(paymentService == null) {
            log.error("批扣--该渠道服务暂未启用");
            throw new  ChargeOperationException("2","该渠道服务暂未启用" );
        }

        //获取到该渠道的与该主体批扣限制信息
        List<Integer>  limitInfo = this.channelBeanDao.getChannelBatchLimit(pcd.getChannelNo() , pcd.getMainBody());
        if(limitInfo == null || limitInfo.isEmpty()){
            log.error("批扣--该渠道服务暂未启用");
            throw new  ChargeOperationException("2","该渠道服务暂未启用" );
        }
        //获取到限制金额
        int batchLimit = limitInfo.get(0);
        //获取到扣款信息
        List<ParamsEntity>  chargeDetailInfoList;
        try {
            chargeDetailInfoList = JSON.parseArray(pcd.getData(), ParamsEntity.class);
        }catch (Exception e){
            log.error("批扣--扣款基本信息异常",e);
            throw new  ChargeOperationException("2","数据异常" );
        }
        //未获取到数据
        if(chargeDetailInfoList == null || chargeDetailInfoList.isEmpty() ){
            log.error("批扣--数据异常--未获取到扣款数据");
            throw new ChargeOperationException("2", "数据异常");
        }
        //有效数据
        List<ChargeDetailInfo> validList = new ArrayList<>();
        //无效数据
        List<ChargeDetailInfo> invalidList = new ArrayList<>();
        try {
            //将原始数据拆分有效数据与无效数据
            this.splitChargeData(chargeDetailInfoList, validList, invalidList);
        }catch (Exception e){
            log.error("批扣--数据异常--数据拆分异常");
            throw new  ChargeOperationException("2","数据拆分异常" );
        }
        //创建一个批扣基础信息对象
        BatchChargeBasicInfo basicInfo = new BatchChargeBasicInfo(pcd.getPlatformCode() ,UUIDUtil.getUUID(), pcd.getChannelNo());
        //设置主体（存储的时候需要主体）
        basicInfo.setMainBody(pcd.getMainBody());
        try {
            //持久化数据并异步调用扣款服务
            this.saveBatchInfoAndSendToChannel(validList, invalidList, basicInfo ,paymentService,batchLimit,pcd.getPlatformCode());
            //获取到业务平台批扣码
            resultInfo.setResultNo(basicInfo.getWorkBatchNo());
        }catch (Exception e){
            log.error("批扣--持久化数据失败",e);
            if(e instanceof ChargeOperationException ){
                throw  e;
            }else{
                throw new  ChargeOperationException("2","持久化数据失败" );
            }
        }

        return resultInfo;
    }


    /**
     *
     * Author: zou yao
     * Description: {批量扣款数据同步服务}
     * Date: 2017/7/28 14:11
     *
    **/
    @Override
    public void batchChargeDataSynchronize() {
        //获取到需要同步的服务的数据
        List<BatchChargeBasicInfo>   synBatchChargeList = batchChargeDao.getNeedSynchronizedBatchInfo();
        log.info("批扣--批量扣款数据同步服务被调用");
        if(synBatchChargeList != null && (!synBatchChargeList.isEmpty())) {
            log.info("批扣--批量扣款数据同步服务被调用--需要同步的批扣数量 {} 条批扣记录" , synBatchChargeList.size());
            for (BatchChargeBasicInfo basicInfo : synBatchChargeList) {
                //获取到扣款渠道
                PaymentService paymentService = channelBeanService.getInstance(basicInfo.getChannelNo() , basicInfo.getMainBody());
                //如果为获取到服务
                if (paymentService != null) {
                    //循环调用数据同步服务
                    asynInvokeBatchChargeInfoSyn(paymentService ,basicInfo.getBatchNo() , basicInfo.getMainBody());
                }
            }
        }
    }


    /**
     *
     * Author: zou yao
     * Description: {异步启用批扣数据同步服务}
     * Date: 2017/7/28 14:26
     *
    **/
    private void asynInvokeBatchChargeInfoSyn(final PaymentService paymentService , final String batchNo ,final String mainBody){
        //获取到线程池对象
        Executor executor = BatchChargeExecutor.getExecutor();
        log.info("批扣--数据同步--调用异步服务--批扣号{} , 主体 {}" , batchNo ,mainBody );
        //新建一个线程执行异步任务
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    paymentService.batchPaymentQuery(batchNo ,mainBody);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    /**
     *
     * Author: zou yao
     * Description: {将数据持久化到数据库中,启用事务处理}
     * Date: 2017/7/20 12:29
     * param platformCode
     *
    **/
    private void saveBatchInfoAndSendToChannel(List<ChargeDetailInfo> validList, List<ChargeDetailInfo> invalidList,  BatchChargeBasicInfo basicInfo, PaymentService paymentService, int batchLimit, String platformCode){
        log.info("批扣--平台号 {} ,开始持久化数据");
        //如果无数据则不需要
        if(validList.isEmpty() && invalidList.isEmpty()){
            log.error("批扣--无数据需要持久化");
            throw new  ChargeOperationException("2","无数据需要持久化" );
        }
        //如果无效数据不为空
        if(!invalidList.isEmpty()){
            //存入无效数据
            this.saveInvalidList(invalidList,basicInfo);
        }
        //如果不限制
        if(batchLimit != -1) {
            //拆分集合后存储数据并且异步调用服务
            if (!validList.isEmpty()) {
                //创建一个连接批次号与数据的Map
                Map<String , List<ChargeDetailInfo>> loopBatchMap = new HashMap<>();
                //获取到循环次数
                int countLoop = (validList.size() - 1) / batchLimit + 1;
                for (int i = 0; i < countLoop; i++) {
                    //获取到开始索引
                    int startIndex = i * batchLimit;
                    //获取到结束索引
                    int endIndex = (i+1) * batchLimit > validList.size() ? validList.size() : (i+1) * batchLimit;
                    //拆分集合
                    List<ChargeDetailInfo> batchChargeData = validList.subList(startIndex, endIndex);
                    //存储有效数据
                    this.saveValidList(batchChargeData ,basicInfo);
                    //将拆分后的批次号与数据连接起来
                    loopBatchMap.put(basicInfo.getBatchNo(), batchChargeData);
                }
                //获取到Map的Key
                Set<String> loopBatchNoSet = loopBatchMap.keySet();
                //获取到迭代器做循环
                for (String nowBatchNo : loopBatchNoSet) {
                    //获取到此次循环的Key
                    //获取到此次循环的集合
                    List<ChargeDetailInfo> nowChargeData = loopBatchMap.get(nowBatchNo);
                    //异步调用服务
                    this.asynchronousInvokeService(paymentService, nowChargeData, nowBatchNo,platformCode ,basicInfo.getMainBody());
                }
            }
        }else{
            //一次性存入所有数据
            if (!validList.isEmpty()) {
                this.saveValidList(validList ,basicInfo);
                //异步调用服务
                this.asynchronousInvokeService(paymentService, validList, basicInfo.getBatchNo(),platformCode,basicInfo.getMainBody());
            }
        }
    }



    /**
     *
     * Author: zou yao
     * Description: {存储有效数据}
     * Date: 2017/7/25 10:38
     *
    **/
    private void saveValidList(List<ChargeDetailInfo> validList ,BatchChargeBasicInfo basicInfo){
        log.info("批扣--开始持久化--有效-批扣数据数据");
        int batchValidFlag = 0;
        basicInfo.setSendInfoCount(validList.size());
        //首先插入批次信息到数据库中存储无效数据
        while (batchValidFlag != 1) {
            basicInfo.setBatchNo(this.getUniqueBatchNo());
            //查看是否插入
            batchValidFlag = batchChargeDao.insertBatchBasicInfo(basicInfo);
        }
        //直接存储信息
        this.batchChargeDao.insertValidBatchChargeInfo(validList, basicInfo);
    }


    /**
     *
     * Author: zou yao
     * Description: {存储无效数据}
     * Date: 2017/7/25 10:40
     *
    **/
    private void saveInvalidList(List<ChargeDetailInfo> invalidList ,BatchChargeBasicInfo basicInfo){
        log.info("批扣--开始持久化--无效--批扣数据数据");
        int batchValidFlag = 0 ;
        basicInfo.setSendInfoCount(invalidList.size());
        //首先插入批次信息到数据库中存储无效数据
        while(batchValidFlag != 1){
            basicInfo.setBatchNo(this.getUniqueBatchNo());
            //查看是否插入
            batchValidFlag = batchChargeDao.insertInvalidBatchBasicInfo(basicInfo);
        }
        //成功插入批次号，插入无效数据
        this.batchChargeDao.insertInvalidBatchChargeInfo(invalidList , basicInfo);
    }



    /**
     *
     * Author: zou yao
     * Description: {异步调用扣款服务}
     * Date: 2017/7/20 12:31
     * param platformCode
     *
    **/
    private void asynchronousInvokeService(final PaymentService paymentService , final List<ChargeDetailInfo> chargeData ,final String batchNo,final String platformCode ,final String mainBody){
        log.info("批扣--异步调用扣款服务");
        //获取到线程池对象
        Executor executor = BatchChargeExecutor.getExecutor();

        //新建一个线程执行异步任务
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    paymentService.batchPayment(chargeData ,batchNo,platformCode ,mainBody);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     *
     * Author: zou yao
     * Description: {获取到唯一的批次号}
     * Date: 2017/7/20 14:22
     *
     **/
    private String getUniqueBatchNo(){
        return System.currentTimeMillis()+"";
    }



    /**
     *
     * Author: zou yao
     * Description: {将原始数据拆分有效数据与无效数据}
     * Date: 2017/7/19 17:00
     *
    **/
    private void splitChargeData(List<ParamsEntity> sourceList ,List<ChargeDetailInfo> validList ,List<ChargeDetailInfo> inValidList) throws InterruptedException {
        log.info("批扣--将原始数据拆分有效数据与无效数据");
        //List不可为null
        if(sourceList != null && validList != null && inValidList !=null){
            Iterator<ParamsEntity>  chargeInfoIterator = sourceList.iterator();
            while(chargeInfoIterator.hasNext()){
                //获取到信息
                ChargeDetailInfo sourceInfo = chargeInfoIterator.next();
                Thread.sleep(1);
                //设置扣款编号（合同号加上当前时间时间戳后12位）
                String chargeNo = sourceInfo.getLoanNo().trim() + String.valueOf(System.currentTimeMillis()).substring(1);
                //设置合同号
                sourceInfo.setChargeNo(chargeNo);
                //做数据有效检验
                if(ChargeDataVerify.verifyData(sourceInfo)){
                    validList.add(sourceInfo);
                }else{
                    inValidList.add(sourceInfo);
                }
                //释放
                chargeInfoIterator.remove();
            }
        }
    }

}
