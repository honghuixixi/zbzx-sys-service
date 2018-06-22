package com.aek.ebey.sys.web.request;

import javax.validation.constraints.NotNull;

import com.aek.ebey.sys.web.validator.group.Add;
import com.aek.ebey.sys.web.validator.group.Edit;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class BaseRequest {

	/**
	 * 创建人ID
	 */
	@ApiModelProperty(value = "创建人ID")
	@NotNull(groups = Add.class, message = "C_001")
	protected Long createBy;

	/**
	 * 修改人ID
	 */
	@ApiModelProperty(value = "修改人ID")
	@NotNull(groups = Edit.class, message = "C_002")
	protected Long updateBy;

	public Long getCreateBy() {
		return createBy;
	}

	public void setCreateBy(Long createBy) {
		this.createBy = createBy;
	}

	public Long getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(Long updateBy) {
		this.updateBy = updateBy;
	}

}
