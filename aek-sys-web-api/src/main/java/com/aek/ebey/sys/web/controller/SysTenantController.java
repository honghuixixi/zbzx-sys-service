package com.aek.ebey.sys.web.controller;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
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
import com.aek.common.core.util.RemindDateUtils;
import com.aek.ebey.sys.core.SysConstants;
import com.aek.ebey.sys.core.jackson.annotation.AllowProperty;
import com.aek.ebey.sys.core.jackson.annotation.IgnoreProperties;
import com.aek.ebey.sys.enums.TenantType;
import com.aek.ebey.sys.model.SysSupplierTenant;
import com.aek.ebey.sys.model.SysTenant;
import com.aek.ebey.sys.model.SysUser;
import com.aek.ebey.sys.model.bo.VerifyCode;
import com.aek.ebey.sys.model.custom.HplTenant;
import com.aek.ebey.sys.model.custom.SuperviseTenant;
import com.aek.ebey.sys.model.custom.SupplierTenant;
import com.aek.ebey.sys.model.query.TenantQuery;
import com.aek.ebey.sys.model.vo.CmsTenantQuery;
import com.aek.ebey.sys.model.vo.SysTenantAdminVo;
import com.aek.ebey.sys.model.vo.SysTenantCensusVo;
import com.aek.ebey.sys.model.vo.SysTenantUserVo;
import com.aek.ebey.sys.model.vo.WxTenantVo;
import com.aek.ebey.sys.service.SysTenantService;
import com.aek.ebey.sys.service.SysUserService;
import com.aek.ebey.sys.web.request.CensusRequest;
import com.aek.ebey.sys.web.request.HplTenantRequest;
import com.aek.ebey.sys.web.request.SupplierTenantRequest;
import com.aek.ebey.sys.web.request.TenantRequest;
import com.aek.ebey.sys.web.validator.group.Add;
import com.aek.ebey.sys.web.validator.group.Edit;
import com.aek.ebey.sys.web.validator.group.GET;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.plugins.Page;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;


/**
 * <p>
 * 机构 前端控制器
 * </p>
 *
 * @author aek
 * @since 2017-05-06
 */
@RestController
@RequestMapping("/zbzxsys/tenant")
@Api(value = "SysTenantController", description = "租户管理")
public class SysTenantController extends BaseController {

	@Autowired
	private SysTenantService tenantService;

	@Autowired
	private SysUserService userService;

	/**
	 * 创建子机构
	 * 
	 * @param request
	 *            子机构信息(包含管理员信息)
	 * @return
	 */
	@PreAuthorize("hasAuthority('SYS_HPL_TENANT_NEW','SYS_SUPERVISE_TENANT_NEW')")
	@PostMapping(value = "/")
	@ApiOperation(value = "创建机构", httpMethod = "POST", produces = "application/json")
	@ApiResponse(code = 0, message = "OK", response = Result.class)
	public Result<SysTenant> createSubTenant(@Validated(value = Add.class) @RequestBody TenantRequest request) {
		SysUser admin = null;
		SysTenant tenant = null;

		if (request.getCreateAdmin()) {
			admin = new SysUser(request.getRealName(), request.getPassword(), request.getMobile(),request.getEmail());
		}
		tenant = BeanMapper.map(request, SysTenant.class);
		this.disposeCustomData(request, tenant);
		this.tenantService.createSubTenant(tenant, admin);
		return response(tenant);
	}
	
	/**
	 * 创建申请试用子机构
	 * 
	 * @param request
	 * 			子机构信息(包含管理员信息)
	 * @return
	 */
	//@PreAuthorize("hasAuthority('SYS_HPL_TENANT_NEW','SYS_SUPERVISE_TENANT_NEW')")
	@PostMapping(value = "/createTrialSubTenant")
	@ApiOperation(value = "创建申请试用子机构", httpMethod = "POST", produces = "application/json")
	@ApiResponse(code = 0, message = "OK", response = Result.class)
	public Result<SysTenant> createTrialSubTenant(@Validated(value = Add.class) @RequestBody TenantRequest request){
		SysUser admin = null;
		SysTenant tenant = null;

		if (request.getCreateAdmin()) {
			admin = new SysUser(request.getRealName(),request.getPassword(), request.getMobile(),request.getEmail());
		}
		
		VerifyCode verifyCode = new VerifyCode();
		verifyCode.setCode(request.getVerifyCode());
		tenant = BeanMapper.map(request, SysTenant.class);
		this.disposeCustomData(request, tenant);
		this.tenantService.createTrialSubTenant(tenant, admin,verifyCode);
		return response(tenant);
	};
	
