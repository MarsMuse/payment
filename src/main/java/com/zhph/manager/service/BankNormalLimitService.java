package com.zhph.manager.service;


import com.zhph.manager.model.UserBean;
import com.zhph.payment.charge.entity.BankNormalLimit;

import java.util.Map;

/**
 * Created by zhph on 2017/8/4.
 */
public interface BankNormalLimitService {

     Map<String,Object> getListForBankNormalList(Map<String , Object> parameter);
     int updateBankNormal(BankNormalLimit bankNormalLimit, UserBean user);
     //添加渠道
     int addCannal(BankNormalLimit bankNormalLimit, UserBean user) ;
     //更新渠道状态
     int updateBankNormalState(String id,String isEnable, UserBean user);
     //删除指定值
     int deleteById(String id, UserBean user);
}
