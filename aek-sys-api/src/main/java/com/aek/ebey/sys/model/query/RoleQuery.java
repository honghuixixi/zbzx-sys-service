package com.aek.ebey.sys.model.query;

import com.aek.common.core.base.page.PageHelp;
import com.aek.ebey.sys.model.SysRole;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 角色数据查询封装类
 * 
 * @author Mr.han
 *
 */
@ApiModel
public class RoleQuery extends PageHelp<SysRole> {

	/**
	 * 租户ID
	 */
	@ApiModelProperty(value = "租户ID")
	private Long tenantId;

	/**
	 * 角色ID
	 */
	@ApiModelProperty(value = "角色ID")
	private Long roleId;

	/**
	 * 角色状态
	 */
	@ApiModelProperty(value = "角色状态[1=启用,0=未启用]", allowableValues = "0,1")
	private Boolean enable;

	/**
	 * 角色类型
	 */
	@ApiModelProperty(value = "角色类型[1=预设角色,2=自定义角色]", allowableValues = "1,2")
	private Integer roleType;

	/**
	 * 检索关键字
	 */
	@ApiModelProperty(value = "检索关键字")
	private String keyword;

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public Boolean getEnable() {
		return enable;
	}

	public void setEnable(Boolean enable) {
		this.enable = enable;
	}

	public Integer getRoleType() {
		return roleType;
	}

	public void setRoleType(Integer roleType) {
		this.roleType = roleType;
	}

	public Long getTenantId() {
		return tenantId;
	}

	public void setTenantId(Long tenantId) {
		this.tenantId = tenantId;
	}

}