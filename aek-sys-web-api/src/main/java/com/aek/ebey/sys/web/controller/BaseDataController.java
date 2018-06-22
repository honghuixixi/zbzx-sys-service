package com.aek.ebey.sys.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aek.common.core.Result;
import com.aek.common.core.base.BaseController;
import com.aek.ebey.sys.service.BaseDataService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;

@RestController
@RequestMapping(value = "/zbzxsys/basedata")
@Api(value = "BaseDataController", description = "基础数据调用")
public class BaseDataController extends BaseController {

	@Autowired
	private BaseDataService baseDataService;

	/**
	 * 获取全局静态基础数据(全局枚举类型)
	 * 
	 * @return
	 */
	@PreAuthorize("isAuthenticated()")
	@GetMapping(value = "/staticVariable")
	@ApiOperation(value = "静态基础数据全局枚举类型", httpMethod = "GET", produces = "application/json")
	@ApiResponse(code = 0, message = "OK", response = Result.class)
	public Result<Object> staticVariable() {
		return response(this.baseDataService.findStaticVariable());
	}
}
