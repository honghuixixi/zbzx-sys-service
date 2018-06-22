package com.aek.ebey.sys.core.scheduled;

import java.util.Date;
import java.util.List;
import java.util.Timer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.aek.common.core.util.DateUtil;
import com.aek.ebey.sys.model.SysTenant;
import com.aek.ebey.sys.service.SysTenantService;

/**
 * 用户中心定时任务类
 * 
 * @author HongHui
 * @date 2017年7月31日
 */
@Component
public class SysScheduledTask {

	//private static final Logger LOGGER = LoggerFactory.getLogger(SysScheduledTask.class);

	@Autowired
	private SysTenantService tenantService;

	/**
	 * 每天凌晨执行机构过期时间检测，若过期则置为停用
	 */
	@Scheduled(cron = "0 0 0 * * ?")
	public void executeTenantExpireTimeTask() {
		List<SysTenant> expiredTenants = tenantService.findExpiredTenant();
		for (SysTenant sysTenant : expiredTenants) {
			SysTenant updateTenant = new SysTenant();
			updateTenant.setId(sysTenant.getId());
			updateTenant.setEnable(false);
			tenantService.updateById(updateTenant);
		}
	}

	/**
	 * 每天凌晨查询当天即将到期的机构,并生成定时任务
	 */
	@Scheduled(cron = "0 0 0 * * ?")
	public void executeGetExpiredTenanyBetweenOneDay() {
		Date beginDate = DateUtil.getDayBegin();
		Date endDate = DateUtil.getDatePlusOneDay(beginDate);
		List<SysTenant> expiredTenants = tenantService.findExpiredTenantBetweenDate(beginDate, endDate);
		Timer timer = new java.util.Timer(true);
		for (SysTenant sysTenant : expiredTenants) {
			SysExpireTenantTimerTask task = new SysExpireTenantTimerTask(sysTenant.getId(),tenantService);
			timer.schedule(task, sysTenant.getExpireTime());
		}
	}

}
