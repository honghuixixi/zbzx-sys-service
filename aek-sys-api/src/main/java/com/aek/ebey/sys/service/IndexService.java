package com.aek.ebey.sys.service;

import java.util.List;

import com.aek.ebey.sys.model.SysUser;

public interface IndexService {
	/**
	 * 找回密码发送验证码
	 * 
	 * @param account
	 *            账户(密码或者手机号)
	 * @return
	 */
	void sendCode(String account);
	
	/**
	 * 注册时发送验证码
	 * 
	 * @param account
	 * 				账户(密码或者手机号)
	 */
	void sendRstCode(String account);

	/**
	 * 重置密码
	 * 
	 * @param userId
	 *            用户ID
	 * @param code
	 *            验证码
	 * @param password
	 *            新密码
	 * @return
	 */
	boolean modifyPassword(Long userId, String mobile,String code, String password);


	/**
	 * 验证手机验证码，正确则返回用户列表
	 */
	public List<SysUser> validateCode(String account, String code);
	
	/**
	 * 重置密码
	 */
	public boolean resetPassword(Long userId, String password, String confirmPassword);
	
}
