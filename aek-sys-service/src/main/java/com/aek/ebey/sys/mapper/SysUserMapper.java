package com.aek.ebey.sys.mapper;

import org.apache.ibatis.annotations.Param;

import com.aek.common.core.base.BaseMapper;
import com.aek.ebey.sys.model.SysUser;

/**
 * <p>
 * Mapper接口
 * </p>
 *
 * @author aek
 * @since 2017-05-06
 */
public interface SysUserMapper extends BaseMapper<SysUser> {

	/**
	 * 统计用户是否具有指定权限
	 * @param userId
	 * @param tenandId
	 * @param permissionCode
	 * @return
	 */
	int getUserPermissionCount(@Param("userId") Long userId,@Param("tenandId") Long tenandId,@Param("permissionCode") String permissionCode);

	/**
	 * 查找未删除且可用的用户
	 * @param userId
	 * @return
	 */
	SysUser getUserEnableAndNotDel(@Param("userId") Long userId);
}
