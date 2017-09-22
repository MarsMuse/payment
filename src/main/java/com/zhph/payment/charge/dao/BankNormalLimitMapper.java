package com.zhph.payment.charge.dao;

import com.zhph.payment.charge.entity.BankNormalLimit;
import com.zhph.payment.charge.entity.FinancingChannel;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
/**
 * 扣款银行限制表 dao
 * @author kang
 * 2017年7月14日上午10:00:13
 */
@Repository
public interface BankNormalLimitMapper {
    
    int deleteByPrimaryKey(String priNumber);

    
    int insert(BankNormalLimit record) throws Exception;

    
    int insertSelective(BankNormalLimit record);

    
    public BankNormalLimit selectByPrimaryKey(String priNumber);

    
    int updateByPrimaryKeySelective(BankNormalLimit record);

    
    int updateByPrimaryKey(BankNormalLimit record);
    /**
     * 查询扣款银行限制表总数
     * @param map
     * @return
     */
    public int getBankNormalLimitCount(Map<String, Object> map);
    /**
     * 扣款银行限制表列表
     * @param map
     * @return
     */
    public List<BankNormalLimit> getBankNormalLimitList(Map<String, Object> map);
    
    /**
     * 根据银行编码 查询优先顺序与对应平台
     * @author likang
     * @date 2017-7-18下午4:15:39
     */
    public List<BankNormalLimit> getBankNormalLimitByBankKey(@Param("bankKey") String bankKey);
    
    
    public List<FinancingChannel> getFinancingChannelList(Map<String, Object> map);
    
    public int updateBankNormalLimit(BankNormalLimit bankNormalLimit);

    /**
     * 根据内部银行编码查询各渠道扣款限制信息
     * @return
     */
    public List<BankNormalLimit> queryByBankKey(Map<String, Object> map);

    /**
     * 查询第三方扣款请求银行编码
     * @param paymentChannel 支付渠道
     * @param bankKey 内部银行编码
     * @return
     */
    public String queryPayCode(@Param("paymentChannel") String paymentChannel, @Param("bankKey") String bankKey);

    /**
     * 通过扣款渠道查询第三方请求扣款银行编码
     * @param paymentChannel
     * @return
     */
    @MapKey("bankKey")
    public Map<String,Map<String,Object>> queryPayCodesByChannel(@Param("paymentChannel") String paymentChannel, @Param("mainBody") String mainBody);



    /**
     * web 页面查询所有的渠道扣款
     * @return
     * @param parameter
     * @param rowBounds
     */
     List<BankNormalLimit> getListForChannelList(@Param("parameter")Map<String, Object> parameter, @Param("rowBounds") RowBounds rowBounds);
}