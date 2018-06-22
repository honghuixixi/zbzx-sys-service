package com.aek.ebey.sys.service.ribbon;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.aek.common.core.Result;
import com.aek.ebey.sys.model.vo.TenantAssetsVo;
import com.aek.ebey.sys.service.ribbon.hystrix.AssetClientHystrix;

/**
 * 设备模块调用
 * 
 * @author Mr.han
 *
 */
@FeignClient(value = "${feign-assets.serviceId}",fallback = AssetClientHystrix.class)
public interface AssetClientService {

	/**
	 * 调用接口，部门id 查找该部门是否存在设备
	 * 
	 * @param id
	 * @return true 存在/false 不存在
	 * 
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/assets/restAPI/deviceQuery")
	Result<Boolean> deviceQuery(@RequestParam(value = "deptIds") Long[] deptIds, @RequestHeader("X-AEK56-Token") String token);

	/**
	 * 根据TenantIds统计资产
	 * @param ids
	 * @param token
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/assets/assetsInfo/getAssetsByTenantIds")
	Result<List<TenantAssetsVo>> getAssetsByTenantIds(@RequestParam("ids") Long[] ids,@RequestHeader("X-AEK56-Token") String token);
}
