package com.aek.ebey.sys.model;

import com.aek.common.core.base.BaseModel;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;

/**
 * <p>
 * 
 * </p>
 *
 * @author aek
 * @since 2017-05-12
 */
@TableName("sys_dict")
public class SysDict extends BaseModel {

	private static final long serialVersionUID = 1L;
	/**
	 * 父类型id
	 */
	@TableField(value = "parent_id")
	private Long parentId;
	/**
	 * 数据英文标识
	 */
	private String code;
	/**
	 * 文本
	 */
	private String text;
	/**
	 * 值
	 */
	private String value;
	/**
	 * 排序字段
	 */
	private Integer sort;
	/**
	 * 添加人id
	 */
	@TableField(value = "add_by")
	private Long addBy;
	/**
	 * 启用标识(0:禁用,1:启用)
	 */
	private Boolean enable;
	/**
	 * 删除标记(0:默认,1:删除)
	 */
	@TableField(value = "del_flag")
	private Boolean delFlag;
	/**
	 * 备注
	 */
	private String remark;
	/**
	 * 租户id
	 */
	@TableField(value = "tenant_id")
	private Long tenantId;
	/**
	 * 扩展字段
	 */
	@TableField(value = "custom_data")
	private String customData;

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

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

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public Long getAddBy() {
		return addBy;
	}

	public void setAddBy(Long addBy) {
		this.addBy = addBy;
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

	public Long getTenantId() {
		return tenantId;
	}

	public void setTenantId(Long tenantId) {
		this.tenantId = tenantId;
	}

	public String getCustomData() {
		return customData;
	}

	public void setCustomData(String customData) {
		this.customData = customData;
	}

}
