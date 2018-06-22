package com.aek.ebey.sys.model.vo;

import java.util.List;

import com.aek.ebey.sys.model.SysModule;
import com.aek.ebey.sys.model.SysPermission;
import com.google.common.collect.Lists;

public class SysModuleVo extends SysModule {

	private static final long serialVersionUID = 934277632086705337L;

	/**
	 * 当前模块可见的菜单与权限
	 */
	private List<SysPermission> permissions = Lists.newArrayList();

	public List<SysPermission> getPermissions() {
		return permissions;
	}

	public void setPermissions(List<SysPermission> permissions) {
		this.permissions = permissions;
	}

}
