package com.aek.ebey.sys.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aek.common.core.BeanMapper;
import com.aek.common.core.Result;
import com.aek.common.core.base.BaseController;
import com.aek.common.core.serurity.WebSecurityUtils;
import com.aek.ebey.sys.model.SysDept;
import com.aek.ebey.sys.model.SysUser;
import com.aek.ebey.sys.model.query.DeptQuery;
import com.aek.ebey.sys.model.query.QcUserDeptQuery;
import com.aek.ebey.sys.model.vo.SysPmUserVo;
import com.aek.ebey.sys.model.vo.SysQcDeptUserVo;
import com.aek.ebey.sys.model.vo.SysQcDeptVo;
import com.aek.ebey.sys.model.vo.SysQcUserVo;
import com.aek.ebey.sys.service.SysDeptService;
import com.aek.ebey.sys.service.SysUserService;
import com.google.common.collect.Lists;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * REST API
 */
@RestController
@Api(value = "restAPIController", description = "REST API")
@RequestMapping(value = "/zbzxsys/restAPI")
public class RestAPIController extends BaseController {

	@Autowired
	private SysDeptService sysDeptService;
	@Autowired
	private SysUserService sysUserService;
	
	@PreAuthorize("isAuthenticated()")
	@ApiOperation(value = "根据机构ID及关键字查询巡检科室列表", httpMethod = "GET")
	@GetMapping("getQcDeptList")
	@ApiImplicitParams({
		@ApiImplicitParam(name="tenantId",value="机构ID",paramType="query",required=true),
		@ApiImplicitParam(name="keyword",value="关键字",paramType="query",required=false)}
	)
	public Result<List<SysQcDeptVo>> getQcDeptList(@RequestParam(value="tenantId", required=true)Long tenantId,@RequestParam(value="keyword", required=false)String keyword) {
		logger.debug(">>>>>根据机构ID及关键字查询巡检科室列表<<<<<<");
		logger.debug("查询参数：tenantId = " + tenantId + ",keyword = " + keyword);
		DeptQuery query = new DeptQuery();
		query.setTenantId(tenantId);
		query.setKeyword(keyword);
		List<SysDept> sysDeptList = sysDeptService.findByKeyword(query);
		List<SysQcDeptVo> list = BeanMapper.mapList(sysDeptList, SysQcDeptVo.class);
		return response(list);
	}
	
	@PreAuthorize("isAuthenticated()")
	@ApiOperation(value = "根据机构ID获取本机构具有巡检实施权限的用户列表", httpMethod = "GET")
	@GetMapping("getQcUserList")
	public Result<List<SysQcUserVo>> getQcUserList() {
		Long tenantId = WebSecurityUtils.getCurrentUser().getTenantId();
		logger.debug(">>>>>根据机构ID获取本机构具有巡检实施权限的用户列表<<<<<<");
		logger.debug("查询参数：tenantId = " + tenantId);
		List<SysUser> sysUserList = sysUserService.findUserListByTenantId(tenantId);
		List<SysUser> hasPermissionUserList = Lists.newArrayList();
		for (SysUser sysUser : sysUserList) {
			boolean hasPermission = sysUserService.getUserPermission(sysUser.getId(), tenantId, "QC_PLAN_IMPLEMENT");
			if(hasPermission){
				hasPermissionUserList.add(sysUser);
			}
		}
		List<SysQcUserVo> list = BeanMapper.mapList(hasPermissionUserList, SysQcUserVo.class);
		return response(list);
	}
	
	@PreAuthorize("isAuthenticated()")
	@ApiOperation(value = "根据巡检用户及巡检部门判断能否创建巡检计划", httpMethod = "POST")
	@PostMapping("verifyCreateQcPlan")
	public Result<SysQcDeptUserVo> verifyCreateQcPlan(@RequestBody QcUserDeptQuery qcUserDept){
		logger.debug(">>>>>根据巡检用户及巡检部门判断能否创建巡检计划<<<<<<");
		logger.debug("查询参数： " + qcUserDept.toString());
		Long tenantId = WebSecurityUtils.getCurrentUser().getTenantId();
		SysQcDeptUserVo qcDeptUser = new SysQcDeptUserVo(true,"验证通过");
		//验证科室
		List<SysQcDeptVo> depts = qcUserDept.getQcDepts();
		for (SysQcDeptVo sysQcDeptVo : depts) {
			SysDept dept = sysDeptService.selectById(sysQcDeptVo.getId());
			if(null == dept || (null != dept && (dept.isDelFlag() || !dept.isEnable()))){
				qcDeptUser.setStatus(false);
				qcDeptUser.setTips("您选择的科室\""+dept.getName()+"\"被删除或禁用，请刷新后重新选择。");
				return response(qcDeptUser);
			}
			if(!dept.getName().equals(sysQcDeptVo.getName())){
				qcDeptUser.setStatus(false);
				qcDeptUser.setTips( "您选择的科室\""+sysQcDeptVo.getName()+"\"被修改，请刷新后重新选择。");
				return response(qcDeptUser);
			}
		}
		
		//验证责任人
		Long userId = qcUserDept.getUserId();
		String userName = qcUserDept.getUserName();
		SysUser sysUser = sysUserService.selectById(userId);
		if(null == sysUser || (null != sysUser && (sysUser.getDelFlag() || !sysUser.getEnable()))){
			qcDeptUser.setStatus(false);
			qcDeptUser.setTips( "您选择的责任人\""+userName+"\"被删除或禁用，请重新选择。");
			return response(qcDeptUser);
		}
		boolean hasPermission = sysUserService.getUserPermission(userId, tenantId, "QC_PLAN_IMPLEMENT");
		if(!hasPermission){
			qcDeptUser.setStatus(false);
			qcDeptUser.setTips( "您选择的责任人\""+userName+"\"没有巡检实施权限，请重新选择。");
			return response(qcDeptUser);
		}
		return response(qcDeptUser);
	}

}
