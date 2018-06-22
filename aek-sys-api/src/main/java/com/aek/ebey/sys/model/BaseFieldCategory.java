package com.aek.ebey.sys.model;

import java.util.Date;

import com.aek.common.core.base.BaseModel;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;

/**
 * 字段分类实体类
 *	
 * @author HongHui
 * @date   2017年7月13日
 */
@TableName("base_field_category")
public class BaseFieldCategory extends BaseModel {

	private static final long serialVersionUID = -6745798847505737016L;

	/**
	 * 父类ID
	 */
	@TableField(value = "parent_id")
	private Long parentId;
	
	/**
	 * 分类名称
	 */
	@TableField(value = "name")
	private String name;
	
	
	/**
	 * 分类类型（1=系统预设，2=自定义）
	 */
	@TableField(value = "type")
	private Integer type;
	
	/**
	 * 删除标记(0=未删除，1=已删除)
	 */
	@TableField(value = "del_flag")
	private Boolean delFlag;
	
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

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Boolean getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(Boolean delFlag) {
		this.delFlag = delFlag;
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

	@Override
	public String toString() {
		return "BaseFieldCategory [parentId=" + parentId + ", name=" + name + ", type=" + type + ", delFlag=" + delFlag
				+ ", createBy=" + createBy + ", createDate=" + createDate + ", updateBy=" + updateBy + ", updateDate="
				+ updateDate + "]";
	}
	
}