	/**
	 * 创建申请试用子机构(浙江装备中心)
	 * 
	 * @param request
	 * 			子机构信息(包含管理员信息)
	 * @return
	 */
	//@PreAuthorize("hasAuthority('SYS_HPL_TENANT_NEW','SYS_SUPERVISE_TENANT_NEW')")
	@PostMapping(value = "/createTrialSubTenantForZjzx")
	@ApiOperation(value = "创建申请试用子机构", httpMethod = "POST", produces = "application/json")
	@ApiResponse(code = 0, message = "OK", response = Result.class)
	public Result<SysTenant> createTrialSubTenantForZjzx(@Validated(value = Add.class) @RequestBody TenantRequest request){
		SysUser admin = null;
		SysTenant tenant = null;

		if (request.getCreateAdmin()) {
			admin = new SysUser(request.getRealName(), request.getLoginName(),request.getPassword(), request.getMobile(),request.getEmail());
		}
		
		VerifyCode verifyCode = new VerifyCode();
		verifyCode.setCode(request.getVerifyCode());
		tenant = BeanMapper.map(request, SysTenant.class);
		this.disposeCustomDataForZjzx(request, tenant);
		this.tenantService.createTrialSubTenantForZjzx(tenant, admin,verifyCode);
		return response(tenant);
	};
	
	/**
	 * 不同类型租户字段不同处理
	 * 
	 * @param request 请求租户属性
	 * @param tenant  数据库租户属性
	 */
	private void disposeCustomDataForZjzx(TenantRequest request, SysTenant tenant) {
		Integer tenantType = request.getTenantType();
		//医疗机构
		if (TenantType.HOSPITAL.getNumber().equals(tenantType)) {
			HplTenant hplTenant = new HplTenant();
			HplTenantRequest hplTenantRequest = request.getHplTenant();
			hplTenant.setCategory(hplTenantRequest.getCategory());
			hplTenant.setEconomicType(hplTenantRequest.getEconomicType());
			hplTenant.setGrade(hplTenantRequest.getGrade());
			hplTenant.setHierarchy(hplTenantRequest.getHierarchy());
			hplTenant.setManageType(hplTenantRequest.getManageType());
			hplTenant.setOrgCode(hplTenantRequest.getOrgCode());
			hplTenant.setFrom("zjzb");
			tenant.setHplTenant(hplTenant);
		}
	}
		
	/**
	 * 不同类型租户字段不同处理
	 * 
	 * @param request 请求租户属性
	 * @param tenant  数据库租户属性
	 */
	private void disposeCustomData(TenantRequest request, SysTenant tenant) {
		Integer tenantType = request.getTenantType();
		//判断机构类型
		//医疗机构
		if (TenantType.HOSPITAL.getNumber().equals(tenantType)) {
			HplTenant hplTenant = new HplTenant();
			HplTenantRequest hplTenantRequest = request.getHplTenant();
			hplTenant.setCategory(hplTenantRequest.getCategory());
			hplTenant.setEconomicType(hplTenantRequest.getEconomicType());
			hplTenant.setGrade(hplTenantRequest.getGrade());
			hplTenant.setHierarchy(hplTenantRequest.getHierarchy());
			hplTenant.setManageType(hplTenantRequest.getManageType());
			hplTenant.setOrgCode(hplTenantRequest.getOrgCode());
			tenant.setHplTenant(hplTenant);
		}
		//监管机构
		if (TenantType.SUPERVISE.getNumber().equals(tenantType)) {
			// 监管机构字段处理
			SuperviseTenant superviseTenant = new SuperviseTenant();
			tenant.setSuperviseTenant(superviseTenant);
		}
	}
	
	/**
	 * 操作指定机构
	 * 
	 * @param tenantId 机构id
	 * @param operation 通过/拒绝操作
	 * @return
	 */
	@PreAuthorize("hasAuthority('SYS_SUPERVISE_TENANT_MANAGE','SYS_HPL_TENANT_MANAGE','SYS_SUPPLIER_TENANT_VERIFY')")
	@PostMapping(value = "/operate/{tenantId}/{operation}")
	@ApiOperation(value = "操作指定机构", httpMethod = "POST", produces = "application/json")
	@ApiResponse(code = 200, message = "OK", response = Result.class)
	@ApiImplicitParams(value={
			@ApiImplicitParam(name = "tenantId",value = "机构ID",paramType = "path" ,required = true),
			@ApiImplicitParam(name = "operation",value = "机构操作",paramType = "path",required = true)})
	public Result<Object> operateTenant(@PathVariable("tenantId") Long tenantId,@PathVariable("operation") Integer operation){
		//操作指定机构
		this.tenantService.operateTenant(tenantId, operation);
		return response();
	};
	
