package com.aek.ebey.sys.model;

import java.util.Date;

import com.aek.common.core.base.BaseModel;
import com.aek.ebey.sys.model.custom.SuperviseTenant;
import com.aek.ebey.sys.model.custom.SupplierTenant;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 供应商实体类
 *	
 * @author HongHui
 * @date   2017年9月19日
 */
@ApiModel
@TableName("sys_tenant")
public class SysSupplierTenant extends BaseModel {

	private static final long serialVersionUID = 4075735023874540388L;
	
	/**
	 * 供应商名称
	 */
	@ApiModelProperty(value = "供应商名称")
	private String name;
	
	/**
	 * 租户类型[1=医疗机构,2=监管机构,3=供应商(维修商，供货商，配件供应商，综合服务商，其他)]
	 */
	@ApiModelProperty(value = "租户类型[1=医疗机构,2=监管机构,3=供应商(维修商，供货商，配件供应商，综合服务商，其他)]")
	@TableField(value = "tenant_type")
	private Integer tenantType;
	
	/**
	 * 供应商来源(1=后台创建，2=前台注册)
	 */
	@ApiModelProperty(value = "1=后台创建，2=前台注册")
	@TableField(value = "origin")
	private Integer origin;
	
	/**
	 * 审核状态
	 */
	@ApiModelProperty(value = "审核状态[1待审核,2自动通过,3人工通过,4已拒绝]")
	@TableField(value = "audit_status")
	private Integer auditStatus;

	/**
	 * 供应商logo
	 */
	@ApiModelProperty(value = "供应商Logo")
	@TableField(value = "logo")
	private String logo;
	
	/**
	 * 管理员ID
	 */
	@ApiModelProperty(value = "供应商管理员ID")
	@TableField(value = "admin_id")
	private Long adminId;
	
	/**
	 * 父供应商ID
	 */
	@ApiModelProperty(value = "父供应商ID")
	@TableField(value = "parent_id")
	private Long parentId;

	/**
	 * 所有供应商ID组 1,2,5,4
	 */
	@ApiModelProperty(value = "所有供应商ID组 如:1,2,5,4")
	@TableField(value = "parent_ids")
	private String parentIds;
	
	/**
	 * 省
	 */
	@ApiModelProperty(value = "省份")
	@TableField(value = "province")
	private String province;

	/**
	 * 市
	 */
	@ApiModelProperty(value = "市")
	@TableField(value = "city")
	private String city;

	/**
	 * 区(县)
	 */
	@ApiModelProperty(value = "区(县)")
	@TableField(value = "county")
	private String county;
	
	/**
	 * 创建人
	 */
	@ApiModelProperty(value = "创建人")
	@TableField(value = "create_by")
	private Long createBy;
	
	/**
	 * 创建时间
	 */
	@ApiModelProperty(value = "创建时间")
	@TableField(value = "create_time")
	private Date createTime;
	
	/**
	 * 更新人
	 */
	@ApiModelProperty(value = "更新人")
	@TableField(value = "update_by")
	private Long updateBy;
	
	/**
	 * 更新时间
	 */
	@ApiModelProperty(value = "更新时间")
	@TableField(value = "update_time")
	private Date updateTime;
	
	/**
	 * 启用标记 1 启用 0 停用
	 */
	@ApiModelProperty("开通状态[1=启用 ,0=停用]")
	private Boolean enable;
	
	/**
	 * 删除标记 1 已删除,0 未删除
	 */
	@ApiModelProperty("删除标记[1=已删除 ,0=未删除]")
	@TableField(value = "del_flag")
	private Boolean delFlag;
	
	/**
	 * 供应商扩展信息
	 */
	@TableField(exist = false)
	private SupplierTenant supplierTenant;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getTenantType() {
		return tenantType;
	}

	public void setTenantType(Integer tenantType) {
		this.tenantType = tenantType;
	}

	public Integer getOrigin() {
		return origin;
	}

	public void setOrigin(Integer origin) {
		this.origin = origin;
	}

	public Integer getAuditStatus() {
		return auditStatus;
	}

	public void setAuditStatus(Integer auditStatus) {
		this.auditStatus = auditStatus;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public Long getAdminId() {
		return adminId;
	}

	public void setAdminId(Long adminId) {
		this.adminId = adminId;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public String getParentIds() {
		return parentIds;
	}

	public void setParentIds(String parentIds) {
		this.parentIds = parentIds;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCounty() {
		return county;
	}

	public void setCounty(String county) {
		this.county = county;
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

	public SupplierTenant getSupplierTenant() {
		return supplierTenant;
	}

	public void setSupplierTenant(SupplierTenant supplierTenant) {
		this.supplierTenant = supplierTenant;
	}
	
}
