package com.aek.ebey.sys.service;

import java.util.List;

import com.aek.common.core.base.BaseService;
import com.aek.ebey.sys.model.SysPermission;
import com.aek.ebey.sys.model.vo.SysPermissionVo;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author aek
 * @since 2017-05-06
 */
public interface SysPermissionService extends BaseService<SysPermission> {
	/**
	 * 查询指定模块的权限及菜单
	 * 
	 * @param moduleId
	 * @return
	 */
	List<SysPermission> findByModuleId(Long moduleId);

	/**
	 * 根据租户ID查询该租户可拥有权限列表
	 * 
	 * @param tenantId
	 * @return
	 */
	List<SysPermission> findPermissionByTenantIdWithCan(Long tenantId);

	/**
	 * 根据角色ID和模块ID，查询菜单列表
	 * 
	 * @param roleId
	 *            角色DI
	 * @param moduleId
	 * @return
	 */
	List<SysPermissionVo> findMenuByRoleIdAndModuleId(Long roleId, Long moduleId);

	/**
	 * 根据角色ID和菜单ID，查询菜单中权限
	 * 
	 * @param menuId
	 * 
	 * @param roleId
	 * @return
	 */
	List<SysPermissionVo> findByMenuIdAndRoleId(Long menuId, Long roleId);

	/**
	 * 根据角色和模块查询不在菜单中的权限
	 * 
	 * @param roleId
	 *            角色ID
	 * @param moduleId
	 *            模块ID
	 * @return
	 */
	List<SysPermissionVo> findByModuleIdAndRoleIdWithNotInMenu(Long roleId, Long moduleId);

	/**
	 * 根据模块ID查询模块下所有菜单
	 * 
	 * @param moduleId
	 * @return
	 */
	List<SysPermission> findMenuByModuleId(Long moduleId);

	/**
	 * 根据菜单ID查询权限
	 * 
	 * @param menuId
	 * @return
	 */
	List<SysPermissionVo> findByMenuId(Long menuId);

	/**
	 * 根据用户ID查询服务集合， 关联角色表，查询不重复记录
	 * 
	 * @param userId
	 * @return
	 */
	List<SysPermission> findServicesByUserId(Long userId);

	/**
	 * 根据模块查询不在菜单中的权限
	 * 
	 * @param moduleId
	 * @return
	 */
	List<SysPermissionVo> findByModuleIdWithNotMenu(Long moduleId);

	/**
	 * 根据权限code标识查询
	 * 
	 * @param permissCode
	 * @return
	 */
	SysPermission findByCode(String permissCode);

	/**
	 * 根据模块ID 删除该模块下所有权限(不可逆操作谨慎调用)
	 * 
	 * @param moduleId
	 */
	void deleteByModuleId(Long moduleId);

	/**
	 * 根据名称查询权限信息
	 * 
	 * @param name
	 * @return
	 */
	SysPermission findByName(String name);

	/**
	 * 根据父ID查询
	 * 
	 * @return
	 */
	List<SysPermission> findByParentId(Long parentId);

	/**
	 * 权限添加
	 * 
	 * @param permission
	 */
	void add(SysPermission permission);
	
	/**
	 * 添加权限关联信息
	 * @param moduleId
	 * @param permissionId
	 */
	public void addPermissionRelation(Long moduleId,Long permissionId);

	/**
	 * 权限修改
	 * 
	 * @param permission
	 */
	void edit(SysPermission permission);

	/**
	 * 删除权限
	 * 
	 * @param id
	 */
	void delete(Long id);

}
