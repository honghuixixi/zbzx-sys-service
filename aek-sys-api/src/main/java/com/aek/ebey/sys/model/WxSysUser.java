package com.aek.ebey.sys.model;

import java.util.Date;

import com.aek.common.core.base.BaseModel;

/**
 * 微信用户与设备用户绑定关系
 *	
 * @author HongHui
 * @date   2017年12月4日
 */
public class WxSysUser extends BaseModel{

	private static final long serialVersionUID = -7443719330598953524L;
	
	//用户ID
	private Long userId;
	//用户密码
	private String password;
	//微信OpenId
	private String openId;
	//绑定日期
	private Date createTime;
	
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getOpenId() {
		return openId;
	}
	public void setOpenId(String openId) {
		this.openId = openId;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
}
