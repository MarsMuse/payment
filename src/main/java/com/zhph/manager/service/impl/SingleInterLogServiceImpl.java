package com.zhph.manager.service.impl;

import com.zhph.base.pagination.BindedRowBounds;
import com.zhph.base.pagination.Page;
import com.zhph.manager.dao.SingleInterLogMapper;
import com.zhph.manager.model.SingleInterLogList;
import com.zhph.manager.service.SingleInterLogService;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhph on 2017/8/7.
 */
@Service
public class SingleInterLogServiceImpl implements SingleInterLogService {

    @Resource
    private SingleInterLogMapper singleInterLogMapper;

    @Override
    public Map<String,Object> getSingleInterLog(Map<String, Object> parameter){
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
            List<SingleInterLogList> dataList = null;
            try{
                dataList  = singleInterLogMapper.getListForChannelList(parameter,rowBounds);
            }catch (Exception e){
                e.printStackTrace();
            }
            result.put("result", dataList);
            result.put("totalCount", rowBounds.getPage().getTotalRecords());
        }
        else{
            List<SingleInterLogList> dataList = singleInterLogMapper.getListForChannelList(parameter, RowBounds.DEFAULT);
            result.put("result", dataList);
        }
        return result;
    }

    @Override
    public String queryInterfaceContent(String parameter) {
        return singleInterLogMapper.queryInterfaceContent(parameter);
    }
}
