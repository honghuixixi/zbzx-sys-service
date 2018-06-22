package com.aek.ebey.sys.web.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aek.common.core.BeanMapper;
import com.aek.common.core.Result;
import com.aek.common.core.base.BaseController;
import com.aek.common.core.serurity.WebSecurityUtils;
import com.aek.common.core.serurity.model.AuthUser;
import com.aek.common.core.sms.RegexValidateUtil;
import com.aek.common.core.sms.SmsResult;
import com.aek.common.core.util.WebUtil;
import com.aek.ebey.sys.core.jackson.annotation.AllowProperty;
import com.aek.ebey.sys.core.jackson.annotation.IgnoreProperties;
import com.aek.ebey.sys.enums.AccountType;
import com.aek.ebey.sys.model.SysUser;
import com.aek.ebey.sys.model.query.UserQuery;
import com.aek.ebey.sys.model.vo.SysPmUserVo;
import com.aek.ebey.sys.model.vo.SysTakeOderUserHelpVo;
import com.aek.ebey.sys.model.vo.SysRepUserVo;
import com.aek.ebey.sys.model.vo.SysUserVo;
import com.aek.ebey.sys.model.vo.WxUserVo;
import com.aek.ebey.sys.service.SysUserService;
import com.aek.ebey.sys.service.ribbon.AuthClientService;
import com.aek.ebey.sys.web.request.UserRequest;
import com.aek.ebey.sys.web.validator.group.Add;
import com.aek.ebey.sys.web.validator.group.Edit;
import com.aek.ebey.sys.web.validator.group.Register;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.google.common.collect.Lists;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;

/**
 * 用户管理
 * 
 * @author HongHui
 * @date 2017年10月9日
 */
@RestController
@RequestMapping("/zbzxsys/user")
@Api(value = "SysUserController", description = "用户管理")
public class SysUserController extends BaseController {

	private static final Logger logger = LogManager.getLogger(SysUserController.class);

	@Autowired
	private SysUserService sysUserService;
	@Autowired
	private AuthClientService authClientService;

	/**
	 * 添加用户
	 * 
	 * @param data
	 * @param request
	 * @return
	 */
	@PreAuthorize("hasAuthority('SYS_USER_NEW')")
	@PostMapping(value = "/add")
	@ApiOperation(value = "添加用户")
	@ApiResponse(code = 200, message = "OK", response = Result.class)
	public Result<Object> add(@Validated(value = Add.class) @RequestBody UserRequest data, HttpServletRequest request) {
		data.setId(null);
		SysUser user = BeanMapper.map(data, SysUser.class);
		user.setRegistrationIp(WebUtil.getHost(request));
		sysUserService.add(user);
		return response();
	}

	/**
	 * 网站用户注册
	 * 
	 * @param data
	 * @return
	 */
	@PostMapping(value = "/register")
	@ApiOperation(value = "注册用户")
	@ApiResponse(code = 200, message = "OK", response = Result.class)
	public Result<Object> register(@Validated(value = Register.class) @RequestBody UserRequest data,
			HttpServletRequest request) {
		SysUser user = BeanMapper.map(data, SysUser.class);
		user.setRegistrationIp(WebUtil.getHost(request));
		sysUserService.register(user);
		return response();
	}

	/**
	 * 网站用户登录
	 * 
	 * @param data
	 * @return
	 */
	@GetMapping(value = "/websiteLogin")
	@ApiOperation(value = "网站用户登录")
	@ApiResponse(code = 200, message = "OK", response = Result.class)
	public Result<SysUser> login(@RequestParam(value = "loginName", required = true) String loginName,
			@RequestParam(value = "password", required = true) String password) {
		return response(sysUserService.login(loginName, password));
	}

	/**
	 * 网站用户id查机构名称
	 * 
	 * @param data
	 * @return
	 */
	@GetMapping(value = "/getTenantName")
	@ApiOperation(value = "网站用户id查机构名称")
	@ApiResponse(code = 200, message = "OK", response = Result.class)
	public Result<String> getTenantName(@RequestParam(value = "userId", required = true) String userId) {
		long id = Long.parseLong(userId);
		SysUser user = sysUserService.selectById(id);
		String tenantName = null;
		if (user != null) {
			tenantName = user.getTenantName();
		}
		return response(tenantName);
	}

	/**
	 * 网站注册登录名即时校验
	 * 
	 * @param loginName
	 *            登录名
	 * @return
	 */
	@GetMapping(value = "/checkLoginName")
	@ApiOperation(value = "登录名即时校验")
	@ApiResponse(code = 200, message = "OK", response = Result.class)
	public Result<Boolean> checkLoginName(@RequestParam(value = "loginName", required = true) String loginName) {
		Boolean result = sysUserService.findByLoginName(loginName) != null ? true : false;
		return response(result);
	}

