package com.aek.ebey.sys.model.custom;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 监管机构字段信息
 * 
 * @author Mr.han
 *
 */
@ApiModel
public class SuperviseTenant implements Serializable {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value="监管机构名称")
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
