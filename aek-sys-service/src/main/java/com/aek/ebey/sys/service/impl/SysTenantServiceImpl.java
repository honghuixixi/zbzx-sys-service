package com.aek.ebey.sys.service.impl;


import java.text.CollationKey;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.baomidou.mybatisplus.enums.SqlLike;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aek.common.core.BeanMapper;
import com.aek.common.core.Result;
import com.aek.common.core.base.BaseServiceImpl;
import com.aek.common.core.config.RedisRepository;
import com.aek.common.core.exception.ExceptionFactory;
import com.aek.common.core.serurity.WebSecurityUtils;
import com.aek.common.core.serurity.model.AuthUser;
import com.aek.common.core.util.DateUtil;
import com.aek.ebey.sys.core.SysConstants;
import com.aek.ebey.sys.enums.TenantAuditStatus;
import com.aek.ebey.sys.enums.TenantOperation;
import com.aek.ebey.sys.enums.TenantOrigin;
import com.aek.ebey.sys.enums.TenantType;
import com.aek.ebey.sys.mapper.SysSupplierTenantCredentialsMapper;
import com.aek.ebey.sys.mapper.SysTenantMapper;
import com.aek.ebey.sys.model.SysDept;
import com.aek.ebey.sys.model.SysRole;
import com.aek.ebey.sys.model.SysRoleUser;
import com.aek.ebey.sys.model.SysSupplierTenant;
import com.aek.ebey.sys.model.SysSupplierTenantCredentials;
import com.aek.ebey.sys.model.SysTenant;
import com.aek.ebey.sys.model.SysUser;
import com.aek.ebey.sys.model.bo.TenantExcelBo;
import com.aek.ebey.sys.model.bo.VerifyCode;
import com.aek.ebey.sys.model.custom.HplTenant;
import com.aek.ebey.sys.model.custom.SupplierTenant;
import com.aek.ebey.sys.model.custom.WebSiteUser;
import com.aek.ebey.sys.model.query.TenantQuery;
import com.aek.ebey.sys.model.vo.CmsTenantQuery;
import com.aek.ebey.sys.model.vo.SysTenantCensusVo;
import com.aek.ebey.sys.model.vo.SysTenantRoleVo;
import com.aek.ebey.sys.model.vo.TenantAssetsVo;
import com.aek.ebey.sys.model.vo.WxTenantVo;
import com.aek.ebey.sys.service.SysDeptService;
import com.aek.ebey.sys.service.SysRoleService;
import com.aek.ebey.sys.service.SysRoleUserService;
import com.aek.ebey.sys.service.SysSupplierTenantCredentialsService;
import com.aek.ebey.sys.service.SysTenantModuleService;
import com.aek.ebey.sys.service.SysTenantService;
import com.aek.ebey.sys.service.SysUserService;
import com.aek.ebey.sys.service.ribbon.AssetClientService;
import com.aek.ebey.sys.service.ribbon.AuthClientService;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.google.common.collect.Lists;

/**
 * <p>
 * 医院机构表 服务实现类
 * </p>
 *
 * @author aek
 * @since 2017-05-06
 */
@Service
@Transactional
public class SysTenantServiceImpl extends BaseServiceImpl<SysTenantMapper, SysTenant> implements SysTenantService {

	private Logger logger = LoggerFactory.getLogger(SysTenantServiceImpl.class);

	@Autowired
	private SysTenantMapper tenantMapper;

	@Autowired
	private SysUserService userService;

	@Autowired
	private SysDeptService deptService;

	@Autowired
	private SysRoleService roleService;

	@Autowired
	private SysTenantModuleService tenantModuleService;

	@Autowired
	private SysRoleUserService roleUserService;

	@Autowired
	private AuthClientService authClientService;
	
	@Autowired
	private RedisRepository redisRepository;
	
	@Autowired
	private AssetClientService deviceService;
	
	@Autowired
	private SysSupplierTenantCredentialsService supplierTenantCredentialsService;
	
	@Autowired
	private SysSupplierTenantCredentialsMapper  supplierTenantCredentgialsMapper;

	@Override
	public void createSubTenant(SysTenant tenant, SysUser admin) {
		Date currTime = new Date();

		SysUser createUser = this.userService.selectById(tenant.getCreateBy());
		if (createUser == null) {
			throw ExceptionFactory.create("G_004");
		}

		SysTenant manageTenant = this.selectById(tenant.getManageTenantId());
		if (manageTenant == null) {
			//上级行政机构不能为空
			throw ExceptionFactory.create("O_012");
		}

		// 重复验证
		if (TenantType.HOSPITAL.getNumber().equals(tenant.getTenantType())) {
			HplTenant hplTenant = tenant.getHplTenant();
			if(StringUtils.isNotEmpty(tenant.getLicense())){
				if (null != this.findTenantByLicense(tenant.getLicense())) {
					//组织机构代码已存在
					throw ExceptionFactory.create("O_011");
				}
			}
			if(null != hplTenant){
				if (hplTenant.getOrgCode() == null) {
					//医疗机构代码不能为空
					throw ExceptionFactory.create("O_013");
				}
				if (CollectionUtils.isNotEmpty(this.findByOrgCode(hplTenant.getOrgCode()))) {
					//医疗机构代码重复
					throw ExceptionFactory.create("O_016");
				}
			}
		}
		if (this.isRepeatByNameAndType(tenant.getName(), tenant.getTenantType())) {
			//医疗机构已存在,请联系管理员开通个人账号
			throw ExceptionFactory.create("O_017");
		}

		SysTenant parentTenant = this.selectById(tenant.getParentId());

		// 处理子机构创建数量限制
		this.disposeSubTenant(tenant);

		this.initParentIds(tenant, parentTenant);

		// 非系统管理员创建继承父机构到期时间及父机构类型
		/*if (!createUser.getAdminFlag()) {
			tenant.setExpireTime(parentTenant.getExpireTime());
			tenant.setTenantType(parentTenant.getTenantType());
		}*/
		
		//非爱医康用户创建医疗机构时，继承父机构到期时间及父机构类型
		if(!SysConstants.AEK_TENANT_ID.equals(parentTenant.getId()) && TenantType.HOSPITAL.getNumber() == tenant.getTenantType()){
			tenant.setExpireTime(parentTenant.getExpireTime());
			tenant.setTenantType(parentTenant.getTenantType());
		}

		tenant.setCreateBy(createUser.getId());
		tenant.setCreateTime(currTime);
		tenant.setOrigin(TenantOrigin.BACK_CREATE.getNumber());
		tenant.setAuditStatus(TenantAuditStatus.THROUGH.getNumber());
		tenant.setUpdateBy(createUser.getId());
		tenant.setUpdateTime(currTime);

		//处理当前机构与上级行政机构建立关系的时间
		if(null != tenant.getManageTenantId()){
			tenant.setManageTenantTime(currTime);
		}
		
		// 处理机构类型数据
		this.disposeCustomData(tenant);
		this.insert(tenant);

		// 初始化机构部门信息(根节点默认与机构名称相同)
		this.deptService.initTenantDept(tenant);

		// 初始化模块
		this.tenantModuleService.initTenantModule(tenant.getId());

		// 初始化机构角色信息
		this.roleService.initTenantRole(tenant.getId());

		// 初始化机构管理员
		if (null != admin) {
			this.userService.initTenantAdminUser(tenant, admin,WebSecurityUtils.getCurrentUser());
		}

		// 更新新建机构关联信息
		this.updateById(tenant);
	}
	
