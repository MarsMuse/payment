package com.zhph.manager.service.impl;

import com.zhph.base.pagination.BindedRowBounds;
import com.zhph.base.pagination.Page;
import com.zhph.manager.dao.WorkPlatformDao;
import com.zhph.manager.model.WorkPlatformBasicInfo;
import com.zhph.manager.service.WorkPlatformService;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 *
 * Author: Zou Yao
 * Description: (业务平台信息服务)
 * Time: 2017/8/3 16:47
 *
**/
@Service
public class WorkPlatformServiceImp implements WorkPlatformService {

    @Resource
    private WorkPlatformDao workPlatformDao;


    /**
     *
     * Author: zou yao
     * Description: {获取到密钥信息}
     * Date: 2017/8/3 16:34
     *
    **/
    @Override
    public Map<String, Object> getListForCipherKey(Map<String, Object> parameter) {
        Map<String, Object>  result = new HashMap<String , Object>();

        if(parameter.get("currentPage") != null && parameter.get("limit") != null){
            //获取到当前页
            int currentPage = Integer.parseInt(parameter.get("currentPage").toString());
            //获取到每页数据量
            int limit = Integer.parseInt(parameter.get("limit").toString());
            //载入分页数据
            Page page = new Page(currentPage, limit);
            //将分页数据载入分页接口中
            BindedRowBounds  rowBounds  =  new BindedRowBounds(page);
            List<WorkPlatformBasicInfo> dataList = workPlatformDao.getListForCipherKey(parameter , rowBounds);
            result.put("result", dataList);
            result.put("totalCount", rowBounds.getPage().getTotalRecords());
        }
        else{
            List<WorkPlatformBasicInfo>  dataList = workPlatformDao.getListForCipherKey(parameter ,  RowBounds.DEFAULT);
            result.put("result", dataList);
        }

        return result;
    }


    /**
     *
     * Author: zou yao
     * Description: {更新密钥信息操作}
     * Date: 2017/8/3 19:28
     *
    **/
    @Override
    public String updateCipherKeyInfo(Map<String, Object> parameter) {
        int result;

        result = workPlatformDao.updateCipherKeyInfo(parameter);
        return String.valueOf(result);
    }
}
