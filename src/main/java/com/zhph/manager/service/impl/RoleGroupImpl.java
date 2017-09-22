package com.zhph.manager.service.impl;

import java.util.List;
import java.util.Map;

import com.zhph.manager.dao.RoleGroupMapper;
import com.zhph.manager.model.RoleGroupBean;
import com.zhph.manager.service.IRoleGroup;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


@Service
public class RoleGroupImpl implements IRoleGroup {

	@Resource
	private RoleGroupMapper roleGroupMapper;
	
	@Override
	public void insertRoleRelation(List<RoleGroupBean> roleGroupList) {
		int roleRelationCount = roleGroupMapper.queryRoleRelation(roleGroupList.get(0).getGroupId());
		if(roleRelationCount>0){
			roleGroupMapper.deleteRoleRelation(roleGroupList.get(0).getGroupId());
		}
		roleGroupMapper.insertRoleRelation(roleGroupList);
	}

	@Override
	public List<Map> getRoleRelationByLoginCode(String loginCode) {
		return roleGroupMapper.getRoleRelationByLoginCode(loginCode);
	}

}
