package com.aek.ebey.sys.model.query;

import com.aek.ebey.sys.model.SysDept;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 部门检索类
 */
@ApiModel
public class DeptQuery extends SysDept {

	private static final long serialVersionUID = -4161000055000237953L;
	/**
	 * 父部门id
	 */
	@ApiModelProperty("父部门id")
	private Long parentId;
	
	/**
	 * 名称
	 */
	private String name;
	
	/**
	 * 检索关键字
	 */
	@ApiModelProperty(value = "检索关键字")
	private String keyword;

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
