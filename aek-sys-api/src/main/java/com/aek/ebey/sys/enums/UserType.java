package com.aek.ebey.sys.enums;

/**
 * 用户类型
 * 
 * @author Mr.han
 *
 */
public enum UserType {
	/**
	 * 爱医康超级管理员
	 */
	AEK_SUPER_ADMIN("爱医康超级管理员"),
	/**
	 * 爱医康管理员
	 */
	AEK_ADMIN("爱医康管理员"),
	/**
	 * 医疗机构管理员
	 */
	ORG_ADMIN("机构管理员"),
	/**
	 * 医疗机构一般用户
	 */
	ORG_COMMON("一般用户");

	private String title;

	private UserType(String title) {
		this.title = title;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}
