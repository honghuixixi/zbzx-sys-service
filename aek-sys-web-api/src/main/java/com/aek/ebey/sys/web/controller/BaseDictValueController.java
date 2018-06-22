package com.aek.ebey.sys.web.controller;

import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
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
import com.aek.common.core.serurity.WebSecurityUtils;
import com.aek.common.core.serurity.model.AuthUser;
import com.aek.ebey.sys.model.BaseDictValue;
import com.aek.ebey.sys.service.BaseDictValueService;
import com.aek.ebey.sys.web.request.DictValueBatchRequest;
import com.aek.ebey.sys.web.request.DictValueRequest;
import com.aek.ebey.sys.web.validator.group.Add;
import com.aek.ebey.sys.web.validator.group.Edit;
import com.google.common.collect.Lists;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;

/**
 * 字典控制类
 *
 * @author Honghui
 * @date 2017年6月14日
 * @version 1.0
 */
@RestController
@RequestMapping("/zbzxsys/dictValue")
@Api(value = "BaseDictValueController", description = "字典值管理")
public class BaseDictValueController extends BaseController {

	@Autowired
	private BaseDictValueService baseDictValueService;

	/**
	 * 新增字典值
	 * @param request
	 * @return
	 */
	@PreAuthorize("hasAuthority('SYS_DICT_MANAGE')")
	@PostMapping(value = "/save")
	@ApiOperation(value = "新增字典值", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponse(code = 200, message = "OK", response = Result.class)
	public Result<Object> save(@Validated(value = Add.class) @RequestBody DictValueRequest request) {
		BaseDictValue dictValue = BeanMapper.map(request, BaseDictValue.class);
		this.baseDictValueService.save(dictValue);
		return response();
	}

	/**
	 * 新增字典值批量添加
	 * @param request
	 * @return
	 */
	@PreAuthorize("hasAuthority('SYS_DICT_MANAGE')")
	@PostMapping(value = "/batch/save")
	@ApiOperation(value = "新增字典值", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponse(code = 200, message = "OK", response = Result.class)
	public Result<Object> saveBatch(@Validated(value = Add.class) @RequestBody DictValueBatchRequest request) {
		List<String> dictValueNames = Lists.newArrayList();
		for (String dictValueName:request.getDictValueNames()) {
            if(!dictValueNames.contains(dictValueName)){
            	dictValueNames.add(dictValueName);
            }
	    }
		AuthUser authUser = WebSecurityUtils.getCurrentUser();
		if (CollectionUtils.isNotEmpty(dictValueNames)) {
			List<BaseDictValue> dictValues = Lists.newArrayList();
			for (String dictValueName : dictValueNames) {
				BaseDictValue dictValue = new BaseDictValue();
				dictValue.setCreateDate(new Date());
				dictValue.setCreateBy(authUser.getId());
				dictValue.setDictId(request.getDictId());
				dictValue.setName(dictValueName);
				dictValue.setDefFlag(false);
				dictValues.add(dictValue);
			}
			this.baseDictValueService.saveBatch(dictValues);
		}
		return response();
	}

	/**
	 * 编辑字典值
	 * @param request
	 * @return
	 */
	@PreAuthorize("hasAuthority('SYS_DICT_MANAGE')")
	@PutMapping(value = "/edit")
	@ApiOperation(value = "编辑字典值", httpMethod = "PUT", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponse(code = 200, message = "OK", response = Result.class)
	public Result<Object> edit(@Validated(value = Edit.class) @RequestBody DictValueRequest request) {
		BaseDictValue dictValue = BeanMapper.map(request, BaseDictValue.class);
		this.baseDictValueService.edit(dictValue);
		return response();
	}

	/**
	 * 删除字典值
	 * @param id
	 * @return
	 */
	@PreAuthorize("hasAuthority('SYS_DICT_MANAGE')")
	@DeleteMapping(value = "/delete/{id}")
	@ApiOperation(value = "删除字典值", httpMethod = "DELETE", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiImplicitParam(name = "id", value = "字典值ID", paramType = "path", required = true)
	@ApiResponse(code = 200, message = "OK", response = Result.class)
	public Result<Object> delete(@PathVariable(value = "id", required = true) Long id) {
		this.baseDictValueService.deleteById(id);
		return response();
	}

	/**
	 * 查询字典值列表
	 * @param dectId 字典ID
	 * @return
	 */
	@PreAuthorize("hasAnyAuthority('SYS_DICT_LIST_VIEW','SYS_DICT_MANAGE')")
	@RequestMapping(value = "/list/{dectId}", method = RequestMethod.GET)
	@ApiOperation(value = "查询字典值列表", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiImplicitParam(name = "dectId", value = "字典ID", paramType = "path", required = true)
	@ApiResponse(code = 200, message = "OK", response = Result.class)
	public Result<List<BaseDictValue>> getDiveValueList(@PathVariable(value = "dectId", required = true) Long dectId) {
		return response(this.baseDictValueService.findByDictId(dectId));
	}

}
