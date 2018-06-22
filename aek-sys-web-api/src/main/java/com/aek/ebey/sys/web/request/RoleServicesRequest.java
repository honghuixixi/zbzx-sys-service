package com.aek.ebey.sys.web.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotEmpty;

import com.aek.ebey.sys.web.validator.group.Edit;

import javax.validation.constraints.NotNull;

/**
 * Created by rookie on 2017/3/27 0027.
 */
@ApiModel
public class RoleServicesRequest {

	@ApiModelProperty("服务ID")
	@NotNull(groups = { Edit.class }, message = "G_001")
	private Long id;

	/**
	 * api名称
	 */
	@ApiModelProperty("服务名称")
	@NotEmpty(message = "S_001")
	private String name;

	/**
	 * 接口对应地址
	 */
	@ApiModelProperty("服务地址")
	private String url;
	/**
	 * 接口对应的机构ID
	 */
	@ApiModelProperty("所属机构")
	private Long orgId;

	/**
	 * 权限标识
	 */
	@ApiModelProperty("权限标识")
	private String permission;

	/**
	 * 服务的父类ID
	 */
	@ApiModelProperty("父级服务ID")
	private Long parentId;

	/**
	 * 服务类型 1 菜单 2 api接口
	 */
	@ApiModelProperty(value = "服务类型(1=菜单,2=api接口)", allowableValues = "1,2")
	private Integer type;

	/**
	 * 服务所属平台 1监管 2物资 3质控中心 4 会员中心
	 */
	@ApiModelProperty(value = "服务所属平台(1=监管 2=物资 3=质控中心 4=会员中心)", allowableValues = "1,2,3,4")
	private Integer platform;
	
	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getPlatform() {
		return platform;
	}

	public void setPlatform(Integer platform) {
		this.platform = platform;
	}

	public String getPermission() {
		return permission;
	}

	public void setPermission(String permission) {
		this.permission = permission;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}
