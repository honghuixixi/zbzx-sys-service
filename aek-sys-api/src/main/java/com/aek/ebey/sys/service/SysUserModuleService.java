package com.aek.ebey.sys.service;

import java.util.List;

import com.aek.common.core.base.BaseService;
import com.aek.ebey.sys.model.SysUserModule;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author aek
 * @since 2017-05-27
 */
public interface SysUserModuleService extends BaseService<SysUserModule> {

	void modifySysUserModuleAll(List<SysUserModule> sysUserModuleList);
	
}
