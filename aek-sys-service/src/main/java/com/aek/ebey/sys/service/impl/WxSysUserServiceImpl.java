package com.aek.ebey.sys.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aek.common.core.base.BaseServiceImpl;
import com.aek.ebey.sys.mapper.WxSysUserMapper;
import com.aek.ebey.sys.model.WxSysUser;
import com.aek.ebey.sys.service.WxSysUserService;

/**
 *  微信用户绑定服务
 *	
 * @author HongHui
 * @date   2017年11月30日
 */
@Service
@Transactional
public class WxSysUserServiceImpl extends BaseServiceImpl<WxSysUserMapper, WxSysUser> implements WxSysUserService {
	
	@Autowired
	private WxSysUserMapper wxSysUserMapper;
	
	/**
	 * 保存微信用户绑定关系保存至数据库
	 * @param accessToken
	 */
	public void saveWxSysUser(WxSysUser wxSysUser){
		wxSysUserMapper.insertWxSysUser(wxSysUser);
	}
	
	/**
	 * 判断openid是否已经绑定用户
	 * @return
	 */
	public boolean exist(String openId){
		int count = wxSysUserMapper.countWxSysUser(openId);
		return count > 0 ? true : false;
	}
	
	/**
	 * 查询OpenId绑定的用户
	 * @param openId
	 * @return
	 */
	public WxSysUser getWxSysUser(String openId){
		return wxSysUserMapper.selectWxSysUserByOpenId(openId);
	}
	
	/**
	 * 查询userId绑定的微信用户
	 * @param openId
	 * @return
	 */
	public WxSysUser getWxSysUser(Long userId){
		return wxSysUserMapper.selectWxSysUserByUserId(userId);
	}
}
