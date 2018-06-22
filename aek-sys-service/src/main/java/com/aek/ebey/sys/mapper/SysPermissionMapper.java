package com.aek.ebey.sys.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.aek.common.core.base.BaseMapper;
import com.aek.ebey.sys.model.SysPermission;
import com.aek.ebey.sys.model.vo.SysPermissionVo;

/**
 * <p>
 * Mapper接口
 * </p>
 *
 * @author aek
 * @since 2017-05-06
 */
public interface SysPermissionMapper extends BaseMapper<SysPermission> {
	/**
	 * 根据角色ID和模块ID，查询菜单
	 * 
	 * @param roleId
	 * @param moduleId
	 * @return
	 */
	List<SysPermissionVo> selectMenuByRoleIdAndModuleId(@Param("roleId") Long roleId, @Param("moduleId") Long moduleId);

	/**
	 * 查询菜单中的权限俄
	 * 
	 * @param roleId
	 *            角色ID
	 * @param menuId
	 *            菜单ID
	 * @return
	 */
	List<SysPermissionVo> selectBuMenuIdAndRoleId(@Param("roleId") Long roleId, @Param("menuId") Long menuId);

	/**
	 * 查询不在菜单中的权限
	 * 
	 * @param roleId
	 * @param moduleId
	 * @return
	 */
	List<SysPermissionVo> selectByModuleIdAndRoleIdWithNotInMenu(@Param("roleId") Long roleId,
			@Param("moduleId") Long moduleId);

	/**
	 * 根据用户id查询权限集合 关联角色表查询，去重复
	 * 
	 * @param userID
	 * @return
	 */
	List<SysPermission> selectServicesByUserId(@Param("userId") Long userId);

}