	/**
	 * 查询指定监管机构下的医疗机构列表
	 * 
	 * @param query
	 * @return
	 */
	@PreAuthorize("isAuthenticated()")
	@IgnoreProperties(allow = { @AllowProperty(pojo = SysTenant.class, name = { "id", "name", "tenantType",
			"commercialUse", "province", "city", "county", "trial", "origin", "auditStatus", "subTenantLimit",
			"subTenant", "expireTime", "createTime","manageTenantId","tenantCatogory","deviceCount","adminName","adminMobile", "enable", "manageTenantName" }) })
	@GetMapping(value = "/manageTreeTable")
	@ApiOperation(value = "分页查询监管树医疗机构列表", httpMethod = "GET", produces = "application/json")
	@ApiImplicitParams({ @ApiImplicitParam(name = "pageNo", value = "起始页 [默认1]", paramType = "query", required = false),
			@ApiImplicitParam(name = "pageSize", value = "分页大小[默认10]", paramType = "query", required = false),		
			@ApiImplicitParam(name = "category", value = "机构类别", paramType = "query", required = false),
			@ApiImplicitParam(name = "tenantId", value = "租户ID", paramType = "query", required = false),
			@ApiImplicitParam(name = "keyword", value = "关键字", paramType = "query", required = false) })
	@ApiResponse(code = 200, message = "OK", response = Result.class)
	public Result<Page<SysTenant>> getManageTreeTable(TenantQuery query){
		return response(this.tenantService.getManageTreeTable(query));		
	};
	
	/**
	 * 查询监管树树形结构谱
	 * 
	 * @param tenantId
	 * @return
	 */
	@PreAuthorize("isAuthenticated()")
	@IgnoreProperties(allow = { @AllowProperty(pojo = SysTenant.class, name = { "id", "name","parentTenantRank","placeName"}) })
	@GetMapping(value = "/manageTree")
	@ApiOperation(value = "查询监管树树形结构谱", httpMethod = "GET")			
	@ApiImplicitParam(name = "tenantId", value = "租户ID",required = false)
	@ApiResponse(code = 200, message = "OK", response = Result.class)
	public Result<List<SysTenant>> getManageTree(@RequestParam(value = "tenantId", required = false) Long tenantId){		
		return response(this.tenantService.getManageTree(tenantId));		
	};
	
	/**
	 * CMS获取监管树
	 * 
	 * @param tenantId
	 * @return
	 */
	@PreAuthorize("isAuthenticated()")
	@IgnoreProperties(allow = { @AllowProperty(pojo = SysTenant.class, name = { "id", "name","manageTenantId"}) })
	@GetMapping(value = "/getmanageTreeForCms")
	@ApiOperation(value = "CMS获取监管树", httpMethod = "GET")	
	@ApiImplicitParam(name = "tenantId", value = "租户ID",required = true)
	@ApiResponse(code = 200, message = "OK", response = Result.class)
	public Result<List<SysTenant>> getManageTreeForCms(@RequestParam(value = "tenantId", required = true) Long tenantId){
		//TODO
		return response(tenantService.getManageTreeForCms(tenantId));
	}
	
	/**
	 * CMS获取监管树
	 * 
	 * @param tenantId
	 * @return
	 */
	@PreAuthorize("isAuthenticated()")
	@IgnoreProperties(allow = { @AllowProperty(pojo = SysTenant.class, name = { "id", "name","manageTenantId","parentTenantRank"}) })
	@GetMapping(value = "/getManageTreeForCmsHelp")
	@ApiOperation(value = "CMS获取监管树", httpMethod = "GET")	
	@ApiImplicitParam(name = "tenantId", value = "租户ID",required = true)
	@ApiResponse(code = 200, message = "OK", response = Result.class)
	public Result<List<SysTenant>> getManageTreeForCmsHelp(@RequestParam(value = "tenantId", required = true) Long tenantId){
		//TODO
		return response(tenantService.getManageTreeForCmsHelp(tenantId));
	}
	
