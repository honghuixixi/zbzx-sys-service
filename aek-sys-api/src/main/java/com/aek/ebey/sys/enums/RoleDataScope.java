package com.aek.ebey.sys.enums;

/**
 * 角色数据范围
 * 
 * @author Mr.han
 *
 */
public enum RoleDataScope {

	/**
	 * 所在机构所有数据
	 */
	ORG_ALL("所在机构所有数据", 1),

	/**
	 * 所在部门及下级部门数据
	 */
	DEPT_ALL("所在部门及下级部门数据", 2),

	/**
	 * 所在部门数据
	 */
	DEPT("所在部门数据", 3),
	
	/**
	 * 自定义部门数据
	 */
	DEPT_DEFINED("自定义部门数据", 4);


	private String title;

	private Integer number;

	private RoleDataScope(String title, Integer number) {
		this.title = title;
		this.setNumber(number);
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

}
