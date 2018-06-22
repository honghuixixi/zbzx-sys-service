package com.aek.ebey.sys.service.ribbon.hystrix;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import com.aek.ebey.sys.service.ribbon.AuthClientService;

/**
 * 断路器
 * 
 * @author Administrator
 *
 */
@Component
public class AuthClientHystrix implements AuthClientService {
	
	private static final Logger logger = LogManager.getLogger(AuthClientHystrix.class);

	String url_removePerms="[/cache/permission/delbyrole/{roleId}]";
	String url_removeUser="[/cache/permission/delbymobile/{mobile}]";
	String url_removePermsBymobile="[/cache/user/delbymobile/{mobile}]";
	String url_removeUserDetailByTenant="[/cache/module/mod/{tenantId}]";
	String url_removePermsByTenant="[/cache/user/delbytenant/{tenantId}]";
	@Override
	public Map<String, Object> removePerms(Long roleId, String token) {
		logger.error(
				"service invocation error ------------> service : [auth-server] invocation method : [removePerms] url: " + url_removePerms);
		return null;
	}

	@Override
	public Map<String, Object> removeUser(String mobile, String token) {
		logger.error(
				"service invocation error ------------> service : [auth-server] invocation method : [removeUser] url: " + url_removeUser);
		return null;
	}

	@Override
	public Map<String, Object> removePermsBymobile(String mobile, String token) {
		logger.error(
				"service invocation error ------------> service : [auth-server] invocation method : [removePermsBymobile] url: " + url_removePermsBymobile);
		return null;
	}

	@Override
	public Map<String, Object> removeUserDetailByTenant(Long tenantId, String token) {
		logger.error(
				"service invocation error ------------> service : [auth-server] invocation method : [removeUserDetailByTenant] url: " + url_removeUserDetailByTenant);
		return null;
	}

	@Override
	public String testAdmin() {
		logger.error("service invocation error ------------> service : [auth-server] invocation method : [testAdmin] url: [/apis/admin]");
		return null;
	}

	@Override
	public Map<String, Object> removePermsByTenant(Long tenantId, String token) {
		logger.error(
				"service invocation error ------------> service : [auth-server] invocation method : [removePermsByTenant] url: " + url_removePermsByTenant);
		return null;
	}

}
