package com.zhph.channel.listener;

import com.zhph.base.common.ApplicationContextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import payment.api.system.PaymentEnvironment;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.File;


/**
 *
 * Author: Zou Yao
 * Description: (中金配置文件读取监听器)
 * Time: 2017/7/24 11:26
 *
**/
public class ZjContextLoaderListener implements ServletContextListener {

    //日志输出对象
    private  Logger logger = LoggerFactory.getLogger(ZjContextLoaderListener.class);



    /**
     *
     * Author: zou yao
     * Description: {初始化中金监听器}
     * Date: 2017/7/24 11:30
     *
    **/
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        logger.debug("开始启动中金监听器，读取中金配置文件。。。");
        try {
            String paymentConfigPath = sce.getServletContext().getInitParameter("zj.config.path");
            String keyPath = ApplicationContextUtils.getProperty("ZJ.KEY.PATH");
            String projectPath = ZjContextLoaderListener.class.getClassLoader().getResource("").getPath();
            PaymentEnvironment.initialize(projectPath + paymentConfigPath + File.separator + keyPath);
        }catch (Exception e){
            logger.debug("读取中金配置文件出现异常。。。",e);
            e.printStackTrace();
        }
    }


    /**
     *
     * Author: zou yao
     * Description: {注销中金监听器}
     * Date: 2017/7/24 11:30
     *
    **/
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        logger.debug("开始注销中金监听器，读取中金配置文件。。。");
    }
}
