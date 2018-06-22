package com.aek.ebey.sys.web.controller.invoking;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aek.common.core.Result;
import com.aek.common.core.base.BaseController;
import com.aek.ebey.sys.model.SysDept;
import com.aek.ebey.sys.service.SysDeptService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;

/**
 * 部门提供服务调用
 * 
 * @author Mr.han
 *
 */
@RestController
@RequestMapping(value = "/zbzxsys/invoke/dept")
@Api(value = "InvokeDeptController", description = "部门服务调用")
public class InvokeDeptController extends BaseController {

	@Autowired
	private SysDeptService sysDeptService;

	/**
	 * 根据部门ids查询部门列表
	 * 
	 * @param ids
	 * @return
	 */
	@PreAuthorize("isAuthenticated()")
	@GetMapping(value = "/list/{ids}")
	@ApiOperation(value = "根据部门ids查询部门列表")
	@ApiImplicitParam(name = "ids", value = "部门ids", paramType = "path", required = true)
	@ApiResponse(code = 200, message = "OK", response = Result.class)
	public Result<List<SysDept>> findByDeptIds(@PathVariable(value = "ids", required = true) String ids) {
		List<SysDept> depts = this.sysDeptService.findByIds(ids);
		return response(depts);
	}

	/**
	 * 根据部门ids查询部门列表
	 * 
	 * @param ids
	 * @return
	 */
	@PreAuthorize("isAuthenticated()")
	@GetMapping(value = "/{deptId}/sub")
	@ApiOperation(value = "根据部门id查询部门列表")
	@ApiImplicitParam(name = "deptId", value = "部门Id", paramType = "path", required = true)
	@ApiResponse(code = 200, message = "OK", response = Result.class)
	public Result<String> findAllSubDeptById(@PathVariable(value = "deptId", required = true) Long deptId) {
		return response(this.sysDeptService.findAllSubDeptStr(deptId));
	}

}
