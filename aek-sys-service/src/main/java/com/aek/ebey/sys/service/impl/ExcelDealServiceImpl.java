package com.aek.ebey.sys.service.impl;

import java.util.Date;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.commons.collections.CollectionUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aek.common.core.config.RedisRepository;
import com.aek.common.core.exception.ExceptionFactory;
import com.aek.common.core.serurity.JwtTokenUtil;
import com.aek.common.core.serurity.WebSecurityUtils;
import com.aek.common.core.util.ExcelSelectUtils;
import com.aek.common.core.util.ExcelStyleUtil;
import com.aek.common.core.util.ExcelUtils;
import com.aek.ebey.sys.core.SysConstants;
import com.aek.ebey.sys.core.processors.DispatchExcelTenantThread;
import com.aek.ebey.sys.core.processors.ReadExcelTenantProducer;
import com.aek.ebey.sys.core.processors.UploadRateData;
import com.aek.ebey.sys.model.SysArea;
import com.aek.ebey.sys.model.SysTenant;
import com.aek.ebey.sys.model.SysUser;
import com.aek.ebey.sys.model.bo.TenantExcelBo;
import com.aek.ebey.sys.model.bo.TenantImportVo;
import com.aek.ebey.sys.model.bo.UploadRate;
import com.aek.ebey.sys.model.custom.HplTenant;
import com.aek.ebey.sys.service.ExcelDealService;
import com.aek.ebey.sys.service.SysAreaService;
import com.aek.ebey.sys.service.SysTenantService;
import com.aek.ebey.sys.service.SysUserService;
import com.alibaba.fastjson.JSON;

@Service
@Transactional
public class ExcelDealServiceImpl implements ExcelDealService {

	@Autowired
	private SysTenantService tenantService;

	@Autowired
	private SysAreaService areaService;

	@Autowired
	private SysUserService userService;

	@Autowired
	private RedisRepository redisRepository;
	
	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Override
	public HSSFWorkbook getExportTenantTemplt() {
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet("导入模板");

		HSSFCellStyle titleStyle = ExcelStyleUtil.tenantTempltTitleStyle(workbook);// title样式
		CellStyle headerStyle = ExcelStyleUtil.tenantTempltHeaderStyle(workbook); // header样式

		// 注解
		HSSFPatriarch patriarch = sheet.createDrawingPatriarch();

		sheet.setDefaultColumnWidth(26); // 默认宽度
		sheet.setDefaultRowHeightInPoints(21); // 默认高度
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 8)); // 合拼单元格

		int index = 0; // 行坐标

		// title 行
		HSSFRow titleRow = sheet.createRow((short) index++);
		String title = " 1.第1、2行为固定结构，不可更改；第3～4行为示例，导入前请删除  \n 2.列标题不可修改，将鼠标移动到第2行单元格上，查看填写规则 \n 3.导入的机构管理员密码默认为：12345678";
		titleRow.setHeightInPoints(63);
		HSSFCell cell = titleRow.createCell(0);
		cell.setCellValue(title);
		cell.setCellStyle(titleStyle);

		// header 行
		HSSFRow headerRow = sheet.createRow(index++);
		headerRow.setHeightInPoints(25);
		String[] tableHeader = { "机构名称（必填）", "所在省份（必填）", "所在城市（必填）", "所在区县（必填）", "上级行政机构（必填）", "医疗机构代码（必填）",
				"机构管理员姓名（必填）", "手机号（必填）", " 邮箱" };
		for (int i = 0; i < tableHeader.length; i++) {
			HSSFCell headerCell = headerRow.createCell(i);
			headerCell.setCellValue(tableHeader[i]);
			headerCell.setCellStyle(headerStyle);

			// 城市，县/区 加注释
			if (i == 2) {
				ExcelUtils.addComment(patriarch, headerCell, "输入格式示例：杭州市；北京市；石家庄市");
			}
			if (i == 3) {
				ExcelUtils.addComment(patriarch, headerCell, "输入格式示例：西湖区；海淀区；长安区");
			}
		}

		// 设置下拉选 省
		List<SysArea> provinces = this.areaService.queryProvince();
		String[] provincesName = new String[provinces.size()];
		for (int i = 0; i < provinces.size(); i++) {
			SysArea province = provinces.get(i);
			provincesName[i] = province.getName();
		}
		ExcelUtils.setHSSFValidation(sheet, provincesName, 2, 1000, 1, 1);
		// ExcelUtils.setHSSFPrompt(sheet, 2, 500, 1, 1);

		// 监管机构
		List<SysTenant> supervises = tenantService.findAllSuperviseByEveryLevel();
		String[] superviseName = new String[supervises.size()];
		for (int i = 0; i < supervises.size(); i++) {
			SysTenant tenant = supervises.get(i);
			/*if (tenant.getName().length() > 15) {
				tenant.setName(tenant.getName().substring(0, 15) + "..."); // 名称太长省略
			}*/
			superviseName[i] = tenant.getName();
		}
		//ExcelUtils.setHSSFSuperviseValidation(workbook,sheet, superviseName, 2, 500, 4, 4);
		ExcelSelectUtils.setHSSFSuperviseValidation(workbook, sheet, superviseName, 2, 1000, 5);
		
		// demo数据
		String[] demoInfo1 = { "某某省人民医院", "浙江省", "杭州市", "西湖区", "浙江省医疗设备监管中心", "024001", "张三", "15612340989",
				"123@aek56.com" };
		HSSFRow demoInfoRow1 = sheet.createRow(index++);
		for (int i = 0; i < demoInfo1.length; i++) {
			HSSFCell demoInfoCell = demoInfoRow1.createCell(i);
			demoInfoCell.setCellValue(demoInfo1[i]);
		}

		String[] demoInfo2 = { "某某省骨科医院", "浙江省", "杭州市", "西湖区", "浙江省医疗设备监管中心", "024002", "李四", "13012345678",
				"456@aek56.com" };
		HSSFRow demoInfoRow2 = sheet.createRow(index++);
		for (int i = 0; i < demoInfo2.length; i++) {
			HSSFCell demoInfoCell = demoInfoRow2.createCell(i);
			demoInfoCell.setCellValue(demoInfo2[i]);
		}
		return workbook;
	}

