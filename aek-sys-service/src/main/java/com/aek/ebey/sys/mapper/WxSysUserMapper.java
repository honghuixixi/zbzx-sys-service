package com.aek.ebey.sys.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.aek.ebey.sys.model.WxSysUser;
import com.aek.ebey.sys.model.vo.WxUserVo;
import com.baomidou.mybatisplus.mapper.BaseMapper;

/**
 * 系统用户与微信绑定数据Mapper类
 *	
 * @author HongHui
 * @date   2017年12月1日
 */
@Mapper
public interface WxSysUserMapper extends BaseMapper<WxSysUser> {

	/**
	 * 保存WxSysUser
	 * @param wxSysUser
	 */
	public void insertWxSysUser(@Param("wxSysUser") WxSysUser wxSysUser);
	
	/**
	 * 查询openId是否已经绑定系统用户
	 * @return
	 */
	public int countWxSysUser(@Param("openId") String openId);
	
	/**
	 * 查询openId绑定系统用户
	 * @param openId
	 * @return
	 */
	public WxSysUser selectWxSysUserByOpenId(@Param("openId") String openId);
	
	/**
	 * 查询userId绑定系统用户
	 * @param userId
	 * @return
	 */
	public WxSysUser selectWxSysUserByUserId(@Param("userId") Long userId);
	
	/**
	 * 根据userId和openId获取用户是否接受消息标志
	 * @param userId
	 * @param openId
	 * @return
	 */
	public Boolean getWxUserEnable(@Param("userId")Long userId,@Param("openId")String openId);
	
}
