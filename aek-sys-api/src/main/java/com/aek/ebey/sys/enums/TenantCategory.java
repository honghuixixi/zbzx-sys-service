package com.aek.ebey.sys.enums;

/**
 * 机构类型
 * 
 * @author Mr.han
 *
 */
public enum TenantCategory {

	/**
	 * 医疗机构
	 */
	HOSPITAL("医疗机构", 1),
	/**
	 * 基层医疗卫生机构
	 */
	BASE_HOSPITAL("基层医疗卫生机构", 2),
	/**
	 * 疾病预防控制中心
	 */
	CDC("疾病预防控制中心", 3);

	private String title;

	private Integer number;

	private TenantCategory(String title, Integer number) {
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
