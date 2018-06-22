package com.aek.ebey.sys.enums;

/**
 * 字段类型枚举
 *	
 * @author HongHui
 * @date   2017年7月14日
 */
public enum FieldTypeEnum {

	TINYTEXT(1, "短文本"),
	LONGTEXT(2, "长文本"),
	SELECT(3,"下拉框"),
	RADIO(4,"单选"),
	CHECKBOX(5,"多选"),
	NUMBER(6,"数字"),
	DATE(7,"日期"),
	TIME(8,"时间"),
	IMAGE(9,"图片"),
	FILE(10,"文件"),
	MONEY(11,"金额");
	
	private Integer number;
	private String name;
	
	private FieldTypeEnum(Integer number, String name) {
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
