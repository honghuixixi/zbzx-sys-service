package com.aek.ebey.sys.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aek.common.core.BeanMapper;
import com.aek.common.core.base.BaseServiceImpl;
import com.aek.common.core.exception.ExceptionFactory;
import com.aek.ebey.sys.mapper.SysPermissionMapper;
import com.aek.ebey.sys.model.SysModule;
import com.aek.ebey.sys.model.SysPermission;
import com.aek.ebey.sys.model.SysRole;
import com.aek.ebey.sys.model.SysRolePermission;
import com.aek.ebey.sys.model.SysRolePermissionPreset;
import com.aek.ebey.sys.model.SysRolePreset;
import com.aek.ebey.sys.model.SysTenantModule;
import com.aek.ebey.sys.model.vo.SysPermissionVo;
import com.aek.ebey.sys.service.SysModuleService;
import com.aek.ebey.sys.service.SysPermissionService;
import com.aek.ebey.sys.service.SysRolePermissionPresetService;
import com.aek.ebey.sys.service.SysRolePermissionService;
import com.aek.ebey.sys.service.SysRolePresetService;
import com.aek.ebey.sys.service.SysRoleService;
import com.aek.ebey.sys.service.SysTenantModuleService;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.google.common.collect.Lists;

/**
 * <p>
 * 权限表 服务实现类
 * </p>
 *
 * @author aek
 * @since 2017-05-06
 */
