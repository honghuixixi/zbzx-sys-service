package com.aek.ebey.sys.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aek.common.core.BeanMapper;
import com.aek.common.core.Result;
import com.aek.common.core.base.BaseServiceImpl;
import com.aek.common.core.exception.ExceptionFactory;
import com.aek.common.core.serurity.WebSecurityUtils;
import com.aek.common.core.serurity.model.AuthUser;
import com.aek.common.core.util.PinyinUtil;
import com.aek.ebey.sys.core.SysConstants;
import com.aek.ebey.sys.mapper.SysDeptMapper;
import com.aek.ebey.sys.model.SysDept;
import com.aek.ebey.sys.model.SysTenant;
import com.aek.ebey.sys.model.SysUser;
import com.aek.ebey.sys.model.query.DeptQuery;
import com.aek.ebey.sys.model.vo.SysDeptVo;
import com.aek.ebey.sys.service.SysDeptService;
import com.aek.ebey.sys.service.SysTenantService;
import com.aek.ebey.sys.service.SysUserService;
import com.aek.ebey.sys.service.ribbon.AssetClientService;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.google.common.collect.Lists;

/**
 * <p>
 * 部门表 服务实现类
 * </p>
 *
 * @author aek
 * @since 2017-05-06
 */
@Service
@Transactional
public class SysDeptServiceImpl extends BaseServiceImpl<SysDeptMapper, SysDept> implements SysDeptService {
	
	private static Logger logger  = LoggerFactory.getLogger(SysDeptServiceImpl.class);

	@Autowired
	private SysDeptMapper deptMapper;

	@Autowired
	private SysUserService sysUserService;

	@Autowired
	private SysTenantService sysTenantService;

	@Autowired
	private SysDeptService deptService;

	@Autowired
	private AssetClientService deviceService;

	@Override
	public void initTenantDept(SysTenant tenant) {
		Date currTime = new Date();
		// 根部门
		SysDept dept = new SysDept();
		dept.setTenantId(tenant.getId());
		dept.setName(tenant.getName());
		dept.setNamePy(PinyinUtil.cn2py(tenant.getName()));
		dept.setSort(0);
		dept.setParentId(SysConstants.ROOT_PARENT);
		dept.setParentIds(SysConstants.ROOT_PARENT_STR);
		dept.setCreateTime(currTime);
		dept.setPreset(true);
		this.deptService.insert(dept);

		List<SysDept> depts = new ArrayList<SysDept>();

		for (int i = 0; i < 3; i++) {
			SysDept newDept = new SysDept();
			newDept.setTenantId(tenant.getId());

			if (i == 0) {
				newDept.setName("临床工程部");
				newDept.setNamePy(PinyinUtil.cn2py(newDept.getName()));
				newDept.setSort(i);
			}

			if (i == 1) {
				newDept.setName("信息科");
				newDept.setNamePy(PinyinUtil.cn2py(newDept.getName()));
				newDept.setSort(i);
			}
			
			if (i == 2) {
				newDept.setName("后勤部");
				newDept.setNamePy(PinyinUtil.cn2py(newDept.getName()));
				newDept.setSort(i);
			}

			newDept.setParentId(dept.getId());
			newDept.setParentIds(dept.getParentIds() + "," + dept.getId());
			newDept.setCreateTime(currTime);
			newDept.setPreset(true);
			depts.add(newDept);
		}
		this.deptService.insertBatch(depts);
	}
	
	@Override
	public void initSupplierTenantDept(SysTenant tenant) {
		Date currTime = new Date();
		// 根部门
		SysDept dept = new SysDept();
		dept.setTenantId(tenant.getId());
		dept.setName(tenant.getName());
		dept.setNamePy(PinyinUtil.cn2py(tenant.getName()));
		dept.setSort(0);
		dept.setParentId(SysConstants.ROOT_PARENT);
		dept.setParentIds(SysConstants.ROOT_PARENT_STR);
		dept.setCreateTime(currTime);
		dept.setPreset(true);
		this.deptService.insert(dept);
	}

