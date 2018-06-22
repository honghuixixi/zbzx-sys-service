package com.aek.ebey.sys.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aek.common.core.base.BaseServiceImpl;
import com.aek.common.core.exception.ExceptionFactory;
import com.aek.common.core.serurity.WebSecurityUtils;
import com.aek.common.core.serurity.model.AuthUser;
import com.aek.ebey.sys.enums.DictCascadeStatusEnum;
import com.aek.ebey.sys.enums.DictDependTypeEnum;
import com.aek.ebey.sys.enums.DictManageTypeEnum;
import com.aek.ebey.sys.mapper.BaseDictMapper;
import com.aek.ebey.sys.model.BaseDict;
import com.aek.ebey.sys.model.BaseDictValue;
import com.aek.ebey.sys.model.query.DictQuery;
import com.aek.ebey.sys.service.BaseDictService;
import com.aek.ebey.sys.service.BaseDictValueService;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;

/**
 * 字典表服务实现类
 *	
 * @author HongHui
 * @date   2017年10月11日
 */
@Service
@Transactional
public class BaseDictServiceImpl extends BaseServiceImpl<BaseDictMapper, BaseDict> implements BaseDictService {

	@Autowired
	private BaseDictValueService dictValueService;
	@Autowired
	private BaseDictMapper baseDictMapper;

	@Override
	@Transactional(readOnly = true)
	public Page<BaseDict> findByPage(DictQuery query) {
		Page<BaseDict> page = query.getPage();
		String keyword = StringUtils.trimToNull(query.getKeyword());
		Wrapper<BaseDict> wrapper = new EntityWrapper<BaseDict>();
		//wrapper.eq("tenant_id", query.getTenantId()).eq("enable", true).eq("del_flag", false);
		//wrapper.orNew("tenant_id is null").eq("enable", true).eq("del_flag", false);
		wrapper.andNew("(tenant_id="+query.getTenantId()+" or tenant_id is null) and enable=true and del_flag=false");
		if (null != query.getCascadeStatus()) {
			wrapper.eq("cascade_status", query.getCascadeStatus());
		}
		if (null != query.getManageType()) {
			wrapper.andNew("manage_type="+query.getManageType());
		}
		if (StringUtils.isNotBlank(keyword)) {
			wrapper.andNew("name LIKE {0}", "%" + keyword + "%");
		}
		page = this.selectPage(page, wrapper);
		// 查询字典所拥有字典值的个数
		List<BaseDict> dicts = page.getRecords();
		if (CollectionUtils.isNotEmpty(dicts)) {
			for (BaseDict dict : dicts) {
				dict.setDictValueCount(dictValueService.findCountByDictId(dict.getId()));
			}
		}
		return page;
	}

	@Override
	public void save(BaseDict baseDict) {
		//判断字典名称是否重复
		this.checkRepeat(baseDict.getId(),baseDict.getName());
		AuthUser authUser = WebSecurityUtils.getCurrentUser();
		baseDict.setCreateBy(authUser.getId());
		baseDict.setDelFlag(false);
		baseDict.setEnable(true);
		baseDict.setCreateDate(new Date());
		baseDict.setDependType(DictDependTypeEnum.NO_DEPEND.getNumber()); // 新建字典无依赖
		baseDict.setManageType(DictManageTypeEnum.CUSTOM.getNumber());    // 自定义新建
		this.insert(baseDict);
	}

	@Override
	public void edit(BaseDict baseDict) {
		AuthUser authUser = WebSecurityUtils.getCurrentUser();
		BaseDict currentDict = this.selectById(baseDict.getId());
		if (null == currentDict) {
			throw ExceptionFactory.create("BD_002");
		}
		// 基础字典无法修改
		if (currentDict.getManageType() == DictManageTypeEnum.BASE.getNumber()) {
			throw ExceptionFactory.create("基础字典无法删除和修改");
		}
		//判断字典名称是否重复
		this.checkRepeat(baseDict.getId(),baseDict.getName());
		
		if (currentDict.getCascadeStatus() == DictCascadeStatusEnum.CASCADE.getNumber() 
		    && baseDict.getCascadeStatus() == DictCascadeStatusEnum.ALONE.getNumber()) {
			// 判断当前字典是否已经被级联使用
			BaseDictValue dictValue = this.dictValueService.findByCascadeDictId(baseDict.getId());
			if (dictValue != null) {
				throw ExceptionFactory.create("当前字典已经被级联使用，无法修改为级联状态为独立");
			}
			// 判断当前字典值是否已经级联下级字典
			List<BaseDictValue> dictValues = this.dictValueService.findByDictId(baseDict.getId());
			for (BaseDictValue baseDictValue : dictValues) {
				if (baseDictValue.getCascadeDictId() != null && baseDictValue.getCascadeDictId() > 0) {
					throw ExceptionFactory.create("当前字典的字典值[%s]已级联下级字典，无法修改为级联状态为独立", baseDictValue.getName());
				}
			}
		}
		baseDict.setUpdateBy(authUser.getId());
		baseDict.setUpdateDate(new Date());
		baseDictMapper.updateAllColumnById(baseDict);
	}

	@Override
	public void delete(Long id) {
		BaseDict dict = this.selectById(id);
		if (null == dict) {
			throw ExceptionFactory.create("BD_002");
		}
		// 基础字典无法删除
		if (dict.getManageType() == DictManageTypeEnum.BASE.getNumber()) {
			throw ExceptionFactory.create("基础字典无法删除和修改");
		}
		// 字典被依赖无法删除
		if (dict.getDependType() != DictDependTypeEnum.NO_DEPEND.getNumber()) {
			throw ExceptionFactory.create("字典被依赖无法删除");
		}
		// 字典被级联使用无法删除
		BaseDictValue dictValue = this.dictValueService.findByCascadeDictId(id);
		if (dictValue != null) {
			throw ExceptionFactory.create("当前被字典值[%s]级联使用，无法删除", dictValue.getName());
		}
		this.deleteById(id);
		this.dictValueService.deleteByDictId(id);
	}
	
	/**
	 * 名称重复检查
	 * 
	 * @param dictId
	 * @param dictName
	 */
	private void checkRepeat(Long dictId, String dictName) {
		Wrapper<BaseDict> wrapper = new EntityWrapper<BaseDict>();
		if(null != dictId){
			wrapper.eq("name", dictName).andNew("id != "+dictId);
		}else{
			wrapper.eq("name", dictName);
		}
		if (CollectionUtils.isNotEmpty(this.selectList(wrapper))) {
			throw ExceptionFactory.create("BD_004");
		}
	}

}
