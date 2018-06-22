package com.aek.ebey.sys.model;

import com.aek.common.core.base.BaseModel;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;

import io.swagger.annotations.ApiModel;

/**
 * <p>
 * 城市代码表
 * </p>
 *
 * @author aek
 * @since 2017-05-06
 */
@TableName("sys_area")
@ApiModel(value = "area", description = "区域")
public class SysArea extends BaseModel {

	private static final long serialVersionUID = 1L;

	/**
	 * 行政代码
	 */
	@TableField(value = "code")
	private String code;
	/**
	 * 名称
	 */
	@TableField(value = "name")
	private String name;
	/**
	 * 父id
	 */
	@TableField(value = "parent_id")
	private Integer parentId;
	/**
	 * 每个汉字的首字母
	 */
	@TableField(value = "name_py")
	private String namePy;
	/**
	 * 区域等级(省,市,区县)
	 */
	@TableField(value = "level")
	private Integer level;

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

}
