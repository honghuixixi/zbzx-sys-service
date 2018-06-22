package com.aek.ebey.sys.web.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.aek.common.core.base.BaseController;
import com.aek.ebey.sys.service.ribbon.AuthClientService;

import io.swagger.annotations.Api;

@RestController
@RequestMapping(value = "/sys/cache/test")
@Api(value = "TestController", description = "测试")
public class TestClearUserCacheController extends BaseController{

	@Autowired
	private AuthClientService authClientService;
	
	/**
	 * 
	 * 测试
	 * 
	 * @param mobile
	 * @param token
	 * @return
	 * 
	 */
	@RequestMapping(value = "/admin", method = RequestMethod.GET)
	public Map<String, Object> testAdmin(
			@RequestHeader("${jwt.header}") String token){
		String test = authClientService.testAdmin();
		System.out.println("apis/admin 返回数据=" + test);
		return null;
	}
	
	/**
	 * 清空机构所有用户缓存信息
	 * 
	 * @param tenantId
	 * @return
	 */
	@RequestMapping(value = "/module/mod/{tenantId}", method = RequestMethod.POST)
	public Map<String,Object> removeUserDetailByTenant(@PathVariable(value = "tenantId", required = true) Long tenantId,
			@RequestHeader("${jwt.header}") String token){
		Map<String,Object> map = authClientService.removeUserDetailByTenant(tenantId, token);
		return map;
	}
	
	/**
	 * 
	 * 清空用户缓存(用户信息)
	 * 
	 * @param mobile
	 * @param token
	 * @return
	 * 
	 */
	@RequestMapping(value = "/permission/delbymobile/{mobile}", method = RequestMethod.POST)
	public Map<String,Object> removePermsBymobile(@PathVariable(value = "mobile", required = true) String mobile,
			@RequestHeader("${jwt.header}") String token){
		Map<String,Object> map = authClientService.removePermsBymobile(mobile, token);
		return map;
	}
	
	/**
	 * 修改角色调用接口 修改角色下用户缓存信息
	 * 
	 * @param roleId
	 * @param token
	 * @return
	 * 
	 */
	@RequestMapping(value = "/permission/delbyrole/{roleId}", method = RequestMethod.POST)
	public Map<String, Object> removePerms(@PathVariable(value = "roleId", required = true) Long roleId,
			@RequestHeader("${jwt.header}") String token){
		Map<String,Object> map = authClientService.removePerms(roleId, token);
		return map;
	}

}
