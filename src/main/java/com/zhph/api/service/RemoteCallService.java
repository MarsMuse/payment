package com.zhph.api.service;


import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 *
 * Author: zou yao
 * Description: 定时接口类  定时处理接口类
 * Date: 2017/8/7 15:48
 *
**/
@Path(value ="/remoteCall")
public interface RemoteCallService {



    /**
     *
     * Author: zou yao
     * Description: {批口信息推送接口}
     * Date: 2017/8/7 15:50
     *
    **/
    @GET
    @Path("/batchPush")
    @Produces({ MediaType.APPLICATION_JSON })
    String pushBatchInfo();


    /**
     *
     * Author: zou yao
     * Description: {更新批量扣款信息}
     * Date: 2017/8/7 15:50
     *
     **/
    @GET
    @Path("/batchUpdate")
    @Produces({ MediaType.APPLICATION_JSON })
    String updateBatchCharge();

    /**
     * 单扣推送
     * @return
     */
    @GET
    @Path("/singlePush")
    @Produces({ MediaType.APPLICATION_JSON })
    String singlePush();

    /**
     * 单扣更新
     * @return
     */
    @GET
    @Path("/updateSingleCharge")
    @Produces({ MediaType.APPLICATION_JSON })
    String updateSingleCharge();


    @GET
    @Path("/test")
    @Produces({ MediaType.APPLICATION_JSON })
    String test() throws Exception;


}
