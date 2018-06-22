package com.aek.ebey.sys.enums;

/**
 * 字段分类类型枚举
 *	
 * @author HongHui
 * @date   2017年7月14日
 */
public enum FieldCategoryTypeEnum {

	SYSTEM_PRESET(1, "系统预设"),
	CUSTOM_TYPE(2,"自定义");
	
	private Integer number;
	private String name;
	
	private FieldCategoryTypeEnum(Integer number, String name) {
		this.number = number;
		this.name = name;
	}
	
	public Integer getNumber() {
		return number;
	}
	public void setNumber(Integer number) {
		this.number = number;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
}
