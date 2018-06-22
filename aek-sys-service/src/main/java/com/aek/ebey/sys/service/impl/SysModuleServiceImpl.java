package com.aek.ebey.sys.service.impl;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aek.common.core.base.BaseServiceImpl;
import com.aek.common.core.exception.ExceptionFactory;
import com.aek.ebey.sys.enums.ModuleSource;
import com.aek.ebey.sys.mapper.SysModuleMapper;
import com.aek.ebey.sys.model.SysModule;
import com.aek.ebey.sys.model.SysTenant;
import com.aek.ebey.sys.model.SysTenantModule;
import com.aek.ebey.sys.model.custom.ModuleCustom;
import com.aek.ebey.sys.model.query.ModuleQuery;
import com.aek.ebey.sys.service.SysModuleService;
import com.aek.ebey.sys.service.SysPermissionService;
import com.aek.ebey.sys.service.SysRolePermissionPresetService;
import com.aek.ebey.sys.service.SysRolePermissionService;
import com.aek.ebey.sys.service.SysTenantModuleService;
import com.aek.ebey.sys.service.SysTenantService;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.google.common.collect.Lists;

/**
 * <p>
 * 系统模块表 服务实现类
 * </p>
 *
 * @author aek
 * @since 2017-05-06
 */
@Service
@Transactional
public class SysModuleServiceImpl extends BaseServiceImpl<SysModuleMapper, SysModule> implements SysModuleService {

	@Autowired
	private SysModuleMapper moduleMapper;

	@Autowired
	private SysTenantService tenantService;

	@Autowired
	private SysTenantModuleService tenantModuleService;

	@Autowired
	private SysPermissionService permissionService;

	@Autowired
	private SysRolePermissionService rolePermissionService;

	@Autowired
	private SysRolePermissionPresetService rolePermissionPresetService;

	@Override
	@Transactional(readOnly = true)
	public List<SysModule> findAllModuleWithNotDel() {

		Wrapper<SysModule> wrapper = new EntityWrapper<SysModule>();
		wrapper.eq("del_flag", false);
		wrapper.eq("enable", true);
		List<SysModule> modules = moduleMapper.selectList(wrapper);

		for (SysModule module : modules) {
			module.setCoustom(JSON.parseObject(module.getCustomData(), ModuleCustom.class));
		}
		return modules;
	}

	@Override
	@Transactional(readOnly = true)
	public List<SysModule> findModuleByTenantIdWithCan(Long tenantId) {
		SysTenant tenant = this.tenantService.selectById(tenantId);
		// 当前租户类型
		Integer tenantType = tenant.getTenantType();

		List<SysModule> modules = this.findAllModuleWithNotDel();
		Iterator<SysModule> it = modules.iterator();

		// 根据租户类型判断当前租户是否可以拥有该模块
		while (it.hasNext()) {
			SysModule module = it.next();
			ModuleCustom custom = module.getCoustom();
			List<Integer> tenantTypes = custom.getTenantType();

			// 是否为所有类型租户默认拥有模块
			boolean defaultExist = custom.getDefaultExist();
			if (defaultExist) {
				continue;
			}

			if (!tenantTypes.contains(tenantType)) {
				it.remove();
			}
		}
		return modules;
	}

	@Override
	@Transactional(readOnly = true)
	public boolean isDefaultExist(Long moduleId) {
		SysModule module = this.selectById(moduleId);
		ModuleCustom moduleCustom = JSON.parseObject(module.getCustomData(), ModuleCustom.class);
		return moduleCustom.getDefaultExist();
	}

