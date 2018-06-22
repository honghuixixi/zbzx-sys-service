package com.aek.ebey.sys.model.vo;

/**
 *  机构巡检人员
 *	
 * @author HongHui
 * @date   2017年11月9日
 */
public class SysQcUserVo {

	private Long id;         //用户ID
	private String realName;     //用户姓名
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
