package com.aek.ebey.sys.enums;

/**
 * 模块类型
 * 
 * @author Mr.han
 *
 */
public enum ModuleType {

	/**
	 * 业务工具
	 */
	WORK_TOOL("业务应用", 1),

	/**
	 * 系统工具
	 */
	SYSTEM_TOOL("系统应用", 2),

	/**
	 * 数据分析工具
	 */
	DATA_ANALYSE_TOOL("数据分析应用", 3);

	private String title;

	private Integer number;

	private ModuleType(String title, Integer number) {
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
