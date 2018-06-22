package com.aek.ebey.sys.enums;

/**
 * 机构等次
 * 
 * @author Mr.han
 *
 */
public enum TenantHierarchy {

	/**
	 * 特等
	 */
	AA("特等", 1),

	/**
	 * 甲等
	 */
	A("甲等", 2),

	/**
	 * 乙等
	 */
	B("乙等", 3),

	/**
	 * 丙等
	 */
	C("丙等", 4),

	/**
	 * 未定义
	 */
	UNDEFINITION("未评", 6);

	private String title;

	private Integer number;

	private TenantHierarchy(String title, Integer number) {
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