	/**
	 * CMS根据医疗机构获取监管机构Ids
	 * 
	 * @param tenantId
	 * @return
	 */
	@PreAuthorize("isAuthenticated()")
	@GetMapping(value = "/getManageIdsForCms")
	@ApiOperation(value = "CMS根据医疗机构获取监管机构Ids", httpMethod = "GET")			
	@ApiImplicitParam(name = "tenantId", value = "租户ID",required = true)
	@ApiResponse(code = 200, message = "OK", response = Result.class)
	public Result<List<Long>> getManageIds(@RequestParam(value = "tenantId", required = true) Long tenantId){
		return response(tenantService.getManageIds(tenantId));
	}
	/**
	 * CMS获取医疗机构
	 * 
	 * @param manageId
	 * @param type
	 * @param Keyword
	 * @return
	 */
	@PreAuthorize("isAuthenticated()")
	@IgnoreProperties(allow = { @AllowProperty(pojo = SysTenant.class, name = { "id", "name","manageTenantId"}) })
	@GetMapping(value = "/getHospitalForCms")
	@ApiOperation(value = "CMS获取医疗机构", httpMethod = "GET")
	@ApiImplicitParams({ @ApiImplicitParam(name = "manageId", value = "上级行政机构id", paramType = "query", required = true),
		@ApiImplicitParam(name = "type", value = "医疗机构类型（CMS）", paramType = "query", required = false),		
		@ApiImplicitParam(name = "Keyword", value = "搜索关键字", paramType = "query", required = false)})
	@ApiResponse(code = 200, message = "OK", response = Result.class)
	public Result<List<SysTenant>> getHospitalForCms(@RequestParam(value="manageId",required=true)Long manageId,
			@RequestParam(value="type",required=false)Integer type,
			@RequestParam(value="Keyword",required=false)String keyword){
		CmsTenantQuery query = new CmsTenantQuery();
		query.setManageId(manageId);
		query.setType(type);
		query.setKeyword(keyword);
		return response(tenantService.getHospitalForCms(query));
	}

	/**
	 * 修改机构信息
	 * 
	 * @param request
	 * @return
	 */
	@PreAuthorize("hasAuthority('SYS_SUPERVISE_TENANT_MANAGE','SYS_HPL_TENANT_MANAGE')")
	@PutMapping(value = "/edit")
	@ApiOperation(value = "编辑机构信息", httpMethod = "PUT", produces = "application/json")
	@ApiResponse(code = 0, message = "OK", response = Result.class)
	public Result<SysTenant> editTenant(@Validated(value = Edit.class) @RequestBody TenantRequest request) {
		SysTenant tenant = null;
		SysUser admin = null;

		if (request.getCreateAdmin()) {
			admin = new SysUser(request.getRealName(), request.getPassword(), request.getMobile(), request.getEmail());
		}

		tenant = BeanMapper.map(request, SysTenant.class);
		this.tenantService.modifySubTenant(tenant, admin);
		return response(tenant);
	}
	
	/**
	 * 辅助监管树根据ID查询机构信息
	 *
	 * @return
	 */
	@PreAuthorize("isAuthenticated()")
	@GetMapping(value = "/viewTreeTable/{tenantId}")
	@ApiOperation(value = "根据ID查询机构信息", httpMethod = "GET", produces = "application/json")
	@ApiResponse(code = 0, message = "OK", response = Result.class)
	@ApiImplicitParam(name = "tenantId", value = "机构ID", paramType = "path", required = true)
	public Result<SysTenantUserVo> queryTenantTreeTable(@PathVariable("tenantId") Long tenantId) {
		SysTenant tenant = this.tenantService.selectById(tenantId);
		SysTenantUserVo tvo = new SysTenantUserVo();
		tvo.setTenant(tenant);
		SysUser createUser = this.userService.selectById(tenant.getCreateBy());
		if (createUser != null) {
			tvo.setCreateName(createUser.getRealName());
		}
		if (tenant != null) {
			// 监管或医疗机构详细信息
			if (TenantType.HOSPITAL.getNumber().equals(tenant.getTenantType())) {
				HplTenant hpl = JSON.parseObject(tenant.getCustomData(), HplTenant.class);
				tenant.setHplTenant(hpl);
			} else if (TenantType.SUPERVISE.getNumber().equals(tenant.getTenantType())) {
				SuperviseTenant supervise = JSON.parseObject(tenant.getCustomData(), SuperviseTenant.class);
				tenant.setSuperviseTenant(supervise);
			}

			// 管理员
			if (tenant.getAdminId() != null) {
				SysUser admin = this.userService.selectById(tenant.getAdminId());
				if(null != admin && !admin.getDelFlag()){
					SysTenantAdminVo tAdmin = BeanMapper.map(admin, SysTenantAdminVo.class);
					tvo.setTenantAdmin(tAdmin);
				}
			}

			// 上级行政单位
			if (tenant.getManageTenantId() != null) {
				SysTenant manageTenant = this.tenantService.selectById(tenant.getManageTenantId());
				if (manageTenant != null) {
					tenant.setManageTenantName(manageTenant.getName());
				}
			}

		}
		return response(tvo);
	}

