package com.aek.ebey.sys.model;

import java.util.Date;

import com.aek.common.core.base.BaseModel;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;

/**
 * 字段表实体类
 *	
 * @author HongHui
 * @date   2017年7月13日
 */
@TableName("base_field")
public class BaseField extends BaseModel{

	private static final long serialVersionUID = -3169852421046850935L;

	/**
	 * 字段分类ID
	 */
	@TableField(value = "field_category_id")
	private Long fieldCategoryId;
	
	/**
	 * 字段中文名称
	 */
	@TableField(value = "chinese_name")
	private String chineseName;

	/**
	 * 字段英文名称
	 */
	@TableField(value = "english_name")
	private String englishName;
	
	/**
	 * 字段类型(1=短文本，2=长文本，3=下拉框，4=单选，5=多选，6=数字，7=日期，8=时间，9=图片，10=文件，11=金额)
	 */
	@TableField(value = "field_type")
	private Integer fieldType;
	
	/**
	 * 验证规则
	 */
	@TableField(value = "validate_rule")
	private String validateRule;
	
	/**
	 * 错误提示
	 */
	@TableField(value = "error_message")
	private String errorMessage;
	
	/**
	 * 管理类型(1=基础，2=扩展，3=自定义)
	 */
	@TableField(value = "manage_type")
	private Integer manageType;
	
	/**
	 * 是否必填（0=非必填，1=必填）
	 */
	@TableField(value = "required_flag")
	private Boolean requiredFlag;
	
	/**
	 * 启用状态（0=禁用，1=启用）
	 */
	@TableField(value = "enable")
	private Boolean enable;
	
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

	public Long getFieldCategoryId() {
		return fieldCategoryId;
	}

	public void setFieldCategoryId(Long fieldCategoryId) {
		this.fieldCategoryId = fieldCategoryId;
	}

	public String getChineseName() {
		return chineseName;
	}

	public void setChineseName(String chineseName) {
		this.chineseName = chineseName;
	}

	public String getEnglishName() {
		return englishName;
	}

	public void setEnglishName(String englishName) {
		this.englishName = englishName;
	}

	public Integer getFieldType() {
		return fieldType;
	}

	public void setFieldType(Integer fieldType) {
		this.fieldType = fieldType;
	}

	public String getValidateRule() {
		return validateRule;
	}

	public void setValidateRule(String validateRule) {
		this.validateRule = validateRule;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public Integer getManageType() {
		return manageType;
	}

	public void setManageType(Integer manageType) {
		this.manageType = manageType;
	}

	public Boolean getRequiredFlag() {
		return requiredFlag;
	}

	public void setRequiredFlag(Boolean requiredFlag) {
		this.requiredFlag = requiredFlag;
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

	public Boolean getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(Boolean delFlag) {
		this.delFlag = delFlag;
	}

	@Override
	public String toString() {
		return "BaseField [fieldCategoryId=" + fieldCategoryId + ", chineseName=" + chineseName + ", englishName="
				+ englishName + ", fieldType=" + fieldType + ", validateRule=" + validateRule + ", errorMessage="
				+ errorMessage + ", manageType=" + manageType + ", requiredFlag=" + requiredFlag + ", enable=" + enable
				+ ", delFlag=" + delFlag + ", createBy=" + createBy + ", createDate=" + createDate + ", updateBy="
				+ updateBy + ", updateDate=" + updateDate + "]";
	}
	
}
