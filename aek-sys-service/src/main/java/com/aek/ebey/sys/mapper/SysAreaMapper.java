package com.aek.ebey.sys.mapper;

import com.aek.ebey.sys.model.SysArea;

import java.util.List;

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
public interface SysAreaMapper extends BaseMapper<SysArea> {

	List<SysArea> queryByIdAndLevel(@Param("areaId") Long areaId, @Param("level") Integer level);

	List<SysArea> queryByParentIdAndLevel(@Param("parentId") Long parentId, @Param("level") Integer level);

	List<SysArea> queryByLevel(@Param("level") Integer level);

}