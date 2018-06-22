package com.aek.ebey.sys.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aek.common.core.BeanMapper;
import com.aek.common.core.Result;
import com.aek.common.core.base.BaseController;
import com.aek.ebey.sys.core.jackson.annotation.AllowProperty;
import com.aek.ebey.sys.core.jackson.annotation.IgnoreProperties;
import com.aek.ebey.sys.model.SysRole;
import com.aek.ebey.sys.model.query.RoleQuery;
import com.aek.ebey.sys.model.vo.ModuleMenuVo;
import com.aek.ebey.sys.model.vo.SysPermissionVo;
import com.aek.ebey.sys.model.vo.SysRoleVo;
import com.aek.ebey.sys.service.SysRoleService;
import com.aek.ebey.sys.web.request.RoleRequest;
import com.aek.ebey.sys.web.validator.group.Add;
import com.aek.ebey.sys.web.validator.group.Edit;
import com.baomidou.mybatisplus.plugins.Page;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;

/**
 * <p>
 * 角色表 前端控制器
 * </p>
 *
 * @author aek
 * @since 2017-05-06
 */
@RestController
@RequestMapping("/zbzxsys/role")
@Api(value = "SysRoleController", description = "角色管理")
public class SysRoleController extends BaseController {

	@Autowired
	private SysRoleService roleService;

	/**
	 * 机构新建自定义角色
	 * 
	 * @param request
	 * @return
	 */
	@PreAuthorize("hasAnyAuthority('SYS_ROLE_NEW')")
	@PostMapping(value = "/add")
	@ApiOperation(value = "创建角色", httpMethod = "POST", produces = "application/json")
	@ApiResponse(code = 0, message = "OK", response = Result.class)
	public Result<Object> addRole(@Validated(value = Add.class) @RequestBody RoleRequest request) {
		SysRoleVo role = BeanMapper.map(request, SysRoleVo.class);
		this.roleService.addRole(role);
		return response();
	}

	/**
	 * 获取角色详细信息
	 * 
	 * @param tenantId
	 *            租户ID
	 * @return
	 */
	@PreAuthorize("hasAnyAuthority('SYS_ROLE_NEW')")
	@IgnoreProperties(allow = { @AllowProperty(pojo = SysPermissionVo.class, name = { "id", "parentId", "moduleId",
			"name", "enable", "delFlag", "description" }) })
	@GetMapping(value = "/baseRole/{tenantId}")
	@ApiOperation(value = "权限基础信息（角色创建时获取）", httpMethod = "GET", produces = "application/json")
	@ApiImplicitParams({ @ApiImplicitParam(name = "tenantId", value = "租户ID", paramType = "path", required = true) })
	@ApiResponse(code = 0, message = "OK", response = Result.class)
	public Result<List<ModuleMenuVo>> baseRoleInfo(@PathVariable(required = true) Long tenantId) {
		return response(this.roleService.baseRoleInfo(tenantId));
	}

	/**
	 * 更新角色
	 * 
	 * @param request
	 * @return
	 */
	@PreAuthorize("hasAuthority('SYS_ROLE_MANAGE')")
	@PutMapping(value = "/update")
	@ApiOperation(value = "更新角色", httpMethod = "PUT", produces = "application/json")
	@ApiResponse(code = 0, message = "OK", response = Result.class)
	public Result<Object> updateRole(@Validated(value = Edit.class) @RequestBody RoleRequest request) {
		SysRoleVo role = BeanMapper.map(request, SysRoleVo.class);
		this.roleService.modifyRole(role);
		return response();
	}

	/**
	 * 获取角色详细信息
	 * 
	 * @param id
	 * @return
	 */
	@PreAuthorize("hasAuthority('SYS_ROLE_DETAILED_VIEW')")
	@IgnoreProperties(allow = {
			@AllowProperty(pojo = SysRoleVo.class, name = { "id", "name", "dataScope", "presetId", "createTime",
					"createName", "enable", "delFlag", "moduleMenus","roleCustom"}),
			@AllowProperty(pojo = SysPermissionVo.class, name = { "id", "name", "moduleId", "parentId", "menuFlag",
					"enable", "delFlag", "description" }) })
	@GetMapping(value = "/query/{roleId}")
	@ApiOperation(value = "获取角色详细信息", httpMethod = "GET", produces = "application/json")
	@ApiImplicitParams({ @ApiImplicitParam(name = "roleId", value = "角色ID", paramType = "path", required = true) })
	@ApiResponse(code = 0, message = "OK", response = Result.class)
	public Result<SysRoleVo> roleDetai(@PathVariable(required = true) Long roleId) {
		return response(this.roleService.findRoleDetai(roleId));
	}