	/**
	 * 根据ID查询机构信息
	 *
	 * @return
	 */
	@PreAuthorize("hasAuthority('SYS_SUPERVISE_TENANT_MANAGE','SYS_HPL_TENANT_MANAGE','SYS_SUPERVISE_TENANT_DETAILED_VIEW','SYS_HPL_TENANT_DETAILED_VIEW')")
	@GetMapping(value = "/view/{tenantId}")
	@ApiOperation(value = "根据ID查询机构信息", httpMethod = "GET", produces = "application/json")
	@ApiResponse(code = 0, message = "OK", response = Result.class)
	@ApiImplicitParam(name = "tenantId", value = "机构ID", paramType = "path", required = true)
	public Result<SysTenantUserVo> queryTenant(@PathVariable("tenantId") Long tenantId) {
		SysTenant tenant = this.tenantService.selectById(tenantId);
		SysTenantUserVo tvo = new SysTenantUserVo();
		tvo.setTenant(tenant);
		SysUser createUser = this.userService.selectById(tenant.getCreateBy());
		if (createUser != null) {
			tvo.setCreateName(createUser.getRealName());
		}
		if (tenant != null) {
			// 监管或医疗机构详细信息
			if (TenantType.HOSPITAL.getNumber().equals(tenant.getTenantType())) {
				HplTenant hpl = JSON.parseObject(tenant.getCustomData(), HplTenant.class);
				tenant.setHplTenant(hpl);
			} else if (TenantType.SUPERVISE.getNumber().equals(tenant.getTenantType())) {
				SuperviseTenant supervise = JSON.parseObject(tenant.getCustomData(), SuperviseTenant.class);
				tenant.setSuperviseTenant(supervise);
			}

			// 管理员
			if (tenant.getAdminId() != null) {
				SysUser admin = this.userService.selectById(tenant.getAdminId());
				if(null != admin && !admin.getDelFlag()){
					SysTenantAdminVo tAdmin = BeanMapper.map(admin, SysTenantAdminVo.class);
					tvo.setTenantAdmin(tAdmin);
				}
			}

			// 上级行政单位
			if (tenant.getManageTenantId() != null) {
				SysTenant manageTenant = this.tenantService.selectById(tenant.getManageTenantId());
				if (manageTenant != null) {
					tenant.setManageTenantName(manageTenant.getName());
				}
			}

		}
		return response(tvo);
	}

	/**
	 * 查询机构管理首页列表
	 *
	 * @return
	 */
	@PreAuthorize("hasAuthority('SYS_HPL_TENANT_LIST_VIEW','SYS_SUPERVISE_TENANT_LIST_VIEW')")
	@IgnoreProperties(allow = { @AllowProperty(pojo = SysTenant.class, name = { "id", "name", "tenantType",
			"commercialUse", "province", "city", "county", "trial", "origin", "auditStatus", "subTenantLimit",
			"subTenant", "expireTime", "createTime", "enable", "manageTenantName" }) })
	@GetMapping(value = "/index/list")
	@ApiOperation(value = "分页查询子机构列表", httpMethod = "GET", produces = "application/json")
	@ApiImplicitParams({ @ApiImplicitParam(name = "pageNo", value = "起始页 [默认1]", paramType = "query", required = false),
			@ApiImplicitParam(name = "pageSize", value = "分页大小[默认10]", paramType = "query", required = false),
			@ApiImplicitParam(name = "tenantId", value = "租户ID [必须]", paramType = "query", required = true),
			@ApiImplicitParam(name = "enable", value = "启用未启用[true=启用,false=未启用]", paramType = "query", required = false),
			@ApiImplicitParam(name = "origin", value = "来源[1后台创建，2前台注册]", paramType = "query", required = false),
			@ApiImplicitParam(name = "auditStatus", value = "审核状态[1待审核,2自动通过,3人工通过,4已拒绝]", paramType = "query", required = false),
			@ApiImplicitParam(name = "tenantType", value = "机构类型[1=医疗机构,2=监管机构,3=设备供应商,4=设备维修商,5=配件供应商]", paramType = "query", required = false),
			@ApiImplicitParam(name = "commercialUse", value = "账户类型[0=试用,1=正式]", paramType = "query", required = false),
			@ApiImplicitParam(name = "trial", value = "是否测试[1 否, 0 是]", paramType = "query", required = false),
			@ApiImplicitParam(name = "keyword", value = "关键字", paramType = "query", required = false) })
	@ApiResponse(code = 200, message = "OK", response = Result.class)
	public Result<Page<SysTenant>> findPageByKeyword(TenantQuery query) {
		Page<SysTenant> page = this.tenantService.findPageByKeyword(query);
		List<SysTenant> tenants = page.getRecords();
		for (SysTenant tenant : tenants) {
			if (tenant.getManageTenantId() != null) {
				SysTenant manageTenant = this.tenantService.selectById(tenant.getManageTenantId());
				if (manageTenant != null) {
					tenant.setManageTenantName(manageTenant.getName());
				}
			}
		}
		return response(page);
	}

