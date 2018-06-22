package com.aek.ebey.sys.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aek.common.core.BeanMapper;
import com.aek.common.core.base.BaseServiceImpl;
import com.aek.common.core.exception.ExceptionFactory;
import com.aek.common.core.serurity.WebSecurityUtils;
import com.aek.common.core.serurity.model.AuthUser;
import com.aek.ebey.sys.core.SysConstants;
import com.aek.ebey.sys.enums.RoleDataScope;
import com.aek.ebey.sys.mapper.SysRoleMapper;
import com.aek.ebey.sys.model.SysDept;
import com.aek.ebey.sys.model.SysPermission;
import com.aek.ebey.sys.model.SysRole;
import com.aek.ebey.sys.model.SysRolePermission;
import com.aek.ebey.sys.model.SysRolePermissionPreset;
import com.aek.ebey.sys.model.SysRolePreset;
import com.aek.ebey.sys.model.SysRoleUser;
import com.aek.ebey.sys.model.SysTenantModule;
import com.aek.ebey.sys.model.SysUser;
import com.aek.ebey.sys.model.bo.SysDeptBo;
import com.aek.ebey.sys.model.custom.RoleCustom;
import com.aek.ebey.sys.model.query.RoleQuery;
import com.aek.ebey.sys.model.vo.MenuVo;
import com.aek.ebey.sys.model.vo.ModuleMenuVo;
import com.aek.ebey.sys.model.vo.SysPermissionVo;
import com.aek.ebey.sys.model.vo.SysRoleVo;
import com.aek.ebey.sys.model.vo.SysTenantRoleVo;
import com.aek.ebey.sys.service.SysDeptService;
import com.aek.ebey.sys.service.SysPermissionService;
import com.aek.ebey.sys.service.SysRolePermissionPresetService;
import com.aek.ebey.sys.service.SysRolePermissionService;
import com.aek.ebey.sys.service.SysRolePresetService;
import com.aek.ebey.sys.service.SysRoleService;
import com.aek.ebey.sys.service.SysRoleUserService;
import com.aek.ebey.sys.service.SysTenantModuleService;
import com.aek.ebey.sys.service.SysUserService;
import com.aek.ebey.sys.service.ribbon.AuthClientService;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.google.common.collect.Lists;

/**
 * <p>
 * 角色表 服务实现类
 * </p>
 *
 * @author aek
 * @since 2017-05-06
 */
@Service
@Transactional
public class SysRoleServiceImpl extends BaseServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {

	private static final Logger logger = LogManager.getLogger(SysRoleServiceImpl.class);

	@Autowired
	private SysPermissionService permissionService;

	@Autowired
	private SysRolePermissionService rolePermissionService;

	@Autowired
	private SysUserService sysUserService;

	@Autowired
	private SysRolePresetService rolePresetService;

	@Autowired
	private SysRolePermissionPresetService rolePermissionPresetService;

	@Autowired
	private SysTenantModuleService tenantModuleService;

	@Autowired
	private SysRoleUserService roleUserService;

	@Autowired
	private AuthClientService authClientService;
	
	@Autowired
	private SysRoleMapper sysRoleMapper;
	
	@Autowired
	private SysDeptService sysDeptService;

	@Override
	public void initTenantRole(Long tenantId) {
		Date date = new Date();

		// 根据租户类型查询可用模板角色
		List<SysRolePreset> presetRoles = this.rolePresetService.findRolePresetByTenantType(tenantId);
		List<SysRolePermission> rolePermissions = Lists.newArrayList();

		// 查询当前机构所拥有的模块
		List<Long> tenantModultIds = this.tenantModuleService.findTenantModuleIdByTenantId(tenantId);

		// 插入所有角色及角色权限关系
		for (SysRolePreset presetRole : presetRoles) {
			SysRole role = new SysRole();
			role.setTenantId(tenantId);
			role.setName(presetRole.getName());
			role.setPresetId(presetRole.getId());
			role.setDataScope(presetRole.getDataScope());
			role.setDescript(presetRole.getDescript());
			role.setCreateTime(date);
			role.setDelFlag(false);
			role.setEnable(true);
			this.insert(role);

			// 查询模板角色权限
			List<SysRolePermissionPreset> presetRolePermissions = this.rolePermissionPresetService
					.findByPresetRoleId(presetRole.getId());

			for (SysRolePermissionPreset presetRolePermis : presetRolePermissions) {
				SysRolePermission rolePermis = new SysRolePermission();
				rolePermis.setTenantId(tenantId);
				rolePermis.setModuleId(presetRolePermis.getModuleId());
				rolePermis.setPermissionId(presetRolePermis.getPermissionId());
				rolePermis.setRoleId(role.getId());
				rolePermis.setCreateTime(date);
				rolePermis.setEnable(presetRolePermis.getEnable());

				// 已拥有模块权限开放
				boolean delFlag = tenantModultIds.contains(presetRolePermis.getModuleId()) ? false : true;
				rolePermis.setDelFlag(delFlag);

				rolePermissions.add(rolePermis);
			}
		}
		if (rolePermissions.size() != 0) {
			this.rolePermissionService.insertBatch(rolePermissions);
		}
	}