	/**
	 * 网站注册手机号码即时校验
	 * 
	 * @param loginName
	 *            登录名
	 * @return
	 */
	@GetMapping(value = "/checkMobile")
	@ApiOperation(value = "手机号码即时校验")
	@ApiResponse(code = 200, message = "OK", response = Result.class)
	public Result<Boolean> checkMobile(@RequestParam(value = "mobile", required = true) String mobile) {
		Boolean result = sysUserService.isUserMobileExist(mobile);
		return response(result);
	}

	/**
	 * 网站注册机构机构名称即时校验
	 * 
	 * @param tenantName
	 *            机构名称
	 * @return
	 */
	@GetMapping(value = "/checkTenantName")
	@ApiOperation(value = "手机号码即时校验")
	@ApiResponse(code = 200, message = "OK", response = Result.class)
	public Result<Boolean> checkTenantName(@RequestParam(value = "tenantName", required = true) String tenantName) {
		Boolean result = sysUserService.findByTenantName(tenantName) != null ? true : false;
		return response(result);
	}

	/**
	 * 网站登录登录名存在即时校验
	 * 
	 * @param loginName
	 *            登录名
	 * @return
	 */
	@GetMapping(value = "/isLoginNameExist")
	@ApiOperation(value = "登录名存在即时校验")
	@ApiResponse(code = 200, message = "OK", response = Result.class)
	public Result<Boolean> isLoginNameExist(@RequestParam(value = "loginName", required = true) String loginName) {
		Boolean result = sysUserService.isLoginNameExist(loginName);
		return response(result);
	}

	/**
	 * 修改个人用户信息
	 * 
	 * @param request
	 * @return
	 */
	@PreAuthorize("hasAuthority('SYS_USER_MANAGE')")
	@PostMapping(value = "/edit")
	@ApiOperation(value = "修改用户")
	@ApiResponse(code = 200, message = "OK", response = Result.class)
	public Result<SysUser> edit(@Validated(value = Edit.class) @RequestBody UserRequest request) {
		SysUser user = BeanMapper.map(request, SysUser.class);
		this.sysUserService.edit(user);
		return response(user);
	}
	
	/**
	 * 修改个人用户realName
	 * 
	 * @param request
	 * @return
	 */
	@PreAuthorize("hasAuthority('SYS_USER_MANAGE')")
	@PostMapping(value = "/editRealName")
	@ApiOperation(value = "修改用户realName")
	@ApiResponse(code = 200, message = "OK", response = Result.class)
	public Result<SysUser> editRealName(@Validated(value = Edit.class) @RequestBody UserRequest request) {
		SysUser user = BeanMapper.map(request, SysUser.class);
		this.sysUserService.editRealName(user);
		return response(user);
	}

	/**
	 * 根据ID查询用户信息
	 * 
	 * @param id
	 * @return
	 */
	@PreAuthorize("hasAuthority('SYS_USER_DETAILED_VIEW')")
	@GetMapping(value = "/{id}")
	@ApiOperation(value = "查看用户详细信息")
	@ApiImplicitParam(name = "id", value = "用户ID", paramType = "path", required = true)
	@ApiResponse(code = 200, message = "OK", response = Result.class)
	public Result<SysUserVo> findById(@PathVariable(value = "id", required = true) Long id) {
		SysUserVo userVo = null;
		SysUser sysUser = this.sysUserService.selectById(id);
		if (sysUser != null) {
			userVo = BeanMapper.map(sysUser, SysUserVo.class);
		}
		return response(userVo);
	}

	/**
	 * 修改个人密码
	 * 
	 * @param id
	 * @param password
	 * @return
	 */
	/*
	 * @PreAuthorize("isAuthenticated()")
	 * 
	 * @PostMapping(value = "/onese/changePwd")
	 * 
	 * @ApiOperation(value = "修改用户密码（自己）")
	 * 
	 * @ApiResponse(code = 200, message = "OK", response = Result.class) public
	 * Result<Object> changeOwnPassword(@RequestBody String password) { AuthUser
	 * authUser = WebSecurityUtils.getCurrentUser();
	 * this.sysUserService.changePassword(authUser.getId(), password, true);
	 * return response(); }
	 */

