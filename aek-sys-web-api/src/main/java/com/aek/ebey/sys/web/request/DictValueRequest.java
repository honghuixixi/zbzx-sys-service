package com.aek.ebey.sys.web.request;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.aek.ebey.sys.web.validator.group.Add;
import com.aek.ebey.sys.web.validator.group.Edit;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 数据字典响应数据实体类
 *
 * @author Honghui
 * @date 2017年6月14日
 * @version 1.0
 */
@ApiModel(value = "DictValueRequest", description = "字典值创建")
public class DictValueRequest{

	@ApiModelProperty(value = "字典值ID")
	@NotNull(groups = { Edit.class }, message = "G_001")
	private Long id;

	@ApiModelProperty(value = "字典ID")
	@NotNull(groups = { Add.class }, message = "BDV_001")
	private Long dictId;

	@ApiModelProperty(value = "字典值名称")
	@Length(groups = { Edit.class, Add.class }, max = 40, message = "G_007")
	private String name;

	@ApiModelProperty(value = "是否默认")
	private Boolean defFlag;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getDictId() {
		return dictId;
	}

	public void setDictId(Long dictId) {
		this.dictId = dictId;
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

}
