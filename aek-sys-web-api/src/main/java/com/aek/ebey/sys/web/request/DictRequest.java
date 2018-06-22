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
@ApiModel(value = "DictRequest", description = "数据字典创建")
public class DictRequest{

	@ApiModelProperty(value = "字典id")
	@NotNull(groups = { Edit.class }, message = "G_001")
	private Long id;
	
	@ApiModelProperty(value = "租户id")
	@NotNull(groups = { Add.class }, message = "G_002")
	private Long tenantId;

	@ApiModelProperty(value = "字典表名称")
	@Length(groups = { Edit.class, Add.class }, max = 40, message = "G_007")
	@NotNull(groups = { Edit.class, Add.class }, message = "BD_003")
	private String name;

	@ApiModelProperty(value = "级联状态[1=级联，2=独立]")
	@NotNull(groups = { Add.class }, message = "BD_001")
	private Integer cascadeStatus;

	@ApiModelProperty(value = "备注信息")
	private String remarks;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getCascadeStatus() {
		return cascadeStatus;
	}

	public void setCascadeStatus(Integer cascadeStatus) {
		this.cascadeStatus = cascadeStatus;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Long getTenantId() {
		return tenantId;
	}

	public void setTenantId(Long tenantId) {
		this.tenantId = tenantId;
	}

}