	@Override
	public void createTrialSubTenant(SysTenant tenant, SysUser admin, VerifyCode verifyCode) {
		Date currTime = new Date();

		SysTenant manageTenant = this.selectById(tenant.getManageTenantId());
		if (manageTenant == null) {
			//上级行政机构不能为空
			throw ExceptionFactory.create("O_012");
		}

		//机构来源判断
		if(tenant.getOrigin() != null && tenant.getOrigin() == TenantOrigin.FRONT_REGISTER.getNumber()){
			//缓存中取出验证码
			String key = SysConstants.SYS_REGISTER_CODE + admin.getMobile();
			String verifyCodeJson = this.redisRepository.get(key);
			if(StringUtils.isNotBlank(verifyCodeJson)){
				VerifyCode currVerifyCode = JSON.parseObject(verifyCodeJson, VerifyCode.class);
				String cacheVerifyCode = currVerifyCode.getCode();
				
				//验证码错误
				if(!verifyCode.getCode().equals(cacheVerifyCode)){
					throw ExceptionFactory.create("L_003");
				};		
			}else{
				throw ExceptionFactory.create("L_007");
			}
		};
		
		// 重复验证
		if (TenantType.HOSPITAL.getNumber().equals(tenant.getTenantType())) {
			HplTenant hplTenant = tenant.getHplTenant();
			if(StringUtils.isNotEmpty(tenant.getLicense())){
				if (null != this.findTenantByLicense(tenant.getLicense())) {
					//组织机构代码已存在
					throw ExceptionFactory.create("O_011");
				}
			}
			if(null != hplTenant){
				if (hplTenant.getOrgCode() == null) {
					//医疗机构代码不能为空
					throw ExceptionFactory.create("O_013");
				}
				if (CollectionUtils.isNotEmpty(this.findByOrgCode(hplTenant.getOrgCode()))) {
					//医疗机构代码重复
					throw ExceptionFactory.create("O_016");
				}
			}
		}
		if (this.isRepeatByNameAndType(tenant.getName(), tenant.getTenantType())) {
			//机构名称重复
			throw ExceptionFactory.create("O_015");
		}

		SysTenant parentTenant = this.selectById(tenant.getParentId());
		
		this.initParentIds(tenant, parentTenant);
		
		tenant.setCreateTime(currTime);
		
		//根据机构来源判断为后台创建或前台注册
		if(tenant.getOrigin() !=null && tenant.getOrigin() == TenantOrigin.FRONT_REGISTER.getNumber()){
			//前台创建待审核
			tenant.setOrigin(TenantOrigin.FRONT_REGISTER.getNumber());
			tenant.setAuditStatus(TenantAuditStatus.PENDING.getNumber());
			//设置机构到期时间
			tenant.setExpireTime(parentTenant.getExpireTime());
		}

		tenant.setUpdateTime(currTime);
		tenant.setTrial(1);
		
		//处理当前机构与上级行政机构建立关系的时间
		if(null != tenant.getManageTenantId()){
			tenant.setManageTenantTime(currTime);
		}


		// 处理机构类型数据
		this.disposeCustomData(tenant);
		this.insert(tenant);

		// 初始化机构部门信息(根节点默认与机构名称相同)
		this.deptService.initTenantDept(tenant);

		// 初始化模块
		this.tenantModuleService.initTenantModule(tenant.getId());

		// 初始化机构角色信息
		this.roleService.initTenantRole(tenant.getId());

		// 初始化机构管理员
		if (null != admin) {
			SysUser createUser = this.userService.initTenantAdminUser(tenant, admin,null);
			//赋予机构创建人
			tenant.setCreateBy(createUser.getId());
			
			//赋予管理员创建人
			admin.setCreateBy(createUser.getId());
			this.userService.updateById(admin);
			
			//同步机构更新人
			tenant.setUpdateBy(createUser.getId());
		}
		
		// 更新新建机构关联信息
		this.updateById(tenant);
		
	}
	
	@Override
	public void createTrialSubTenantForZjzx(SysTenant tenant, SysUser admin, VerifyCode verifyCode) {
		Date currTime = new Date();

		SysTenant manageTenant = this.selectById(tenant.getManageTenantId());
		if (manageTenant == null) {
			//上级行政机构不能为空
			throw ExceptionFactory.create("O_012");
		}

		//机构来源判断
		if(tenant.getOrigin() != null && tenant.getOrigin() == TenantOrigin.FRONT_REGISTER.getNumber()){
			//缓存中取出验证码
			String key = SysConstants.SYS_REGISTER_CODE + admin.getMobile();
			String verifyCodeJson = this.redisRepository.get(key);
			if(StringUtils.isNotBlank(verifyCodeJson)){
				VerifyCode currVerifyCode = JSON.parseObject(verifyCodeJson, VerifyCode.class);
				String cacheVerifyCode = currVerifyCode.getCode();
				
				//验证码错误
				if(!verifyCode.getCode().equals(cacheVerifyCode)){
					throw ExceptionFactory.create("L_003");
				};		
			}else{
				throw ExceptionFactory.create("L_007");
			}
		};
			
		// 重复验证
		if (TenantType.HOSPITAL.getNumber().equals(tenant.getTenantType())) {
			HplTenant hplTenant = tenant.getHplTenant();
			if(StringUtils.isNotEmpty(tenant.getLicense())){
				if (null != this.findTenantByLicense(tenant.getLicense())) {
					//组织机构代码已存在
					throw ExceptionFactory.create("O_011");
				}
			}
//			if(null != hplTenant){
//				if (hplTenant.getOrgCode() == null) {
//					//医疗机构代码不能为空
//					throw ExceptionFactory.create("O_013");
//				}
//				if (CollectionUtils.isNotEmpty(this.findByOrgCode(hplTenant.getOrgCode()))) {
//					//医疗机构代码重复
//					throw ExceptionFactory.create("O_016");
//				}
//			}
		}
		if (this.isRepeatByNameAndType(tenant.getName(), tenant.getTenantType())) {
			//机构名称重复
			throw ExceptionFactory.create("O_015");
		}

		SysTenant parentTenant = this.selectById(tenant.getParentId());
		
		this.initParentIds(tenant, parentTenant);
		
		tenant.setCreateTime(currTime);
		
		//根据机构来源判断为后台创建或前台注册
		if(tenant.getOrigin() !=null && tenant.getOrigin() == TenantOrigin.FRONT_REGISTER.getNumber()){
			//前台创建待审核
			tenant.setOrigin(TenantOrigin.FRONT_REGISTER.getNumber());
			tenant.setAuditStatus(TenantAuditStatus.PENDING.getNumber());
			//设置机构到期时间
			tenant.setExpireTime(parentTenant.getExpireTime());
		}

		tenant.setUpdateTime(currTime);
		tenant.setTrial(1);
		
		//处理当前机构与上级行政机构建立关系的时间
		if(null != tenant.getManageTenantId()){
			tenant.setManageTenantTime(currTime);
		}


		// 处理机构类型数据
		this.disposeCustomData(tenant);
		this.insert(tenant);

		// 初始化机构部门信息(根节点默认与机构名称相同)
		this.deptService.initTenantDept(tenant);

		// 初始化模块
		this.tenantModuleService.initTenantModule(tenant.getId());

		// 初始化机构角色信息
		this.roleService.initTenantRole(tenant.getId());

		// 初始化机构管理员
		if (null != admin) {
			SysUser createUser = this.userService.initTenantAdminUserForZjzx(tenant, admin,null);
			//赋予机构创建人
			tenant.setCreateBy(createUser.getId());
			
			//赋予管理员创建人
			admin.setCreateBy(createUser.getId());
			this.userService.updateById(admin);
			
			//同步机构更新人
			tenant.setUpdateBy(createUser.getId());
		}
		
		// 更新新建机构关联信息
		this.updateById(tenant);
		
	}
	
	@Override
	public void createSupplierTenant(SysTenant tenant, SysUser admin) {
		Date currTime = new Date();

		SysUser createUser = this.userService.selectById(tenant.getCreateBy());
		if (createUser == null) {
			throw ExceptionFactory.create("G_004");
		}

		if (this.isRepeatByNameAndType(tenant.getName(), tenant.getTenantType())) {
			//供应商名称重复
			throw ExceptionFactory.create("S_006");
		}

		SysTenant parentTenant = this.selectById(tenant.getParentId());

		this.initParentIds(tenant, parentTenant);

		tenant.setCreateBy(createUser.getId());
		tenant.setCreateTime(currTime);
		tenant.setOrigin(TenantOrigin.BACK_CREATE.getNumber());
		tenant.setAuditStatus(TenantAuditStatus.THROUGH.getNumber());
		tenant.setUpdateBy(createUser.getId());
		tenant.setUpdateTime(currTime);
		
		// 处理机构类型数据
		this.disposeCustomData(tenant);
		this.insert(tenant);
		
		//处理供应商证件信息
		List<SysSupplierTenantCredentials> supplierCredentials = tenant.getSupplierCredentials();
		if(null != supplierCredentials){
			for (SysSupplierTenantCredentials sysSupplierTenantCredentials : supplierCredentials) {
				sysSupplierTenantCredentials.setTenantId(tenant.getId());
				if(null != sysSupplierTenantCredentials){
					supplierTenantCredentialsService.insert(sysSupplierTenantCredentials);
				}
			}
		}
		
		// 初始化机构部门信息(根节点默认与机构名称相同)
		this.deptService.initSupplierTenantDept(tenant);

		// 初始化模块
		this.tenantModuleService.initTenantModule(tenant.getId());

		// 初始化机构角色信息
		this.roleService.initTenantRole(tenant.getId());

		// 初始化机构管理员
		if (null != admin) {
			this.userService.initTenantAdminUser(tenant, admin,WebSecurityUtils.getCurrentUser());
		}
	}

	@Override
	public void operateTenant(Long tenantId, Integer operation) {
		SysTenant tenant = this.selectById(tenantId);
		
		if(operation != null && operation == TenantOperation.PASS.getNumber()){
			//通过
			tenant.setAuditStatus(TenantAuditStatus.MANUAL_THROUGH.getNumber());
			this.updateById(tenant);
		}else if (operation != null && operation == TenantOperation.REFUSE.getNumber()) {
			//拒绝
			tenant.setAuditStatus(TenantAuditStatus.NOTAPPROVED.getNumber());
			this.updateById(tenant);
		}else{
			//操作机构失败
			throw ExceptionFactory.create("G_012");
		};
		
	}
	
