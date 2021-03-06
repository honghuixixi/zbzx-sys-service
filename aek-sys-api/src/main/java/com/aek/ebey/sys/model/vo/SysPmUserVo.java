package com.aek.ebey.sys.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 *  机构巡检人员
 *	
 * @author HongHui
 * @date   2017年11月9日
 */
@ApiModel
public class SysPmUserVo {

	@ApiModelProperty(value="用户ID")
	private Long id;         //用户ID
	@ApiModelProperty(value="用户姓名")
	private String realName;     //用户姓名
	@ApiModelProperty(value="手机号")
	private String mobile;   //手机号
	
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
	
}
