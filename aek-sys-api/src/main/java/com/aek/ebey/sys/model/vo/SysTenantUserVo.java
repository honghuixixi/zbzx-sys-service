package com.aek.ebey.sys.model.vo;

import com.aek.ebey.sys.model.SysTenant;

/**
 * 租户信息查询辅助类
 * 
 * @author Mr.han
 *
 */
public class SysTenantUserVo {
	/**
	 * 租户
	 */
	private SysTenant tenant;
	/**
	 * 管理员
	 */
	private SysTenantAdminVo tenantAdmin = new SysTenantAdminVo();

	/**
	 * 创建人姓名
	 */
	private String createName;
	

	public SysTenant getTenant() {
		return tenant;
	}

	public void setTenant(SysTenant tenant) {
		this.tenant = tenant;
	}

	public SysTenantAdminVo getTenantAdmin() {
		return tenantAdmin;
	}

	public void setTenantAdmin(SysTenantAdminVo tenantAdmin) {
		this.tenantAdmin = tenantAdmin;
	}

	public String getCreateName() {
		return createName;
	}

	public void setCreateName(String createName) {
		this.createName = createName;
	}

}
