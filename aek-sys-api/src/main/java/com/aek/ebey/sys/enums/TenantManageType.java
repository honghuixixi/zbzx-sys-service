package com.aek.ebey.sys.enums;

/**
 * 机构管理类型
 * 
 * @author Mr.han
 *
 */
public enum TenantManageType {

	/**
	 * 盈利性
	 */
	BUSINESS("营利性", 1),

	/**
	 * 非盈利性
	 */
	NOT_BUSINESS("非营利性", 2),

	/**
	 * 其他
	 */
	OTHER("其他", 3);

	private String title;

	private Integer number;

	private TenantManageType(String title, Integer number) {
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
