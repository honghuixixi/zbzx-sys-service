package com.aek.ebey.sys.service;

import com.aek.ebey.sys.model.SysModule;
import com.aek.ebey.sys.model.SysTenantModule;
import com.aek.ebey.sys.model.vo.SysModuleVo;

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
public interface SysTenantModuleService extends BaseService<SysTenantModule> {

	/**
	 * 初始化机构模块信息 (复制所有模块至机构模块表，状态为未启用，删除，在添加模块时再激活)
	 * 
	 * @param tenantId
	 *            租户ID
	 */
	void initTenantModule(Long tenantId);

	/**
	 * 根据租户ID查询已拥有的模块 （只获取模块ID）
	 * 
	 * @param tenantId
	 *            租户ID
	 * @return 租户已拥有模块ID集合
	 */
	List<Long> findTenantModuleIdByTenantId(Long tenantId);

	/**
	 * 根据租户ID查询租户所有模块信息（包括可用,不可用）
	 * 
	 * @param tenantId
	 *            租户ID
	 * @return
	 */
	List<SysTenantModule> findAllByTenantId(Long tenantId);

	/**
	 * 查询角色在那些模块有权限
	 * 
	 * @param roleId
	 *            角色ID
	 * @return
	 */
	List<SysTenantModule> findModuleByRoleId(Long roleId);

	/**
	 * 移除租户的指定模块
	 * 
	 * @param tenantId
	 *            租户ID
	 * @param moduleId
	 *            模块ID
	 */
	void deleteTenantModule(Long tenantId, Long moduleId);

	/**
	 * 查询租户模块信息
	 * 
	 * @param tenantId
	 *            租户ID
	 * @param moduleId
	 *            模块ID
	 * @return
	 */
	SysTenantModule findByTenantIdAndModuleId(Long tenantId, Long moduleId);

	/**
	 * 添加租户模块
	 * 
	 * @param tenantId
	 *            租户ID
	 * @param moduleIds
	 *            模块ID集合
	 */
	void addTenantModule(Long tenantId, List<Long> moduleIds);

	/**
	 * 获取当前租户可以添加的模块列表(根据上级机构拥有模块确定,子机构可拥有模块列表)
	 * 
	 * @param tenantId
	 * @return
	 */
	List<SysModule> findTenantCanAddModule(Long tenantId);

	/**
	 * 查询租户模块列表 (机构ID和模块类型分类)
	 * 
	 * @param tenantId
	 *            机构ID
	 * @param moduleType
	 *            模块类型
	 * @return
	 */
	List<SysTenantModule> findByTenantIdAndModuleType(Long tenantId, Long moduleType);

	/**
	 * 查询用户可见的模块及菜单权限
	 * 
	 * @param userId
	 * @param tenantId
	 * @return
	 */
	@Deprecated
	List<SysModuleVo> findUserSeeModuleWithPermission(Long userId, Long tenantId);

	/**
	 * 根据租户ID查询已拥有的模块
	 * 
	 * @param tenantId
	 *            租户ID
	 * @return
	 */
	List<SysTenantModule> findByTenantId(Long tenantId);

	/**
	 * 根据模块ID查询
	 * 
	 * @param moduleId
	 * @return
	 */
	List<SysTenantModule> findByModuleId(Long moduleId);

	/**
	 * 系统模块修改级联修改机构模块关系表
	 * 
	 * @param module
	 */
	void updateByModule(SysModule module);

	/**
	 * 根据模块ID删除机构模块关系
	 * 
	 * @param moduleId
	 */
	void deleteByModuleId(Long moduleId);

}
