package com.zhph.manager.dao;

import com.zhph.payment.charge.entity.ChargeRecordDetail;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by zhph on 2017/8/7.
 */
@Repository
public interface ChargeDao {
       /**
        * 带分页数据请求
        * @param parameter
        * @param rowBounds
        * @return
        */
       List<ChargeRecordDetail> getListForChannelList(@Param("parameter")Map<String, Object> parameter, @Param("rowBounds") RowBounds rowBounds);
}
