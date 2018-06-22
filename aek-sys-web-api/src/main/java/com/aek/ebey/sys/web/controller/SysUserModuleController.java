package com.aek.ebey.sys.web.controller;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aek.common.core.BeanMapper;
import com.aek.common.core.Result;
import com.aek.common.core.base.BaseController;
import com.aek.common.core.serurity.WebSecurityUtils;
import com.aek.common.core.serurity.model.AuthUser;
import com.aek.ebey.sys.model.SysUserModule;
import com.aek.ebey.sys.service.SysUserModuleService;
import com.aek.ebey.sys.service.ribbon.AuthClientService;
import com.aek.ebey.sys.web.request.UserModuleRequest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;

/**
 * <p>
 * 用户模块信息  前端控制器
 * </p>
 *
 * @author aek
 * @since 2017-05-27
 */
@RestController
@RequestMapping("/zbzxsys/user_module/sysUserModule")
@Api(value = "SysUserModuleController", description = "用户应用设置保存管理")
public class SysUserModuleController extends BaseController {
	private static final Logger logger = LoggerFactory.getLogger(SysUserModuleController.class);
	
	
	@Autowired
	private SysUserModuleService sysUserModuleService;
	@Autowired
	private AuthClientService authClientService;
	
	/**
	 * 保存个人应用设置
	 * 
	 * @param request
	 * @return
	 */
	@PostMapping(value = "/save_all")
	@ApiOperation(value = "保存用户应用设置", httpMethod = "POST", produces = "application/json")
	@ApiResponse(code = 0, message = "OK", response = Result.class)
	public Result<Object> modifySysUserModuleAll(@RequestBody List<UserModuleRequest> request) {
		List<SysUserModule> sysUserModuleList = BeanMapper.mapList(request, SysUserModule.class);
		this.sysUserModuleService.modifySysUserModuleAll(sysUserModuleList);
		final AuthUser authUser = WebSecurityUtils.getCurrentUser();
		final String token = WebSecurityUtils.getCurrentToken();
		//异步删除使用当前角色的用户权限缓存
		new Thread() {
			@Override
			public void run() {
				logger.info("=================用户[mobile="+authUser.getMobile()+"]保存用户应用设置,清除用户缓存开始=========================");
				Map<String, Object> clearCurrentUserCacheResult = authClientService.removeUser(authUser.getMobile(), token);
				logger.info("=================用户[mobile="+authUser.getMobile()+"]保存用户应用设置，清除用户缓存结果="+clearCurrentUserCacheResult.toString()+"=========================");
			}
		}.start();
		return response();
	}
	
}
