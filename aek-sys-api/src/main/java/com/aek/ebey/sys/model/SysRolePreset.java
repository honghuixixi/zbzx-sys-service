package com.aek.ebey.sys.model;

import com.aek.common.core.base.BaseModel;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import java.util.Date;

/**
 * <p>
 * 预设角色表
 * </p>
 *
 * @author aek
 * @since 2017-05-06
 */
@TableName("sys_role_preset")
public class SysRolePreset extends BaseModel {

	private static final long serialVersionUID = 1L;

	/**
	 * 角色名称
	 */
	private String name;
	/**
	 * 角色数据范围: 1=所在机构所有数据,2=所在部门及下级部门数据,3=所在部门数据,4=自己的数据
	 */
	@TableField(value = "data_scope")
	private Integer dataScope;
	/**
	 * 预设角色描述信息
	 */
	private String descript;
	/**
	 * 创建者
	 */
	@TableField(value = "create_by")
	private Long createBy;
	/**
	 * 创建时间
	 */
	@TableField(value = "create_time")
	private Date createTime;
	/**
	 * 更新者
	 */
	@TableField(value = "update_by")
	private Long updateBy;
	/**
	 * 
	 */
	@TableField(value = "update_time")
	private Date updateTime;
	/**
	 * 启用标记 1 启用 0 停用
	 */
	private Boolean enable;
	/**
	 * 删除标记 1 已删除,0 未删除
	 */
	@TableField(value = "del_flag")
	private Boolean delFlag;
	/**
	 * 备注
	 */
	private String remark;

	/**
	 * 当前预设角色所赋予的机构类型 以逗号间隔"1,3,4"
	 */
	private String tenantType;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getDataScope() {
		return dataScope;
	}

	public void setDataScope(Integer dataScope) {
		this.dataScope = dataScope;
	}

	public Long getCreateBy() {
		return createBy;
	}

	public void setCreateBy(Long createBy) {
		this.createBy = createBy;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Long getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(Long updateBy) {
		this.updateBy = updateBy;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public Boolean isEnable() {
		return enable;
	}

	public void setEnable(Boolean enable) {
		this.enable = enable;
	}

	public Boolean isDelFlag() {
		return delFlag;
	}

	public void setDelFlag(Boolean delFlag) {
		this.delFlag = delFlag;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getDescript() {
		return descript;
	}

	public void setDescript(String descript) {
		this.descript = descript;
	}

	public String getTenantType() {
		return tenantType;
	}

	public void setTenantType(String tenantType) {
		this.tenantType = tenantType;
	}

}
