package com.aek.ebey.sys.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aek.common.core.config.RedisRepository;
import com.aek.common.core.exception.BusinessException;
import com.aek.common.core.exception.ExceptionFactory;
import com.aek.common.core.sms.Sms;
import com.aek.common.core.sms.SmsResult;
import com.aek.common.core.util.SecurityUtil;
import com.aek.ebey.sys.core.SysConstants;
import com.aek.ebey.sys.enums.AccountType;
import com.aek.ebey.sys.model.SysUser;
import com.aek.ebey.sys.model.WxSysUser;
import com.aek.ebey.sys.model.bo.VerifyCode;
import com.aek.ebey.sys.service.EmailSendService;
import com.aek.ebey.sys.service.IndexService;
import com.aek.ebey.sys.service.SysUserService;
import com.aek.ebey.sys.service.WxSysUserService;
import com.alibaba.fastjson.JSON;

@Service
@Transactional
public class IndexServiceImpl implements IndexService {

	@Autowired
	private SysUserService userService;

	@Autowired
	private Sms sms;

	@Autowired
	private RedisRepository redisRepository;

	@Autowired
	private EmailSendService emailSenService;
	
	@Autowired
	private WxSysUserService wxSysUserService;

	@Override
	public void sendCode(String mobile) {

		// 查询是否在1分钟内发送过验证码，则不发送返回1分钟重试
		Date currTime = new Date();
		String key = SysConstants.SYS_GET_PASSWORD_CODE + mobile;

		String verifyCodeJson = this.redisRepository.get(key);
		if (StringUtils.isNotBlank(verifyCodeJson)) {

			VerifyCode currVerifyCode = JSON.parseObject(verifyCodeJson, VerifyCode.class);
			long date = currTime.getTime() - currVerifyCode.getSendTime().getTime();

			if ((date / 60000) < 1) {
				throw ExceptionFactory.create("U_019");
			}
		}

		// 根据手机号获取用户列表
		List<SysUser> userList = this.userService.findUserListByMobile(mobile);
		if (null == userList || (null != userList && userList.size() == 0) ) {
		    throw ExceptionFactory.create("U_009");
		} 

		// 随机数6位
		String code = this.sms.randomCode(6);

		// 手机验证码
		SmsResult smsResult = this.sms.sendCode(mobile, code);
		if(null != smsResult){
			if(smsResult.getSuccess() != 1){
				if(smsResult.getErrcode() == 32){
					throw ExceptionFactory.create("U_022");
				}else{
					throw new BusinessException(smsResult.getMsg());
				}
			}
		} else {
			throw new BusinessException("验证码发送失败");
		}

		// 过期时间 30分钟
		long expireTime = 30 * 60;
		VerifyCode verifyCode = new VerifyCode();
		verifyCode.setCode(code);
		verifyCode.setSendTime(currTime);

		// 保存验证码
		String value = JSON.toJSONString(verifyCode);
		this.redisRepository.setExpire(key, value, expireTime);
	}
	
	@Override
	public void sendRstCode(String account) {
		//校验手机号不为空
		if(account != null && account.length() > 0){
			// 查询是否在1分钟内发送过验证码，则不发送返回1分钟重试
			Date currTime = new Date();
			String key = SysConstants.SYS_REGISTER_CODE + account;

			String verifyCodeJson = this.redisRepository.get(key);
			if (StringUtils.isNotBlank(verifyCodeJson)) {

				VerifyCode currVerifyCode = JSON.parseObject(verifyCodeJson, VerifyCode.class);
				long date = currTime.getTime() - currVerifyCode.getSendTime().getTime();

				if ((date / 60000) < 1) {
					throw ExceptionFactory.create("U_019");
				}
			}
			
			// 随机数6位
			String code = this.sms.randomCode(6);
			
			// 手机验证码
			SmsResult smsResult = this.sms.sendCode(account, code);
			if (smsResult.getSuccess() != 1) {
				throw ExceptionFactory.create("U_022");
			}
			
			// 过期时间 30分钟
			long expireTime = 30 * 60;
			VerifyCode verifyCode = new VerifyCode();
			verifyCode.setCode(code);
			verifyCode.setSendTime(currTime);

			// 保存验证码
			String value = JSON.toJSONString(verifyCode);
			this.redisRepository.setExpire(key, value, expireTime);
		}
	}

	@Override
	public boolean modifyPassword(Long userId,String mobile, String code, String password) {

		SysUser user = userService.selectById(userId);
		String key = SysConstants.SYS_GET_PASSWORD_CODE + mobile;

		// 查询验证码是否在30分钟有效期内，否则重试
		String verifyCode = this.redisRepository.get(key);

		if (StringUtils.isNotBlank(verifyCode)) {
			VerifyCode currVerifyCode = JSON.parseObject(verifyCode, VerifyCode.class);

			// 判断验证码是否正确
			if (!currVerifyCode.getCode().equals(code)) {
				throw ExceptionFactory.create("U_021");
			}

			// 判断密码是否为空
			if (StringUtils.isEmpty(password)) {
				throw ExceptionFactory.create("U_004");
			}

		} else {
			// 缓存验证码不存在，提示重新发送
			throw ExceptionFactory.create("U_020");
		}

		boolean flag = false;
		user.setPassword(this.userService.encrypt(password));
		if (this.userService.updateById(user)) {
			this.redisRepository.del(key);
			flag = true;
			
			//更新微信绑定用户密码
			WxSysUser wxSysUser = wxSysUserService.getWxSysUser(user.getId());
			if(null != wxSysUser){
				wxSysUser.setPassword(SecurityUtil.encryptDes(password));
				wxSysUserService.updateById(wxSysUser);
			}
		}
		
		return flag;
	}

	
	@Override
    public List<SysUser> validateCode(String account, String code) {
	    // 手机验证码缓存KEY
        String key = SysConstants.SYS_GET_PASSWORD_CODE + account;

        // 查询验证码是否在30分钟有效期内，否则重试
        String verifyCode = this.redisRepository.get(key);

        if (StringUtils.isNotBlank(verifyCode)) {
            VerifyCode currVerifyCode = JSON.parseObject(verifyCode, VerifyCode.class);

            // 判断验证码是否正确
            if (!currVerifyCode.getCode().equals(code)) {
                throw ExceptionFactory.create("U_021");
            } 
        } else {
            // 缓存验证码不存在，提示重新发送
            throw ExceptionFactory.create("U_020");
        }
        //获取用户列表
        List<SysUser> userList = userService.findUserListByMobile(account);
        return userList;
    }

	
	@Override
    public boolean resetPassword(Long userId, String password, String confirmPassword) {
	    //参数验证
	    if (null == userId || StringUtils.isBlank(password) || StringUtils.isBlank(confirmPassword)) {
	        throw ExceptionFactory.create("501"); 
	    }
	    if (!password.equals(confirmPassword)) {
	        throw new BusinessException("502","两次输入的密码不一致");
	    }
	    
        SysUser user = this.userService.selectById(userId);
        if (null == user) {
            throw new BusinessException("503","用户不存在");
        }
        boolean flag = false;
        user.setPassword(this.userService.encrypt(password));
        if (this.userService.updateById(user)) {
            flag = true;
            //更新微信绑定用户密码
            WxSysUser wxSysUser = wxSysUserService.getWxSysUser(user.getId());
            if(null != wxSysUser){
                wxSysUser.setPassword(SecurityUtil.encryptDes(password));
                wxSysUserService.updateById(wxSysUser);
            }
        }
        return flag;
    }

}
