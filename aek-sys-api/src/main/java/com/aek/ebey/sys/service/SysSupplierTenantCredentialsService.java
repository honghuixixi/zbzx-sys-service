package com.aek.ebey.sys.service;

import java.util.List;

import com.aek.common.core.base.BaseService;
import com.aek.ebey.sys.model.SysSupplierTenantCredentials;

/**
 * 供应商证件服务类
 *	
 * @author HongHui
 * @date   2017年9月19日
 */
public interface SysSupplierTenantCredentialsService extends BaseService<SysSupplierTenantCredentials>{

	/**
	 * 获取供应商证件信息
	 * @param tenantId
	 * @return
	 */
	public List<SysSupplierTenantCredentials> findSupplierTenantCredentials(Long tenantId);
	
	/**
	 * 更新供应商证件信息
	 * @param sysSupplierTenantCredentials
	 */
	public void modifySupplierTenantCredentials(SysSupplierTenantCredentials sysSupplierTenantCredentials);
	
}
