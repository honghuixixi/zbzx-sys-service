package com.aek.ebey.sys.model;

import com.aek.common.core.base.BaseModel;
import com.aek.ebey.sys.model.custom.HplTenant;
import com.aek.ebey.sys.model.custom.SuperviseTenant;
import com.aek.ebey.sys.model.custom.SupplierTenant;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 医院机构表
 * </p>
 *
 * @author aek
 * @since 2017-05-06
 */
@ApiModel
@TableName("sys_tenant")
public class SysTenant extends BaseModel {

	private static final long serialVersionUID = 1L;

	/**
	 * 名称
	 */
	@ApiModelProperty(value = "机构名称")
	private String name;

	/**
	 * 管理机构ID
	 */
	@ApiModelProperty(value = "管理机构ID")
	@TableField(value = "manage_tenant_id")
	private Long manageTenantId;
	
	/**
	 * 机构与上级行政单位建立关系的时间
	 */
	@ApiModelProperty(value = "机构与上级行政单位建立关系的时间")
	@TableField(value = "manage_tenant_time")
	private Date manageTenantTime;
	
	/**
	 * 租户类型[1=医疗机构,2=监管机构,3=设备供应商,4=设备维修商,5=配件供应商]
	 */
	@ApiModelProperty(value = "租户类型[1=医疗机构,2=监管机构,3=供应商,4=设备维修商,5=配件供应商]")
	@TableField(value = "tenant_type")
	private Integer tenantType;

	/**
	 * 是否试用[0=试用,1=正式]
	 */
	@ApiModelProperty(value = "是否试用[0=试用,1=正式]")
	@TableField(value = "commercial_use")
	private Integer commercialUse;

	/**
	 * 是否测试
	 */
	@ApiModelProperty(value = "是否为测试[0=是,1=不是]")
	private Integer trial;

	/**
	 * 创建子机构数量限制(0,不可创建下级机构)
	 */
	@ApiModelProperty(value = "允许创建下级机构数量(0 不可创建下级机构)")
	@TableField(value = "sub_tenant_limit")
	private Integer subTenantLimit;
	/**
	 * 已创建下级机构数量
	 */
	@ApiModelProperty(value = "已创建下级机构数量")
	private Integer subTenant;
	/**
	@TableField(value = "sub_tenant")
	 * 创建用户数量限制(0,不可创建用户)
	 */
	@ApiModelProperty(value = "创建用户数量限制(0,不可创建用户)")
	@TableField(value = "user_limit")
	private Integer userLimit;
	/**
	 * 机构类别
	 */
	@ApiModelProperty(value = "机构类别")
	@TableField(exist=false)
	private Integer tenantCatogory;

	/**
	 * 租户到期时间
	 */
	@ApiModelProperty(value = "机构有效时间(值为null,永久有效)")
	@TableField(value = "expire_time")
	private Date expireTime;
	
	/**
	 * 机构logo
	 */
	@ApiModelProperty(value = "机构logo")
	@TableField(value = "logo")
	private String logo;
	
	/**
	 * 机构来源(后台创建，前台注册，渠道商创建)
	 */
	@ApiModelProperty(value = "1后台创建，2前台注册,3其他")
	private Integer origin;

	/**
	 * 审核状态
	 */
	@ApiModelProperty(value = "审核状态[1待审核,2自动通过,3人工通过,4已拒绝]")
	@TableField(value = "audit_status")
	private Integer auditStatus;

	/**
	 * 管理员ID
	 */
	@ApiModelProperty(value = "管理员ID")
	@TableField(value = "admin_id")
	private Long adminId;

	/**
	 * 父机构ID
	 */
	@ApiModelProperty(value = "父机构ID")
	@TableField(value = "parent_id")
	private Long parentId;

	/**
	 * 所有机构ID组 1,2,5,4
	 */

	@ApiModelProperty(value = "所有机构ID组 1,2,5,4")
	@TableField(value = "parent_ids")
	private String parentIds;

	/**
	 * 是否推送消息
	 */
	@ApiModelProperty(value = "是否推送消息")
	private Boolean notify;