	/**
	 * 修改个人密码(换一种传值方式)
	 * 
	 * @param id
	 * @param password
	 * @return
	 */
	@PreAuthorize("isAuthenticated()")
	@PostMapping(value = "/onese/changePwd")
	@ApiOperation(value = "修改用户密码（自己）")
	@ApiResponse(code = 200, message = "OK", response = Result.class)
	public Result<Object> changeOwnPassword(@RequestBody SysUser user) {
		AuthUser authUser = WebSecurityUtils.getCurrentUser();
		this.sysUserService.changePassword(authUser.getId(), user.getPassword(), true);
		return response();
	}

	/**
	 * 修改其他用户密码
	 * 
	 * @param id
	 * @param password
	 * @return
	 */
	@PreAuthorize("hasAuthority('SYS_USER_MANAGE')")
	@PostMapping(value = "/{id}/changePwd", consumes = "application/json")
	@ApiOperation(value = "修改用户密码（其他用户）")
	@ApiImplicitParam(name = "id", value = "用户ID", paramType = "path", required = true)
	@ApiResponse(code = 200, message = "OK", response = Result.class)
	public Result<Object> changeUserPassword(@PathVariable(required = true, value = "id") Long id,
			@RequestBody SysUser user) {
		this.sysUserService.changePassword(id, user.getPassword(), false);
		return response();
	}

	/**
	 * 用户停用
	 * 
	 * @param id
	 *            用户ID
	 * @return
	 */
	@PreAuthorize("hasAuthority('SYS_USER_MANAGE')")
	@PostMapping(value = "/disable/{id}")
	@ApiOperation(value = "用户停用")
	@ApiImplicitParam(name = "id", value = "用户ID", paramType = "path", required = true)
	@ApiResponse(code = 200, message = "OK", response = Result.class)
	public Result<Object> disableUser(@PathVariable(value = "id", required = true) Long id) {
		SysUser entity = new SysUser();
		entity.setEnable(false);
		entity.setUpdateTime(new Date());
		Wrapper<SysUser> wrapper = new EntityWrapper<SysUser>();
		wrapper.eq("id", id);
		wrapper.eq("del_flag", false);
		wrapper.eq("enable", true);
		boolean flag = this.sysUserService.update(entity, wrapper);
		if (flag) {
			String token = WebSecurityUtils.getCurrentToken();
			new Thread() {
				@Override
				public void run() {
					SysUser user = sysUserService.selectById(id);
					logger.info("=================用户[mobile=" + user.getMobile()
							+ "]被停用,清除用户缓存开始=========================");
					// 机构模块发生变化清空当前租户及其子租户的用户缓存
					Map<String, Object> clearCurrentTenantCacheResult = authClientService
							.removePermsBymobile(user.getMobile(), token);
					logger.info("=================用户[mobile=" + user.getMobile() + "]被停用，清除用户缓存结果="
							+ clearCurrentTenantCacheResult.toString() + "=========================");
				}
			}.start();
			return response();
		} else {
			return responseMsg("U_017");
		}

	}

	/**
	 * 启用用户
	 * 
	 * @param id
	 *            用户ID
	 * @return
	 */
	@PreAuthorize("hasAuthority('SYS_USER_MANAGE')")
	@PostMapping(value = "/recover/{id}")
	@ApiOperation(value = "用户启用")
	@ApiImplicitParam(name = "id", value = "用户ID", paramType = "path", required = true)
	@ApiResponse(code = 200, message = "OK", response = Result.class)
	public Result<Object> recoverUser(@PathVariable(value = "id", required = true) Long id) {
		SysUser entity = new SysUser();
		entity.setEnable(true);
		entity.setUpdateTime(new Date());
		Wrapper<SysUser> wrapper = new EntityWrapper<SysUser>();
		wrapper.eq("id", id);
		wrapper.eq("del_flag", false);
		wrapper.eq("enable", false);
		boolean flag = this.sysUserService.update(entity, wrapper);
		if (flag) {
			return response();
		} else {
			return responseMsg("U_018");
		}
	}

	/**
	 * 批量停用用户
	 * 
	 * @param userIds
	 *            用户ID集合
	 * @return
	 */
	@PreAuthorize("hasAuthority('SYS_USER_MANAGE')")
	@PostMapping(value = "/batch/disable")
	@ApiOperation(value = "用户停用（批量）")
	@ApiResponse(code = 200, message = "OK", response = Result.class)
	public Result<Object> disableBatchUser(@RequestBody List<Long> userIds) {
		this.sysUserService.disableBatchUser(userIds);
		return response();
	}

