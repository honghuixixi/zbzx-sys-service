package com.aek.ebey.sys.model;

import com.aek.common.core.base.BaseModel;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import java.util.Date;

/**
 * <p>
 * 角色表
 * </p>
 *
 * @author aek
 * @since 2017-05-06
 */
@TableName("sys_role")
public class SysRole extends BaseModel {

	private static final long serialVersionUID = 1L;

	/**
	 * 租户id
	 */
	@TableField(value = "tenant_id")
	private Long tenantId;
	/**
	 * 角色名称
	 */
	private String name;
	/**
	 * 角色标识
	 */
	private String code;
	/**
	 * 角色数据范围: 1=所在机构所有数据,2=所在部门及下级部门数据,3=所在部门数据，4=自定义部门数据
	 */
	@TableField(value = "data_scope")
	private Integer dataScope;

	/**
	 * 预设角色ID
	 */
	@TableField(value = "preset_id")
	private Long presetId;

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
	 * 更新时间
	 */
	@TableField(value = "update_time")
	private Date updateTime;

	/**
	 * 删除标志 1 删除 0 未删除
	 */
	@TableField(value = "del_flag")
	private Boolean delFlag;

	/**
	 * 是否启用 0 关闭 1启用
	 */
	private Boolean enable;

	@TableField(value = "custom_data")
	private String customData;

	/*-------------------------查询返回字段---------------------------------*/
	/**
	 * 使用当前角色的用户数量
	 */
	@TableField(exist = false)
	private int userCount;

	public Long getTenantId() {
		return tenantId;
	}

	public void setTenantId(Long tenantId) {
		this.tenantId = tenantId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
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

	public String getCustomData() {
		return customData;
	}

	public void setCustomData(String customData) {
		this.customData = customData;
	}

	public Long getPresetId() {
		return presetId;
	}

	public void setPresetId(Long presetId) {
		this.presetId = presetId;
	}

	public Boolean getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(Boolean delFlag) {
		this.delFlag = delFlag;
	}

	public Boolean getEnable() {
		return enable;
	}

	public void setEnable(Boolean enable) {
		this.enable = enable;
	}

	public int getUserCount() {
		return userCount;
	}

	public void setUserCount(int userCount) {
		this.userCount = userCount;
	}

	public String getDescript() {
		return descript;
	}

	public void setDescript(String descript) {
		this.descript = descript;
	}

}