	@Override
	@Transactional(readOnly = true)
	public SysDept findRootDeptByTenantId(Long tenantId) {
		SysDept query = new SysDept();
		query.setTenantId(tenantId);
		query.setParentId(SysConstants.ROOT_PARENT);
		query.setParentIds(SysConstants.ROOT_PARENT_STR);
		query.setDelFlag(false);
		query.setEnable(true);
		return this.deptMapper.selectOne(query);
	}

	@Override
	public void save(SysDept dept) {
		Date date = new Date();
		// 判断机构存在
		if (this.sysTenantService.selectById(dept.getTenantId()) == null) {
			throw ExceptionFactory.create("O_008");
		}

		// 部门名称不能重复
		if (this.findByNameAndTenantId(dept.getName(), dept.getTenantId()) != null) {
			throw ExceptionFactory.create("D_011");
		}

		dept.setNamePy(PinyinUtil.cn2py(dept.getName()));
		dept.setPreset(false);
		dept.setCreateTime(date);

		this.initParentIds(dept);
		this.insert(dept);
	}

	@Override
	public void edit(SysDept dept) {

		// 判断部门存在
		SysDept curSysDept = this.selectById(dept.getId());

		if (curSysDept == null) {
			throw ExceptionFactory.create("G_001");
		}

		// 判断是否为预设部门
		/*if (curSysDept.getPreset()) {
			throw ExceptionFactory.create("D_013");
		}*/

		// 科室名称不能重复
		if (StringUtils.isNotBlank(dept.getName())) {
			if (!StringUtils.equals(dept.getName(), curSysDept.getName())) {
				if (this.findByNameAndTenantId(dept.getName(), dept.getTenantId()) != null) {
					throw ExceptionFactory.create("D_011");
				}
				dept.setNamePy(PinyinUtil.cn2py(dept.getName()));
			}
		}

		this.initParentIds(dept);
		this.updateById(dept);

		// 修改部门节点
		if (dept.getParentId() != null && curSysDept.getParentId() != dept.getParentId()) {
			this.updateDeptRecursion(dept);
		}

		// 更新用户信息
		if (StringUtils.isNotBlank(dept.getName())) {
			this.sysUserService.updateByDeptId(dept);
		}

	}

	/**
	 * 递归更新子部门
	 * 
	 * @param dept
	 */
	private void updateDeptRecursion(SysDept dept) {
		List<SysDept> subDepts = this.findListByParentId(dept.getId());
		if (CollectionUtils.isNotEmpty(subDepts)) {
			for (SysDept subDept : subDepts) {
				// 更新子部门 parentIds
				String parentIds = dept.getParentIds() + "," + subDept.getParentId();
				subDept.setParentIds(parentIds);

				this.updateById(subDept);
				this.updateDeptRecursion(subDept);
			}
		}
	}

	/**
	 * 初始化父ids
	 * 
	 * @param dept
	 */
	private void initParentIds(SysDept dept) {
		Long parentId = dept.getParentId();
		SysDept parentDept = this.selectById(parentId);

		if (parentDept != null) {
			String parentIds = parentDept.getParentIds() + "," + dept.getParentId();
			dept.setParentIds(parentIds);
		}
	}

