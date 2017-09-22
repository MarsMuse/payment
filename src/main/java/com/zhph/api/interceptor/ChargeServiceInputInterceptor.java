package com.zhph.api.interceptor;


import com.alibaba.fastjson.JSON;
import com.zhph.api.entity.ChargeOperationException;
import com.zhph.api.entity.ResultInformation;
import com.zhph.payment.charge.service.PlatformSecurityService;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.message.Message;
import org.apache.cxf.message.MessageContentsList;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;
import org.apache.cxf.transport.http.AbstractHTTPDestination;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 *
 * Author: Zou Yao
 * Description: (扣款服务请求拦截器，在此处进行数据解密，校验等操作)
 * Time: 2017/7/17 11:03
 *
**/
@Component("chargeServiceInputInterceptor")
public class ChargeServiceInputInterceptor extends AbstractPhaseInterceptor<Message> {

    //日志打印对象
    private Logger log = LoggerFactory.getLogger(ChargeServiceInputInterceptor.class);
    //注入扣款信息验证服务
    @Resource
    private PlatformSecurityService platformSecurityService;
    //中断点Class路径
    private static final String INTERCEPTOR_POINT_CLASS = "org.apache.cxf.jaxrs.interceptor.JAXRSOutInterceptor";


    public ChargeServiceInputInterceptor() {
        //在方法被调用前拦截到
        super(Phase.PRE_INVOKE);
    }



    /**
     *
     * Author: zou yao
     * Description: {处理扣款请求}
     * Date: 2017/7/17 11:27
     * Param: No such property: code for class: Script1
     *
    **/
    @Override
    public void handleMessage(Message message) throws Fault {
        log.info("扣款服务请求拦截器--处理扣款请求");
        //获取到请求方式 GET POST PUT ...
        String requestType = message.get(Message.HTTP_REQUEST_METHOD).toString().toUpperCase();

        //返回参数对象
        ResultInformation resultInfo = null;
        try{
            if("GET".equals(requestType)){
                //调用参数处理与校验方法
                resultInfo = new ResultInformation("1" ,"合法请求！");
            }else if("POST".equals(requestType)) {
                //获取到请求体参数
                MessageContentsList parameters = MessageContentsList.getContentsList(message);
                resultInfo = this.preparePostInfo(parameters);
                if("1".equals(resultInfo.getCode())){
                    parameters.remove(0);
                    //将解析完的密文传输给调用函数
                    parameters.set(0,resultInfo.getMessage());
                }
            }
        }catch (Exception e){
            log.error("扣款服务请求拦截器--请求数据在校验期间发生异常",e);
            if(e instanceof ChargeOperationException){
                ChargeOperationException ce = (ChargeOperationException) e;
                resultInfo = new ResultInformation(ce.getCode() ,ce.getMessage());
                e.printStackTrace();
            }else{
                log.error("扣款服务请求拦截器--请求数据在校验期间发生异常。MessageId: {}",message.getId());
                resultInfo = new ResultInformation("2" ,"请求参数异常！");
            }
        }finally {
            //将返回信息进行预处理
            this.dealResultInfo(message,resultInfo);
        }
    }



    /**
     *
     * Author: zou yao
     * Description: {请求数据预处理}
     * Date: 2017/7/18 15:14
     * Param: No such property: code for class: Script1
     *
    **/
    private ResultInformation preparePostInfo(MessageContentsList parameters) throws Exception{
        log.info("扣款服务--请求数据预处理");
        //返回参数对象
        ResultInformation resultInfo;
        //判定是否存在参数
        if(parameters == null || parameters.isEmpty()){
            resultInfo = new ResultInformation("2" ,"请求参数异常！");
            log.error("扣款服务--请求参数异常！");
            log.error(resultInfo.getMessage());
        }else{
            String  param=  parameters.get(0).toString();
            log.debug("扣款服务--将数据传入验证服务进行数据验证");
            //将数据传入验证服务进行数据验证
            resultInfo = platformSecurityService.verifyRequestInfo(param);
        }
        return resultInfo;
    }

    /**
     *
     * Author: zou yao
     * Description: {处理返回信息}
     * Date: 2017/7/18 14:52
     * Param: No such property: code for class: Script1
     *
     **/
    private void dealResultInfo(Message message , ResultInformation resultInfo){
        log.info("扣款服务--处理返回信息");
        //如果被判定为请求失效
        if(resultInfo == null || "2".equals(resultInfo.getCode())){
            //获取到响应
            HttpServletResponse response = (HttpServletResponse) message.get(AbstractHTTPDestination.HTTP_RESPONSE);
            if(resultInfo == null) {
                response.setStatus(403);
            } else{
                response.setContentType("html/text;charset=UTF-8");
                try {
                    PrintWriter writer = response.getWriter();
                    //将响应信息转换成JSON字符串
                    String responseInfo = JSON.toJSONString(resultInfo);
                    //输出响应信息
                    writer.print(responseInfo);
                } catch (IOException e) {
                    log.error("获取响应输出流异常，连接可能已断开/超时。" ,e);
                    e.printStackTrace();

                }
            }
            //越过调用方法与输出拦截器,产生中断
            message.getInterceptorChain().doInterceptStartingAfter(message , INTERCEPTOR_POINT_CLASS);
        }
    }

}
