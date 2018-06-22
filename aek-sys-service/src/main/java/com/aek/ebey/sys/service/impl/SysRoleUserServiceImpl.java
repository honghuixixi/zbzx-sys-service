package com.aek.ebey.sys.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aek.common.core.BeanMapper;
import com.aek.common.core.base.BaseServiceImpl;
import com.aek.common.core.serurity.WebSecurityUtils;
import com.aek.ebey.sys.enums.RoleDataScope;
import com.aek.ebey.sys.mapper.SysRoleUserMapper;
import com.aek.ebey.sys.model.SysRole;
import com.aek.ebey.sys.model.SysRoleUser;
import com.aek.ebey.sys.model.SysTenant;
import com.aek.ebey.sys.model.SysUser;
import com.aek.ebey.sys.model.vo.SysRoleVo;
import com.aek.ebey.sys.model.vo.SysTenantRoleVo;
import com.aek.ebey.sys.service.SysRolePermissionService;
import com.aek.ebey.sys.service.SysRoleService;
import com.aek.ebey.sys.service.SysRoleUserService;
import com.aek.ebey.sys.service.SysTenantService;
import com.aek.ebey.sys.service.SysUserService;
import com.aek.ebey.sys.service.ribbon.AuthClientService;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.google.common.collect.Lists;

/**
 * <p>
 * 用户角色关联表 服务实现类
 * </p>
 *
 * @author aek
 * @since 2017-05-06
 */
