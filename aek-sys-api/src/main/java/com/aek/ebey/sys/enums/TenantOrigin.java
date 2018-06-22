package com.aek.ebey.sys.enums;

/**
 * 机构来源
 * 
 * @author Mr.han
 *
 */
public enum TenantOrigin {

	/**
	 * 后台创建
	 */
	BACK_CREATE("后台创建", 1),

	/**
	 * 前台注册
	 */
	FRONT_REGISTER("前台注册", 2);

	private String title;

	private Integer number;

	private TenantOrigin(String title, Integer number) {
		this.title = title;
		this.number = number;
	}

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}
