package com.aek.ebey.sys.model.custom;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 供应商扩展信息custom_data
 *	
 * @author HongHui
 * @date   2017年9月19日
 */
@ApiModel
public class SupplierTenant implements Serializable{

	private static final long serialVersionUID = -1280983476092837761L;

	/**
	 * 服务类型[1=维修商，2=供货商，3=配件供应商，4=综合服务商，5=其他]
	 */
	@ApiModelProperty(value = "服务类型[1=维修商，2=供货商，3=配件供应商，4=综合服务商，5=其他]")
	private Integer serviceType;
	
	/**
	 * 详细地址
	 */
	@ApiModelProperty(value = "详细地址")
	private String address;
	
	/**
	 * 联系人
	 */
	@ApiModelProperty(value = "联系人")
	private String contact;
	
	/**
	 * 联系电话
	 */
	@ApiModelProperty(value = "联系电话")
	private String contactMobile;
	
	/**
	 * 客服电话
	 */
	@ApiModelProperty(value = "客服电话")
	private String servicePhone;
	
	/**
	 * 企业法人
	 */
	@ApiModelProperty(value = "企业法人")
	private String enterpriseLegalPerson;
	
	/**
	 * 开户名称
	 */
	@ApiModelProperty(value = "开户名称")
	private String accountName;
	
	/**
	 * 开户银行
	 */
	@ApiModelProperty(value = "开户银行")
	private String accountBank;
	
	/**
	 * 纳税号
	 */
	@ApiModelProperty(value = "纳税号")
	private String taxNumber;
	
	/**
	 * 发票抬头
	 */
	@ApiModelProperty(value = "发票抬头")
	private String invoiceHeader;
	
	/**
	 * 商家简介
	 */
	@ApiModelProperty(value = "商家简介")
	private String introduce;
	
	/**
	 * 服务范围
	 */
	@ApiModelProperty(value = "服务范围")
	private String serviceScope;

	public Integer getServiceType() {
		return serviceType;
	}

	public void setServiceType(Integer serviceType) {
		this.serviceType = serviceType;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getContactMobile() {
		return contactMobile;
	}

	public void setContactMobile(String contactMobile) {
		this.contactMobile = contactMobile;
	}

	public String getEnterpriseLegalPerson() {
		return enterpriseLegalPerson;
	}

	public void setEnterpriseLegalPerson(String enterpriseLegalPerson) {
		this.enterpriseLegalPerson = enterpriseLegalPerson;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getAccountBank() {
		return accountBank;
	}

	public void setAccountBank(String accountBank) {
		this.accountBank = accountBank;
	}

	public String getTaxNumber() {
		return taxNumber;
	}

	public void setTaxNumber(String taxNumber) {
		this.taxNumber = taxNumber;
	}

	public String getInvoiceHeader() {
		return invoiceHeader;
	}

	public void setInvoiceHeader(String invoiceHeader) {
		this.invoiceHeader = invoiceHeader;
	}

	public String getIntroduce() {
		return introduce;
	}

	public void setIntroduce(String introduce) {
		this.introduce = introduce;
	}

	public String getServiceScope() {
		return serviceScope;
	}

	public void setServiceScope(String serviceScope) {
		this.serviceScope = serviceScope;
	}

	public String getServicePhone() {
		return servicePhone;
	}

	public void setServicePhone(String servicePhone) {
		this.servicePhone = servicePhone;
	}
}
