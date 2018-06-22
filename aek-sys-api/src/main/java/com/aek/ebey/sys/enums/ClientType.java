package com.aek.ebey.sys.enums;

/**
 * 用户登录客户端类型
 * 
 * @author Mr.han
 *
 */
public enum ClientType {
	/**
	 * PC后台登录
	 */
	PC("PC后台", 1);

	private String title;

	private Integer number;

	private ClientType(String title, Integer number) {
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
