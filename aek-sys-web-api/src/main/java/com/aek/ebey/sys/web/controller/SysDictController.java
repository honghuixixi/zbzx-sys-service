package com.aek.ebey.sys.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aek.common.core.BeanMapper;
import com.aek.common.core.Result;
import com.aek.common.core.base.BaseController;
import com.aek.ebey.sys.model.SysDict;
import com.aek.ebey.sys.model.vo.SysDictVo;
import com.aek.ebey.sys.service.SysDictService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;

/**
 * <p>
 * 数据字典表 前端控制器
 * </p>
 *
 * @author aek
 * @since 2017-05-12
 */
@RestController
@RequestMapping("/zbzxsys/dict")
@Api(description = "数据字典")
public class SysDictController extends BaseController {

	@Autowired
	private SysDictService sysDictService;

	@GetMapping("/list/{tenantId}/{code}")
	@ApiOperation(value = "根据code查字典列表", httpMethod = "GET")
	@ApiResponse(code = 200, message = "OK", response = Result.class)
	public Result<Object> findListByParentCode(@PathVariable Long tenantId, @PathVariable String code) {
		List<SysDict> list = this.sysDictService.findListByParentCode(tenantId, code);
		List<SysDictVo> mapList = BeanMapper.mapList(list, SysDictVo.class);
		return response(mapList);
	}
}
