package com.aek.ebey.sys.web.request;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import com.aek.ebey.sys.web.validator.group.Add;
import com.aek.ebey.sys.web.validator.group.Edit;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class PermissionRequest extends BaseRequest {

	@ApiModelProperty(value = "权限ID")
	@NotNull(groups = { Edit.class }, message = "G_001")
	private Long id;

	@ApiModelProperty(value = "菜单ID [类型为菜单或者不在菜单下的权限值为0]")
	@NotNull(groups = { Add.class }, message = "P_001")
	private Long parentId;

	@ApiModelProperty(value = "所属模块ID")
	@NotNull(groups = { Add.class }, message = "P_002")
	private Long moduleId;

	@ApiModelProperty(value = "权限名称")
	@NotEmpty(groups = { Add.class }, message = "P_003")
	private String name;

	@ApiModelProperty(value = "权限标识[类型为菜单值为空]")
	private String code;

	@ApiModelProperty(value = "是否为菜单标识[true 菜单/ false 权限]")
	@NotNull(groups = { Add.class }, message = "P_004")
	private Boolean menuFlag;

	@ApiModelProperty(value = "描述信息")
	@NotNull(groups = { Add.class }, message = "P_005")
	private String description;

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
	 * @return the moduleId
	 */
	public Long getModuleId() {
		return moduleId;
	}

	/**
	 * @param moduleId
	 *            the moduleId to set
	 */
	public void setModuleId(Long moduleId) {
		this.moduleId = moduleId;
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

	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @param code
	 *            the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * @return the menuFlag
	 */
	public Boolean getMenuFlag() {
		return menuFlag;
	}

	/**
	 * @param menuFlag
	 *            the menuFlag to set
	 */
	public void setMenuFlag(Boolean menuFlag) {
		this.menuFlag = menuFlag;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

}
