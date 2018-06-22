package com.aek.ebey.sys.model;

import com.aek.common.core.base.BaseModel;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import java.util.Date;

/**
 * <p>
 * 用户登录日志表
 * </p>
 *
 * @author aek
 * @since 2017-05-06
 */
@TableName("sys_login_log")
public class SysLoginLog extends BaseModel {

	private static final long serialVersionUID = 1L;
	/**
	 * 用户ID
	 */
	@TableField(value = "user_id")
	private Long userId;
	/**
	 * 用户名称
	 */
	private String name;
	/**
	 * 登录IP地址
	 */
	private String ip;
	/**
	 * 登录时间
	 */
	@TableField(value = "login_time")
	private Date loginTime;
	/**
	 * 客户端类型
	 */
	@TableField(value = "client_type")
	private String clientType;
	/**
	 * 浏览器类型
	 */
	@TableField(value = "browser_type")
	private String browserType;
	/**
	 * 浏览器版本号
	 */
	@TableField(value = "browser_version")
	private String browserVersion;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public Date getLoginTime() {
		return loginTime;
	}

	public void setLoginTime(Date loginTime) {
		this.loginTime = loginTime;
	}

	public String getClientType() {
		return clientType;
	}

	public void setClientType(String clientType) {
		this.clientType = clientType;
	}

	public String getBrowserType() {
		return browserType;
	}

	public void setBrowserType(String browserType) {
		this.browserType = browserType;
	}

	public String getBrowserVersion() {
		return browserVersion;
	}

	public void setBrowserVersion(String browserVersion) {
		this.browserVersion = browserVersion;
	}

}
