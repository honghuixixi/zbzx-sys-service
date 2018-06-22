package com.aek.ebey.sys.service;

import java.util.Map;

/**
 * 基础数据获取接口类
 * 
 * @author Mr.han
 *
 */
public interface BaseDataService {
	/**
	 * 获取全局静态枚举类
	 * 
	 * @return 枚举变量表
	 */
	Map<String, Map<Object, String>> findStaticVariable();
}
