package com.zhph.base.utils;


import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 *
 * Author: Zou Yao
 * Description: (推送信息http客户端，利用连接池工具)
 * Time: 2017/7/28 13:25
 *
**/
public class PushHttpClientUtil {

    //最大连接
    private static final int MAX_TOTAL = 1024;
    //单个路由默认启动链路数量
    private static final int MAX_ROUTE_DEF = 256;
    //默认连接超时
    private static final int TIMEOUT_DEF  = 60*1000;
    //http连接管理对象
    private static CloseableHttpClient httpClient;
    //连接池管理
    private static PoolingHttpClientConnectionManager connectionManager;
    //清理连接定时
    private static final int CLEAN_CONNECTION_TIMER = 60;
    //静态代码块初始化HttpClient与HttpClientConnectionManager
    static{
        init();
    }


    /**
     *
     * Author: zou yao
     * Description: {HttpPOST请求方式获取到值}
     * Date: 2017/7/28 13:49
     *
    **/
    public static String httpPost(String data , String url) throws  IOException{
        if(url == null || "".equals(url.trim())){
            return null;
        }
        String result;
        CloseableHttpResponse resPost = null;
        try{
            HttpPost  post = new HttpPost(url);
            HttpEntity content = new StringEntity(data, ContentType.APPLICATION_JSON);
            post.setEntity(content);
            resPost = httpClient.execute(post);
            HttpEntity entity = resPost.getEntity();
            result = EntityUtils.toString(entity );
        }finally{
            if(resPost != null){
                resPost.close();
            }
        }
        return result;
    }



    /**
     *
     * Author: zou yao
     * Description: {定时清理空闲连接}
     * Date: 2017/7/28 13:28
     *
    **/
    private static void closeConnectionTask(){
        new Thread(new Runnable() {

            @Override
            public void run() {
                while(!Thread.currentThread().isInterrupted()){
                    try {
                        TimeUnit.SECONDS.sleep(CLEAN_CONNECTION_TIMER);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    connectionManager.closeExpiredConnections();
                }
            }
        }).start();
    }


    /**
     *
     * Author: zou yao
     * Description: {初始化入口}
     * Date: 2017/7/28 13:29
     *
    **/
    private static void init(){
        //获取到连接管理工具
        connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setMaxTotal(MAX_TOTAL);
        connectionManager.setDefaultMaxPerRoute(MAX_ROUTE_DEF);
        final RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(TIMEOUT_DEF)
                .setConnectTimeout(TIMEOUT_DEF).setConnectionRequestTimeout(TIMEOUT_DEF).build();
        //获取到客户端
        httpClient = HttpClients.custom().setConnectionManager(connectionManager).setDefaultRequestConfig(requestConfig).build();
        //启动定时器清理连接
        closeConnectionTask();
    }


}
