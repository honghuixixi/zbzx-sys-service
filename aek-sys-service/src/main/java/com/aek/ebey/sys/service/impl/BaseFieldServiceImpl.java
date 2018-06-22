package com.aek.ebey.sys.service.impl;

import java.util.Date;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aek.common.core.base.BaseServiceImpl;
import com.aek.common.core.serurity.WebSecurityUtils;
import com.aek.common.core.serurity.model.AuthUser;
import com.aek.ebey.sys.mapper.BaseFieldMapper;
import com.aek.ebey.sys.model.BaseField;
import com.aek.ebey.sys.model.query.FieldQuery;
import com.aek.ebey.sys.service.BaseFieldService;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;

/**
 * 字段服务实现类
 *	
 * @author HongHui
 * @date   2017年7月13日
 */
@Service
@Transactional
public class BaseFieldServiceImpl extends BaseServiceImpl<BaseFieldMapper, BaseField> implements BaseFieldService {

	@Override
	public void save(BaseField baseField) {
		AuthUser authUser = WebSecurityUtils.getCurrentUser();
		baseField.setDelFlag(false);
		baseField.setEnable(true);
		baseField.setCreateDate(new Date());
		baseField.setCreateBy(authUser.getId());
		baseField.setUpdateDate(new Date());
		this.insert(baseField);
	}

	@Override
	public void edit(BaseField baseField) {
		baseField.setUpdateBy(WebSecurityUtils.getCurrentUser().getId());
		baseField.setUpdateDate(new Date());
		this.updateById(baseField);
	}

	@Override
	public void delete(BaseField baseField) {
		baseField.setDelFlag(true);
		baseField.setUpdateBy(WebSecurityUtils.getCurrentUser().getId());
		baseField.setUpdateDate(new Date());
		this.updateById(baseField);
	}

	@Override
	public Page<BaseField> findByPage(FieldQuery fieldQuery) {
		Page<BaseField> page = fieldQuery.getPage();
		Wrapper<BaseField> wrapper = new EntityWrapper<BaseField>();
		wrapper.eq("del_flag", false);
		if(null != fieldQuery.getFieldCategoryId()) {
			wrapper.eq("field_category_id", fieldQuery.getFieldCategoryId());
		}
		page = this.selectPage(page, wrapper);
		return page;
	}
	
}
