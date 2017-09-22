package com.zhph.manager.service;

import java.util.Map;

/**
 * Created by zhph on 2017/8/7.
 */
public interface SingleInterLogService {
    //查询所有
    public Map<String,Object> getSingleInterLog(Map<String, Object> parameter);

    /**
     * 查询单条记录 报文内容
     * @param parameter
     * @return
     */
    public String queryInterfaceContent(String parameter);
}
