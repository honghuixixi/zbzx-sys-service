package com.aek.ebey.sys.model.bo;

import java.util.List;

import com.aek.ebey.sys.model.SysTenant;
import com.aek.ebey.sys.model.SysUser;
import com.google.common.collect.Lists;

/**
 * excel机构导入bean
 * 
 * @author Mr.han
 *
 */
public class TenantExcelBo extends SysTenant {

	private static final long serialVersionUID = -890219283722710681L;


	/**
	 * 机构管理员
	 */
	private SysUser admin = new SysUser();

	/**
	 * 是否整条记录正确，true导入/false 不导入
	 */
	private boolean correctly = true;

	/**
	 * 当前位错误数据, 最后写入
	 */
	private List<String> errorMsg = Lists.newArrayList();

	/**
	 * 错误位置，excle col 本条记录错误点
	 */
	private List<Integer> errorIndex = Lists.newArrayList();
	
	/**
	 * 是否是最后一条数据
	 */
	private boolean isLastFlag;

	/**
	 * @return the admin
	 */
	public SysUser getAdmin() {
		return admin;
	}

	/**
	 * @param admin
	 *            the admin to set
	 */
	public void setAdmin(SysUser admin) {
		this.admin = admin;
	}

	/**
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public boolean isCorrectly() {
		return correctly;
	}

	public void setCorrectly(boolean correctly) {
		this.correctly = correctly;
	}

	public List<Integer> getErrorIndex() {
		return errorIndex;
	}

	public void setErrorIndex(List<Integer> errorIndex) {
		this.errorIndex = errorIndex;
	}

	public List<String> getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(List<String> errorMsg) {
		this.errorMsg = errorMsg;
	}

	public boolean isLastFlag() {
		return isLastFlag;
	}

	public void setLastFlag(boolean isLastFlag) {
		this.isLastFlag = isLastFlag;
	}

}
