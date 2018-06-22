package com.aek.ebey.sys.service;

import com.aek.ebey.sys.model.SysDict;

import java.util.List;

import com.aek.common.core.base.BaseService;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author aek
 * @since 2017-05-12
 */
public interface SysDictService extends BaseService<SysDict> {

	List<SysDict> findListByParentCode(Long tenantId, String code);

}
