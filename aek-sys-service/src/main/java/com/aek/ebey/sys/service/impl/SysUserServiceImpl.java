package com.aek.ebey.sys.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aek.common.core.BeanMapper;
import com.aek.common.core.base.BaseServiceImpl;
import com.aek.common.core.config.RedisRepository;
import com.aek.common.core.exception.ExceptionFactory;
import com.aek.common.core.serurity.WebSecurityUtils;
import com.aek.common.core.serurity.model.AuthUser;
import com.aek.common.core.sms.RegexValidateUtil;
import com.aek.common.core.sms.Sms;
import com.aek.common.core.sms.SmsResult;
import com.aek.common.core.util.PinyinUtil;
import com.aek.common.core.util.SecurityUtil;
import com.aek.ebey.sys.core.SysConstants;
import com.aek.ebey.sys.mapper.SysUserMapper;
import com.aek.ebey.sys.mapper.WxSysUserMapper;
import com.aek.ebey.sys.model.SysDept;
import com.aek.ebey.sys.model.SysRole;
import com.aek.ebey.sys.model.SysRolePermission;
import com.aek.ebey.sys.model.SysRoleUser;
import com.aek.ebey.sys.model.SysTenant;
import com.aek.ebey.sys.model.SysUser;
import com.aek.ebey.sys.model.WxSysUser;
import com.aek.ebey.sys.model.bo.VerifyCode;
import com.aek.ebey.sys.model.custom.WebSiteUser;
import com.aek.ebey.sys.model.query.UserQuery;
import com.aek.ebey.sys.model.vo.SysRepUserVo;
import com.aek.ebey.sys.model.vo.WxUserVo;
import com.aek.ebey.sys.service.EmailSendService;
import com.aek.ebey.sys.service.SysDeptService;
import com.aek.ebey.sys.service.SysRolePermissionService;
import com.aek.ebey.sys.service.SysRoleService;
import com.aek.ebey.sys.service.SysRoleUserService;
import com.aek.ebey.sys.service.SysTenantModuleService;
import com.aek.ebey.sys.service.SysTenantService;
import com.aek.ebey.sys.service.SysUserService;
import com.aek.ebey.sys.service.WxSysUserService;
import com.aek.ebey.sys.service.ribbon.AuthClientService;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.google.common.collect.Lists;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author aek
 * @since 2017-05-06
 */

@Service
@Transactional
public class SysUserServiceImpl extends BaseServiceImpl<SysUserMapper, SysUser> implements SysUserService {

	private static final Logger logger = LogManager.getLogger(SysUserServiceImpl.class);

	@Autowired
	private SysUserMapper userMapper;

	@Autowired
	private SysDeptService sysDeptService;

	@Autowired
	private SysTenantService sysTenantService;

	@Autowired
	private SysRoleService sysRoleService;

	@Autowired
	private SysRolePermissionService rolePermissionService;

	@Autowired
	private SysRoleUserService roleUserService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private AuthClientService authClientService;

	@Autowired
	private Sms sms;

	@Autowired
	private RedisRepository redisRepository;

	@Autowired
	private EmailSendService emailSendService;

	@Autowired
	private SysUserService userService;
	
	@Autowired
	private SysDeptService deptService;
	
	@Autowired
	private SysTenantModuleService tenantModuleService;
	
	@Autowired
	private SysRoleService roleService;
	
	@Autowired
	private WxSysUserService wxSysUserService;
	
	@Autowired
	private WxSysUserMapper wxSysUserMapper;

	@Override
	public SysUser initTenantAdminUser(SysTenant tenant, SysUser admin,AuthUser authUser) {
		Date currTime = new Date();
		// 机构根部门
		SysDept rootDept = this.sysDeptService.findRootDeptByTenantId(tenant.getId());

		admin.setTenantId(tenant.getId());
		admin.setRealNamePy(PinyinUtil.cn2py(admin.getRealName()));
		admin.setTenantName(tenant.getName());
		admin.setDeptId(rootDept.getId());
		admin.setDeptName(rootDept.getName());
		admin.setCreateTime(currTime);
		admin.setUpdateTime(currTime);
		admin.setAdminFlag(false);
		if (StringUtils.isNotBlank(admin.getEmail())) {
			admin.setEmailActivate(true);
		}
		this.createTenantAdmin(admin,authUser);
		// 添加管理员id
		tenant.setAdminId(admin.getId());
		this.sysTenantService.updateById(tenant);

		// 机构管理员赋予角色
		SysRole role = this.sysRoleService.findAdminRoleByTenantId(tenant.getId());
		this.sysRoleService.addRoleToUser(role.getId(), tenant.getId(), admin.getId());
		
		return admin;
	}
	