	/**
	 * token有效期(秒)
	 */
	@ApiModelProperty(value = "token有效期(秒)")
	@TableField(value = "token_max_expire")
	private Integer tokenMaxExpire;
	/**
	 * 创建者
	 */
	@ApiModelProperty(value = "创建者")
	@TableField(value = "create_by")
	private Long createBy;
	/**
	 * 创建时间
	 */
	@ApiModelProperty(value = "创建时间")
	@TableField(value = "create_time")
	private Date createTime;
	/**
	 * 更新者
	 */
	@ApiModelProperty(value = "更新者")
	@TableField(value = "update_by")
	private Long updateBy;
	/**
	 * 更新时间
	 */
	@ApiModelProperty(value = "更新时间")
	@TableField(value = "update_time")
	private Date updateTime;
	/**
	 * 启用标记 1 启用 0 停用
	 */
	@ApiModelProperty(value ="开通状态[1 启用 ,0 停用]")
	private Boolean enable;
	/**
	 * 删除标记 1 已删除,0 未删除
	 */
	@ApiModelProperty("删除标记 1 已删除,0 未删除")
	@TableField(value = "del_flag")
	private Boolean delFlag;

	@TableField(value = "custom_data")
	private String customData;

	/**
	 * 省
	 */
	@ApiModelProperty(value ="省")
	private String province;

	/**
	 * 市
	 */
	@ApiModelProperty(value ="市")
	private String city;

	/**
	 * 区(县)
	 */
	@ApiModelProperty(value ="区(县)")
	private String county;
	
	@ApiModelProperty(value ="机构所在地名")
	@TableField(exist = false)
	private String placeName;


	/**
	 * 组织机构代码
	 */
	@ApiModelProperty(value ="组织机构代码")
	private String license;

	/**
	 * 组织机构代码证明(图片服务地址)
	 */
	@ApiModelProperty(value ="组织机构代码证明(图片服务地址)")
	@TableField(value = "license_img_url")
	private String licenseImgUrl;
	
	/**
	 * 该机构下设备总数
	 */
	@ApiModelProperty(value ="该机构下设备总数")
	@TableField(exist = false)
	private String deviceCount;
	
	/**
	 * 机构管理员名称
	 */
	@ApiModelProperty(value ="机构管理员名称")
	@TableField(exist = false)
	private String adminName;
	
	/**
	 * 机构管理员手机号码
	 */
	@ApiModelProperty(value ="机构管理员手机号码")
	@TableField(exist = false)
	private String adminMobile;
	
	/**
	 * 机构级别[1=省级,2=市级,3=区/县]
	 */
	@ApiModelProperty(value ="机构级别[1=省级,2=市级,3=区/县]")
	@TableField(exist = false)
	private Integer parentTenantRank;

	/*--------------------医疗机构信息-----------------------------------*/
	@TableField(exist = false)
	private HplTenant hplTenant;

	/*--------------------监管机构信息-----------------------------------*/
	@TableField(exist = false)
	private SuperviseTenant superviseTenant;
	
	/*--------------------供应商信息-----------------------------------*/
	@TableField(exist = false)
	private SupplierTenant supplierTenant;
	
	/*--------------------供应商证件信息-----------------------------------*/
	@TableField(exist = false)
	private List<SysSupplierTenantCredentials> supplierCredentials;

	/*----------------------查询信息------------------------------------*/
	/**
	 * 管理机构信息
	 */
	@TableField(exist = false)
	private String manageTenantName;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLicense() {
		return license;
	}

	public void setLicense(String license) {
		this.license = license;
	}

	public String getLicenseImgUrl() {
		return licenseImgUrl;
	}

