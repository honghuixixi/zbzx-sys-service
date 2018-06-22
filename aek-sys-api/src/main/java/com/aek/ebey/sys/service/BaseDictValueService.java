package com.aek.ebey.sys.service;

import java.util.List;

import com.aek.common.core.base.BaseService;
import com.aek.ebey.sys.model.BaseDictValue;

/**
 * 字典值服务接口类
 *
 * @author HongHui
 * @date   2017年10月11日
 */
public interface BaseDictValueService extends BaseService<BaseDictValue> {
	
	/**
	 * 根据字典ID查询字典值数量
	 * 
	 * @param dictId
	 * @return
	 */
	Integer findCountByDictId(Long dictId);

	/**
	 * 根据级联字典表ID查询
	 * 
	 * @param cascadeDictId
	 */
	BaseDictValue findByCascadeDictId(Long cascadeDictId);

	/**
	 * 根据字典ID查询字典值
	 * 
	 * @param dictId
	 * @return
	 */
	List<BaseDictValue> findByDictId(Long dictId);

	/**
	 * 根据字典表ID删除字典值
	 * 
	 * @param dictId
	 */
	boolean deleteByDictId(Long dictId);
	
	/**
	 * 根据字典值ID删除字典值
	 * 
	 * @param dictValueId
	 */
	boolean deleteById(Long dictValueId);

	/**
	 * 字典值新建
	 * 
	 * @param dictValue
	 */
	void save(BaseDictValue dictValue);

	/**
	 * 修改字典值
	 * 
	 * @param dictValue
	 */
	void edit(BaseDictValue dictValue);

	/**
	 * 批量添加字典值
	 * 
	 * @param dictValues
	 */
	void saveBatch(List<BaseDictValue> dictValues);

}
