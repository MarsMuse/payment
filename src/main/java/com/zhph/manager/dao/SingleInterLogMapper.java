package com.zhph.manager.dao;

import com.zhph.manager.model.SingleInterLogList;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;

public interface SingleInterLogMapper {
    /**
     * 带分页数据请求
     * @param parameter
     * @param rowBounds
     * @return
     */
    List<SingleInterLogList> getListForChannelList(@Param("parameter") Map<String, Object> parameter, @Param("rowBounds") RowBounds rowBounds);

    String queryInterfaceContent(String priNumber);
}