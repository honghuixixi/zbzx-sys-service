package com.aek.ebey.sys.web.request;

import java.util.Date;

import javax.validation.constraints.NotNull;

import com.aek.ebey.sys.web.validator.group.Add;
import com.aek.ebey.sys.web.validator.group.Edit;
import com.baomidou.mybatisplus.annotations.TableField;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 供应商证件信息实体
 *	
 * @author HongHui
 * @date   2017年9月19日
 */
@ApiModel
public class SysSupplierTenantCredentialsRequest extends BaseRequest {

	@NotNull(groups = Edit.class, message = "G_001")
	@ApiModelProperty(value = "证件ID")
	private Long id;
	
	@ApiModelProperty(value = "供应商ID")
	@NotNull(groups={Add.class,Edit.class}, message = "S_008")
	@TableField(value = "tenant_id")
	private Long tenantId;
	
	@ApiModelProperty(value = "证件类型[1=组织机构代码证,2=营业执照证,3=医疗器械经营许可证,4=税务登记证]")
	@NotNull(groups={Add.class,Edit.class}, message = "S_009")
	@TableField(value = "type")
	private Integer type;
	
	@ApiModelProperty(value = "证件编号")
	//@NotNull(groups={Add.class,Edit.class}, message = "S_010")
	@TableField(value = "code")
	private String code;
	
	@ApiModelProperty(value = "证件照")
	//@NotNull(groups={Add.class,Edit.class}, message = "S_011")
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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
}
