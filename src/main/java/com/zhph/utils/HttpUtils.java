package com.zhph.utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import com.zhph.api.entity.TransInformation;
import com.zhph.base.encrypt.entity.InformationSecurityEntity;
import com.zhph.base.encrypt.util.InformationSecurityUtil;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSON;

/**
 * 
 * @ClassName:  HttpUtils   
 * @Description:(调用Apache 的http组件实现在服务器模拟浏览器发起http请求 ，并且获取到响应)   
 * @author: mars
 * @date:   2017年4月28日 下午5:40:36   
 *     
 * @Copyright: 2017 
 *
 */
public class HttpUtils {
    public static void main(String[] args) {
        CloseableHttpClient httpClient= HttpClients.createDefault(); 
        HttpPost  httpPost  = new HttpPost("http://127.0.0.1:8080/payment/ws/v1/chargeDealService/batchCharge");
        
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        
        System.out.println("请求参数："+nvps.toString());  
          
        //设置header信息  
        //指定报文头【Content-type】、【User-Agent】  
        httpPost.setHeader("Content-type", "application/x-www-form-urlencoded");  
        httpPost.setHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
        String  info = "这条就是我需要加密的信息------"; 
        
        String pri="E:/testKey/dev/payment_dev_pri.pfx"; 
        String password= "abc123456";
        String pub = "E:/testKey/payment/payment_pub.cer";
        
        InformationSecurityEntity ise = InformationSecurityUtil.encryptInformation(info, pub, pri, password);
        TransInformation ti = new TransInformation("CS007", "C001" ,ise.getEncryptKey(), ise.getCipherData(), ise.getPlatformSignature());
        String content = JSON.toJSONString(ti);


        httpPost.setEntity(new StringEntity(content,"UTF-8"));

        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(httpPost);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        HttpEntity httpEntity= response.getEntity();  
        try {
            String strResult= EntityUtils.toString(httpEntity);
            System.out.println(strResult);
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
