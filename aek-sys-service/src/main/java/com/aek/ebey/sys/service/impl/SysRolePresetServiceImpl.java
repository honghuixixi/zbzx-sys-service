package com.aek.ebey.sys.service.impl;

import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aek.common.core.base.BaseServiceImpl;
import com.aek.ebey.sys.mapper.SysRolePresetMapper;
import com.aek.ebey.sys.model.SysRolePreset;
import com.aek.ebey.sys.model.SysTenant;
import com.aek.ebey.sys.service.SysRolePresetService;
import com.aek.ebey.sys.service.SysTenantService;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;

/**
 * <p>
 * 预设角色表 服务实现类
 * </p>
 *
 * @author aek
 * @since 2017-05-06
 */
@Service
@Transactional
public class SysRolePresetServiceImpl extends BaseServiceImpl<SysRolePresetMapper, SysRolePreset>
		implements SysRolePresetService {

	@Autowired
	private SysTenantService tenantService;

	@Override
	public List<SysRolePreset> findAllTempletRole() {
		Wrapper<SysRolePreset> wrapper = new EntityWrapper<SysRolePreset>();
		wrapper.eq("enable", true).eq("del_flag", false);
		return this.selectList(wrapper);
	}

	@Override
	public List<SysRolePreset> findRolePresetByTenantType(Long tenantId) {
		SysTenant tenant = this.tenantService.selectById(tenantId);

		List<SysRolePreset> rolePresets = this.findAllTempletRole();
		Iterator<SysRolePreset> iterator = rolePresets.iterator();
		while (iterator.hasNext()) {
			SysRolePreset rolePreset = iterator.next();
			String tenantType = rolePreset.getTenantType();
			if (!tenantType.contains(String.valueOf(tenant.getTenantType()))) {
				iterator.remove();
			}
		}
		return rolePresets;
	}

	@Override
	public List<SysRolePreset> findAll() {
		Wrapper<SysRolePreset> wrapper = new EntityWrapper<SysRolePreset>();
		wrapper.eq("del_flag", false);
		return this.selectList(wrapper);
	}
}
