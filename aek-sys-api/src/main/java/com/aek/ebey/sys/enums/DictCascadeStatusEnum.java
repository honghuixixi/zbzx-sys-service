package com.aek.ebey.sys.enums;

/**
 * 字典级联状态枚举类
 *	
 * @author HongHui
 * @date   2017年10月11日
 */
public enum DictCascadeStatusEnum {

	CASCADE(1, "级联"),
	ALONE(2,"独立");
	
	private Integer number;
	private String name;
	
	private DictCascadeStatusEnum(Integer number, String name) {
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
