package com.aek.ebey.sys.model.vo;

import com.aek.ebey.sys.model.SysTenantModule;

/**
 * 机构模块输出类
 * 
 * @author Mr.han
 *
 */
public class SysTenantModuleVo extends SysTenantModule {

	private static final long serialVersionUID = -1335475943481533935L;

	/**
	 * 是否为默认拥有模块
	 */
	private boolean isDefaultExist;

	public boolean isDefaultExist() {
		return isDefaultExist;
	}

	public void setDefaultExist(boolean isDefaultExist) {
		this.isDefaultExist = isDefaultExist;
	}
}