	@Override
	public SysUser initTenantAdminUserForZjzx(SysTenant tenant, SysUser admin,AuthUser authUser) {
		Date currTime = new Date();
		// 机构根部门
		SysDept rootDept = this.sysDeptService.findRootDeptByTenantId(tenant.getId());

		//标志来源于zjzx
		WebSiteUser webSiteUser = new WebSiteUser();
		webSiteUser.setFrom("zjzb");
		admin.setCustomData(JSON.toJSONString(webSiteUser));
		admin.setLoginName(admin.getLoginName());
		admin.setTenantId(tenant.getId());
		admin.setRealNamePy(PinyinUtil.cn2py(admin.getRealName()));
		admin.setTenantName(tenant.getName());
		admin.setDeptId(rootDept.getId());
		admin.setDeptName(rootDept.getName());
		admin.setCreateTime(currTime);
		admin.setUpdateTime(currTime);
		admin.setAdminFlag(false);
		if (StringUtils.isNotBlank(admin.getEmail())) {
			admin.setEmailActivate(true);
		}
		this.createTenantAdmin(admin,authUser);
		// 添加管理员id
		tenant.setAdminId(admin.getId());
		this.sysTenantService.updateById(tenant);

		// 机构管理员赋予角色
		SysRole role = this.sysRoleService.findAdminRoleByTenantId(tenant.getId());
		this.sysRoleService.addRoleToUser(role.getId(), tenant.getId(), admin.getId());
		
		return admin;
	}

	@Override
	public void createTenantAdmin(SysUser admin,AuthUser authUser) {
		// 重复验证
		//this.checkDuplicate(admin);
		admin.setPassword(this.encrypt(admin.getPassword()));

		//非null创建人已登录(null创建人为游客未登录)
		if(authUser != null){
			admin.setCreateBy(authUser.getId());
			admin.setCreatorName(authUser.getRealName());
		}else {
			admin.setCreatorName(admin.getRealName());
		};

		// 创建用户邮箱存在，状态设为激活
		if (StringUtils.isNotBlank(admin.getMobile())) {
			admin.setEmailActivate(true);
		}
		this.insert(admin);
	}

	/**
	 * 增加修改数据重复检查
	 * 
	 * @param user
	 */
	private void checkDuplicate(SysUser user) {
		SysUser mobileUser = null;
		SysUser emailUser = null;

		if (StringUtils.isNotBlank(user.getMobile())) {
			if (!RegexValidateUtil.checkCellphone(user.getMobile())) {
				throw ExceptionFactory.create("U_026");
			}
			mobileUser = this.findByMobile(user.getMobile());
		}
		if (StringUtils.isNotBlank(user.getEmail())) {
			if (!RegexValidateUtil.checkEmail(user.getEmail())) {
				throw ExceptionFactory.create("U_027");
			}
			emailUser = this.findByEmail(user.getEmail());
		}

		if (user.getId() == null) { // 添加
			if (mobileUser != null && !mobileUser.getDelFlag())
				throw ExceptionFactory.create("U_013");
			if (emailUser != null && !emailUser.getDelFlag())
				throw ExceptionFactory.create("U_024");
		} else {
			// 修改除自身数据相同
			if (mobileUser != null && !user.getId().equals(mobileUser.getId()))
				throw ExceptionFactory.create("U_013");
			if (emailUser != null && !user.getId().equals(emailUser.getId()))
				throw ExceptionFactory.create("U_024");
		}
		if (user.getTenantId() != null) {
			SysTenant sysTenant = this.sysTenantService.selectById(user.getTenantId());
			if (sysTenant == null) {
				throw ExceptionFactory.create("O_008");
			}
			user.setTenantName(sysTenant.getName());
		}

		// 判断部门存在
		if (user.getDeptId() != null) {
			SysDept dept = this.sysDeptService.selectById(user.getDeptId());
			if (dept == null) {
				throw ExceptionFactory.create("D_009");
			}
			user.setDeptName(dept.getName());
			user.setParentDeptIds(dept.getParentIds());
		}
	}

	@Override
	public SysUser findByMobile(String mobile) {
		SysUser user = new SysUser();
		user.setMobile(mobile);
		user.setDelFlag(false);
		return this.userMapper.selectOne(user);
	}
	
	@Override
    public List<SysUser> findUserListByMobile(String mobile) {
        Wrapper<SysUser> sysUserWrapper = new EntityWrapper<SysUser>();
        sysUserWrapper.eq("mobile", mobile);
        sysUserWrapper.eq("del_flag", false);
        return this.userMapper.selectList(sysUserWrapper);
    }