	@Override
	public SysRole findAdminRoleByTenantId(Long tanantId) {
		Wrapper<SysRole> wrapper = new EntityWrapper<SysRole>();
		wrapper.eq("preset_id", SysConstants.TEMPLET_ADMIN_ROLE_ID).eq("tenant_id", tanantId);
		return this.selectOne(wrapper);
	}

	@Override
	public void addRoleToUser(Long roleId, Long tenantId, Long userId) {
		SysRoleUser roleUser = new SysRoleUser();
		roleUser.setRoleId(roleId);
		roleUser.setUserId(userId);
		roleUser.setTenantId(tenantId);
		this.roleUserService.insert(roleUser);
	}

	@Override
	public void addRole(SysRoleVo roleVo) {
		Date currTime = new Date();

		// 插入角色信息
		SysRole role = BeanMapper.map(roleVo, SysRole.class);
		role.setCreateTime(currTime);
		AuthUser authUser = WebSecurityUtils.getCurrentUser();
		role.setCreateBy(authUser.getId());
		
		// 自定义部门数据时，处理角色custom信息，添加自定义部门
		if(RoleDataScope.DEPT_DEFINED.getNumber().equals(role.getDataScope())){
			role.setCustomData(JSON.toJSONString(roleVo.getRoleCustom()));
		}
		
		this.insert(role);

		// 角色拥有权限
		List<Long> permissionIds = roleVo.getPermissionIds();

		// 租户已拥有模块ID
		List<Long> moduleIds = this.tenantModuleService.findTenantModuleIdByTenantId(role.getTenantId());

		// 根据租户ID,获取可拥有权限列表
		List<SysPermission> permissions = this.permissionService.findPermissionByTenantIdWithCan(roleVo.getTenantId());

		List<SysRolePermission> rolePermissions = Lists.newArrayList();

		for (SysPermission permission : permissions) {
			SysRolePermission rolePermission = new SysRolePermission();
			rolePermission.setRoleId(role.getId());
			rolePermission.setTenantId(role.getTenantId());
			rolePermission.setPermissionId(permission.getId());
			rolePermission.setModuleId(permission.getModuleId());
			rolePermission.setCreateTime(currTime);

			boolean delFlag = moduleIds.contains(permission.getModuleId()) ? false : true;
			boolean enable = permissionIds.contains(permission.getId()) ? true : false;
			rolePermission.setDelFlag(delFlag);
			rolePermission.setEnable(enable);
			rolePermissions.add(rolePermission);
		}

		// 插入角色权限关系表
		if (CollectionUtils.isNotEmpty(rolePermissions)) {
			this.rolePermissionService.insertBatch(rolePermissions);

		}
	}

	@Override
	public void modifyRole(SysRoleVo roleVo) {
		// 查询并判断预设角色不能修改
		SysRole currRole = this.selectById(roleVo.getId());
		if (null == currRole) {
			throw ExceptionFactory.create("R_002");
		}

		// 模板角色不能修改
		if (null != currRole.getPresetId() && currRole.getPresetId() > 0) {
			throw ExceptionFactory.create("R_006");
		}

		Date currTime = new Date();
		SysRole role = BeanMapper.map(roleVo, SysRole.class);
		role.setUpdateTime(currTime);
		
		// 自定义部门数据时，处理角色custom信息，添加自定义部门
		RoleCustom roleCustom = roleVo.getRoleCustom();
		if(RoleDataScope.DEPT_DEFINED.getNumber().equals(role.getDataScope()) && null != roleCustom){
			role.setCustomData(JSON.toJSONString(roleCustom));
		}
		
		this.updateById(role);

		if(null == roleVo.getIsEditName() || !roleVo.getIsEditName()){
			// 角色拥有权限
			List<Long> permissionIds = roleVo.getPermissionIds();
			List<SysRolePermission> rolePermissions = this.rolePermissionService.findByRoleId(role.getId());			
			if (CollectionUtils.isNotEmpty(permissionIds)) {
				for (SysRolePermission rolePermission : rolePermissions) {
					boolean enable = permissionIds.contains(rolePermission.getPermissionId()) ? true : false;
					rolePermission.setEnable(enable);
				}
			} else {
				for (SysRolePermission rolePermission : rolePermissions) {
					rolePermission.setEnable(false);
				}
			}
			this.rolePermissionService.updateBatchById(rolePermissions);

			// 异步删除使用当前角色的用户权限缓存
			this.delUserCahceByRole(roleVo.getId());
		}
	}

