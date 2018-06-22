package com.aek.ebey.sys.model.vo;

import java.io.Serializable;
import java.util.List;


import com.aek.common.core.base.session.SessionUser;
import com.aek.ebey.sys.model.SysTenant;
import com.google.common.collect.Lists;

/**
 * 缓存用户（用户前后端交互）
 * 
 * @author Mr.han
 *
 */
public class CacheUserVo implements Serializable {

	private static final long serialVersionUID = -6743845850204305831L;

	/**
	 * 当前用户信息
	 */
	private SessionUser sessionUser;

	/**
	 * 可切换机构
	 */
	private List<SysTenant> subTenants = Lists.newArrayList();

	/**
	 * 可见模块列表
	 */
	private List<SysModuleVo> modules = Lists.newArrayList();

	/**
	 * @return the sessionUser
	 */
	public SessionUser getSessionUser() {
		return sessionUser;
	}

	/**
	 * @param sessionUser
	 *            the sessionUser to set
	 */
	public void setSessionUser(SessionUser sessionUser) {
		this.sessionUser = sessionUser;
	}

	/**
	 * @return the subTenants
	 */
	public List<SysTenant> getSubTenants() {
		return subTenants;
	}

	/**
	 * @param subTenants
	 *            the subTenants to set
	 */
	public void setSubTenants(List<SysTenant> subTenants) {
		this.subTenants = subTenants;
	}

	public List<SysModuleVo> getModules() {
		return modules;
	}

	public void setModules(List<SysModuleVo> modules) {
		this.modules = modules;
	}

}
