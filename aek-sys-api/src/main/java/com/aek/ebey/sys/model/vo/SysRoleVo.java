package com.aek.ebey.sys.model.vo;

import java.util.List;

import com.aek.ebey.sys.model.SysRole;
import com.aek.ebey.sys.model.custom.RoleCustom;
import com.google.common.collect.Lists;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 角色输出类
 * 
 * @author Mr.han
 *
 */
@ApiModel
public class SysRoleVo extends SysRole {

	private static final long serialVersionUID = 1L;
	/*------------------------角色查询---------------------------*/
	@ApiModelProperty(value = "创建人名称")
	private String createName;

	/**
	 * 角色的模块权限
	 */
	private List<ModuleMenuVo> moduleMenus = Lists.newArrayList();

	/**
	 * 查询用户角色，当前角色是否被选中
	 */
	private Boolean check;

	/*------------------------角色创建---------------------------*/
	/**
	 * 权限ID集合
	 */
	@ApiModelProperty(value = "权限ID集合")
	private List<Long> permissionIds = Lists.newArrayList();
	
	/*------------------------------------医疗租户信息-------------------------------------*/
	@ApiModelProperty(value = "角色扩展信息(包含自定义部门数据范围部门信息)")
	private RoleCustom roleCustom;
	
	/**
	 * 是否只编辑名称
	 */
	private Boolean isEditName;

	public List<Long> getPermissionIds() {
		return permissionIds;
	}

	public void setPermissionIds(List<Long> permissionIds) {
		this.permissionIds = permissionIds;
	}

	public String getCreateName() {
		return createName;
	}

	public void setCreateName(String createName) {
		this.createName = createName;
	}

	public List<ModuleMenuVo> getModuleMenus() {
		return moduleMenus;
	}

	public void setModuleMenus(List<ModuleMenuVo> moduleMenus) {
		this.moduleMenus = moduleMenus;
	}

	public Boolean getCheck() {
		return check;
	}

	public void setCheck(Boolean check) {
		this.check = check;
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
