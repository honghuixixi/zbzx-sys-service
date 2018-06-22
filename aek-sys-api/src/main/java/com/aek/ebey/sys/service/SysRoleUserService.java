package com.aek.ebey.sys.service;

import com.aek.ebey.sys.model.SysRole;
import com.aek.ebey.sys.model.SysRoleUser;
import com.aek.ebey.sys.model.SysTenant;
import com.aek.ebey.sys.model.SysUser;
import com.aek.ebey.sys.model.vo.SysRoleVo;
import com.aek.ebey.sys.model.vo.SysTenantRoleVo;

import java.util.List;

import com.aek.common.core.base.BaseService;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author aek
 * @since 2017-05-06
 */
public interface SysRoleUserService extends BaseService<SysRoleUser> {

	/**
	 * 根据角色ID查询使用当前角色的用户
	 * 
	 * @param roleId
	 *            角色ID
	 * @return
	 */
	List<SysRoleUser> findByRoleId(Long roleId);

	/**
	 * 停用使用当前角色的用户
	 * 
	 * @param roleId
	 *            角色ID
	 */
	void deactivateRoleUserByRoleId(Long roleId);

	/**
	 * 根据角色恢复使用当前角色的关系
	 * 
	 * @param roleId
	 *            角色ID
	 */
	void recoverRoleUserByRoleId(Long roleId);

	/**
	 * 根据角色Id 和用户状态查询 用户信息
	 * 
	 * @param roleId
	 * @param status
	 * @return
	 */
	List<SysUser> findUserByRoleIdAndStatus(Long roleId, Boolean status);

	/**
	 * 根据用户ID查询用户角色信息
	 * 
	 * @param userId
	 *            用户ID
	 * @return
	 */
	List<SysTenantRoleVo> findRoleByUserId(Long userId);

	/**
	 * 查询用户所有机构及下属机构的角色信息（包含选中未选中状态）
	 * 
	 * @param userId
	 * @return
	 */
	List<SysTenantRoleVo> findAllRoleByUserId(Long userId);

	/**
	 * 查询用户指定租户的角色（包含选中未选中状态）
	 * 
	 * @param userId
	 * @param tenantId
	 * @return
	 */
	SysTenantRoleVo findRoleByUserIdAndTenantId(Long userId, Long tenantId);

	/**
	 * 查询用户指定租户下的角色
	 * 
	 * @param userId
	 * @param tenantId
	 * @return
	 */
	List<SysRole> findRoleByTenantIdAndUserId(Long tenantId, Long userId);

	/**
	 * 查询用户指定租户的角色
	 * 
	 * @param tenantId
	 * @param userId
	 * @return
	 */
	List<SysRoleVo> findByTenantIdAndUserId(Long tenantId, Long userId);

	/**
	 * 修改用户指定租户的角色
	 * 
	 * @param tenantRoleVo
	 */
	void modifyUserRoleByTenant(SysTenantRoleVo tenantRoleVo);

	/**
	 * 编辑用户所有的权限
	 * 
	 * @param tenantRoleVos
	 */
	void modifyUserRoleAll(List<SysTenantRoleVo> tenantRoleVos);

	/**
	 * 删除用户指定租户的全部角色
	 * 
	 * @param tenantId
	 */
	void deleteByTenantId(Long userId, Long tenantId);

	/**
	 * 查询使用当前角色的用户数量
	 * 
	 * @param roleId
	 * @return
	 */
	int findUserCountByRoleId(Long roleId);

	/**
	 * 查询用户是否拥有机构admin角色
	 * 
	 * @param userId
	 * @return
	 */
	SysRole findUserAdminRole(Long userId);

	/**
	 * 查询用户指定权限的数据范围
	 * 
	 * @param userId
	 * @param PermissId
	 * @return 0 表示没有权限数据范围
	 */
	int findUserDateScopeByPermissId(Long userId, Long tenantId, Long PermissId);

	/**
	 * 查询当前用户在所有子机构的角色集合
	 * @param userId
	 * @param subTenants
	 * @return
	 */
	List<SysTenantRoleVo> findRoleByUserIdAndSubTenantList(Long userId,List<SysTenant> subTenants);

}
