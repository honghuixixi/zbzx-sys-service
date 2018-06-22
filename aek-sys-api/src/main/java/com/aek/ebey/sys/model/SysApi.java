package com.aek.ebey.sys.model;

import com.aek.common.core.base.BaseModel;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;

/**
 * <p>
 * 系统api表
 * </p>
 *
 * @author aek
 * @since 2017-05-06
 */
@TableName("sys_api")
public class SysApi extends BaseModel {

	private static final long serialVersionUID = 1L;

	/**
	 * 模块版本号
	 */
	@TableField(value = "version_number")
	private String versionNumber;
	/**
	 * 模块id
	 */
	@TableField(value = "module_id")
	private Long moduleId;
	/**
	 * api路径
	 */
	private String endpoint;
	/**
	 * 
	 */
	@TableField(value = "custom_data")
	private String customData;

	public String getVersionNumber() {
		return versionNumber;
	}

	public void setVersionNumber(String versionNumber) {
		this.versionNumber = versionNumber;
	}

	public Long getModuleId() {
		return moduleId;
	}

	public void setModuleId(Long moduleId) {
		this.moduleId = moduleId;
	}

	public String getEndpoint() {
		return endpoint;
	}

	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}

	public String getCustomData() {
		return customData;
	}

	public void setCustomData(String customData) {
		this.customData = customData;
	}

}