	/**
	 * 用户启用（批量）
	 * 
	 * @param userIds
	 *            用户ID集合
	 * @return
	 */
	@PreAuthorize("hasAuthority('SYS_USER_MANAGE')")
	@PostMapping(value = "/batch/enable")
	@ApiOperation(value = "用户启用（批量）")
	@ApiResponse(code = 200, message = "OK", response = Result.class)
	public Result<Object> enableBatchUser(@RequestBody List<Long> userIds) {
		this.sysUserService.enableBatchUser(userIds);
		return response();
	}

	/**
	 * 根据ID查询用户信息
	 * 
	 * @param id
	 *            用户ID
	 * @return
	 */
	@PreAuthorize("hasAuthority('SYS_USER_MANAGE')")
	@DeleteMapping(value = "/delete/{id}")
	@ApiOperation(value = "删除用户", httpMethod = "DELETE")
	@ApiImplicitParam(name = "id", value = "用户ID", paramType = "path", required = true)
	@ApiResponse(code = 200, message = "OK", response = Result.class)
	public Result<Object> deleteById(@PathVariable(value = "id", required = true) Long id) {
		SysUser entity = new SysUser();
		entity.setDelFlag(true);
		entity.setUpdateTime(new Date());
		Wrapper<SysUser> wrapper = new EntityWrapper<SysUser>();
		wrapper.eq("id", id);
		wrapper.eq("del_flag", false);
		boolean flag = this.sysUserService.update(entity, wrapper);
		if (flag) {
			return response();
		} else {
			return responseMsg("U_009");
		}
	}

	/**
	 * 批量删除用户
	 * 
	 * @param userIds
	 *            用户ID集合
	 * @return
	 */
	@PreAuthorize("hasAuthority('SYS_USER_MANAGE')")
	@PostMapping(value = "/batch/delete")
	@ApiOperation(value = "用户删除（批量）")
	@ApiResponse(code = 200, message = "OK", response = Result.class)
	public Result<Object> batchDeleteUser(@RequestBody List<Long> userIds) {
		this.sysUserService.deleteBatchUser(userIds);
		return response();
	}

	/**
	 * 查询用户列表(分页)
	 * 
	 * @param query
	 * @return
	 */
	@PreAuthorize("hasAuthority('SYS_USER_DEPT_VIEW')")
	@IgnoreProperties(allow = { @AllowProperty(pojo = SysUser.class, name = { "id", "realName", "mobile", "email",
			"mobile", "deptName", "jobName", "enable", "createTime" }) })
	@GetMapping(value = "/search")
	@ApiOperation(value = "查询用户列表(分页)", httpMethod = "GET", produces = "application/json")
	@ApiImplicitParams({ @ApiImplicitParam(name = "pageNo", value = "起始页 [默认1]", paramType = "query", required = false),
			@ApiImplicitParam(name = "pageSize", value = "分页大小[默认10]", paramType = "query", required = false),
			@ApiImplicitParam(name = "tenantId", value = "租户ID [必须]", paramType = "query", required = true),
			@ApiImplicitParam(name = "enable", value = "启用未启用[true=启用,false=未启用]", paramType = "query", required = false),
			@ApiImplicitParam(name = "deptId", value = "部门ID", paramType = "query", required = false),
			@ApiImplicitParam(name = "keyword", value = "关键字", paramType = "query", required = false) })
	@ApiResponse(code = 200, message = "OK", response = Result.class)
	public Result<Page<SysUser>> search(UserQuery query) {
		Page<SysUser> page = this.sysUserService.search(query);
		return response(page);
	}

	/**
	 * 根据机构ID查询用户列表
	 * 
	 * @param query
	 * @return
	 */
	@PreAuthorize("hasAuthority('SYS_TENANT_MANAGE')")
	@RequestMapping(value = "/users/tenantId", method = RequestMethod.GET)
	@ApiOperation(value = "查询租户下的用户列表", httpMethod = "GET", produces = "application/json")
	@ApiImplicitParams({ @ApiImplicitParam(name = "tenantId", value = "机构ID", paramType = "query", required = true),
			@ApiImplicitParam(name = "pageNo", value = "起始页 [默认1]", paramType = "query", required = false),
			@ApiImplicitParam(name = "pageSize", value = "分页大小[默认8]", paramType = "query", required = false),
			@ApiImplicitParam(name = "enable", value = "用户状态", paramType = "query", required = false), })
	@ApiResponse(code = 200, message = "OK", response = Result.class)
	public Result<Page<SysUser>> findUserByTenantId(UserQuery query) {
		Page<SysUser> page = this.sysUserService.findByTenantId(query);
		return response(page);
	}

