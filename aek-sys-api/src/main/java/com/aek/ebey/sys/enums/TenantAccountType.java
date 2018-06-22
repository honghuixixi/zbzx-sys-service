package com.aek.ebey.sys.enums;

/**
 * 医疗机构账户类型
 * 
 * @author Mr.han
 *
 */
public enum TenantAccountType {

	/** 试用账户 */
	TEST("试用", 0),

	/** 正式账户 */
	FORMAL("正式", 1);

	private String title;

	private Integer number;

	private TenantAccountType(String title, Integer number) {
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
