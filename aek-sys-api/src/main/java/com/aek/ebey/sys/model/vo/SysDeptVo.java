package com.aek.ebey.sys.model.vo;

import java.util.List;

import com.aek.ebey.sys.model.SysDept;
import com.google.common.collect.Lists;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 部门数据范围封装类
 * 
 * @author Mr.han
 *
 */
@ApiModel
public class SysDeptVo extends SysDept {

	private static final long serialVersionUID = 1452695795343244337L;

	@ApiModelProperty(value = "子部门列表")
	private List<SysDeptVo> subDepts = Lists.newArrayList();

	public List<SysDeptVo> getSubDepts() {
		return subDepts;
	}

	public void setSubDepts(List<SysDeptVo> subDepts) {
		this.subDepts = subDepts;
	}

}
