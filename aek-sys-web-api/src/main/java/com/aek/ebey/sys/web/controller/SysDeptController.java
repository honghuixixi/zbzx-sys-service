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
import com.aek.common.core.serurity.WebSecurityUtils;
import com.aek.ebey.sys.core.jackson.annotation.AllowProperty;
import com.aek.ebey.sys.core.jackson.annotation.IgnoreProperties;
import com.aek.ebey.sys.model.SysDept;
import com.aek.ebey.sys.model.query.DeptQuery;
import com.aek.ebey.sys.model.vo.SysDeptVo;
import com.aek.ebey.sys.service.SysDeptService;
import com.aek.ebey.sys.web.request.DeptRequest;
import com.aek.ebey.sys.web.validator.group.Add;
import com.aek.ebey.sys.web.validator.group.Edit;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;

/**
 * <p>
 * 部门表 前端控制器
 * </p>
 *
 * @author aek
 * @since 2017-05-06
 */
@RestController
@RequestMapping("/zbzxsys/dept")
@Api(value = "SysDeptController", description = "部门管理")
public class SysDeptController extends BaseController {

	@Autowired
	private SysDeptService sysDeptService;

	@PreAuthorize("hasAuthority('SYS_DEPT_VIEW_EDIT_DELETE')")
	@PostMapping(value = "/save")
	@ApiOperation(value = "新建部门", httpMethod = "POST", produces = "application/json")
	@ApiResponse(code = 200, message = "OK", response = Result.class)
	public Result<Object> save(@Validated(value = Add.class) @RequestBody DeptRequest request) {
		SysDept sysDept = BeanMapper.map(request, SysDept.class);
		this.sysDeptService.save(sysDept);
		return response();
	}

	@PreAuthorize("hasAuthority('SYS_DEPT_VIEW_EDIT_DELETE')")
	@PostMapping(value = "/edit")
	@ApiOperation(value = "修改部门", httpMethod = "POST", produces = "application/json")
	@ApiResponse(code = 200, message = "OK", response = Result.class)
	public Result<SysDept> edit(@Validated(value = Edit.class) @RequestBody DeptRequest request) {
		SysDept sysDept = BeanMapper.map(request, SysDept.class);
		sysDeptService.edit(sysDept);
		return response(sysDept);
	}

	@PreAuthorize("hasAuthority('SYS_DEPT_VIEW_EDIT_DELETE')")
	@DeleteMapping(value = "/delete/{deptId}")
	@ApiOperation(value = "删除指定的部门", httpMethod = "DELETE", produces = "application/json")
	@ApiImplicitParams(value = {
			@ApiImplicitParam(name = "deptId", value = "部门ID", paramType = "path", required = true),
			@ApiImplicitParam(name = "forceDel", value = "强制删除(false 只作查询是否可以删除)", paramType = "query", required = true) })
	@ApiResponse(code = 200, message = "OK", response = Result.class)
	public Result<Object> delete(@PathVariable(value = "deptId", required = true) Long deptId,
			@RequestParam(value = "forceDel", required = true) Boolean forceDel) {
		this.sysDeptService.delete(deptId, forceDel);
		return response();
	}

	/**
	 * 搜索部门（过滤掉设备所在部门）(未启用)
	 * 
	 * @param tenantId
	 * @param keyword
	 * @return
	 */
	@PreAuthorize("hasAuthority('SYS_DEPT_VIEW_EDIT_DELETE')")
	@IgnoreProperties(allow = { @AllowProperty(pojo = SysDept.class, name = { "id", "name" }) })
	@GetMapping(value = "/search/tenant/{tenantId}/{deptId}")
	@ApiOperation(value = "搜索部门（过滤掉设备所在部门）", httpMethod = "GET", produces = "application/json")
	@ApiImplicitParams({ @ApiImplicitParam(name = "keyword", value = "关键字检索", paramType = "query"),
			@ApiImplicitParam(name = "tenantId", value = "机构ID", paramType = "path", required = true),
			@ApiImplicitParam(name = "deptId", value = "设备所在部门ID", paramType = "path", required = true),
			@ApiImplicitParam(name = "name", value = "部门名称", paramType = "query") })
	@ApiResponse(code = 200, message = "OK", response = Result.class)
	public Result<List<SysDept>> findBykeywordExcludeSelf(@PathVariable(value = "tenantId", required = true) Long tenantId,
			@PathVariable(value = "deptId", required = true) Long deptId,DeptQuery query) {
		query.setTenantId(tenantId);
		query.setDeptId(deptId);
		return response(this.sysDeptService.findByKeywordExcludeSelf(query));
	}
	
