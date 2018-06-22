package com.aek.ebey.sys.model.vo;

import java.util.ArrayList;
import java.util.List;

/**
 * 角色权限查询辅助类
 * 
 * @author Administrator
 *
 */
public class ModuleMenuVo {

	/**
	 * 模块名称
	 */
	private String moduleName;

	/**
	 * 是否拥有该模块
	 */
	private Boolean delFlag;
	/**
	 * 是否启用
	 */
	private Boolean enable;
	/**
	 * 模块中的菜单
	 */
	private List<MenuVo> menus = new ArrayList<>();

	/**
	 * 不在菜单中的权限
	 */
	private List<SysPermissionVo> permissions = new ArrayList<>();

	public String getModuleName() {
		return moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	public List<MenuVo> getMenus() {
		return menus;
	}

	public void setMenus(List<MenuVo> menus) {
		this.menus = menus;
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
