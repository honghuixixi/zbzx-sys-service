package com.aek.ebey.sys.web.controller;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aek.common.core.BeanMapper;
import com.aek.common.core.Result;
import com.aek.common.core.base.BaseController;
import com.aek.common.core.exception.ExceptionFactory;
import com.aek.common.core.serurity.WebSecurityUtils;
import com.aek.common.core.serurity.model.AuthUser;
import com.aek.ebey.sys.core.SysConstants;
import com.aek.ebey.sys.enums.TenantType;
import com.aek.ebey.sys.model.SysSupplierTenantCredentials;
import com.aek.ebey.sys.model.SysTenant;
import com.aek.ebey.sys.model.SysUser;
import com.aek.ebey.sys.model.custom.SupplierTenant;
import com.aek.ebey.sys.model.query.TenantQuery;
import com.aek.ebey.sys.model.vo.SysTenantAdminVo;
import com.aek.ebey.sys.model.vo.SysTenantUserVo;
import com.aek.ebey.sys.service.SysSupplierTenantCredentialsService;
import com.aek.ebey.sys.service.SysTenantService;
import com.aek.ebey.sys.service.SysUserService;
import com.aek.ebey.sys.web.request.SupplierTenantRequest;
import com.aek.ebey.sys.web.request.SysSupplierTenantCredentialsRequest;
import com.aek.ebey.sys.web.validator.group.Add;
import com.aek.ebey.sys.web.validator.group.Edit;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.plugins.Page;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;

/**
 * 供应商控制器
 *	
 * @author HongHui
 * @date   2017年9月19日
 */
@RestController
@RequestMapping("/zbzxsys/supplier")
@Api(value = "SysSupplierTenantController", description = "供应商管理")
public class SysSupplierTenantController extends BaseController {

	private static final Logger logger = LoggerFactory.getLogger(SysSupplierTenantController.class);
	
	@Autowired
	private SysTenantService sysTenantService;
	
	@Autowired
	private SysUserService sysUserService;
	
	@Autowired
	private SysSupplierTenantCredentialsService sysSupplierTenantCredentialsService;
	
	/**
	 * 创建供应商信息
	 * @return
	 */
	@PreAuthorize("hasAuthority('SYS_SUPPLIER_TENANT_NEW')")
	@PostMapping(value = "/createSupplier")
	@ApiOperation(value = "创建供应商", httpMethod = "POST", produces = "application/json")
	@ApiResponse(code = 0, message = "OK", response = Result.class)
	public Result<SysTenant> createSupplier(@Validated(value = Add.class) @RequestBody SupplierTenantRequest request){
		logger.debug("==创建供应商接口===");
		SysTenant supplierTenant = BeanMapper.map(request, SysTenant.class);
		supplierTenant.setSupplierTenant(request.getSupplierTenant());
		supplierTenant.setSupplierCredentials(request.getSupplierCredentials());
		supplierTenant.setParentId(SysConstants.AEK_TENANT_ID);
		SysUser supplierAdmin = null;
		if(null != request.getCreateAdmin() && request.getCreateAdmin()){
			supplierAdmin = new SysUser();
			supplierAdmin.setRealName(request.getRealName());
			supplierAdmin.setPassword(request.getPassword());
			supplierAdmin.setMobile(request.getMobile());
			supplierAdmin.setEmail(request.getEmail());
		}
		//TODO 创建供应商
		sysTenantService.createSupplierTenant(supplierTenant, supplierAdmin);
		return response(supplierTenant);
	}
	
	/**
	 * 编辑供应商信息
	 * @return
	 */
	@PreAuthorize("hasAuthority('SYS_SUPPLIER_TENANT_MANAGE')")
	@PutMapping(value = "/edit")
	@ApiOperation(value = "编辑供应商", httpMethod = "PUT", produces = "application/json")
	@ApiResponse(code = 0, message = "OK", response = Result.class)
	public Result<SysTenant> editSupplier(@Validated(value = Edit.class) @RequestBody SupplierTenantRequest request) {
		logger.debug("==编辑供应商接口===");
		SysTenant supplierTenant = BeanMapper.map(request, SysTenant.class);
		supplierTenant.setSupplierTenant(request.getSupplierTenant());
		SysUser supplierAdmin = null;
		if((null != request.getCreateAdmin()) && (request.getCreateAdmin())){
			supplierAdmin = new SysUser();
			supplierAdmin.setRealName(request.getRealName());
			supplierAdmin.setPassword(request.getPassword());
			supplierAdmin.setMobile(request.getMobile());
			supplierAdmin.setEmail(request.getEmail());
		}
		//TODO 编辑供应商
		sysTenantService.modifySupplierTenant(supplierTenant, supplierAdmin);
		return response(supplierTenant);
	}
	
