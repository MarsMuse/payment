package com.zhph.manager.dao;

import com.zhph.manager.model.PaymentRecorde;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;

public interface PaymentRecordeMapper {
    /**
     * 带分页数据请求
     * @param parameter
     * @param rowBounds
     * @return
     */
    List<PaymentRecorde> getListForChannelList(@Param("parameter")Map<String, Object> parameter, @Param("rowBounds") RowBounds rowBounds);
}