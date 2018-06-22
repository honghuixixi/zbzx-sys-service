package com.aek.ebey.sys.web.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aek.common.core.BeanMapper;
import com.aek.common.core.Result;
import com.aek.common.core.base.BaseController;
import com.aek.ebey.sys.model.SysArea;
import com.aek.ebey.sys.model.vo.SysAreaVo;
import com.aek.ebey.sys.service.SysAreaService;
import com.aek.ebey.sys.web.request.AreaResponse;
import com.google.common.collect.Lists;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;

/**
 * <p>
 * 区域表 前端控制器
 * </p>
 *
 * @author aek
 * @since 2017-05-06
 */
@RestController
@RequestMapping("/zbzxsys/area")
@Api(value = "area", description = "区域(省/市/区)")
public class SysAreaController extends BaseController {

	@Autowired
	private SysAreaService sysAreaService;

	//@PreAuthorize("isAuthenticated()")
	@GetMapping("/province")
	@ApiOperation(value = "获取省", httpMethod = "GET")
	@ApiResponse(code = 0, message = "Ok", response = Result.class)
	public Result<List<SysArea>> loadProvinceArea() {
		List<SysArea> areaList = sysAreaService.queryProvince();
		return response(areaList);
	}
	
	//@PreAuthorize("isAuthenticated()")
	@GetMapping("/province/city")
	@ApiOperation(value = "获取省市集合", httpMethod = "GET")
	@ApiResponse(code = 0, message = "Ok", response = Result.class)
	public Result<List<SysAreaVo>> loadProvinceCityArea() {
		List<SysArea> areaList = sysAreaService.queryProvince();
		List<SysAreaVo> responses = Lists.newArrayList();
		if (CollectionUtils.isNotEmpty(areaList)) {
			responses = BeanMapper.mapList(areaList, SysAreaVo.class);
			for (SysAreaVo sysAreaVo : responses) {
				List<SysArea> cityList = sysAreaService.queryTown(sysAreaVo.getId());
				if(CollectionUtils.isNotEmpty(cityList)){
					List<SysAreaVo> cityListVos = BeanMapper.mapList(cityList, SysAreaVo.class);
					sysAreaVo.setCitys(cityListVos);
				}
			}
		}
		return response(responses);
	}

	//@PreAuthorize("isAuthenticated()")
	@GetMapping("/{province}/city")
	@ApiOperation(value = "获取省下的市", httpMethod = "GET")
	@ApiImplicitParam(name = "province", value = "省份ID", required = true, paramType = "path")
	@ApiResponse(code = 200, message = "Ok", response = Result.class)
	public Result<List<AreaResponse>> loadTown(@PathVariable(value = "province", required = true) Long province) {
		List<SysArea> areaList = sysAreaService.queryTown(province);
		List<AreaResponse> responses = Lists.newArrayList();
		if (CollectionUtils.isNotEmpty(areaList)) {
			responses = BeanMapper.mapList(areaList, AreaResponse.class);
		}
		return response(responses);
	}

	//@PreAuthorize("isAuthenticated()")
	@GetMapping("/{city}/region")
	@ApiOperation(value = "获取市下的区/县", httpMethod = "GET")
	@ApiImplicitParam(name = "city", value = "市ID", required = true, paramType = "path")
	@ApiResponse(code = 200, message = "Ok", response = Result.class)
	public Result<List<AreaResponse>> loadRegion(@PathVariable("city") Long city) {
		List<AreaResponse> responses = BeanMapper.mapList(sysAreaService.queryRegion(city), AreaResponse.class);
		return response(responses);
	}

}
