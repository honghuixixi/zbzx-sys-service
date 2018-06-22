package com.aek.ebey.sys.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aek.common.core.base.BaseServiceImpl;
import com.aek.ebey.sys.mapper.SysRolePermissionPresetMapper;
import com.aek.ebey.sys.model.SysRolePermissionPreset;
import com.aek.ebey.sys.service.SysRolePermissionPresetService;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;

/**
 * <p>
 * 预设角色权限关系表 服务实现类
 * </p>
 *
 * @author aek
 * @since 2017-05-06
 */
@Service
@Transactional
public class SysRolePermissionPresetServiceImpl
		extends BaseServiceImpl<SysRolePermissionPresetMapper, SysRolePermissionPreset>
		implements SysRolePermissionPresetService {

	@Autowired
	private SysRolePermissionPresetMapper rolePermissionPresetMapper;

	@Override
	public List<SysRolePermissionPreset> findByPresetRoleId(Long presetRoleId) {
		Wrapper<SysRolePermissionPreset> wrapper = new EntityWrapper<SysRolePermissionPreset>();
		wrapper.eq("del_flag", false).eq("preset_role_id", presetRoleId);
		return this.rolePermissionPresetMapper.selectList(wrapper);
	}

	@Override
	public void deleteByModule(Long moduleId) {
		Wrapper<SysRolePermissionPreset> wrapper = new EntityWrapper<SysRolePermissionPreset>();
		wrapper.eq("module_id", moduleId);
		this.delete(wrapper);
	}

	@Override
	public void deleteByPermissionId(Long permissionId) {
		Wrapper<SysRolePermissionPreset> wrapper = new EntityWrapper<SysRolePermissionPreset>();
		wrapper.eq("permission_id", permissionId);
		this.delete(wrapper);
	}

}
