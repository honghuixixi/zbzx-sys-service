package com.aek.ebey.sys.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class CmsTenantQuery {

	@ApiModelProperty(value="上级行政机构id")
	private Long manageId;
	@ApiModelProperty(value="搜索关键字")
	private String keyword;
	@ApiModelProperty(value="医疗机构类型（CMS），取值如下：1：医疗机构2：医基层疗卫生机构部3：疾病预防控制中心")
	private Integer type;
	
	public Long getManageId() {
		return manageId;
	}
	public void setManageId(Long manageId) {
		this.manageId = manageId;
	}
	
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	
	
}
