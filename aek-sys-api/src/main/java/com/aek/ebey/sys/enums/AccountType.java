package com.aek.ebey.sys.enums;

/**
 * 账户类型
 *	
 * @author HongHui
 * @date   2017年8月8日
 */
public enum AccountType {

	MOBILE(1,"手机"),
	EMAIL(2,"邮箱");
	
	private Integer number;
	private String desc;
	
	private AccountType(Integer number, String desc) {
		this.number = number;
		this.desc = desc;
	}

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
	
}
