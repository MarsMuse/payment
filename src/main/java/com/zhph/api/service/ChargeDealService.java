package com.zhph.api.service;

import com.zhph.api.entity.ResultInformation;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 *
 * @Author: Zou Yao
 * @Description: (扣款处理接口服务接口)
 * @Time: 2017/7/17 9:30
 *
**/
@Path(value ="/chargeDealService")
public interface ChargeDealService {

    /**
     *
     * @param message
     * @return
     */
	@POST
    @Path("/singleCharge")
    @Produces({ MediaType.APPLICATION_JSON })
	ResultInformation  singleChargeOperation(String message);

    /**
     * 单扣查询交易号状态
     * @param message
     * @return
     */
    @POST
    @Path("/singleQueryCharge")
    @Produces({ MediaType.APPLICATION_JSON })
    ResultInformation singleQueryCharge(String message);

    /**
     *
     * @Author: zou yao
     * @Description: {批量扣款操作接口}
     * @Date: 2017/7/17 9:34
     * @Param: message
     *
    **/
    @POST
    @Path("/batchCharge")
    @Produces({ MediaType.APPLICATION_JSON })
    ResultInformation batchChargeOperation(String message);





    


}
