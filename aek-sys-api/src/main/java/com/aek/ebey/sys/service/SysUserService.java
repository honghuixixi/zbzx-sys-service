package com.aek.ebey.sys.service;

import java.util.List;

import com.aek.common.core.base.BaseService;
import com.aek.common.core.serurity.model.AuthUser;
import com.aek.common.core.sms.SmsResult;
import com.aek.ebey.sys.model.SysDept;
import com.aek.ebey.sys.model.SysTenant;
import com.aek.ebey.sys.model.SysUser;
import com.aek.ebey.sys.model.query.UserQuery;
import com.aek.ebey.sys.model.vo.SysRepUserVo;
import com.aek.ebey.sys.model.vo.WxUserVo;
import com.baomidou.mybatisplus.plugins.Page;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author aek
 * @since 2017-05-06
 */
public interface SysUserService extends BaseService<SysUser> {

	/**
	 * 初始化机构管理员
	 * 
	 * @param tenant
	 * @param admin
	 * @return
	 */
	SysUser initTenantAdminUser(SysTenant tenanta, SysUser admin,AuthUser authUser);
	
	/**
	 * 初始化机构管理员(装备中心)
	 * 
	 * @param tenant
	 * @param admin
	 * @return
	 */
	SysUser initTenantAdminUserForZjzx(SysTenant tenanta, SysUser admin,AuthUser authUser);

	/**
	 * 根据用户名查询用户
	 * 
	 * @param account
	 * @return
	 */
	SysUser findByAccount(String account);
	
	/**
	 * 根据手机号查询用户
	 * @param account
	 * @param code
	 * @return
	 */
	List<SysUser> getUserByMobile(String account,String code);
	
	void changeLoginName(String loginName,Long userId);
	
	void resetPwd(String pwd,Long userId);

	/**
	 * 创建机构管理员
	 * 
	 * @param admin
	 *            管理员信息
	 */
	void createTenantAdmin(SysUser admin,AuthUser authUser);

	/**
	 * 根据手机号码查询用户信息
	 * 
	 * @param mobile
	 *            手机号码
	 * @return
	 */
	SysUser findByMobile(String mobile);
	
	/**
	 * 获取用户列表
	 */
	List<SysUser> findUserListByMobile(String mobile);

	/**
	 * 根据邮箱查找用户
	 * 
	 * @param Email
	 * @return
	 */
	SysUser findByEmail(String Email);

	/**
	 * 修改用户密码
	 * 
	 * @param userId
	 *            用户id
	 * @param password
	 *            新密码
	 * @return
	 */
	void changePassword(Long userId, String password, boolean oneseFlag);

	/**
	 * 添加用户
	 * 
	 * @param user
	 *            用户信息
	 */
	void add(SysUser user);
	
	/**
	 * 用户注册
	 * 
	 * @param user
	 *            用户信息
	 */
	void register(SysUser user);
	
	/**
	 * 网站用户登录
	 * 
	 * @param user
	 *            用户信息
	 */
	SysUser login(String loginName,String password);
	
	/**
	 * 登录名查找用户
	 * 
	 * @param loginName
	 *            登录名
	 */
	SysUser findByLoginName(String loginName);
	
	/**
	 * 机构名查找用户
	 * 
	 * @param tenantName
	 *            登录名
	 */
	SysUser findByTenantName(String tenantName);

	/**
	 * 修改用户信息
	 * 
	 * @param user
	 */
	void edit(SysUser user);
	
	/**
	 * 修改用户信息realName
	 * 
	 * @param user
	 */
	void editRealName(SysUser user);

	/**
	 * 根据关键字检索
	 * 
	 * @param deptIds
	 *            部门ID集合
	 * @param user
	 *            具体检索
	 * @param keyword
	 *            模糊匹配
	 * @return
	 */
	Page<SysUser> search(UserQuery query);

	/**
	 * 根据部门更新用户信息
	 * 
	 * @param dept
	 */
	void updateByDeptId(SysDept dept);

	/**
	 * 密码加密
	 * 
	 * @param password
	 * @return
	 */
	String encrypt(String password);

	/**
	 * 查询租户下的用户
	 * 
	 * @param tenantId
	 * @param enable
	 * @return
	 */
	Page<SysUser> findByTenantId(UserQuery query);
	
	/**
	 * 根据机构查询机构下用户
	 * @param tenantId
	 * @return
	 */
	List<SysUser> findUserListByTenantId(Long tenantId);
	
