package com.aek.ebey.sys.enums;

public enum TenantOperation {
	/**
	 * 1-通过
	 */
	PASS("通过",1),
	/**
	 * 2-拒绝
	 */
	REFUSE("拒绝",2),
	/**
	 * 3-查看
	 */
	CHECK("查看",3);
	
	private String title;
	
	private Integer number;
	
	private TenantOperation(String title, Integer number) {
		this.title = title;
		this.number = number;
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