	@Override
	public void importExcelTenant(TenantExcelBo tenant,AuthUser currentUser) {
		SysTenant sysTenant = BeanMapper.map(tenant, SysTenant.class);
		sysTenant.setCreateBy(currentUser.getId());
		sysTenant.setCreateTime(new Date());
		sysTenant.setParentId(SysConstants.AEK_TENANT_ID); // 导入默认父机构为AEK
		sysTenant.setSubTenant(0);
		sysTenant.setSubTenantLimit(0);
		sysTenant.setTenantType(TenantType.HOSPITAL.getNumber());
		sysTenant.setTrial(1);
		sysTenant.setUserLimit(0);
		sysTenant.setOrigin(TenantOrigin.BACK_CREATE.getNumber());
		sysTenant.setAuditStatus(TenantAuditStatus.THROUGH.getNumber());
		sysTenant.setNotify(true);
		sysTenant.setCommercialUse(1);
		SysTenant parentTenant = this.selectById(sysTenant.getParentId());
		this.initParentIds(sysTenant, parentTenant);
		this.disposeCustomData(sysTenant);
		this.insert(sysTenant);

		// 初始化模块
		this.tenantModuleService.initTenantModule(sysTenant.getId());

		// 初始化机构角色信息
		this.roleService.initTenantRole(sysTenant.getId());

		// 初始化机构部门信息(根节点默认与机构名称相同)
		this.deptService.initTenantDept(sysTenant);

		SysUser admin = tenant.getAdmin();
		admin.setPassword("12345678"); // 默认密码
		this.userService.initTenantAdminUser(sysTenant, admin,currentUser); // 初始化机构管理员

		// 更新新建机构关联信息
		this.updateById(sysTenant);
	}

	@Override
	public void modifySubTenant(SysTenant tenant, SysUser admin) {
		Date currTime = new Date();
		tenant.setUpdateTime(currTime);

		SysUser updateUser = this.userService.selectById(tenant.getUpdateBy());
		if (null == updateUser) {
			throw ExceptionFactory.create("G_005");
		}

		// 名称唯一检查
		List<SysTenant> tenants = this.findByNameAndType(tenant.getName(), tenant.getTenantType());
		if (CollectionUtils.isNotEmpty(tenants)) {
			for (SysTenant t : tenants) {
				if (!t.getId().equals(tenant.getId())) {
					throw ExceptionFactory.create("O_015");
				}
			}
		}
		if (TenantType.HOSPITAL.getNumber().equals(tenant.getTenantType())) {
			if(StringUtils.isNotEmpty(tenant.getLicense())){
				SysTenant licenseTenant = this.findTenantByLicense(tenant.getLicense());
				if (licenseTenant != null) {
					if (!licenseTenant.getId().equals(tenant.getId())) {
						throw ExceptionFactory.create("O_011");
					}
				}
			}
			HplTenant hplTenant = tenant.getHplTenant();
			if(null != hplTenant){
				List<SysTenant> orgCodeTenants = this.findByOrgCode(hplTenant.getOrgCode());
				if (CollectionUtils.isNotEmpty(orgCodeTenants)) {
					for (SysTenant orgCodeTenant : orgCodeTenants) {
						if (!orgCodeTenant.getId().equals(tenant.getId())) {
							throw ExceptionFactory.create("O_016");
						}
					}
				}
			}
		}

		// 处理子机构限制
		this.disposeSubTenant(tenant);

		// 机构到期时间，级联更新子机构
		//if (updateUser.getAdminFlag()) {
		List<SysTenant> subTenants = this.findAllSubTenant(tenant.getId());
		if (tenant.getExpireTime() != null) {
			if (CollectionUtils.isNotEmpty(subTenants)) {
				for (SysTenant subTenant : subTenants) {
					subTenant.setExpireTime(tenant.getExpireTime());
				}
				this.updateBatchById(subTenants);
			}
		} else {
			if (CollectionUtils.isNotEmpty(subTenants)) {
				for (SysTenant subTenant : subTenants) {
					subTenant.setExpireTime(null);
					this.tenantMapper.updateAllColumnById(subTenant);
				}
			}
			SysTenant expireTenant = this.tenantMapper.selectById(tenant.getId());
			expireTenant.setExpireTime(null);
			this.tenantMapper.updateAllColumnById(expireTenant);
		}
		//}
		
		//处理当前机构与上级行政机构建立关系的时间
		SysTenant oldSysTenant = this.tenantMapper.selectById(tenant.getId());
		long newManageTenantId = tenant.getManageTenantId();
		if(null != oldSysTenant && newManageTenantId != oldSysTenant.getManageTenantId()){
			tenant.setManageTenantTime(currTime);
		}
		
		// 处理机构类型数据
		this.disposeCustomData(tenant);
		this.updateById(tenant);
		if(StringUtils.isBlank(tenant.getLicenseImgUrl())){
			SysTenant sysTenant = this.tenantMapper.selectById(tenant.getId());
			sysTenant.setLicenseImgUrl(null);
			this.tenantMapper.updateAllColumnById(sysTenant);
		}

		// 如果更新了机构名称，则需同步修改用户表中冗余字段tenant_name的值
		if(null != oldSysTenant && StringUtils.isNotBlank(oldSysTenant.getName()) && StringUtils.isNotBlank(tenant.getName()) && !oldSysTenant.getName().equals(tenant.getName())){
			this.userService.updateUserTenantNameByTenantId(tenant.getId(), tenant.getName());
			
			// 修改机构根部门名称
			SysDept rootDept = this.deptService.findRootDeptByTenantId(tenant.getId());
			rootDept.setName(tenant.getName());
			rootDept.setUpdateTime(currTime);
			this.deptService.updateById(rootDept);
			
			// 更新用户部门名称
			this.userService.updateUserDeptNameByDeptId(rootDept.getId(), tenant.getName());
		}
			
		// 创建机构管理员
		if (null != admin) {
			this.userService.initTenantAdminUser(tenant, admin,WebSecurityUtils.getCurrentUser());
		}
	}
	
	@Override
	public void modifySupplierTenant(SysTenant tenant, SysUser admin) {
		SysTenant oldSysTenant = this.tenantMapper.selectById(tenant.getId());
		Date currTime = new Date();
		tenant.setUpdateTime(currTime);

		SysUser updateUser = this.userService.selectById(tenant.getUpdateBy());
		if (null == updateUser) {
			throw ExceptionFactory.create("G_005");
		}

		if(StringUtils.isNotBlank(tenant.getName()) && null != tenant.getTenantType()){
			// 名称唯一检查
			List<SysTenant> tenants = this.findByNameAndType(tenant.getName(), tenant.getTenantType());
			if (CollectionUtils.isNotEmpty(tenants)) {
				for (SysTenant t : tenants) {
					if (!t.getId().equals(tenant.getId())) {
						throw ExceptionFactory.create("O_015");
					}
				}
			}
		}
		
		// 检查是否为供应商类型
		if (null != tenant.getTenantType() && !TenantType.SUPPLIER.getNumber().equals(tenant.getTenantType())) {
			throw ExceptionFactory.create("S_007");
		}
		
		// 处理机构类型数据
		this.disposeCustomData(tenant);
		this.updateById(tenant);
		
		// 如果更新了供应商名称，则需同步修改用户表中冗余字段tenant_name的值
		if(null != oldSysTenant && StringUtils.isNotBlank(oldSysTenant.getName()) && StringUtils.isNotBlank(tenant.getName()) && !oldSysTenant.getName().equals(tenant.getName())){
			this.userService.updateUserTenantNameByTenantId(tenant.getId(), tenant.getName());
			// 修改机构根部门名称
			SysDept rootDept = this.deptService.findRootDeptByTenantId(tenant.getId());
			rootDept.setName(tenant.getName());
			rootDept.setUpdateTime(currTime);
			this.deptService.updateById(rootDept);
			
			// 更新用户部门名称
			this.userService.updateUserDeptNameByDeptId(rootDept.getId(), tenant.getName());
		}
		
		//处理供应商证件信息
		List<SysSupplierTenantCredentials> supplierCredentials = tenant.getSupplierCredentials();
		if(null != supplierCredentials){
			for (SysSupplierTenantCredentials sysSupplierTenantCredentials : supplierCredentials) {
				sysSupplierTenantCredentials.setTenantId(tenant.getId());
				if(null != sysSupplierTenantCredentials){
					if(null != sysSupplierTenantCredentials.getId()){
						supplierTenantCredentialsService.updateById(sysSupplierTenantCredentials);
						SysSupplierTenantCredentials supplierTenantCredentials = supplierTenantCredentgialsMapper.selectById(sysSupplierTenantCredentials.getId());
						if(StringUtils.isBlank(sysSupplierTenantCredentials.getCode())){
							supplierTenantCredentials.setCode(null);
						}
						if(StringUtils.isBlank(sysSupplierTenantCredentials.getImageUrl())){
							supplierTenantCredentials.setImageUrl(null);
						}
						supplierTenantCredentgialsMapper.updateAllColumnById(supplierTenantCredentials);
					}else{
						supplierTenantCredentialsService.insert(sysSupplierTenantCredentials);
					}
				}
			}
		}

		// 创建机构管理员
		if (null != admin) {
			this.userService.initTenantAdminUser(tenant, admin,WebSecurityUtils.getCurrentUser());
		}
	}

