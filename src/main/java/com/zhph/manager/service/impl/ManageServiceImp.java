package com.zhph.manager.service.impl;

import com.zhph.base.encrypt.util.MessageDigestUtil;
import com.zhph.base.pagination.BindedRowBounds;
import com.zhph.base.pagination.Page;
import com.zhph.manager.dao.ManageDao;
import com.zhph.manager.model.Menu;
import com.zhph.manager.model.UserBean;
import com.zhph.manager.service.ManageService;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 *
 * Author: Zou Yao
 * Description: (公用的管理服务)
 * Time: 2017/8/3 14:49
 *
**/
@Service
public class ManageServiceImp implements ManageService {

    @Resource
    private ManageDao manageDao;


    /**
     *
     * Author: zou yao
     * Description: {获取到用户菜单}
     * Date: 2017/8/3 15:30
     *
    **/
    @Override
    public List<Menu> getUserMenuList(UserBean user) {
        List<Menu> result;
        String loginCode = user.getLoginCode();
        if(loginCode == null || "".equals(loginCode)){
            result = null;
        }else{
            result = manageDao.getUserMenuList(loginCode);
        }
        return result;
    }


    /**
     *
     * Author: zou yao
     * Description: {获取到用户信息}
     * Date: 2017/8/4 15:07
     *
    **/
    @Override
    public Map<String, Object> getListForUserInfo(Map<String, Object> parameter) {

        Map<String, Object>  result = new HashMap<>();

        if(parameter.get("currentPage") != null && parameter.get("limit") != null){
            //获取到当前页
            int currentPage = Integer.parseInt(parameter.get("currentPage").toString());
            //获取到每页数据量
            int limit = Integer.parseInt(parameter.get("limit").toString());
            //载入分页数据
            Page page = new Page(currentPage, limit);
            //将分页数据载入分页接口中
            BindedRowBounds rowBounds  =  new BindedRowBounds(page);
            List<UserBean> dataList = manageDao.getListForUserInfo(parameter , rowBounds);
            result.put("result", dataList);
            result.put("totalCount", rowBounds.getPage().getTotalRecords());
        }
        else{
            List<UserBean>  dataList = manageDao.getListForUserInfo(parameter ,  RowBounds.DEFAULT);
            result.put("result", dataList);
        }
        return result;
    }


    /**
     *
     * Author: zou yao
     * Description: {验证用户是否存在}
     * Date: 2017/8/4 16:32
     *
    **/
    @Override
    public String verifyAccount(String loginCode) {

        return String.valueOf(manageDao.verifyAccount(loginCode));
    }


    /**
     *
     * Author: zou yao
     * Description: {新增用户}
     * Date: 2017/8/4 17:32
     *
    **/
    @Override
    public String addUser(UserBean user) {
        //加密
        String password = user.getUserPass();
        user.setUserPass(MessageDigestUtil.digestToHexString(password));
        return String.valueOf(manageDao.addUser(user));
    }


    /**
     *
     * Author: zou yao
     * Description: {修改用户锁定状态}
     * Date: 2017/8/7 10:08
     *
    **/
    @Override
    public String updateLock(String id, String lock) {
        return String.valueOf(manageDao.updateLock(id, lock));
    }


    /**
     *
     * Author: zou yao
     * Description: {物理删除}
     * Date: 2017/8/7 11:24
     *
    **/
    @Override
    public String physicalDelete(String id) {
        return String.valueOf(manageDao.physicalDelete(id));
    }
}
