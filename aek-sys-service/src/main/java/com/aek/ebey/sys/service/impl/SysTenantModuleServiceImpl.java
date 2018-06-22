package com.aek.ebey.sys.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aek.common.core.base.BaseServiceImpl;
import com.aek.common.core.exception.ExceptionFactory;
import com.aek.common.core.serurity.WebSecurityUtils;
import com.aek.ebey.sys.core.SysConstants;
import com.aek.ebey.sys.enums.ModuleSource;
import com.aek.ebey.sys.mapper.SysTenantModuleMapper;
import com.aek.ebey.sys.model.SysModule;
import com.aek.ebey.sys.model.SysPermission;
import com.aek.ebey.sys.model.SysRole;
import com.aek.ebey.sys.model.SysRolePermission;
import com.aek.ebey.sys.model.SysTenant;
import com.aek.ebey.sys.model.SysTenantModule;
import com.aek.ebey.sys.model.custom.ModuleCustom;
import com.aek.ebey.sys.model.vo.SysModuleVo;
import com.aek.ebey.sys.model.vo.SysRoleVo;
import com.aek.ebey.sys.service.SysModuleService;
import com.aek.ebey.sys.service.SysPermissionService;
import com.aek.ebey.sys.service.SysRolePermissionService;
import com.aek.ebey.sys.service.SysRoleUserService;
import com.aek.ebey.sys.service.SysTenantModuleService;
import com.aek.ebey.sys.service.SysTenantService;
import com.aek.ebey.sys.service.ribbon.AuthClientService;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.google.common.collect.Lists;

/**
 * <p>
 * 机构模块表 服务实现类
 * </p>
 *
 * @author aek
 * @since 2017-05-06
 */
