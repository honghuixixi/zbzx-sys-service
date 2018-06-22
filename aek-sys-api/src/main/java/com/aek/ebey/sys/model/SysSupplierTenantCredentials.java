package com.aek.ebey.sys.model;

import java.util.Date;

import com.aek.common.core.base.BaseModel;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 供应商证件信息实体
 *	
 * @author HongHui
 * @date   2017年9月19日
 */
@ApiModel
@TableName("sys_tenant_credentials")
public class SysSupplierTenantCredentials extends BaseModel {

	private static final long serialVersionUID = 1082734683968402908L;

	@ApiModelProperty(value = "供应商ID")
	@TableField(value = "tenant_id")
	private Long tenantId;
	
	@ApiModelProperty(value = "证件类型[1=组织机构代码证,2=营业执照证,3=医疗器械经营许可证,4=税务登记证]")
	@TableField(value = "type")
	private Integer type;
	
	@ApiModelProperty(value = "证件编号")
	@TableField(value = "code")
	private String code;
	
	@ApiModelProperty(value = "证件照")
	@TableField(value = "image_url")
	private String imageUrl;
	
	@ApiModelProperty(value = "有效时间")
	@TableField(value = "expire_time")
	private Date expireTime;

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public Date getExpireTime() {
		return expireTime;
	}

	public void setExpireTime(Date expireTime) {
		this.expireTime = expireTime;
	}

	public Long getTenantId() {
		return tenantId;
	}

	public void setTenantId(Long tenantId) {
		this.tenantId = tenantId;
	}
	
}
