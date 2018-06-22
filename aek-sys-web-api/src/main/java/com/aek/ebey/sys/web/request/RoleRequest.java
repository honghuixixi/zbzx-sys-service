package com.aek.ebey.sys.web.request;

import com.aek.ebey.sys.model.custom.RoleCustom;
import com.aek.ebey.sys.web.validator.group.Add;
import com.aek.ebey.sys.web.validator.group.Edit;
import com.google.common.collect.Lists;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;

import java.util.List;

/**
 * 角色创建请求接口类
 * 
 * @author Mr.han
 *
 */
@ApiModel
public class RoleRequest extends BaseRequest {

	@ApiModelProperty(value = "角色ID")
	@NotNull(groups = { Edit.class }, message = "G_001")
	private Long id;

	/**
	 * 角色名称
	 */
	@ApiModelProperty(value = "角色名称")
	@NotNull(groups = { Add.class, Edit.class }, message = "R_001")
	private String name;

	/**
	 * 租户ID
	 */
	@ApiModelProperty(value = "租户ID")
	@NotNull(groups = { Add.class, Edit.class }, message = "G_002")
	private Long tenantId;

	/**
	 * 权限ID集合
	 */
	@ApiModelProperty(value = "权限ID集合")
	@NotNull(groups = { Add.class, Edit.class }, message = "R_003")
	private List<Long> permissionIds = Lists.newArrayList();

	/**
	 * 数据范围 [1 所在机构所有数据 2 所在部门及下级部门数据 3 所在部门数据]
	 */
	@ApiModelProperty(value = "数据范围[1 =所在机构所有数据 2= 所在部门及下级部门数据 3= 所在部门数据 4=自定义部门数据]", allowableValues = "1,2,3,4")
	@NotNull(groups = { Add.class, Edit.class }, message = "R_004")
	private Integer dataScope;
	
	/**
	 * 是否只编辑名称
	 */
	@ApiModelProperty(value = "是否只编辑名称")
	private Boolean isEditName;
	
	/*------------------------------------自定义部门信息-------------------------------------*/
	@ApiModelProperty(value = "角色扩展信息(包含自定义部门数据范围部门信息)")
	private RoleCustom roleCustom;

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

	public Long getTenantId() {
		return tenantId;
	}

	public void setTenantId(Long tenantId) {
		this.tenantId = tenantId;
	}

	public Integer getDataScope() {
		return dataScope;
	}

	public void setDataScope(Integer dataScope) {
		this.dataScope = dataScope;
	}

	public List<Long> getPermissionIds() {
		return permissionIds;
	}

	public void setPermissionIds(List<Long> permissionIds) {
		this.permissionIds = permissionIds;
	}

	public Boolean getIsEditName() {
		return isEditName;
	}

	public void setIsEditName(Boolean isEditName) {
		this.isEditName = isEditName;
	}

	public RoleCustom getRoleCustom() {
		return roleCustom;
	}

	public void setRoleCustom(RoleCustom roleCustom) {
		this.roleCustom = roleCustom;
	}

}
