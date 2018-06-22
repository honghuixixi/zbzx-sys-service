package com.aek.ebey.sys.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aek.common.core.BeanMapper;
import com.aek.common.core.Result;
import com.aek.common.core.base.BaseController;
import com.aek.ebey.sys.model.SysPermission;
import com.aek.ebey.sys.service.SysPermissionService;
import com.aek.ebey.sys.web.request.PermissionRequest;
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
 * 权限表 前端控制器
 * </p>
 *
 * @author aek
 * @since 2017-05-06
 */
@RestController
@RequestMapping("/zbzxsys/permission")
@Api(value = "SysPermissionController", description = "权限管理")
public class SysPermissionController extends BaseController {

	@Autowired
	private SysPermissionService permissionService;

	/**
	 * 添一条权限
	 * 
	 * @param request
	 * @return
	 */
	@PreAuthorize("isAuthenticated()")
	@PostMapping(value = "/")
	@ApiOperation(value = "添加权限", httpMethod = "POST", produces = "application/json")
	@ApiResponse(code = 200, message = "OK", response = Result.class)
	public Result<Object> add(@Validated(value = { Add.class }) PermissionRequest request) {
		SysPermission permission = BeanMapper.map(request, SysPermission.class);
		this.permissionService.add(permission);
		return response();
	}
	
	/**
	 * 添加权限关联信息
	 * 
	 * @param request
	 * @return
	 */
	@PreAuthorize("isAuthenticated()")
	@PostMapping(value = "/add/{moduleId}/{permissionId}")
	@ApiOperation(value = "添加权限关联信息", httpMethod = "POST", produces = "application/json")
	@ApiResponse(code = 200, message = "OK", response = Result.class)
	public Result<Object> addPermissionRelation(@PathVariable(name = "moduleId", required = true) Long moduleId,@PathVariable(name = "permissionId", required = true) Long permissionId) {
		this.permissionService.addPermissionRelation(moduleId, permissionId);
		return response();
	}

	/**
	 * 修改权限
	 * 
	 * @param request
	 * @return
	 */
	@PreAuthorize("isAuthenticated()")
	@PutMapping(value = "/")
	@ApiOperation(value = "修改权限", httpMethod = "POST", produces = "application/json")
	@ApiResponse(code = 200, message = "OK", response = Result.class)
	public Result<Object> edit(@Validated(value = { Edit.class }) PermissionRequest request) {
		SysPermission permission = BeanMapper.map(request, SysPermission.class);
		this.permissionService.edit(permission);
		return response();
	}

	/**
	 * 删除权限
	 * 
	 * @param request
	 * @return
	 */
	@PreAuthorize("isAuthenticated()")
	@DeleteMapping(value = "/{id}")
	@ApiOperation(value = "删除", httpMethod = "DELETE", produces = "application/json")
	@ApiImplicitParam(name = "id", value = "权限ID", paramType = "path", required = true)
	@ApiResponse(code = 200, message = "OK", response = Result.class)
	public Result<Object> delete(@PathVariable(name = "id", required = true) Long id) {
		this.permissionService.delete(id);
		return response();
	}

	/**
	 * 分页查询权限列表
	 * 
	 * @param request
	 * @return
	 */
	@PreAuthorize("isAuthenticated()")
	@GetMapping(value = "/")
	@ApiOperation(value = "分页查询权限列表", httpMethod = "GET", produces = "application/json")
	@ApiImplicitParams({ @ApiImplicitParam(name = "pageNo", value = "起始页 [默认1]", paramType = "query", required = false),
			@ApiImplicitParam(name = "pageSize", value = "分页大小[默认10]", paramType = "query", required = false),
			@ApiImplicitParam(name = "keyword", value = "关键字", paramType = "query", required = false) })
	@ApiResponse(code = 200, message = "OK", response = Result.class)
	public Result<Page<SysPermission>> getPageByKeyword(PermissionRequest request) {
		return null;
	}

}