	/**
	 * 供应商详情
	 * @return
	 */
	@PreAuthorize("hasAuthority('SYS_SUPPLIER_TENANT_DETAILED_VIEW')")
	@GetMapping(value = "/view/{tenantId}")
	@ApiOperation(value = "根据ID查询供应商信息", httpMethod = "GET", produces = "application/json")
	@ApiResponse(code = 0, message = "OK", response = Result.class)
	@ApiImplicitParam(name = "tenantId", value = "机构ID", paramType = "path", required = true)
	public Result<SysTenantUserVo> querySupplier(@PathVariable("tenantId") Long tenantId) {
		logger.debug("==获取供应商详情接口===");
		SysTenant tenant = this.sysTenantService.selectById(tenantId);
		SysTenantUserVo tvo = new SysTenantUserVo();
		tvo.setTenant(tenant);
		SysUser createUser = this.sysUserService.selectById(tenant.getCreateBy());
		if (createUser != null) {
			tvo.setCreateName(createUser.getRealName());
		}
		if (tenant != null) {
			if (TenantType.SUPPLIER.getNumber().equals(tenant.getTenantType())){
				SupplierTenant supplier = JSON.parseObject(tenant.getCustomData(), SupplierTenant.class);
				tenant.setSupplierTenant(supplier);
			}
			if (tenant.getAdminId() != null) {
				SysUser admin = this.sysUserService.selectById(tenant.getAdminId());
				if(null != admin && !admin.getDelFlag()){
					SysTenantAdminVo tAdmin = BeanMapper.map(admin, SysTenantAdminVo.class);
					tvo.setTenantAdmin(tAdmin);
				}
			}
		}
		return response(tvo);
	}
	
	/**
	 * 查询供应商列表
	 * @return
	 */
	@PreAuthorize("hasAuthority('SYS_SUPPLIER_TENANT_LIST_VIEW')")
	@GetMapping(value = "/list")
	@ApiOperation(value = "分页查询供应商列表", httpMethod = "GET", produces = "application/json")
	@ApiImplicitParams({ 
		@ApiImplicitParam(name = "pageNo", value = "起始页 [默认1]", paramType = "query", required = false),
		@ApiImplicitParam(name = "pageSize", value = "分页大小[默认10]", paramType = "query", required = false),
		@ApiImplicitParam(name = "enable", value = "启用未启用[true=启用,false=未启用]", paramType = "query", required = false),
		@ApiImplicitParam(name = "origin", value = "来源[1后台创建，2前台注册]", paramType = "query", required = false),
		@ApiImplicitParam(name = "auditStatus", value = "审核状态[1待审核,2自动通过,3人工通过,4已拒绝]", paramType = "query", required = false),
		@ApiImplicitParam(name = "serviceType", value = "服务类型[1=维修商，2=供货商，3=配件供应商，4=综合服务商，5=其他]", paramType = "query", required = false),
		@ApiImplicitParam(name = "keyword", value = "关键字", paramType = "query", required = false) })
	@ApiResponse(code = 200, message = "OK", response = Result.class)
	public Result<Page<SysTenant>> findPageByKeyword(TenantQuery query) {
		logger.debug("==获取供应商列表接口===");
		query.setTenantType(TenantType.SUPPLIER.getNumber());
		Page<SysTenant> page = this.sysTenantService.findSupplierPageByKeyword(query);
		AuthUser authUser = WebSecurityUtils.getCurrentUser();
		List<SysTenant> tenants = page.getRecords();
		for (SysTenant tenant : tenants) {
			SupplierTenant supplier = JSON.parseObject(tenant.getCustomData(), SupplierTenant.class);
			tenant.setSupplierTenant(supplier);
		}
		return response(page);
	}
	
	/**
	 * 停用供应商
	 * @return
	 */
	@PreAuthorize("hasAuthority('SYS_SUPPLIER_TENANT_MANAGE')")
	@PostMapping(value = "/stop/{tenantId}")
	@ApiOperation(value = "停用供应商", httpMethod = "POST", produces = "application/json")
	@ApiResponse(code = 200, message = "OK", response = Result.class)
	@ApiImplicitParam(name = "tenantId", value = "机构ID", paramType = "path", required = true)
	public Result<Object> stopTenant(@PathVariable(value = "tenantId", required = true) Long tenantId) {
		logger.debug("==停用供应商接口===");
		this.sysTenantService.stopTenantById(tenantId);
		return response();
	}
	
