package com.aek.ebey.sys.web.request;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.aek.ebey.sys.web.validator.group.Add;
import com.aek.ebey.sys.web.validator.group.Edit;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 字段分类请求实体类
 *	
 * @author HongHui
 * @date   2017年7月14日
 */
@ApiModel(value = "FieldCategoryRequest", description = "字段分类请求实体类")
public class FieldCategoryRequest{

	@ApiModelProperty(value = "ID")
	@NotNull(groups = { Edit.class }, message = "G_001")
	private Long id;
	
	@ApiModelProperty(value = "父类ID")
	private Long parentId;
	
	@ApiModelProperty(value = "字段分类名称")
	@Length(groups = { Edit.class, Add.class }, max = 40, message = "G_007")
	@NotNull(groups = { Edit.class,Add.class }, message = "BFC_001")
	private String name;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

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
}
