package com.aek.ebey.sys.model.bo;

/**
 * 机构导入返回信息对象
 *	
 * @author HongHui
 * @date   2017年8月3日
 */
public class TenantImportVo {

	//导入进度缓存key
	private String importRateRedisKey;
	//导入错误数据缓存key
	private String importErrorRedisKey;
	
	public TenantImportVo(String importRateRedisKey, String importErrorRedisKey) {
		this.importRateRedisKey = importRateRedisKey;
		this.importErrorRedisKey = importErrorRedisKey;
	}
	public String getImportRateRedisKey() {
		return importRateRedisKey;
	}
	public void setImportRateRedisKey(String importRateRedisKey) {
		this.importRateRedisKey = importRateRedisKey;
	}
	public String getImportErrorRedisKey() {
		return importErrorRedisKey;
	}
	public void setImportErrorRedisKey(String importErrorRedisKey) {
		this.importErrorRedisKey = importErrorRedisKey;
	}
	
}
