package com.aek.ebey.sys.model.vo;

import java.util.List;

import com.aek.common.core.base.BaseModel;
import com.aek.common.core.base.BaseModel.BaseView;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.fasterxml.jackson.annotation.JsonView;
import com.google.common.collect.Lists;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>
 * 城市代码表
 * </p>
 *
 * @author aek
 * @since 2017-05-06
 */
@ApiModel(value = "AreaResponse", description = "区域返回值")
public class SysAreaVo{

	/**
	 * 区域ID
	 */
	@ApiModelProperty(value = "区域ID")
	private Long id;
	
	/**
	 * 行政代码
	 */
	@ApiModelProperty(value = "行政代码")
	private String code;
	
	/**
	 * 区域名称
	 */
	@ApiModelProperty(value = "名称")
	private String name;
	
	/**
	 * 父id
	 */
	@ApiModelProperty(value = "父id")
	private Integer parentId;
	
	/**
	 * 每个汉字的首字母
	 */
	@ApiModelProperty(value = "每个汉字的首字母d")
	private String namePy;
	
	/**
	 * 区域等级(省,市,区县)
	 */
	@ApiModelProperty(value = "区域等级(省,市,区县)")
	private Integer level;
	
	/**
	 * 省下的市集合
	 */
	private List<SysAreaVo> citys = Lists.newArrayList();

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public String getNamePy() {
		return namePy;
	}

	public void setNamePy(String namePy) {
		this.namePy = namePy;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<SysAreaVo> getCitys() {
		return citys;
	}

	public void setCitys(List<SysAreaVo> citys) {
		this.citys = citys;
	}

}