	/**
	 * 判断密码是否正确
	 * 
	 * @param password
	 * @param userId
	 * @return
	 */
	@PreAuthorize("isAuthenticated()")
	@GetMapping(value = "/password")
	@ApiOperation(value = "判断密码是否正确", httpMethod = "GET", produces = "application/json")
	@ApiImplicitParams(value = {
			@ApiImplicitParam(name = "password", value = "当前密码", required = true, paramType = "query"),
			@ApiImplicitParam(name = "userId", value = "用户id", required = true, paramType = "query") })
	@ApiResponse(code = 0, message = "OK", response = Result.class)
	public Result<Object> checkPassword(@RequestParam String password, @RequestParam Long userId) {
		return response(this.sysUserService.checkPassword(password, userId));
	}

	/**
	 * 判断用户信息是否重复
	 * 
	 * @param value
	 *            信息(手机号码或者邮箱)
	 * @param type
	 *            1.手机 2.邮箱
	 * @return
	 */
	@PreAuthorize("isAuthenticated()")
	@GetMapping(value = "/info/checkRepeat")
	@ApiOperation(value = "判断用户信息是否重复", httpMethod = "GET", produces = "application/json")
	@ApiImplicitParams(value = {
			@ApiImplicitParam(name = "value", value = "信息(手机号码或者邮箱)", required = true, paramType = "query"),
			@ApiImplicitParam(name = "type", value = "1.手机 2.邮箱", required = true, paramType = "query") })
	@ApiResponse(code = 0, message = "OK", response = Result.class)
	public Result<Object> checkUserInfo(@RequestParam(required = true) String value,
			@RequestParam(required = true) Integer type) {
		if (StringUtils.isBlank(value)) {
			return response(false);
		}
		// 判断手机号码
		if (type == AccountType.MOBILE.getNumber()) {
			if (RegexValidateUtil.checkCellphone(value) && this.sysUserService.findByMobile(value) == null) {
				return response(true);
			}
		}
		// 判断邮箱
		if (type == AccountType.EMAIL.getNumber()) {
			if (RegexValidateUtil.checkEmail(value) && this.sysUserService.findByEmail(value) == null) {
				return response(true);
			}
		}
		return response(false);
	}

	/**
	 * 重置手机号（发送验证码）
	 * 
	 * @param mobile
	 * @return
	 */
	@PreAuthorize("isAuthenticated()")
	@GetMapping(value = "/user/mobile")
	@ApiOperation(value = "重置手机（发送验证码）", httpMethod = "GET", produces = "application/json")
	@ApiImplicitParam(name = "mobile", value = "手机号码", required = true, paramType = "query")
	@ApiResponse(code = 0, message = "OK", response = Result.class)
	public Result<Object> sendCode(@RequestParam String mobile) {
		SmsResult sendCode = this.sysUserService.sendCode(mobile);
		if (sendCode.getSuccess() != 1) {
			return responseMsg("U_022", sendCode.getMsg());
		}
		return response();
	}

	/**
	 * 重置手机号
	 * 
	 * @param mobile
	 *            手机号
	 * @param code
	 *            验证码
	 * @param userId
	 *            用户ID
	 * @return
	 */
	@PreAuthorize("isAuthenticated()")
	@GetMapping(value = "/reset/mobile")
	@ApiOperation(value = "重置手机号", httpMethod = "GET", produces = "application/json")
	@ApiImplicitParams(value = {
			@ApiImplicitParam(name = "mobile", value = "手机号码", required = true, paramType = "query"),
			@ApiImplicitParam(name = "code", value = "验证码", required = true, paramType = "query"),
			@ApiImplicitParam(name = "userId", value = "用户id", required = true, paramType = "query") })
	@ApiResponse(code = 0, message = "OK", response = Result.class)
	public Result<Object> resetMobile(@RequestParam(value = "mobile", required = true) String mobile,
			@RequestParam(value = "code", required = true) String code,
			@RequestParam(value = "userId", required = true) Long userId) {
		return response(sysUserService.resetMobile(mobile, code, userId));
	}

	/**
	 * 绑定修改邮箱发送邮件验证
	 * 
	 * @param id
	 *            用户ID
	 * @param email
	 *            用户邮箱
	 * @return
	 */
	@PreAuthorize("isAuthenticated()")
	@GetMapping(value = "/{id}/modify/email/send")
	@ApiOperation(value = "绑定修改邮箱发送邮件验证", httpMethod = "GET", produces = "application/json")
	@ApiImplicitParams(value = { @ApiImplicitParam(name = "id", value = "用户ID", paramType = "path", required = true),
			@ApiImplicitParam(name = "email", value = "邮件地址", paramType = "query", required = true) })
	@ApiResponse(code = 200, message = "OK", response = Result.class)
	public Result<Object> modifyEmailSend(@PathVariable(value = "id", required = true) Long id,
			@RequestParam(value = "email", required = true) String email) {
		this.sysUserService.modifyEmailSend(email, id);
		return response();
	}

