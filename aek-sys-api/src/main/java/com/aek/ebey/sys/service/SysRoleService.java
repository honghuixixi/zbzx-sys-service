package com.aek.ebey.sys.service;

import java.util.List;
import java.util.Map;

import com.aek.common.core.base.BaseService;
import com.aek.ebey.sys.model.SysRole;
import com.aek.ebey.sys.model.query.RoleQuery;
import com.aek.ebey.sys.model.vo.ModuleMenuVo;
import com.aek.ebey.sys.model.vo.SysRoleVo;
import com.aek.ebey.sys.model.vo.SysTenantRoleVo;
import com.baomidou.mybatisplus.plugins.Page;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author aek
 * @since 2017-05-06
 */
public interface SysRoleService extends BaseService<SysRole> {

	/**
	 * 初始化机构角色
	 * 
	 * @param tenantId
	 *            机构ID
	 */
	void initTenantRole(Long tenantId);

	/**
	 * 根据租户ID查询该租户的管理员角色
	 * 
	 * @param tenantId
	 *            机构ID
	 * @return
	 */
	SysRole findAdminRoleByTenantId(Long tenantId);

	/**
	 * 给用户添加一个角色
	 * 
	 * @param roleId
	 *            角色ID
	 * @param tenantId
	 *            机构ID
	 * @param userId
	 *            用户ID
	 */
	void addRoleToUser(Long roleId, Long tenantId, Long userId);

	/**
	 * 机构创建自定义角色
	 * 
	 * @param roleVo
	 */
	void addRole(SysRoleVo roleVo);

	/**
	 * 修改自定义角色
	 * 
	 * @param roleVo
	 */
	void modifyRole(SysRoleVo roleVo);

	/**
	 * 查询角色详细信息
	 * 
	 * @param roleId
	 *            角色ID
	 * @return
	 */
	SysRoleVo findRoleDetai(Long roleId);

	/**
	 * 创见角色获取角色权限的基础信息
	 * 
	 * @param tenantId
	 *            机构ID
	 * @return
	 */
	List<ModuleMenuVo> baseRoleInfo(Long tenantId);

	/**
	 * 删除角色信息(逻辑删除，角色被用户使用不能删除)
	 * 
	 * @param roleId
	 */
	void deleteRoleById(Long roleId);

	/**
	 * 停用角色(逻辑停用，角色被使用提醒,isForced=true 强制停用操作)
	 * 
	 * @param roleId
	 *            角色ID
	 * @param isForced
	 *            是否强制停用
	 */
	void stopRole(Long roleId, Boolean isForced);

	/**
	 * 角色恢复
	 * 
	 * @param roleId
	 *            角色ID
	 */
	void recoverRoleByRoleId(Long roleId);

	/**
	 * 根据机构ID查询角色列表
	 * 
	 * @param tenantId
	 *            机构ID
	 * @return
	 */
	List<SysRole> findRoleListByTenantId(Long tenantId);

	/**
	 * 分页查询机构角色信息
	 * 
	 * @param query
	 * @return
	 */
	Page<SysRole> findByPage(RoleQuery query);

	/**
	 * 查询机构下的角色
	 * 
	 * @param tenantId
	 * @return
	 */
	List<SysRole> findByTenantId(Long tenantId);
	
	/**
	 * 查询用户在当前机构下拥有的角色
	 * @param userId
	 * @param tenantId
	 * @return
	 */
	List<SysRoleVo> findByUserIdAndTenantId(Map<String,Object> map);
	
	/**
	 * 查询用户在当前机构及其子机构下拥有的角色
	 * @param userId
	 * @param tenantId
	 * @param parentIds
	 * @return
	 */
	List<SysTenantRoleVo> findAllRoleByUserIdAndTenantId(Long userId,Long tenantId,String parentIds);

}