	@Override
	public SysRoleVo findRoleDetai(Long roleId) {
		SysRole role = this.selectById(roleId);
		if (role == null) {
			throw ExceptionFactory.create("R_002");
		}
		SysRoleVo vo = BeanMapper.map(role, SysRoleVo.class);
		SysUser createUser = this.sysUserService.selectById(vo.getCreateBy());
		if (createUser != null) {
			vo.setCreateName(createUser.getRealName());
		} else {
			vo.setCreateName("系统预设");
		}
		
		// 处理RoleCustom信息
		String customData = vo.getCustomData();
		if(StringUtils.isNotBlank(customData)){
			RoleCustom roleCustom = JSON.parseObject(customData, RoleCustom.class);
			List<SysDeptBo> depts = roleCustom.getDepts();
			for (SysDeptBo sysDeptBo : depts) {
				SysDept sysDept = sysDeptService.selectById(sysDeptBo.getDeptId());
				sysDeptBo.setDeptName(sysDept==null?"":sysDept.getName());
			}
			vo.setRoleCustom(roleCustom);
		}

		// 模块
		List<ModuleMenuVo> moduleMenuVos = vo.getModuleMenus();

		// 查询角色涉及的模块
		List<SysTenantModule> tenantModules = this.tenantModuleService.findModuleByRoleId(roleId);
		for (SysTenantModule tenantModule : tenantModules) {
			ModuleMenuVo moduleMenuVo = new ModuleMenuVo();
			moduleMenuVo.setModuleName(tenantModule.getName());

			// 否拥有该模块
			moduleMenuVo.setDelFlag(tenantModule.getDelFlag());
			moduleMenuVo.setEnable(tenantModule.getEnable());

			// 模块中菜单列表
			List<MenuVo> menusVo = moduleMenuVo.getMenus();
			List<SysPermissionVo> menus = this.permissionService.findMenuByRoleIdAndModuleId(roleId,
					tenantModule.getModuleId());

			for (SysPermissionVo menu : menus) {
				MenuVo menuVo = new MenuVo();
				menuVo.setMenuId(menu.getId());
				menuVo.setMenuName(menu.getName());
				menuVo.setDelFlag(menu.getDelFlag());
				menuVo.setEnable(menu.getEnable());

				// 菜单中的权限
				List<SysPermissionVo> permissions = this.permissionService.findByMenuIdAndRoleId(menu.getId(), roleId);
				menuVo.setPermissions(permissions);
				menusVo.add(menuVo);
			}

			// 不在菜单中的权限
			List<SysPermissionVo> permissions = this.permissionService.findByModuleIdAndRoleIdWithNotInMenu(roleId,
					tenantModule.getModuleId());
			moduleMenuVo.setPermissions(permissions);

			moduleMenuVos.add(moduleMenuVo);
		}
		return vo;
	}

