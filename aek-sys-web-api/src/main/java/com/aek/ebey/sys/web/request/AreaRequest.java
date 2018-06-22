package com.aek.ebey.sys.web.request;

/**
 * Created by rookie on 2017/3/6 0006.
 */
public class AreaRequest {

	/**
	 * 区域编码
	 */
	private String code;

	/**
	 * 区域类型(省/市/县)
	 */
	private String type;

	/**
	 * 区域名称
	 */
	protected String name;
	/**
	 * 排序
	 */
	protected Integer sort;

	/**
	 * 父类Id
	 */
	private Long parentId;

	/**
	 * 父类id集合 <code>1,2,3</code>
	 */
	private String parentIds;

	/**
	 * 备注
	 */
	private String remark;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
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

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
}
