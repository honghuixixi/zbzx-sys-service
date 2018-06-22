package com.aek.ebey.sys.web.request;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.aek.ebey.sys.web.validator.group.Add;
import com.google.common.collect.Lists;

/**
 * 用户保存租户角色保存辅助类
 * 
 * @author Mr.han
 *
 */
public class TenantRoleRequest {
	/**
	 * 租户ID
	 */
	@NotNull(groups = { Add.class }, message = "G_002")
	private Long tenantId;

	/**
	 * 用户ID
	 */
	@NotNull(groups = { Add.class }, message = "U_009")
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
	 * @return the roleIds
	 */
	public List<Long> getRoleIds() {
		return roleIds;
	}

	/**
	 * @param roleIds
	 *            the roleIds to set
	 */
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
