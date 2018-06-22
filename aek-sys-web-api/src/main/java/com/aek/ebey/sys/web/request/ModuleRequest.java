package com.aek.ebey.sys.web.request;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import com.aek.ebey.sys.web.validator.group.Add;
import com.aek.ebey.sys.web.validator.group.Edit;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 模块创建 修改
 * 
 * @author Mr.han
 *
 */
@ApiModel
public class ModuleRequest extends BaseRequest {

	@ApiModelProperty(value = "模块ID")
	@NotNull(groups = { Edit.class }, message = "G_001")
	private Long id;

	@ApiModelProperty(value = "模块名称")
	@NotEmpty(groups = { Add.class }, message = "M_002")
	private String name;

	@ApiModelProperty(value = "版本号")
	@NotEmpty(groups = { Add.class }, message = "M_003")
	private String versionNumber;

	@ApiModelProperty(value = "1.业务应用、2.系统应用、3.数据分析应用")
	@NotNull(groups = { Add.class }, message = "M_004")
	private Long moduleType;

	@ApiModelProperty(value = "路由地址")
	@NotEmpty(groups = { Add.class }, message = "M_005")
	private String url;

	@ApiModelProperty(value = "是否默认拥有(机构创建时默认开通拥有此模块)")
	@NotNull(groups = { Add.class }, message = "M_007")
	private Boolean defaultExist;

	// 当前默认所有模块适用所有机构类型，前端不用传该值,默认获取所有
	// @ApiModelProperty(value = "适用范围(当前模块可被哪些机构类型所使用，1.医疗机构、2.监管机构)")
	// @NotEmpty(groups = { Add.class }, message = "M_006")
	private List<Integer> tenantType;

	@ApiModelProperty(value = "描述信息")
	@NotNull(groups = { Add.class }, message = "M_008")
	private String description;

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the versionNumber
	 */
	public String getVersionNumber() {
		return versionNumber;
	}

	/**
	 * @param versionNumber
	 *            the versionNumber to set
	 */
	public void setVersionNumber(String versionNumber) {
		this.versionNumber = versionNumber;
	}

	/**
	 * @return the moduleType
	 */
	public Long getModuleType() {
		return moduleType;
	}

	/**
	 * @param moduleType
	 *            the moduleType to set
	 */
	public void setModuleType(Long moduleType) {
		this.moduleType = moduleType;
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url
	 *            the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return the defaultExist
	 */
	public Boolean getDefaultExist() {
		return defaultExist;
	}

	/**
	 * @param defaultExist
	 *            the defaultExist to set
	 */
	public void setDefaultExist(Boolean defaultExist) {
		this.defaultExist = defaultExist;
	}

	/**
	 * @return the tenantType
	 */
	public List<Integer> getTenantType() {
		return tenantType;
	}

	/**
	 * @param tenantType
	 *            the tenantType to set
	 */
	public void setTenantType(List<Integer> tenantType) {
		this.tenantType = tenantType;
	}

}
