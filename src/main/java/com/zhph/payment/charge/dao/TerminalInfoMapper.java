package com.zhph.payment.charge.dao;

import com.zhph.payment.charge.entity.TerminalInfo;

/**
 * Created by zhph on 2017/1/18.
 */
public interface TerminalInfoMapper {
    /**
     * 通过主键查询支付终端信息
     * @return
     */
    public TerminalInfo queryByPK(String priNumber);
}
