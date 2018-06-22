package com.aek.ebey.sys.web.request;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import com.aek.ebey.sys.web.validator.group.Add;
import com.aek.ebey.sys.web.validator.group.Edit;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class DeptRequest extends BaseRequest {
	/**
	 * 部门ID
	 */
	@ApiModelProperty("ID")
	@NotNull(groups = { Edit.class }, message = "G_001")
	private Long id;

	/**
	 * 机构ID
	 */
	@ApiModelProperty("所在机构ID")
	@NotNull(groups = { Add.class, Edit.class }, message = "G_002")
	private Long tenantId;

	/**
	 * 父级部门id
	 */
	@ApiModelProperty("父级部门id")
	@NotNull(groups = { Add.class }, message = "D_001")
	private Long parentId;

	/**
	 * 名称
	 */
	@ApiModelProperty("名称")
	@NotEmpty(groups = { Add.class }, message = "D_003")
	private String name;

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the tenantId
	 */
	public Long getTenantId() {
		return tenantId;
	}

	/**
	 * @param tenantId
	 *            the tenantId to set
	 */
	public void setTenantId(Long tenantId) {
		this.tenantId = tenantId;
	}

	/**
	 * @return the parentId
	 */
	public Long getParentId() {
		return parentId;
	}

	/**
	 * @param parentId
	 *            the parentId to set
	 */
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

}