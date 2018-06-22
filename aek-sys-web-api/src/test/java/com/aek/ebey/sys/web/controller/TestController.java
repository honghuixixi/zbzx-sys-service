package com.aek.ebey.sys.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aek.common.core.Result;
import com.aek.common.core.base.BaseController;
import com.aek.common.core.sms.Sms;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;

@RestController
@RequestMapping(value = "/sys/test")
@Api(value = "TestController", description = "测试")
public class TestController extends BaseController {

	@Autowired
	private Sms sms;

	@GetMapping(value = "/demo2")
	@ApiOperation(value = "短信发送测试（验证码）", httpMethod = "GET", produces = "application/json")
	@ApiImplicitParams(value = {
			@ApiImplicitParam(name = "mobile", value = "手机号码", required = true, paramType = "query"),
			@ApiImplicitParam(name = "code", value = "验证码", required = true, paramType = "query") })
	@ApiResponse(code = 0, message = "OK", response = Result.class)
	public Result<Object> sentSmsTest1(@RequestParam(value = "mobile", required = true) String mobile,
			@RequestParam(value = "code", required = true) String code) {
		this.sms.sendCodeAsyn(mobile, code);
		return response(this.sms.sendCode(mobile, code));
	}

	@GetMapping(value = "/demo3")
	@ApiOperation(value = "短信发送测试（登录通知）", httpMethod = "GET", produces = "application/json")
	@ApiImplicitParams(value = {
			@ApiImplicitParam(name = "mobile", value = "手机号码", required = true, paramType = "query"),
			@ApiImplicitParam(name = "code", value = "验证码", required = true, paramType = "query"),
			@ApiImplicitParam(name = "expire", value = "有效期", required = true, paramType = "query") })
	@ApiResponse(code = 0, message = "OK", response = Result.class)
	public Result<Object> sentSmsTest2(@RequestParam(value = "mobile", required = true) String mobile,
			@RequestParam(value = "code", required = true) String code,
			@RequestParam(value = "expire", required = true) Integer expire) {
		this.sms.sendLoginPwdAsyn(mobile, code, expire);
		return response();
	}
}
