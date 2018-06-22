package com.aek.ebey.sys.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aek.common.core.Result;
import com.aek.common.core.base.BaseController;
import com.aek.ebey.sys.model.SysApi;
import com.aek.ebey.sys.service.SysApiService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * <p>
 * 系统api表 前端控制器
 * </p>
 *
 * @author aek
 * @since 2017-05-06
 */
@RestController
@RequestMapping("/zbzxsys/api")
@Api(description="系统API管理")
public class SysApiController extends BaseController {

	@Autowired
	private SysApiService sysApiService;

	@PostMapping("/add")
	@ApiOperation(code=200, value="OK", notes="新增API", httpMethod="POST")
	public Result<Object> add(@RequestBody SysApi sysApi){
		sysApiService.insert(sysApi);
		return response();
	}
}
