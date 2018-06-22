package com.aek.ebey.sys.web.request;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.aek.ebey.sys.web.validator.group.Add;
import com.google.common.collect.Lists;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 租户模块保存请求类
 * 
 * @author Mr.han
 *
 */
@ApiModel(value = "租户模块保存")
public class TenantModuleRequest {
	/**
	 * 租户ID
	 */
	@ApiModelProperty(value = "租户ID")
	@NotNull(groups = { Add.class }, message = "G_001")
	private Long tenantId;
	/**
	 * 模块ID集合
	 */
	@ApiModelProperty(value = "模块ID集合")
	private List<Long> moduleIds = Lists.newArrayList();

	public Long getTenantId() {
		return tenantId;
	}

	public void setTenantId(Long tenantId) {
		this.tenantId = tenantId;
	}

	public List<Long> getModuleIds() {
		return moduleIds;
	}

	public void setModuleIds(List<Long> moduleIds) {
		this.moduleIds = moduleIds;
	}

}