	/**
	 * 根据机构Id获取本机构具有维修审批权限的用户列表
	 * @param query
	 * @return
	 */
	List<SysUser> getcheckUserList(String keyword);

	/**
	 * 根据部门id查询用户
	 */
	List<SysUser> findByDeptId(Long deptId);

	/**
	 * 批量查询 id 以逗号','分割
	 * 
	 * @param ids
	 * @return
	 */
	List<SysUser> findByIds(String ids);

	/**
	 * 批量查询 <功能详细描述>
	 * 
	 * @param ids
	 * @return [参数说明]
	 * 
	 * @return List<SysUser> [返回类型说明]
	 * @exception throws
	 *                [违例类型] [违例说明]
	 * @see [类、类#方法、类#成员]
	 */
	List<SysUser> findByIds(Long[] ids);

	/**
	 * 新手机发送验证码
	 * 
	 * @param mobile
	 */
	SmsResult sendCode(String mobile);

	/**
	 * 重置手机
	 * 
	 * @param mobile
	 * @param code
	 * @param userId
	 */
	boolean resetMobile(String mobile, String code, Long userId);

	/**
	 * 判断密码
	 * 
	 * @param password
	 * @param userId
	 */
	boolean checkPassword(String password, Long userId);

	/**
	 * 修改邮箱(发送邮件)
	 * 
	 * @param email
	 */
	void modifyEmailSend(String email, Long userId);

	/**
	 * 修改激活邮箱
	 * 
	 * @param userId
	 * @return 邮箱地址
	 */
	String modifyEmail(Long userId);

	/**
	 * 解绑邮箱
	 * 
	 * @param userId
	 */
	void unbundlingEmail(Long userId);

	/**
	 * 
	 * 提交添加邮箱
	 * 
	 * @param id
	 * @param email
	 */
	boolean submitEmail(Long id, String email);

	/**
	 * 根据机构ID
	 * 
	 * @param tenantId
	 * @param permissionId
	 * @return
	 */
	List<Long> findByTenantAndCanPermiss(String tenantId, Long permissionId);

	/**
	 * 停用用户（批量）
	 * 
	 * @param userIds
	 *            用户ID集合
	 */
	void disableBatchUser(List<Long> userIds);

	/**
	 * 用户启用（批量）
	 * 
	 * @param userIds
	 *            用户ID集合
	 */
	void enableBatchUser(List<Long> userIds);

	/**
	 * 删除用户（批量）
	 * 
	 * @param userIds
	 */
	void deleteBatchUser(List<Long> userIds);

	/**
	 * 根据手机号码判断用户是否存在
	 * 
	 * @param mobile
	 *            手机号码
	 * @return
	 */
	boolean isUserMobileExist(String mobile);
	
	/**
	 * 根据登录名用户是否存在
	 * 
	 * @param loginName
	 *            登录名
	 * @return
	 */
	boolean isLoginNameExist(String loginName);

	/**
	 * 根据邮箱号判断用户存在，注意 判断邮箱为激活状态的用户
	 * 
	 * @param email
	 *            邮箱号码
	 * @return
	 */
	boolean isUserEmailExist(String email);

	/**
	 * 统计机构及下级机构的用户总数
	 * 
	 * @param tenantId
	 * @return
	 */
	int findUserCountByTenantId(Long tenantId);
	
	/**
	 * 根据用户机构Id更新用户机构名称
	 * @param tenantId
	 * @param newTenantName
	 */
	void updateUserTenantNameByTenantId(Long tenantId,String newTenantName);
	
	/**
	 * 根据用户部门Id更新用户部门名称
	 * @param tenantId
	 * @param newTenantName
	 */
	void updateUserDeptNameByDeptId(Long deptId,String newDeptName);
	
	/**
	 * 查询某个用户是否具有某个权限
	 * @param userId
	 * @param tenandId
	 * @param permissionCode
	 * @return
	 */
	boolean getUserPermission(Long userId,Long tenandId,String permissionCode); 

	/**
	 * 查找未删除且可用的用户
	 * @param userId
	 * @param tenandId
	 * @param permissionCode
	 * @return
	 */
	boolean getUserEnableAndNotDel(Long userId);
	
	/**
	 * 微信用户根据openid获取用户信息
	 * @param openId
	 * @return
	 */
	WxUserVo getWxUser(String openId);
}
