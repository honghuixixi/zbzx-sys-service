package com.aek.ebey.sys.service.impl;

import com.aek.ebey.sys.model.SysDict;
import com.aek.ebey.sys.mapper.SysDictMapper;
import com.aek.ebey.sys.service.SysDictService;
import com.aek.common.core.base.BaseServiceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 数据字典表 服务实现类
 * </p>
 *
 * @author aek
 * @since 2017-05-12
 */
@Service
@Transactional
public class SysDictServiceImpl extends BaseServiceImpl<SysDictMapper, SysDict> implements SysDictService {

	@Autowired
	private SysDictMapper sysDictMapper;

	@Transactional(readOnly = true)
	public List<SysDict> findListByParentCode(Long tenantId, String code) {
		return this.sysDictMapper.findListByParentCode(tenantId, code);
	}
}
