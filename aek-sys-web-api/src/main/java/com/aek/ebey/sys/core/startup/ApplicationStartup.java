package com.aek.ebey.sys.core.startup;

import java.util.Date;
import java.util.List;
import java.util.Timer;

import javax.annotation.Resource;
import javax.servlet.ServletContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ServletContextAware;

import com.aek.common.core.util.DateUtil;
import com.aek.ebey.sys.core.scheduled.SysExpireTenantTimerTask;
import com.aek.ebey.sys.model.SysTenant;
import com.aek.ebey.sys.service.SysTenantService;

/**
 * 容器重启时，需将当前一天之中即将过期的机构置为停用
 *	
 * @author HongHui
 * @date   2017年7月31日
 */
@Component
public class ApplicationStartup implements InitializingBean,ServletContextAware {

	private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationStartup.class);

	@Resource
	private SysTenantService sysTenantService;
	
	@Override
	public void setServletContext(ServletContext servletContext) {
		LOGGER.debug("==加载当天即将过期的机构数据===");
		Date beginDate = DateUtil.getDayBegin();
		Date endDate = DateUtil.getDatePlusOneDay(beginDate);
		List<SysTenant> expiredTenants = sysTenantService.findExpiredTenantBetweenDate(beginDate, endDate);
		Timer timer = new java.util.Timer(true);
		for (SysTenant sysTenant : expiredTenants) {
			LOGGER.debug(sysTenant.getName());
			SysExpireTenantTimerTask task = new SysExpireTenantTimerTask(sysTenant.getId(),sysTenantService);
			timer.schedule(task, sysTenant.getExpireTime());
		}
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		
	}
}
