package com.aek.ebey.sys.web.controller;

import java.util.List;

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
import com.aek.ebey.sys.model.BaseFieldCategory;
import com.aek.ebey.sys.model.vo.BaseFieldCategoryVO;
import com.aek.ebey.sys.service.BaseFieldCategoryService;
import com.aek.ebey.sys.web.request.FieldCategoryRequest;
import com.aek.ebey.sys.web.validator.group.Add;
import com.aek.ebey.sys.web.validator.group.Edit;
import com.google.gson.Gson;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;

/**
 * 字段分类控制类
 *	
 * @author HongHui
 * @date   2017年7月14日
 */
@RestController
@RequestMapping("/zbzxsys/fieldCategory")
@Api(value = "BaseFieldCategoryController", description = "字段分类表管理")
public class BaseFieldCategoryController extends BaseController {

	private static final Logger LOGGER = LoggerFactory.getLogger(BaseFieldCategoryController.class);
	
	@Autowired
	private BaseFieldCategoryService baseFieldCategoryService;
	
	/**
	 * 查询字段分类
	 * @return
	 */
	@PreAuthorize("isAuthenticated()")
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	@ApiOperation(value = "查询字段分类列表", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponse(code = 200, message = "OK", response = Result.class)
	public Result<List<BaseFieldCategoryVO>> getFieldCategoryList() {
		LOGGER.info("==字段分类列表查询接口：/base/fieldCategory/list ===");
		List<BaseFieldCategoryVO> categoryList = this.baseFieldCategoryService.findAllCategory();
		Result<List<BaseFieldCategoryVO>> result = response(categoryList);
		LOGGER.info("==字段分类列表查询接口返回数据：" + new Gson().toJson(result));
		return result;
	}
	
	/**
	 * 新增字段分类
	 * @param request
	 * @return
	 */
	@PreAuthorize("isAuthenticated()")
	@PostMapping(value = "/save")
	@ApiOperation(value = "新增字段分类", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponse(code = 200, message = "OK", response = Result.class)
	public Result<Object> save(@Validated(value = Add.class) @RequestBody FieldCategoryRequest request) {
		BaseFieldCategory baseFieldCategory = BeanMapper.map(request, BaseFieldCategory.class);
		baseFieldCategoryService.save(baseFieldCategory);
		return response();
	}
	
	/**
	 * 编辑字段分类
	 * @param request
	 * @return
	 */
	@PreAuthorize("isAuthenticated()")
	@PostMapping(value = "/edit")
	@ApiOperation(value = "编辑字段分类", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponse(code = 200, message = "OK", response = Result.class)
	public Result<Object> edit(@Validated(value = Edit.class) @RequestBody FieldCategoryRequest request) {
		BaseFieldCategory baseFieldCategory = BeanMapper.map(request, BaseFieldCategory.class);
		baseFieldCategoryService.edit(baseFieldCategory);
		return response();
	}
	
	/**
	 *  删除字段分类
	 * @param id
	 * @return
	 */
	@PreAuthorize("isAuthenticated()")
	@PostMapping(value = "/delete/{id}")
	@ApiOperation(value = "删除字段分类", httpMethod = "DELETE", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiImplicitParam(name = "id", value = "字段分类ID", paramType = "path", required = true)
	@ApiResponse(code = 200, message = "OK", response = Result.class)
	public Result<Object> delete(@PathVariable(value = "id", required = true) Long id) {
		BaseFieldCategory baseFieldCategory = new BaseFieldCategory();
		baseFieldCategory.setId(id);
		this.baseFieldCategoryService.delete(baseFieldCategory);
		return response();
	}
	
}