@Service
@Transactional
public class SysTenantModuleServiceImpl extends BaseServiceImpl<SysTenantModuleMapper, SysTenantModule>
		implements SysTenantModuleService {

	private static final Logger logger = LoggerFactory.getLogger(SysTenantModuleServiceImpl.class);

	@Autowired
	private SysModuleService moduleService;

	@Autowired
	private SysTenantModuleMapper tenantModuleMapper;

	@Autowired
	private SysTenantService tenantService;

	@Autowired
	private SysRolePermissionService rolePermissionService;

	@Autowired
	private SysRoleUserService roleUserService;

	@Autowired
	private SysPermissionService permissionService;

	@Autowired
	private AuthClientService authClientService;

	@Override
	public void initTenantModule(Long tenantId) {
		Date date = new Date();

		// 查询机构可拥有模块
		List<SysModule> modules = this.moduleService.findModuleByTenantIdWithCan(tenantId);
		List<SysTenantModule> tenantModules = new ArrayList<SysTenantModule>();

		for (SysModule module : modules) {
			SysTenantModule tenantModule = new SysTenantModule();
			tenantModule.setTenantId(tenantId);
			tenantModule.setReleaseTime(module.getCreateTime());
			tenantModule.setModuleType(module.getModuleType());
			tenantModule.setVersionNumber(module.getVersionNumber());
			tenantModule.setModuleId(module.getId());
			tenantModule.setName(module.getName());
			tenantModule.setUrl(module.getUrl());
			tenantModule.setCreateTime(date);
			tenantModule.setModuleSource(ModuleSource.INDEPENDENT_ADD.getNumber());
			tenantModule.setDescription(module.getDescription());

			// 模块默认拥有
			boolean defaultExist = module.getCoustom().getDefaultExist();
			boolean delFlag = defaultExist ? false : true;
			boolean enable = defaultExist ? true : false;

			tenantModule.setDelFlag(delFlag);
			tenantModule.setEnable(enable);

			tenantModules.add(tenantModule);
		}
		if (tenantModules.size() != 0) {
			this.insertBatch(tenantModules);
		}
	}

	@Override
	public List<SysTenantModule> findByTenantId(Long tenantId) {
		Wrapper<SysTenantModule> wrapper = new EntityWrapper<SysTenantModule>();
		wrapper.eq("tenant_id", tenantId).eq("enable", true).eq("del_flag", false);
		return this.selectList(wrapper);
	}

	@Override
	public List<Long> findTenantModuleIdByTenantId(Long tenantId) {
		List<Long> mduleIds = Lists.newArrayList();
		List<SysTenantModule> tenantModules = this.findByTenantId(tenantId);

		for (SysTenantModule tenantModule : tenantModules) {
			mduleIds.add(tenantModule.getModuleId());
		}
		return mduleIds;
	}

	@Override
	public List<SysTenantModule> findAllByTenantId(Long tenantId) {
		Wrapper<SysTenantModule> wrapper = new EntityWrapper<SysTenantModule>();
		wrapper.eq("tenant_id", tenantId);
		return this.selectList(wrapper);
	}

	@Override
	public List<SysTenantModule> findModuleByRoleId(Long roleId) {
		return this.tenantModuleMapper.selectModuleByRoleId(roleId);
	}

	@Override
	public void deleteTenantModule(Long tenantId, Long moduleId) {

		// 判断当前移除模块是否为默认拥有模块，不可移除
		boolean defaultExist = this.moduleService.isDefaultExist(moduleId);
		if (defaultExist) {
			throw ExceptionFactory.create("M_001");
		}

		List<SysTenantModule> tenantModules = Lists.newArrayList();
		List<SysRolePermission> rolePermissions = Lists.newArrayList();

		SysTenantModule tenantModule = this.findByTenantIdAndModuleId(tenantId, moduleId);
		tenantModule.setEnable(false);
		this.updateById(tenantModule);

		// 查询该模块下的权限
		List<SysRolePermission> permission = this.rolePermissionService.findByTenantIdAndModuleId(tenantId, moduleId);
		if (CollectionUtils.isNotEmpty(permission)) {
			for (SysRolePermission rolePermission : permission) {
				rolePermission.setDelFlag(true);
			}
			rolePermissions.addAll(permission);
		}

		// 子机构级联移除
		List<SysTenant> subTenants = this.tenantService.findAllSubTenant(tenantId);
		List<Long> tenantIds = Lists.newArrayList();
		for (SysTenant subTenant : subTenants) {
			tenantIds.add(subTenant.getId());
			SysTenantModule subTenantModule = this.findByTenantIdAndModuleId(subTenant.getId(), moduleId);

			if (subTenantModule != null && subTenantModule.getEnable() == true) {
				subTenantModule.setEnable(false);
				tenantModules.add(subTenantModule);

				// 查询该模块下的权限
				List<SysRolePermission> permissions = this.rolePermissionService
						.findByTenantIdAndModuleId(subTenant.getId(), moduleId);
				if (CollectionUtils.isNotEmpty(permissions)) {
					for (SysRolePermission rolePermission : permissions) {
						rolePermission.setDelFlag(true);
					}
					rolePermissions.addAll(permissions);
				}

			}
		}
		if (CollectionUtils.isNotEmpty(tenantModules)) {
			this.updateBatchById(tenantModules);
		}
		if (CollectionUtils.isNotEmpty(rolePermissions)) {
			this.rolePermissionService.updateBatchById(rolePermissions);
		}
		// 清除机构缓存信息
		String token = WebSecurityUtils.getCurrentToken();
		new Thread() {
			@Override
			public void run() {
				logger.info("=================清除当前机构[tenantId=" + tenantId + "]用户缓存开始=========================");
				// 机构模块发生变化清空当前租户及其子租户的用户缓存
				Map<String, Object> clearCurrentTenantCacheResult = authClientService.removeUserDetailByTenant(tenantId,
						token);
				logger.info("=================清除当前机构[tenantId=" + tenantId + "]用户缓存结果="
						+ clearCurrentTenantCacheResult.toString() + "=========================");
			}
		}.start();
	}

	@Override
	public SysTenantModule findByTenantIdAndModuleId(Long tenantId, Long moduleId) {
		Wrapper<SysTenantModule> wrapper = new EntityWrapper<SysTenantModule>();
		wrapper.eq("tenant_id", tenantId).eq("module_id", moduleId);
		return this.selectOne(wrapper);
	}

	@Override
	public void addTenantModule(Long tenantId, List<Long> moduleIds) {
		List<SysTenantModule> tenantModules = Lists.newArrayList();
		List<SysRolePermission> rolePermissions = Lists.newArrayList();

		if (CollectionUtils.isNotEmpty(moduleIds)) {
			for (Long moduleId : moduleIds) {
				SysTenantModule tenantModule = this.findByTenantIdAndModuleId(tenantId, moduleId);
				if (tenantModule != null) {
					// 查询存在且未启用，标记为启用
					tenantModule.setDelFlag(false);
					tenantModule.setEnable(true);
					tenantModules.add(tenantModule);

					// 所有角色在该模块下的权限也标记为可用
					Wrapper<SysRolePermission> wrapper = new EntityWrapper<SysRolePermission>();
					wrapper.eq("tenant_id", tenantId).eq("module_id", moduleId).eq("del_flag", true);
					List<SysRolePermission> permissions = this.rolePermissionService.selectList(wrapper);

					if (CollectionUtils.isNotEmpty(permissions)) {
						for (SysRolePermission rolePermission : permissions) {
							rolePermission.setDelFlag(false);
						}
						rolePermissions.addAll(permissions);
					}
				}
			}
		}

		if (CollectionUtils.isNotEmpty(tenantModules)) {
			this.updateBatchById(tenantModules);
		}
		if (CollectionUtils.isNotEmpty(rolePermissions)) {
			this.rolePermissionService.updateBatchById(rolePermissions);
		}
		String token = WebSecurityUtils.getCurrentToken();
		new Thread() {
			@Override
			public void run() {
				logger.info("=================清除当前机构[tenantId=" + tenantId + "]用户缓存开始=========================");
				// 机构模块发生变化清空当前租户及其子租户的用户缓存
				Map<String, Object> clearCurrentTenantCacheResult = authClientService.removeUserDetailByTenant(tenantId,
						token);
				logger.info("=================清除当前机构[tenantId=" + tenantId + "]用户缓存结果="
						+ clearCurrentTenantCacheResult.toString() + "=========================");
			}
		}.start();

	}

	@Override
	public List<SysModule> findTenantCanAddModule(Long tenantId) {
		SysTenant tenant = this.tenantService.selectById(tenantId);
		if (tenant == null) {
			throw ExceptionFactory.create("O_008");
		}

		SysTenant parentTenant = this.tenantService.selectById(tenant.getParentId());

		List<SysModule> modules = Lists.newArrayList();
		if (parentTenant.getId().equals(SysConstants.AEK_TENANT_ID)) {
			// 父机构为爱医康则可分配该机构类型任意模块
			modules = this.moduleService.findModuleByTenantIdWithCan(tenantId);

		} else {
			// 查询父机构已拥有模块的基础上进行分配
			List<SysTenantModule> parentTenantModules = this.findByTenantId(parentTenant.getId());
			for (SysTenantModule tenantModule : parentTenantModules) {
				SysModule module = this.moduleService.selectById(tenantModule.getModuleId());
				modules.add(module);
			}
		}

		List<SysTenantModule> tenantModules = this.findByTenantId(tenantId);
		Iterator<SysModule> it = modules.iterator();
		// 已拥有模块remove
		while (it.hasNext()) {
			SysModule module = it.next();
			Long moduleId = module.getId();
			boolean flag = false;

			for (SysTenantModule tenantModule : tenantModules) {
				if (moduleId.equals(tenantModule.getModuleId())) {
					flag = true;
				}
			}
			if (flag) {
				it.remove();
			}
		}

		return modules;
	}

	@Override
	public List<SysTenantModule> findByTenantIdAndModuleType(Long tenantId, Long moduleType) {
		Wrapper<SysTenantModule> wrapper = new EntityWrapper<SysTenantModule>();
		wrapper.eq("tenant_id", tenantId).eq("enable", true).eq("del_flag", false);
		if (moduleType != null) {
			wrapper.eq("module_type", moduleType);
		}
		return this.selectList(wrapper);
	}

	@Override
	@Deprecated
	public List<SysModuleVo> findUserSeeModuleWithPermission(Long userId, Long tenantId) {
		List<SysModuleVo> moduleVos = Lists.newArrayList();

		// 拥有admin角色的用户，拥有下级机构的所有可用权限
		SysRole role = this.roleUserService.findUserAdminRole(userId);

		if (role != null) {
			// 当前机构所有的模块
			List<SysTenantModule> tenantModules = this.findByTenantId(tenantId);
			for (SysTenantModule tenantModule : tenantModules) {
				SysModuleVo vo = new SysModuleVo();
				vo.setId(tenantModule.getModuleId());
				vo.setName(tenantModule.getName());
				List<SysPermission> permissions = this.permissionService.findByModuleId(tenantModule.getModuleId());
				vo.setPermissions(permissions);
				moduleVos.add(vo);
			}

		} else {
			// 查询用户角色所涉及的模块
			List<SysRoleVo> roles = this.roleUserService.findByTenantIdAndUserId(tenantId, userId);
			List<Long> releIds = Lists.newArrayList();
			for (SysRoleVo roleVo : roles) {
				releIds.add(roleVo.getId());
			}
			// TODO 查询权限。。。

		}

		return null;
	}

	@Override
	public List<SysTenantModule> findByModuleId(Long moduleId) {
		Wrapper<SysTenantModule> wrapper = new EntityWrapper<SysTenantModule>();
		wrapper.eq("module_id", moduleId);
		return this.selectList(wrapper);
	}

	@Override
	public void updateByModule(SysModule module) {
		Date now = new Date();
		SysTenantModule tenantModule = new SysTenantModule();
		tenantModule.setName(module.getName());
		tenantModule.setModuleType(module.getModuleType());
		tenantModule.setUrl(module.getUrl());
		tenantModule.setVersionNumber(module.getVersionNumber());
		tenantModule.setUpdateTime(now);

		ModuleCustom coustom = module.getCoustom();
		if (coustom != null && coustom.getDefaultExist() != null) {
			boolean defaultExist = coustom.getDefaultExist();
			boolean delflag = defaultExist ? false : true;
			if (defaultExist) {
				tenantModule.setEnable(coustom.getDefaultExist());
				tenantModule.setDelFlag(delflag);
			} else {

			}
		}

		Wrapper<SysTenantModule> wrapper = new EntityWrapper<SysTenantModule>();
		wrapper.eq("module_id", module.getId());
		this.update(tenantModule, wrapper);
	}

	@Override
	public void deleteByModuleId(Long moduleId) {
		Wrapper<SysTenantModule> wrapper = new EntityWrapper<SysTenantModule>();
		wrapper.eq("module_id", moduleId);
		this.delete(wrapper);
	}
}
