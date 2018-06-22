package com.aek.ebey.sys.web.request;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import com.aek.ebey.sys.web.validator.group.Add;
import com.aek.ebey.sys.web.validator.group.Edit;
import com.aek.ebey.sys.web.validator.group.Register;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "user", description = "用户基本信息")
public class UserRequest extends BaseRequest {

	@ApiModelProperty("ID")
	@NotNull(groups = { Edit.class }, message = "G_001")
	private Long id;

	/**
	 * 所属租户
	 */
	@ApiModelProperty(value = "所属租户")
	@NotNull(groups = { Add.class }, message = "G_002")
	private Long tenantId;
	
	/**
	 * 租户名称
	 */
	@ApiModelProperty(value = "租户名称")
	@NotNull(groups = {Register.class }, message = "U_031")
	private String tenantName;

	/**
	 * 真实姓名
	 */
	@ApiModelProperty(value = "真实姓名")
	@NotEmpty(groups = { Add.class,Register.class }, message = "U_005")
	private String realName;
	
	/**
	 * 登录名
	 */
	@ApiModelProperty(value = "登录名")
	@NotEmpty(groups = { Register.class }, message = "U_030")
	private String loginName;

	/**
	 * 登录密码
	 */
	@ApiModelProperty(value = "登录密码")
	@NotEmpty(groups = { Add.class,Register.class }, message = "U_004")
	private String password;

	/**
	 * 手机号码(可用手机登录)
	 */
	@ApiModelProperty(value = "手机号码")
	@Length(max = 11, min = 11, groups = { Add.class }, message = "U_012")
	@NotEmpty(groups = { Add.class,Register.class }, message = "U_007")
	private String mobile;
	
	/**
	 * 手机验证码(注册)
	 */
	@ApiModelProperty(value = "手机验证码")
	@NotEmpty(groups = { Register.class }, message = "U_032")
	private String verifyCode;

	/**
	 * 邮箱(可用邮箱登录)
	 */
	@ApiModelProperty(value = "邮箱")
	private String email;

	/**
	 * 所在部门ID
	 */
	@NotNull(groups = { Add.class }, message = "G_003")
	@ApiModelProperty(value = "所在部门ID")
	private Long deptId;
	
	/**
	 * 职务名称
	 */
	@ApiModelProperty(value = "职务名称")
	private String jobName;


	public Long getTenantId() {
		return tenantId;
	}

	public void setTenantId(Long tenantId) {
		this.tenantId = tenantId;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
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

	public Long getDeptId() {
		return deptId;
	}

	public void setDeptId(Long deptId) {
		this.deptId = deptId;
	}

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getTenantName() {
		return tenantName;
	}

	public void setTenantName(String tenantName) {
		this.tenantName = tenantName;
	}

	public String getVerifyCode() {
		return verifyCode;
	}

	public void setVerifyCode(String verifyCode) {
		this.verifyCode = verifyCode;
	}
}