@Service
@Transactional
public class SysPermissionServiceImpl extends BaseServiceImpl<SysPermissionMapper, SysPermission>
		implements SysPermissionService {
	@Autowired
	private SysModuleService moduleService;

	@Autowired
	private SysPermissionMapper sysPermissionMapper;

	@Autowired
	private SysTenantModuleService tenantModuleService;

	@Autowired
	private SysRoleService roleService;

	@Autowired
	private SysRolePermissionService rolePermissionService;

	@Autowired
	private SysRolePermissionPresetService rolePermissionPresetService;

	@Autowired
	private SysRolePresetService rolePresetService;

	@Override
	public List<SysPermission> findPermissionByTenantIdWithCan(Long tenantId) {
		// 租户可拥有的模块
		List<SysModule> modules = moduleService.findModuleByTenantIdWithCan(tenantId);

		StringBuilder moduleIds = new StringBuilder();
		if (!modules.isEmpty() && modules.size() > 1) {
			for (SysModule module : modules) {
				moduleIds.append(module.getId());
				moduleIds.append(",");
			}
			moduleIds.deleteCharAt(moduleIds.length() - 1);
		}

		// 根据可拥有模块查询权限
		Wrapper<SysPermission> wrapper = new EntityWrapper<SysPermission>();
		wrapper.in("module_id", moduleIds.toString());

		return this.selectList(wrapper);
	}

	@Override
	public List<SysPermissionVo> findMenuByRoleIdAndModuleId(Long roleId, Long moduleId) {
		return this.sysPermissionMapper.selectMenuByRoleIdAndModuleId(roleId, moduleId);
	}

	@Override
	public List<SysPermissionVo> findByMenuIdAndRoleId(Long menuId, Long roleId) {
		return this.sysPermissionMapper.selectBuMenuIdAndRoleId(roleId, menuId);
	}

	@Override
	public List<SysPermissionVo> findByModuleIdAndRoleIdWithNotInMenu(Long roleId, Long moduleId) {
		return this.sysPermissionMapper.selectByModuleIdAndRoleIdWithNotInMenu(roleId, moduleId);
	}

	@Override
	public List<SysPermission> findMenuByModuleId(Long moduleId) {
		Wrapper<SysPermission> wrapper = new EntityWrapper<SysPermission>();
		wrapper.eq("module_id", moduleId).eq("menu_flag", 1).eq("enable", 1);
		return this.selectList(wrapper);
	}

	@Override
	public List<SysPermissionVo> findByMenuId(Long menuId) {

		Wrapper<SysPermission> wrapper = new EntityWrapper<SysPermission>();
		wrapper.eq("parent_id", menuId).eq("menu_flag", 0).eq("enable", 1);

		List<SysPermission> permissions = this.selectList(wrapper);
		List<SysPermissionVo> permissionVos = Lists.newArrayList();
		for (SysPermission permission : permissions) {
			SysPermissionVo vo = BeanMapper.map(permission, SysPermissionVo.class);
			vo.setEnable(false);
			vo.setDelFlag(true);
			permissionVos.add(vo);
		}
		return permissionVos;
	}

	@Override
	public List<SysPermissionVo> findByModuleIdWithNotMenu(Long moduleId) {

		Wrapper<SysPermission> wrapper = new EntityWrapper<SysPermission>();
		wrapper.eq("module_id", moduleId).eq("menu_flag", 0).eq("enable", 1).le("parent_id", 0);

		List<SysPermission> permissions = this.selectList(wrapper);
		List<SysPermissionVo> permissionVos = Lists.newArrayList();

		for (SysPermission permission : permissions) {
			SysPermissionVo vo = BeanMapper.map(permission, SysPermissionVo.class);
			vo.setEnable(false);
			vo.setDelFlag(true);
			permissionVos.add(vo);
		}
		return permissionVos;
	}

	@Override
	public List<SysPermission> findServicesByUserId(Long userId) {
		return this.sysPermissionMapper.selectServicesByUserId(userId);
	}

	@Override
	public List<SysPermission> findByModuleId(Long moduleId) {
		Wrapper<SysPermission> wrapper = new EntityWrapper<SysPermission>();
		wrapper.eq("module_id", moduleId).eq("enable", 1);
		return this.selectList(wrapper);
	}

	@Override
	public SysPermission findByCode(String permissCode) {
		Wrapper<SysPermission> wrapper = new EntityWrapper<SysPermission>();
		wrapper.eq("code", permissCode).eq("enable", 1);
		return this.selectOne(wrapper);
	}

	@Override
	public void deleteByModuleId(Long moduleId) {
		Wrapper<SysPermission> wrapper = new EntityWrapper<SysPermission>();
		wrapper.eq("module_id", moduleId);
		this.delete(wrapper);
	}

	@Override
	public void add(SysPermission permission) {
		this.checkPermission(permission);
		Date now = new Date();
		permission.setCreateTime(now);
		this.insert(permission);

		// 添加至角色权限表 1.查询所有机构 判断是否拥有当前模块 2.拥有模块则默认可选当前权限
		List<SysRolePermission> rolePermissionss = Lists.newArrayList();
		List<SysTenantModule> tenantModules = this.tenantModuleService.findByModuleId(permission.getModuleId());
		for (SysTenantModule tenantModule : tenantModules) {
			List<SysRole> roles = this.roleService.findByTenantId(tenantModule.getTenantId());
			for (SysRole role : roles) {
				SysRolePermission rolePermission = new SysRolePermission();
				rolePermission.setDelFlag(tenantModule.getDelFlag());
				rolePermission.setEnable(false); // 默认不勾选
				rolePermission.setRoleId(role.getId());
				rolePermission.setModuleId(permission.getModuleId());
				rolePermission.setPermissionId(permission.getId());
				rolePermission.setTenantId(tenantModule.getTenantId());
				rolePermission.setCreateTime(now);
				rolePermissionss.add(rolePermission);
			}
		}
		this.rolePermissionService.insertBatch(rolePermissionss);

		// 添加至预设角色权限关系
		List<SysRolePermissionPreset> rolePermissionsPresets = Lists.newArrayList();
		List<SysRolePreset> rolePresets = this.rolePresetService.findAll();
		for (SysRolePreset rolePreset : rolePresets) {
			SysRolePermissionPreset rolePermissionPreset = new SysRolePermissionPreset();
			rolePermissionPreset.setPresetRoleId(rolePreset.getId());
			rolePermissionPreset.setPermissionId(permission.getId());
			rolePermissionPreset.setModuleId(permission.getModuleId());
			rolePermissionPreset.setEnable(false); // 默认不勾选
			rolePermissionPreset.setDelFlag(false); // 默认不删除
			rolePermissionPreset.setCreateTime(now);
			rolePermissionsPresets.add(rolePermissionPreset);
		}
		this.rolePermissionPresetService.insertBatch(rolePermissionsPresets);
	}
	
	@Override
	public void addPermissionRelation(Long moduleId,Long permissionId) {
		// 添加至角色权限表 1.查询所有机构 判断是否拥有当前模块 2.拥有模块则默认可选当前权限
		List<SysRolePermission> rolePermissionss = Lists.newArrayList();
		List<SysTenantModule> tenantModules = this.tenantModuleService.findByModuleId(moduleId);
		for (SysTenantModule tenantModule : tenantModules) {
			List<SysRole> roles = this.roleService.findByTenantId(tenantModule.getTenantId());
			for (SysRole role : roles) {
				SysRolePermission rolePermission = new SysRolePermission();
				rolePermission.setDelFlag(tenantModule.getDelFlag());
				rolePermission.setEnable(false); // 默认不勾选
				rolePermission.setRoleId(role.getId());
				rolePermission.setModuleId(moduleId);
				rolePermission.setPermissionId(permissionId);
				rolePermission.setTenantId(tenantModule.getTenantId());
				rolePermission.setCreateTime(new Date());
				rolePermissionss.add(rolePermission);
			}
		}
		this.rolePermissionService.insertBatch(rolePermissionss);

		// 添加至预设角色权限关系
		List<SysRolePermissionPreset> rolePermissionsPresets = Lists.newArrayList();
		List<SysRolePreset> rolePresets = this.rolePresetService.findAll();
		for (SysRolePreset rolePreset : rolePresets) {
			SysRolePermissionPreset rolePermissionPreset = new SysRolePermissionPreset();
			rolePermissionPreset.setPresetRoleId(rolePreset.getId());
			rolePermissionPreset.setPermissionId(permissionId);
			rolePermissionPreset.setModuleId(moduleId);
			rolePermissionPreset.setEnable(false); // 默认不勾选
			rolePermissionPreset.setDelFlag(false); // 默认不删除
			rolePermissionPreset.setCreateTime(new Date());
			rolePermissionsPresets.add(rolePermissionPreset);
		}
		this.rolePermissionPresetService.insertBatch(rolePermissionsPresets);
	}

	@Override
	public void edit(SysPermission permission) {
		this.checkPermission(permission);
		Date now = new Date();
		permission.setUpdateTime(now);
		this.updateById(permission);
	}

	/**
	 * 权限新增修改验证
	 * 
	 * @param permission
	 */
	private void checkPermission(SysPermission permission) {
		if (permission.getParentId() != null && permission.getParentId() > 0) {
			if (this.findByParentId(permission.getParentId()) == null) {
				throw ExceptionFactory.create("P_010");
			}
		}
		if (permission.getId() == null) { // 新增
			if (this.findByName(permission.getName()) != null) {
				throw ExceptionFactory.create("P_006");
			}
			if (!permission.getMenuFlag()) {
				// 权限
				if (permission.getCode() == null) {
					throw ExceptionFactory.create("P_007");
				}

				if (this.findByCode(permission.getCode()) != null) {
					throw ExceptionFactory.create("P_008");
				}
			} else {
				// 菜单
				permission.setCode(null);
			}

		} else { // 修改
			SysPermission beforePermission = this.selectById(permission.getId());
			// 权限修改，不可以修改至其他模块下,数据改动太大
			if (!beforePermission.getModuleId().equals(permission.getId())) {
				throw ExceptionFactory.create("P_011");
			}

			if (permission.getCode() != null) {
				SysPermission codePerrmission = this.findByCode(permission.getCode());
				if (codePerrmission != null && (!codePerrmission.getId().equals(permission.getId()))) {
					throw ExceptionFactory.create("P_008");
				}
			}
			if (permission.getName() != null) {
				SysPermission namePermission = this.findByName(permission.getName());
				if (namePermission != null && (!namePermission.getId().equals(permission.getId()))) {
					throw ExceptionFactory.create("P_006");
				}
			}

			// 如果是将菜单修改为权限，则需要判断其原始是否有下级权限存在，存在不能删除
			if (permission.getMenuFlag() != null) {
				if (beforePermission.getMenuFlag() && !permission.getMenuFlag()) {
					// 菜单改权限
					List<SysPermission> permissions = this.findByParentId(permission.getId());
					if (CollectionUtils.isNotEmpty(permissions)) {
						throw ExceptionFactory.create("P_009");
					}
					if (permission.getCode() == null) {
						throw ExceptionFactory.create("P_007");
					}
				} else {
					// 权限改菜单
					permission.setParentId(0L);
					this.permissionCodeToNull(permission.getId());
				}
			}
		}
	}

	/**
	 * 设置菜单cede为NUll
	 * 
	 * @param id
	 */
	private void permissionCodeToNull(Long id) {
		SysPermission sysPermission = sysPermissionMapper.selectById(id);
		sysPermission.setCode(null);
		sysPermissionMapper.updateAllColumnById(sysPermission);
	}

	@Override
	public SysPermission findByName(String name) {
		Wrapper<SysPermission> wrapper = new EntityWrapper<SysPermission>();
		wrapper.eq("name", name);
		return this.selectOne(wrapper);
	}

	@Override
	public List<SysPermission> findByParentId(Long parentId) {
		Wrapper<SysPermission> wrapper = new EntityWrapper<SysPermission>();
		wrapper.eq("parent_id", parentId);
		return this.selectList(wrapper);
	}

	/**
	 * 删除权限 <br/>
	 * 1.如果删除的是菜单，需要判断菜单下是否存在权限,存在权限不能删除 <br/>
	 * 2.删除角色权限关系表中的数据 <br/>
	 * 3.删除预设角色权限关系表数据 <br/>
	 * 
	 */
	@Override
	public void delete(Long id) {
		SysPermission permission = this.selectById(id);
		if (permission.getMenuFlag()) {
			if (CollectionUtils.isNotEmpty(this.findByParentId(id))) {
				throw ExceptionFactory.create("该菜单下存在权限不能删除");
			}
		}
		this.rolePermissionService.deleteByPermissionId(id);
		this.rolePermissionPresetService.deleteByPermissionId(id);
		this.deleteById(id);
	}

}