    public SysUser findByUserId(Long userId) {
		SysUser user = new SysUser();
		user.setId(userId);
		return this.userMapper.selectOne(user);
	}

	@Override
	public SysUser findByEmail(String Email) {
		SysUser user = new SysUser();
		user.setEmail(Email);
		user.setEmailActivate(true);
		user.setDelFlag(false);
		return this.userMapper.selectOne(user);
	}

	@Override
	public String encrypt(String password) {
		return passwordEncoder.encode(password);
	}

	/**
	 * 修改用户密码
	 * 
	 * @param userId
	 *            用户id
	 * 
	 * @param password
	 *            新密码
	 * @param password
	 *            新密码
	 * @return
	 */
	@Override
	public void changePassword(Long userId, String password, boolean oneseFlag) {
		SysUser user = this.selectById(userId);
		if (null == user) {
			throw ExceptionFactory.create("U_009");
		}

		user.setPassword(this.encrypt(password));

		AuthUser authUser = WebSecurityUtils.getCurrentUser();
		user.setUpdateBy(authUser.getId());
		user.setUpdateTime(new Date());
		this.updateById(user);

		//更新微信绑定用户密码
		WxSysUser wxSysUser = wxSysUserService.getWxSysUser(user.getId());
		if(null != wxSysUser){
			wxSysUser.setPassword(SecurityUtil.encryptDes(password));
			wxSysUserService.updateById(wxSysUser);
		}
		
		// 个人修改不清理缓存，其他修改清理缓存
		if (!oneseFlag) {
			final String token = WebSecurityUtils.getCurrentToken();
			new Thread() {
				@Override
				public void run() {
					clearUserPerms(user.getMobile(), token);
				}
			}.start();
		}
	}

	/**
	 * 
	 * 服务调用清理用户缓存权限信息
	 * 
	 * @param mobile
	 * @param token
	 */
	private void clearUserPerms(String mobile, String token) {
		// 用户被禁用、密码被租户管理员修改，根据手机号踢出用户
		logger.info("===用户[mobile=" + mobile + "]密码被修改,清除用户缓存开始=========================");
		Map<String, Object> clearCurrentUserCacheResult = authClientService.removePermsBymobile(mobile, token);
		logger.info("===用户[mobile=" + mobile + "] 密码被修改，清除用户缓存结果 " + clearCurrentUserCacheResult.toString());
	}

	@Override
	public void add(SysUser user) {
		this.checkDuplicate(user);
		user.setRealNamePy(PinyinUtil.cn2py(user.getRealName()));
		// 处理密码
		user.setPassword(this.encrypt(user.getPassword()));

		AuthUser authUser = WebSecurityUtils.getCurrentUser();
		user.setCreateBy(authUser.getId());
		user.setCreatorName(authUser.getRealName());
		user.setCreateTime(new Date());
		// 创建用户邮箱存在，状态设为激活
		if (StringUtils.isNotBlank(user.getMobile())) {
			user.setEmailActivate(true);
		}
		this.userMapper.insert(user);
	}
	

	@Override
	public void register(SysUser user) {
		//缓存中取出验证码
		String key = SysConstants.SYS_REGISTER_CODE + user.getMobile();
		String verifyCodeJson = this.redisRepository.get(key);
		if(StringUtils.isNotBlank(verifyCodeJson)){
			VerifyCode currVerifyCode = JSON.parseObject(verifyCodeJson, VerifyCode.class);
			String cacheVerifyCode = currVerifyCode.getCode();			
			//验证码错误
			if(!user.getVerifyCode().equals(cacheVerifyCode)){
				throw ExceptionFactory.create("L_003");
			};		
		}else if (!StringUtils.isNotBlank(verifyCodeJson)) {
			throw ExceptionFactory.create("L_007"); 
		}
		
		user.setRealNamePy(PinyinUtil.cn2py(user.getRealName()));
		// 处理密码
		user.setPassword(this.encrypt(user.getPassword()));

		//AuthUser authUser = WebSecurityUtils.getCurrentUser();
		//user.setCreateBy(authUser.getId());
		//user.setCreatorName(authUser.getRealName());
		user.setCreateTime(new Date());
		WebSiteUser webSiteUser = new WebSiteUser();
		webSiteUser.setFrom("webSite");
		user.setCustomData(JSON.toJSONString(webSiteUser));
		// 创建用户邮箱存在，状态设为激活
		if (StringUtils.isNotBlank(user.getMobile())) {
			user.setEmailActivate(true);
		}
		this.userMapper.insert(user);
		
		SysTenant tenant = new SysTenant();
		tenant.setName(user.getTenantName());
		tenant.setParentId(1L);
		tenant.setTrial(1);
		tenant.setCommercialUse(1);
		tenant.setTenantType(1);
		tenant.setOrigin(3);
		tenant.setAuditStatus(2);
		tenant.setAdminId(user.getId());
		tenant.setCreateTime(new Date());
		tenant.setCustomData(JSON.toJSONString(webSiteUser));
		sysTenantService.insert(tenant);
	
		//注册用户关联用户
		user.setTenantId(tenant.getId());		
		this.userMapper.updateById(user);
		
		// 初始化机构部门信息(根节点默认与机构名称相同)
		this.deptService.initTenantDept(tenant);

		// 初始化模块
		this.tenantModuleService.initTenantModule(tenant.getId());

		// 初始化机构角色信息
		this.roleService.initTenantRole(tenant.getId());
	}

