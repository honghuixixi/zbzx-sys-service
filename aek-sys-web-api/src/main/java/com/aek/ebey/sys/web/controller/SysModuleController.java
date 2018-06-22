package com.aek.ebey.sys.web.controller;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
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
import com.aek.ebey.sys.enums.TenantType;
import com.aek.ebey.sys.model.SysModule;
import com.aek.ebey.sys.model.SysUser;
import com.aek.ebey.sys.model.custom.ModuleCustom;
import com.aek.ebey.sys.model.query.ModuleQuery;
import com.aek.ebey.sys.service.SysModuleService;
import com.aek.ebey.sys.service.SysUserService;
import com.aek.ebey.sys.web.request.ModuleRequest;
import com.aek.ebey.sys.web.validator.group.Add;
import com.baomidou.mybatisplus.plugins.Page;
import com.google.common.collect.Lists;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;

/**
 * <p>
 * 系统模块表 前端控制器
 * </p>
 *
 * @author aek
 * @since 2017-05-06
 */
@RestController
@RequestMapping("/zbzxsys/sysModule")
public class SysModuleController extends BaseController {

	@Autowired
	private SysModuleService moduleService;

	@Autowired
	private SysUserService userService;

	/**
	 * 添加新模块
	 * 
	 * @param request
	 * @return
	 */
	@PreAuthorize("isAuthenticated()")
	@PostMapping(value = "/")
	@ApiOperation(value = "添加模块", httpMethod = "POST", produces = "application/json")
	@ApiResponse(code = 200, message = "OK", response = Result.class)
	public Result<Object> add(@Validated(value = { Add.class }) ModuleRequest request) {
		SysModule module = this.disposeModuleInfo(request);
		this.moduleService.add(module);
		return response();
	}

	/**
	 * 模块修改
	 * 
	 * @param request
	 * @return
	 */
	@PreAuthorize("isAuthenticated()")
	@PutMapping(value = "/")
	@ApiOperation(value = "修改模块", httpMethod = "PUT", produces = "application/json")
	@ApiResponse(code = 200, message = "OK", response = Result.class)
	public Result<Object> edit(@Validated(value = { Add.class }) ModuleRequest request) {
		SysModule module = this.disposeModuleInfo(request);
		this.moduleService.edit(module);
		return response();
	}

	/**
	 * 处理模块添加修改数据
	 * 
	 * @param request
	 * @return
	 */
	private SysModule disposeModuleInfo(ModuleRequest request) {

		// 目前模块适用于所有的机构类型
		List<Integer> tenantType = request.getTenantType();
		if (CollectionUtils.isEmpty(tenantType)) {
			tenantType = tenantType == null ? Lists.newArrayList() : tenantType;
			for (TenantType type : TenantType.values()) {
				tenantType.add(type.getNumber());
			}
		}

		ModuleCustom custom = new ModuleCustom();
		custom.setDefaultExist(request.getDefaultExist());
		custom.setTenantType(tenantType);

		SysModule module = BeanMapper.map(request, SysModule.class);
		module.setCoustom(custom);
		return module;
	}

	/**
	 * 删除模块
	 * 
	 * @param id
	 * @return
	 */
	@PreAuthorize("isAuthenticated()")
	@DeleteMapping(value = "/{id}")
	@ApiOperation(value = "删除模块", httpMethod = "DELETE", produces = "application/json")
	@ApiImplicitParam(name = "id", value = "模块ID", paramType = "path", required = true)
	@ApiResponse(code = 200, message = "OK", response = Result.class)
	public Result<Object> delete(@PathVariable(name = "id", required = true) Long id) {
		this.moduleService.delete(id);
		return response();
	}

	/**
	 * 分页查询模块列表
	 * 
	 * @param query
	 * @return
	 */
	@PreAuthorize("isAuthenticated()")
	@GetMapping(value = "/")
	@ApiOperation(value = "分页查询模块列表", httpMethod = "GET", produces = "application/json")
	@ApiImplicitParams({ @ApiImplicitParam(name = "pageNo", value = "起始页 [默认1]", paramType = "query", required = false),
			@ApiImplicitParam(name = "pageSize", value = "分页大小[默认10]", paramType = "query", required = false),
			@ApiImplicitParam(name = "keyword", value = "关键字", paramType = "query", required = false) })
	@ApiResponse(code = 200, message = "OK", response = Result.class)
	public Result<Page<SysModule>> getPageByKeyword(ModuleQuery query) {
		Page<SysModule> page = this.moduleService.findPageByKeyword(query);
		List<SysModule> modules = page.getRecords();

		// 查询创建人修改人
		if (CollectionUtils.isNotEmpty(modules)) {
			for (SysModule module : modules) {
				if (module.getCreateBy() != null) {
					SysUser createUser = this.userService.selectById(module.getCreateBy());
					if (createUser != null) {
						module.setCreateName(createUser.getRealName());
					}
				}
				if (module.getUpdateBy() != null) {
					SysUser updateUser = this.userService.selectById(module.getCreateBy());
					if (updateUser != null) {
						module.setUpdateName(updateUser.getRealName());
					}
				}
			}
		}
		return response(page);
	}

}
