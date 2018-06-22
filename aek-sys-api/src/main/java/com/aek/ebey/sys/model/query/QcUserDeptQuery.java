package com.aek.ebey.sys.model.query;

import java.util.List;

import com.aek.ebey.sys.model.vo.SysQcDeptVo;

/**
 * 新建巡检计划验证用户及部门是否被删除或修改
 *	
 * @author HongHui
 * @date   2017年11月23日
 */
public class QcUserDeptQuery {

	private Long userId;         //巡检用户ID
	private String userName;     //用户名称
	
	private List<SysQcDeptVo> qcDepts;  //巡检科室集合

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public List<SysQcDeptVo> getQcDepts() {
		return qcDepts;
	}

	public void setQcDepts(List<SysQcDeptVo> qcDepts) {
		this.qcDepts = qcDepts;
	}

	@Override
	public String toString() {
		return "QcUserDeptQuery [userId=" + userId + ", qcDepts=" + qcDepts + "]";
	}
	
}
