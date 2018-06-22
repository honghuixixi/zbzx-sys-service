package com.aek.ebey.sys.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.aek.common.core.BeanMapper;
import com.aek.common.core.Result;
import com.aek.common.core.base.BaseController;
import com.aek.ebey.sys.model.BaseField;
import com.aek.ebey.sys.model.query.FieldQuery;
import com.aek.ebey.sys.service.BaseFieldService;
import com.aek.ebey.sys.web.request.FieldRequest;
import com.aek.ebey.sys.web.validator.group.Add;
import com.aek.ebey.sys.web.validator.group.Edit;
import com.baomidou.mybatisplus.plugins.Page;
import com.google.gson.Gson;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;

/**
 * 字段控制类
 *	
 * @author HongHui
 * @date   2017年7月13日
 */
@RestController
@RequestMapping("/zbzxsys/field")
@Api(value = "BaseFieldController", description = "字段表管理")
public class BaseFieldController extends BaseController {

	private static final Logger LOGGER = LoggerFactory.getLogger(BaseFieldController.class);
	
	@Autowired
	private BaseFieldService baseFieldService;
	
	/**
	 * 分页查询字段列表信息
	 * @param query
	 * @return
	 */
	@PreAuthorize("isAuthenticated()")
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	@ApiOperation(value = "分页查询字段表", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiImplicitParams({ 
			@ApiImplicitParam(name = "fieldCategoryId", paramType = "query", value = "字段分类ID", required = true),
			@ApiImplicitParam(name = "pageNo", paramType = "query", value = "当前页", required = false),
			@ApiImplicitParam(name = "pageSize", paramType = "query", value = "每页数据数量", required = false) 
	})
	@ApiResponse(code = 200, message = "OK", response = Result.class)
	public Result<Page<BaseField>> getBaseFieldPage(FieldQuery query) {
		LOGGER.info("==字段分页列表查询接口：/base/field/list ===");
		Page<BaseField> page = this.baseFieldService.findByPage(query);
		Result<Page<BaseField>> result = response(page);
		LOGGER.info("==字段分页列表查询接口返回数据：" + new Gson().toJson(result));
		return result;
	}
	
	
	/**
	 * 新增字段
	 * @param request
	 * @return
	 */
	@PreAuthorize("isAuthenticated()")
	@PostMapping(value = "/save")
	@ApiOperation(value = "新增字段", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponse(code = 200, message = "OK", response = Result.class)
	public Result<Object> save(@Validated(value = Add.class)  FieldRequest request) {
		LOGGER.info("==字段新增接口：/base/field/save ===");
		BaseField baseField = BeanMapper.map(request, BaseField.class);
		LOGGER.info("传入数据=" + baseField.toString());
		baseFieldService.save(baseField);
		return response();
	}
	
	/**
	 * 根据ID查询字段详情
	 * @param id
	 * @return
	 */
	@PreAuthorize("isAuthenticated()")
	@RequestMapping(value = "/search/{id}", method = RequestMethod.GET)
	@ApiOperation(value = "查询字段详情", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiImplicitParam(name = "id", paramType = "path", value = "字段ID", required = true)
	@ApiResponse(code = 200, message = "OK", response = Result.class)
	public Result<BaseField> getFieldById(@PathVariable(value = "id", required = true) Long id) {
		LOGGER.info("==字段查詢接口：/base/field/search ===");
		Result<BaseField> result = response(this.baseFieldService.selectById(id));
		LOGGER.info("==字段查询接口返回数据：" + new Gson().toJson(result));
		return result;
	}
	
	/**
	 * 编辑字段
	 * @param request
	 * @return
	 */
	@PreAuthorize("isAuthenticated()")
	@PostMapping(value = "/edit")
	@ApiOperation(value = "编辑字段", httpMethod = "PUT", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponse(code = 200, message = "OK", response = Result.class)
	public Result<Object> edit(@Validated(value = Edit.class) @RequestBody FieldRequest request) {
		BaseField baseField = BeanMapper.map(request, BaseField.class);
		this.baseFieldService.edit(baseField);
		return response();
	}
	
	/**
	 *  删除字段
	 * @param id
	 * @return
	 */
	@PreAuthorize("isAuthenticated()")
	@PostMapping(value = "/delete/{id}")
	@ApiOperation(value = "删除字段", httpMethod = "DELETE", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiImplicitParam(name = "id", value = "字段ID", paramType = "path", required = true)
	@ApiResponse(code = 200, message = "OK", response = Result.class)
	public Result<Object> delete(@PathVariable(value = "id", required = true) Long id) {
		BaseField baseField = new BaseField();
		baseField.setId(id);
		this.baseFieldService.delete(baseField);
		return response();
	}
	
	/**
	 * 字段停用
	 * @param id
	 * @return
	 */
	@PreAuthorize("isAuthenticated()")
	@PostMapping(value = "/disable/{id}")
	@ApiOperation(value = "字段停用")
	@ApiImplicitParam(name = "id", value = "字段ID", paramType = "path", required = true)
	@ApiResponse(code = 200, message = "OK", response = Result.class)
	public Result<Object> disableField(@PathVariable(value = "id", required = true) Long id) {
		BaseField baseField = new BaseField();
		baseField.setId(id);
		baseField.setEnable(false);
		this.baseFieldService.edit(baseField);
		return response();
	}
	
	/**
	 * 字段启用
	 * @param id
	 * @return
	 */
	@PreAuthorize("isAuthenticated()")
	@PostMapping(value = "/enable/{id}")
	@ApiOperation(value = "字段启用")
	@ApiImplicitParam(name = "id", value = "字段ID", paramType = "path", required = true)
	@ApiResponse(code = 200, message = "OK", response = Result.class)
	public Result<Object> enableField(@PathVariable(value = "id", required = true) Long id) {
		BaseField baseField = new BaseField();
		baseField.setId(id);
		baseField.setEnable(true);
		this.baseFieldService.edit(baseField);
		return response();
	}

	/**
	 * 字段必填
	 * @param id
	 * @return
	 */
	@PreAuthorize("isAuthenticated()")
	@PostMapping(value = "/required/{id}")
	@ApiOperation(value = "字段必填")
	@ApiImplicitParam(name = "id", value = "字段ID", paramType = "path", required = true)
	@ApiResponse(code = 200, message = "OK", response = Result.class)
	public Result<Object> requiredField(@PathVariable(value = "id", required = true) Long id) {
		BaseField baseField = new BaseField();
		baseField.setId(id);
		baseField.setRequiredFlag(true);
		this.baseFieldService.edit(baseField);
		return response();
	}
	
	/**
	 * 字段非必填
	 * @param id
	 * @return
	 */
	@PreAuthorize("isAuthenticated()")
	@PostMapping(value = "/unrequired/{id}")
	@ApiOperation(value = "字段非必填")
	@ApiImplicitParam(name = "id", value = "字段ID", paramType = "path", required = true)
	@ApiResponse(code = 200, message = "OK", response = Result.class)
	public Result<Object> unRequiredField(@PathVariable(value = "id", required = true) Long id) {
		BaseField baseField = new BaseField();
		baseField.setId(id);
		baseField.setRequiredFlag(false);
		this.baseFieldService.edit(baseField);
		return response();
	}
}
