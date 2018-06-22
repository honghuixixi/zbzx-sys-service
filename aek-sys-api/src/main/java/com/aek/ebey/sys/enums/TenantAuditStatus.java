package com.aek.ebey.sys.enums;

/**
 * 机构审核状态
 * 
 * @author Mr.han
 *
 */
public enum TenantAuditStatus {
	/**
	 * 1-待审核
	 */
	PENDING("待审核", 1),
	/**
	 * 2-自动通过
	 */
	THROUGH("自动通过", 2),
	/**
	 * 3-人工通过
	 */
	MANUAL_THROUGH("人工通过", 3),
	/**
	 * 4-已拒绝
	 */
	NOTAPPROVED("已拒绝", 4);

	private String title;

	private Integer number;

	private TenantAuditStatus(String title, Integer number) {
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
