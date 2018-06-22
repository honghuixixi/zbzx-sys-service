package com.aek.ebey.sys.model;

import java.util.Date;

import com.aek.common.core.base.BaseModel;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;

/**
 * <p>
 * 预设角色权限关系表
 * </p>
 *
 * @author aek
 * @since 2017-05-06
 */
@TableName("sys_role_permission_preset")
public class SysRolePermissionPreset extends BaseModel {

	private static final long serialVersionUID = 1L;

	/**
	 * 预设角色ID
	 */
	@TableField(value = "preset_role_id")
	private Long presetRoleId;

	/**
	 * 权限ID
	 */
	@TableField(value = "permission_id")
	private Long permissionId;

	/**
	 * 模块ID
	 */
	@TableField(value = "module_id")
	private Long moduleId;

	/**
	 * 是否启用 0 关闭 1启用
	 */
	private Boolean enable;
	/**
	 * 删除标志 1 删除 0 未删除
	 */
	@TableField(value = "del_flag")
	private Boolean delFlag;
	/**
	 * 创建时间
	 */
	@TableField(value = "create_time")
	private Date createTime;
	/**
	 * 更新时间
	 */
	@TableField(value = "update_time")
	private Date updateTime;

	public Long getPresetRoleId() {
		return presetRoleId;
	}

	public void setPresetRoleId(Long presetRoleId) {
		this.presetRoleId = presetRoleId;
	}

	public Long getPermissionId() {
		return permissionId;
	}

	public void setPermissionId(Long permissionId) {
		this.permissionId = permissionId;
	}

	public Long getModuleId() {
		return moduleId;
	}

	public void setModuleId(Long moduleId) {
		this.moduleId = moduleId;
	}

	public Boolean getEnable() {
		return enable;
	}

	public void setEnable(Boolean enable) {
		this.enable = enable;
	}

	public Boolean getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(Boolean delFlag) {
		this.delFlag = delFlag;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
