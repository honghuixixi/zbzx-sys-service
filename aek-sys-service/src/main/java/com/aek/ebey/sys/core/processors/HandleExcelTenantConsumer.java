package com.aek.ebey.sys.core.processors;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import com.aek.common.core.config.RedisRepository;
import com.aek.common.core.enums.TenantType;
import com.aek.common.core.serurity.model.AuthUser;
import com.aek.common.core.sms.RegexValidateUtil;
import com.aek.ebey.sys.model.SysTenant;
import com.aek.ebey.sys.model.bo.TenantExcelBo;
import com.aek.ebey.sys.model.custom.HplTenant;
import com.aek.ebey.sys.service.SysAreaService;
import com.aek.ebey.sys.service.SysTenantService;
import com.aek.ebey.sys.service.SysUserService;
import com.alibaba.fastjson.JSON;

/**
 * 处理机构Excel数据导入数据对象，将队列中的数据对象进行验证以及入库操作
 * 
 * @author HongHui
 * @date 2017年8月2日
 */
public class HandleExcelTenantConsumer implements Runnable {	

	// 机构数据对象
	private TenantExcelBo tenantExcelBo;
	// 错误数据放入缓存Key
	private String errorTenantExcelBoRedisKey;
	//当前登录用户
	private AuthUser currentUser;
	
	private SysTenantService sysTenantService;
	private SysAreaService sysAreaService;
	private SysUserService sysUserService;
	private RedisRepository redisRepository;
	private UploadRateData uploadRateData;
	
	public HandleExcelTenantConsumer(TenantExcelBo tenantExcelBo,String errorTenantExcelBoRedisKey,
			SysTenantService sysTenantService,SysAreaService sysAreaService,SysUserService sysUserService,RedisRepository redisRepository,
			UploadRateData uploadRateData,AuthUser currentUser) {
		this.tenantExcelBo = tenantExcelBo;
		this.errorTenantExcelBoRedisKey = errorTenantExcelBoRedisKey;
		this.sysTenantService = sysTenantService;
		this.sysAreaService = sysAreaService;
		this.sysUserService = sysUserService;
		this.redisRepository = redisRepository;
		this.uploadRateData = uploadRateData;
		this.currentUser = currentUser;
	}

	@Override
	public void run() {
		//1.校验导入数据
		checkTenantExcelBo(tenantExcelBo);
		//2.将数据导入数据库，有误则放入错误缓存
		if (tenantExcelBo.isCorrectly()) {
			//2.1数据正确，则将导入数据库
			this.sysTenantService.importExcelTenant(tenantExcelBo,currentUser);
			uploadRateData.successNumIncrement();
		} else {
			//2.2数据有错误，则将该数据对象放入redis缓存
			redisRepository.leftPush(errorTenantExcelBoRedisKey, JSON.toJSONString(tenantExcelBo));
			uploadRateData.errorNumIncrement();
		}
	}

