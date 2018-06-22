package com.aek.ebey.sys.model;

import java.util.Date;

import com.aek.common.core.base.BaseModel;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;

/**
 * 字典表
 * 
 * @author Honghui
 * @date 2017年6月14日
 * @version 1.0
 */
@TableName("base_dict")
public class BaseDict extends BaseModel {

	private static final long serialVersionUID = -1292982895750706092L;

	/**
	 * 父字典id
	 */
	@TableField(value = "parent_id")
	private Integer parentId;

	/**
	 * 租户ID
	 */
	@TableField(value = "tenant_id")
	private Long tenantId;

	/**
	 * 字典表名称
	 */
	@TableField(value = "name")
	private String name;

	/**
	 * 默认值
	 */
	@TableField(value = "value")
	private String value;

	/**
	 * 级联状态(1=级联，2=独立)
	 */
	@TableField(value = "cascade_status")
	private Integer cascadeStatus;

	/**
	 * 管理类型(1=基础，2=自定义)
	 */
	@TableField(value = "manage_type")
	private Integer manageType;

	/**
	 * 依赖类型(1=系统依赖，2=自定义字段依赖，3=无依赖)
	 */
	@TableField(value = "depend_type")
	private Integer dependType;

	/**
	 * 删除标记(0=未删除，1=删除)
	 */
	@TableField(value = "del_flag")
	private Boolean delFlag;

	/**
	 * 启用标记(0=禁用，1=启用)
	 */
	@TableField(value = "enable")
	private Boolean enable;

	/**
	 * 创建人id
	 */
	@TableField(value = "create_by")
	private Long createBy;

	/**
	 * 创建时间
	 */
	@TableField(value = "create_date")
	private Date createDate;

	/**
	 * 更新人id
	 */
	@TableField(value = "update_by")
	private Long updateBy;

	/**
	 * 更新时间
	 */
	@TableField(value = "update_date")
	private Date updateDate;

	/**
	 * 备注信息
	 */
	@TableField(value = "remarks")
	private String remarks;

	/*---------------------------查询冗余字段-------------------------*/
	/**
	 * 字典值数量
	 */
	@TableField(exist = false)
	private Integer dictValueCount;

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

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

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Integer getCascadeStatus() {
		return cascadeStatus;
	}

	public void setCascadeStatus(Integer cascadeStatus) {
		this.cascadeStatus = cascadeStatus;
	}

	public Integer getManageType() {
		return manageType;
	}

	public void setManageType(Integer manageType) {
		this.manageType = manageType;
	}

	public Integer getDependType() {
		return dependType;
	}

	public void setDependType(Integer dependType) {
		this.dependType = dependType;
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

	public Long getCreateBy() {
		return createBy;
	}

	public void setCreateBy(Long createBy) {
		this.createBy = createBy;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Long getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(Long updateBy) {
		this.updateBy = updateBy;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Integer getDictValueCount() {
		return dictValueCount;
	}

	public void setDictValueCount(Integer dictValueCount) {
		this.dictValueCount = dictValueCount;
	}

	@Override
	public String toString() {
		return "BaseDict [parentId=" + parentId + ", tenantId=" + tenantId + ", name=" + name + ", value=" + value
				+ ", cascadeStatus=" + cascadeStatus + ", manageType=" + manageType + ", dependType=" + dependType
				+ ", delFlag=" + delFlag + ", enable=" + enable + ", createBy=" + createBy + ", createDate="
				+ createDate + ", updateBy=" + updateBy + ", updateDate=" + updateDate + ", remarks=" + remarks
				+ ", dictValueCount=" + dictValueCount + "]";
	}
	
}
