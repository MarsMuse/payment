package com.zhph.manager.dao;

import com.zhph.manager.model.Menu;
import com.zhph.manager.model.UserBean;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;


/**
 *
 * Author: Zou Yao
 * Description: (管理数据库访问对象)
 * Time: 2017/8/3 15:29
 *
**/
@Repository
public interface ManageDao {


    /**
     *
     * Author: zou yao
     * Description: {获取到用户菜单}
     * Date: 2017/8/3 15:29
     *
    **/
    List<Menu> getUserMenuList(@Param("loginCode") String  loginCode);



    /**
     *
     * Author: zou yao
     * Description: {获取到用户信息列表}
     * Date: 2017/8/4 15:02
     *
    **/
    List<UserBean> getListForUserInfo(@Param("parameter")Map<String, Object> parameter  , @Param("rowBounds")RowBounds rowBounds);



    /**
     *
     * Author: zou yao
     * Description: {获取到用户条数}
     * Date: 2017/8/4 16:34
     *
    **/
    Integer verifyAccount(@Param("loginCode") String loginCode);



    /**
     *
     * Author: zou yao
     * Description: {新增用户}
     * Date: 2017/8/4 17:31
     *
    **/
    Integer addUser(@Param("user")UserBean user);


    /**
     *
     * Author: zou yao
     * Description: {修改锁定状态}
     * Date: 2017/8/7 10:05
     *
    **/
    Integer updateLock(@Param("id")String id,@Param("lock") String lock);


    /**
     *
     * Author: zou yao
     * Description: {物理删除}
     * Date: 2017/8/7 11:25
     *
    **/
    Integer physicalDelete(@Param("id")String id);
}
