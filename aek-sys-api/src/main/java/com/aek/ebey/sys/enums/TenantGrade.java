package com.aek.ebey.sys.enums;

/**
 * 机构等级
 * 
 * @author Mr.han
 *
 */
public enum TenantGrade {

	/**
	 * 一级
	 */
	LEVEL_1("一级", 1),

	/**
	 * 二级
	 */
	LEVEL_2("二级", 2),

	/**
	 * 三级
	 */
	LEVEL_3("三级", 3),
	
	/**
	 * 未定级
	 */
	UNDEFINITION("未定级", 4);

	private String title;

	private Integer number;

	private TenantGrade(String title, Integer number) {
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
