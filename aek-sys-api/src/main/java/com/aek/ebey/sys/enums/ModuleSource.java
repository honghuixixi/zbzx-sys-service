package com.aek.ebey.sys.enums;

/**
 * 模块来源
 * 
 * @author Mr.han
 *
 */
public enum ModuleSource {

	/**
	 * 独立添加
	 */
	INDEPENDENT_ADD("独立添加", 1);
	
	private String title;

	private Integer number;

	private ModuleSource(String title, Integer number) {
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
