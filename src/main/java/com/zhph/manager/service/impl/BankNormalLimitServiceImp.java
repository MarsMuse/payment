package com.zhph.manager.service.impl;

import com.zhph.base.pagination.BindedRowBounds;
import com.zhph.base.pagination.Page;
import com.zhph.base.common.DateUtil;
import com.zhph.manager.model.UserBean;
import com.zhph.manager.service.BankNormalLimitService;
import com.zhph.payment.charge.dao.BankNormalLimitMapper;
import com.zhph.payment.charge.entity.BankNormalLimit;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhph on 2017/8/3.
 */
@Service
public class BankNormalLimitServiceImp implements BankNormalLimitService {
    @Resource
    private BankNormalLimitMapper bankNormalLimitMapper;

    /**
     * 扣款银行限制表
     */
    public Map<String,Object> getListForBankNormalList(Map<String , Object> parameter){
        Map<String, Object> result = new HashMap<String , Object>();
        if(parameter.get("currentPage") != null && parameter.get("limit") != null){
            //获取到当前页
            int currentPage = Integer.parseInt(parameter.get("currentPage").toString());
            //获取到每页数据量
            int limit = Integer.parseInt(parameter.get("limit").toString());
            //载入分页数据
            Page page = new Page(currentPage, limit);
            //将分页数据载入分页接口中
            BindedRowBounds rowBounds  =  new BindedRowBounds(page);
            List<BankNormalLimit> dataList = null;
            try{
                dataList  = bankNormalLimitMapper.getListForChannelList(parameter,rowBounds);
            }catch (Exception e){
                e.printStackTrace();
            }
            result.put("result", dataList);
            result.put("totalCount", rowBounds.getPage().getTotalRecords());
        }
        else{
            List<BankNormalLimit> dataList = bankNormalLimitMapper.getListForChannelList(parameter,RowBounds.DEFAULT);
            result.put("result", dataList);
        }
        return result;
    }

    /**
     * 更新
     * @param bankNormalLimit
     */
    public int updateBankNormal(BankNormalLimit bankNormalLimit,UserBean user) {
        bankNormalLimit.setUpdateTime(DateUtil.getNowDate().toString());
        bankNormalLimit.setUpdateUserId(user.getUserId());
        int updateCount = bankNormalLimitMapper.updateBankNormalLimit(bankNormalLimit);
        return updateCount;
    }

    @Override
    public int updateBankNormalState(String id,String isEnable,UserBean user) {
        BankNormalLimit bank = new BankNormalLimit();
        bank.setIsEnable(isEnable);
        bank.setPriNumber(id);
        bank.setUpdateTime(DateUtil.getNowDate().toString());
        bank.setUpdateUserId(user.getUserId());
       return  bankNormalLimitMapper.updateByPrimaryKeySelective(bank);
    }

    @Override
    public int addCannal(BankNormalLimit bankNormalLimit, UserBean user) {
        int updateCount = 0;
        try {
            bankNormalLimit.setCreateTime(DateUtil.getNowDate().toString());
            bankNormalLimit.setCreateUserId(user.getUserId());
            updateCount = bankNormalLimitMapper.insert(bankNormalLimit);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return updateCount;
    }

    @Override
    public int deleteById(String id, UserBean user) {
        return bankNormalLimitMapper.deleteByPrimaryKey(id);
    }
}
