package com.aek.ebey.sys.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aek.common.core.email.EmailSend;
import com.aek.common.core.exception.ExceptionFactory;
import com.aek.common.core.sms.RegexValidateUtil;
import com.aek.common.core.support.email.Email;
import com.aek.ebey.sys.core.SysEmialRedirectConfig;
import com.aek.ebey.sys.model.SysUser;
import com.aek.ebey.sys.service.EmailSendService;
import com.aek.ebey.sys.service.SysUserService;

/**
 * 邮件发送实现类
 * 
 * @author Mr.han
 *
 */
@Service
@Transactional
public class EmailSendServiceImpl implements EmailSendService {

	@Autowired
	private EmailSend emailSend;
	
	@Autowired
	private SysUserService userService;
	
	@Autowired
	private SysEmialRedirectConfig sysEmialRedirectConfig;

	@Override
	public boolean sendByModifyEmail(String email, Long userId) {
		
		this.checkEmail(email);
		
		SysUser user = this.userService.selectById(userId);
		
		String emailRedirectUrl =sysEmialRedirectConfig.getUrl();
		
		String content =  "<div style='width: 100%;height: 490px; background: #f2f2f2;padding: 50px;color:#666;'>"   
							+ "<div style=' width: 535px;height: 300px;margin: 0 auto;'>"
								+ "<header style=' height: 70px;background: #4ab29b;color: #fff;text-align: center;font-size: 25px;line-height: 70px;'>爱医康</header>"
								+ "<p style=' background: #fff;font-size: 16px; height: 230px; padding: 20px;margin-top: -5px'>"
									+ "<span>" + user.getRealName() + "，您好！</span><br><br><br>"
									+ "<span>感谢您使用爱医康设备管理平台，请点击下面的按钮验证您的邮箱，如按钮无效，请点击<a href='"+emailRedirectUrl+"/emailed.html?id=" + userId + "' style=' text-decoration: none;cursor: pointer;'>这里</a>。</span>"
									+ "<a href='"+emailRedirectUrl+"/emailed.html?id=" + userId + "' style='margin: 60px  auto;padding: 0 15px;height: 30px;line-height: 30px;color: #fff;border-radius: 3px;background: #f7931e;width: 125px;text-align: center;list-style: none;cursor: pointer;display: block; text-decoration: none;cursor: pointer;color: #7bc1f8;color:#fff'>验证邮箱</a>"
								+ "</p>"
							+ "</div>"
						+ "</div>";
		
		Email emailEntity = new Email();
		emailEntity.setTopic("爱医康邮箱激活通知");
		emailEntity.setSendTo(email);
		emailEntity.setBody(content);

		return this.emailSend.send(emailEntity);
	}

	@Override
	public boolean sendByResetPassword(String email, String code, String realName) {

		// 判断邮件格式
		this.checkEmail(email);
		String content = "<div style='width: 100%;height: 490px; background: #f2f2f2;padding: 50px;color:#666;'>"   
						    + "<div style='width: 535px;height: 300px; margin: 0 auto;'>"
						    	+ "<header style='height: 70px;background: #4ab29b;color: #fff;text-align: center;font-size: 25px;line-height: 70px;'>爱医康</header>"
						    	+ "<p style='background: #fff;font-size: 16px; height: 230px;padding: 20px;margin-top: -5px'>"
						    		+ "<span>" + realName + "，您好！</span><br><br><br>"
						    		+ "<span>请在30分钟内输入以下验证码完成验证，如非本人操作，请忽略此邮件。</span>"
						    		+ "<span style='font-size: 30px;display: block; text-align: center;margin-top: 20px;color:#333'>"+code+"</span>"
						    	+ "</p>"
					    	+ "</div>"
					    + "</div>";
				
		Email emailEntity = new Email();
		emailEntity.setTopic("爱医康找回密码通知");
		emailEntity.setSendTo(email);
		emailEntity.setBody(content);

		return this.emailSend.send(emailEntity);
	}

	/**
	 * 邮箱格式检查
	 * 
	 * @param email
	 */
	private void checkEmail(String email) {
		// 判断邮件格式
		if (!RegexValidateUtil.checkEmail(email)) {
			throw ExceptionFactory.create("U_006");
		}
	}

}
