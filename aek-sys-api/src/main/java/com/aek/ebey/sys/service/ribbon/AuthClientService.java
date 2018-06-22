package com.aek.ebey.sys.service.ribbon;

import java.util.Map;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.aek.ebey.sys.service.ribbon.hystrix.AuthClientHystrix;

/**
 * 
 * <一句话功能简述>用户远程调用接口 <功能详细描述>
 * 
 * @author Shuangwf
 * @version 1.0, 2017年5月18日
 */

@FeignClient(value="${feign-zbzx-auth.serviceId}",fallback = AuthClientHystrix.class)
public interface AuthClientService {

	/**
	 * 修改角色调用接口 修改角色下用户缓存信息
	 * 用户角色发生变化，清楚userdetail缓存
	 * @param roleId
	 * @param token
	 * @return
	 *
	 */
	@RequestMapping(value = "/cache/permission/delbyrole/{roleId}", method = RequestMethod.POST)
	public Map<String, Object> removePerms(@PathVariable(value = "roleId", required = true) Long roleId,
										   @RequestHeader("X-AEK56-Token") String token);
			//@RequestHeader("${jwt.header}") String token);


	/**
	 * 测试
	 *
	 * @param roleId
	 * @param token
	 * @return
	 *
	 */
	@RequestMapping(value = "/admin", method = RequestMethod.GET)
	public String testAdmin();

	/**
	 *
	 * 清空用户缓存(用户信息)
	 * 用户角色发生变化，根据手机号清空用户详情缓存
	 * @param mobile
	 * @param token
	 * @return
	 *
	 */
	@RequestMapping(value = "/cache/permission/delbymobile/{mobile}", method = RequestMethod.POST)
	public Map<String, Object> removeUser(@PathVariable(value = "mobile", required = true) String mobile,
			@RequestHeader("X-AEK56-Token") String token);

	/**
	 * 
	 * 用户被禁用、密码被租户管理员修改，根据手机号踢出用户
	 * @param mobile
	 * @param token
	 * @return
	 * 
	 */
	@RequestMapping(value = "/cache/user/delbymobile/{mobile}", method = RequestMethod.POST)
	public Map<String, Object> removePermsBymobile(@PathVariable(value = "mobile", required = true) String mobile,
			@RequestHeader("X-AEK56-Token") String token);
	

	/**
	 * 机构模块发生变化清空当前租户及其子租户的用户缓存
	 * @param tenantId
	 * @return
	 */
	@RequestMapping(value = "/cache/module/mod/{tenantId}", method = RequestMethod.POST)
	public Map<String, Object> removeUserDetailByTenant(
			@PathVariable(value = "tenantId", required = true) Long tenantId,
			@RequestHeader("X-AEK56-Token") String token);
	
	/**
	 * 机构被停用/删除，踢出该租户及子租户下的所有用户
	 * @param tenantId
	 * @return
	 */
	@RequestMapping(value = "/cache/user/delbytenant/{tenantId}",method = RequestMethod.POST)
	public Map<String, Object> removePermsByTenant(
			@PathVariable(value = "tenantId", required = true) Long tenantId,
			@RequestHeader("X-AEK56-Token") String token);
	
}
