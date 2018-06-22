package com.aek.ebey.sys.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.aek.common.core.BeanMapper;
import com.aek.common.core.Result;
import com.aek.common.core.base.BaseController;
import com.aek.ebey.sys.model.BaseDict;
import com.aek.ebey.sys.model.query.DictQuery;
import com.aek.ebey.sys.service.BaseDictService;
import com.aek.ebey.sys.web.request.DictRequest;
import com.aek.ebey.sys.web.validator.group.Add;
import com.aek.ebey.sys.web.validator.group.Edit;
import com.baomidou.mybatisplus.plugins.Page;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;

/**
 * 字典控制类
 *
 * @author Honghui
 * @date 2017年6月14日
 * @version 1.0
 */
@RestController
@RequestMapping("/zbzxsys/dict")
@Api(value = "BaseDictController", description = "字典表管理")
public class BaseDictController extends BaseController {

	@Autowired
	private BaseDictService baseDictService;

	/**
	 * 新建字典
	 * @param request
	 * @return
	 */
	@PreAuthorize("hasAuthority('SYS_DICT_MANAGE')")
	@PostMapping(value = "/save")
	@ApiOperation(value = "新建字典", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponse(code = 200, message = "OK", response = Result.class)
	public Result<Object> save(@Validated(value = Add.class) @RequestBody DictRequest request) {
		BaseDict baseDict = BeanMapper.map(request, BaseDict.class);
		this.baseDictService.save(baseDict);
		return response();
	}

	/**
	 * 编辑字典
	 * @param request
	 * @return
	 */
	@PreAuthorize("hasAuthority('SYS_DICT_MANAGE')")
	@PutMapping(value = "/edit")
	@ApiOperation(value = "编辑字典", httpMethod = "PUT", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponse(code = 200, message = "OK", response = Result.class)
	public Result<Object> edit(@Validated(value = Edit.class) @RequestBody DictRequest request) {
		BaseDict baseDict = BeanMapper.map(request, BaseDict.class);
		this.baseDictService.edit(baseDict);
		return response();
	}

	/**
	 * 删除字典
	 * @param id
	 * @return
	 */
	@PreAuthorize("hasAuthority('SYS_DICT_MANAGE')")
	@DeleteMapping(value = "/delete/{id}")
	@ApiOperation(value = "删除字典", httpMethod = "DELETE", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiImplicitParam(name = "id", value = "字典ID", paramType = "path", required = true)
	@ApiResponse(code = 200, message = "OK", response = Result.class)
	public Result<Object> delete(@PathVariable(value = "id", required = true) Long id) {
		this.baseDictService.delete(id);
		return response();
	}

	/**
	 * 分页检索查询字典表
	 * @param query 查询条件
	 * @return
	 */
	@PreAuthorize("hasAnyAuthority('SYS_DICT_LIST_VIEW','SYS_DICT_MANAGE')")
	@RequestMapping(value = "/search", method = RequestMethod.GET)
	@ApiOperation(value = "分页检索查询字典表", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiImplicitParams({ @ApiImplicitParam(name = "tenantId", paramType = "query", value = "租户ID", required = true),
			@ApiImplicitParam(name = "cascadeStatus", paramType = "query", value = "级联状态[1=级联状态，2=独立]", required = false),
			@ApiImplicitParam(name = "manageType", paramType = "query", value = "管理类型[1=基础，2=自定义]", required = false),
			@ApiImplicitParam(name = "keyword", paramType = "query", value = "关键字", required = false),
			@ApiImplicitParam(name = "pageNo", paramType = "query", value = "当前页", required = false),
			@ApiImplicitParam(name = "pageSize", paramType = "query", value = "每页数据数量", required = false) })
	@ApiResponse(code = 200, message = "OK", response = Result.class)
	public Result<Page<BaseDict>> getBaseDictPage(@ApiParam(hidden = true) DictQuery query) {
		Page<BaseDict> page = this.baseDictService.findByPage(query);
		return response(page);
	}

	/**
	 * 根据ID查询字典详情
	 * @param id
	 * @return
	 */
	@PreAuthorize("hasAnyAuthority('SYS_DICT_LIST_VIEW','SYS_DICT_MANAGE')")
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ApiOperation(value = "查询字典详情", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiImplicitParam(name = "id", paramType = "path", value = "字典ID", required = true)
	@ApiResponse(code = 200, message = "OK", response = Result.class)
	public Result<BaseDict> getById(@PathVariable(value = "id", required = true) Long id) {
		return response(this.baseDictService.selectById(id));
	}

}
