package com.aek.ebey.sys.core.scheduled;

import java.util.TimerTask;

import com.aek.ebey.sys.model.SysTenant;
import com.aek.ebey.sys.service.SysTenantService;

/**
 *	机构过期时间已到，将机构停用
 *
 * @author HongHui
 * @date   2017年7月31日
 */
public class SysExpireTenantTimerTask extends TimerTask {

	private SysTenantService sysTenantService;
	private Long id;           //机构id

	public SysExpireTenantTimerTask(Long id,SysTenantService sysTenantService) {
		this.id = id;
		this.sysTenantService = sysTenantService;
	}

	@Override
	public void run() {
		SysTenant entity = new SysTenant();
		entity.setId(id);
		entity.setEnable(false);
		sysTenantService.updateById(entity);
	}

	public SysTenantService getSysTenantService() {
		return sysTenantService;
	}

	public void setSysTenantService(SysTenantService sysTenantService) {
		this.sysTenantService = sysTenantService;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
}
