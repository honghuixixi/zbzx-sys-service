package com.aek.ebey.sys.web.request;

import java.util.Date;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.aek.ebey.sys.web.validator.group.Add;
import com.aek.ebey.sys.web.validator.group.Edit;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 机构创建修改
 * 
 * @author Mr.han
 *
 */
@ApiModel
public class TenantRequest extends BaseRequest {

	@NotNull(groups = Edit.class, message = "G_001")
	@ApiModelProperty(value = "机构ID")
	private Long id;

	/**
	 * 机构名称
	 */
	@Length(max = 40, groups = Add.class, message = "O_001")
	@ApiModelProperty(value = "机构名称")
	private String name;

	/**
	 * 上级机构ID
	 */
	@ApiModelProperty("上级机构ID")
	@NotNull(groups = Add.class, message = "O_002")
	private Long parentId;

	/**
	 * 管理机构ID
	 */
	@ApiModelProperty("上级行政单位")
	@NotNull(groups = Add.class, message = "O_012")
	private Long manageTenantId;

	/**
	 * 租户类型
	 */
	@ApiModelProperty(value = "租户类型[1=医疗机构,2=监管机构,3=设备供应商,4=设备维修商,5=配件供应商]", allowableValues = "1,2,3,4,5")
	@NotNull(groups = Add.class, message = "O_003")
	private Integer tenantType;
	
	/**
	 * 机构来源
	 */
	@ApiModelProperty(value = "机构来源[1=后台创建，2=前台注册]",allowableValues = "1,2")
	private Integer origin;

	/**
	 * 账户类型
	 */
	@ApiModelProperty(value = "账户类型[0=试用,1=正式]", allowableValues = "0,1")
	@NotNull(groups = Add.class, message = "O_004")
	private Integer commercialUse;

	/**
	 * 是否为测试机构
	 */
	@ApiModelProperty(value = "是否为测试[0=是,1=不是]", allowableValues = "0,1", example = "0")
	@NotNull(groups = Add.class, message = "O_005")
	private Integer trial;

	/**
	 * 允许创建下级机构数量
	 */
	@ApiModelProperty(value = "允许创建下级机构数量(0 不可创建下级机构)")
	@Min(value = 0, groups = Add.class, message = "O_006")
	private Integer subTenantLimit;

	/**
	 * 机构有效时间
	 */
	@ApiModelProperty(value = "机构有效时间(值为null,永久有效)")
	private Date expireTime;

	/**
	 * 机构代码
	 */
	@ApiModelProperty(value = "机构代码")
	private String license;

	/**
	 * 组织机构代码证(图片服务器地址)
	 */
	@ApiModelProperty(value = "组织结构代码证(图片服务器地址)")
	private String licenseImgUrl;

	@ApiModelProperty(value = "机构地址省")
	private String province;

	@ApiModelProperty(value = "机构地址市")
	private String city;

	@ApiModelProperty(value = "机构地址区(县)")
	private String county;

	/*------------------------------------机构管理员信息-------------------------------------*/
	@ApiModelProperty(value = "登录名")
	private String loginName;	
	/**
	 * 判断是否同时创建管理员
	 */
	@ApiModelProperty("是否同时创建管理员")
	@NotNull(groups = Add.class, message = "O_007")
	private Boolean createAdmin;

	@ApiModelProperty(value = "管理员姓名")
	private String realName;

	@ApiModelProperty(value = "管理员手机号码")
	private String mobile;
	
	@ApiModelProperty(value = "验证码")
	private String verifyCode;

	@ApiModelProperty(value = "管理员邮箱")
	private String email;

	@ApiModelProperty(value = "管理员登录密码")
	private String password;

	/*------------------------------------医疗租户信息-------------------------------------*/
	@ApiModelProperty(value = "医疗机构信息(当租户类型为医疗机构时使用)")
	private HplTenantRequest hplTenant;

	/*------------------------------------监管租户信息-------------------------------------*/
	@ApiModelProperty(value = "监管机构信息(当租户类型为监管机构时使用)")
	private SuperviseTenantRequest superviseTenant;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public Integer getTenantType() {
		return tenantType;
	}

	public void setTenantType(Integer tenantType) {
		this.tenantType = tenantType;
	}

	public Integer getCommercialUse() {
		return commercialUse;
	}

	public void setCommercialUse(Integer commercialUse) {
		this.commercialUse = commercialUse;
	}
	
	public Integer getOrigin() {
		return origin;
	}

	public void setOrigin(Integer origin) {
		this.origin = origin;
	}

	public Integer getTrial() {
		return trial;
	}

	public void setTrial(Integer trial) {
		this.trial = trial;
	}

	public Integer getSubTenantLimit() {
		return subTenantLimit;
	}

	public void setSubTenantLimit(Integer subTenantLimit) {
		this.subTenantLimit = subTenantLimit;
	}

	public Date getExpireTime() {
		return expireTime;
	}

	public void setExpireTime(Date expireTime) {
		this.expireTime = expireTime;
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

	public HplTenantRequest getHplTenant() {
		return hplTenant;
	}

	public void setHplTenant(HplTenantRequest hplTenant) {
		this.hplTenant = hplTenant;
	}

	public Boolean getCreateAdmin() {
		return createAdmin;
	}

	public void setCreateAdmin(Boolean createAdmin) {
		this.createAdmin = createAdmin;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	
	public String getVerifyCode() {
		return verifyCode;
	}

	public void setVerifyCode(String verifyCode) {
		this.verifyCode = verifyCode;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Long getManageTenantId() {
		return manageTenantId;
	}

	public void setManageTenantId(Long manageTenantId) {
		this.manageTenantId = manageTenantId;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

}
