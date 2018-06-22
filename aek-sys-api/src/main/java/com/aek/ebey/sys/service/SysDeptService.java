package com.aek.ebey.sys.service;

import java.util.List;

import com.aek.common.core.base.BaseService;
import com.aek.ebey.sys.model.SysDept;
import com.aek.ebey.sys.model.SysTenant;
import com.aek.ebey.sys.model.query.DeptQuery;
import com.aek.ebey.sys.model.vo.SysDeptVo;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author aek
 * @since 2017-05-06
 */
public interface SysDeptService extends BaseService<SysDept> {

	/**
	 * 初始化租户部门信息
	 * 
	 * @param tenant
	 */
	void initTenantDept(SysTenant tenant);
	
	/**
	 * 初始化供应商部门
	 * @param tenant
	 */
	void initSupplierTenantDept(SysTenant tenant);

	/**
	 * 获取机构根部门信息
	 * 
	 * @param tenantId
	 *            机构ID
	 * @return
	 */
	SysDept findRootDeptByTenantId(Long tenantId);

	/**
	 * 添加部门
	 * 
	 * @param dept
	 */
	void save(SysDept dept);

	/**
	 * 修改组织
	 * 
	 * @param sysDept
	 */
	void edit(SysDept dept);

	/**
	 * 删除组织节点 检查组织下是否有分支，是否存在人员，存在则不能删除
	 * 
	 * @param id
	 *            组织ID
	 * @param forceDel
	 *            强制删除
	 */
	void delete(Long id, boolean forceDel);

	/**
	 * 根据部门名称和租户ID
	 * 
	 * @param name
	 * @param tenantId
	 * @return
	 */
	SysDept findByNameAndTenantId(String name, Long tenantId);

	/**
	 * 检索组织部门信息
	 * 
	 * @param query
	 * @return
	 */
	List<SysDept> findByKeyword(DeptQuery query);
	
	/**
	 * 检索组织部门信息（过滤登录人所在部门）
	 * 
	 * @param query
	 * @return
	 */
	List<SysDept> findByKeywordExcludeSelf(DeptQuery query);

	/**
	 * 根据租户ID查询部门组织树
	 * 
	 * @param tenantId
	 *            租户ID
	 * @return
	 */
	SysDeptVo findDeptTree(Long tenantId);

	/**
	 * 递归查询部门树
	 * 
	 * @param dept
	 * @return
	 */
	List<SysDeptVo> findByRecursion(SysDeptVo dept);

	/**
	 * 查询子部门(一级子部门)
	 * 
	 * @param parentId
	 * @return
	 */
	List<SysDept> findListByParentId(Long parentId);

	/**
	 * 查询所有的子部门（包含子部门的子部门）
	 * 
	 * @param deptId
	 * @return
	 */
	List<SysDept> findAllSubDept(Long deptId);

	/**
	 * 查询所有子部门 （包含子部门的子部门）
	 * 
	 * @param deptId
	 * @return 字符串拼接 ids [1,2,3,4]
	 */
	String findAllSubDeptStr(Long deptId);

	/**
	 * 批量查询部门信息
	 * 
	 * @param ids
	 * @return
	 */
	List<SysDept> findByIds(String ids);
	
	/**
	 * 批量查询部门信息
	 * 
	 * @param ids
	 * @return
	 */
	List<SysDept> findByIds(Long[] ids);

}
