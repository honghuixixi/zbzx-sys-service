package com.aek.ebey.sys.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aek.common.core.base.BaseServiceImpl;
import com.aek.common.core.exception.ExceptionFactory;
import com.aek.common.core.serurity.WebSecurityUtils;
import com.aek.common.core.serurity.model.AuthUser;
import com.aek.ebey.sys.mapper.BaseDictMapper;
import com.aek.ebey.sys.mapper.BaseDictValueMapper;
import com.aek.ebey.sys.model.BaseDict;
import com.aek.ebey.sys.model.BaseDictValue;
import com.aek.ebey.sys.service.BaseDictService;
import com.aek.ebey.sys.service.BaseDictValueService;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.google.common.collect.Lists;

/**
 * 字典值服务实现类
 *	
 * @author HongHui
 * @date   2017年10月11日
 */
@Service
@Transactional
public class BaseDictValueServiceImpl extends BaseServiceImpl<BaseDictValueMapper, BaseDictValue>
		implements BaseDictValueService {

	@Autowired
	private BaseDictService baseDictService;
	@Autowired
	private BaseDictValueService baseDictValueService;
	@Autowired
	private BaseDictMapper baseDictMapper;

	@Override
	public Integer findCountByDictId(Long dictId) {
		Wrapper<BaseDictValue> wrapper = new EntityWrapper<BaseDictValue>();
		wrapper.eq("dict_id", dictId);
		return this.selectCount(wrapper);
	}

	@Override
	public BaseDictValue findByCascadeDictId(Long cascadeDictId) {
		Wrapper<BaseDictValue> wrapper = new EntityWrapper<BaseDictValue>();
		wrapper.eq("cascade_dict_id", cascadeDictId);
		return this.selectOne(wrapper);
	}

	@Override
	public List<BaseDictValue> findByDictId(Long dictId) {
		Wrapper<BaseDictValue> wrapper = new EntityWrapper<BaseDictValue>();
		wrapper.eq("dict_id", dictId);
		return this.selectList(wrapper);
	}

	@Override
	public boolean deleteByDictId(Long dictId) {
		Wrapper<BaseDictValue> wrapper = new EntityWrapper<BaseDictValue>();
		wrapper.eq("dict_id", dictId);
		return this.delete(wrapper);
	}

	@Override
	public void save(BaseDictValue dictValue) {
		AuthUser authUser = WebSecurityUtils.getCurrentUser();
		dictValue.setCreateBy(authUser.getId());
		dictValue.setCreateDate(new Date());
		// 同一字典集名称不重复
		this.checkRepeat(dictValue.getId(),dictValue.getDictId(), dictValue.getName());
		// 字典默认值修改
		if (dictValue.getDefFlag() != null && dictValue.getDefFlag()) {
			this.setDictValueDefFlag(dictValue.getDictId(), dictValue.getName());
		} else {
			dictValue.setDefFlag(false);
		}
		this.insert(dictValue);
	}

	@Override
	public void edit(BaseDictValue dictValue) {
		AuthUser authUser = WebSecurityUtils.getCurrentUser();
		dictValue.setUpdateBy(authUser.getId());
		dictValue.setUpdateDate(new Date());
		// 同一字典集名称不重复
		this.checkRepeat(dictValue.getId(),dictValue.getDictId(), dictValue.getName());
		// 字典默认值修改
		if (dictValue.getDefFlag() != null && dictValue.getDefFlag()) {
			this.setDictValueDefFlag(dictValue.getDictId(), dictValue.getName());
		} else {
			BaseDict dict = this.baseDictService.selectById(dictValue.getDictId());
			BaseDictValue oldBaseDictValue = this.baseDictValueService.selectById(dictValue.getId());
			if(null != dict && StringUtils.isNotBlank(dict.getValue()) && dict.getValue().equals(oldBaseDictValue.getName())){
				//清空字典默认值
				dict.setValue(null);
				baseDictMapper.updateAllColumnById(dict);
			}
		}
		this.updateById(dictValue);
	}

	/**
	 * 名称重复检查
	 * 
	 * @param dictValueId
	 * @param dictId
	 * @param dictValueName
	 */
	private void checkRepeat(Long dictValueId,Long dictId, String dictValueName) {
		Wrapper<BaseDictValue> wrapper = new EntityWrapper<BaseDictValue>();
		if(null != dictValueId){
			wrapper.eq("name", dictValueName).andNew("dict_id = "+dictId).andNew("id != "+dictValueId);
		}else{
			wrapper.eq("name", dictValueName).andNew("dict_id = "+dictId);
		}
		if (CollectionUtils.isNotEmpty(this.selectList(wrapper))) {
			throw ExceptionFactory.create("BDV_002");
		}
	}
	
	/**
	 * 名称重复检查
	 * 
	 * @param dictValueId
	 * @param dictId
	 * @param dictValueName
	 */
	private boolean checkDictValueRepeat(Long dictValueId,Long dictId, String dictValueName) {
		Wrapper<BaseDictValue> wrapper = new EntityWrapper<BaseDictValue>();
		if(null != dictValueId){
			wrapper.eq("name", dictValueName).andNew("dict_id = "+dictId).andNew("id != "+dictValueId);
		}else{
			wrapper.eq("name", dictValueName).andNew("dict_id = "+dictId);
		}
		if (CollectionUtils.isNotEmpty(this.selectList(wrapper))) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 设置字典默认值 1.修改已设置默认的字典值 2.设置字典表的默认字典值
	 * 
	 * @param dictId 字典Id
	 * @param defName 默认值名称
	 */
	private void setDictValueDefFlag(Long dictId, String defName) {
		BaseDictValue editDictValue = new BaseDictValue();
		editDictValue.setDefFlag(false);
		Wrapper<BaseDictValue> wrapper = new EntityWrapper<BaseDictValue>();
		wrapper.eq("dict_id", dictId).eq("def_flag", true);
		this.update(editDictValue, wrapper);
		// 更新字典表默认值
		BaseDict dict = this.baseDictService.selectById(dictId);
		dict.setValue(defName);
		this.baseDictService.updateById(dict);
	}

	@Override
	public void saveBatch(List<BaseDictValue> dictValues) {
		List<BaseDictValue> newDictValues = Lists.newArrayList();
		for (BaseDictValue baseDictValue : dictValues) {
			if(!checkDictValueRepeat(baseDictValue.getId(),baseDictValue.getDictId(),baseDictValue.getName())){
				newDictValues.add(baseDictValue);
			}
		}
		if(newDictValues.size() > 0){
			this.insertBatch(newDictValues);
		}
	}

	@Override
	public boolean deleteById(Long dictValueId) {
		//如果删除当前的字典值为字典表的默认值，则需清空字典值的默认值
		BaseDictValue baseDictValue = this.baseDictValueService.selectById(dictValueId);
		if(null != baseDictValue && baseDictValue.getDefFlag()){
			BaseDict baseDict = baseDictMapper.selectById(baseDictValue.getDictId());
			baseDict.setValue(null);
			baseDictMapper.updateAllColumnById(baseDict);
		}
		Wrapper<BaseDictValue> wrapper = new EntityWrapper<BaseDictValue>();
		wrapper.eq("id", dictValueId);
		return this.delete(wrapper);
	}

	 
}