	/**
	 * 停用机构
	 *
	 * @return
	 */
	@PreAuthorize("hasAuthority('SYS_SUPERVISE_TENANT_MANAGE','SYS_HPL_TENANT_MANAGE')")
	@PostMapping(value = "/stop/{tenantId}")
	@ApiOperation(value = "停用机构", httpMethod = "POST", produces = "application/json")
	@ApiResponse(code = 200, message = "OK", response = Result.class)
	@ApiImplicitParam(name = "tenantId", value = "机构ID", paramType = "path", required = true)
	public Result<Object> stopTenant(@PathVariable(value = "tenantId", required = true) Long tenantId) {
		this.tenantService.stopTenantById(tenantId);
		return response();
	}

	/**
	 * 恢复机构
	 *
	 * @return
	 */
	@PreAuthorize("hasAuthority('SYS_SUPERVISE_TENANT_MANAGE','SYS_HPL_TENANT_MANAGE')")
	@PostMapping(value = "/recover/{tenantId}")
	@ApiOperation(value = "恢复机构", httpMethod = "POST", produces = "application/json")
	@ApiResponse(code = 200, message = "OK", response = Result.class)
	@ApiImplicitParams(value={
			@ApiImplicitParam(name = "tenantId",value = "机构ID",paramType = "path" ,required = true),
			@ApiImplicitParam(name = "expireTime",value = "机构有效时间(值为null,永久有效)",required = true)})
	public Result<Object> recoverTenant(@PathVariable(value = "tenantId", required = true) Long tenantId,
			@RequestParam(value = "expireTime", required = false) Date expireTime) {
		this.tenantService.recoverTenantById(tenantId,expireTime);
		return response();
	}
	
	/**
	 * 删除机构
	 *
	 * @return
	 */
	@PreAuthorize("hasAuthority('SYS_SUPERVISE_TENANT_MANAGE','SYS_HPL_TENANT_MANAGE')")
	@RequestMapping(value = "/delete/{tenantId}")
	@ApiOperation(value = "删除机构", httpMethod = "DELETE", produces = "application/json")
	@ApiResponse(code = 200, message = "OK", response = Result.class)
	@ApiImplicitParams(value = {
			@ApiImplicitParam(name = "tenantId", value = "机构ID", paramType = "path", required = true),
			@ApiImplicitParam(name = "force", value = "是否执行删除操作,默认为false(true=执行/false=不执行)", paramType = "query", required = false) })
	public Result<Object> deleteTenant(@PathVariable(value = "tenantId", required = true) Long tenantId,
			@RequestParam(value = "force", required = false, defaultValue = "false") Boolean force) {
		SysTenant tenant = this.tenantService.selectById(tenantId);
		if (tenant == null) {
			throw ExceptionFactory.create("O_008");
		}
		// 监管单位判断是否被使用或拥有下级机构
		if (tenant.getTenantType().equals(TenantType.SUPERVISE.getNumber())) {
			List<SysTenant> manageTenants = this.tenantService.findByManageTenantId(tenantId);
			if (CollectionUtils.isNotEmpty(manageTenants)) {
				throw ExceptionFactory.create("O_014");
			}
		}
		// 是否执行删除操，false则只是查询是否可以删除
		if (force) {
			this.tenantService.deleteTenantById(tenantId);
		}
		return response();
	}

	/**
	 * 机构延期
	 * 
	 * @param tenantId
	 * @param days
	 * @param updateBy
	 * @return
	 */
	@PreAuthorize("hasAuthority('SYS_SUPERVISE_TENANT_MANAGE','SYS_HPL_TENANT_MANAGE')")
	@RequestMapping(value = "/delay/tenant/{tenantId}")
	@ApiOperation(value = "机构延期", httpMethod = "DELETE", produces = "application/json")
	@ApiImplicitParams(value = {
			@ApiImplicitParam(name = "tenantId", value = "机构ID", paramType = "path", required = true),
			@ApiImplicitParam(name = "days", value = "时间(天)", paramType = "query", required = true),
			@ApiImplicitParam(name = "updateBy", value = "修改人ID", paramType = "query", required = true) })
	@ApiResponse(code = 200, message = "OK", response = Result.class)
	public Result<Object> delayTenant(@PathVariable(value = "tenantId", required = true) Long tenantId,
			@RequestParam(value = "days", required = true) Integer days,
			@RequestParam(value = "updateBy", required = true) Long updateBy) {
		this.tenantService.delayTenant(updateBy, tenantId, days);
		return response();
	}

