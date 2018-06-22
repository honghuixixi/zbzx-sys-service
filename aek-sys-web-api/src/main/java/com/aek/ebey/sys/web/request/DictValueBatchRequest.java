package com.aek.ebey.sys.web.request;

import java.util.List;
import java.util.Set;

import javax.validation.constraints.NotNull;

import com.aek.ebey.sys.web.validator.group.Add;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 数据字典响应数据实体类
 *
 * @author Honghui
 * @date 2017年6月14日
 * @version 1.0
 */
@ApiModel(value = "DictValueBatchRequest", description = "字典值创建(批量创建)")
public class DictValueBatchRequest{

	@ApiModelProperty(value = "字典ID")
	@NotNull(groups = { Add.class }, message = "BDV_001")
	private Long dictId;

	@ApiModelProperty(value = "字典值名称集合")
	private List<String> dictValueNames;

	public Long getDictId() {
		return dictId;
	}

	public void setDictId(Long dictId) {
		this.dictId = dictId;
	}

	public List<String> getDictValueNames() {
		return dictValueNames;
	}

	public void setDictValueNames(List<String> dictValueNames) {
		this.dictValueNames = dictValueNames;
	}

}
