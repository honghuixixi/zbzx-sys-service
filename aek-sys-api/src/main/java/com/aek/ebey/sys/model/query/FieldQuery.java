package com.aek.ebey.sys.model.query;

import com.aek.common.core.base.page.PageHelp;
import com.aek.ebey.sys.model.BaseField;

/**
 * 字段分页查询实体
 *	
 * @author HongHui
 * @date   2017年7月13日
 */
public class FieldQuery extends PageHelp<BaseField> {

	/**
	 * 字段分类ID
	 */
	private Long fieldCategoryId;

	public Long getFieldCategoryId() {
		return fieldCategoryId;
	}

	public void setFieldCategoryId(Long fieldCategoryId) {
		this.fieldCategoryId = fieldCategoryId;
	}

}
