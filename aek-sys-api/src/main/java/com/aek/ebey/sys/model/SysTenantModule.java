package com.aek.ebey.sys.model;

import com.aek.common.core.base.BaseModel;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import java.util.Date;

/**
 * <p>
 * 机构模块表
 * </p>
 *
 * @author aek
 * @since 2017-05-06
 */
@TableName("sys_tenant_module")
public class SysTenantModule extends BaseModel {

	private static final long serialVersionUID = 1L;
	/**
	 * 机构ID
	 */
	@TableField(value = "tenant_id")
	private Long tenantId;
	/**
	 * 模块ID
	 */
	@TableField(value = "module_id")
	private Long moduleId;
	/**
	 * 模块名称
	 */
	private String name;

	/**
	 * 模块版本号
	 */
	@TableField(value = "version_number")
	private String versionNumber;
	/**
	 * 模块分类(模块分类,业务应用,系统应用,数据分析应用,第三方应用)
	 */
	@TableField(value = "module_type")
	private Long moduleType;

	/**
	 * 路由
	 */
	@TableField(value = "url")
	private String url;

	/**
	 * 模块发布时间
	 */
	@TableField(value = "release_time")
	private Date releaseTime;

	/**
	 * 创建时间
	 */
	@TableField(value = "create_time")
	private Date createTime;
	/**
	 * 更新时间
	 */
	@TableField(value = "update_time")
	private Date updateTime;
	/**
	 * 是否启用 0 关闭 1启用
	 */
	private Boolean enable;
	/**
	 * 删除标志 1 删除 0 未删除
	 */
	@TableField(value = "del_flag")
	private Boolean delFlag;
	/**
	 * 模块来源 1 独立添加
	 */
	@TableField(value = "module_source")
	private Integer moduleSource;

	/**
	 * 模块描述
	 */
	@TableField(value = "description")
	private String description;
	
	public Long getTenantId() {
		return tenantId;
	}

	public void setTenantId(Long tenantId) {
		this.tenantId = tenantId;
	}

	public Long getModuleId() {
		return moduleId;
	}

	public void setModuleId(Long moduleId) {
		this.moduleId = moduleId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getVersionNumber() {
		return versionNumber;
	}

	public void setVersionNumber(String versionNumber) {
		this.versionNumber = versionNumber;
	}

	public Long getModuleType() {
		return moduleType;
	}

	public void setModuleType(Long moduleType) {
		this.moduleType = moduleType;
	}

	public Date getReleaseTime() {
		return releaseTime;
	}

	public void setReleaseTime(Date releaseTime) {
		this.releaseTime = releaseTime;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Integer getModuleSource() {
		return moduleSource;
	}

	public void setModuleSource(Integer moduleSource) {
		this.moduleSource = moduleSource;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