/*	@Override
	public UploadRate importTenant(Workbook workbook) throws Exception {
		logger.info("import tenant info begin----------------------");

		// 文件格式检查
		Sheet sheet = workbook.getSheetAt(0);
		if (sheet == null) {
			throw ExceptionFactory.create("EXCEL_01");
		}

		if (sheet.getLastRowNum() < 2) {
			throw ExceptionFactory.create("EXCEL_02");
		}

		int succesNum = 0; // 上传成功数量
		int lastCellNum = 9; // 行最后列位置
		List<TenantExcelBo> tenantExcels = Lists.newArrayList();

		for (int i = 2; i <= sheet.getLastRowNum(); i++) {
			Row row = sheet.getRow(i);
			TenantExcelBo tenant = new TenantExcelBo();
			int emptynum = 0;
			for (int j = 0; j < lastCellNum && row != null; j++) {
				Cell cell = row.getCell(j);
				if (cell == null) {
					cell = row.createCell(j);
				}
				cell.setCellType(Cell.CELL_TYPE_STRING); // 列转换为文本处理
				String cellValue = StringUtils.trimToEmpty(cell.getStringCellValue());
				if (StringUtils.isEmpty(cellValue)) {
					emptynum++;
				}
				this.checkTenantImportCell(cellValue, j, tenant);
			}

			// 空白行忽略
			if (emptynum >= 9) {
				break;
			}
			if (tenant.isCorrectly()) {
				logger.info("import tenant info json : {}", JSON.toJSONString(tenant));
				this.tenantService.importExcelTenant(tenant);
				succesNum++;
			} else {
				tenantExcels.add(tenant);
			}
		}

		if (CollectionUtils.isNotEmpty(tenantExcels)) {
			Long userId = WebSecurityUtils.getCurrentUser().getId();
			String key = SysConstants.SYS_EXCEL_IMPORT_TENANT + userId;
			long expireTime = 5 * 60 * 60; // 5小时有效
			this.redisRepository.setExpire(key, JSON.toJSONString(tenantExcels), expireTime);
		}

		logger.info("import tenant info end----------------------");

		// 文件内容为空则提示错误
		if (succesNum == 0 && tenantExcels.size() == 0) {
			throw ExceptionFactory.create("EXCEL_04");
		}
		return new UploadRate(succesNum, tenantExcels.size());
	}*/

	/**
	 * 
	 * @param cellValue
	 * @return
	 */
	/*private void checkTenantImportCell(String cellValue, int cellIndex, TenantExcelBo tenant) {
		String cellName = ""; // 列名称
		String errorMsg = ""; // 错误消息
		String notOutput = "notOutput"; // 消息不记录标识

		List<String> errorMsgs = tenant.getErrorMsg();
		List<Integer> errorIndexs = tenant.getErrorIndex();
		switch (cellIndex) {
		case 0:
			cellName = "机构名称";
			tenant.setName(cellValue);
			if (StringUtils.isEmpty(cellValue)) {
				errorMsg = "机构名称不能为空";
				break;
			}
			if (cellValue.length() > 40) {
				errorMsg = "机构名称格式错误";
				break;
			}
			if (this.tenantService.isRepeatByNameAndType(cellValue, TenantType.HOSPITAL.getNumber())) {
				errorMsg = "机构名称已存在";
			}
			break;
		case 1:
			cellName = "所在省份";
			tenant.setProvince(cellValue);
			if (StringUtils.isEmpty(cellValue)) {
				errorMsg = "所在省份不能为空";
			} else {
				if (!areaService.isProvinceNameExist(cellValue)) {
					errorMsg = "名称错误不存在";
				}
			}
			break;
		case 2:
			cellName = "所在城市";
			tenant.setCity(cellValue);
			if (errorIndexs.contains(1)) {
				errorMsg = notOutput;
				break;
			}
			if (StringUtils.isEmpty(cellValue)) {
				errorMsg = "所在城市不能为空";
			} else {
				if (!areaService.isCityNameExist(tenant.getProvince(), cellValue)) {
					errorMsg = "名称错误不存在";
				}
			}
			break;
		case 3:
			cellName = "所在区县";
			tenant.setCounty(cellValue);
			if (errorIndexs.contains(2)) {
				errorMsg = notOutput;
				break;
			}
			if (StringUtils.isEmpty(cellValue)) {
				errorMsg = "所在区县不能为空";
			} else {
				if (!areaService.isRegionNameExist(tenant.getCity(), cellValue)) {
					errorMsg = "名称错误不存在";
				}
			}
			break;
		case 4:
			cellName = "上级行政机构";
			tenant.setManageTenantName(cellValue);
			if (StringUtils.isEmpty(cellValue)) {
				errorMsg = "上级行政机构不能为空";
			} else {
				SysTenant manageTenant = this.tenantService.findByTenantName(cellValue);
				if (manageTenant == null) {
					errorMsg = "上级行政机构不存在";
				} else {
					tenant.setManageTenantId(manageTenant.getId());
				}
			}
			break;
		case 5:
			cellName = "医疗机构代码";
			HplTenant hpl = new HplTenant();
			hpl.setOrgCode(cellValue);
			tenant.setHplTenant(hpl);
			if (StringUtils.isEmpty(cellValue)) {
				errorMsg = "医疗机构代码不能为空";
			}
			if (CollectionUtils.isNotEmpty(this.tenantService.findByOrgCode(cellValue))) {
				errorMsg = "医疗机构代码重复";
			}
			break;
		case 6:
			cellName = "机构管理员姓名";
			tenant.getAdmin().setRealName(cellValue);
			if (StringUtils.isEmpty(cellValue)) {
				errorMsg = "机构管理员姓名不能为空";
				break;
			}
			if (cellValue.length() > 40) {
				errorMsg = "机构管理员姓名格式错误";
			}
			break;
		case 7:
			cellName = "手机号";
			tenant.getAdmin().setMobile(cellValue);
			if (StringUtils.isEmpty(cellValue)) {
				errorMsg = "手机号不能为空";
			} else {
				if (!RegexValidateUtil.checkCellphone(cellValue)) {
					errorMsg = "格式不正确";
				} else {
					if (this.userService.isUserMobileExist(cellValue)) {
						errorMsg = "该手机号码已经被使用";
					}
				}
			}
			break;
		case 8:
			tenant.getAdmin().setEmail(cellValue);
			cellName = "邮箱";
			if (StringUtils.isNotEmpty(cellValue)) {
				if (!RegexValidateUtil.checkEmail(cellValue)) {
					errorMsg = "格式不正确";
				} else {
					if (this.userService.isUserEmailExist(cellValue)) {
						errorMsg = "该邮箱已经被使用";
					}
				}
			}
			break;
		default:
			break;
		}

		if (StringUtils.isNotEmpty(errorMsg)) {
			tenant.setCorrectly(false); // 设置当前机构不可导入
			errorIndexs.add(cellIndex); // 记录错误节点
			if (!notOutput.equals(errorMsg)) {
				errorMsgs.add(cellName + "[" + errorMsg + "]");
			}
		}
	}*/

	@Override
	public HSSFWorkbook getExportTenantError(String errorTenantExcelBoRedisKey) {
		List<String> value = this.redisRepository.getList(errorTenantExcelBoRedisKey, 0, Integer.MAX_VALUE);
		List<TenantExcelBo> tenantExcels = JSON.parseArray(value.toString(), TenantExcelBo.class);

		if (CollectionUtils.isEmpty(tenantExcels)) {
			throw ExceptionFactory.create("EXCEL_03");
		}

		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet("导入错误数据");
		HSSFCellStyle titleStyle = ExcelStyleUtil.tenantTempltTitleStyle(workbook);// title样式
		CellStyle headerStyle = ExcelStyleUtil.tenantTempltHeaderStyle(workbook); // header样式
		CellStyle backWrongStyle = ExcelStyleUtil.tenantImportCellWrongStyle(workbook);// cell错误背景色样式
		CellStyle wrongStyle = ExcelStyleUtil.tenantImportWrongStyle(workbook); // cell尾错误样式

		// 注解
		HSSFPatriarch patriarch = sheet.createDrawingPatriarch();

		sheet.setDefaultColumnWidth(26); // 默认宽度
		sheet.setDefaultRowHeightInPoints(21); // 默认高度
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 9)); // 合拼单元格

		int index = 0; // 行坐标

		// title 行
		HSSFRow titleRow = sheet.createRow((short) index++);
		String title = " 1.第1、2行为固定结构，不可更改；第3～4行为示例，导入前请删除  \n 2.列标题不可修改，将鼠标移动到第2行单元格上，查看填写规则 \n 3.导入的机构管理员密码默认为：12345678";
		titleRow.setHeightInPoints(63);
		HSSFCell cell = titleRow.createCell(0);
		cell.setCellValue(title);
		cell.setCellStyle(titleStyle);

		// header 行
		HSSFRow headerRow = sheet.createRow(index++);
		headerRow.setHeightInPoints(25);
		String[] tableHeader = { "机构名称（必填）", "所在省份（必填）", "所在城市（必填）", "所在区县（必填）", "上级行政机构（必填）", "医疗机构代码（必填）",
				"机构管理员姓名（必填）", "手机号（必填）", " 邮箱", "导入失败原因" };
		for (int i = 0; i < tableHeader.length; i++) {
			HSSFCell headerCell = headerRow.createCell(i);
			headerCell.setCellValue(tableHeader[i]);
			headerCell.setCellStyle(headerStyle);
			// 城市，县/区 加注释
			if (i == 2) {
				ExcelUtils.addComment(patriarch, headerCell, "输入格式示例：杭州市；北京市；石家庄市");
			}
			if (i == 3) {
				ExcelUtils.addComment(patriarch, headerCell, "输入格式示例：西湖区；海淀区；长安区");
			}
		}

		// 设置下拉选 省
		List<SysArea> provinces = this.areaService.queryProvince();
		String[] provincesName = new String[provinces.size()];
		for (int i = 0; i < provinces.size(); i++) {
			SysArea province = provinces.get(i);
			provincesName[i] = province.getName();
		}
		ExcelUtils.setHSSFValidation(sheet, provincesName, 2, 1000, 1, 1);

		// 监管机构
		List<SysTenant> supervises = tenantService.findAllSuperviseByEveryLevel();
		String[] superviseName = new String[supervises.size()];
		for (int i = 0; i < supervises.size(); i++) {
			SysTenant tenant = supervises.get(i);
			if (tenant.getName().length() > 15) {
				tenant.setName(tenant.getName().substring(0, 15) + "..."); // 名称太长省略
			}
			superviseName[i] = tenant.getName();
		}
		//ExcelUtils.setHSSFSuperviseValidation(workbook,sheet, superviseName, 2, 500, 4, 4);
		ExcelSelectUtils.setHSSFSuperviseValidation(workbook, sheet, superviseName, 2, 1000, 5);

		for (TenantExcelBo bo : tenantExcels) {
			List<String> errorMsg = bo.getErrorMsg();
			List<Integer> errorIndexs = bo.getErrorIndex();

			Row row = sheet.createRow(index++);
			row.createCell(0).setCellValue(bo.getName());
			row.createCell(1).setCellValue(bo.getProvince());
			row.createCell(2).setCellValue(bo.getCity());
			row.createCell(3).setCellValue(bo.getCounty());
			row.createCell(4).setCellValue(bo.getManageTenantName());

			HplTenant hplTenant = bo.getHplTenant();
			if(null != hplTenant){
				row.createCell(5).setCellValue(hplTenant.getOrgCode());
			}else{
				row.createCell(5).setCellValue("");
			}
			

			SysUser admin = bo.getAdmin();
			row.createCell(6).setCellValue(admin.getRealName());
			row.createCell(7).setCellValue(admin.getMobile());
			row.createCell(8).setCellValue(admin.getEmail());

			StringBuilder builder = new StringBuilder();
			for (String msg : errorMsg) {
				builder.append(msg).append("\n");
			}

			Cell errorCell = row.createCell(9);
			errorCell.setCellStyle(wrongStyle);
			errorCell.setCellValue(builder.toString());

			for (Integer errorIndex : errorIndexs) {
				row.getCell(errorIndex).setCellStyle(backWrongStyle);
			}
		}

		return workbook;
	}

	@Override
	public TenantImportVo multiThreadImportTenant(Workbook workBook) throws RuntimeException{
		// 文件格式检查
		Sheet sheet = workBook.getSheetAt(0);
		if (sheet == null) {
			throw ExceptionFactory.create("EXCEL_01");
		}

		if (sheet.getLastRowNum() < 2) {
			throw ExceptionFactory.create("EXCEL_02");
		}
		Long userId = WebSecurityUtils.getCurrentUser().getId();
		String deviceId = jwtTokenUtil.getDeviceIdFromToken(WebSecurityUtils.getCurrentToken());
		BlockingQueue<TenantExcelBo> queue = new LinkedBlockingQueue<>(100);
		String importRateRedisKey = deviceId+":"+SysConstants.SYS_EXCEL_IMPORT_TENANT_SCHEDULE + ":" + userId + ":"+new Date().getTime();
		String errorTenantExcelBoRedisKey = deviceId+":"+SysConstants.SYS_EXCEL_IMPORT_TENANT + ":" + userId + ":"+new Date().getTime();
		UploadRateData updateRateData = new UploadRateData(redisRepository,importRateRedisKey);
		ReadExcelTenantProducer producer = new ReadExcelTenantProducer(queue,importRateRedisKey,redisRepository,workBook,updateRateData);
		DispatchExcelTenantThread dispatch = new DispatchExcelTenantThread(queue,errorTenantExcelBoRedisKey,tenantService,areaService,userService,redisRepository,updateRateData,WebSecurityUtils.getCurrentUser());
		Thread p = new Thread(producer);
		Thread d = new Thread(dispatch);
		p.start();
		d.start();
		return new TenantImportVo(importRateRedisKey,errorTenantExcelBoRedisKey);
	}
	
	@Override
	public UploadRate getExcelImportTenantRate(String importRateRedisKey){
		UploadRate uploadRate = JSON.parseObject(redisRepository.get(importRateRedisKey), UploadRate.class);
		return uploadRate;
	}

}
