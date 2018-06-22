package com.aek.ebey.sys.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aek.common.core.base.BaseServiceImpl;
import com.aek.common.core.serurity.WebSecurityUtils;
import com.aek.ebey.sys.enums.FieldCategoryTypeEnum;
import com.aek.ebey.sys.mapper.BaseFieldCategoryMapper;
import com.aek.ebey.sys.model.BaseFieldCategory;
import com.aek.ebey.sys.model.vo.BaseFieldCategoryVO;
import com.aek.ebey.sys.service.BaseFieldCategoryService;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;

/**
 * 字段分类服务实现类
 *	
 * @author HongHui
 * @date   2017年7月13日
 */
@Service
@Transactional
public class BaseFieldCategoryServiceImpl extends BaseServiceImpl<BaseFieldCategoryMapper, BaseFieldCategory> implements BaseFieldCategoryService {

	@Override
	public boolean save(BaseFieldCategory baseFieldCategory) {
		baseFieldCategory.setDelFlag(false);
		baseFieldCategory.setType(FieldCategoryTypeEnum.CUSTOM_TYPE.getNumber());
		baseFieldCategory.setCreateBy(WebSecurityUtils.getCurrentUser().getId());
		baseFieldCategory.setCreateDate(new Date());
		return this.insert(baseFieldCategory);
	}

	@Override
	public void edit(BaseFieldCategory baseFieldCategory) {
		baseFieldCategory.setUpdateBy(WebSecurityUtils.getCurrentUser().getId());
		baseFieldCategory.setUpdateDate(new Date());
		this.updateById(baseFieldCategory);
	}

	@Override
	public void delete(BaseFieldCategory baseFieldCategory) {
		baseFieldCategory.setDelFlag(true);
		baseFieldCategory.setUpdateBy(WebSecurityUtils.getCurrentUser().getId());
		baseFieldCategory.setUpdateDate(new Date());
		this.updateById(baseFieldCategory);
	}

	@Override
	public List<BaseFieldCategoryVO> findAllCategory() {
		List<BaseFieldCategoryVO> categoryList = this.findParentCategory();
		for (BaseFieldCategoryVO baseFieldCategoryVO : categoryList) {
			List<BaseFieldCategoryVO> subCategoryList = this.findChildCategoryByParentId(baseFieldCategoryVO.getId());
			baseFieldCategoryVO.setSubCategoryList(subCategoryList);
		}
		return categoryList;
	}

	@Override
	public List<BaseFieldCategoryVO> findParentCategory() {
		Wrapper<BaseFieldCategory> wrapper = new EntityWrapper<BaseFieldCategory>();
		wrapper.isNull("parent_id").eq("del_flag", false);
		List<BaseFieldCategory> parentCategoryList = this.selectList(wrapper);
		List<BaseFieldCategoryVO> baseFieldCategoryVOList = new ArrayList<BaseFieldCategoryVO>();
		for (BaseFieldCategory baseFieldCategory : parentCategoryList) {
			BaseFieldCategoryVO baseFieldCategoryVO = new BaseFieldCategoryVO();
			baseFieldCategoryVO.setId(baseFieldCategory.getId());
			baseFieldCategoryVO.setName(baseFieldCategory.getName());
			baseFieldCategoryVOList.add(baseFieldCategoryVO);
		}
		return baseFieldCategoryVOList;
	}

	@Override
	public List<BaseFieldCategoryVO> findChildCategoryByParentId(Long parentId) {
		Wrapper<BaseFieldCategory> wrapper = new EntityWrapper<BaseFieldCategory>();
		wrapper.eq("parent_id",parentId).eq("del_flag", false).orderBy("type", true);
		List<BaseFieldCategory> subCategoryList = this.selectList(wrapper);
		List<BaseFieldCategoryVO> baseFieldCategoryVOList = new ArrayList<BaseFieldCategoryVO>();
		for (BaseFieldCategory baseFieldCategory : subCategoryList) {
			BaseFieldCategoryVO baseFieldCategoryVO = new BaseFieldCategoryVO();
			baseFieldCategoryVO.setId(baseFieldCategory.getId());
			baseFieldCategoryVO.setName(baseFieldCategory.getName());
			baseFieldCategoryVOList.add(baseFieldCategoryVO);
		}
		return baseFieldCategoryVOList;
	}
	
}