	@Override
	public void edit(SysUser user) {
		if (StringUtils.isBlank(user.getRealName())) {
			throw ExceptionFactory.create("U_034");
		}
		if (StringUtils.isBlank(user.getMobile())) {
			throw ExceptionFactory.create("U_035");
		}
		this.checkDuplicate(user);
		AuthUser authUser = WebSecurityUtils.getCurrentUser();
		final String token = WebSecurityUtils.getCurrentToken();
		SysUser oldUser = this.userService.selectById(user.getId());
		//当修改当前登录用户手机时，则需清理用户登录信息，重新登录
		if((user.getMobile() != null) && (!user.getMobile().equals(oldUser.getMobile()))){
			new Thread() {
				@Override
				public void run() {
					logger.info("===修改用户手机号清理用户token缓存=========================");
					authClientService.removePermsBymobile(oldUser.getMobile(), token);
				}
			}.start();
		}
		//当修改当前登录用户部门时，则需更新用户user-detail信息
		if((user.getDeptId() != null) && (!user.getDeptId().equals(oldUser.getDeptId()))){
			new Thread() {
				@Override
				public void run() {
					logger.info("===修改用户部门清理用户缓存=========================");
					authClientService.removeUser(oldUser.getMobile(),token);
				}
			}.start();
		}

		if(user!=null && StringUtils.isBlank(user.getJobName())){
			SysUser sysUser = this.userMapper.selectById(user.getId());
			sysUser.setJobName(null);
			this.userMapper.updateAllColumnById(sysUser);
		}
		this.userMapper.updateById(user);
	}
	
	@Override
	public void editRealName(SysUser user) {
		if (StringUtils.isBlank(user.getRealName())) {
			throw ExceptionFactory.create("U_034");
		}	
		this.userMapper.updateById(user);
	}

	@Override
	public Page<SysUser> search(UserQuery query) {
		Page<SysUser> page = query.getPage();
		Wrapper<SysUser> wrapper = new EntityWrapper<SysUser>();
		wrapper.eq("tenant_id", query.getTenantId()).eq("del_flag", false);

		if (query.getEnable() != null) {
			wrapper.eq("enable", query.getEnable());
		}

		if (query.getDeptId() != null && query.getDeptId() > 0) {
			List<SysDept> subDepts = this.sysDeptService.findAllSubDept(query.getDeptId());

			if (CollectionUtils.isNotEmpty(subDepts)) {
				List<Long> deptIds = Lists.newArrayList();
				deptIds.add(query.getDeptId());
				for (SysDept subDept : subDepts) {
					deptIds.add(subDept.getId());
				}
				wrapper.in("dept_id", deptIds);
			} else {
				wrapper.eq("dept_id", query.getDeptId());
			}
		}
		query.setKeyword(StringUtils.trimToNull(query.getKeyword()));
		if (StringUtils.isNotBlank(query.getKeyword())) {
			String keyword = query.getKeyword();
			if (keyword.startsWith("%") || keyword.startsWith("[") || keyword.startsWith("[]")
					|| keyword.startsWith("_")) {
				wrapper.andNew("id like {0} or real_name like {0} or mobile like {0}", "%[" + keyword + "]%");// ("
			} else {
				wrapper.andNew("id like {0} or real_name like {0} or mobile like {0}", "%" + keyword + "%");
			}
		}
		return this.selectPage(page, wrapper);
	}

	@Override
	public Page<SysUser> findByTenantId(UserQuery query) {
		Wrapper<SysUser> wrapper = new EntityWrapper<SysUser>();
		wrapper.eq("tenant_id", query.getTenantId()).eq("del_flag", false);
		if (query.getEnable() != null) {
			wrapper.eq("enable", query.getEnable());
		}
		Page<SysUser> page = query.getPage();
		return this.selectPage(page, wrapper);
	}
	
