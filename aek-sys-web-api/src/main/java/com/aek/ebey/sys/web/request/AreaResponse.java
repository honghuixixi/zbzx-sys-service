package com.aek.ebey.sys.web.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by rookie on 2017/3/29 0029.
 */
@ApiModel(value = "AreaResponse", description = "区域返回值")
public class AreaResponse {

	@ApiModelProperty(value = "区域ID")
	private Long id;

	/**
	 * 区域编码
	 */
	@ApiModelProperty(value = "区域编码")
	private String code;

	/**
	 * 区域名称
	 */
	@ApiModelProperty(value = "区域名称")
	protected String name;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
