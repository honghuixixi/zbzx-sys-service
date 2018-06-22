package com.aek.ebey.sys.web.controller.invoking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aek.common.core.Result;
import com.aek.common.core.base.BaseController;
import com.aek.ebey.sys.model.SysPermission;
import com.aek.ebey.sys.service.SysPermissionService;
import com.aek.ebey.sys.service.SysRoleUserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;

@RestController
@RequestMapping(value = "/zbzxsys/invoke/role")
@Api(value = "InvokeRoleController", description = "用户角色权限服务调用")
public class InvokeRoleController extends BaseController {

	@Autowired
	private SysRoleUserService roleUserService;

	@Autowired
	private SysPermissionService permissionService;

	/**
	 * 根据部门ids查询部门列表
	 * 
	 * @param ids
	 * @return
	 */
	@PreAuthorize("isAuthenticated()")
	@GetMapping(value = "/user/{userId}/tenantId/{tenantId}/permissCode/{code}")
	@ApiOperation(value = "查询用户指定权限的数据范围  返回0 不存在", httpMethod = "GET")
	@ApiImplicitParams(value = {
			@ApiImplicitParam(name = "userId", value = "用户ID", paramType = "path", required = true),
			@ApiImplicitParam(name = "tenantId", value = "机构ID", paramType = "path", required = true),
			@ApiImplicitParam(name = "code", value = "权限code标识", paramType = "path", required = true) })
	@ApiResponse(code = 200, message = "OK", response = Result.class)
	public Result<Integer> findUserDateScopeByPermissCode(@PathVariable(value = "userId", required = true) Long userId,
			@PathVariable(value = "tenantId", required = true) Long tenantId,
			@PathVariable(value = "code", required = true) String code) {
		SysPermission permission = this.permissionService.findByCode(code);
		int dateScope = 0;
		if (permission != null) {
			dateScope = this.roleUserService.findUserDateScopeByPermissId(userId, tenantId, permission.getId());
		}
		return response(dateScope);
	}

}
