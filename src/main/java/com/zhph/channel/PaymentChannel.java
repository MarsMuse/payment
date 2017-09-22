package com.zhph.channel;

import java.util.List;

import com.zhph.entity.ParamsEntity;
import com.zhph.entity.PaymentResult;
import com.zhph.payment.charge.entity.ChargeDetailInfo;
import com.zhph.payment.charge.entity.PaymentRecord;

/**
 * 抽象第三方支付 
 * Created by zhph on 2017/2/6.
 */
public interface PaymentChannel {
    /**
     * 单扣
     * @param params 扣款参数
     * @return 单扣扣款结果
     * @throws Exception 扣款异常
     */
    PaymentResult singlePayment(ChargeDetailInfo params)throws Exception;

    /**
     * 单扣查询
     * @param
     * @return 单扣查询结果
     * @throws Exception 单扣查询异常
     */
    PaymentResult singlePaymentQuery(ChargeDetailInfo params)throws Exception;

    /**
     * 批量扣款
     * @param paramsEntitys 扣款参数
     * @return 扣款结果编码
     * @throws
     */
    PaymentResult batchPayment(List<ChargeDetailInfo> paramsEntitys,String batchNo,String platformCode)throws Exception;

    /**
     * 批扣查询
     * @param batchNo 批次号
     * @return 批扣结果
     * @throws Exception 扣款异常
     */
    PaymentResult batchPaymentQuery(String batchNo,String mainBody)throws Exception;

 
}
