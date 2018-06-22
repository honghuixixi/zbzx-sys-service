package com.aek.ebey.sys.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.aek.common.core.base.BaseMapper;
import com.aek.ebey.sys.model.SysDept;
import com.aek.ebey.sys.model.query.DeptQuery;

/**
 * <p>
 * Mapper接口
 * </p>
 *
 * @author aek
 * @since 2017-05-06
 */
public interface SysDeptMapper extends BaseMapper<SysDept> {

	/**
	 * 根据节点ID查询
	 * 
	 * @param parentId
	 * @return
	 */
	List<SysDept> selectByParentId(@Param("parentId") Long parentId);

	/**
	 * 查询组织树列表
	 * 
	 * @param sysOffice
	 * @returnx
	 */
	List<SysDept> selectOfficeList(@Param("query") DeptQuery query);

	/**
	 * 查询角色自定部门数据范围
	 * 
	 * @return
	 */
	List<SysDept> selectRoleDept(@Param("roleId") Long roleId, @Param("orgId") Long orgId);
}