package com.aek.ebey.sys.service;

/**
 * 邮件发送类
 * 
 * @author Mr.han
 *
 */
public interface EmailSendService {

	/**
	 * 绑定修改邮箱地址发送
	 * 
	 * @param email
	 *            邮件地址
	 * @param userId
	 *            用户ID
	 */
	boolean sendByModifyEmail(String email, Long userId);

	/**
	 * 找回密码发送邮件
	 * 
	 * @param email
	 *            邮件地址
	 * @param code
	 *            验证码
	 * @param realName
	 *            用户真实姓名
	 * @return
	 */
	boolean sendByResetPassword(String email, String code, String realName);

}
