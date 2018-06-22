package com.aek.ebey.sys.enums;

/**
 * 字典依赖类型枚举
 *	
 * @author HongHui
 * @date   2017年7月14日
 */
public enum DictDependTypeEnum {

	SYS_DEPEND(1, "系统依赖"),
	CUSTOM_DEPEND(2, "自定义字段依赖"),
	NO_DEPEND(3,"无依赖");
	
	private Integer number;
	private String name;
	
	private DictDependTypeEnum(Integer number, String name) {
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
