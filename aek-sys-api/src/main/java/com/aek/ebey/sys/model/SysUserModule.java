package com.aek.ebey.sys.model;

import com.aek.common.core.base.BaseModel;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;

/**
 * <p>
 * 用户模块信息
 * </p>
 *
 * @author aek
 * @since 2017-05-27
 */
@TableName("sys_user_module")
public class SysUserModule extends BaseModel {

	private static final long serialVersionUID = 1L;

	/**
	 * 用户ID
	 */
	@TableField(value = "user_id")
	private Long userId;
	/**
	 * 模块ID
	 */
	@TableField(value = "module_id")
	private Long moduleId;
	/**
	 * 模块分组（1：置顶；2；3等）
	 */
	@TableField(value = "module_group")
	private Integer moduleGroup;
	/**
	 * 模块在分组内的顺序
	 */
	@TableField(value = "module_order")
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
