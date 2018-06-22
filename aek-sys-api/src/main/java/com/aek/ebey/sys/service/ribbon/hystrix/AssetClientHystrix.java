package com.aek.ebey.sys.service.ribbon.hystrix;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import com.aek.common.core.Result;
import com.aek.common.core.exception.ExceptionFactory;
import com.aek.ebey.sys.model.vo.TenantAssetsVo;
import com.aek.ebey.sys.service.ribbon.AssetClientService;

/**
 * 断路器
 * 
 * @author Mr.han
 *
 */
@Component
public class AssetClientHystrix implements AssetClientService {
	private static final Logger logger = LogManager.getLogger(AuthClientHystrix.class);

	@Override
	public Result<Boolean> deviceQuery(Long[] deptIds, String token) {
		logger.error("service : [assets-web-api] invocation method : [deviceQuery] url: [/assets/outWard/deviceQuery]");
		throw ExceptionFactory.create("G_011",
				"com.aek.ebey.sys.service.ribbon.hystrix.AssetClientHystrix.deviceQuery(Long id, String token)");
	}

	@Override
	public Result<List<TenantAssetsVo>> getAssetsByTenantIds(Long[] ids, String token) {
		logger.error("service : [assets-web-api] invocation method : [getAssetsByTenantIds] url: [/assets/assetsInfo/getAssetsByTenantIds]--->查询指定机构设备数量失败");
		return null;
	}

}
