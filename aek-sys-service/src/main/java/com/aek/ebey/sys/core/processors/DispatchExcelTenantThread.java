package com.aek.ebey.sys.core.processors;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.aek.common.core.config.RedisRepository;
import com.aek.common.core.serurity.model.AuthUser;
import com.aek.ebey.sys.model.bo.TenantExcelBo;
import com.aek.ebey.sys.service.SysAreaService;
import com.aek.ebey.sys.service.SysTenantService;
import com.aek.ebey.sys.service.SysUserService;

/**
 * 将队列中的数据放入线程池中处理
 *	
 * @author HongHui
 * @date   2017年8月2日
 */
public class DispatchExcelTenantThread implements Runnable {

	// 线程池
	private static final ExecutorService  threadPool = Executors.newFixedThreadPool(100); 
	
	// 机构数据对象队列
	private BlockingQueue<TenantExcelBo> queue; 
	// 错误数据放入缓存Key
	private String errorTenantExcelBoRedisKey;
	//当前登录用户
	private AuthUser currentUser;
	
	private SysTenantService sysTenantService;
	private SysAreaService sysAreaService;
	private SysUserService sysUserService;
	private RedisRepository redisRepository;
	private UploadRateData uploadRateData;
	
	public DispatchExcelTenantThread(BlockingQueue<TenantExcelBo> queue,String errorTenantExcelBoRedisKey,
			SysTenantService sysTenantService,SysAreaService sysAreaService,SysUserService sysUserService,RedisRepository redisRepository,
			UploadRateData uploadRateData,AuthUser currentUser) {
		this.queue = queue;
		this.errorTenantExcelBoRedisKey = errorTenantExcelBoRedisKey;
		this.sysTenantService = sysTenantService;
		this.sysAreaService = sysAreaService;
		this.sysUserService = sysUserService;
		this.redisRepository = redisRepository;
		this.uploadRateData = uploadRateData;
		this.currentUser = currentUser;
	}

	@Override
	public void run() {
		boolean isRunning = true;
		while(isRunning){
			try {
				TenantExcelBo tenantExcelBo = queue.take();
				HandleExcelTenantConsumer command = new HandleExcelTenantConsumer(tenantExcelBo,errorTenantExcelBoRedisKey,
						sysTenantService,sysAreaService,sysUserService,redisRepository,uploadRateData,currentUser);
				threadPool.execute(command);
				if(tenantExcelBo.isLastFlag()){
					isRunning = false;
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
