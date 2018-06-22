package com.aek.ebey.sys.service;

import com.aek.common.core.base.BaseService;
import com.aek.ebey.sys.model.BaseDict;
import com.aek.ebey.sys.model.query.DictQuery;
import com.baomidou.mybatisplus.plugins.Page;

/**
 * 字典服务接口类
 *
 * @author HongHui
 * @date   2017年10月11日
 */
public interface BaseDictService extends BaseService<BaseDict> {
	
	/**
	 * 保存字典表
	 * 
	 * @param baseDict
	 */
	void save(BaseDict baseDict);

	/**
	 * 修改字典表
	 * 
	 * @param baseDict
	 */
	void edit(BaseDict baseDict);

	/**
	 * 分页查询字典表信息
	 * 
	 * @param query
	 * @return
	 */
	Page<BaseDict> findByPage(DictQuery query);

	/**
	 * 删除字典
	 * 
	 * @param id
	 */
	void delete(Long id);

}
