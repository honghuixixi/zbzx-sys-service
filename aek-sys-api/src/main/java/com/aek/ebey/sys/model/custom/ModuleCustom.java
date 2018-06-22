package com.aek.ebey.sys.model.custom;

import java.io.Serializable;
import java.util.List;

import com.google.common.collect.Lists;

/**
 * 模块Custom信息
 * 
 * @author Mr.han
 *
 */
public class ModuleCustom implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 租户类型 当前模块可以被那些租户类型使用
	 */
	private List<Integer> tenantType = Lists.newArrayList();

	/**
	 * 是否所有机构默认拥有该模块
	 */
	private Boolean defaultExist;

	public List<Integer> getTenantType() {
		return tenantType;
	}

	public void setTenantType(List<Integer> tenantType) {
		this.tenantType = tenantType;
	}

	public Boolean getDefaultExist() {
		return defaultExist;
	}

	public void setDefaultExist(Boolean defaultExist) {
		this.defaultExist = defaultExist;
	}

}
