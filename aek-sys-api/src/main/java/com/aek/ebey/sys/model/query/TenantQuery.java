package com.aek.ebey.sys.model.query;

import com.aek.common.core.base.page.PageHelp;
import com.aek.ebey.sys.model.SysTenant;
import com.aek.ebey.sys.model.custom.HplTenant;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class TenantQuery extends PageHelp<SysTenant> {

	@ApiModelProperty("机构id")
	private Long tenantId;

	@ApiModelProperty("来源[1后台创建，2前台注册]")
	private Integer origin;

	@ApiModelProperty("审核状态[1待审核,2自动通过,3人工通过,4已拒绝]")
	private Integer auditStatus;

	@ApiModelProperty("机构类型[1=医疗机构,2=监管机构,3=供应商]")
	private Integer tenantType;

	@ApiModelProperty("账户类型[0=试用,1=正式]")
	private Integer commercialUse;

	@ApiModelProperty("开通状态[1 启用 ,0 停用]")
	private Boolean enable;

	@ApiModelProperty("是否测试[1 否, 0 是]")
	private Integer trial;
	
	@ApiModelProperty("机构类别[1=妇幼保健院, 2=中心卫生院/乡（镇）卫生院/街道卫生院, 3=疗养院, 4=门诊部, 5=诊所, 6=村卫生室（所）,7=急救中心/急救站, 8=临床检验中心, 9=专科疾病防治院 /专科疾病防治所/专科疾病防治站, 10=护理院/护理站, 11=其他诊疗机构]")
	private Integer category;

	@ApiModelProperty("检索关键词")
	private String keyword;
	
	@ApiModelProperty(value = "服务类型[1=维修商，2=供货商，3=配件供应商，4=综合服务商，5=其他]")
	private Integer serviceType;

	/**
	 * @return the tenantId
	 */
	public Long getTenantId() {
		return tenantId;
	}

	/**
	 * @param tenantId
	 *            the tenantId to set
	 */
	public void setTenantId(Long tenantId) {
		this.tenantId = tenantId;
	}

	/**
	 * @return the origin
	 */
	public Integer getOrigin() {
		return origin;
	}

	/**
	 * @param origin
	 *            the origin to set
	 */
	public void setOrigin(Integer origin) {
		this.origin = origin;
	}

	/**
	 * @return the auditStatus
	 */
	public Integer getAuditStatus() {
		return auditStatus;
	}

	/**
	 * @param auditStatus
	 *            the auditStatus to set
	 */
	public void setAuditStatus(Integer auditStatus) {
		this.auditStatus = auditStatus;
	}

	/**
	 * @return the tenantType
	 */
	public Integer getTenantType() {
		return tenantType;
	}
	
	public Integer getCategory() {
		return category;
	}

	public void setCategory(Integer category) {
		this.category = category;
	}

	/**
	 * @param tenantType
	 *            the tenantType to set
	 */
	public void setTenantType(Integer tenantType) {
		this.tenantType = tenantType;
	}

	/**
	 * @return the commercialUse
	 */
	public Integer getCommercialUse() {
		return commercialUse;
	}

	/**
	 * @param commercialUse
	 *            the commercialUse to set
	 */
	public void setCommercialUse(Integer commercialUse) {
		this.commercialUse = commercialUse;
	}

	/**
	 * @return the enable
	 */
	public Boolean getEnable() {
		return enable;
	}

	/**
	 * @param enable
	 *            the enable to set
	 */
	public void setEnable(Boolean enable) {
		this.enable = enable;
	}

	/**
	 * @return the trial
	 */
	public Integer getTrial() {
		return trial;
	}

	/**
	 * @param trial
	 *            the trial to set
	 */
	public void setTrial(Integer trial) {
		this.trial = trial;
	}
	
	/**
	 * @return the keyword
	 */
	public String getKeyword() {
		return keyword;
	}

	/**
	 * @param keyword
	 *            the keyword to set
	 */
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public Integer getServiceType() {
		return serviceType;
	}

	public void setServiceType(Integer serviceType) {
		this.serviceType = serviceType;
	}

}