	@Override
	public void delete(Long deptId, boolean forceDel) {
		SysDept dept = this.selectById(deptId);

		if (dept == null) {
			throw ExceptionFactory.create("G_001");
		}

		// 判断是否为预设部门
		if (dept.getPreset()) {
			throw ExceptionFactory.create("D_012");
		}

		List<SysUser> subDeptUsers = Lists.newArrayList();

		// 判断是否存在子部门
		List<SysDept> subAllDept = this.findAllSubDept(deptId);
		
	
		
		if (CollectionUtils.isNotEmpty(subAllDept)) {
			// 加入当前部门
			subAllDept.add(dept);
			for (SysDept subDept : subAllDept) {
				// 存在启用中用户不能删除，停用则自动归属上级部门
				List<SysUser> users = this.sysUserService.findByDeptId(subDept.getId());
				if (CollectionUtils.isNotEmpty(users)) {
					for (SysUser user : users) {
						if (user.getEnable()) {
							throw ExceptionFactory.create("D_008");
						}
					}
					subDeptUsers.addAll(users);
				}
			}
		}else{
			// 不存在子部门的时候，存在启用中用户不能删除，停用则自动归属上级部门
			List<SysUser> users = this.sysUserService.findByDeptId(dept.getId());
			if (CollectionUtils.isNotEmpty(users)) {
				for (SysUser user : users) {
					if (user.getEnable()) {
						throw ExceptionFactory.create("D_008");
					}
				}
			}
		}

		// 判断是否存在设备
		Long[] depts = null;
		if (CollectionUtils.isNotEmpty(subAllDept)) {
			
			depts = new Long[subAllDept.size()];
			for (int i = 0; i < depts.length; i++) {
				SysDept subDept = subAllDept.get(i);
				depts[i] = subDept.getId();
			}
		} else {
			// 不存在下级部门只查询当前部门
			depts = new Long[] { deptId };
		}
		Result<Boolean> result = deviceService.deviceQuery(depts, WebSecurityUtils.getCurrentToken());
		if (result.getData()) {
			throw ExceptionFactory.create("D_014");
		}
		
		// 不强制删除并且子部门不为空，提示
		if(!forceDel && CollectionUtils.isNotEmpty(subAllDept)){
			throw ExceptionFactory.create("D_015");
		}
		
		// 设置作废
		if (forceDel) {
			dept.setDelFlag(true);
			this.updateById(dept);
			logger.info("当前部门："+dept.getId()+",上级部门："+dept.getParentId());
			logger.info("当前部门下所有用户："+subDeptUsers.size());
			
			//删除子部门
			if(CollectionUtils.isNotEmpty(subAllDept)){
				for (SysDept sysDept : subAllDept) {
					sysDept.setDelFlag(true);
				}
				this.updateBatchById(subAllDept);
			}
			
			//当前部门用户,若当前部门被删除，则将该部分下的用户归属到当前部门的上级部门
			List<SysUser> currentDeptUsers = this.sysUserService.findByDeptId(dept.getId());
			subDeptUsers.addAll(currentDeptUsers);
			
			// 修改停用用户归属部门,直接归属上级部门
			if (CollectionUtils.isNotEmpty(subDeptUsers)) {
				SysDept parentDept = this.selectById(dept.getParentId());
				
				for (SysUser user : subDeptUsers) {
					user.setDeptId(parentDept.getId());
					user.setDeptName(parentDept.getName());
				}
				this.sysUserService.updateBatchById(subDeptUsers);
			}
		}
	}

	@Override
	@Transactional(readOnly = true)
	public SysDept findByNameAndTenantId(String name, Long tenantId) {
		SysDept dept = new SysDept();
		dept.setName(name);
		dept.setTenantId(tenantId);
		dept.setDelFlag(false);
		return this.deptMapper.selectOne(dept);
	}

