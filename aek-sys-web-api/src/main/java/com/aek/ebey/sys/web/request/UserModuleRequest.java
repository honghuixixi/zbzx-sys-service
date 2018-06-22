package com.aek.ebey.sys.web.request;

import javax.validation.constraints.NotNull;

import com.aek.ebey.sys.web.validator.group.Add;

import io.swagger.annotations.ApiModelProperty;

/**
 * 用户保存应用设置辅助类
 * @author liuhui
 *
 */
public class UserModuleRequest {
	
	/**
	 * 用户ID
	 */
	@NotNull(groups = { Add.class }, message = "U_001")
	private Long userId;
	
	
	/**
	 * 模块ID
	 */
	@NotNull(groups = { Add.class }, message = "U_002")
	private Long moduleId;
	/**
	 * 模块分组（1：置顶；2；3等）
	 */
	@ApiModelProperty(value = "模块分组[1=置顶,2=爱医康应用，3=第三方应用]", allowableValues = "1,2,3")
	@NotNull(groups = { Add.class }, message = "U_003")
	private Integer moduleGroup;
	/**
	 * 模块在分组内的顺序
	 */
	@ApiModelProperty(value = "模块在分组内的顺序[根据数值大小排序值越大在后]")
	@NotNull(groups = { Add.class }, message = "U_004")
	private Integer moduleOrder;
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public Long getModuleId() {
		return moduleId;
	}
	public void setModuleId(Long moduleId) {
		this.moduleId = moduleId;
	}
	public Integer getModuleGroup() {
		return moduleGroup;
	}
	public void setModuleGroup(Integer moduleGroup) {
		this.moduleGroup = moduleGroup;
	}
	public Integer getModuleOrder() {
		return moduleOrder;
	}
	public void setModuleOrder(Integer moduleOrder) {
		this.moduleOrder = moduleOrder;
	}

	
}
