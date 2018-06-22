package com.aek.ebey.sys.model.vo;

import java.util.Date;

import com.aek.common.core.util.DateUtil;

/**
 * 机构统计实体类
 * 
 * @author Mr.han
 *
 */
public class SysTenantCensusVo {
	/**
	 * 统计时间(天)
	 */
	private Date date;

	/**
	 * 机构数量
	 */
	private int count;

	/**
	 * 时间字符串输出
	 */
	private String dateStr;

	/**
	 * @return the date
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * @param date
	 *            the date to set
	 */
	public void setDate(Date date) {
		this.date = date;
	}

	/**
	 * @return the count
	 */
	public int getCount() {
		return count;
	}

	/**
	 * @param count
	 *            the count to set
	 */
	public void setCount(int count) {
		this.count = count;
	}

	public String getDateStr() {
		this.setDateStr(DateUtil.format(this.getDate()));
		return this.dateStr;
	}

	public void setDateStr(String dateStr) {
		this.dateStr = dateStr;
	}

}
