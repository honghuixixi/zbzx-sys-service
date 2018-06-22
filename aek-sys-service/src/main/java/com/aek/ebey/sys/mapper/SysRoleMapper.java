package com.aek.ebey.sys.mapper;

import com.aek.ebey.sys.model.SysRole;
import com.aek.ebey.sys.model.vo.SysRoleVo;
import com.aek.ebey.sys.model.vo.SysTenantRoleVo;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.aek.common.core.base.BaseMapper;

/**
 * <p>
 * Mapper接口
 * </p>
 *
 * @author aek
 * @since 2017-05-06
 */
public interface SysRoleMapper extends BaseMapper<SysRole> {

	List<SysRoleVo> selectByUserIdAndTenantId(Map<String,Object> map);
	
	List<SysTenantRoleVo> selectAllRoleByUserIdAndTenantId(@Param("userId") Long userId, @Param("tenantId") Long tenantId,@Param("parentIds") String parentIds);
}