	public void setLicenseImgUrl(String licenseImgUrl) {
		this.licenseImgUrl = licenseImgUrl;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCounty() {
		return county;
	}

	public void setCounty(String county) {
		this.county = county;
	}

	public Integer getCommercialUse() {
		return commercialUse;
	}

	public void setCommercialUse(Integer commercialUse) {
		this.commercialUse = commercialUse;
	}

	public Integer getTrial() {
		return trial;
	}

	public void setTrial(Integer trial) {
		this.trial = trial;
	}

	public Integer getTenantType() {
		return tenantType;
	}

	public void setTenantType(Integer tenantType) {
		this.tenantType = tenantType;
	}

	public Integer getSubTenantLimit() {
		return subTenantLimit;
	}

	public void setSubTenantLimit(Integer subTenantLimit) {
		this.subTenantLimit = subTenantLimit;
	}

	public Integer getSubTenant() {
		return subTenant;
	}

	public void setSubTenant(Integer subTenant) {
		this.subTenant = subTenant;
	}

	public Integer getUserLimit() {
		return userLimit;
	}

	public void setUserLimit(Integer userLimit) {
		this.userLimit = userLimit;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getExpireTime() {
		return expireTime;
	}

	public void setExpireTime(Date expireTime) {
		this.expireTime = expireTime;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public Integer getOrigin() {
		return origin;
	}

	public void setOrigin(Integer origin) {
		this.origin = origin;
	}

	public Integer getAuditStatus() {
		return auditStatus;
	}

	public void setAuditStatus(Integer auditStatus) {
		this.auditStatus = auditStatus;
	}

	public Long getAdminId() {
		return adminId;
	}

	public void setAdminId(Long adminId) {
		this.adminId = adminId;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public String getParentIds() {
		return parentIds;
	}

	public void setParentIds(String parentIds) {
		this.parentIds = parentIds;
	}

	public Integer getTokenMaxExpire() {
		return tokenMaxExpire;
	}

	public void setTokenMaxExpire(Integer tokenMaxExpire) {
		this.tokenMaxExpire = tokenMaxExpire;
	}

	public Long getCreateBy() {
		return createBy;
	}

	public void setCreateBy(Long createBy) {
		this.createBy = createBy;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Long getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(Long updateBy) {
		this.updateBy = updateBy;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public Boolean getEnable() {
		return enable;
	}

	public void setEnable(Boolean enable) {
		this.enable = enable;
	}

	public Boolean getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(Boolean delFlag) {
		this.delFlag = delFlag;
	}

	public String getCustomData() {
		return customData;
	}

	public void setCustomData(String customData) {
		this.customData = customData;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Boolean getNotify() {
		return notify;
	}

	public void setNotify(Boolean notify) {
		this.notify = notify;
	}

	public HplTenant getHplTenant() {
		return hplTenant;
	}

	public void setHplTenant(HplTenant hplTenant) {
		this.hplTenant = hplTenant;
	}

	public SuperviseTenant getSuperviseTenant() {
		return superviseTenant;
	}

	public void setSuperviseTenant(SuperviseTenant superviseTenant) {
		this.superviseTenant = superviseTenant;
	}

	public Long getManageTenantId() {
		return manageTenantId;
	}

	public void setManageTenantId(Long manageTenantId) {
		this.manageTenantId = manageTenantId;
	}

	public String getManageTenantName() {
		return manageTenantName;
	}

	public void setManageTenantName(String manageTenantName) {
		this.manageTenantName = manageTenantName;
	}
	
	public String getDeviceCount() {
		return deviceCount;
	}

	public void setDeviceCount(String deviceCount) {
		this.deviceCount = deviceCount;
	}
	
	public String getAdminName() {
		return adminName;
	}

	public void setAdminName(String adminName) {
		this.adminName = adminName;
	}

	public String getAdminMobile() {
		return adminMobile;
	}

	public void setAdminMobile(String adminMobile) {
		this.adminMobile = adminMobile;
	}
	

	public Integer getParentTenantRank() {
		return parentTenantRank;
	}

	public void setParentTenantRank(Integer parentTenantRank) {
		this.parentTenantRank = parentTenantRank;
	}


	public Date getManageTenantTime() {
		return manageTenantTime;
	}

	public void setManageTenantTime(Date manageTenantTime) {
		this.manageTenantTime = manageTenantTime;
	}

	public SupplierTenant getSupplierTenant() {
		return supplierTenant;
	}

	public void setSupplierTenant(SupplierTenant supplierTenant) {
		this.supplierTenant = supplierTenant;
	}

	public List<SysSupplierTenantCredentials> getSupplierCredentials() {
		return supplierCredentials;
	}

	public void setSupplierCredentials(List<SysSupplierTenantCredentials> supplierCredentials) {
		this.supplierCredentials = supplierCredentials;
	}
	public Integer getTenantCatogory() {
		return tenantCatogory;
	}

	public void setTenantCatogory(Integer tenantCatogory) {
		this.tenantCatogory = tenantCatogory;
	}
	

	public String getPlaceName() {
		return placeName;
	}

	public void setPlaceName(String placeName) {
		this.placeName = placeName;
	}
}
