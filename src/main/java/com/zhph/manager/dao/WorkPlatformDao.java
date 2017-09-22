package com.zhph.manager.dao;

import com.zhph.manager.model.WorkPlatformBasicInfo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;


/**
 *
 * Author: Zou Yao
 * Description: (业务平台信息数据库访问对象)
 * Time: 2017/8/3 16:29
 *
**/
@Repository
public interface WorkPlatformDao {


    /**
     *
     * Author: zou yao
     * Description: {获取到业务平台信息列表}
     * Date: 2017/8/3 16:29
     *
    **/
    List<WorkPlatformBasicInfo> getListForCipherKey(@Param("parameter")Map<String, Object> parameter  , @Param("rowBounds")RowBounds rowBounds);


    /**
     *
     * Author: zou yao
     * Description: {更新密钥信息}
     * Date: 2017/8/3 19:25
     *
    **/
    int updateCipherKeyInfo(@Param("parameter")Map<String, Object> parameter);
}
