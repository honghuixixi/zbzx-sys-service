package com.aek.ebey.sys.web.request;

import java.util.List;

import com.google.common.collect.Lists;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 角色创建 角色在那些模块有权限信息
 * 
 * @author Mr.han
 *
 */
@ApiModel
public class RoleModuleRequest {

	/**
	 * 菜单ID
	 */
	@ApiModelProperty(value = "菜单ID")
	private Long menuId;

	/**
	 * 权限ID集合
	 */
	@ApiModelProperty(value = "权限ID集合")
	private List<Long> permissionIds = Lists.newArrayList();

	public Long getMenuId() {
		return menuId;
	}

	public void setMenuId(Long menuId) {
		this.menuId = menuId;
	}

	public List<Long> getPermissionIds() {
		return permissionIds;
	}

	public void setPermissionIds(List<Long> permissionIds) {
		this.permissionIds = permissionIds;
	}

}
