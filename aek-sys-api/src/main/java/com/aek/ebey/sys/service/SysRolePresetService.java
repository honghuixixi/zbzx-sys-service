package com.aek.ebey.sys.service;

import java.util.List;

import com.aek.common.core.base.BaseService;
import com.aek.ebey.sys.model.SysRolePreset;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author aek
 * @since 2017-05-06
 */
public interface SysRolePresetService extends BaseService<SysRolePreset> {
	/**
	 * 查询所有可用模板角色
	 * 
	 * @return
	 */
	List<SysRolePreset> findAllTempletRole();

	/**
	 * 根据机构类型查询当前机构的预设角色
	 * 
	 * @param tenantId
	 * @return
	 */
	List<SysRolePreset> findRolePresetByTenantType(Long tenantId);

	/**
	 * 查询所有预设角色
	 * 
	 * @return
	 */
	List<SysRolePreset> findAll();

}
