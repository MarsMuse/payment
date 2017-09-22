package com.zhph.payment.charge.service.business;

import java.util.List;

import com.zhph.payment.charge.entity.ChargeDetailInfo;



/**
 *
 * Author: Zou Yao
 * Description: (第三方渠道支付服务交互接口，由于所有和第三方平台交互边界现在在平台内部，故不需要返回值，直接将数据持久化到支付平台)
 * Time: 2017/7/21 17:43
 *
**/
public interface PaymentService {


    /**
     *
     * Author: zou yao
     * Description: {单扣接口，提供单笔扣款服务}
     * Date: 2017/7/21 17:44
     *
    **/
    void singlePayment(ChargeDetailInfo chargeDetailInfo)throws Exception;


     /**
      * 根据业务平台查询正在扣款中的数据 。在更新业务状态
      * 	注：实现类需要对类添加数据标识，，
      *   		如：  params.setChargeType(ConstantPay.SINGLESTATE_QUERY);
    				params.setPaymentChannel(ConstantPay.BAOFOO_CODE);
      * @author likang
      * @date 2017-8-1上午10:18:25
      */
    void singlePaymentQuery(ChargeDetailInfo chargeDetailInfo)throws Exception;



    /**
     *
     * Author: zou yao
     * Description: {批扣接口，提供批量扣款服务}
     * Date: 2017/7/21 17:47
     * @param platformCode 
     *
    **/
    void batchPayment(List<ChargeDetailInfo > chargeDetailInfoList , String batchNo, String platformCode ,String mainBody)throws Exception;

 



    /**
     *
     * Author: zou yao
     * Description: {批量扣款查询接口}
     * Date: 2017/7/21 17:48
     *
    **/
    void batchPaymentQuery(String  batchNo ,String mainBody)throws Exception;
}
