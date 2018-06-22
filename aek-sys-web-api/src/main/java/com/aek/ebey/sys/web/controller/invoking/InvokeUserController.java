package com.aek.ebey.sys.web.controller.invoking;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aek.common.core.BeanMapper;
import com.aek.common.core.Result;
import com.aek.common.core.base.BaseController;
import com.aek.ebey.sys.model.SysPermission;
import com.aek.ebey.sys.model.SysUser;
import com.aek.ebey.sys.model.vo.SysTakeOderUserHelpVo;
import com.aek.ebey.sys.service.SysPermissionService;
import com.aek.ebey.sys.service.SysUserService;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.google.common.collect.Lists;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;

/**
 * 用户提供服务调用
 * 
 * @author Mr.han
 *
 */
@RestController
@RequestMapping(value = "/zbzxsys/invoke/user")
@Api(value = "InvokeUserController", description = "用户服务调用")
public class InvokeUserController extends BaseController {

	@Autowired
	private SysUserService sysUserService;
	
	@Autowired
	private SysPermissionService permissionService;

	/**
	 * 根据用户id和机构id查询用户
	 * 
	 * @param id
	 * @param tenantId
	 * @return
	 */
	@PreAuthorize("isAuthenticated()")
	@GetMapping(value = "/getUser")
	@ApiOperation(value = "根据用户id和机构id查询用户")
	@ApiResponse(code = 200, message = "OK", response = Result.class)
	public Result<SysTakeOderUserHelpVo> getUser(@RequestParam(value = "id", required = true) Long id,@RequestParam(value = "tenantId", required = true) Long tenantId) {
		Wrapper<SysUser> wrapper = new EntityWrapper<>();
		wrapper.eq("id", id).eq("tenant_id", tenantId).eq("enable", 1).eq("del_flag", 0);
		SysUser user = sysUserService.selectOne(wrapper);	
		return response(BeanMapper.map(user, SysTakeOderUserHelpVo.class));
	}
	
	/**
	 * 根据用户ids查询用户列表
	 * 
	 * @param ids
	 * @return
	 */
	@PreAuthorize("isAuthenticated()")
	@GetMapping(value = "/list/{ids}")
	@ApiOperation(value = "根据用户ids查询用户列表")
	@ApiImplicitParam(name = "ids", value = "用户IDs", paramType = "path", required = true)
	@ApiResponse(code = 200, message = "OK", response = Result.class)
	public Result<List<SysUser>> findByIds(@PathVariable(value = "ids", required = true) String ids) {
		List<SysUser> users = this.sysUserService.findByIds(ids);
		return response(users);
	}

	/**
	 * 根据机构ID查询拥有某权限的用户
	 * 
	 * @param ids
	 * @return
	 */
	@PreAuthorize("isAuthenticated()")
	@GetMapping(value = "/tenant/{tenantId}/permission/{permissionCode}")
	@ApiOperation(value = "根据机构ID查询拥有某权限的用户")
	@ApiImplicitParams(value = {
			@ApiImplicitParam(name = "tenantId", value = "机构ID", paramType = "path", required = true),
			@ApiImplicitParam(name = "permissionCode", value = "权限code", paramType = "path", required = true) })
	@ApiResponse(code = 200, message = "OK", response = Result.class)
	public Result<List<Long>> findByTenantAndCanPermiss(
			@PathVariable(value = "tenantId", required = true) String tenantId,
			@PathVariable(value = "permissionCode", required = true) String permissionCode) {
		List<Long> userIds = Lists.newArrayList();
		SysPermission permission = this.permissionService.findByCode(permissionCode);
		if (permission == null) {
			return response(userIds);
		}
		userIds = this.sysUserService.findByTenantAndCanPermiss(tenantId, permission.getId());
		return response(userIds);
	}

}
