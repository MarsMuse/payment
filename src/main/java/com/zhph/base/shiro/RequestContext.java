package com.zhph.base.shiro;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 * Author: Zou Yao
 * Description: (将请求上下文环境与线程绑定，将request与后台服务层实现解耦操作)
 * Time: 2017/8/2 10:53
 *
**/ 
public class RequestContext {
    //
    private final static ThreadLocal<HttpServletRequest>  CONTEXT = new ThreadLocal<HttpServletRequest>();



    /**
     *
     * Author: zou yao
     * Description: {绑定Request}
     * Date: 2017/8/3 9:08
     *
    **/
    public static void setRequest(HttpServletRequest request){
        CONTEXT.set(request);
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
     * Description: {获取发到Request}
     * Date: 2017/8/3 9:09
     *
    **/
    public static  HttpServletRequest getRequest(){
        return CONTEXT.get();
    }



    /**
     *
     * Author: zou yao
     * Description: {获取到Session}
     * Date: 2017/8/3 9:09
     *
    **/
    public static HttpSession getSession(){
        return CONTEXT.get() == null ? null : CONTEXT.get().getSession();
    }
}