	@Override
	public List<SysUser> findUserListByTenantId(Long tenantId) {
		Wrapper<SysUser> wrapper = new EntityWrapper<SysUser>();
		wrapper.eq("tenant_id", tenantId).eq("del_flag", false).eq("enable", true).orderBy("id", true);
		return this.selectList(wrapper);
	}

	@Override
	public void updateByDeptId(SysDept dept) {
		Wrapper<SysUser> wrapper = new EntityWrapper<SysUser>();
		wrapper.eq("dept_id", dept.getId());
		List<SysUser> users = this.selectList(wrapper);

		for (SysUser user : users) {
			user.setDeptName(dept.getName());
			user.setParentDeptIds(dept.getParentIds());
		}
		if (CollectionUtils.isNotEmpty(users)) {
			this.updateBatchById(users);
		}
	}

	@Override
	public List<SysUser> findByDeptId(Long deptId) {
		Wrapper<SysUser> wrapper = new EntityWrapper<SysUser>();
		wrapper.eq("dept_id", deptId).eq("del_flag", false);
		return this.selectList(wrapper);
	}

	@Override
	public SysUser findByAccount(String account) {
		SysUser user = this.findByMobile(account);

		if (user == null) {
			user = this.findByEmail(account);
			if (user == null) {
				throw ExceptionFactory.create("U_009");
			}
		}
		return user;
	}

	@Override
	public List<SysUser> findByIds(String ids) {
		List<SysUser> list = Lists.newArrayList();
		String[] idss = StringUtils.split(ids, ",");

		if (idss.length > 0) {
			for (String i : idss) {
				SysUser u = this.selectById(i);
				if (u != null) {
					list.add(u);
				}
			}
		}
		return list;
	}

	@Override
	public List<SysUser> findByIds(Long[] ids) {
		List<SysUser> list = Lists.newArrayList();
		if (ids.length > 0) {
			for (Long id : ids) {
				SysUser u = this.selectById(id);
				if (u != null) {
					list.add(u);
				}
			}
		}
		return list;
	}

	@Override
	public SmsResult sendCode(String mobile) {
		SysUser user = this.findByMobile(mobile);

		if (user != null) {
			throw ExceptionFactory.create("U_013");
		}

		Date currTime = new Date();
		String key = SysConstants.SYS_GET_PASSWORD_CODE + mobile;

		// 查询是否在1分钟内发送过验证码，则不发送返回1分钟重试
		String smsVlue = this.redisRepository.get(key);
		if (StringUtils.isNotBlank(smsVlue)) {
			VerifyCode currSmsCode = JSON.parseObject(smsVlue, VerifyCode.class);
			Long date = currTime.getTime() - currSmsCode.getSendTime().getTime();
			if ((date / 60000) < 1) {
				throw ExceptionFactory.create("U_019");
			}
		}

		String code = this.sms.randomCode(6);
		SmsResult result = this.sms.sendCode(mobile, code);

		// 发送失败
		if (result.getSuccess() != 1) {
			return result;
		}

		// 过期时间
		long expireTime = 30 * 60;
		VerifyCode smsCode = new VerifyCode();
		smsCode.setCode(code);
		smsCode.setSendTime(currTime);

		// 保存验证码
		String value = JSON.toJSONString(smsCode);
		this.redisRepository.setExpire(key, value, expireTime);
		return result;
	}

	@Override
	public boolean resetMobile(String mobile, String code, Long userId) {
		SysUser currUser = this.selectById(userId);

		if (this.findByMobile(mobile) != null) {
			throw ExceptionFactory.create("U_013");
		}

		String key = SysConstants.SYS_GET_PASSWORD_CODE + mobile;

		// 查询验证码是否在30分钟有效期内，否则重试
		String smsVlue = this.redisRepository.get(key);

		if (StringUtils.isNotBlank(smsVlue)) {
			VerifyCode currSmsCode = JSON.parseObject(smsVlue, VerifyCode.class);

			// 判断验证码是否正确
			if (!currSmsCode.getCode().equals(code)) {
				throw ExceptionFactory.create("U_021");
			}

		} else {
			// 缓存验证码不存在，提示重新发送
			throw ExceptionFactory.create("U_020");
		}

		boolean flag = false;
		currUser.setMobile(mobile);
		if (this.updateById(currUser)) {
			this.redisRepository.del(key);
			flag = true;
		}
		return flag;
	}

	@Override
	public boolean checkPassword(String password, Long userId) {
		SysUser user = this.selectById(userId);
		if (user == null) {
			return false;
		}

		if (StringUtils.isBlank(user.getPassword()) || StringUtils.isBlank(password)) {
			return false;
		}
		// 判断密码是否正确
		if (!this.passwordEncoder.matches(password, user.getPassword())) {
			return false;
		}
		return true;
	}

