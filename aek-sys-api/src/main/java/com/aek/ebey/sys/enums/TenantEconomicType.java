package com.aek.ebey.sys.enums;

/**
 * 机构经济类型
 * 
 * @author Mr.han
 *
 */
public enum TenantEconomicType {
	/**
	 * 公立
	 */
	PUBLIC("公立", 1),
	/**
	 * 民营
	 */
	PRIVATELY("民营", 2);

	private String title;

	private Integer number;

	private TenantEconomicType(String title, Integer number) {
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
