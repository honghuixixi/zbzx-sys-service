package com.aek.ebey.sys.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aek.common.core.base.BaseServiceImpl;
import com.aek.ebey.sys.mapper.SysSupplierTenantCredentialsMapper;
import com.aek.ebey.sys.model.SysSupplierTenantCredentials;
import com.aek.ebey.sys.service.SysSupplierTenantCredentialsService;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;

/**
 * 供应商证件服务类
 *	
 * @author HongHui
 * @date   2017年9月19日
 */
@Service
@Transactional
public class SysSupplierTenantCredentialsServiceImpl extends BaseServiceImpl<SysSupplierTenantCredentialsMapper, SysSupplierTenantCredentials> implements SysSupplierTenantCredentialsService {

	@Autowired
	private SysSupplierTenantCredentialsMapper sysSupplierTenantCredentialsMapper;
	
	@Override
	public List<SysSupplierTenantCredentials> findSupplierTenantCredentials(Long tenantId) {
		Wrapper<SysSupplierTenantCredentials> wrapper = new EntityWrapper<SysSupplierTenantCredentials>();
		wrapper.eq("tenant_id", tenantId);
		return this.selectList(wrapper);
	}

	@Override
	public void modifySupplierTenantCredentials(SysSupplierTenantCredentials sysSupplierTenantCredentials) {
		this.updateById(sysSupplierTenantCredentials);
		SysSupplierTenantCredentials tenantCredentials = sysSupplierTenantCredentialsMapper.selectById(sysSupplierTenantCredentials.getId());
		if(StringUtils.isBlank(sysSupplierTenantCredentials.getCode())){
			tenantCredentials.setCode(null);
		}
		if(StringUtils.isBlank(sysSupplierTenantCredentials.getImageUrl())){
			tenantCredentials.setImageUrl(null);
		}
		sysSupplierTenantCredentialsMapper.updateAllColumnById(tenantCredentials);
	}

	
}