	/**
	 * 关键词搜索
	 * 
	 * @param tenantId
	 * @param keyword
	 * @return
	 */
	@PreAuthorize("hasAuthority('SYS_DEPT_VIEW_EDIT_DELETE')")
	@IgnoreProperties(allow = { @AllowProperty(pojo = SysDept.class, name = { "id", "name" }) })
	@GetMapping(value = "/search/tenant/{tenantId}")
	@ApiOperation(value = "搜索部门", httpMethod = "GET", produces = "application/json")
	@ApiImplicitParams({ @ApiImplicitParam(name = "keyword", value = "关键字检索", paramType = "query"),
			@ApiImplicitParam(name = "tenantId", value = "机构ID", paramType = "path", required = true),
			@ApiImplicitParam(name = "name", value = "部门名称", paramType = "query") })
	@ApiResponse(code = 200, message = "OK", response = Result.class)
	public Result<List<SysDept>> findBykeyword(@PathVariable(value = "tenantId", required = true) Long tenantId,
			DeptQuery query) {
		query.setTenantId(tenantId);
		return response(this.sysDeptService.findByKeyword(query));
	}

	/**
	 * 查询部门树
	 * 
	 * @param tenantId
	 * @return
	 */
	// @PreAuthorize("hasAnyAuthority('SYS_USER_DEPT_VIEW')")
	@IgnoreProperties(allow = { @AllowProperty(pojo = SysDeptVo.class, name = { "id", "name", "preset", "subDepts" }) })
	@GetMapping(value = "/tree/tenant/{tenantId}")
	@ApiOperation(value = "查询部门树列表", httpMethod = "GET", produces = "application/json")
	@ApiImplicitParam(name = "tenantId", value = "租户ID", paramType = "path")
	@ApiResponse(code = 200, message = "OK", response = Result.class)
	public Result<SysDeptVo> findDeptTree(@PathVariable(value = "tenantId", required = true) Long tenantId) {
		return response(this.sysDeptService.findDeptTree(tenantId));
	}

	/**
	 * 根据部门ID集合查询部门信息
	 * 
	 * @param ids
	 * @return
	 */
	@PostMapping(value = "/findByIds", produces = "application/json;charset=UTF-8")
	@ApiOperation(value = "根据id集合批量查询部门信息")
	@ApiResponse(code = 200, message = "OK", response = Result.class)
	public Result<List<SysDept>> findUserByIds(@RequestBody Long[] ids) {
		List<SysDept> list = this.sysDeptService.findByIds(ids);
		return response(list);
	}
	
	/**
	 * 查询机构下所有部门集合
	 * @param query
	 * @return
	 */
	@PreAuthorize("isAuthenticated()")
	@IgnoreProperties(allow = { @AllowProperty(pojo = SysDept.class, name = { "id", "name" }) })
	@GetMapping(value = "/getDeptList")
	@ApiOperation(value = "查询机构下所有部门集合", httpMethod = "GET", produces = "application/json")
	@ApiImplicitParams({ 
		@ApiImplicitParam(name = "keyword", value = "关键字检索", paramType = "query"),
		@ApiImplicitParam(name = "name", value = "部门名称", paramType = "query") 
	})
	@ApiResponse(code = 200, message = "OK", response = Result.class)
	public Result<List<SysDept>> getDeptList(DeptQuery query) {
		Long tenantId = WebSecurityUtils.getCurrentUser().getTenantId();
		logger.debug(">>>>>根据机构ID获取本机构部门列表<<<<<<");
		logger.debug("查询参数：tenantId = " + tenantId);
		query.setTenantId(tenantId);
		return response(this.sysDeptService.findByKeyword(query));
	}

}
