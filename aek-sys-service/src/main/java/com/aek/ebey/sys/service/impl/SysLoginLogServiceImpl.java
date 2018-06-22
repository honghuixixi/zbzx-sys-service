package com.aek.ebey.sys.service.impl;

import com.aek.ebey.sys.model.SysLoginLog;
import com.aek.ebey.sys.service.SysLoginLogService;
import com.aek.common.core.base.BaseProviderImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 用户登录日志表 服务实现类
 * </p>
 *
 * @author aek
 * @since 2017-05-06
 */
@Service
@Transactional
public class SysLoginLogServiceImpl extends BaseProviderImpl<SysLoginLog> implements SysLoginLogService {
	
}
