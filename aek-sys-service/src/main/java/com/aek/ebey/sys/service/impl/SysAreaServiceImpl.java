package com.aek.ebey.sys.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aek.common.core.base.BaseServiceImpl;
import com.aek.ebey.sys.mapper.SysAreaMapper;
import com.aek.ebey.sys.model.SysArea;
import com.aek.ebey.sys.service.SysAreaService;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;

/**
 * <p>
 * 系统api表 服务实现类
 * </p>
 *
 * @author aek
 * @since 2017-05-06
 */
@Service
@Transactional
public class SysAreaServiceImpl extends BaseServiceImpl<SysAreaMapper, SysArea> implements SysAreaService {

	private static final Integer PROVINCE = 0;
	private static final Integer TOWN = 1;
	private static final Integer REGION = 2;

	@Autowired
	private SysAreaMapper sysAreaMapper;

	public List<SysArea> queryByIdAndLevel(Long areaId, Integer level) {
		return sysAreaMapper.queryByIdAndLevel(areaId, level);
	}

	public List<SysArea> queryByParentIdAndLevel(Long parentId, Integer level) {
		return sysAreaMapper.queryByParentIdAndLevel(parentId, level);
	}

	public List<SysArea> queryByLevel(Integer level) {
		return sysAreaMapper.queryByLevel(level);
	}

	@Override
	public List<SysArea> queryProvince() {
		return this.queryByLevel(PROVINCE);
	}

	@Override
	public List<SysArea> queryCity() {
		return this.queryByLevel(TOWN);
	}

	@Override
	public List<SysArea> queryRegion() {
		return this.queryByLevel(REGION);
	}

	@Override
	public List<SysArea> queryTown(Long parentId) {
		return this.queryByParentIdAndLevel(parentId, TOWN);
	}

	@Override
	public List<SysArea> queryRegion(Long parentId) {
		return this.queryByParentIdAndLevel(parentId, REGION);
	}

	@Override
	public SysArea findByName(String name, int level) {
		Wrapper<SysArea> wrapper = new EntityWrapper<SysArea>();
		wrapper.eq("name", name).eq("level", level);
		return this.selectOne(wrapper);
	}

	@Override
	public boolean isProvinceNameExist(String provinceName) {
		Wrapper<SysArea> wrapper = new EntityWrapper<SysArea>();
		wrapper.eq("name", provinceName).eq("level", PROVINCE);
		return this.selectCount(wrapper) > 0;
	}

	@Override
	public boolean isCityNameExist(String provinceName, String cityName) {
		SysArea province = this.findByName(provinceName, PROVINCE);
		if (province == null) {
			return false;
		}
		Wrapper<SysArea> wrapper = new EntityWrapper<SysArea>();
		wrapper.eq("name", cityName).eq("level", TOWN).eq("parent_id", province.getId());
		return this.selectCount(wrapper) > 0;
	}

	@Override
	public boolean isRegionNameExist(String cityName, String regionName) {
		SysArea city = this.findByName(cityName, TOWN);
		if (city == null) {
			return false;
		}
		Wrapper<SysArea> wrapper = new EntityWrapper<SysArea>();
		wrapper.eq("name", regionName).eq("level", REGION).eq("parent_id", city.getId());
		return this.selectCount(wrapper) > 0;
	}

}