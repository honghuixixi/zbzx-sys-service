package com.aek.ebey.sys.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.aek.common.core.base.BaseMapper;
import com.aek.ebey.sys.model.SysTenantModule;

/**
 * <p>
 * Mapper接口
 * </p>
 *
 * @author aek
 * @since 2017-05-06
 */
public interface SysTenantModuleMapper extends BaseMapper<SysTenantModule> {
	/**
	 * 批量插入机构模块
	 * 
	 * @param orgModules
	 *            机构模块列表
	 */
	void insertBatch(@Param("list") List<SysTenantModule> tenantModules);

	/**
	 * 根据角色ID查询角色在那些模块有权限
	 * 
	 * @param roleId
	 *            角色ID
	 * @return
	 */
	List<SysTenantModule> selectModuleByRoleId(@Param("roleId") Long roleId);

}