	@Override
	@Transactional(readOnly = true)
	public List<SysDept> findByKeyword(DeptQuery query) {
		Wrapper<SysDept> wrapper = new EntityWrapper<SysDept>();
		wrapper.eq("tenant_id", query.getTenantId()).eq("del_flag", false);

		if (StringUtils.isNotBlank(query.getKeyword())) {
			wrapper.andNew("name LIKE {0} OR name_py LIKE {0}", "%" + query.getKeyword() + "%");
		}
		
		if (StringUtils.isNotBlank(query.getName())) {
			wrapper.eq("name", query.getName());
		}
		
		return this.selectList(wrapper);
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<SysDept> findByKeywordExcludeSelf(DeptQuery query) {
		Wrapper<SysDept> wrapper = new EntityWrapper<SysDept>();
		wrapper.eq("tenant_id", query.getTenantId()).eq("del_flag", false);

		if (StringUtils.isNotBlank(query.getKeyword())) {
			wrapper.andNew("name LIKE {0} OR name_py LIKE {0}", "%" + query.getKeyword() + "%");
		}
		
		if (StringUtils.isNotBlank(query.getName())) {
			wrapper.eq("name", query.getName());
		}
		
		//过滤掉设备所在部门
		List<SysDept> list = this.selectList(wrapper);
		Long deptId = query.getDeptId();
		if(list !=null && list.size() >0 ){
			if(deptId !=null){
				for (SysDept dept : list) {
					if(deptId.longValue()==dept.getId().longValue()){
						list.remove(dept);
					}
				}
			}		
		}
		
		return list;
	}

	@Override
	@Transactional(readOnly = true)
	public SysDeptVo findDeptTree(Long tenantId) {
		SysDept rootDept = this.findRootDeptByTenantId(tenantId);
		if (null != rootDept) {
			SysDeptVo rootDeptVo = BeanMapper.map(rootDept, SysDeptVo.class);
			rootDeptVo.setSubDepts(this.findByRecursion(rootDeptVo));
			return rootDeptVo;
		}
		return null;
	}

	@Override
	@Transactional(readOnly = true)
	public List<SysDeptVo> findByRecursion(SysDeptVo dept) {
		Wrapper<SysDept> wrapper = new EntityWrapper<SysDept>();
		wrapper.eq("parent_id", dept.getId()).eq("del_flag", false);
		List<SysDept> subDepts = this.selectList(wrapper);
		List<SysDeptVo> subDepVos = BeanMapper.mapList(subDepts, SysDeptVo.class);

		if (CollectionUtils.isNotEmpty(subDepVos)) {
			for (SysDeptVo subDeptVo : subDepVos) {
				this.findByRecursion(subDeptVo);
			}
			dept.setSubDepts(subDepVos);
		}
		return subDepVos;
	}

	/**
	 * 根据部门id查找子部门
	 */
	@Override
	@Transactional(readOnly = true)
	public List<SysDept> findListByParentId(Long deptId) {
		Wrapper<SysDept> wrapper = new EntityWrapper<SysDept>();
		wrapper.eq("parent_id", deptId).eq("del_flag", false);
		return this.selectList(wrapper);
	}

	@Override
	@Transactional(readOnly = true)
	public List<SysDept> findByIds(String ids) {
		List<SysDept> list = Lists.newArrayList();
		String[] idss = StringUtils.split(ids, ",");

		if (idss.length > 0) {
			for (String id : idss) {
				SysDept dept = this.selectById(id);
				if (dept != null) {
					list.add(dept);
				}
			}
		}
		return list;
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<SysDept> findByIds(Long[] ids) {
		List<SysDept> list = Lists.newArrayList();
		if (ids.length > 0) {
			for (Long id : ids) {
				SysDept dept = this.selectById(id);
				if (dept != null) {
					list.add(dept);
				}
			}
		}
		return list;
	}

	@Override
	@Transactional(readOnly = true)
	public List<SysDept> findAllSubDept(Long deptId) {
		SysDept dept = this.selectById(deptId);
		if (dept == null)
			return null;

		Wrapper<SysDept> wrapper = new EntityWrapper<SysDept>();
		wrapper.eq("del_flag", false);
		//wrapper.eq("del_flag", false).like("parent_ids", dept.getParentIds() + "," + deptId, SQLlikeType.RIGHT);
		wrapper.andNew("parent_ids='"+dept.getParentIds() + "," + deptId + "' or parent_ids like '" + dept.getParentIds() + "," + deptId +",%'");
		return this.selectList(wrapper);
	}

	@Override
	@Transactional(readOnly = true)
	public String findAllSubDeptStr(Long deptId) {
		String subDeptStr = "";
		List<SysDept> subDepts = this.findAllSubDept(deptId);

		if (CollectionUtils.isNotEmpty(subDepts)) {
			StringBuilder buider = new StringBuilder();
			for (SysDept subDept : subDepts) {
				buider.append(subDept.getId()).append(",");
			}
			buider.deleteCharAt(buider.length() - 1);
			subDeptStr = buider.toString();
		}
		return subDeptStr;
	}
}
