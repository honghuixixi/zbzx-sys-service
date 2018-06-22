package com.aek.ebey.sys.model.custom;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 医疗机构字段信息
 * 
 * @author Mr.han
 *
 */
@ApiModel
public class HplTenant {

	/**
	 * 机构类别
	 * 
	 *1=医疗机构,2=基层医疗卫生机构部, 3=疾病预防控制中心
	 */
	@ApiModelProperty(value="1=医疗机构,2=基层医疗卫生机构部, 3=疾病预防控制中心")
	private Integer category;

	/**
	 * 经济类型[1=公立,2=民营]
	 */
	@ApiModelProperty(value="经济类型[1=公立,2=民营]")
	private Integer economicType;

	/**
	 * 管理类型[1=营利性,2=非营利性,3=其他]
	 */
	@ApiModelProperty(value="管理类型[1=营利性,2=非营利性,3=其他]")
	private Integer manageType;

	/**
	 * 等级 1=一等, 2=二等, 3=三等, 4=未定义
	 */
	@ApiModelProperty(value="等级 1=一等, 2=二等, 3=三等, 4=未定义")
	private Integer grade;

	/**
	 * 等次 1=特等, 2=甲等, 3=乙等, 4=丙等, 5=丁等, 6=未定义
	 */
	@ApiModelProperty(value="等次 1=特等, 2=甲等, 3=乙等, 4=丙等, 5=丁等, 6=未定义")
	private Integer hierarchy;

	/**
	 * 医疗机构代码
	 */
	@ApiModelProperty(value="医疗机构代码")
	private String orgCode;
	
	/**
	 * 医疗机构标识来源以区分设备平台和装备中心账号
	 */
	@ApiModelProperty(value="医疗机构标识来源以区分设备平台和装备中心账号（装备中心标识zjzb）")
	private String from;

	public Integer getCategory() {
		return category;
	}

	public void setCategory(Integer category) {
		this.category = category;
	}

	public Integer getEconomicType() {
		return economicType;
	}

	public void setEconomicType(Integer economicType) {
		this.economicType = economicType;
	}

	public Integer getManageType() {
		return manageType;
	}

	public void setManageType(Integer manageType) {
		this.manageType = manageType;
	}

	public Integer getGrade() {
		return grade;
	}

	public void setGrade(Integer grade) {
		this.grade = grade;
	}

	public Integer getHierarchy() {
		return hierarchy;
	}

	public void setHierarchy(Integer hierarchy) {
		this.hierarchy = hierarchy;
	}

	public String getOrgCode() {
		return orgCode;
	}

	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

}
