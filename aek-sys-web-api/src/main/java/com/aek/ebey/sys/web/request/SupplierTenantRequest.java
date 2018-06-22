package com.aek.ebey.sys.web.request;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.aek.ebey.sys.model.SysSupplierTenantCredentials;
import com.aek.ebey.sys.model.custom.SupplierTenant;
import com.aek.ebey.sys.web.validator.group.Add;
import com.aek.ebey.sys.web.validator.group.Edit;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 供应商信息创建修改请求实体类
 *	
 * @author HongHui
 * @date   2017年9月19日
 */
@ApiModel
public class SupplierTenantRequest extends BaseRequest {

	@NotNull(groups = Edit.class, message = "G_001")
	@ApiModelProperty(value = "供应商ID")
	private Long id;

	/**
	 * 供应商名称
	 */
	@Length(max = 40, groups = Add.class, message = "S_001")
	@ApiModelProperty(value = "供应商名称")
	private String name;

	/**
	 * 租户类型
	 */
	@ApiModelProperty(value = "租户类型[1=医疗机构,2=监管机构,3=供应商]", allowableValues = "1,2,3")
	@NotNull(groups = Add.class, message = "O_003")
	private Integer tenantType;
	
	/**
	 * 供应商来源
	 */
	@ApiModelProperty(value = " 供应商来源[1=后台创建，2=前台注册]",allowableValues = "1,2")
	private Integer origin;

	/**
	 * 供应商地址省
	 */
	@ApiModelProperty(value = "供应商地址省")
	private String province;

	/**
	 * 供应商地址市
	 */
	@ApiModelProperty(value = "供应商地址市")
	private String city;

	/**
	 * 供应商地址区(县)
	 */
	@ApiModelProperty(value = "供应商地址区(县)")
	private String county;
	
	/**
	 * 供应商logo
	 */
	@ApiModelProperty(value = "供应商Logo")
	private String logo;

	/*------------------------------------供应商管理员信息-------------------------------------*/
	/**
	 * 判断是否同时创建管理员
	 */
	@ApiModelProperty("是否同时创建供应商管理员")
	private Boolean createAdmin;

	/**
	 * 用户姓名
	 */
	@ApiModelProperty(value = "用户姓名")
	//@NotNull(groups = Add.class, message = "S_002")
	private String realName;

	/**
	 * 手机号码
	 */
	@ApiModelProperty(value = "手机号码")
	//@NotNull(groups = Add.class, message = "S_003")
	private String mobile;
	
	/**
	 * 邮箱地址
	 */
	@ApiModelProperty(value = "邮箱地址")
	private String email;

	/**
	 * 登录密码
	 */
	@ApiModelProperty(value = "登录密码")
	//@NotNull(groups = Add.class, message = "S_004")
	private String password;

	/*------------------------------------供应商租户信息-------------------------------------*/
	@ApiModelProperty(value = "供应商信息(当租户类型为供应商时使用)")
	private SupplierTenant supplierTenant;
	
	/*------------------------------------供应商证件信息-------------------------------------*/
	@ApiModelProperty(value = "供应商证件信息")
	private List<SysSupplierTenantCredentials> supplierCredentials;

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

	public Integer getTenantType() {
		return tenantType;
	}

	public void setTenantType(Integer tenantType) {
		this.tenantType = tenantType;
	}

	public Integer getOrigin() {
		return origin;
	}

	public void setOrigin(Integer origin) {
		this.origin = origin;
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

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}
	
}
