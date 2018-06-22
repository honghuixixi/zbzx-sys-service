package com.aek.ebey.sys.model;

import com.aek.common.core.base.BaseModel;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;

/**
 * <p>
 * 用户角色关联表
 * </p>
 *
 * @author aek
 * @since 2017-05-06
 */
@TableName("sys_role_user")
public class SysRoleUser extends BaseModel {

	private static final long serialVersionUID = 1L;

	/**
	 * 角色ID
	 */
	@TableField(value = "role_id")
	private Long roleId;
	/**
	 * 用户ID
	 */
	@TableField(value = "user_id")
	private Long userId;
	/**
	 * 租户ID
	 */
	@TableField(value = "tenant_id")
	private Long tenantId;

	/**
	 * 是否启用 0 关闭 1启用
	 */
	private Boolean enable;

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getTenantId() {
		return tenantId;
	}

	public void setTenantId(Long tenantId) {
		this.tenantId = tenantId;
	}

	public Boolean isEnable() {
		return enable;
	}

	public void setEnable(Boolean enable) {
		this.enable = enable;
	}

}
