package com.zhph.payment.charge.dao;


import com.zhph.payment.charge.entity.PaymentRecord;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Map;

/**
 * 扣款记录表数据层
 */
@Repository
public interface PaymentRecordMapper {
    int insert(PaymentRecord record);

    /**
     * 更改扣款记录
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(PaymentRecord record);

    /**
     * 插入查询回来的信息
     * @param record
     * @return
     */
    int insertByQueryState(PaymentRecord record);
    /**
     * 查询当天的扣款失败信息
     * @param loanContractNo 合同编号
     * @param bankKey 银行编码
     * @param  mainbody 主体
     * @return
     */
    @MapKey("paymentChannel")
     Map<String,Map<String,Object>> queryPaymentNum(@Param("loanContractNo") String loanContractNo, @Param("bankKey") String bankKey,@Param("mainbody") String mainbody);
    /**
     * 查询指定合同编号在指定渠道当天的扣款总额
     * @param loanContractNo 合同编号
     * @param paymentChannel 扣款渠道
     * @return
     */
     Double sumTotalPaymentAmount(@Param("loanContractNo") String loanContractNo, @Param("paymentChannel") String paymentChannel);

}