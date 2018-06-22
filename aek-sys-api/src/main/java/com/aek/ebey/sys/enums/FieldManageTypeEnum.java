package com.aek.ebey.sys.enums;

/**
 * 字段管理类型枚举
 *	
 * @author HongHui
 * @date   2017年7月14日
 */
public enum FieldManageTypeEnum {

	BASE(1, "基础"),
	EXTEND(2, "扩展"),
	CUSTOM(3,"自定义");
	
	private Integer number;
	private String name;
	
	private FieldManageTypeEnum(Integer number, String name) {
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
