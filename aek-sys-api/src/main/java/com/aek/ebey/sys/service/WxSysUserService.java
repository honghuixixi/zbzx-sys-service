package com.aek.ebey.sys.service;

import com.aek.common.core.base.BaseService;
import com.aek.ebey.sys.model.WxSysUser;

/**
 *  微信用户绑定服务
 *	
 * @author HongHui
 * @date   2017年11月30日
 */
public interface WxSysUserService extends BaseService<WxSysUser>{
	
	/**
	 * 保存微信用户绑定关系保存至数据库
	 * @param accessToken
	 */
	public void saveWxSysUser(WxSysUser wxSysUser);
	
	/**
	 * 判断openid是否已经绑定用户
	 * @return
	 */
	public boolean exist(String openId);
	
	/**
	 * 查询OpenId绑定的用户
	 * @param openId
	 * @return
	 */
	public WxSysUser getWxSysUser(String openId);
	
	/**
	 * 查询userId绑定的微信用户
	 * @param openId
	 * @return
	 */
	public WxSysUser getWxSysUser(Long userId);
	
}
