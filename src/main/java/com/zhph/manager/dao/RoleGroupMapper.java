package com.zhph.manager.dao;

import com.zhph.manager.model.RoleGroupBean;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface RoleGroupMapper {
    void insertRoleRelation(List<RoleGroupBean> roleGroupList);
    
    int queryRoleRelation(String groupId);
    
    void deleteRoleRelation(String groupId);
    
    List<Map> getRoleRelationByLoginCode(String loginCode);
}
