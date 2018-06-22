package com.aek.ebey.sys.model.custom;

import java.util.List;

import com.aek.ebey.sys.model.bo.SysDeptBo;
import com.google.common.collect.Lists;

/**
 *  角色Custom信息
 *
 * @author  Honghui
 * @date    2017年9月22日
 * @version 1.0
 */
public class RoleCustom {
	
	/**
	 * 当角色为自定义部门时，deptIds为自定义部门ID集合
	 */
	private List<SysDeptBo> depts = Lists.newArrayList();

	public List<SysDeptBo> getDepts() {
		return depts;
	}

	public void setDepts(List<SysDeptBo> depts) {
		this.depts = depts;
	}
	
}