	/**
	 * 获取所有上级机构名称 以/间隔
	 * 
	 * @param tenantId
	 * @param userId
	 * @return
	 */
	@PreAuthorize("isAuthenticated()")
	@GetMapping(value = "/view/tenant/{tenantId}/user/{userId}")
	@ApiImplicitParams(value = {
			@ApiImplicitParam(name = "tenantId", value = "机构ID", paramType = "path", required = true),
			@ApiImplicitParam(name = "userId", value = "用户ID", paramType = "path", required = true) })
	@ApiOperation(value = "获取所有上级机构名称", httpMethod = "GET", produces = "application/json")
	@ApiResponse(code = 200, message = "OK", response = Result.class)
	public Result<String> findUserCurrUpTenant(@PathVariable(value = "tenantId", required = true) Long tenantId,
			@PathVariable(value = "userId", required = true) Long userId) {
		return response(this.tenantService.findUserCurrUpTenant(tenantId, userId));
	}

	/**
	 * 获取所有上级行政单位（新建机构用于选择）
	 * 
	 * @return
	 */
	@PreAuthorize("isAuthenticated()")
	@IgnoreProperties(allow = { @AllowProperty(pojo = SysTenant.class, name = { "id", "name", "tenantType" }) })
	@GetMapping(value = "/all/manageTenant")
	@ApiOperation(value = "获取所有上级行政单位（新建机构用于选择）", httpMethod = "GET", produces = "application/json")
	@ApiImplicitParam(name = "province", value = "省(过滤条件)", paramType = "query", required = false)
	@ApiResponse(code = 200, message = "OK", response = Result.class)
	public Result<List<SysTenant>> findAllHigManageTenant(
			@RequestParam(value = "province", required = false) String province) {
		List<SysTenant> superviseTeants = this.tenantService.findAllSuperviseByEveryLevel();
		Long rootSuperviseId = SysConstants.ROOT_SUPERVISE_TENANT_ID; // 顶级监管机构ID

		if (StringUtils.isNotBlank(province)) {
			// 过滤
			Iterator<SysTenant> iterator = superviseTeants.iterator();
			while (iterator.hasNext()) {
				SysTenant tenant = iterator.next();
				String tenantProvince = tenant.getProvince();
				Long tenantId = tenant.getId();
				if (!StringUtils.equals(province, tenantProvince) && !rootSuperviseId.equals(tenantId)) {
					iterator.remove();
				}
			}

		}
		return response(superviseTeants);
	}

	/**
	 * 查询机构下级统计信息，按天统计
	 * 
	 * @return
	 */
	@PreAuthorize("isAuthenticated()")
	@GetMapping(value = "/censusByDay")
	@ApiImplicitParams(value = {
			@ApiImplicitParam(name = "tenantId", value = "机构ID", paramType = "query", required = true),
			@ApiImplicitParam(name = "dateLabel", value = "自定义时间段时,不需要传入该参数;0:上月;1:本月;2:本季度;3:本年", paramType = "query", required = false),
			@ApiImplicitParam(name = "startDate", value = "开始时间(格式:yyyy-MM-dd HH:mm:ss)", paramType = "query", required = false),
			@ApiImplicitParam(name = "endDate", value = "截止时间(格式:yyyy-MM-dd HH:mm:ss)", paramType = "query", required = false) })
	@ApiOperation(value = "获取所有上级机构名称", httpMethod = "GET", produces = "application/json")
	@ApiResponse(code = 200, message = "OK", response = Result.class)
	public Result<List<SysTenantCensusVo>> getTenantCensusByDay(CensusRequest request) {
		Date beginDate = null;
		Date endDate = null;

		Integer label = request.getDateLabel();
		if (label != null && label < 4) {
			Date now = new Date();

			// 上月
			if (label == 0) {
				Calendar cal = Calendar.getInstance();
				cal.add(Calendar.MONTH, -1);
				beginDate = RemindDateUtils.getCurrentMonthStartTime(cal.getTime());
				endDate = RemindDateUtils.getCurrentMonthEndTime(cal.getTime());
			}
			// 本月
			if (label == 1) {
				beginDate = RemindDateUtils.getCurrentMonthStartTime(now);
				endDate = RemindDateUtils.getCurrentMonthEndTime(now);
				// 设置时分秒
			}
			// 本季度
			if (label == 2) {
				beginDate = RemindDateUtils.getCurrentQuarterStartTime(now);
				endDate = RemindDateUtils.getCurrentQuarterEndTime(now);
			}
			// 本年
			if (label == 3) {
				beginDate = RemindDateUtils.getCurrentYearStartTime(now);
				endDate = RemindDateUtils.getCurrentYearEndTime(now);
			}

		} else if (request.getStartDate() != null && request.getEndDate() != null) {
			beginDate = request.getStartDate();
			endDate = request.getEndDate();
		}

		// 参数不存在，错误提示
		if (beginDate == null || endDate == null || beginDate.after(endDate)) {
			throw ExceptionFactory.create("501");
		}

		// 设置小时，防止误差
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(endDate);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		endDate = calendar.getTime();

		return response(this.tenantService.findTenantCensusByTime(request.getTenantId(), beginDate, endDate));
	}

