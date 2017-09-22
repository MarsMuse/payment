package com.zhph.base.listener;

import org.springframework.web.util.WebUtils;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.File;


/**
 *
 * Author: Zou Yao
 * Description: (应用上下文监听)
 * Time: 2017/7/18 10:42
 *
**/
public class WebContextListener implements ServletContextListener {

    private static String webRoot = "";
    private static String classRoot = "";
    private static final String FORWARD_SLASH="/";

    /**
     *
     * Author: zou yao
     * Description: {启动监听调用方法}
     * Date: 2017/7/18 10:41
     * Param: No such property: code for class: Script1
     *
    **/
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        //0：linux 1:windows
        int osFlag = 0;
        ServletContext  sc  =  sce.getServletContext();
        if(FORWARD_SLASH.equals(File.separator)){
            sc.log("服务器为linux");
        }else{
            osFlag = 1;
            sc.log("服务器为windows");
        }
        sc.log("应用上下文监听器开始启动");
        String appRootKey  = sc.getInitParameter(WebUtils.WEB_APP_ROOT_KEY_PARAM);
        appRootKey =  appRootKey==null? WebUtils.DEFAULT_WEB_APP_ROOT_KEY:appRootKey;
        sc.log("获取Web根目录的文件系统路径，其存储在系统属性中的Key为【" + appRootKey + "】");
        webRoot = System.getProperty(appRootKey);
        sc.log("获取Web根目录的文件系统路径，其存储在系统属性中的Value为【" + webRoot + "】");
        classRoot = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        //如果是linux则需要加入斜杠
        if(osFlag ==0){
            if(!classRoot.startsWith(FORWARD_SLASH)){
                classRoot =FORWARD_SLASH+classRoot;
            }
            if(!webRoot.startsWith(FORWARD_SLASH)){
                webRoot =FORWARD_SLASH+webRoot;
            }
        }
    }



    /**
     *
     * Author: zou yao
     * Description: {移除监听方法 }
     * Date: 2017/7/18 10:42
     * Param: No such property: code for class: Script1
     *
    **/
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        ServletContext sc = sce.getServletContext();
        sc.log("移除系统属性中缓存的Web根目录文件系统路径。");
        WebUtils.removeWebAppRootSystemProperty(sc);
        sc.log("应用上下文监听器注销。");
    }



    /**
     *
     * Author: zou yao
     * Description: {获取到项目根目录 注：结尾带“/”}
     * Date: 2017/7/18 10:13
     * Param: No such property: code for class: Script1
     *
    **/
    public static String getWebRoot(){

        return webRoot;
    }


    /**
     *
     * Author: zou yao
     * Description: {获取到类路径 注：结尾带“/”}
     * Date: 2017/7/18 11:16
     * Param: No such property: code for class: Script1
     *
    **/
    public static String getClassRoot(){

        return classRoot;
    }
}