	/**
	 * 检查对象数据有效性
	 * @param cellValue
	 * @param cellIndex
	 * @param tenantExcelBo
	 */
	private void checkTenantExcelBo(TenantExcelBo tenantExcelBo) {
		List<String> errorMsgs = tenantExcelBo.getErrorMsg();
		List<Integer> errorIndexs = tenantExcelBo.getErrorIndex();
		//机构名称
		String name = tenantExcelBo.getName();
		boolean isNameCorrectly = true;
		if (StringUtils.isBlank(name)) {
			errorMsgs.add("机构名称[机构名称不能为空]");
			isNameCorrectly = false;
		} else if (name.length() > 40) {
			errorMsgs.add("机构名称[机构名称格式错误]");
			isNameCorrectly = false;
		} else if (this.sysTenantService.isRepeatByNameAndType(name, TenantType.HOSPITAL.getNumber())) {
			errorMsgs.add("机构名称[机构名称已存在]");
			isNameCorrectly = false;
		}
		if(!isNameCorrectly){
			errorIndexs.add(0);
		}
		//所在省份
		boolean isProvinceCorrectly = true;
		String province = tenantExcelBo.getProvince();
		if (StringUtils.isBlank(province)) {
			errorMsgs.add("所在省份[所在省份不能为空]");
			isProvinceCorrectly = false;
		} else {
			if (!sysAreaService.isProvinceNameExist(province)) {
				errorMsgs.add("所在省份[名称错误不存在]");
				isProvinceCorrectly = false;
			}
		}
		if(!isProvinceCorrectly){
			errorIndexs.add(1);
		}
		//所在城市
		boolean isCityCorrectly = true;
		String city = tenantExcelBo.getCity();
		if (StringUtils.isEmpty(city)) {
			errorMsgs.add("所在城市[所在城市不能为空]");
			isCityCorrectly = false;
		} else {
			if (!sysAreaService.isCityNameExist(province, city)) {
				errorMsgs.add("所在城市[名称错误不存在]");
				isCityCorrectly = false;
			}
		}
		if(!isCityCorrectly){
			errorIndexs.add(2);
		}
		//所在区县
		boolean isCountyCorrectly = true;
		String county = tenantExcelBo.getCounty();
		if (StringUtils.isEmpty(county)) {
			errorMsgs.add("所在区县[所在区县不能为空]");
			isCountyCorrectly = false;
		} else {
			if (!sysAreaService.isRegionNameExist(city, county)) {
				errorMsgs.add("所在区县[名称错误不存在]");
				isCountyCorrectly = false;
			}
		}
		if(!isCountyCorrectly){
			errorIndexs.add(3);
		}
		//上级行政机构
		boolean isManageTenantNameCorrectly = true;
		String manageTenantName = tenantExcelBo.getManageTenantName();
		if (StringUtils.isEmpty(manageTenantName)) {
			errorMsgs.add("上级行政机构[上级行政机构不能为空]");
			isManageTenantNameCorrectly = false;
		} else {
			SysTenant manageTenant = this.sysTenantService.findByTenantName(manageTenantName);
			if (manageTenant == null) {
				errorMsgs.add("上级行政机构[上级行政机构不存在]");
				isManageTenantNameCorrectly = false;
			} else {
				tenantExcelBo.setManageTenantId(manageTenant.getId());
			}
		}
		if(!isManageTenantNameCorrectly){
			errorIndexs.add(4);
		}
		//医疗机构代码
		String orgCode = "";
		boolean isOrgCodeCorrectly = true; 
		HplTenant hpl = tenantExcelBo.getHplTenant();
		if(null != hpl){
			orgCode = tenantExcelBo.getHplTenant().getOrgCode();
		}
		if (null == hpl || StringUtils.isEmpty(orgCode)) {
			errorMsgs.add("医疗机构代码[医疗机构代码不能为空]");
			isOrgCodeCorrectly = false;
		}
		if (CollectionUtils.isNotEmpty(this.sysTenantService.findByOrgCode(orgCode))) {
			errorMsgs.add("医疗机构代码[医疗机构代码重复]");
			isOrgCodeCorrectly = false;
		}
		if(!isOrgCodeCorrectly){
			errorIndexs.add(5);
		}
		//机构管理员姓名
		boolean isRealNameCorrectly = true; 
		String realName = tenantExcelBo.getAdmin().getRealName();
		if (StringUtils.isEmpty(realName)) {
			errorMsgs.add("机构管理员姓名[机构管理员姓名不能为空]");
			isRealNameCorrectly = false; 
		}else if (realName.length() > 40) {
			errorMsgs.add("机构管理员姓名[机构管理员姓名格式错误]");
			isRealNameCorrectly = false; 
		}
		if(!isRealNameCorrectly){
			errorIndexs.add(6);
		}
		//手机
		boolean isMobileCorrectly = true; 
		String mobile = tenantExcelBo.getAdmin().getMobile();
		if (StringUtils.isEmpty(mobile)) {
			errorMsgs.add("手机[手机号不能为空]");
			isMobileCorrectly = false; 
		} else {
			if (!RegexValidateUtil.checkCellphone(mobile)) {
				errorMsgs.add("手机[格式不正确]");
				isMobileCorrectly = false;
			} else {
				if (this.sysUserService.isUserMobileExist(mobile)) {
					errorMsgs.add("手机[该手机号码已经被使用]");
					isMobileCorrectly = false;
				}
			}
		}
		if(!isMobileCorrectly){
			errorIndexs.add(7);
		}
		//邮箱
		boolean isEmailCorrectly = true; 
		String email = tenantExcelBo.getAdmin().getEmail();
		if (StringUtils.isNotEmpty(email)) {
			if (!RegexValidateUtil.checkEmail(email)) {
				errorMsgs.add("邮箱[格式不正确]");
				isEmailCorrectly = false; 
			} else {
				if (this.sysUserService.isUserEmailExist(email)) {
					errorMsgs.add("邮箱[该邮箱已经被使用]");
					isEmailCorrectly = false; 
				}
			}
		}
		if(!isEmailCorrectly){
			errorIndexs.add(8);
		}
		if(null != errorMsgs && errorMsgs.size() > 0){
			tenantExcelBo.setCorrectly(false);
			List<String> newErrorMsgs = new ArrayList<String>();
			for(int i = 0; i < errorMsgs.size(); i++){
				newErrorMsgs.add((i+1)+"."+errorMsgs.get(i));
			}
			tenantExcelBo.setErrorMsg(newErrorMsgs);
		}
	}
	
}
