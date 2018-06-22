package com.aek.ebey.sys.model.vo;

import java.io.Serializable;

/**
 * 统计机构资产辅助类
 * @author cyl
 *
 */
public class TenantAssetsVo implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Long tenantId;
	private String assetsTotal;
	
	public Long getTenantId() {
		return tenantId;
	}
	public void setTenantId(Long tenantId) {
		this.tenantId = tenantId;
	}
	public String getAssetsTotal() {
		return assetsTotal;
	}
	public void setAssetsTotal(String assetsTotal) {
		this.assetsTotal = assetsTotal;
	}
	
}