@Service
@Transactional
public class SysRoleUserServiceImpl extends BaseServiceImpl<SysRoleUserMapper, SysRoleUser>
		implements SysRoleUserService {
	
	private static final Logger logger = LogManager.getLogger(SysUserServiceImpl.class);
	
	@Autowired
	private SysUserService userService;

	@Autowired
	private SysTenantService tenantService;

	@Autowired
	private SysRoleService roleService;

	@Autowired
	private SysRolePermissionService rolePermissionService;

	@Autowired
	private SysRoleUserService roleUserService;

	@Autowired
	private AuthClientService authClientService;

	@Override
	@Transactional(readOnly = true)
	public List<SysRoleUser> findByRoleId(Long roleId) {
		Wrapper<SysRoleUser> wrapper = new EntityWrapper<SysRoleUser>();
		wrapper.eq("role_id", roleId).eq("enable", true);
		return this.selectList(wrapper);
	}

	@Override
	public void deactivateRoleUserByRoleId(Long roleId) {
		List<SysRoleUser> roleUsers = this.findByRoleId(roleId);

		if (CollectionUtils.isNotEmpty(roleUsers)) {
			for (SysRoleUser roleUser : roleUsers) {
				roleUser.setEnable(false);
			}
		}
		if (CollectionUtils.isNotEmpty(roleUsers)) {
			this.updateBatchById(roleUsers);
		}
	}

	@Override
	public void recoverRoleUserByRoleId(Long roleId) {
		Wrapper<SysRoleUser> wrapper = new EntityWrapper<SysRoleUser>();
		wrapper.eq("role_id", roleId).eq("enable", false);
		List<SysRoleUser> roleUsers = this.selectList(wrapper);

		for (SysRoleUser roleUser : roleUsers) {
			roleUser.setEnable(true);
		}
		if (CollectionUtils.isNotEmpty(roleUsers)) {
			this.updateBatchById(roleUsers);
		}
	}

	@Override
	@Transactional(readOnly = true)
	public List<SysUser> findUserByRoleIdAndStatus(Long roleId, Boolean status) {
		Wrapper<SysRoleUser> wrapper = new EntityWrapper<SysRoleUser>();
		wrapper.eq("role_id", roleId).eq("enable", true);

		List<SysRoleUser> roleUsers = this.selectList(wrapper);
		List<SysUser> users = Lists.newArrayList();

		for (SysRoleUser roleUser : roleUsers) {
			SysUser user = this.userService.selectById(roleUser.getUserId());

			// 按条件添加结果集
			if (user != null && !user.getDelFlag()) {
				if (status == null) {
					users.add(user);

				} else {
					if (status == user.getEnable()) {
						users.add(user);
					}
				}
			}
		}
		return users;
	}

	@Override
	@Transactional(readOnly = true)
	public List<SysTenantRoleVo> findRoleByUserId(Long userId) {
		//List<SysTenantRoleVo> tenantRoles = Lists.newArrayList();
		Set<SysTenantRoleVo> tenantRoles = new HashSet<SysTenantRoleVo>();
		Wrapper<SysRoleUser> wrapper = new EntityWrapper<SysRoleUser>();
		wrapper.eq("user_id", userId).eq("enable", true).groupBy("tenant_id").groupBy("user_id").groupBy("role_id").groupBy("id").groupBy("enable").orderBy("tenant_id");
		List<SysRoleUser> roleUsers = this.selectList(wrapper);
		Set<SysRoleUser> roleUsesSet = new HashSet<SysRoleUser>(roleUsers);
		Set<Long> tenantIds = new HashSet<Long>();
		for (SysRoleUser roleUser : roleUsesSet) {
			tenantIds.add(roleUser.getTenantId());
		}
		for (Long tenantId : tenantIds) {
			SysTenantRoleVo tenantRole = new SysTenantRoleVo();
			SysTenant tenant = this.tenantService.selectById(tenantId);
			tenantRole.setTenantId(tenant.getId());
			tenantRole.setTenantName(tenant.getName());
			List<SysRoleVo> roles = this.findByTenantIdAndUserId(tenantId, userId);
			tenantRole.setRoles(roles);
			tenantRoles.add(tenantRole);
		}
		List<SysTenantRoleVo> list = new ArrayList<SysTenantRoleVo>(tenantRoles);
		return list;
	}

	@Override
	@Transactional(readOnly = true)
	public List<SysRoleVo> findByTenantIdAndUserId(Long tenantId, Long userId) {
		Wrapper<SysRoleUser> wrapper = new EntityWrapper<SysRoleUser>();
		wrapper.eq("tenant_id", tenantId).eq("user_id", userId).eq("enable", true);
		List<SysRoleUser> roleUsers = this.selectList(wrapper);

		List<SysRoleVo> roles = Lists.newArrayList();
		Set<Long> roleIds = new HashSet<Long>();
		for (SysRoleUser roleUser : roleUsers) {
			roleIds.add(roleUser.getRoleId());
		}
		for (Long roleId : roleIds) {
			SysRole role = this.roleService.selectById(roleId);
			if (role != null) {
				roles.add(BeanMapper.map(role, SysRoleVo.class));
			}
		}
		return roles;
	}

	@Override
	@Transactional(readOnly = true)
	public List<SysTenantRoleVo> findAllRoleByUserId(Long userId) {
		
		SysUser user = this.userService.selectById(userId);
		SysTenant tenant = this.tenantService.selectById(user.getTenantId());
		
		//==========之前代码问题：当前机构存在很多子机构时，加载数据很慢，如爱医康机构存在很多子机构=============
		// 查询所有下级机构
		//List<SysTenant> subTenants = this.tenantService.findAllSubTenant(tenant.getId());
		// 添加当前机构
		//subTenants.add(0, tenant);
		//List<SysTenantRoleVo> tenantRoles = Lists.newArrayList();
		//for (SysTenant subTenant : subTenants) {
			//tenantRoles.add(this.findRoleByUserIdAndTenantId(userId, subTenant.getId()));
		//}
		//==========之前代码问题：当前机构存在很多子机构时，加载数据很慢，如爱医康机构存在很多子机构=============
		
		//==========改进方案：只加载当前机构角色列表===============
		List<SysTenantRoleVo> tenantRoles = this.tenantService.findAllRoleSubTenant(tenant.getId(),tenant.getParentIds());
		tenantRoles.add(0,this.findRoleByUserIdAndTenantId(userId, tenant.getId()));
		//==========改进方案：只加载当前机构角色列表===============
		return tenantRoles;
	}

	@Override
	@Transactional(readOnly = true)
	public SysTenantRoleVo findRoleByUserIdAndTenantId(Long userId, Long tenantId) {
		SysTenantRoleVo tenantRoleVo = new SysTenantRoleVo();
		SysTenant tenant = this.tenantService.selectById(tenantId);
		tenantRoleVo.setTenantId(tenant.getId());
		tenantRoleVo.setTenantName(tenant.getName());

		// 查询机构下所有可用角色
		List<SysRole> roles = this.roleService.findRoleListByTenantId(tenantId);
		List<SysRoleVo> roleVos = BeanMapper.mapList(roles, SysRoleVo.class);

		// 查询当前用户已拥有该机构的角色ids
		List<Long> haveRoleIds = this.findUserHaveRoleByTeant(userId, tenantId);

		// 是否选中
		for (SysRoleVo vo : roleVos) {
			boolean check = haveRoleIds.contains(vo.getId()) ? true : false;
			vo.setCheck(check);
		}
		tenantRoleVo.setRoles(roleVos);
		return tenantRoleVo;
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<SysTenantRoleVo> findRoleByUserIdAndSubTenantList(Long userId,List<SysTenant> subTenants){
		List<SysTenantRoleVo> tenantRoles = Lists.newArrayList();
		for (SysTenant sysTenant : subTenants) {
			SysTenantRoleVo tenantRoleVo = new SysTenantRoleVo();
			tenantRoleVo.setTenantId(sysTenant.getId());
			tenantRoleVo.setTenantName(sysTenant.getName());
			
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("userId", userId);
			map.put("tenantId", sysTenant.getId());
			//查询机构下所有可用角色+是否选中
			List<SysRoleVo> roleVos = this.roleService.findByUserIdAndTenantId(map);

			tenantRoleVo.setRoles(roleVos);
			
			tenantRoles.add(tenantRoleVo);
		}
		return tenantRoles;
	}

	/**
	 * 查询用户指定租户下拥有的角色ids
	 * 
	 * @param userId
	 *            用户ID
	 * @param tenantId
	 *            角色ID
	 * @return
	 */
	private List<Long> findUserHaveRoleByTeant(Long userId, Long tenantId) {
		List<Long> roleIds = Lists.newArrayList();

		Wrapper<SysRoleUser> wrapper = new EntityWrapper<SysRoleUser>();
		wrapper.eq("tenant_id", tenantId).eq("user_id", userId).eq("enable", true);
		List<SysRoleUser> roleUsers = this.selectList(wrapper);
		if (CollectionUtils.isNotEmpty(roleUsers)) {
			for (SysRoleUser roleUser : roleUsers) {
				roleIds.add(roleUser.getRoleId());
			}
		}
		return roleIds;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void modifyUserRoleByTenant(SysTenantRoleVo tenantRoleVo) {

		List<Long> roleIds = tenantRoleVo.getRoleIds();
		Long userId = tenantRoleVo.getUserId();
		Long tenantId = tenantRoleVo.getTenantId();

		// 删除用户指定租户的角色信息
		this.deleteByTenantId(userId, tenantId);

		List<SysRoleUser> roleUsers = Lists.newArrayList();
		
		Set<Long> roleIdsSet = new HashSet(roleIds);
		// 再次添加
		if (CollectionUtils.isNotEmpty(roleIds)) {
			for (Long roleId : roleIdsSet) {
				SysRoleUser roleUser = new SysRoleUser();
				roleUser.setEnable(true);
				roleUser.setRoleId(roleId);
				roleUser.setUserId(userId);
				roleUser.setTenantId(tenantId);
				roleUsers.add(roleUser);
			}
			if(0 != roleUsers.size()){
				this.insertBatch(roleUsers);
			}
		}
		this.delUserCahce(userId);
	}

	@Override
	public void modifyUserRoleAll(List<SysTenantRoleVo> tenantRoleVos) {
		if (CollectionUtils.isNotEmpty(tenantRoleVos)) {
			for (SysTenantRoleVo tenantRoleVo : tenantRoleVos) {
				this.modifyUserRoleByTenant(tenantRoleVo);
			}
		}
	}

	@Override
	public void deleteByTenantId(Long userId, Long tenantId) {
		// 删除用户指定租户的角色信息
		Wrapper<SysRoleUser> wrapper = new EntityWrapper<SysRoleUser>();
		wrapper.eq("user_id", userId).eq("tenant_id", tenantId);
		this.delete(wrapper);

		this.delUserCahce(userId);

	}

	/**
	 * 用户角色修改或变动，删除使用当前角色的用户登录缓存信息
	 * 
	 * @param userId 用户ID
	 */
	private void delUserCahce(Long userId) {
		final SysUser user = userService.selectById(userId);
		final String token = WebSecurityUtils.getCurrentToken();
		//异步删除使用当前角色的用户权限缓存
		new Thread() {
			@Override
			public void run() {
				logger.info("=================用户[mobile="+user.getMobile()+"]角色发生变化时,清除用户缓存开始=========================");
				Map<String, Object> clearCurrentUserCacheResult = authClientService.removeUser(user.getMobile(), token);
				logger.info("=================用户[mobile="+user.getMobile()+"]角色发生变化时，清除用户缓存结果="+clearCurrentUserCacheResult.toString()+"=========================");
			}
		}.start();
	}

	@Override
	@Transactional(readOnly = true)
	public int findUserCountByRoleId(Long roleId) {
		Wrapper<SysRoleUser> wrapper = new EntityWrapper<SysRoleUser>();
		wrapper.eq("role_id", roleId);
		return this.selectCount(wrapper);
	}

	@Override
	@Transactional(readOnly = true)
	public SysRole findUserAdminRole(Long userId) {

		SysUser user = this.userService.selectById(userId);
		SysRole role = this.roleService.findAdminRoleByTenantId(user.getTenantId());

		Wrapper<SysRoleUser> wrapper = new EntityWrapper<SysRoleUser>();
		wrapper.eq("role_id", role.getId()).eq("user_id", userId).eq("tenant_id", user.getTenantId()).eq("enable",
				true);
		SysRoleUser roleUser = this.selectOne(wrapper);

		// 判断当前用户是否拥有
		if (roleUser != null) {
			return role;
		}
		return null;
	}

	@Override
	@Transactional(readOnly = true)
	public int findUserDateScopeByPermissId(Long userId, Long tenantId, Long permissId) {
		// 判断当前用户是否拥有租户admin角色,管理员角色拥有租户内最大数据范围
		SysRole adninRole = this.roleUserService.findUserAdminRole(userId);
		if (adninRole != null) {
			return RoleDataScope.ORG_ALL.getNumber();
		}

		// 假定数据范围最低
		int dataScoper = RoleDataScope.DEPT.getNumber();

		// 获取当前用户拥有角色
		List<SysRole> roles = this.findRoleByTenantIdAndUserId(userId, tenantId);

		if (CollectionUtils.isNotEmpty(roles)) {
			for (SysRole role : roles) {
				// 角色是否拥有该权限
				if (this.rolePermissionService.isCanPermission(role.getId(), permissId)) {
					int roleDataScope = role.getDataScope();

					// 最高数据范围，直接返回
					if (RoleDataScope.ORG_ALL.getNumber().equals(roleDataScope)) {
						return RoleDataScope.ORG_ALL.getNumber();
					}
					if (roleDataScope < dataScoper) {
						dataScoper = roleDataScope;
					}

				}
			}
		}
		return dataScoper;
	}

	@Override
	@Transactional(readOnly = true)
	public List<SysRole> findRoleByTenantIdAndUserId(Long tenantId, Long userId) {

		Wrapper<SysRoleUser> wrapper = new EntityWrapper<SysRoleUser>();
		wrapper.eq("tenant_id", tenantId).eq("user_id", userId).eq("enable", true);
		List<SysRoleUser> roleUsers = this.selectList(wrapper);

		List<SysRole> roles = Lists.newArrayList();
		if (CollectionUtils.isNotEmpty(roleUsers)) {
			for (SysRoleUser roleUser : roleUsers) {
				roles.add(this.roleService.selectById(roleUser.getRoleId()));
			}
		}
		return roles;
	}

}