	/**
	 * 初始化parentIds
	 * 
	 * @param tenant
	 *            当前机构
	 * @param parentTenant
	 *            父机构
	 */
	private void initParentIds(SysTenant tenant, SysTenant parentTenant) {
		String parentIds = null;
		if (parentTenant == null) {
			tenant.setParentId(SysConstants.ROOT_PARENT);
			tenant.setParentIds(SysConstants.ROOT_PARENT_STR);
		} else {
			parentIds = parentTenant.getParentIds() + "," + tenant.getParentId();
			tenant.setParentId(parentTenant.getId());
			tenant.setParentIds(parentIds);
		}
	}
	

	/**
	 * 处理子机构创建数量限制
	 * 
	 * @param tenant
	 */
	private void disposeSubTenant(SysTenant tenant) {
		SysTenant parentTenant = null;

		// 创建
		if (null == tenant.getId()) {
			parentTenant = this.selectById(tenant.getParentId());

			if (parentTenant == null) {
				//上级机构不存在
				throw ExceptionFactory.create("O_002");
			}

			// 判断父机构创建子机构的限制次数
			int tenantNum = (tenant.getSubTenantLimit() == null ? 0 : tenant.getSubTenantLimit()) + 1; // 加上当前机构
			int parentSpareNum = parentTenant.getSubTenantLimit() - parentTenant.getSubTenant(); // 父机构剩余创建数量
			if (parentSpareNum < tenantNum) {
				//创建子机构数量不足
				throw ExceptionFactory.create("O_006");
			}
			parentTenant.setSubTenant(parentTenant.getSubTenant() + tenantNum);

		} else {
			// 当前机构原始信息
			SysTenant beforeTenant = this.selectById(tenant.getId());

			// 父机构
			parentTenant = this.selectById(beforeTenant.getParentId());

			// 是否修改创建子机构限制
			if (tenant.getSubTenantLimit() != null) {
				if (!(beforeTenant.getSubTenantLimit().equals(tenant.getSubTenantLimit()))) {

					// 当前机构已创建子租户大于修改创建限制
					if (beforeTenant.getSubTenant() > tenant.getSubTenantLimit()) {
						throw ExceptionFactory.create("O_009", String.valueOf(beforeTenant.getSubTenant()));
					}

					// 本次使用创建数量
					int tenantNum = tenant.getSubTenantLimit() + 1;

					// 上次使用创建数量
					int beforeTenantNum = beforeTenant.getSubTenantLimit() + 1;

					// 父机构原始已创建数量
					int parentBeforeTenantNum = parentTenant.getSubTenant() - beforeTenantNum;

					// 父机构剩余创建数量
					int parentSpareNum = parentTenant.getSubTenantLimit() - parentBeforeTenantNum;

					if (parentSpareNum < tenantNum) {
						throw ExceptionFactory.create("O_006");
					}

					parentTenant.setSubTenant(parentBeforeTenantNum + tenantNum);
				}
			}
		}

		// 更新父机构
		this.updateById(parentTenant);
	}

	/**
	 * 处理机构类型数据
	 * 
	 * @param tenant
	 */
	private void disposeCustomData(SysTenant tenant) {

		Integer tenantType = tenant.getTenantType();
		// 判断机构类型
		if (TenantType.HOSPITAL.getNumber().equals(tenantType)) {
			tenant.setCustomData(JSON.toJSONString(tenant.getHplTenant()));
		}
		if (TenantType.SUPERVISE.getNumber().equals(tenantType)) {
			tenant.setCustomData(JSON.toJSONString(tenant.getSuperviseTenant()));
		}
		if (TenantType.SUPPLIER.getNumber().equals(tenantType)){
			tenant.setCustomData(JSON.toJSONString(tenant.getSupplierTenant()));
		}
	}

	@Override
	public List<SysTenant> findAllSubTenant(Long tenantId) {
		SysTenant tenant = this.selectById(tenantId);

		Wrapper<SysTenant> wrapper = new EntityWrapper<SysTenant>();
		wrapper.eq("del_flag", false).like("parent_ids", tenant.getParentIds() + "," + tenant.getId(),
				SqlLike.RIGHT).eq("enable", true);

		return this.selectList(wrapper);
	}
	
	@Override
	public List<SysTenantRoleVo> findAllRoleSubTenant(Long tenantId,String parentIds) {
		return tenantMapper.findAllRoleSubTenant(tenantId, parentIds);
	}

	@Override
	public void stopTenantById(Long tenantId) {
		Date currTime = new Date();
		SysTenant tenant = this.selectById(tenantId);
		tenant.setEnable(false);
		tenant.setUpdateTime(currTime);
		this.tenantMapper.updateById(tenant);
		// 查询该机构下的所有子机构
		List<SysTenant> subTenants = findAllSubTenant(tenantId);
		// 停用当前机构所有子机构
		for (SysTenant sysTenant : subTenants) {
			sysTenant.setEnable(false);
			sysTenant.setUpdateTime(currTime);
			this.tenantMapper.updateById(sysTenant);
		}
		String token = WebSecurityUtils.getCurrentToken();
		new Thread() {
			@Override
			public void run() {
				logger.info("=================清除当前机构[tenantId=" + tenantId + "]用户缓存开始=========================");
				// 清除当前租户及其子租户所有用户缓存
				Map<String, Object> clearCurrentTenantCacheResult = authClientService.removePermsByTenant(tenantId,
						token);
				logger.info("=================清除当前机构[tenantId=" + tenantId + "]用户缓存结果="
						+ clearCurrentTenantCacheResult.toString() + "=========================");

			}
		}.start();
	}

	@Override
	public void deleteTenantById(Long tenantId) {
		Date currTime = new Date();
		SysTenant tenant = this.selectById(tenantId);
		tenant.setDelFlag(true);
		tenant.setUpdateTime(currTime);
		this.tenantMapper.updateById(tenant);
		
		/*List<SysUser> admins = new ArrayList<SysUser>();
		if(null != tenant && null != tenant.getAdminId()){
			SysUser admin = this.userService.selectById(tenant.getAdminId());
			if(null != admin){
				admin.setDelFlag(true);
				admin.setUpdateTime(currTime);
				admins.add(admin);
			}
		}*/
		//删除该机构下所有用户
		List<SysUser> admins = userService.findUserListByTenantId(tenant.getId());
		for (SysUser sysUser : admins) {
			sysUser.setDelFlag(true);
			sysUser.setUpdateTime(currTime);
		}
		
		List<SysTenant> list = this.findAllSubTenant(tenantId);
		if (CollectionUtils.isNotEmpty(list)) {
			for (SysTenant t : list) {
				t.setDelFlag(true);
				t.setUpdateTime(currTime);
				
				if(null != t && null != t.getAdminId()){
					SysUser admin = userService.selectById(t.getAdminId());
					if(null != admin){
						admin.setDelFlag(true);
						admin.setUpdateTime(currTime);
						admins.add(admin);
					}
				}
			}
			this.updateBatchById(list);
		}
		
		//批量删除机构及其子机构管理员
		if(admins.size() > 0){
			this.userService.updateBatchById(admins);
		}

		String token = WebSecurityUtils.getCurrentToken();
		new Thread() {
			@Override
			public void run() {
				logger.info("=================清除当前机构[tenantId=" + tenantId + "]用户缓存开始=========================");
				// 清除当前租户及其子租户所有用户缓存
				Map<String, Object> clearCurrentTenantCacheResult = authClientService.removePermsByTenant(tenantId,
						token);
				logger.info("=================清除当前机构[tenantId=" + tenantId + "]用户缓存结果="
						+ clearCurrentTenantCacheResult.toString() + "=========================");
			}
		}.start();
	}

	@Override
	public void recoverTenantById(Long tenantId,Date expireTime) {
		//设置机构有效时间(expireTime值为null,永久有效)
		if(expireTime != null){	
			SysTenant tenant = this.selectById(tenantId);
			tenant.setExpireTime(expireTime);
			tenant.setEnable(true);
			tenant.setUpdateTime(new Date());			
			this.tenantMapper.updateById(tenant);
		}else {
			SysTenant tenant = this.selectById(tenantId);
			tenant.setExpireTime(null);
			tenant.setEnable(true);
			tenant.setUpdateTime(new Date());
			this.tenantMapper.updateAllColumnById(tenant);
		}
	}

