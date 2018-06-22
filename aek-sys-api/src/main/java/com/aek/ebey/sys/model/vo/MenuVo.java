package com.aek.ebey.sys.model.vo;

import java.util.ArrayList;
import java.util.List;

/**
 * 权限菜单辅助查询类
 * 
 * @author Mr.han
 *
 */
public class MenuVo {

	/**
	 * 菜单ID
	 */
	private Long menuId;

	/**
	 * 菜单名称
	 */
	private String menuName;

	/**
	 * 是否拥有
	 */
	private Boolean delFlag;

	/**
	 * 是否选中
	 */
	private Boolean enable;

	/**
	 * 菜单中的权限
	 */
	private List<SysPermissionVo> permissions = new ArrayList<>();

	public String getMenuName() {
		return menuName;
	}

	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}

	public Long getMenuId() {
		return menuId;
	}

	public void setMenuId(Long menuId) {
		this.menuId = menuId;
	}

	public List<SysPermissionVo> getPermissions() {
		return permissions;
	}

	public void setPermissions(List<SysPermissionVo> permissions) {
		this.permissions = permissions;
	}

	public Boolean getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(Boolean delFlag) {
		this.delFlag = delFlag;
	}

	public Boolean getEnable() {
		return enable;
	}

	public void setEnable(Boolean enable) {
		this.enable = enable;
	}

}
