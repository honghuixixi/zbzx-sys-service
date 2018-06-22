package com.aek.ebey.sys.model.vo;

import java.util.List;

import com.google.common.collect.Lists;

/**
 * 用户在租户角色查询辅助类
 * 
 * @author Mr.han
 *
 */
public class SysTenantRoleVo {
	/**
	 * 租户ID
	 */
	private Long tenantId;
	/**
	 * 租户名称
	 */
	private String tenantName;
	/*--------------------------查询-----------------------------*/
	/**
	 * 角色集合
	 */
	private List<SysRoleVo> roles = Lists.newArrayList();

	/*--------------------------保存----------------------------*/

	/**
	 * 用户ID
	 */
	private Long userId;

	/**
	 * 已选中的角色ID
	 */
	private List<Long> roleIds = Lists.newArrayList();

	/**
	 * @return the tenantId
	 */
	public Long getTenantId() {
		return tenantId;
	}

	/**
	 * @param tenantId
	 *            the tenantId to set
	 */
	public void setTenantId(Long tenantId) {
		this.tenantId = tenantId;
	}

	/**
	 * @return the tenantName
	 */
	public String getTenantName() {
		return tenantName;
	}

	/**
	 * @param tenantName
	 *            the tenantName to set
	 */
	public void setTenantName(String tenantName) {
		this.tenantName = tenantName;
	}

	public List<SysRoleVo> getRoles() {
		return roles;
	}

	public void setRoles(List<SysRoleVo> roles) {
		this.roles = roles;
	}

	public List<Long> getRoleIds() {
		return roleIds;
	}

	public void setRoleIds(List<Long> roleIds) {
		this.roleIds = roleIds;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

}