	@Override
	public List<ModuleMenuVo> baseRoleInfo(Long tenantId) {
		List<ModuleMenuVo> moduleMenuVos = Lists.newArrayList();

		// 查询机构可拥有模块
		List<SysTenantModule> tenantModules = this.tenantModuleService.findAllByTenantId(tenantId);
		for (SysTenantModule tenantModule : tenantModules) {
			ModuleMenuVo moduleMenuVo = new ModuleMenuVo();
			moduleMenuVo.setModuleName(tenantModule.getName());
			moduleMenuVo.setDelFlag(tenantModule.getDelFlag());
			moduleMenuVo.setEnable(tenantModule.getEnable());
			List<MenuVo> menuVos = moduleMenuVo.getMenus();

			// 根据模块ID查询模块下所有菜单
			List<SysPermission> menus = this.permissionService.findMenuByModuleId(tenantModule.getModuleId());
			for (SysPermission menu : menus) {
				MenuVo menuVo = new MenuVo();
				menuVo.setMenuId(menu.getId());
				menuVo.setMenuName(menu.getName());
				menuVo.setDelFlag(tenantModule.getDelFlag());
				menuVo.setEnable(false);

				// 菜单中的权限并设置是否拥有该权限
				List<SysPermissionVo> permissionVos = this.permissionService.findByMenuId(menu.getId());
				for (SysPermissionVo vo : permissionVos) {
					vo.setDelFlag(tenantModule.getDelFlag());
				}

				menuVo.setPermissions(permissionVos);
				menuVos.add(menuVo);
			}
			// 不在菜单中的权限并设置是否拥有该权限
			List<SysPermissionVo> permissionVos = this.permissionService
					.findByModuleIdWithNotMenu(tenantModule.getModuleId());
			for (SysPermissionVo vo : permissionVos) {
				vo.setDelFlag(tenantModule.getDelFlag());
			}

			moduleMenuVo.setPermissions(permissionVos);
			moduleMenuVos.add(moduleMenuVo);
		}
		return moduleMenuVos;
	}

	@Override
	public void deleteRoleById(Long roleId) {
		SysRole role = this.selectById(roleId);
		if (null == role) {
			throw ExceptionFactory.create("R_002");
		}

		// 模板角色不能删除
		if (null != role.getPresetId() && role.getPresetId() > 0) {
			throw ExceptionFactory.create("R_006");
		}

		// 角色被使用不能删除
		List<SysRoleUser> roleUsers = this.roleUserService.findByRoleId(roleId);
		if (CollectionUtils.isNotEmpty(roleUsers)) {
			throw ExceptionFactory.create("R_005");
		}

		// 更新逻辑删除
		role.setDelFlag(true);
		role.setEnable(false);
		this.updateById(role);

		// 角色修改后，操作缓存，触发登录用户
		this.delUserCahceByRole(roleId);
	}

	/**
	 * 角色修改或变动，删除使用当前角色的用户登录缓存信息
	 * 
	 * @param roleId
	 *            角色ID
	 */
	private void delUserCahceByRole(Long roleId) {
		final String token = WebSecurityUtils.getCurrentToken();
		// 异步删除使用当前角色的用户权限缓存
		new Thread() {
			@Override
			public void run() {
				logger.info("=================角色[roleId=" + roleId + "]角色发生变化时,清除用户缓存开始=========================");
				Map<String, Object> clearCurrentUserCacheResult = authClientService.removePerms(roleId, token);
				logger.info("=================角色[roleId=" + roleId + "]角色发生变化时，清除用户缓存结果="
						+ clearCurrentUserCacheResult.toString() + "=========================");
			}
		}.start();
	}

	@Override
	public void stopRole(Long roleId, Boolean isForced) {
		SysRole role = this.selectById(roleId);
		if (null == role) {
			throw ExceptionFactory.create("R_002");
		}

		// 强制操作不进行判断，直接停用角色
		if (!isForced) {
			List<SysRoleUser> roleUsers = this.roleUserService.findByRoleId(roleId);
			if (CollectionUtils.isNotEmpty(roleUsers)) {
				throw ExceptionFactory.create("R_007", String.valueOf(roleUsers.size()));
			}
		}

		// 停用角色用户关系
		this.roleUserService.deactivateRoleUserByRoleId(roleId);

		role.setEnable(false);
		this.updateById(role);

		// 处理使用当前角色的用户
		this.delUserCahceByRole(roleId);
	}

	@Override
	public void recoverRoleByRoleId(Long roleId) {
		SysRole role = this.selectById(roleId);
		if (null == role) {
			throw ExceptionFactory.create("R_002");
		}
		// 已删除角色不能再次启用
		if (role.getDelFlag()) {
			throw ExceptionFactory.create("R_008");
		}
		// 恢复使用当前角色的用户
		this.roleUserService.recoverRoleUserByRoleId(roleId);

		role.setEnable(true);
		this.updateById(role);
	}

	@Override
	public List<SysRole> findRoleListByTenantId(Long tenantId) {
		Wrapper<SysRole> wrapper = new EntityWrapper<SysRole>();
		wrapper.eq("tenant_id", tenantId).eq("enable", true).eq("del_flag", false);
		return this.selectList(wrapper);
	}

