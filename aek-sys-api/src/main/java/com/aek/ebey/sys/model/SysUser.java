package com.aek.ebey.sys.model;

import java.util.Date;

import com.aek.common.core.base.BaseModel;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * <p>
 * 用户表
 * </p>
 *
 * @author aek
 * @since 2017-05-06
 */
@TableName("sys_user")
public class SysUser extends BaseModel {

	private static final long serialVersionUID = 1L;

	public SysUser() {
		super();
	}

	public SysUser(String realName, String password, String mobile, String email) {
		super();
		this.realName = realName;
		this.password = password;
		this.mobile = mobile;
		this.email = email;
	}
	
	public SysUser(String realName,String loginName, String password, String mobile, String email) {
		super();
		this.realName = realName;
		this.loginName = loginName;
		this.password = password;
		this.mobile = mobile;
		this.email = email;
	}

	/**
	 * 所属租户ID
	 */
	@TableField(value = "tenant_id")
	private Long tenantId;
	/**
	 * 登录名
	 */
	@TableField(value = "login_name")
	private String loginName;
	/**
	 * 所属租户名称
	 */
	@TableField(value = "tenant_name")
	private String tenantName;
	/**
	 * 真实姓名
	 */
	@TableField(value = "real_name")
	private String realName;
	/**
	 * 真实姓名拼音
	 */
	@TableField(value = "real_name_py")
	private String realNamePy;
	/**
	 * 密码
	 */
	private String password;
	
	/**
	 * 登录验证码
	 */
	@TableField(exist=false)
	private String verifyCode;

	/**
	 * 手机号码
	 */
	private String mobile;
	/**
	 * 邮箱
	 */
	private String email;
	/**
	 * 邮箱是否被激活
	 */
	@TableField(value = "email_activate")
	private Boolean emailActivate;
	/**
	 * 归属部门ID
	 */
	@TableField(value = "dept_id")
	private Long deptId;
	/**
	 * 归属部门名称
	 */
	@TableField(value = "dept_name")
	private String deptName;
	/**
	 * 所有上级部门ID
	 */
	@TableField(value = "parent_dept_ids")
	private String parentDeptIds;
	/**
	 * 职务ID
	 */
	@TableField(value = "job_id")
	private Integer jobId;
	/**
	 * 职务名称
	 */
	@TableField(value = "job_name")
	private String jobName;

	/**
	 * 是否管理员(0:否;1:是)
	 */
	@TableField(value = "admin_flag")
	private Boolean adminFlag;

	/**
	 * 密码最后修改时间
	 */
	@TableField(value = "pwd_reset_time")
	private Date pwdResetTime;

	/**
	 * 禁用截至时间
	 */
	@TableField(value = "blocked_until")
	private Date blockedUntil;
	/**
	 * 用户注册IP
	 */
	@TableField(value = "registration_ip")
	private String registrationIp;
	/**
	 * 最后登录IP
	 */
	@TableField(value = "last_login_ip")
	private String lastLoginIp;
	/**
	 * 最后登录时间
	 */
	@TableField(value = "last_login_time")
	private Date lastLoginTime;
	/**
	 * 最后登录平台
	 */
	@TableField(value = "last_client_type")
	private String lastClientType;
	/**
	 * 创建人名称
	 */
	@TableField(value = "creator_name")
	private String creatorName;
	/**
	 * 创建者
	 */
	@TableField(value = "create_by")
	private Long createBy;
	/**
	 * 创建时间
	 */
	@TableField(value = "create_time")
	private Date createTime;
	/**
	 * 更新者
	 */
	@TableField(value = "update_by")
	private Long updateBy;
	/**
	 * 更新时间
	 */
	@TableField(value = "update_time")
	private Date updateTime;
	/**
	 * 是否启用 1=启用 0=不启用
	 */
	private Boolean enable;
	/**
	 * 删除标志 1 删除 0 未删除
	 */
	@TableField(value = "del_flag")
	private Boolean delFlag;

	@TableField(value = "custom_data")
	private String customData;

	public Long getTenantId() {
		return tenantId;
	}

	public void setTenantId(Long tenantId) {
		this.tenantId = tenantId;
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

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getRealNamePy() {
		return realNamePy;
	}

	public void setRealNamePy(String realNamePy) {
		this.realNamePy = realNamePy;
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

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public Integer getJobId() {
		return jobId;
	}

	public void setJobId(Integer jobId) {
		this.jobId = jobId;
	}

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public Date getPwdResetTime() {
		return pwdResetTime;
	}

	public void setPwdResetTime(Date pwdResetTime) {
		this.pwdResetTime = pwdResetTime;
	}

	public Date getBlockedUntil() {
		return blockedUntil;
	}

	public void setBlockedUntil(Date blockedUntil) {
		this.blockedUntil = blockedUntil;
	}

	public String getRegistrationIp() {
		return registrationIp;
	}

	public void setRegistrationIp(String registrationIp) {
		this.registrationIp = registrationIp;
	}

	public String getCreatorName() {
		return creatorName;
	}

	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
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

	public Date getUpdateTime() {
		return updateTime;
	}

	public String getLastLoginIp() {
		return lastLoginIp;
	}

	public void setLastLoginIp(String lastLoginIp) {
		this.lastLoginIp = lastLoginIp;
	}

	public Date getLastLoginTime() {
		return lastLoginTime;
	}

	public void setLastLoginTime(Date lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getCustomData() {
		return customData;
	}

	public void setCustomData(String customData) {
		this.customData = customData;
	}

	public Boolean getAdminFlag() {
		return adminFlag;
	}

	public void setAdminFlag(Boolean adminFlag) {
		this.adminFlag = adminFlag;
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

	public String getParentDeptIds() {
		return parentDeptIds;
	}

	public void setParentDeptIds(String parentDeptIds) {
		this.parentDeptIds = parentDeptIds;
	}

	public String getLastClientType() {
		return lastClientType;
	}

	public void setLastClientType(String lastClientType) {
		this.lastClientType = lastClientType;
	}

	public Boolean getEmailActivate() {
		return emailActivate;
	}

	public void setEmailActivate(Boolean emailActivate) {
		this.emailActivate = emailActivate;
	}

	public String getVerifyCode() {
		return verifyCode;
	}

	public void setVerifyCode(String verifyCode) {
		this.verifyCode = verifyCode;
	}
}
