package com.zhph.payment.charge.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.zhph.base.common.ApplicationContextUtils;
import com.zhph.payment.charge.dao.ChannelBeanDao;
import com.zhph.payment.charge.service.ChannelBeanService;
import com.zhph.payment.charge.service.business.PaymentService;


/**
 *
 * @Author: Zou Yao
 * @Description: (获取到渠道服务Bean)
 * @Time: 2017/7/19 13:42
 *
**/
@Service
public class ChannelBeanServiceImp implements ChannelBeanService {

    //注入渠道Bean获取类
    @Resource
    private ChannelBeanDao channelBeanDao;
    //渠道Bean Map
    private Map<String , String> channelInfo;
    //日志打印对象
    private Logger log = LoggerFactory.getLogger(ChannelBeanServiceImp.class);

    public ChannelBeanServiceImp() {
        channelInfo = new HashMap<String , String>();
    }

    /**
     *
     * @Author: zou yao
     * @Description: {获取到服务Bean}
     * @Date: 2017/7/19 13:43
     * @Param:
     *
    **/
    @Override
    public PaymentService getInstance(String channelNo ,String mainBody) {
        log.info("获取支付渠道服务Bean");
        //渠道服务接口
        PaymentService  paymentService = null;
        try {
            if (channelNo == null || "".equals(channelNo) || mainBody == null || "".equals(mainBody)) {
                return paymentService;
            }
            //如果这个Key存在着需要去更新这个Map
            if (!channelInfo.containsKey(channelNo+"-"+mainBody)) {
                this.updateChannelInfo(channelNo , mainBody);
            }

            String beanName = channelInfo.get(channelNo+"-"+mainBody);
            log.debug("渠道号：{}主体：{} 在本地缓存中获取到服务Bean：{}", channelNo, mainBody ,beanName);
            //如果存在返回值则创建相应对象
            if (beanName != null && (!"".equals(beanName))) {
                paymentService = (PaymentService) ApplicationContextUtils.getBean(beanName);
            }
        }catch (Exception e){
            log.error("获取支付渠道对象Bean出现异常 渠道编号为：{},主体为:{}",channelNo , mainBody);
            log.error("获取支付渠道对象Bean出现异常",e);
            e.printStackTrace();
        }
        return paymentService;
    }


    /**
     *
     * @Author: zou yao
     * @Description: {更新这个Map}
     * @Date: 2017/7/19 13:55
     * @Param:
     *
    **/
    private synchronized void updateChannelInfo(String channelNo ,String mainBody) throws  Exception {
        try {
            //存在被其他线程更新的情况
            if (!channelInfo.containsKey(channelNo + "-" + mainBody)) {
                log.info("更新服务Bean到本地缓存中");
                //获取到Bean信息
                List<String> beanList = this.channelBeanDao.getBeanNameByChannelNo(channelNo, mainBody);
                if (beanList != null && (!beanList.isEmpty())) {
                    channelInfo.put(channelNo + "-" + mainBody, beanList.get(0));
                    log.info("渠道号：{}主体：{} 更新服务Bean:{} 到本地缓存中", channelNo, mainBody, beanList.get(0));
                }else{
                	 log.error("渠道号：{}主体：{} 更新服务Bean:{} 到本地缓存中未找到，请查看数据库zh_channel_basic_info表是否启用");
                }
            }
        }catch (Exception e){
            log.error("渠道号：{}主体：{} 更新服务Bean到本地缓存中出现异常", channelNo, mainBody);
            log.error("更新服务Bean到本地缓存中出现异常",e);
            throw  e;

        }
    }


}
