package com.aek.ebey.sys.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class SysTakeOderUserHelpVo {

	@ApiModelProperty(value="用户ID")
	private Long id;         //用户ID
	@ApiModelProperty(value="用户姓名")
	private String realName;     //用户姓名
	@ApiModelProperty(value="手机号")
	private String mobile;   //手机号
	@ApiModelProperty(value="部门id")
	private Long deptId;
	@ApiModelProperty(value="部门名称")
	private String deptName;
	@ApiModelProperty(value="职务id")
	private Integer jobId;
	@ApiModelProperty(value="职务名称")
	private String jobName;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
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
	
	
}