	@Override
	public void modifyEmailSend(String email, Long userId) {

		// 判断当前邮箱是否被绑定
		if (this.findByEmail(email) != null) {
			throw ExceptionFactory.create("U_024");
		}

		// 缓存Key
		String key = SysConstants.SYS_MODIFY_EMAIL_CODE + userId;

		if (this.emailSendService.sendByModifyEmail(email, userId)) {
			// 过期时间 12 小时有效
			long expireTime = 12 * 60 * 60;
			this.redisRepository.setExpire(key, email, expireTime);
		}
	}

	@Override
	public String modifyEmail(Long userId) {

		// 缓存Key
		String key = SysConstants.SYS_MODIFY_EMAIL_CODE + userId;

		// 缓存不存在
		String email = this.redisRepository.get(key);
		if (StringUtils.isBlank(email)) {
			throw ExceptionFactory.create("U_023");
		}

		// 判断当前邮箱是否被绑定
		if (this.findByEmail(email) != null) {
			throw ExceptionFactory.create("U_024");
		}

		SysUser user = this.selectById(userId);
		user.setEmail(email);
		user.setEmailActivate(true);
		this.updateById(user);

		// 清理缓存
		this.redisRepository.del(key);
		return email;
	}

	@Override
	public void unbundlingEmail(Long userId) {
		SysUser user = this.selectById(userId);
		user.setEmailActivate(false);
		this.updateById(user);
	}

