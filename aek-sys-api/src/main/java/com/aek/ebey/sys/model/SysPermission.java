package com.aek.ebey.sys.model;

import com.aek.common.core.base.BaseModel;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import java.util.Date;

/**
 * <p>
 * 权限表
 * </p>
 *
 * @author aek
 * @since 2017-05-06
 */
@TableName("sys_permission")
public class SysPermission extends BaseModel {

	private static final long serialVersionUID = 1L;

	/**
	 * 父ID
	 */
	@TableField(value = "parent_id")
	private Long parentId;
	/**
	 * 模块ID
	 */
	@TableField(value = "module_id")
	private Long moduleId;
	/**
	 * 权限名称
	 */
	private String name;
	/**
	 * 权限标识
	 */
	private String code;
	/**
	 * url
	 */
	private String url;
	/**
	 * target
	 */
	private String target;
	/**
	 * 菜单标识
	 */
	@TableField(value = "menu_flag")
	private Boolean menuFlag;

	/**
	 * 
	 */
	@TableField(value = "create_time")
	private Date createTime;
	/**
	 * 
	 */
	@TableField(value = "update_time")
	private Date updateTime;
	/**
	 * 是否启用 0 关闭 1启用
	 */
	private Boolean enable;
	/**
	 * 描述
	 */
	private String description;
	/**
	 * 
	 */
	@TableField(value = "custom_data")
	private String customData;

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
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

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCustomData() {
		return customData;
	}

	public void setCustomData(String customData) {
		this.customData = customData;
	}

	public Boolean getEnable() {
		return enable;
	}

	public void setEnable(Boolean enable) {
		this.enable = enable;
	}

	public Boolean getMenuFlag() {
		return menuFlag;
	}

	public void setMenuFlag(Boolean menuFlag) {
		this.menuFlag = menuFlag;
	}

}
