package com.aek.ebey.sys.service;

import java.util.List;

import com.aek.common.core.base.BaseService;
import com.aek.ebey.sys.model.SysRolePermissionPreset;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author aek
 * @since 2017-05-06
 */
public interface SysRolePermissionPresetService extends BaseService<SysRolePermissionPreset> {

	/**
	 * 根据模板ID查询模板角色权限表
	 * 
	 * @param id
	 * @return
	 */
	List<SysRolePermissionPreset> findByPresetRoleId(Long presetRoleId);

	/**
	 * 根据moduleId删除该模块下的权限预设角色关系
	 * 
	 * @param moduleId
	 */
	void deleteByModule(Long moduleId);

	/**
	 * 根据权限ID删除
	 * 
	 * @param permissionId
	 */
	void deleteByPermissionId(Long permissionId);

}