	/**
	 * 删除角色
	 * 
	 * @param roleId
	 * @return
	 */
	@PreAuthorize("hasAuthority('SYS_ROLE_MANAGE')")
	@DeleteMapping(value = "/delete/{roleId}")
	@ApiOperation(value = "删除角色", httpMethod = "DELETE", produces = "application/json")
	@ApiResponse(code = 0, message = "OK", response = Result.class)
	@ApiImplicitParams({ @ApiImplicitParam(name = "roleId", value = "角色ID", paramType = "path", required = true),
			@ApiImplicitParam(name = "orgId", value = "机构ID", paramType = "path", required = true) })
	public Result<Object> delete(@PathVariable(value = "roleId", required = true) Long roleId) {
		this.roleService.deleteRoleById(roleId);
		return response();
	}

	/**
	 * 停用角色
	 * 
	 * @param roleId
	 * @param isForced
	 * @return
	 */
	@PreAuthorize("hasAuthority('SYS_ROLE_MANAGE')")
	@PutMapping(value = "/stop/{roleId}")
	@ApiOperation(value = "停用角色", httpMethod = "PUT", produces = "application/json")
	@ApiResponse(code = 0, message = "OK", response = Result.class)
	@ApiImplicitParams({ @ApiImplicitParam(name = "roleId", value = "角色ID", paramType = "path", required = true),
			@ApiImplicitParam(name = "isForced", value = "强制停用[true/false]", paramType = "query", required = false) })
	public Result<Object> stopRole(@PathVariable(value = "roleId", required = true) Long roleId,
			@RequestParam(value = "isForced", required = false, defaultValue = "false") Boolean isForced) {
		this.roleService.stopRole(roleId, isForced);
		return response();
	}

	/**
	 * 恢复角色
	 * 
	 * @param roleId
	 * @return
	 */
	@PreAuthorize("hasAuthority('SYS_ROLE_MANAGE')")
	@PutMapping(value = "/recover/{roleId}")
	@ApiOperation(value = "恢复角色", httpMethod = "PUT", produces = "application/json")
	@ApiResponse(code = 0, message = "OK", response = Result.class)
	@ApiImplicitParam(name = "roleId", value = "角色ID", paramType = "path", required = true)
	public Result<Object> recoverRole(@PathVariable(value = "roleId", required = true) Long roleId) {
		this.roleService.recoverRoleByRoleId(roleId);
		return response();
	}

	/**
	 * 分页查询角色列表
	 * 
	 * @param query
	 * @return
	 */
	@PreAuthorize("hasAuthority('SYS_ROLE_LIST_VIEW')")
	@IgnoreProperties(allow = { @AllowProperty(pojo = SysRole.class, name = { "id", "name", "presetId", "descript",
			"enable", "userCount" }) })
	@GetMapping(value = "/page")
	@ApiOperation(value = "分页查询角色列表", httpMethod = "GET", produces = "application/json")
	@ApiResponse(code = 0, message = "OK", response = Result.class)
	@ApiImplicitParams({ @ApiImplicitParam(name = "pageNo", value = "起始页 [默认1]", paramType = "query", required = false),
			@ApiImplicitParam(name = "pageSize", value = "分页大小[默认10]", paramType = "query", required = false),
			@ApiImplicitParam(name = "tenantId", value = "租户ID [必须]", paramType = "query", required = true),
			@ApiImplicitParam(name = "enable", value = "启用未启用[true=启用,false=未启用]", paramType = "query", required = false),
			@ApiImplicitParam(name = "roleType", value = "角色类型[1=预设角色,2=自定义角色]", paramType = "query", required = false),
			@ApiImplicitParam(name = "keyword", value = "关键字", paramType = "query", required = false) })
	public Result<Page<SysRole>> getByPage(RoleQuery query) {
		return response(this.roleService.findByPage(query));
	}

}
