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
import com.aek.ebey.sys.model.SysModule;
import com.aek.ebey.sys.model.SysTenantModule;
import com.aek.ebey.sys.model.vo.SysTenantModuleVo;
import com.aek.ebey.sys.service.SysModuleService;
import com.aek.ebey.sys.service.SysTenantModuleService;
import com.aek.ebey.sys.web.request.TenantModuleRequest;
import com.aek.ebey.sys.web.validator.group.Add;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;

/**
 * <p>
 * 机构模块表 前端控制器
 * </p>
 *
 * @author aek
 * @since 2017-05-06
 */
@Api(value = "SysTenantModuleController", description = "机构模块管理")
@RestController
@RequestMapping("/zbzxsys/tenantModule")
public class SysTenantModuleController extends BaseController {

	@Autowired
	private SysTenantModuleService tenantModuleService;

	/**
	 * 移除租户模块(级联移除子租户 模块及权限)
	 * 
	 * @param tenantId
	 * @param moduleId
	 * @return
	 */
	@PreAuthorize("hasAuthority('SYS_TENANT_MANAGE')")
	@DeleteMapping(value = "/delete/tenant/{tenantId}/module/{moduleId}")
	@ApiOperation(value = "移除租户模块(级联移除子租户模块及权限)", httpMethod = "DELETE", produces = "application/json")
	@ApiImplicitParams(value = {
			@ApiImplicitParam(name = "tenantId", value = "租户ID", paramType = "path", required = true),
			@ApiImplicitParam(name = "moduleId", value = "模块ID", paramType = "path", required = true) })
	@ApiResponse(code = 200, message = "OK", response = Result.class)
	public Result<Object> deleteTenantModule(@PathVariable(value = "tenantId", required = true) Long tenantId,
			@PathVariable(value = "moduleId", required = true) Long moduleId) {
		this.tenantModuleService.deleteTenantModule(tenantId, moduleId);
		return response();
	}

	/**
	 * 获取当前租户可添加模块列表
	 * 
	 * @param tenantId
	 * @return
	 */
	@PreAuthorize("hasAuthority('SYS_TENANT_MANAGE')")
	@IgnoreProperties(allow = { @AllowProperty(pojo = SysModule.class, name = { "id", "name" }) })
	@GetMapping(value = "/canAddModule/tenant/{tenantId}")
	@ApiOperation(value = "获取当前租户可添加模块列表", httpMethod = "GET", produces = "application/json")
	@ApiImplicitParam(name = "tenantId", value = "租户ID", paramType = "path", required = true)
	@ApiResponse(code = 200, message = "OK", response = Result.class)
	public Result<List<SysModule>> getTeanantCanAddModule(
			@PathVariable(value = "tenantId", required = true) Long tenantId) {
		return response(this.tenantModuleService.findTenantCanAddModule(tenantId));
	}

	/**
	 * 添加租户模块
	 * 
	 * @param request
	 * @return
	 */
	@PreAuthorize("hasAuthority('SYS_TENANT_MANAGE')")
	@PostMapping(value = "/add")
	@ApiOperation(value = "添加模块", httpMethod = "POST", produces = "application/json")
	@ApiResponse(code = 200, message = "OK", response = Result.class)
	public Result<Object> addTenantModule(@Validated(value = Add.class) @RequestBody TenantModuleRequest request) {
		this.tenantModuleService.addTenantModule(request.getTenantId(), request.getModuleIds());
		return response();
	}

	@Autowired
	private SysModuleService moduleService;

	/**
	 * 根据模块分类查询模块列表
	 *
	 * @return
	 */
	@PreAuthorize("hasAuthority('SYS_TENANT_MANAGE')")
	@GetMapping(value = "/tenant/{tenantId}")
	@ApiOperation(value = "查询机构模块列表（类型分类）", httpMethod = "GET", produces = "application/json")
	@ApiImplicitParams(value = {
			@ApiImplicitParam(name = "tenantId", value = "机构ID", paramType = "path", required = true),
			@ApiImplicitParam(name = "moduleType", value = "模块类型", paramType = "query", required = false) })
	@ApiResponse(code = 200, message = "OK", response = Result.class)
	public Result<List<SysTenantModuleVo>> findByTenantIdAndModuleType(@PathVariable("tenantId") Long tenantId,
			@RequestParam(value = "moduleType", required = false) Long moduleType) {
		List<SysTenantModule> modules = this.tenantModuleService.findByTenantIdAndModuleType(tenantId, moduleType);
		List<SysTenantModuleVo> moduleVos = BeanMapper.mapList(modules, SysTenantModuleVo.class);
		for (SysTenantModuleVo moduleVo : moduleVos) {
			moduleVo.setDefaultExist(moduleService.isDefaultExist(moduleVo.getModuleId()));
		}
		return response(moduleVos);
	}

}