	/**
	 * 恢复供应商
	 *
	 * @return
	 */
	@PreAuthorize("hasAuthority('SYS_SUPPLIER_TENANT_MANAGE')")
	@PostMapping(value = "/recover/{tenantId}")
	@ApiOperation(value = "恢复供应商", httpMethod = "POST", produces = "application/json")
	@ApiResponse(code = 200, message = "OK", response = Result.class)
	@ApiImplicitParams(value={
			@ApiImplicitParam(name = "tenantId",value = "机构ID",paramType = "path" ,required = true)})
	public Result<Object> recoverTenant(@PathVariable(value = "tenantId", required = true) Long tenantId) {
		this.sysTenantService.recoverTenantById(tenantId,null);
		return response();
	}
	
	/**
	 * 删除供应商
	 * @return
	 */
	@PreAuthorize("hasAuthority('SYS_SUPPLIER_TENANT_MANAGE')")
	@RequestMapping(value = "/delete/{tenantId}")
	@ApiOperation(value = "删除供应商", httpMethod = "DELETE", produces = "application/json")
	@ApiResponse(code = 200, message = "OK", response = Result.class)
	@ApiImplicitParam(name = "tenantId", value = "机构ID", paramType = "path", required = true)
	public Result<Object> deleteTenant(@PathVariable(value = "tenantId", required = true) Long tenantId) {
		logger.debug("==删除供应商接口===");
		SysTenant tenant = this.sysTenantService.selectById(tenantId);
		if (tenant == null) {
			throw ExceptionFactory.create("S_005");
		}
		this.sysTenantService.deleteTenantById(tenantId);
		return response();
	}
	
	
	
	/**
	 * 查询供应商证件
	 * @return
	 */
	@PreAuthorize("isAuthenticated()")
	@GetMapping(value = "/view/credentials/{tenantId}")
	@ApiOperation(value = "根据ID查询供应商证件信息", httpMethod = "GET", produces = "application/json")
	@ApiResponse(code = 0, message = "OK", response = Result.class)
	@ApiImplicitParam(name = "tenantId", value = "机构ID", paramType = "path", required = true)
	public Result<List<SysSupplierTenantCredentials>> findSupplierTenantCredentials(@PathVariable("tenantId") Long tenantId) {
		logger.debug("==获取供应商证件信息接口===");
		List<SysSupplierTenantCredentials> credentialsList = sysSupplierTenantCredentialsService.findSupplierTenantCredentials(tenantId);
		return response(credentialsList);
	}
	
	/**
	 * 新增供应商证件
	 * @return
	 */
	@PreAuthorize("isAuthenticated()")
	@PostMapping(value = "/createCredentials")
	@ApiOperation(value = "新增供应商证件", httpMethod = "POST", produces = "application/json")
	@ApiResponse(code = 0, message = "OK", response = Result.class)
	public Result<SysSupplierTenantCredentials> createCredentials(@Validated(value = Add.class) @RequestBody SysSupplierTenantCredentialsRequest request){
		logger.debug("==新增供应商证件接口===");
		SysSupplierTenantCredentials credentials = BeanMapper.map(request, SysSupplierTenantCredentials.class);
		sysSupplierTenantCredentialsService.insert(credentials);
		return response(credentials);
	}
	
	/**
	 * 编辑供应商证件信息
	 * @return
	 */
	@PreAuthorize("isAuthenticated()")
	@PutMapping(value = "/editCredentials")
	@ApiOperation(value = "编辑供应商证件信息", httpMethod = "PUT", produces = "application/json")
	@ApiResponse(code = 0, message = "OK", response = Result.class)
	public Result<SysSupplierTenantCredentials> editCredentials(@Validated(value = Edit.class) @RequestBody SysSupplierTenantCredentialsRequest request) {
		logger.debug("==编辑供应商证件接口===");
		SysSupplierTenantCredentials credentials = BeanMapper.map(request, SysSupplierTenantCredentials.class);
		sysSupplierTenantCredentialsService.modifySupplierTenantCredentials(credentials);
		return response(credentials);
	}
}
