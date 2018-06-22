package com.aek.ebey.sys.model;

import com.aek.common.core.base.BaseModel;
import com.aek.ebey.sys.model.custom.ModuleCustom;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import java.util.Date;

/**
 * <p>
 * 系统模块表
 * </p>
 *
 * @author aek
 * @since 2017-05-06
 */
@TableName("sys_module")
public class SysModule extends BaseModel {

	private static final long serialVersionUID = 1L;

	/**
	 * 模块名称
	 */
	@TableField(value = "name")
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
	 * 价格策略
	 */
	@TableField(value = "price_policy_id")
	private Long pricePolicyId;

	/**
	 * 路由
	 */
	@TableField(value = "url")
	private String url;

	/**
	 * 创建人ID
	 */
	@TableField(value = "create_by")
	private Long createBy;
	/**
	 * 创建时间
	 */
	@TableField(value = "create_time")
	private Date createTime;
	/**
	 * 修改人ID
	 */
	@TableField(value = "update_by")
	private Long updateBy;
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

	@TableField(value = "custom_data")
	private String customData;

	/**
	 * coustomData 映射类
	 */
	@TableField(exist = false)
	private ModuleCustom coustom;

	/**
	 * 模块描述
	 */
	@TableField(value = "description")
	private String description;

	/** -------------------------------沉余数据-------------- */
	/**
	 * 创建人姓名
	 */
	@TableField(exist = false)
	private String createName;

	/**
	 * 更新人姓名
	 */
	@TableField(exist = false)
	private String updateName;

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

	public Long getPricePolicyId() {
		return pricePolicyId;
	}

	public void setPricePolicyId(Long pricePolicyId) {
		this.pricePolicyId = pricePolicyId;
	}

	public Long getCreateBy() {
		return createBy;
	}

	public void setCreateBy(Long createBy) {
		this.createBy = createBy;
	}

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

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getCustomData() {
		return customData;
	}

	public void setCustomData(String customData) {
		this.customData = customData;
	}

	public ModuleCustom getCoustom() {
		return coustom;
	}

	public void setCoustom(ModuleCustom coustom) {
		this.coustom = coustom;
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

	public String getCreateName() {
		return createName;
	}

	public void setCreateName(String createName) {
		this.createName = createName;
	}

	public String getUpdateName() {
		return updateName;
	}

	public void setUpdateName(String updateName) {
		this.updateName = updateName;
	}

}
