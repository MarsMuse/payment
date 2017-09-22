package com.zhph.payment.charge.service.impl;

import com.zhph.api.entity.ConstantPay;
import com.zhph.payment.charge.dao.BatchChargeDao;
import com.zhph.payment.charge.entity.CommonChargeInfo;
import com.zhph.payment.charge.service.ChargeInfoPersistenceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Iterator;
import java.util.List;


/**
 *
 * Author: Zou Yao
 * Description: (扣款返回信息持久化服务（包含单扣/批扣发送标志位修改，单扣/批扣扣款数据回写）)
 * Time: 2017/7/25 17:16
 *
**/
@Service
public class ChargeInfoPersistenceServiceImp implements ChargeInfoPersistenceService {

    //日志打印对象
    private Logger log = LoggerFactory.getLogger(ChargeInfoPersistenceServiceImp.class);
    //批扣数据库访问对象
    @Resource
    private BatchChargeDao batchChargeDao;

    @Override
    public Integer verifyBatchNoNeedUpdate(String batchNo) {
        return batchChargeDao.verifyBatchNoNeedUpdate(batchNo);
    }

    /**
     *
     * Author: zou yao
     * Description: {批量扣款发送标志位更新操作}
     * Date: 2017/7/25 17:05
     *
    **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateBatchSendFlag(String batchNo, String sendFlag) throws Exception {
        log.info("扣款返回信息持久化服务--批量扣款发送标志位更新操作");
        //调用将批扣信息置为发送失败
        int updateFlag = batchChargeDao.updateBatchSendFlag(batchNo ,sendFlag);
        //如果发送异常，并且成功更新则直接更新明细
        if("2".equals(sendFlag) && updateFlag == 1){
            int loseCount = batchChargeDao.batchUpdateCommunicationErrorCharge(batchNo);
            log.info("扣款返回信息持久化服务--批扣号: {} 因为发送失败 {} 条扣款记录" ,batchNo , loseCount);
        }
    }


    /**
     *
     * Author: zou yao
     * Description: {批量扣款扣款信息更新操作}
     * Date: 2017/7/25 17:06
     *
    **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateBatchChargeDiskInfo(List<CommonChargeInfo> chargeInfoList, String batchNo)throws Exception {
        log.info("扣款返回信息持久化服务--批量扣款发送标志位更新操作");
        //获取到已完成更新的交易号
        List<String>  finishUpdate = batchChargeDao.getFinishUpdateBatchChargeNo(batchNo);
        //过滤已经更新的数据
        filterFinishUpdateInfo(chargeInfoList , finishUpdate);
        int updateCount = 0 ;
        //更新数据库操作
        for(CommonChargeInfo info : chargeInfoList){
            if(info.getChargeMessage() == null || "".equals(info.getChargeMessage())){
                info.setChargeMessage(ConstantPay.ZJ_NOT_MESSAGE);
            }
            int nowCount = batchChargeDao.updateBatchChargeInfo(info);
            updateCount +=nowCount;
        }
        log.info("扣款返回信息持久化服务--批扣号: {} 完成扣款数据更新了 {} 条扣款记录" ,batchNo , updateCount);
        //更新数据库已更新条数
        if(updateCount>0){
            int replyCount =batchChargeDao.updateBatchBasicInfoReplyCount(batchNo ,updateCount );
            if(replyCount == 1){
                batchChargeDao.updateBatchBasicInfoEndFlag(batchNo);
                log.info("扣款返回信息持久化服务--批扣号: {} 数据已经完全回盘" ,batchNo);
            }
        }
    }

    /**
     *
     * Author: zou yao
     * Description: {验证该交易号是否需要同步数据}
     * Date: 2017/7/25 17:38
     *
     **/
    @Override
    public Integer verifyChargeNoNeedUpdate(String chargeNo) {
        return null;
    }
    /**
     *
     * Author: zou yao
     * Description: {单扣发送标志位更新操作}
     * Date: 2017/7/25 17:39
     *
     **/
    @Override
    public void updateSingleSendFlag(String batchNo, String sendFlag) throws Exception {

    }

    /**
     *
     * Author: zou yao
     * Description: {单扣扣款信息更新操作}
     * Date: 2017/7/25 17:40
     *
     **/
    @Override
    public void updateSingleChargeDiskInfo(CommonChargeInfo chargeInfo) throws Exception {

    }

    /**
     *
     * Author: zou yao
     * Description: {过滤已完成更新数据}
     * Date: 2017/7/25 17:17
     *
    **/
    private  void filterFinishUpdateInfo(List<CommonChargeInfo> chargeInfoList ,List<String>  finishUpdate ){
        //保证过滤与被过滤集合均存在且不为空
        if(chargeInfoList != null && finishUpdate!=null &&
                (!chargeInfoList.isEmpty()) && (!finishUpdate.isEmpty())){
            Iterator<CommonChargeInfo> infoIterator = chargeInfoList.iterator();
            //循环
            while (infoIterator.hasNext()){
                //获取到需要更新的数据
                CommonChargeInfo tempInfo = infoIterator.next();
                //获取到给数据交易号
                String tempChargeNo = tempInfo.getChargeNo();
                //获取到已完成更新交易号的迭代器
                Iterator<String>  chargeNoIterator = finishUpdate.iterator();
                //查看是否能够匹配
                while (chargeNoIterator.hasNext()){
                    String finishChargeNo = chargeNoIterator.next();
                    //如果该交易号已经被更新
                    if(tempChargeNo.equals(finishChargeNo)){
                        //将该交易号弹出去，加快匹配速度
                        chargeNoIterator.remove();
                        //将该信息弹出，无需再更新
                        infoIterator.remove();
                        //跳出内层循环
                        break;
                    }
                }
                if(finishUpdate.size() == 0){
                    //如果已经全部被弹出，直接跳出外层循环
                    break;
                }
            }
        }
    }
}
