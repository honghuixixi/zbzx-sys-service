package com.aek.ebey.sys.web.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aek.common.core.Result;
import com.aek.common.core.base.BaseController;
import com.aek.ebey.sys.model.SysUser;
import com.aek.ebey.sys.service.IndexService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;

@RestController
@RequestMapping(value = "/zbzxsys/index")
@Api(value = "IndexController", description = "首页密码重置")
public class IndexController extends BaseController {

	@Autowired
	private IndexService indexService;

	@GetMapping(value = "/sendCode")
	@ApiOperation(value = "找回密码（发送验证码）", httpMethod = "GET", produces = "application/json")
	@ApiImplicitParam(name = "account", value = "手机号码", required = true, paramType = "query")
	@ApiResponse(code = 200, message = "OK", response = Result.class)
	public Result<Object> sendCode(@RequestParam String account) {
		this.indexService.sendCode(account);
		return response();
	}
	
	@GetMapping(value = "/sendRstCode")
	@ApiOperation(value = "用户注册（发送验证码）",httpMethod ="GET",produces = "application/json")
	@ApiImplicitParam(name = "account",value ="手机号", required = true,paramType = "query")
	@ApiResponse(code = 200,message = "OK",response = Result.class)
	public Result<Object> sendRstCode(@RequestParam String account){
		this.indexService.sendRstCode(account);
		return response();
	};

	@GetMapping(value = "/resetPassword")
	@ApiOperation(value = "重置密码", httpMethod = "GET", produces = "application/json")
	@ApiImplicitParams(value = {
			@ApiImplicitParam(name = "userId", value = "用户ID", required = true, paramType = "query"),
			@ApiImplicitParam(name = "password", value = "密码", required = true, paramType = "query"),
			@ApiImplicitParam(name = "confirmPassword", value = "确认密码", required = true, paramType = "query") })
	@ApiResponse(code = 200, message = "OK", response = Result.class)
	public Result<Object> resetPassword(@RequestParam(value = "userId", required = true) Long userId,
			@RequestParam(value = "password", required = true) String password,
			@RequestParam(value = "confirmPassword", required = true) String confirmPassword) {
		return response(indexService.resetPassword(userId, password, confirmPassword));
	}
	
	@GetMapping(value = "/validateCode")
    @ApiOperation(value = "重置密码验证手机验证码", httpMethod = "GET", produces = "application/json")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "account", value = "账户", required = true, paramType = "query"),
            @ApiImplicitParam(name = "code", value = "验证码", required = true, paramType = "query") })
    @ApiResponse(code = 200, message = "OK", response = Result.class)
	public Result<List<SysUser>> validateCode(@RequestParam(value = "account", required = true) String account,
	                @RequestParam(value = "code", required = true) String code) {
	    return response(indexService.validateCode(account, code));
	}

	@GetMapping(value = "/modifyPassword")
    @ApiOperation(value = "修改密码", httpMethod = "GET", produces = "application/json")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "userId", value = "用户ID", required = true, paramType = "query"),
            @ApiImplicitParam(name = "mobile", value = "手机号", required = true, paramType = "query"),
            @ApiImplicitParam(name = "code", value = "验证码", required = true, paramType = "query"),
            @ApiImplicitParam(name = "password", value = "密码", required = true, paramType = "query") })
    @ApiResponse(code = 200, message = "OK", response = Result.class)
    public Result<Object> modifyPassword(@RequestParam(value = "userId", required = true) Long userId,
            @RequestParam(value = "mobile", required = true) String mobile,
            @RequestParam(value = "code", required = true) String code,
            @RequestParam(value = "password", required = true) String password) {
        return response(indexService.modifyPassword(userId, mobile, code, password));
    }
	
}