	/**
	 * 绑定修改邮箱
	 * 
	 * @param id
	 *            用户ID
	 * @return
	 */
	@GetMapping(value = "/{id}/modify/email")
	@ApiOperation(value = "绑定修改邮箱", httpMethod = "GET", produces = "application/json")
	@ApiImplicitParam(name = "id", value = "用户ID", paramType = "path", required = true)
	@ApiResponse(code = 200, message = "OK", response = Result.class)
	public Result<Object> modifyEmail(@PathVariable(value = "id", required = true) Long id) {
		return response(this.sysUserService.modifyEmail(id));
	}

	/**
	 * 解绑邮箱
	 * 
	 * @param id
	 *            用户ID
	 * @return
	 */
	@PreAuthorize("isAuthenticated()")
	@GetMapping(value = "/{id}/unbundling/email")
	@ApiOperation(value = "解绑邮箱", httpMethod = "GET", produces = "application/json")
	@ApiImplicitParam(name = "id", value = "用户ID", paramType = "path", required = true)
	@ApiResponse(code = 200, message = "OK", response = Result.class)
	public Result<Object> unbundlingEmail(@PathVariable(value = "id", required = true) Long id) {
		this.sysUserService.unbundlingEmail(id);
		return response();
	}

	/**
	 * 提交邮箱
	 * 
	 * @param id
	 *            用户ID
	 * @param email
	 *            用户邮箱
	 * @return
	 */
	@PreAuthorize("isAuthenticated()")
	@GetMapping(value = "/{id}/submit/email")
	@ApiOperation(value = "提交邮箱", httpMethod = "GET", produces = "application/json")
	@ApiImplicitParams(value = { @ApiImplicitParam(name = "email", value = "邮箱", required = true, paramType = "query"),
			@ApiImplicitParam(name = "id", value = "用户id", required = true, paramType = "path") })
	@ApiResponse(code = 0, message = "OK", response = Result.class)
	public Result<Object> submitEmail(@PathVariable(value = "id", required = true) Long id,
			@RequestParam(value = "email", required = true) String email) {
		return response(this.sysUserService.submitEmail(id, email));
	}

	/**
	 * 根据id集合批量查询用户信息
	 * 
	 * @param userIds
	 * @return
	 */
	@PreAuthorize("hasAuthority('SYS_USER_MANAGE')")
	@PostMapping(value = "/findByIds", produces = "application/json;charset=UTF-8")
	@ApiOperation(value = "根据id集合批量查询用户信息")
	@ApiResponse(code = 200, message = "OK", response = Result.class)
	public Result<List<SysUser>> findUserByIds(@RequestBody Long[] ids) {
		List<SysUser> list = this.sysUserService.findByIds(ids);
		return response(list);
	}

	/**
	 * 查询机构用户数量(包含子下级机构)
	 * 
	 * @param tenantId
	 *            机构ID
	 * @return
	 */
	@PreAuthorize("isAuthenticated()")
	@GetMapping(value = "/count/tenant/{tenantId}", produces = "application/json;charset=UTF-8")
	@ApiOperation(value = "查询机用户数量(包含子下级机构)")
	@ApiResponse(code = 200, message = "OK", response = Result.class)
	public Result<Integer> findUserCount(@PathVariable(value = "tenantId") Long tenantId) {
		int userCount = this.sysUserService.findUserCountByTenantId(tenantId);
		return response(userCount);
	}
	
	@PreAuthorize("isAuthenticated()")
	@ApiOperation(value = "根据机构ID获取本机构具有PM实施权限的用户列表", httpMethod = "GET")
	@GetMapping("/getPmUserList")
	public Result<List<SysPmUserVo>> getPmUserList() {
		Long tenantId = WebSecurityUtils.getCurrentUser().getTenantId();
		logger.debug(">>>>>根据机构ID获取本机构具有PM实施权限的用户列表<<<<<<");
		logger.debug("查询参数：tenantId = " + tenantId);
		List<SysUser> sysUserList = sysUserService.findUserListByTenantId(tenantId);
		List<SysUser> hasPermissionUserList = Lists.newArrayList();
		for (SysUser sysUser : sysUserList) {
			boolean hasPermission = sysUserService.getUserPermission(sysUser.getId(), tenantId, "PM_PLAN_IMPLEMENT");
			if(hasPermission){
				hasPermissionUserList.add(sysUser);
			}
		}
		List<SysPmUserVo> list = BeanMapper.mapList(hasPermissionUserList, SysPmUserVo.class);
		return response(list);
	}

