package com.aek.ebey.sys.service;

import com.aek.ebey.sys.model.SysArea;

import java.util.List;

import com.aek.common.core.base.BaseService;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author aek
 * @since 2017-05-06
 */
public interface SysAreaService extends BaseService<SysArea> {
	List<SysArea> queryByIdAndLevel(Long areaId, Integer level);

	List<SysArea> queryByParentIdAndLevel(Long parentId, Integer level);

	List<SysArea> queryByLevel(Integer level);

	/**
	 * 获取所有的省
	 * 
	 * @return
	 */
	List<SysArea> queryProvince();

	/**
	 * 获取所有的市
	 * 
	 * @return
	 */
	List<SysArea> queryCity();

	/**
	 * 获取所有的县/区
	 * 
	 * @return
	 */
	List<SysArea> queryRegion();

	/**
	 * 根据省获取下面的市
	 *
	 * @param parentId
	 * @return
	 */
	List<SysArea> queryTown(Long parentId);

	/**
	 * 获取省下区县等
	 * 
	 * @param parentId
	 * @return
	 */
	List<SysArea> queryRegion(Long parentId);

	/**
	 * 根据名称查询 区域是否存在
	 * 
	 * @param provinceName
	 *            省名称
	 * @param level
	 *            0 省 1市 2县/区
	 * @return
	 */
	SysArea findByName(String name, int level);

	/**
	 * 根据省名称判断是否存在
	 * 
	 * @param provinceName
	 *            省名称
	 * @return
	 */
	boolean isProvinceNameExist(String provinceName);

	/**
	 * 判断市是否存在，省下级的市
	 * 
	 * @param provinceName
	 *            省名称
	 * @param cityName
	 *            市名称
	 * @return
	 */
	boolean isCityNameExist(String provinceName, String cityName);

	/**
	 * 判断县/区是否存在，市下级县/区
	 * 
	 * @param cityName
	 *            市名称
	 * @param regionName
	 *            县/区名称
	 * @return
	 */
	boolean isRegionNameExist(String cityName, String regionName);

}
