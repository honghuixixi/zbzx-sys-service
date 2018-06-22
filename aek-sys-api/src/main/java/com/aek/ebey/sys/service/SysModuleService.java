package com.aek.ebey.sys.service;

import com.aek.ebey.sys.model.SysModule;
import com.aek.ebey.sys.model.query.ModuleQuery;
import com.baomidou.mybatisplus.plugins.Page;

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
public interface SysModuleService extends BaseService<SysModule> {

	/**
	 * 根据租户类型判断该租户可拥有模块 如: 医疗机构不可拥有监管模块，供应商不可拥有资产模块等
	 * 
	 * @return
	 */
	List<SysModule> findModuleByTenantIdWithCan(Long tenantId);

	/**
	 * 查询已启用的系统模块列表
	 * 
	 * @return
	 */
	List<SysModule> findAllModuleWithNotDel();

	/**
	 * 根据模块名称查询
	 * 
	 * @param name
	 * @return
	 */
	List<SysModule> findByName(String name);

	/**
	 * 判断模块是否为默认拥有
	 * 
	 * @param moduleId
	 *            模块ID
	 * @return
	 */
	boolean isDefaultExist(Long moduleId);

	/**
	 * 添加新模块
	 * 
	 * @param module
	 *            模块数据
	 */
	void add(SysModule module);

	/**
	 * 修改模块
	 * 
	 * @param module
	 *            模块数据
	 */
	void edit(SysModule module);

	/**
	 * 模块删除
	 */
	void delete(Long moduleId);

	/**
	 * 分页查询模块信息
	 * 
	 * @param query
	 * @return
	 */
	Page<SysModule> findPageByKeyword(ModuleQuery query);

}
