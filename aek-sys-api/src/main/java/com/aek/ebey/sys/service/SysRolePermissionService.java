package com.aek.ebey.sys.service;

import com.aek.ebey.sys.model.SysRolePermission;

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
public interface SysRolePermissionService extends BaseService<SysRolePermission> {
	/**
	 * 根据角色ID查询角色权限关系
	 * 
	 * @param roleId
	 * @return
	 */
	List<SysRolePermission> findByRoleId(Long roleId);

	/**
	 * 根据租户ID和模块ID查询
	 * 
	 * @param tenantId
	 *            租户ID
	 * 
	 * @param moduleId
	 *            模块ID
	 * @return
	 */
	List<SysRolePermission> findByTenantIdAndModuleId(Long tenantId, Long moduleId);

	/**
	 * 判断角色是否拥有指定权限 （enable= true,dep_flag = false）
	 * 
	 * @param role
	 * @return
	 */
	boolean isCanPermission(Long roleId, Long permissionId);

	/**
	 * 根据moduleId删除该模块下的权限角色关系
	 * 
	 * @param moduleId
	 */
	void deleteByModuleId(Long moduleId);

	/**
	 * 根据权限ID删除
	 * 
	 * @param permissionId
	 */
	void deleteByPermissionId(Long permissionId);

}
