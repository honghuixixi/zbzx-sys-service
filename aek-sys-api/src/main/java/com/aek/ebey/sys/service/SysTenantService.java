package com.aek.ebey.sys.service;

import java.util.Date;
import java.util.List;

import com.aek.common.core.base.BaseService;
import com.aek.common.core.enums.TenantType;
import com.aek.common.core.serurity.model.AuthUser;
import com.aek.ebey.sys.model.SysSupplierTenant;
import com.aek.ebey.sys.model.SysTenant;
import com.aek.ebey.sys.model.SysUser;
import com.aek.ebey.sys.model.bo.TenantExcelBo;
import com.aek.ebey.sys.model.bo.VerifyCode;
import com.aek.ebey.sys.model.query.TenantQuery;
import com.aek.ebey.sys.model.query.UserQuery;
import com.aek.ebey.sys.model.vo.CmsTenantQuery;
import com.aek.ebey.sys.model.vo.SysTenantCensusVo;
import com.aek.ebey.sys.model.vo.SysTenantRoleVo;
import com.aek.ebey.sys.model.vo.WxTenantVo;
import com.baomidou.mybatisplus.plugins.Page;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author aek
 * @since 2017-05-06
 */
public interface SysTenantService extends BaseService<SysTenant> {

	/**
	 * 上级创建子机构
	 * 
	 * @param tenant
	 *            机构信息
	 * @param admin
	 *            管理员信息(null, 则不创建管理员)
	 *            
	 */
	void createSubTenant(SysTenant tenant, SysUser admin);
	
	/**
	 * 官网平台申请试用创建子机构
	 * 
	 * @param tenant
	 *            机构信息
	 * @param admin
	 *            管理员信息
	 *            
	 * @param verifyCode
	 * 			      申请试用的验证码
	 */
	void createTrialSubTenant(SysTenant tenant, SysUser admin,VerifyCode verifyCode);
	
	/**
	 * zjzx申请试用创建子机构
	 * 
	 * @param tenant
	 *            机构信息
	 * @param admin
	 *            管理员信息
	 *            
	 * @param verifyCode
	 * 			      申请试用的验证码
	 */
	void createTrialSubTenantForZjzx(SysTenant tenant, SysUser admin,VerifyCode verifyCode);
	
	/**
	 * 创建供应商
	 * 
	 * @param tenant
	 *            供应商信息
	 * @param admin
	 *            管理员信息(null, 则不创建管理员)
	 *            
	 */
	void createSupplierTenant(SysTenant tenant, SysUser admin);

	/**
	 * excel 导入机构
	 * 
	 * @param tenant
	 */
	void importExcelTenant(TenantExcelBo tenant,AuthUser currentUser);

	/**
	 * 上级修改子机构
	 * 
	 * @param tenant
	 *            机构信息
	 * 
	 * @param admin
	 *            管理员信息
	 * 
	 */
	void modifySubTenant(SysTenant tenant, SysUser admin);
	
	/**
	 * 修改供应商信息
	 * 
	 * @param tenant
	 *            机构信息
	 * 
	 * @param admin
	 *            管理员信息
	 * 
	 */
	void modifySupplierTenant(SysTenant tenant, SysUser admin);

	/**
	 * 查询所有下属机构(包含子机构的子机构)
	 * 
	 * @param tenantId
	 * @return
	 */
	List<SysTenant> findAllSubTenant(Long tenantId);

	/**
	 * 查询所有下属机构（包含子机构的子机构）
	 * @param tenantId
	 * @return
	 */
	public List<SysTenantRoleVo> findAllRoleSubTenant(Long tenantId,String parentIds);
	
	/**
	 * 分页查询子机构列表
	 * 
	 * @param query
	 *            查询条件
	 * @return
	 */
	Page<SysTenant> findPageByKeyword(TenantQuery query);
	
	/**
	 * 查询监管树树形结构谱
	 * @param tenantId
	 * @return
	 */
	List<SysTenant> getManageTree(Long tenantId);
	
	/**
	 * CMS获取监管树
	 * @param tenantId
	 * @return
	 */
	List<SysTenant> getManageTreeForCms(Long tenantId);
	
	/**
	 * CMS获取监管树
	 * @param tenantId
	 * @return
	 */
	List<SysTenant> getManageTreeForCmsHelp(Long tenantId);
	
	/**
	 * CMS根据医疗机构获取监管机构Ids
	 * @param tenantId
	 * @return
	 */
	List<Long> getManageIds(Long tenantId);
	
	/**
	 *  CMS获取医疗机构
	 * @param query
	 * @return
	 */
	List<SysTenant> getHospitalForCms(CmsTenantQuery query);
	
	/**
	 * 监管树列表分页查询
	 * 
	 * @param query查询条件
	 * @return
	 */
	Page<SysTenant> getManageTreeTable(TenantQuery query);

	/**
	 * 分页查询供应商列表
	 * 
	 * @param query
	 *            查询条件
	 * @return
	 */
	Page<SysTenant> findSupplierPageByKeyword(TenantQuery query);


