package com.aek.ebey.sys.web.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 医院租户属性
 * 
 * @author Mr.han
 *
 */
@ApiModel
public class HplTenantRequest {

	/**
	 * 机构类别
	 * 
	 * 1=妇幼保健院, 2=中心卫生院/乡（镇）卫生院/街道卫生院, 3=疗养院, 4=门诊部, 5=诊所, 6=村卫生室（所）,
	 * 7=急救中心/急救站, 8=临床检验中心, 9=专科疾病防治院 /专科疾病防治所/专科疾病防治站, 10=护理院/护理站, 11=其他诊疗机构
	 */
	@ApiModelProperty(value = "机构类型", allowableValues = "1,2,3,4,5,6,7,8,9,10,11")
	private Integer category;

	/**
	 * 经济类型[1=公立,2=民营]
	 */
	@ApiModelProperty(value = "经济类型[1=公立,2=民营]", allowableValues = "1,2")
	private Integer economicType;

	/**
	 * 管理类型[1=营利性,2=非营利性,3=其他]
	 */
	@ApiModelProperty(value = "管理类型[1=营利性,2=非营利性,3=其他]", allowableValues = "1,2,3")
	private Integer manageType;

	/**
	 * 等级[1=一等,2=二等,3=三等,4=未定义]
	 */
	@ApiModelProperty(value = "等级[1=一等,2=二等,3=三等,4=未定义]", allowableValues = "1,2,3,4")
	private Integer grade;

	/**
	 * 等次[1=特等,2=甲等,3=乙等,4=丙等,5=丁等,6=未定义]
	 */
	@ApiModelProperty(value = "等次[1=特等,2=甲等,3=乙等,4=丙等,5=丁等,6=未定义]", allowableValues = "1,2,3,4")
	private Integer hierarchy;

	/**
	 * 医疗机构代码
	 */
	@ApiModelProperty(value = "医疗机构代码")
	private String orgCode;

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

}
