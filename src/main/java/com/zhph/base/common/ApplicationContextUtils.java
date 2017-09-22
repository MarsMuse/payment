package com.zhph.base.common;

import java.util.Properties;

import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @Description:
 */
public class ApplicationContextUtils implements ApplicationContextAware {
    /**
     * 以静态变量保存ApplicationContext,可在任意代码中取出ApplicaitonContext.
     */
    private static ApplicationContext context;

    /**
     * 实现ApplicationContextAware接口的context注入函数, 将其存入静态变量.
     */
    @Override
    public void setApplicationContext(ApplicationContext context) {
        ApplicationContextUtils.context = context;
    }

    public static ApplicationContext getApplicationContext() {
        return context;
    }

    /**
     * 取spring 注册器
     * @return
     */
    public static DefaultListableBeanFactory  getBeanDefinitionRegistry(){
        ConfigurableApplicationContext configurableApplicationContext =  (ConfigurableApplicationContext)context;
        return (DefaultListableBeanFactory)configurableApplicationContext.getBeanFactory();
    }

    /**
     * 从静态变量ApplicationContext中取得Bean, 自动转型为所赋值对象的类型.
     */
    public static Object getBean(String name){
        return context.getBean(name);
    }

    /**
     * 读取属性文件
     */
    public static String getProperty(String name){
        Properties p = (Properties) context.getBean("configProperties");
        return p.getProperty(name);
    }
}