	/**
	 * 查询用户可见的机构(在该机构拥有角色则可见,机构管理员可见所有子机构)
	 * 
	 * @param userId
	 * @return
	 */
	List<SysTenant> findUserSeeTenant(Long userId);

	/**
	 * 查询机构的所有上级名称
	 * 
	 * @param userId
	 * @param tenantId
	 * @return
	 */
	String findUserCurrUpTenant(Long tenantId, Long userId);

	/**
	 * 根据组织机构代码证查询机构信息
	 * 
	 * @param license
	 * @return
	 */
	SysTenant findTenantByLicense(String license);

	/**
	 * 停用机构
	 * 
	 * @param tenantId
	 */
	void stopTenantById(Long tenantId);
	
	/**
	 * 操作指定机构
	 * @param tenantId 机构id
	 * @param operation 具体操作
	 */
	void operateTenant(Long tenantId,Integer operation);

	/**
	 * 恢复机构
	 * 
	 * @param tenantId
	 */
	void recoverTenantById(Long tenantId,Date expireTime);

	/**
	 * 删除机构(逻辑删除)
	 * 
	 * @param tenantId
	 */
	void deleteTenantById(Long tenantId);

	/**
	 * 机构快速延期
	 * 
	 * @param updateBy
	 *            修改人
	 * @param tenantId
	 *            机构ID
	 * @param days
	 *            天
	 */
	void delayTenant(Long updateBy, Long tenantId, Integer days);

	/**
	 * 机构名称是否存在
	 * 
	 * @param cellValue
	 * @return
	 */
	boolean isTenantNameExist(String tenantName);

	/**
	 * 根基租户名称查询租户信息
	 * 
	 * @param tenantName
	 * @return
	 */
	SysTenant findByTenantName(String tenantName);

	/**
	 * 查询所有监管机构列表，按层级输出
	 * 
	 * @return
	 */
	List<SysTenant> findAllSuperviseByEveryLevel();

	/**
	 * 递归查询当前监管的所有下级及子集监管机构
	 * 
	 * @param manageTenatId
	 *            监管机构ID
	 * @return
	 */
	List<SysTenant> findSuperviseByRecursive(Long manageTenatId);

	/**
	 * 查询当前监管机构所监管下级及子集所有医疗机构ID集合
	 * 
	 * @param manageTenatId
	 *            监管机构ID
	 * @return
	 */
	List<Long> findAllSubHplTenantIds(Long manageTenatId);

	/**
	 * 查询当前监管机构所监管下级及子集所有医疗机构
	 * 
	 * @param manageTenatId
	 *            监管机构ID
	 * @return
	 */
	List<SysTenant> findAllSubHplTenant(Long manageTenatId);

	/**
	 * 根据开始时间，结束时间统计机构创建数量
	 * 
	 * @param tenantId
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	List<SysTenantCensusVo> findTenantCensusByTime(Long tenantId, Date startTime, Date endTime);

	/**
	 * 查询平台医疗机构数量
	 * 
	 * @return
	 */
	int findPlatformHplTenantCount();

	/**
	 * 查询监管机构所监管的医疗机构数量
	 * 
	 * @param manageTenantId
	 *            监管机构ID
	 * @return
	 */
	int findHplTenantByManageTenantId(Long manageTenantId);

	/**
	 * 根据机构类型查询机构 (查询全部不判断删除停用标识)
	 * 
	 * @param type
	 *            机构类型 {@link TenantType}
	 * @return
	 */
	List<SysTenant> findByTenantType(Integer type);
	
	/*
	 * 查询当前监管行政单位的下级
	 * 
	 * @param tenantId
	 * @return
	 */
	List<SysTenant> findByManageTenantId(Long tenantId);

	/**
	 * 根据机构类型和机构名称查询机构
	 * 
	 * @param name
	 *            机构名称
	 * @param type
	 *            机构类型
	 * @return
	 */
	List<SysTenant> findByNameAndType(String name, Integer type);

	/**
	 * 判断是否存在机构名称和类型相同的机构
	 * 
	 * @param name
	 *            机构名称
	 * @param type
	 *            机构类型
	 * @return
	 */
	boolean isRepeatByNameAndType(String name, Integer type);

	/**
	 * 根据医疗机构代码查询机构
	 * 
	 * @param orgCode
	 * @return
	 */
	List<SysTenant> findByOrgCode(String orgCode);
	
	/**
	 * 查询已过期的机构
	 * @return
	 */
	List<SysTenant> findExpiredTenant();
	
	/**
	 * 查询时间段内将过期的机构
	 * @return
	 */
	List<SysTenant> findExpiredTenantBetweenDate(Date beginDate,Date endDate);
	
	/**
	 * 查询平台所有的医疗机构
	 * @return
	 */
	List<Long> findAllHplTenantIds();
	
	/**
	 * 查询平台所有的医疗机构
	 * @return
	 */
	List<SysTenant> findAllHplTenant();

	List<WxTenantVo> selectWxTenant(String keyword,Long tenantId);

	/**
	 * 获取当前机构所有上级行政单位ID
	 * @param tenantId
	 * @return
	 */
	List<Long> getManageTenantByTenantId(Long tenantId);
}
