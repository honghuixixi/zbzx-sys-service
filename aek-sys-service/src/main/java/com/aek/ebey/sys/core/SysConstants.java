package com.aek.ebey.sys.core;

/**
 * 业务常量类
 * 
 * @author Shuangwf
 * @date 2017年4月5日
 *
 */
public final class SysConstants {

	/**
	 * 网站地址
	 */
	// public static final String SYS_WWW = "http://192.168.1.57:8080";

	/**
	 * 树根节点
	 */
	public static final Long ROOT_PARENT = 0L;
	public static final String ROOT_PARENT_STR = "0";

	/**
	 * 机构管理员模板角色数据库ID，不可变
	 */
	public static final Long TEMPLET_ADMIN_ROLE_ID = 1L;

	/**
	 * 爱医康机构数据库ID，不可变
	 */
	public static final Long AEK_TENANT_ID = 1L;

	/**
	 * 默认顶级监管机构ID
	 */
	public static final Long ROOT_SUPERVISE_TENANT_ID = 2L;

	/**
	 * 用户职务类型
	 */
	public static final String SYS_USER_JOB_TYPE = "SYS_USER_JOB_TYPE";

	/*------------------缓存key-------------------------------*/
	/**
	 * 找回密码
	 */
	public static final String SYS_GET_PASSWORD_CODE = "SYS_GET_PASSWORD_CODE";
	
	/**
	 * 用户注册
	 */
	public static final String SYS_REGISTER_CODE = "SYS_REGISTER_CODE";

	/**
	 * 修改邮箱
	 */
	public static final String SYS_MODIFY_EMAIL_CODE = "SYS_MODIFY_EMAIL_CODE";

	/**
	 * excel导入机构,保存错误文件 key + userId
	 */
	public static final String SYS_EXCEL_IMPORT_TENANT = "SYS_EXCEL_IMPORT_TENANT_ERROR";

	/**
	 * excel导入机构,进度数据 key + userId
	 */
	public static final String SYS_EXCEL_IMPORT_TENANT_SCHEDULE = "SYS_EXCEL_IMPORT_TENANT_SCHEDULE";

}