	@Override
	public boolean submitEmail(Long id, String email) {
		SysUser user = this.selectById(id);

		if (StringUtils.isBlank(email)) {
			user.setEmail("");
		} else {
			user.setEmail(email);
		}
		//验证邮箱格式
		if(!RegexValidateUtil.checkEmail(email)){
			throw ExceptionFactory.create("U_027");
		}
		// 判断当前邮箱是否被绑定
		if (this.findByEmail(email) != null) {
			throw ExceptionFactory.create("U_024");
		}
		user.setEmailActivate(false);
		return this.updateById(user);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Long> findByTenantAndCanPermiss(String tenantId, Long permissionId) {

		// 查询当前机构拥有指定权限角色
		Wrapper<SysRolePermission> wrapper = new EntityWrapper<SysRolePermission>();
		wrapper.eq("tenant_id", tenantId).eq("permission_id", permissionId).eq("enable", true).eq("del_flag", false);
		List<SysRolePermission> rolePermissions = this.rolePermissionService.selectList(wrapper);

		// 查询拥有这些角色的用户返回
		List<Long> userIds = Lists.newArrayList();
		if (CollectionUtils.isNotEmpty(rolePermissions)) {
			for (SysRolePermission rolePermission : rolePermissions) {
				List<SysRoleUser> roleUsers = this.roleUserService.findByRoleId(rolePermission.getRoleId());

				if (CollectionUtils.isNotEmpty(roleUsers)) {
					for (SysRoleUser roleUser : roleUsers) {
						if(null != roleUser && null != roleUser.getTenantId() && roleUser.getTenantId().equals(Long.parseLong(tenantId))){
							userIds.add(roleUser.getUserId());
						}
					}
				}
			}
		}
		return userIds;
	}

	@Override
	public void disableBatchUser(List<Long> userIds) {
		SysUser entity = new SysUser();
		entity.setEnable(false);
		AuthUser authUser = WebSecurityUtils.getCurrentUser();
		entity.setUpdateBy(authUser.getId());
		entity.setUpdateTime(new Date());

		Wrapper<SysUser> wrapper = new EntityWrapper<SysUser>();
		wrapper.in("id", userIds);
		wrapper.eq("del_flag", false);
		wrapper.eq("enable", true);
		this.update(entity, wrapper);
		// 批量停用用户，清除用户缓存信息
		String token = WebSecurityUtils.getCurrentToken();
		new Thread() {
			@Override
			public void run() {
				for (Long userId : userIds) {
					SysUser user = userService.selectById(userId);
					if (null != user) {
						clearUserPerms(user.getMobile(), token);
					}

				}
			}
		}.start();
	}

	@Override
	public void enableBatchUser(List<Long> userIds) {
		SysUser entity = new SysUser();
		entity.setEnable(true);
		AuthUser authUser = WebSecurityUtils.getCurrentUser();
		entity.setUpdateBy(authUser.getId());
		entity.setUpdateTime(new Date());

		Wrapper<SysUser> wrapper = new EntityWrapper<SysUser>();
		wrapper.in("id", userIds);
		wrapper.eq("del_flag", false);
		wrapper.eq("enable", false);
		this.update(entity, wrapper);
	}

	@Override
	public void deleteBatchUser(List<Long> userIds) {
		SysUser entity = new SysUser();
		entity.setDelFlag(true);
		AuthUser authUser = WebSecurityUtils.getCurrentUser();
		entity.setUpdateBy(authUser.getId());
		entity.setUpdateTime(new Date());

		// 删除用户将号码置空让此手机号可以重新注册
		entity.setMobile("");
		entity.setEmail("");

		Wrapper<SysUser> wrapper = new EntityWrapper<SysUser>();
		wrapper.in("id", userIds);
		wrapper.eq("del_flag", false);
		this.update(entity, wrapper);

		// 批量删除用户，清除用户缓存信息
		String token = WebSecurityUtils.getCurrentToken();
		new Thread() {
			@Override
			public void run() {
				for (Long userId : userIds) {
					SysUser user = userService.selectById(userId);
					if (null != user) {
						clearUserPerms(user.getMobile(), token);
					}
				}
			}
		}.start();
	}

	@Override
	@Transactional(readOnly = true)
	public boolean isUserMobileExist(String mobile) {
		Wrapper<SysUser> wrapper = new EntityWrapper<SysUser>();
		wrapper.eq("mobile", mobile).eq("del_flag", false);
		return this.selectCount(wrapper) > 0;
	}

	@Override
	@Transactional(readOnly = true)
	public boolean isUserEmailExist(String email) {
		Wrapper<SysUser> wrapper = new EntityWrapper<SysUser>();
		wrapper.eq("email", email).eq("email_activate", true).eq("del_flag", false);
		return this.selectCount(wrapper) > 0;
	}

	@Override
	@Transactional(readOnly = true)
	public int findUserCountByTenantId(Long tenantId) {

		List<SysTenant> tenants = this.sysTenantService.findAllSubTenant(tenantId);
		StringBuilder buider = new StringBuilder();
		if (CollectionUtils.isNotEmpty(tenants)) {
			for (SysTenant tenant : tenants) {
				buider.append(tenant.getId()).append(",");
			}
		}
		buider.append(tenantId);

		Wrapper<SysUser> wrapper = new EntityWrapper<SysUser>();
		wrapper.in("tenant_id", buider.toString()).eq("enable", true).eq("del_flag", false);

		return this.selectCount(wrapper);
	}

	@Override
	public SysUser findByLoginName(String loginName) {
		Wrapper<SysUser> wrapper = new EntityWrapper<SysUser>();
		wrapper.eq("login_name", loginName).eq("enable", true).eq("del_flag", false);		
		return selectOne(wrapper);
	}

	@Override
	public SysUser findByTenantName(String tenantName) {
		EntityWrapper<SysUser> wrapper = new EntityWrapper<SysUser>();
		wrapper.eq("tenant_name", tenantName).eq("del_flag", false);		
		return selectOne(wrapper);
	}

	@Override
	public SysUser login(String loginName, String password) {
	    SysUser loginUserByMobile=null;
	    SysUser loginUserByLoginName=null;
	    Wrapper<SysUser> wrapper1 = new EntityWrapper<SysUser>();
	    Wrapper<SysUser> wrapper2 = new EntityWrapper<SysUser>();
	    if(StringUtils.trimToNull(loginName) != null){
	      wrapper1.eq("mobile", loginName).eq("del_flag", false);
	      loginUserByMobile = selectOne(wrapper1);
	      if(loginUserByMobile != null){
	        if(!passwordEncoder.matches(password, loginUserByMobile.getPassword())){
	          throw ExceptionFactory.create("L_008");
	        }
	        return loginUserByMobile;
	      }
	      
	      if(loginUserByMobile == null){
	        wrapper2.eq("login_name", loginName).eq("del_flag", false);
	        loginUserByLoginName = selectOne(wrapper2);
	        if(loginUserByLoginName == null){
	          throw ExceptionFactory.create("L_009");
	        }
	        if(!passwordEncoder.matches(password, loginUserByLoginName.getPassword())){
	          throw ExceptionFactory.create("L_008");
	        }
	        return loginUserByLoginName;
	      }
	    }

	    return loginUserByMobile != null?loginUserByMobile:loginUserByLoginName;
	}

	@Override
	public boolean isLoginNameExist(String loginName) {
		Wrapper<SysUser> wrapper = new EntityWrapper<SysUser>();
		wrapper.eq("login_name", loginName).eq("del_flag", false);
		return this.selectCount(wrapper) > 0;
	}

	@Override
	public void updateUserTenantNameByTenantId(Long tenantId, String newTenantName) {
		SysUser entity = new SysUser();
		entity.setTenantName(newTenantName);
		Wrapper<SysUser> wrapper = new EntityWrapper<SysUser>();
		wrapper.eq("tenant_id", tenantId);
		this.update(entity, wrapper);
	}

	@Override
	public void updateUserDeptNameByDeptId(Long deptId, String newDeptName) {
		SysUser entity = new SysUser();
		entity.setDeptName(newDeptName);
		Wrapper<SysUser> wrapper = new EntityWrapper<SysUser>();
		wrapper.eq("dept_id", deptId);
		this.update(entity, wrapper);
	}

	@Override
	public boolean getUserPermission(Long userId, Long tenandId, String permissionCode) {
		int count = this.userMapper.getUserPermissionCount(userId, tenandId, permissionCode);
		if(count > 0){
			return true;
		}
		return false;
	}

	@Override
	public boolean getUserEnableAndNotDel(Long userId) {
		SysUser userEnableAndNotDel = this.userMapper.getUserEnableAndNotDel(userId);
		if(userEnableAndNotDel != null){
			return true;
		}
		return false;
	}

	@Override
	public List<SysUser> getcheckUserList(String keyword) {
		Long tenantId = WebSecurityUtils.getCurrentUser().getTenantId();
		Wrapper<SysUser> wrapper = new EntityWrapper<>();
		wrapper.eq("tenant_id", tenantId).eq("del_flag", false);
		if (StringUtils.isNotBlank(keyword)) {
			wrapper.andNew("real_name LIKE {0} OR real_name_py LIKE {0}", "%" + keyword + "%");
		}
		return this.selectList(wrapper);
	}

	@Override
	public WxUserVo getWxUser(String openId) {
		AuthUser currentUser = WebSecurityUtils.getCurrentUser();
		Long userId = currentUser.getId();
		SysUser user = userMapper.selectById(userId);	
		if(user==null)return null;
		WxUserVo wxUser = BeanMapper.map(user, WxUserVo.class);
		wxUser.setName(user.getRealName());
		SysTenant tenant = sysTenantService.selectById(user.getTenantId());
		if(tenant!=null){
			wxUser.setTenantType(tenant.getTenantType());
			SysTenant manageTenant = sysTenantService.selectById(tenant.getManageTenantId());
			if(manageTenant!=null)wxUser.setManageTenantName(manageTenant.getName());
		};
		wxUser.setIsRcvMsg(wxSysUserMapper.getWxUserEnable(userId, openId));
		return wxUser;
	}

	@Override
	public List<SysUser> getUserByMobile(String account, String code) {
		// 查询是否在1分钟内发送过验证码，则不发送返回1分钟重试
		Date currTime = new Date();
		String key = SysConstants.SYS_GET_PASSWORD_CODE + account;
		String verifyCodeJson = this.redisRepository.get(key);
		if (StringUtils.isNotBlank(verifyCodeJson)) {
			VerifyCode currVerifyCode = JSON.parseObject(verifyCodeJson, VerifyCode.class);
//			long date = currTime.getTime() - currVerifyCode.getSendTime().getTime();
//			if ((date / 60000) < 1) {
//				throw ExceptionFactory.create("U_019");
//			}
			//验证码错误
			if(!code.equals(currVerifyCode.getCode())){
				throw ExceptionFactory.create("L_003");
			}
		}else{
			throw ExceptionFactory.create("L_007");
		}
		return findUserListByMobile(account);
	}

	@Override
	public void changeLoginName(String loginName,Long userId) {
		SysUser userDb = userMapper.selectById(userId);
		if(userDb!=null){
			String loginNameDb = userDb.getLoginName();
			if(loginNameDb!=null&&!loginNameDb.equals(loginName)){
				Wrapper<SysUser> wrapper = new EntityWrapper<SysUser>();
				wrapper.eq("login_name", loginName).eq("del_flag", false).eq("enable", true);
				List<SysUser> list = userMapper.selectList(wrapper);
				if(list!=null&&list.size()>0) {
					SysUser sysUser = list.get(0);
					if(sysUser!=null){
						throw ExceptionFactory.create("F_001"); 
					}
				}
			}
			SysUser sysUser = new SysUser();
			sysUser.setId(userId);
			sysUser.setLoginName(loginName);
			userMapper.updateById(sysUser);
		}else {
			throw ExceptionFactory.create("F_002"); 
		}
		
	}

	@Override
	public void resetPwd(String pwb, Long userId) {
		SysUser userDb = userMapper.selectById(userId);
		if(userDb!=null){		
			userDb.setPassword(passwordEncoder.encode(pwb));
			userMapper.updateById(userDb);
		}else {
			throw ExceptionFactory.create("F_003"); 
		}
		
	}

}
