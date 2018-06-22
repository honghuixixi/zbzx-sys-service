package com.aek.ebey.sys.core;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 系統常量配置
 *
 * @author  Honghui
 * @date    2017年6月22日
 * @version 1.0
 */
@Component
@ConfigurationProperties(prefix = "email.redirect")
public class SysEmialRedirectConfig {

	/**
	 * email重定向地址
	 */
	private String url;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
}
