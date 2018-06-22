package com.aek.ebey.sys.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aek.common.core.BeanMapper;
import com.aek.common.core.Result;
import com.aek.common.core.base.BaseController;
import com.aek.ebey.sys.core.jackson.annotation.AllowProperty;
import com.aek.ebey.sys.core.jackson.annotation.IgnoreProperties;
import com.aek.ebey.sys.model.SysUser;
import com.aek.ebey.sys.model.vo.SysRoleVo;
import com.aek.ebey.sys.model.vo.SysTenantRoleVo;
import com.aek.ebey.sys.service.SysRoleUserService;
import com.aek.ebey.sys.web.request.TenantRoleRequest;
import com.aek.ebey.sys.web.validator.group.Add;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;

/**
 * <p>
 * 用户角色关联表 前端控制器
 * </p>
 *
 * @author aek
 * @since 2017-05-06
 */
@RestController
@RequestMapping("/zbzxsys/roleUser")
@Api(value = "SysRoleUserController", description = "用户角色关系")
public class SysRoleUserController extends BaseController {

	@Autowired
	private SysRoleUserService roleUserService;

	@PreAuthorize("hasAnyAuthority('SYS_ROLE_MANAGE','SYS_ROLE_DETAILED_VIEW')")
	@IgnoreProperties(allow = { @AllowProperty(pojo = SysUser.class, name = { "id", "realName", "mobile", "email",
			"deptName", "jobName", "enable" }) })
	@GetMapping(value = "/role/{roleId}")
	@ApiOperation(value = "查询使用当前角色的用户", httpMethod = "GET", produces = "application/json")
	@ApiImplicitParams(value = {
			@ApiImplicitParam(name = "roleId", value = "角色ID", paramType = "path", required = true),
			@ApiImplicitParam(name = "status", value = "用户状态[true=启用,false=停用]", paramType = "query", required = false) })
	@ApiResponse(code = 0, message = "OK", response = Result.class)
	public Result<List<SysUser>> getUserByRoleIdAndStatus(@PathVariable(value = "roleId", required = true) Long roleId,
			@RequestParam(value = "status", required = false) Boolean status) {
		return response(this.roleUserService.findUserByRoleIdAndStatus(roleId, status));
	}

	@PreAuthorize("hasAuthority('SYS_USER_MANAGE')")
	@IgnoreProperties(allow = {
			@AllowProperty(pojo = SysTenantRoleVo.class, name = { "tenantId", "tenantName", "roles" }),
			@AllowProperty(pojo = SysRoleVo.class, name = { "name", "id" }) })
	@GetMapping(value = "/user/{userId}")
	@ApiOperation(value = "查询当前用户的角色", httpMethod = "GET", produces = "application/json")
	@ApiImplicitParam(name = "userId", value = "用户ID", paramType = "path", required = true)
	@ApiResponse(code = 0, message = "OK", response = Result.class)
	public Result<List<SysTenantRoleVo>> getRoleByUserId(@PathVariable(value = "userId", required = true) Long userId) {
		return response(this.roleUserService.findRoleByUserId(userId));
	}

	@PreAuthorize("hasAuthority('SYS_USER_MANAGE')")
	@IgnoreProperties(allow = {
			@AllowProperty(pojo = SysTenantRoleVo.class, name = { "tenantId", "tenantName", "roles" }),
			@AllowProperty(pojo = SysRoleVo.class, name = { "name", "id", "check" }) })
	@GetMapping(value = "/all/user/{userId}")
	@ApiOperation(value = "查询用户所有的角色信息(包含勾选未勾选)", httpMethod = "GET", produces = "application/json")
	@ApiImplicitParam(name = "userId", value = "用户ID", paramType = "path", required = true)
	@ApiResponse(code = 0, message = "OK", response = Result.class)
	public Result<List<SysTenantRoleVo>> getUserAllRole(@PathVariable(value = "userId", required = true) Long userId) {
		return response(this.roleUserService.findAllRoleByUserId(userId));
	}

	@PreAuthorize("hasAuthority('SYS_USER_MANAGE')")
	@IgnoreProperties(allow = { @AllowProperty(pojo = SysRoleVo.class, name = { "name", "id", "check" }) })
	@GetMapping(value = "/user/{userId}/tenant/{tenantId}")
	@ApiOperation(value = "查询用户指定租户下的角色(包含勾选未勾选)", httpMethod = "GET", produces = "application/json")
	@ApiImplicitParams(value = {
			@ApiImplicitParam(name = "userId", value = "用户ID", paramType = "path", required = true),
			@ApiImplicitParam(name = "tenantId", value = "租户ID", paramType = "path", required = true) })
	@ApiResponse(code = 0, message = "OK", response = Result.class)
	public Result<SysTenantRoleVo> getRoleByUserIdAndTenantId(
			@PathVariable(value = "userId", required = true) Long userId,
			@PathVariable(value = "tenantId", required = true) Long tenantId) {
		return response(this.roleUserService.findRoleByUserIdAndTenantId(userId, tenantId));
	}
	
	@PreAuthorize("hasAuthority('SYS_USER_MANAGE')")
	@PostMapping(value = "/edit/tenant")
	@ApiOperation(value = "编辑用户指定租户的角色", httpMethod = "POST", produces = "application/json")
	@ApiResponse(code = 0, message = "OK", response = Result.class)
	public Result<Object> modifyUserRoleByTenant(@Validated(value = Add.class) @RequestBody TenantRoleRequest request) {
		SysTenantRoleVo tenantRoleVo = BeanMapper.map(request, SysTenantRoleVo.class);
		this.roleUserService.modifyUserRoleByTenant(tenantRoleVo);
		return response();
	}

	/**
	 * 编辑用户所有的角色
	 * 
	 * @param request
	 * @return
	 */
	@PreAuthorize("hasAuthority('SYS_USER_MANAGE')")
	@PostMapping(value = "/edit/all")
	@ApiOperation(value = "编辑用户所有角色", httpMethod = "POST", produces = "application/json")
	@ApiResponse(code = 0, message = "OK", response = Result.class)
	public Result<Object> modifyUserRoleAll(@RequestBody List<TenantRoleRequest> request) {
		List<SysTenantRoleVo> tenantRoleVos = BeanMapper.mapList(request, SysTenantRoleVo.class);
		this.roleUserService.modifyUserRoleAll(tenantRoleVos);
		return response();
	}

	@PreAuthorize("hasAuthority('SYS_USER_MANAGE')")
	@DeleteMapping(value = "/user/{userId}/tenant/{tenantId}")
	@ApiOperation(value = "删除用户指定租户的全部角色", httpMethod = "DELETE", produces = "application/json")
	@ApiImplicitParams(value = {
			@ApiImplicitParam(name = "userId", value = "用户ID", paramType = "path", required = true),
			@ApiImplicitParam(name = "tenantId", value = "租户ID", paramType = "path", required = true) })
	@ApiResponse(code = 0, message = "OK", response = Result.class)
	public Result<Object> delete(@PathVariable(value = "userId", required = true) Long userId,
			@PathVariable(value = "tenantId", required = true) Long tenantId) {
		this.roleUserService.deleteByTenantId(userId, tenantId);
		return response();
	}

}
