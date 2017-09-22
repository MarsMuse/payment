package com.zhph.manager.service;

import com.zhph.manager.model.RoleGroupBean;

import java.util.List;
import java.util.Map;


public interface IRoleGroup {
    
    void insertRoleRelation(List<RoleGroupBean> roleGroupList);
    
    List<Map> getRoleRelationByLoginCode(String loginCode);
}