	/**
	 * 根据用户ID集合查询用户信息
	 * 
	 * @param userIds
	 * @return
	 */
	@PreAuthorize("isAuthenticated()")
	@GetMapping(value = "/count", produces = "application/json;charset=UTF-8")
	@ApiImplicitParams(value = {
			@ApiImplicitParam(name = "tenantId", value = "机构ID", paramType = "query", required = true),
			@ApiImplicitParam(name = "flag", value = "1.查询平台医疗机构数量 2.查询监管机构所监管的医疗机构数量", paramType = "query", required = true) })
	@ApiOperation(value = "查询医疗机构数量")
	@ApiResponse(code = 200, message = "OK", response = Result.class)
	public Result<Integer> findTenantCount(@RequestParam(value = "tenantId", required = true) Long tenantId,
			@RequestParam(value = "flag", required = true) Integer flag) {
		int tenantCount = 0;
		if (flag == 1) {
			tenantCount = this.tenantService.findPlatformHplTenantCount();
		}
		if (flag == 2) {
			tenantCount = this.tenantService.findHplTenantByManageTenantId(tenantId);
		}
		return response(tenantCount);
	}

	/**
	   * 创建申请试用子机构
	   *     机构名称即时校验
	   * 
	   * @param tenantName
	   *       机构名称
	   * @return result(true已存在false未重复)
	   */
	  @GetMapping(value = "/tenantNameCheck")
	  @ApiOperation(value = "机构名称即时校验", httpMethod = "GET", produces = "application/json")      
	  @ApiImplicitParam(name = "tenantName", value = "租户名称",required = false)
	  @ApiResponse(code = 200, message = "OK", response = Result.class)
	  public Result<Boolean> tenantNameAndMoblieCheck(@RequestParam(value = "tenantName", required = false) String tenantName){
	    Boolean result = null;
	    if(tenantName != null && !tenantName.equals("")){
	      result= tenantService.findByTenantName(tenantName) != null?true : false;
	    };
	    return response(result);
	  };
	  
	  /**
	   * 创建申请试用子机构
	   *     手机号即时校验
	   * 
	   * @param mobile
	   *       手机号
	   * @return result(true已存在false未重复)
	   */
	  @GetMapping(value = "/mobileCheck")
	  @ApiOperation(value = "手机号即时校验", httpMethod = "GET", produces = "application/json")      
	  @ApiImplicitParam(name = "mobile", value = "用户手机号",required = false)
	  @ApiResponse(code = 200, message = "OK", response = Result.class)
	  public Result<Boolean> moblieCheck(@RequestParam(value = "mobile", required = false) String mobile){
	    Boolean result = null;
	    if(mobile != null && !mobile.equals("")){
	      result= userService.findByMobile(mobile) != null?true : false;
	    };
	    return response(result);
	  };
	  
	  @GetMapping(value = "/selectWxTenant")
	  @ApiOperation(value = "支持关键字查询所有医疗机构和监管机构", httpMethod = "GET", produces = "application/json")      
	  @ApiImplicitParams({ @ApiImplicitParam(name = "keyword", value = "关键字检索", paramType = "query"),
			@ApiImplicitParam(name = "tenantId", value = "机构ID", paramType = "query", required = true)})
	  @ApiResponse(code = 200, message = "OK", response = Result.class)
	  public Result<List<WxTenantVo>> selectWxTenant(@RequestParam(value="keyword",required=false)String keyword,
			  @RequestParam("tenantId")Long tenantId){
		  return response(tenantService.selectWxTenant(keyword, tenantId));
	  }

	@GetMapping(value = "/getManageTenantByTenantId")
	@ApiOperation(value = "根据机构ID查询所有上级监管机构ID", httpMethod = "GET", produces = "application/json")
	@ApiImplicitParams({ @ApiImplicitParam(name = "keyword", value = "关键字检索", paramType = "query"),
			@ApiImplicitParam(name = "tenantId", value = "机构ID", paramType = "query", required = true)})
	@ApiResponse(code = 200, message = "OK", response = Result.class)
	public Result<List<Long>> getManageTenantByTenantId(@RequestParam("tenantId")Long tenantId){
		return response(tenantService.getManageTenantByTenantId(tenantId));
	}
}