	@Override
	public void delayTenant(Long updateBy, Long tenantId, Integer days) {
		SysUser user = this.userService.selectById(updateBy);

		// 只有AEK 用户可操作
		if (!user.getAdminFlag()) {
			throw ExceptionFactory.create("G_010");
		}
		SysTenant tenant = this.selectById(tenantId);

		// null 为永久有效不需要操作
		if (tenant.getExpireTime() == null) {
			throw ExceptionFactory.create("O_010");
		}

		if (days != null && days > 0) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(tenant.getExpireTime());
			int day = calendar.get(Calendar.DATE);
			calendar.set(Calendar.DATE, day + days);

			Date expireTime = calendar.getTime();
			tenant.setExpireTime(expireTime);

			// 级联更新子机构
			List<SysTenant> subTenants = this.findAllSubTenant(tenantId);
			if (CollectionUtils.isNotEmpty(subTenants)) {
				for (SysTenant subTenant : subTenants) {
					subTenant.setExpireTime(expireTime);
				}
			}
			subTenants.add(tenant);
			this.updateBatchById(subTenants);
		}
	}

	@Override
	public Page<SysTenant> findPageByKeyword(TenantQuery query) {
		Page<SysTenant> page = query.getPage();

		Wrapper<SysTenant> wrapper = new EntityWrapper<SysTenant>();
		wrapper.eq("parent_id", query.getTenantId()).eq("del_flag", false);

		if (query.getTrial() != null) {
			wrapper.eq("trial", query.getTrial());
		}

		if (query.getOrigin() != null) {
			wrapper.eq("origin", query.getOrigin());
		}

		if (query.getAuditStatus() != null) {
			wrapper.eq("audit_status", query.getAuditStatus());
		}

		if (query.getTenantType() != null) {
			wrapper.eq("tenant_type", query.getTenantType());
		}

		if (query.getCommercialUse() != null) {
			wrapper.eq("commercial_use", query.getCommercialUse());
		}

		if (query.getEnable() != null) {
			wrapper.eq("enable", query.getEnable());
		}
		
		if(query.getServiceType() != null){
			wrapper.eq("custom_data->'$.serviceType'", query.getServiceType());
		}

		String keyword = StringUtils.trimToNull(query.getKeyword());

		if (keyword != null) {
			if (keyword.startsWith("%") || keyword.startsWith("[") || keyword.startsWith("[]")
					|| keyword.startsWith("_")) {
				wrapper.andNew("name LIKE {0} OR id LIKE {0}", "%[" + keyword + "]%");// "
																						// escape
																						// '/'";
			} else {
				wrapper.andNew("name LIKE {0} OR id LIKE {0}", "%" + keyword + "%");
			}
		}
		wrapper.orderBy("createTime", false);
		return this.selectPage(page, wrapper);
	}
	
	@Override
	public Page<SysTenant> findSupplierPageByKeyword(TenantQuery query) {
		Page<SysTenant> page = query.getPage();

		Wrapper<SysTenant> wrapper = new EntityWrapper<SysTenant>();
		wrapper.eq("del_flag", false);

		if (query.getTrial() != null) {
			wrapper.eq("trial", query.getTrial());
		}

		if (query.getOrigin() != null) {
			wrapper.eq("origin", query.getOrigin());
		}

		if (query.getAuditStatus() != null) {
			wrapper.eq("audit_status", query.getAuditStatus());
		}

		if (query.getTenantType() != null) {
			wrapper.eq("tenant_type", query.getTenantType());
		}

		if (query.getCommercialUse() != null) {
			wrapper.eq("commercial_use", query.getCommercialUse());
		}

		if (query.getEnable() != null) {
			wrapper.eq("enable", query.getEnable());
		}
		
		if(query.getServiceType() != null){
			wrapper.eq("custom_data->'$.serviceType'", query.getServiceType());
		}

		String keyword = StringUtils.trimToNull(query.getKeyword());

		if (keyword != null) {
			if (keyword.startsWith("%") || keyword.startsWith("[") || keyword.startsWith("[]")
					|| keyword.startsWith("_")) {
				wrapper.andNew("name LIKE {0} OR id LIKE {0}", "%[" + keyword + "]%");// "
																						// escape
																						// '/'";
			} else {
				wrapper.andNew("name LIKE {0} OR id LIKE {0}", "%" + keyword + "%");
			}
		}
		wrapper.orderBy("createTime", false);
		return this.selectPage(page, wrapper);
	}
	
	@Override
	public List<SysTenant> getManageTree(Long tenantId) {
		Wrapper<SysTenant> wrapper = new EntityWrapper<SysTenant>();
		SysTenant sysTenant = null;
		SysTenant sysTenantBo = null;
		
		if(WebSecurityUtils.getCurrentUser() == null){
			//登录后再试
			throw ExceptionFactory.create("L_010");
		};
		//根据当前等登录人得到id
		Long loginId = WebSecurityUtils.getCurrentUser().getId();		
		//根据登录人得到他所在机构
		sysTenantBo = this.selectById(userService.selectById(loginId).getTenantId());
		if(sysTenantBo.getTenantType() == 1 || sysTenantBo.getId() == 2){
			//您没有权限查看此模块
			throw ExceptionFactory.create("O_019");
		};

		sysTenant = this.selectById(tenantId);
				
		List<SysTenant> tenants = Lists.newArrayList();
		
		if(sysTenant != null){
			if(sysTenant.getTenantType() == 0){
				//爱怡康
				wrapper .eq("tenant_type", TenantType.SUPERVISE.getNumber())
					.eq("del_flag",0)
			   	   .and("(expire_time is null or expire_time >= NOW())")
			       .and("audit_status in (2,3)")
			       .orderBy("id",false);
				tenants = this.selectList(wrapper);
				
				if (tenants.size() > 0) {
					//n=0爱怡康，1为省级医疗装备监管中心,2为市级医疗装备监管机构,3为区/县级医疗装备监管机构
					int n = 0;	
					for (SysTenant tenant : tenants) {
						tenant.setParentTenantRank(n);
					};					
				}
				return tenants;
				
			}else if (sysTenant.getTenantType() == 2) {
				//查监管的监管机构,默认的限制条件[类型医疗机构,通过,未过期]
				wrapper.eq("manage_tenant_id", sysTenant.getId())
						.eq("del_flag",0)
					   .eq("tenant_type", TenantType.SUPERVISE.getNumber())
				   	   .and("(expire_time is null or expire_time >= NOW())")
				       .and("audit_status in (2,3)")
				   	   .orderBy("id",false);
				
				tenants = this.selectList(wrapper);
				
				//当监管机构为区县
				if(tenants.size() == 0){
					Long manageTenantId = sysTenant.getManageTenantId();			
					//查询次数
					int n = 1;			
					//循环查直至上级行政机构为id为2(国家卫计委id=2)
					for (Long i=manageTenantId;i>2;) {
						i = selectById(i).getManageTenantId();
						n++;
					};
					
					List<SysTenant> tenantsSub = Lists.newArrayList();
					if(n == 3){
						SysTenant sysTenantSub = new SysTenant();
						sysTenantSub.setPlaceName(sysTenant.getCounty());
						sysTenantSub.setParentTenantRank(3);
						tenantsSub.add(sysTenantSub);
						return tenantsSub;
					};
					
					if(n == 2){
						SysTenant sysTenantSub = new SysTenant();
						sysTenantSub.setPlaceName(sysTenant.getCity());
						sysTenantSub.setParentTenantRank(2);
						tenantsSub.add(sysTenantSub);
						return tenantsSub;
					};
					
					if(n == 1){
						SysTenant sysTenantSub = new SysTenant();
						sysTenantSub.setPlaceName(sysTenant.getProvince());
						sysTenantSub.setParentTenantRank(1);
						tenantsSub.add(sysTenantSub);
						return tenantsSub;
					};
					
				};
				
				//检测机构等级[0爱怡康,1为省级医疗装备监管中心,2为市级医疗装备监管机构,3为区/县级医疗装备监管机构]
				if(tenants != null && tenants.size() > 0){
					Long manageTenantId = sysTenant.getManageTenantId();			
					//查询次数
					int n = 1;			
					//循环查直至上级行政机构为id为2(国家卫计委id=2)
					for (Long i=manageTenantId;i>2;) {
						i = selectById(i).getManageTenantId();
						n++;
					};	
					
					if(n == 0){
						//0爱怡康,n=1为省级医疗装备监管中心,2为市级医疗装备监管机构,3为区/县级医疗装备监管机构
						for (SysTenant tenant : tenants) {
							tenant.setParentTenantRank(n);
							tenant.setPlaceName("爱怡康");
						};
					}else if (n == 1) {
						for (SysTenant tenant : tenants) {
							tenant.setParentTenantRank(n);
							tenant.setPlaceName(tenant.getProvince());
						};
					}else if (n == 2) {
						for (SysTenant tenant : tenants) {
							tenant.setParentTenantRank(n);
							tenant.setPlaceName(tenant.getCity());
						};
					}else if (n == 3) {
						for (SysTenant tenant : tenants) {
							tenant.setParentTenantRank(n);
							tenant.setPlaceName(tenant.getCounty());
						};
					};
				};
				return tenants;	
				
			};
		};
		
		
		return tenants;
	}
	
	@Override
	public List<SysTenant> getManageTreeForCms(Long tenantId) {
		List<SysTenant> list = Lists.newArrayList();
		Wrapper<SysTenant> wrapper = new EntityWrapper<SysTenant>();
		//查监管的监管机构,默认的限制条件[类型监管机构,通过,未过期]
		wrapper.eq("manage_tenant_id", tenantId)
			   .eq("del_flag",0)
			   .eq("enable", 1)
			   .eq("tenant_type", TenantType.SUPERVISE.getNumber())
		   	   .and("(expire_time is null or expire_time >= NOW())")
		       .and("audit_status in (2,3)")
		   	   .orderBy("id",false);
		List<SysTenant> listBo = selectList(wrapper);
		list.addAll(listBo);
		if(CollectionUtils.isNotEmpty(listBo)){
			for (SysTenant subTenant : listBo) {
				Wrapper<SysTenant> wrapper2 = new EntityWrapper<SysTenant>();
				//查监管的监管机构,默认的限制条件[类型监管机构,通过,未过期]
				wrapper2.eq("manage_tenant_id", subTenant.getId())
					   .eq("del_flag",0)
					   .eq("enable", 1)
					   .eq("tenant_type", TenantType.SUPERVISE.getNumber())
				   	   .and("(expire_time is null or expire_time >= NOW())")
				       .and("audit_status in (2,3)")
				   	   .orderBy("id",false);
				List<SysTenant> listBo2 = selectList(wrapper2);
				list.addAll(listBo2);
			}
		}
		
		return list;
	}
	
	@Override
	public List<SysTenant> getManageTreeForCmsHelp(Long tenantId) {
		List<SysTenant> list = Lists.newArrayList();
		Wrapper<SysTenant> wrapper = new EntityWrapper<SysTenant>();
		//查监管的监管机构,默认的限制条件[类型监管机构,通过,未过期]
		wrapper.eq("manage_tenant_id", tenantId)
			   .eq("del_flag",0)
			   .eq("enable", 1)
			   .eq("tenant_type", TenantType.SUPERVISE.getNumber())
		   	   .and("(expire_time is null or expire_time >= NOW())")
		       .and("audit_status in (2,3)")
		   	   .orderBy("id",false);
		List<SysTenant> listBo = selectList(wrapper);
		list.addAll(listBo);
		if(CollectionUtils.isNotEmpty(listBo)){
			for (SysTenant subTenant : listBo) {
				Wrapper<SysTenant> wrapper2 = new EntityWrapper<SysTenant>();
				//查监管的监管机构,默认的限制条件[类型监管机构,通过,未过期]
				wrapper2.eq("manage_tenant_id", subTenant.getId())
					   .eq("del_flag",0)
					   .eq("enable", 1)
					   .eq("tenant_type", TenantType.SUPERVISE.getNumber())
				   	   .and("(expire_time is null or expire_time >= NOW())")
				       .and("audit_status in (2,3)")
				   	   .orderBy("id",false);
				List<SysTenant> listBo2 = selectList(wrapper2);
				list.addAll(listBo2);
			}
		}
		
		//添加树根节点
		SysTenant root = selectById(tenantId);
		list.add(root);

		//检测机构等级[1为省级医疗装备监管中心,2为市级医疗装备监管机构,3为区/县级医疗装备监管机构]
		if(list != null && list.size() > 0){
			for (SysTenant sysTenant : list) {
				Long manageTenantId = sysTenant.getManageTenantId();			
				//查询次数
				int n = 1;			
				//循环查直至上级行政机构为id为2(国家卫计委id=2)
				for (Long i=manageTenantId;i>2;) {
					i = selectById(i).getManageTenantId();
					n++;
				}	
				
				//n=1为省级医疗装备监管中心,2为市级医疗装备监管机构,3为区/县级医疗装备监管机构
				switch (n) {
				case 1:
					sysTenant.setParentTenantRank(n);
					break;
				case 2:
					sysTenant.setParentTenantRank(n);
					break;
				case 3:
					sysTenant.setParentTenantRank(n);
					break;
				default:
					break;
				}
			}
		}	
			
		return list;
	}
	
	@Override
	public List<WxTenantVo> selectWxTenant(String keyword, Long tenantId) {		
		List<SysTenant> list = Lists.newArrayList();
		Wrapper<SysTenant> wrapper = new EntityWrapper<SysTenant>();
		//查监管的机构,默认的限制条件[通过,未过期]
		wrapper.eq("manage_tenant_id", tenantId)
			   .eq("del_flag",0)
			   .eq("enable", 1)
		   	   .and("(expire_time is null or expire_time >= NOW())")
		       .and("audit_status in (2,3)");		
		List<SysTenant> listBo = selectList(wrapper);
		list.addAll(listBo);
		if(CollectionUtils.isNotEmpty(listBo)){
			for (SysTenant subTenant : listBo) {
				Wrapper<SysTenant> wrapper2 = new EntityWrapper<SysTenant>();
				//查监管的机构,默认的限制条件[通过,未过期]
				wrapper2.eq("manage_tenant_id", subTenant.getId())
					   .eq("del_flag",0)
					   .eq("enable", 1)
				   	   .and("(expire_time is null or expire_time >= NOW())")
				       .and("audit_status in (2,3)");
				List<SysTenant> listBo2 = selectList(wrapper2);
				list.addAll(listBo2);
			}
		}
		if(list != null && list.size()>0){
			if (StringUtils.isNotBlank(keyword)) {
				if (keyword.startsWith("%") || keyword.startsWith("[") || keyword.startsWith("[]") || keyword.startsWith("_")) {
					keyword.replace(keyword,'\\'+keyword);
				}
				List<SysTenant> wxList = Lists.newArrayList();
				for (SysTenant item : list) {
					if(item.getName().contains(keyword)){
						wxList.add(item);
					}
				}
				if(wxList != null && wxList.size()>0){
					List<WxTenantVo> list1 = BeanMapper.mapList(wxList, WxTenantVo.class);			
					Collections.sort(list1, new Comparator<WxTenantVo>() {  
				        Collator collator = Collator.getInstance(Locale.CHINA);  
				        public int compare(WxTenantVo o1, WxTenantVo o2) {  
				            CollationKey key1 = collator.getCollationKey(o1.getName());  
				            CollationKey key2 = collator.getCollationKey(o2.getName());  
				            return key1.compareTo(key2);  
				        }  
				    });				
					return list1;
				}else {
					return new ArrayList<WxTenantVo>();
				}
			}
			List<WxTenantVo> list2 = BeanMapper.mapList(list, WxTenantVo.class);
			Collections.sort(list2, new Comparator<WxTenantVo>() {  
		        Collator collator = Collator.getInstance(Locale.CHINA);  
		        public int compare(WxTenantVo o1, WxTenantVo o2) {  
		            CollationKey key1 = collator.getCollationKey(o1.getName());  
		            CollationKey key2 = collator.getCollationKey(o2.getName());  
		            return key1.compareTo(key2);  
		        }  
		    });
			return list2;
		}
		return new ArrayList<WxTenantVo>();
	}
	
	@Override
	public List<Long> getManageIds(Long tenantId) {
		SysTenant hospital = this.selectById(tenantId);
		List<Long> list = Lists.newArrayList();
		if(hospital != null){
			Long manageTenantId = hospital.getManageTenantId();
			//查询次数
			int n = 1;			
			//循环查直至上级行政机构为id为2(国家卫计委id=2)
			for (Long i=manageTenantId;i>2;) {
				i = selectById(i).getManageTenantId();
				n++;
			};				
			if(n == 2){
				//n=2为省级医疗机构,3为市级医疗机构,4为区/县级医疗机构
				list.add(manageTenantId);
			}else if (n == 3) {
				SysTenant manageCity = selectById(manageTenantId);
				if(manageCity !=null){
					Long manageTenantId2 = manageCity.getManageTenantId();
					SysTenant manageProvince = selectById(manageTenantId2);
					if(manageProvince !=null){
						Long manageTenantId3 = manageProvince.getId();
						list.add(manageTenantId3);
					}
				}
				list.add(manageTenantId);				
			}else if (n == 4) {
				SysTenant manageCounty = selectById(manageTenantId);
				if(manageCounty !=null){
					Long manageTenantId2 = manageCounty.getManageTenantId();
					SysTenant manageCity = selectById(manageTenantId2);
					if(manageCity !=null){
						Long manageTenantId3 = manageCity.getId();
						list.add(manageTenantId3);
						SysTenant manageProvince = selectById(manageCity.getManageTenantId());
						if(manageProvince != null){
							Long manageTenantId4 = manageProvince.getId();
							list.add(manageTenantId4);
						}
					}
				}
				list.add(manageTenantId);
			};
		}
		return list;
	}
	
	@Override
	public List<SysTenant> getHospitalForCms(CmsTenantQuery query) {
		Wrapper<SysTenant> wrapper = new EntityWrapper<SysTenant>();
		if(query.getManageId() == null){
			throw ExceptionFactory.create("O_020");
		}
		if(query.getManageId() != null){
			wrapper.eq("manage_tenant_id", query.getManageId());
		}
		if (query.getType() != null) {
			wrapper.eq("custom_data->'$.category'", query.getType());
		}
		
		wrapper.eq("del_flag",0)
		   .eq("enable", 1)
		   .eq("tenant_type", TenantType.HOSPITAL.getNumber())
	   	   .and("(expire_time is null or expire_time >= NOW())")
	       .and("audit_status in (2,3)");
		
		String keyword = StringUtils.trimToNull(query.getKeyword());

		if (keyword != null) {
			if (keyword.startsWith("%") || keyword.startsWith("[") || keyword.startsWith("[]")
					|| keyword.startsWith("_")) {
				wrapper.andNew("name LIKE {0}", "%[" + keyword + "]%");
			} else {
				wrapper.andNew("name LIKE {0}", "%" + keyword + "%");
			}
		}
		
	    wrapper .orderBy("id",false);
		List<SysTenant> list = selectList(wrapper);
		return list;
	}


	@Override
	public Page<SysTenant> getManageTreeTable(TenantQuery query) {
		Page<SysTenant> page = query.getPage();
		Wrapper<SysTenant> wrapper = new EntityWrapper<SysTenant>();		
		SysTenant sysTenant = null;
		//根据当前等登录人得到id
		Long loginId = WebSecurityUtils.getCurrentUser().getId();
		
		//首次打开监管树
//		if(loginId != null && query.getTenantId() == null){
//			//根据登录人得到他所在监管机构
//			sysTenant = this.selectById(userService.selectById(loginId).getTenantId());
//		}else if (query.getTenantId() != null) {
//			//非首次打开
//			sysTenant = this.selectById(query.getTenantId());
//		};
		
		sysTenant = this.selectById(query.getTenantId());
				
		//同等级并监管的医疗机构,默认的限制条件[类型为医疗机构,通过,未过期,未删除]
		wrapper.eq("manage_tenant_id", sysTenant.getId())
				.eq("del_flag",0)
			   .eq("tenant_type", TenantType.HOSPITAL.getNumber())
		   	   .and("(expire_time is null or expire_time >= NOW())")
		   	   .and("audit_status in (2,3)");

		if (query.getCategory() != null) {
			wrapper.andNew("JSON_EXTRACT(custom_data,'$.category')={0}", query.getCategory());
		}
		
		String keyword = StringUtils.trimToNull(query.getKeyword());

		if (keyword != null) {
			if (keyword.startsWith("%") || keyword.startsWith("[") || keyword.startsWith("[]")
					|| keyword.startsWith("_")) {
				wrapper.andNew("name LIKE {0} OR id LIKE {0}", "%[" + keyword + "]%");
			} else {
				wrapper.andNew("name LIKE {0} OR id LIKE {0}", "%" + keyword + "%");
			}
		}			
		wrapper.orderBy("id", false);
		page = this.selectPage(page, wrapper);
		
		if (page != null) {
			List<SysTenant> tenants = page.getRecords();
			
			if (tenants.size() > 0) {
				//组装机构ids用于查询设备资产
				Long[] tenantIds=null;
				if(CollectionUtils.isNotEmpty(tenants)){
					tenantIds = new Long[tenants.size()];
					for (int i = 0; i < tenantIds.length; i++) {
						SysTenant tempTenant = tenants.get(i);
						tenantIds[i]=tempTenant.getId();
					}
				};
				
				//logger.debug("入参"+tenantIds);
				//调用设备接口查机构设备数
				List<TenantAssetsVo> data=new ArrayList<TenantAssetsVo>();
				Result<List<TenantAssetsVo>> tenantAssets = deviceService.getAssetsByTenantIds(tenantIds, WebSecurityUtils.getCurrentToken());
				
				if(tenantAssets!=null&&tenantAssets.getData().size() > 0){
					logger.debug("入参"+JSON.toJSONString(tenantIds));
					logger.debug("出参"+JSON.toJSONString(tenantAssets.getData()));
				};
				
				
				if(tenantAssets != null && tenantAssets.getData() != null && tenantAssets.getData().size() > 0){
					data = tenantAssets.getData();
				};
						
				for (SysTenant tenant : tenants) {
					if(tenant.getAdminId() != null){
						SysUser admin = userService.selectById(tenant.getAdminId());
						if(admin != null){
							tenant.setAdminName(admin.getRealName());
							tenant.setAdminMobile(admin.getMobile());
						};
					};
					
					HplTenant hplTenant = JSON.parseObject(tenant.getCustomData(),HplTenant.class);
					if(hplTenant != null){
						tenant.setTenantCatogory(hplTenant.getCategory());
					};
					
					if(data != null && data.size() > 0){
						for (TenantAssetsVo item : data) {
							if(tenant.getId().longValue() == item.getTenantId().longValue() ){
								tenant.setDeviceCount(item.getAssetsTotal());
							};

						};
					};				
				};
			};			
		};
		
		return page;
	}

	@Override
	public List<SysTenant> findUserSeeTenant(Long userId) {
		List<SysTenant> tenants = Lists.newArrayList();
		SysUser user = this.userService.selectById(userId);

		// 当前用户拥有admin角色,直接查询全部下级机构（包含停用机构,管理员可以启用该机构）
		SysRole adminRole = this.roleUserService.findUserAdminRole(userId);
		if (adminRole != null) {
			tenants.add(this.selectById(user.getTenantId()));
			tenants.addAll(this.findAllSubTenant(user.getTenantId()));
			return tenants;
		}

		// 查询拥有角色的机构
		Wrapper<SysRoleUser> wrapper = new EntityWrapper<SysRoleUser>();
		wrapper.eq("enable", true).eq("user_id", userId).groupBy("tenant_id");
		List<SysRoleUser> roleUsers = this.roleUserService.selectList(wrapper);

		for (SysRoleUser roleUser : roleUsers) {
			SysTenant tenant = this.selectById(roleUser.getTenantId());
			// 未 停用删除
			if (tenant.getEnable() == true && tenant.getDelFlag() == false) {
				tenants.add(tenant);
			}
		}
		return tenants;
	}

	@Override
	public String findUserCurrUpTenant(Long tenantId, Long userId) {
		SysUser user = this.userService.selectById(userId);
		SysTenant tenant = this.selectById(tenantId);
		String str = "";
		if(null != tenant){
			str = "/" + tenant.getName();
			if (!tenantId.equals(user.getTenantId())) {
				str = findUserCurrUpTenant(tenant.getParentId(), user.getId()) + str;
			}
		}
		return str;
	}

	@Override
	public SysTenant findTenantByLicense(String license) {
		Wrapper<SysTenant> wrapper = new EntityWrapper<SysTenant>();
		wrapper.eq("license", license).eq("del_flag", false);
		return this.selectOne(wrapper);
	}

	@Override
	public boolean isTenantNameExist(String tenantName) {
		Wrapper<SysTenant> wrapper = new EntityWrapper<SysTenant>();
		wrapper.eq("name", tenantName);
		return this.selectCount(wrapper) > 0;
	}

	@Override
	public SysTenant findByTenantName(String tenantName) {
		Wrapper<SysTenant> wrapper = new EntityWrapper<SysTenant>();
		wrapper.eq("name", tenantName).eq("del_flag", false);
		return this.selectOne(wrapper);
	}

	@Override
	public List<SysTenant> findAllSuperviseByEveryLevel() {
		List<SysTenant> allSubTenant = Lists.newArrayList();
		Wrapper<SysTenant> wrapper = new EntityWrapper<SysTenant>();
		wrapper.eq("manage_tenant_id", SysConstants.AEK_TENANT_ID); // root监管，默认为aek下级
		SysTenant supervise = this.selectOne(wrapper);

		if (supervise != null) {
			allSubTenant.add(supervise);
			List<SysTenant> subTenant = this.findSuperviseByRecursive(supervise.getId());
			if (CollectionUtils.isNotEmpty(subTenant)) {
				allSubTenant.addAll(subTenant);
			}
		}
		return allSubTenant;
	}

	@Override
	public List<SysTenant> findSuperviseByRecursive(Long manageTenatId) {
		List<SysTenant> allSubTenant = Lists.newArrayList();

		Wrapper<SysTenant> wrapper = new EntityWrapper<SysTenant>();
		wrapper.eq("manage_tenant_id", manageTenatId);
		wrapper.eq("tenant_type", TenantType.SUPERVISE.getNumber());
		wrapper.eq("trial", 1);
		wrapper.eq("del_flag", false);
		List<SysTenant> subTenants = this.selectList(wrapper);

		if (CollectionUtils.isNotEmpty(subTenants)) {
			for (SysTenant tenant : subTenants) {
				allSubTenant.add(tenant);
				List<SysTenant> subTenant = this.findSuperviseByRecursive(tenant.getId());
				if (CollectionUtils.isNotEmpty(subTenant)) {
					allSubTenant.addAll(subTenant);
				}
			}
		}
		return allSubTenant;
	}

	@Override
	public List<Long> findAllSubHplTenantIds(Long manageTenatId) {
		List<Long> allSubManageTenantIds = Lists.newArrayList();

		List<SysTenant> allSubManageTenant = this.findAllSubHplTenant(manageTenatId);
		if (CollectionUtils.isNotEmpty(allSubManageTenant)) {
			for (SysTenant tenant : allSubManageTenant) {
				allSubManageTenantIds.add(tenant.getId());
			}
		}
		allSubManageTenantIds.add(manageTenatId);
		return allSubManageTenantIds;
	}

	@Override
	public List<SysTenant> findAllSubHplTenant(Long manageTenatId) {
		List<SysTenant> superviseTenant = this.findSuperviseByRecursive(manageTenatId);

		Wrapper<SysTenant> wrapper = new EntityWrapper<SysTenant>();
		wrapper.eq("tenant_type", TenantType.HOSPITAL.getNumber());
		wrapper.eq("del_flag", false);
		wrapper.eq("enable", true);

		if (CollectionUtils.isNotEmpty(superviseTenant)) {
			// 该监管有下级，查询所有下级监管医疗机构
			StringBuilder builder = new StringBuilder();
			for (SysTenant tenant : superviseTenant) {
				builder.append(tenant.getId()).append(",");
			}
			builder.append(manageTenatId);
			wrapper.in("manage_tenant_id", builder.toString());

		} else {
			// 没有下级只查询当前监管机构
			wrapper.eq("manage_tenant_id", manageTenatId);
		}

		return this.selectList(wrapper);
	}

	@Override
	public List<SysTenantCensusVo> findTenantCensusByTime(Long tenantId, Date startTime, Date endTime) {
		// 判断查询平台机构数量或者监管机构监管医疗机构数量
		boolean flag = tenantId == SysConstants.AEK_TENANT_ID;

		Date now = new Date();
		if (endTime.after(now)) {
			endTime = now;
		} else {
			// 包含结束时间的那天
			Calendar endCalendar = Calendar.getInstance();
			endCalendar.setTime(endTime);
			endCalendar.add(Calendar.DATE, 1);
			endTime = endCalendar.getTime();
		}

		List<SysTenant> manageTenants = Lists.newArrayList();
		if (!flag) {
			// 查询所有下级子集机构
			manageTenants = this.findSuperviseByRecursive(tenantId);
			SysTenant tenant = this.selectById(tenantId);
			manageTenants.add(tenant);
		}

		List<SysTenantCensusVo> census = Lists.newArrayList();
		//int tenantNum = 0;
		while (startTime.before(endTime)) {
			int tenantNumByDay = 0;
			if (flag) {
				tenantNumByDay = this.findTenantCensusByDayWithPlatform(startTime);
			} else {
				tenantNumByDay = this.findTenantCensusByDayWithManageTenant(manageTenants, startTime);
			}

			//tenantNum += tenantNumByDay; // 增量加

			SysTenantCensusVo vo = new SysTenantCensusVo();
			vo.setDate(startTime);
			vo.setCount(tenantNumByDay);
			census.add(vo);

			Calendar calendar = Calendar.getInstance();
			calendar.setTime(startTime);
			calendar.add(Calendar.DATE, 1); // 开始时间天 + 1
			startTime = calendar.getTime();
		}
		return census;
	}

	/**
	 * 根据时间查询平台创建的机构数量
	 * 
	 * @param day
	 * @return
	 */
	private int findTenantCensusByDayWithPlatform(Date day) {
		Wrapper<SysTenant> wrapper = new EntityWrapper<SysTenant>();
		wrapper.eq("tenant_type", TenantType.HOSPITAL.getNumber());
		
		//wrapper.eq("DATE_FORMAT(create_time,'%Y-%m-%d')", DateUtil.format(day));
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(day);
		calendar.add(Calendar.DATE, 1); // 开始时间天 + 1
		day = calendar.getTime();
		wrapper.lt("create_time", DateUtil.format(day, DateUtil.DATE_PATTERN.YYYY_MM_DD_HH_MM_SS));
		
		wrapper.eq("del_flag", false);
		wrapper.eq("enable", true);
		return this.selectCount(wrapper);
	}

	/**
	 * 根据时间查询当前监管机构所创建的机构数量
	 * 
	 * @param manageTenants
	 * @param day
	 * @return
	 */
	private Integer findTenantCensusByDayWithManageTenant(List<SysTenant> manageTenants, Date day) {
		Wrapper<SysTenant> wrapper = new EntityWrapper<SysTenant>();
		wrapper.eq("tenant_type", TenantType.HOSPITAL.getNumber());
		
		//wrapper.eq("DATE_FORMAT(create_time,'%Y-%m-%d')", DateUtil.format(day));
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(day);
		calendar.add(Calendar.DATE, 1); // 开始时间天 + 1
		day = calendar.getTime();
		wrapper.lt("manage_tenant_time", DateUtil.format(day, DateUtil.DATE_PATTERN.YYYY_MM_DD_HH_MM_SS));
		
		wrapper.eq("del_flag", false);
		wrapper.eq("enable", true);

		if (CollectionUtils.isNotEmpty(manageTenants)) {
			// 该监管有下级，查询所有下级监管医疗机构
			StringBuilder builder = new StringBuilder();
			for (SysTenant tenant : manageTenants) {
				builder.append(tenant.getId()).append(",");
			}
			builder.deleteCharAt(builder.length() - 1);
			wrapper.in("manage_tenant_id", builder.toString());
		} else {
			return 0;
		}
		return this.selectCount(wrapper);
	}

	@Override
	public int findPlatformHplTenantCount() {
		Wrapper<SysTenant> wrapper = new EntityWrapper<SysTenant>();
		wrapper.eq("enable", true).eq("del_flag", false).eq("tenant_type", TenantType.HOSPITAL.getNumber());
		return this.selectCount(wrapper);
	}

	@Override
	public int findHplTenantByManageTenantId(Long manageTenantId) {
		List<SysTenant> tenants = this.findAllSubHplTenant(manageTenantId);
		return CollectionUtils.isNotEmpty(tenants) ? tenants.size() : 0;
	}

	@Override
	public List<SysTenant> findByTenantType(Integer type) {
		Wrapper<SysTenant> wrapper = new EntityWrapper<SysTenant>();
		wrapper.eq("tenant_type", type);
		return this.selectList(wrapper);
	}
	
	public List<SysTenant> findByManageTenantId(Long tenantId) {
		Wrapper<SysTenant> wrapper = new EntityWrapper<SysTenant>();
		wrapper.eq("manage_tenant_id", tenantId).eq("del_flag", false);
		return this.selectList(wrapper);
	}

	@Override
	public List<SysTenant> findByNameAndType(String name, Integer type) {
		Wrapper<SysTenant> wrapper = new EntityWrapper<SysTenant>();
		wrapper.eq("name", name).eq("tenant_type", type).eq("del_flag", false);
		return this.selectList(wrapper);
	}

	@Override
	public boolean isRepeatByNameAndType(String name, Integer type) {
		List<SysTenant> tenants = this.findByNameAndType(name, type);
		return tenants != null && tenants.size() > 0;
	}

	@Override
	public List<SysTenant> findByOrgCode(String orgCode) {
		return this.baseMapper.selectByOrgCode(orgCode);
	}

	@Override
	public List<SysTenant> findExpiredTenant() {
		return tenantMapper.selectExpiredTenant();
	}

	@Override
	public List<SysTenant> findExpiredTenantBetweenDate(Date beginDate, Date endDate) {
		return tenantMapper.selectExpiredTenantBetweenDate(beginDate,endDate);
	}

	@Override
	public List<Long> findAllHplTenantIds() {
		List<Long> allHplTenantIds = Lists.newArrayList();
		List<SysTenant> allHplTenants = this.findAllHplTenant();
		if (CollectionUtils.isNotEmpty(allHplTenants)) {
			for (SysTenant tenant : allHplTenants) {
				allHplTenantIds.add(tenant.getId());
			}
		}
		return allHplTenantIds;
	}

	@Override
	public List<SysTenant> findAllHplTenant() {
		Wrapper<SysTenant> wrapper = new EntityWrapper<SysTenant>();
		wrapper.eq("tenant_type", TenantType.HOSPITAL.getNumber());
		wrapper.eq("del_flag", false);
		wrapper.eq("enable", true);
		return this.selectList(wrapper);
	}

	@Override
	public List<Long> getManageTenantByTenantId(Long tenantId) {
		List<Long> tenantIds = new ArrayList<>();
		SysTenant tenant = tenantMapper.selectById(tenantId);
		if (null != tenant && null != tenant.getManageTenantId()) {
			Long manageTenantId = tenant.getManageTenantId();
			while (manageTenantId != 2) {
				SysTenant subTenant = tenantMapper.selectById(manageTenantId);
				if (null != subTenant) {
					tenantIds.add(subTenant.getId());
					manageTenantId = subTenant.getManageTenantId();
				} else {
					break;
				}
			}
		}
		return tenantIds;
	}
}
