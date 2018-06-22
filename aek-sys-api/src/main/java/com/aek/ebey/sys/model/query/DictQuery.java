package com.aek.ebey.sys.model.query;

import com.aek.common.core.base.page.PageHelp;
import com.aek.ebey.sys.model.BaseDict;

/**
 * 字典分页检索实体
 * 
 * @author Honghui
 * @date 2017年6月14日
 * @version 1.0
 */
public class DictQuery extends PageHelp<BaseDict> {

	/**
	 * 机构ID
	 */
	private Long tenantId;

	/**
	 * 级联状态(1=级联，2=独立)
	 */
	private Integer cascadeStatus;

	/**
	 * 管理类型(1=基础，2=自定义)
	 */
	private Integer manageType;

	/**
	 * 关键字
	 */
	private String keyword;

	public Long getTenantId() {
		return tenantId;
	}

	public void setTenantId(Long tenantId) {
		this.tenantId = tenantId;
	}

	public Integer getCascadeStatus() {
		return cascadeStatus;
	}

	public void setCascadeStatus(Integer cascadeStatus) {
		this.cascadeStatus = cascadeStatus;
	}

	public Integer getManageType() {
		return manageType;
	}

	public void setManageType(Integer manageType) {
		this.manageType = manageType;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

}
