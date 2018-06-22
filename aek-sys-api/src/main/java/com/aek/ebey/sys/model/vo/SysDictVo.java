package com.aek.ebey.sys.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class SysDictVo {

	/**
	 * 文本
	 */
	@ApiModelProperty("文本")
	private String text;
	/**
	 * 值
	 */
	@ApiModelProperty("数值")

	private String value;
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
}
