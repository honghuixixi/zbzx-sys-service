package com.aek.ebey.sys.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aek.common.core.base.BaseServiceImpl;
import com.aek.ebey.sys.mapper.SysRolePermissionMapper;
import com.aek.ebey.sys.model.SysRolePermission;
import com.aek.ebey.sys.service.SysRolePermissionService;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;

/**
 * <p>
 * 角色权限关系表 服务实现类
 * </p>
 *
 * @author aek
 * @since 2017-05-06
 */
@Service
@Transactional
public class SysRolePermissionServiceImpl extends BaseServiceImpl<SysRolePermissionMapper, SysRolePermission>
		implements SysRolePermissionService {

	@Override
	public List<SysRolePermission> findByRoleId(Long roleId) {
		Wrapper<SysRolePermission> wrapper = new EntityWrapper<SysRolePermission>();
		wrapper.eq("role_id", roleId);
		return this.selectList(wrapper);
	}

	@Override
	public List<SysRolePermission> findByTenantIdAndModuleId(Long tenantId, Long moduleId) {
		Wrapper<SysRolePermission> wrapper = new EntityWrapper<SysRolePermission>();
		wrapper.eq("tenant_id", tenantId).eq("module_id", moduleId).eq("del_flag", false);
		return this.selectList(wrapper);
	}

	@Override
	public boolean isCanPermission(Long roleId, Long permissionId) {
		Wrapper<SysRolePermission> wrapper = new EntityWrapper<SysRolePermission>();
		wrapper.eq("role_id", roleId).eq("permission_id", permissionId).eq("enable", true).eq("del_flag", false);
		return this.selectCount(wrapper) > 0;
	}

	@Override
	public void deleteByModuleId(Long moduleId) {
		Wrapper<SysRolePermission> wrapper = new EntityWrapper<SysRolePermission>();
		wrapper.eq("module_id", moduleId);
		this.delete(wrapper);
	}

	@Override
	public void deleteByPermissionId(Long permissionId) {
		Wrapper<SysRolePermission> wrapper = new EntityWrapper<SysRolePermission>();
		wrapper.eq("permission_id", permissionId);
		this.delete(wrapper);
	}

}