	@Override
	public void add(SysModule module) {
		this.checkModule(module);
		Date now = new Date();
		ModuleCustom coustom = module.getCoustom();
		module.setCustomData(JSON.toJSONString(coustom));
		module.setCreateTime(now);
		this.insert(module);

		// 是否默认拥有
		boolean defaultExist = coustom.getDefaultExist();

		// 适用机构类型
		List<Integer> tenantType = coustom.getTenantType();

		// 添加机构模块表模型数据
		List<SysTenantModule> tenantModules = Lists.newArrayList();

		for (Integer type : tenantType) {
			List<SysTenant> tenants = this.tenantService.findByTenantType(type);
			if (CollectionUtils.isNotEmpty(tenants)) {
				for (SysTenant tenant : tenants) {
					SysTenantModule tenantModule = new SysTenantModule();
					tenantModule.setTenantId(tenant.getId());
					tenantModule.setModuleId(module.getId());
					tenantModule.setModuleType(module.getModuleType());
					tenantModule.setName(module.getName());
					tenantModule.setUrl(module.getUrl());
					tenantModule.setVersionNumber(module.getVersionNumber());
					tenantModule.setModuleSource(ModuleSource.INDEPENDENT_ADD.getNumber());

					boolean delflag = defaultExist ? false : true;
					tenantModule.setEnable(defaultExist);
					tenantModule.setDelFlag(delflag);
					tenantModule.setReleaseTime(now);
					tenantModule.setCreateTime(now);
					tenantModules.add(tenantModule);
				}
			}
		}
		// 添加至机构模块关系表
		this.tenantModuleService.insertBatch(tenantModules);
	}

	@Override
	public void edit(SysModule module) {
		this.checkModule(module);
		Date now = new Date();
		module.setCustomData(JSON.toJSONString(module.getCoustom()));
		module.setUpdateTime(now);
		this.updateById(module);

		// 修改机构模块关系表
		List<SysTenantModule> tenantModules = this.tenantModuleService.findByModuleId(module.getId());
		for (SysTenantModule tenantModule : tenantModules) {
			tenantModule.setName(module.getName());
			tenantModule.setVersionNumber(module.getVersionNumber());
			tenantModule.setUrl(module.getUrl());
			tenantModule.setModuleType(module.getModuleType());
			tenantModule.setDescription(module.getDescription());
			tenantModule.setUpdateTime(now);
		}
		this.tenantModuleService.updateBatchById(tenantModules);
	}

	/**
	 * 新增修改验证
	 * 
	 * @param module
	 */
	private void checkModule(SysModule module) {
		if (module.getId() == null) {
			// 保存
			if (CollectionUtils.isNotEmpty(this.findByName(module.getName()))) {
				throw ExceptionFactory.create("模块名称被使用");
			}
		} else {
			// 修改 判断不包含自己
			if (StringUtils.isNotBlank(module.getName())) {
				List<SysModule> modules = this.findByName(module.getName());
				if (CollectionUtils.isNotEmpty(modules)) {
					for (SysModule sysModule : modules) {
						if (!sysModule.getId().equals(module.getId())) {
							throw ExceptionFactory.create("模块名称被使用");
						}
					}
				}
			}
		}
	}

	/**
	 * 
	 * 模块删除 (不可逆操作谨慎调用)<br/>
	 * 
	 * 1.删除机构模块关联表(sys_tenant_module)信息 <br/>
	 * 2.删除模块下权限表(sys_permission)信息 <br/>
	 * 3.删除角色权限关系表(sys_role_permission)信息 <br/>
	 * 4.删除预设角色权限关联表信息(sys_role_permission_preset)信息 <br/>
	 * 5.删除模块信息 <br/>
	 */
	@Override
	public void delete(Long moduleId) {
		this.tenantModuleService.deleteByModuleId(moduleId);
		this.permissionService.deleteByModuleId(moduleId);
		this.rolePermissionService.deleteByModuleId(moduleId);
		this.rolePermissionPresetService.deleteByModule(moduleId);
		this.delete(moduleId);
	}

	@Override
	public List<SysModule> findByName(String name) {
		Wrapper<SysModule> wrapper = new EntityWrapper<SysModule>();
		wrapper.eq("name", name);
		return this.selectList(wrapper);
	}

	@Override
	public Page<SysModule> findPageByKeyword(ModuleQuery query) {
		query.setKeyword(StringUtils.trimToNull(query.getKeyword()));

		Wrapper<SysModule> wrapper = new EntityWrapper<SysModule>();
		if (StringUtils.isNotBlank(query.getKeyword())) {
			wrapper.like("name", query.getKeyword());
		}
		return this.selectPage(query.getPage(), wrapper);
	}
}
