package com.aek.ebey.sys.service;

import com.aek.common.core.base.BaseService;
import com.aek.ebey.sys.model.BaseField;
import com.aek.ebey.sys.model.query.FieldQuery;
import com.baomidou.mybatisplus.plugins.Page;

/**
 * 字段服务接口
 *	
 * @author HongHui
 * @date   2017年7月13日
 */
public interface BaseFieldService extends BaseService<BaseField> {

	/**
	 * 保存字段
	 * @param baseField
	 */
	public void save(BaseField baseField);
	
	/**
	 * 编辑字段
	 * @param baseField
	 */
	public void edit(BaseField baseField);
	
	/**
	 * 删除字段
	 * @param baseField
	 */
	public void delete(BaseField baseField);
	
	/**
	 * 分页查询字段列表信息
	 * @param fieldQuery
	 * @return
	 */
	public Page<BaseField> findByPage(FieldQuery fieldQuery);
	
}