	@Override
	public Page<SysRole> findByPage(RoleQuery query) {
		Wrapper<SysRole> wrapper = new EntityWrapper<SysRole>();
		wrapper.eq("tenant_id", query.getTenantId()).eq("del_flag", false);
		if (query.getEnable() != null) {
			wrapper.eq("enable", query.getEnable());
		}

		if (query.getRoleType() != null) {
			if (query.getRoleType().equals(2)) {
				wrapper.andNew("preset_id is null").or("preset_id=0");
			} else if (query.getRoleType().equals(1)) {
				wrapper.gt("preset_id", 0);
			}
		}

		String keyword = StringUtils.trimToNull(query.getKeyword());
		if (StringUtils.isNotBlank(keyword)) {
			wrapper.andNew("name like {0}", "%" + keyword + "%");
		}
		wrapper.orderBy("id");
		Page<SysRole> page = this.selectPage(query.getPage(), wrapper);
		List<SysRole> roles = page.getRecords();

		for (SysRole role : roles) {
			// 查询使用该角色的用户
			role.setUserCount(this.roleUserService.findUserCountByRoleId(role.getId()));
			//处理角色包含权限显示
			if(StringUtils.isBlank(role.getDescript())){
				StringBuilder descript = new StringBuilder();
				SysRoleVo sysRoleVo = this.findRoleDetai(role.getId());
				if(null != sysRoleVo && null != sysRoleVo.getModuleMenus()){
					List<ModuleMenuVo> moduleMenuVoList = sysRoleVo.getModuleMenus();
					List<ModuleMenuVo> existsModuleMenuVoList = new ArrayList<ModuleMenuVo>(); 
					for (ModuleMenuVo moduleMenuVo : moduleMenuVoList) {
						if((!moduleMenuVo.getDelFlag()) && moduleMenuVo.getEnable()){
							existsModuleMenuVoList.add(moduleMenuVo);
						}
					}
					for(int i = 0; i < existsModuleMenuVoList.size(); i++){
						List<SysPermissionVo> permissions = existsModuleMenuVoList.get(i).getPermissions();
						List<SysPermissionVo> existPermissions = new ArrayList<SysPermissionVo>();
						for (SysPermissionVo sysPermissionVo : permissions) {
							if((!sysPermissionVo.getDelFlag()) && sysPermissionVo.getEnable()){
								existPermissions.add(sysPermissionVo);
							}
						}
						List<MenuVo> menus = existsModuleMenuVoList.get(i).getMenus();
						for (MenuVo menuVo : menus) {
							List<SysPermissionVo> menuPermissions = menuVo.getPermissions();
							for (SysPermissionVo sysPermissionVo : menuPermissions) {
								if((!sysPermissionVo.getDelFlag()) && sysPermissionVo.getEnable()){
									existPermissions.add(sysPermissionVo);
								}
							}
						}
						
						if(existPermissions.size() > 0){
							descript.append(existsModuleMenuVoList.get(i).getModuleName()+"[");
							for(int k = 0; k < existPermissions.size(); k++){
								descript.append(existPermissions.get(k).getName());
								if(k != existPermissions.size() - 1){
									descript.append(";");
								}
							}
							if(i == existsModuleMenuVoList.size()-1){
								descript.append("]");
							}else{
								descript.append("],");
							}
						}
					}
				}
				String desc = descript.toString();
				if(StringUtils.isNotBlank(descript.toString()) && descript.toString().endsWith(",")){
					desc = descript.toString().substring(0, descript.toString().length()-1);
				}
				role.setDescript(desc);
			}
		}
		return page;
	}

	@Override
	public List<SysRole> findByTenantId(Long tenantId) {
		Wrapper<SysRole> wrapper = new EntityWrapper<SysRole>();
		wrapper.eq("tenant_id", tenantId);
		return this.selectList(wrapper);
	}

	@Override
	public List<SysRoleVo> findByUserIdAndTenantId(Map<String, Object> map) {
		return sysRoleMapper.selectByUserIdAndTenantId(map);
	}

	@Override
	public List<SysTenantRoleVo> findAllRoleByUserIdAndTenantId(Long userId,Long tenantId,String parentIds){
		return sysRoleMapper.selectAllRoleByUserIdAndTenantId(userId,tenantId,parentIds);
	}
	
}