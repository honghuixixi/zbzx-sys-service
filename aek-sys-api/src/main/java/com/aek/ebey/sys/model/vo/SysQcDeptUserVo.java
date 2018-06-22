package com.aek.ebey.sys.model.vo;

/**
 *  机构巡检人员
 *	
 * @author HongHui
 * @date   2017年11月9日
 */
public class SysQcDeptUserVo {

	private Boolean status;  //验收状态true,可以创建巡检计划
	private String tips;     //提示信息
	
	public SysQcDeptUserVo(Boolean status, String tips) {
		this.status = status;
		this.tips = tips;
	}
	
	public Boolean getStatus() {
		return status;
	}
	
	public void setStatus(Boolean status) {
		this.status = status;
	}
	
	public String getTips() {
		return tips;
	}
	
	public void setTips(String tips) {
		this.tips = tips;
	}
	
}