	@PreAuthorize("isAuthenticated()")
	@ApiOperation(value="根据机构Id获取本机构具有Repair权限的用户列表",httpMethod="GET")
	@GetMapping("/getRepUserList")
	public Result<List<SysRepUserVo>> getRepUserList(){
		Long tenantId = WebSecurityUtils.getCurrentUser().getTenantId();
		List<SysUser> sysUserList = sysUserService.findUserListByTenantId(tenantId);
		List<SysUser> hasPermissionUserList = Lists.newArrayList();
		for (SysUser sysUser : sysUserList) {
			boolean hasPermission = sysUserService.getUserPermission(sysUser.getId(), tenantId, "REP_APPLY_REPAIR");
			if(hasPermission){
				hasPermissionUserList.add(sysUser);
			}
		}
		List<SysRepUserVo> list = BeanMapper.mapList(hasPermissionUserList, SysRepUserVo.class);
		return response(list);
	}
	
	@PreAuthorize("isAuthenticated()")
	@ApiOperation(value="根据机构Id获取本机构具有接单权限的用户列表",httpMethod="GET")
	@GetMapping("/getTakeOrderUserList")
	public Result<List<SysTakeOderUserHelpVo>> getRepUserListHelp(){
		Long tenantId = WebSecurityUtils.getCurrentUser().getTenantId();
		List<SysUser> sysUserList = sysUserService.findUserListByTenantId(tenantId);
		List<SysUser> hasPermissionUserList = Lists.newArrayList();
		for (SysUser sysUser : sysUserList) {
			boolean hasPermission = sysUserService.getUserPermission(sysUser.getId(), tenantId, "REP_APPLY_TAKE_NEW");
			if(hasPermission){
				hasPermissionUserList.add(sysUser);
			}
		}
		//丰富原方法
		List<SysTakeOderUserHelpVo> list = BeanMapper.mapList(hasPermissionUserList, SysTakeOderUserHelpVo.class);
		return response(list);
	}
	
	@PreAuthorize("isAuthenticated()")
	@ApiOperation(value="转单时根据机构Id获取本机构具有Repair权限的用户列表并且过滤掉自己(转单不可以转给自己)",httpMethod="GET")
	@GetMapping("/getRepExchangeUserList")
	public Result<List<SysRepUserVo>> getRepExchangeUserList(){
		AuthUser currentUser = WebSecurityUtils.getCurrentUser();
		Long tenantId = currentUser.getTenantId();
		List<SysUser> sysUserList = sysUserService.findUserListByTenantId(tenantId);
		List<SysUser> userList = new ArrayList<SysUser>();
		for (SysUser sysUser : sysUserList) {
			if(sysUser.getId().longValue()!=currentUser.getId().longValue()){
				userList.add(sysUser);
			}
		}	
		List<SysUser> hasPermissionUserList = Lists.newArrayList();
		for (SysUser sysUser : userList) {
			boolean hasPermission = sysUserService.getUserPermission(sysUser.getId(), tenantId, "REP_APPLY_REPAIR");
			if(hasPermission){
				hasPermissionUserList.add(sysUser);
			}
		}
		List<SysRepUserVo> list = BeanMapper.mapList(hasPermissionUserList, SysRepUserVo.class);
		return response(list);
	}
	
	@PreAuthorize("isAuthenticated()")
	@ApiOperation(value="校验用户是否具有Repair权限",httpMethod="GET")
	@GetMapping("/checkIsRep")
	public Result<Integer> checkIsRep(@RequestParam("id") Long id){
		Boolean flag = false;
		SysUser user = sysUserService.selectById(id);
		if(user !=null){
			Long tenantId = user.getTenantId();
			flag = sysUserService.getUserPermission(id, tenantId, "REP_APPLY_REPAIR");
			if(flag==false){
				return response(1);//无权限
			}
			Boolean userFlag = sysUserService.getUserEnableAndNotDel(id);
			if(userFlag==false){
				return response(2);//已停用
			}
			if(flag==true&&userFlag==true){
				return response(3);//通过
			}
		}
		return response(null);
	}
	
