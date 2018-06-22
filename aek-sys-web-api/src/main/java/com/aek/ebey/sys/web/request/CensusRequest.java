package com.aek.ebey.sys.web.request;

import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "统计信息请求实体类")
public class CensusRequest {
	@ApiModelProperty(notes = "机构ID")
	private Long tenantId;

	@ApiModelProperty(notes = "自定义时间段时,不需要传入该参数;0:上月;1:本月;2:本季度;3:本年", allowableValues = ",0,1,2,3")
	private Integer dateLabel;

	@ApiModelProperty(notes = "开始时间")
	private Date startDate;

	@ApiModelProperty(notes = "截止时间")
	private Date endDate;

	/**
	 * @return the dateLabel
	 */
	public Integer getDateLabel() {
		return dateLabel;
	}

	/**
	 * @param dateLabel
	 *            the dateLabel to set
	 */
	public void setDateLabel(Integer dateLabel) {
		this.dateLabel = dateLabel;
	}

	/**
	 * @return the startDate
	 */
	public Date getStartDate() {
		return startDate;
	}

	/**
	 * @param startDate
	 *            the startDate to set
	 */
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	/**
	 * @return the endDate
	 */
	public Date getEndDate() {
		return endDate;
	}

	/**
	 * @param endDate
	 *            the endDate to set
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Long getTenantId() {
		return tenantId;
	}

	public void setTenantId(Long tenantId) {
		this.tenantId = tenantId;
	}

}
