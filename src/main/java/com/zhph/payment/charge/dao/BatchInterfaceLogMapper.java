package com.zhph.payment.charge.dao;

import org.springframework.stereotype.Repository;

import com.zhph.payment.charge.entity.BatchInterfaceLog;


/**
 * 批扣日志记录
 * @author likang
 *
 */
@Repository
public interface BatchInterfaceLogMapper {
 
 
    int insert(BatchInterfaceLog record);
 
    int insertSelective(BatchInterfaceLog record);
    

  
}