	@PreAuthorize("isAuthenticated()")
	@ApiOperation(value="校验用户是否具有接单权限或停用",httpMethod="GET")
	@GetMapping("/checkIsRepHelp")
	public Result<Integer> checkIsRepHelp(@RequestParam("id") Long id){
		Boolean flag = false;
		SysUser user = sysUserService.selectById(id);
		if(user !=null){
			Long tenantId = user.getTenantId();
			flag = sysUserService.getUserPermission(id, tenantId, "REP_APPLY_TAKE_NEW");
			if(flag==false){
				return response(1);//无权限
			}
			Boolean userFlag = sysUserService.getUserEnableAndNotDel(id);
			if(userFlag==false){
				return response(2);//已停用
			}
			if(flag==true&&userFlag==true){
				return response(3);//通过
			}
		}
		return response(null);
	}
	
	@PreAuthorize("isAuthenticated()")
	@ApiOperation(value="校验用户是否具有单据审批权限或停用",httpMethod="GET")
	@GetMapping("/checkIsRepBill")
	public Result<Integer> checkIsRepBill(@RequestParam("id") Long id){
		Boolean flag = false;
		SysUser user = sysUserService.selectById(id);
		if(user !=null){
			Long tenantId = user.getTenantId();
			flag = sysUserService.getUserPermission(id, tenantId, "REP_REPAIR_PRICE_CHECK");
			if(flag==false){
				return response(1);//无权限
			}
			Boolean userFlag = sysUserService.getUserEnableAndNotDel(id);
			if(userFlag==false){
				return response(2);//已停用
			}
			if(flag==true&&userFlag==true){
				return response(3);//通过
			}
		}
		return response(null);
	}
	
	@PreAuthorize("isAuthenticated()")
	@ApiOperation(value="根据机构Id获取本机构具有维修审批权限的用户列表",httpMethod="GET")	
	@ApiImplicitParam(name = "keyword", value = "搜索关键字", required = false, paramType = "query")
	@GetMapping("/getcheckUserList")
	public Result<List<SysRepUserVo>> getcheckUserList(@RequestParam("keyword")String keyword){
		List<SysUser> checkUserList = sysUserService.getcheckUserList(keyword);
		List<SysUser> hasPermissionUserList = Lists.newArrayList();
		Long tenantId = WebSecurityUtils.getCurrentUser().getTenantId();
		for (SysUser sysUser : checkUserList) {
			boolean hasPermission = sysUserService.getUserPermission(sysUser.getId(), tenantId, "REP_REPAIR_PRICE_CHECK");
			if(hasPermission){
				hasPermissionUserList.add(sysUser);
			}
		}
		List<SysRepUserVo> list = BeanMapper.mapList(hasPermissionUserList, SysRepUserVo.class);
		return response(list);
	}
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/getWxUser")
	@ApiOperation(value="微信用户根据用户openid获取用户信息",httpMethod="GET")
	@ApiImplicitParam(name = "openId", value = "用户openId", required = true)
	@ApiResponse(code = 200, message = "OK", response = Result.class)
	public Result<WxUserVo> getWxUser(@RequestParam("openId")String openId){	
		return response(sysUserService.getWxUser(openId));
	}
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/getUserByMobile")
	@ApiOperation(value="根据用户mobile获取用户信息",httpMethod="GET")
	@ApiImplicitParams(value = { @ApiImplicitParam(name = "mobile", value = "手机号码", required = true, paramType = "query"),
			@ApiImplicitParam(name = "code", value = "验证码", required = true, paramType = "query") })
	@ApiResponse(code = 200, message = "OK", response = Result.class)
	public Result<List<SysUser>> getUserByMobile(@RequestParam("mobile")String mobile,@RequestParam("code")String code){	
		return response(sysUserService.getUserByMobile(mobile,code));
	}
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/changeLoginName")
	@ApiImplicitParams(value = { @ApiImplicitParam(name = "loginName", value = "登录名", required = true, paramType = "query"),
			@ApiImplicitParam(name = "userId", value = "用户id", required = true, paramType = "query") })
	@ApiResponse(code = 200, message = "OK", response = Result.class)
	public Result<Object> changeLoginName(@RequestParam("loginName")String changeLoginName,@RequestParam("userId")Long userId){	
		sysUserService.changeLoginName(changeLoginName,userId);
		return response();
	}
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/resetPwd")
	@ApiImplicitParam(name = "userId", value = "用户id", required = true, paramType = "query")
	@ApiResponse(code = 200, message = "OK", response = Result.class)
	public Result<Object> resetPwd(@RequestParam("userId")Long userId){	
		String pwd="11111111";
		sysUserService.resetPwd(pwd,userId);
		return response();
	}
}
