package com.aek.ebey.sys.model;

import com.aek.common.core.base.BaseModel;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import java.util.Date;

/**
 * <p>
 * 部门表
 * </p>
 *
 * @author aek
 * @since 2017-05-06
 */
@TableName("sys_dept")
public class SysDept extends BaseModel {

	private static final long serialVersionUID = 1L;
	/**
	 * 机构ID
	 */
	@TableField(value = "tenant_id")
	private Long tenantId;
	/**
	 * 部门ID
	 */
	@TableField(exist=false)
	private Long deptId;
	/**
	 * 上级部门ID
	 */
	@TableField(value = "parent_id")
	private Long parentId;
	/**
	 * 所有上级部门ID
	 */
	@TableField(value = "parent_ids")
	private String parentIds;
	/**
	 * 部门名称
	 */
	private String name;
	/**
	 * 部门名称拼音简拼
	 */
	@TableField(value = "name_py")
	private String namePy;
	/**
	 * 部门内部排序
	 */
	private Integer sort;
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
	 * 是否启用 0 关闭 1启用
	 */
	private Boolean enable;
	/**
	 * 删除标志 1 删除 0 未删除
	 */
	@TableField(value = "del_flag")
	private Boolean delFlag;
	/**
	 * 是否预设 1 是 0 否
	 */
	@TableField(value = "preset")
	private Boolean preset;
	/**
	 * 
	 */
	@TableField(value = "custom_data")
	private String customData;

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public String getParentIds() {
		return parentIds;
	}

	public void setParentIds(String parentIds) {
		this.parentIds = parentIds;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNamePy() {
		return namePy;
	}

	public void setNamePy(String namePy) {
		this.namePy = namePy;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
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

	public String getCustomData() {
		return customData;
	}

	public void setCustomData(String customData) {
		this.customData = customData;
	}

	public Long getTenantId() {
		return tenantId;
	}

	public void setTenantId(Long tenantId) {
		this.tenantId = tenantId;
	}

	public Boolean getPreset() {
		return preset;
	}

	public void setPreset(Boolean preset) {
		this.preset = preset;
	}

	public Long getDeptId() {
		return deptId;
	}

	public void setDeptId(Long deptId) {
		this.deptId = deptId;
	}

}
