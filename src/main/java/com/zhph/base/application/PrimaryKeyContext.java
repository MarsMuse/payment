package com.zhph.base.application;


/**
 *
 * Author: Zou Yao
 * Description: (主键与线程的绑定)
 * Time: 2017/8/4 11:09
 *
**/
public class PrimaryKeyContext {
    //线程与主键bind容器
    private final static ThreadLocal<String>  CONTEXT = new ThreadLocal<>();



    /**
     *
     * Author: zou yao
     * Description: {绑定主键字符串}
     * Date: 2017/8/3 9:08
     *
     **/
    public static void setPrimaryKey(String primaryKey){
        CONTEXT.set(primaryKey);
    }


    /**
     *
     * Author: zou yao
     * Description: {取消绑定}
     * Date: 2017/8/3 9:08
     *
     **/
    public static void clear(){
        CONTEXT.remove();
    }


    /**
     *
     * Author: zou yao
     * Description: {获取主键字符串}
     * Date: 2017/8/3 9:09
     *
     **/
    public static  String getPrimaryKey(){
        return CONTEXT.get();
    }

}
