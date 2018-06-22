package com.aek.ebey.sys.mapper;

import com.aek.ebey.sys.model.SysDict;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.aek.common.core.base.BaseMapper;

/**
 * <p>
 * Mapper接口
 * </p>
 *
 * @author aek
 * @since 2017-05-12
 */
public interface SysDictMapper extends BaseMapper<SysDict> {

	List<SysDict> findListByParentCode(@Param("tenantId")Long tenantId, @Param("code")String code);

}