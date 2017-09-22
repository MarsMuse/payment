package com.zhph.payment.charge.util;


import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 *
 * Author: Zou Yao
 * Description: (批扣处理线程管理类，终态类)
 * Time: 2017/7/20 10:09
 *
**/
public final class  BatchChargeExecutor {

    //最大线程数
    private static final int MAXIMUM_POOL_SIZE = 100;


    /**
     *
     * Author: zou yao
     * Description: {不被外界实例化对象}
     * Date: 2017/7/20 10:30
     *
    **/
    private BatchChargeExecutor(){
        throw new RuntimeException("此类不可调用构造器");
    }

    
    
    /**
     *
     * Author: Zou Yao
     * Description: (静态内部类实现懒加载保证单例线程对象)
     * Time: 2017/7/20 10:52
     *
    **/ 
    private static class ExecutorManager{
        //维持一个线程类
        private static Executor exe  = Executors.newFixedThreadPool(MAXIMUM_POOL_SIZE);
        
    }
    
    
    
    /**
     *
     * Author: zou yao
     * Description: {获取到内部线程池实例}
     * Date: 2017/7/20 10:52
     * 
    **/
    public static Executor getExecutor(){
        return ExecutorManager.exe;
    }
}
