package com.aek.ebey.sys.model.vo;

import com.aek.ebey.sys.model.SysPermission;

/**
 * 权限查询辅助类
 * 
 * @author Mr.han
 *
 */
public class SysPermissionVo extends SysPermission {

	private static final long serialVersionUID = 1L;

	/**
	 * 是否能使用该权限
	 */
	private Boolean delFlag;

	public Boolean getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(Boolean delFlag) {
		this.delFlag = delFlag;
	}
}
