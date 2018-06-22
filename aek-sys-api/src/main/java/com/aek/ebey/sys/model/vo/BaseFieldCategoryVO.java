package com.aek.ebey.sys.model.vo;

import java.util.List;

/**
 * 字段分类返回VO实体类
 *	
 * @author HongHui
 * @date   2017年7月13日
 */
public class BaseFieldCategoryVO {

	/**
	 * 分类ID
	 */
	private Long id;
	/**
	 * 分类名称
	 */
	private String name;
	
	/**
	 * 子分类集合
	 */
	private List<BaseFieldCategoryVO> subCategoryList;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<BaseFieldCategoryVO> getSubCategoryList() {
		return subCategoryList;
	}

	public void setSubCategoryList(List<BaseFieldCategoryVO> subCategoryList) {
		this.subCategoryList = subCategoryList;
	}
	
}
