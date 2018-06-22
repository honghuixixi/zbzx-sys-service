package com.aek.ebey.sys.web.controller.invoking;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aek.common.core.Result;
import com.aek.common.core.base.BaseController;
import com.aek.ebey.sys.enums.TenantType;
import com.aek.ebey.sys.mapper.SysTenantMapper;
import com.aek.ebey.sys.model.SysTenant;
import com.aek.ebey.sys.model.Tenants;
import com.aek.ebey.sys.service.SysTenantService;
import com.alibaba.fastjson.JSON;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;

/**
 * 部门提供服务调用
 * 
 * @author Mr.han
 *
 */
@RestController
@RequestMapping(value = "/zbzxsys/invoke/tenant")
@Api(value = "InvokeTenantController", description = "租户服务调用")
public class InvokeTenantController extends BaseController {

	@Autowired
	private SysTenantService tenantService;
	
	@Autowired
	private SysTenantMapper sysTenantMapper;

	@PreAuthorize("isAuthenticated()")
	@GetMapping(value = "/getTenants")
	@ApiOperation(value = "查询机构id与name")
	@ApiImplicitParam(name = "tenantIds", value = "机构IDs",required = true)
	@ApiResponse(code = 200, message = "OK", response = Result.class)
	public Result<List<Tenants>> getTenants(@RequestParam(value="tenantIds")Long[] tenantIds) {
		List<Tenants> tenants = sysTenantMapper.getTenants(tenantIds);
		System.out.println(JSON.toJSONString(tenants));
		return response(tenants);
	}
	/**
	 * 查询当前机构所有监管机构ID，包含子集
	 * 
	 * @param id
	 *            当前监管机构ID
	 * @return
	 */
	@PreAuthorize("isAuthenticated()")
	@GetMapping(value = "/{id}/hplTenantIds")
	@ApiOperation(value = "查询监管机构所监管的医疗机构（递归子集）")
	@ApiImplicitParam(name = "id", value = "当前机构ID", paramType = "path", required = true)
	@ApiResponse(code = 200, message = "OK", response = Result.class)
	public Result<List<Long>> findByDeptIds(@PathVariable(value = "id", required = true) Long id) {
		SysTenant sysTenant = tenantService.selectById(id);
		if(null != sysTenant && TenantType.SUPERVISE.getNumber().equals(sysTenant.getTenantType())){
			List<Long> manageTenantIds = this.tenantService.findAllSubHplTenantIds(id);
			return response(manageTenantIds);
		} else {
			return response(new ArrayList<Long>());
		}
	}
	
	/**
	 * 查询平台所有医疗机构
	 * @return
	 */
	@PreAuthorize("isAuthenticated()")
	@GetMapping(value = "/all/hplTenantIds")
	@ApiOperation(value = "查询平台所有医疗机构")
	@ApiResponse(code = 200, message = "OK", response = Result.class)
	public Result<List<Long>> findAllHplTenant() {
		List<Long> allHplTenantIds = this.tenantService.findAllHplTenantIds();
		return response(allHplTenantIds);
	}

	
	/**
	 * 查询机构信息
	 * @return
	 */
	@PreAuthorize("isAuthenticated()")
	@GetMapping(value = "/{id}")
	@ApiOperation(value = "查询机构信息")
	@ApiResponse(code = 200, message = "OK", response = Result.class)
	public Result<SysTenant> findTenantById(@PathVariable(value = "id", required = true) Long id) {
		SysTenant sysTenant = this.tenantService.selectById(id);
		return response(sysTenant);
	}
}
