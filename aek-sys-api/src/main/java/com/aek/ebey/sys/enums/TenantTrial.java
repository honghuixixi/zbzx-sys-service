package com.aek.ebey.sys.enums;

/**
 * 租户是否测试
 * 
 * @author Mr.han
 *
 */
public enum TenantTrial {
	/**
	 * 测试 是
	 */
	YES("是", 0),
	/**
	 * 测试 不是
	 */
	NO("否", 1);

	private String title;

	private Integer number;

	private TenantTrial(String title, Integer number) {
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
