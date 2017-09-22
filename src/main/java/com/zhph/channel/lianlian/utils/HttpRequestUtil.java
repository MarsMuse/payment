package com.zhph.channel.lianlian.utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;


/**
 * Created by liuping on 16/3/28.
 */
public class HttpRequestUtil {

	private static Logger log = Logger.getLogger(HttpRequestUtil.class);

	public static String httpPostWithJSON(String url) throws Exception {

        HttpPost httpPost = new HttpPost(url);
        CloseableHttpClient client = HttpClients.createDefault();
        String respContent = null;
        
//        json方式
        JSONObject jsonParam = new JSONObject();  
        jsonParam.put("name", "admin");
        jsonParam.put("pass", "123456");
        StringEntity entity = new StringEntity(jsonParam.toString(),"utf-8");//解决中文乱码问题    
        entity.setContentEncoding("UTF-8");    
        entity.setContentType("application/json");    
        httpPost.setEntity(entity);
        System.out.println();
        
    
//        表单方式
        List<BasicNameValuePair> pairList = new ArrayList<BasicNameValuePair>(); 
        pairList.add(new BasicNameValuePair("name", "admin"));
        pairList.add(new BasicNameValuePair("pass", "123456"));
        httpPost.setEntity(new UrlEncodedFormEntity(pairList, "utf-8"));   
        
        
        HttpResponse resp = client.execute(httpPost);
        if(resp.getStatusLine().getStatusCode() == 200) {
            HttpEntity he = resp.getEntity();
            respContent = EntityUtils.toString(he,"UTF-8");
        }
        return respContent;
    }

    
//    public static void main(String[] args) throws Exception {
//        String result = httpPostWithJSON("http://localhost:8080/hcTest2/Hc");
//        System.out.println(result);
//    }
//
//	@SuppressWarnings("finally")
//	public static String sendPost(String url, NameValuePair[] data) {
//		String result = "";
//		try {
//			log.info("---------------------------第三方征信系统请求开始------------------------");
//			HttpClient client = new HttpClient();
//			PostMethod post = new PostMethod(url);
//			client.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
//			client.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
//
//			post.setRequestBody(data);
//			client.executeMethod(post);
//			log.info("---------------------------第三方征信系统发送请求------------------------");
//			result = post.getResponseBodyAsString();
//			log.info("---------------------------第三方征信系统请求结果------------------------");
//			log.info(result);
//		} catch (IOException e) {
//			log.info("---------------------------第三方征信系统发送请求异常------------------------");
//			e.printStackTrace();
//			throw (e);
//		} finally {
//			return result;
//		}
//	}
//
//	@SuppressWarnings("finally")
//	public static String sendGet(String url, NameValuePair[] data) {
//		String result = "";
//		GetMethod getMethod = null;
//		try {
//			HttpClient client = new HttpClient();
//			getMethod = new GetMethod(url);
//
//			client.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
//			client.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
//			getMethod.setQueryString(data);
//			client.executeMethod(getMethod);
//			result = getMethod.getResponseBodyAsString();
//		} catch (IOException e) {
//			e.printStackTrace();
//		} finally {
//			getMethod.releaseConnection();
//			return result;
//		}
//	}

}
