package com.aek.ebey.sys.web.request;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.aek.ebey.sys.web.validator.group.Add;
import com.aek.ebey.sys.web.validator.group.Edit;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 字段请求实体类
 *	
 * @author HongHui
 * @date   2017年7月14日
 */
@ApiModel(value = "FieldRequest", description = "字段表请求实体类")
public class FieldRequest {
	
	@ApiModelProperty(value = "字段ID")
	@NotNull(groups = { Edit.class }, message = "G_001")
	private Long id;
	
	@ApiModelProperty(value = "字段分类ID")
	@NotNull(groups = { Add.class }, message = "BFC_002")
	private Long fieldCategoryId;
	
	@ApiModelProperty(value = "字段中文名称")
	@Length(groups = { Edit.class, Add.class }, max = 40, message = "G_007")
	@NotNull(groups = { Edit.class,Add.class }, message = "BF_001")
	private String chineseName;

	@ApiModelProperty(value = "字段英文名称")
	@Length(groups = { Add.class }, max = 40, message = "G_007")
	@NotNull(groups = { Add.class }, message = "BF_002")
	private String englishName;
	
	@ApiModelProperty(value = "字段类型")
	@NotNull(groups = { Add.class }, message = "BF_003")
	private Integer fieldType;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public Long getFieldCategoryId() {
		return fieldCategoryId;
	}

	public void setFieldCategoryId(Long fieldCategoryId) {
		this.fieldCategoryId = fieldCategoryId;
	}
}
