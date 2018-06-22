package com.aek.ebey.sys.model;

import java.util.Date;

import com.aek.common.core.base.BaseModel;
import com.baomidou.mybatisplus.annotations.TableField;

/**
 * 字典值表
 * 
 * @author Honghui
 * @date 2017年6月14日
 * @version 1.0
 */
public class BaseDictValue extends BaseModel {

	private static final long serialVersionUID = -5643943339136068141L;

	/**
	 * 字典id
	 */
	@TableField(value = "dict_id")
	private Long dictId;

	/**
	 * 级联字典ID
	 */
	@TableField(value = "cascade_dict_id")
	private Long cascadeDictId;

	/**
	 * 字典值
	 */
	@TableField(value = "name")
	private String name;

	/**
	 * 是否默认(1=是 /0=否)
	 */
	@TableField(value = "def_flag")
	private Boolean defFlag;

	/**
	 * 排序字段
	 */
	@TableField(value = "sort")
	private Integer sort;

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

	public Long getDictId() {
		return dictId;
	}

	public void setDictId(Long dictId) {
		this.dictId = dictId;
	}

	public Long getCascadeDictId() {
		return cascadeDictId;
	}

	public void setCascadeDictId(Long cascadeDictId) {
		this.cascadeDictId = cascadeDictId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Boolean getDefFlag() {
		return defFlag;
	}

	public void setDefFlag(Boolean defFlag) {
		this.defFlag = defFlag;
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
		return "BaseDictValue [dictId=" + dictId + ", cascadeDictId=" + cascadeDictId + ", name=" + name + ", defFlag="
				+ defFlag + ", sort=" + sort + ", createBy=" + createBy + ", createDate=" + createDate + ", updateBy="
				+ updateBy + ", updateDate=" + updateDate + "]";
	}
	
}
