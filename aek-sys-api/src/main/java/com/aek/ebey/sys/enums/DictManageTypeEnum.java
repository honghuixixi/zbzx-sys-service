package com.aek.ebey.sys.enums;

/**
 * 字典管理类型枚举
 *	
 * @author HongHui
 * @date   2017年7月14日
 */
public enum DictManageTypeEnum {

	BASE(1, "基础"),
	CUSTOM(2, "自定义");
	
	private Integer number;
	private String name;
	
	private DictManageTypeEnum(Integer number, String name) {
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
