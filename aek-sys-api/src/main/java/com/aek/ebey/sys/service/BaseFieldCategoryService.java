package com.aek.ebey.sys.service;

import java.util.List;

import com.aek.common.core.base.BaseService;
import com.aek.ebey.sys.model.BaseFieldCategory;
import com.aek.ebey.sys.model.vo.BaseFieldCategoryVO;

/**
 * 字段分类服务接口
 *	
 * @author HongHui
 * @date   2017年7月13日
 */
public interface BaseFieldCategoryService extends BaseService<BaseFieldCategory> {

	/**
	 * 保存字段分类
	 * @param baseFieldCategory
	 */
	public boolean save(BaseFieldCategory baseFieldCategory);
	
	/**
	 * 编辑字段分类
	 * @param baseFieldCategory
	 */
	public void edit(BaseFieldCategory baseFieldCategory);
	
	/**
	 * 删除字段分类
	 * @param baseFieldCategory
	 */
	public void delete(BaseFieldCategory baseFieldCategory);
	
	/**
	 * 递归查询字段分类集合
	 * @param fieldQuery
	 * @return
	 */
	public List<BaseFieldCategoryVO> findAllCategory();
	
	/**
	 * 查询所有的一级分类集合
	 * @return
	 */
	public List<BaseFieldCategoryVO> findParentCategory();
	
	/**
	 * 根据父类id查询其子类集合
	 * @param parentId
	 * @return
	 */
	public List<BaseFieldCategoryVO> findChildCategoryByParentId(Long parentId);
	
}
