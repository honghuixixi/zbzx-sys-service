package com.aek.ebey.sys.web.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;

import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.aek.common.core.Result;
import com.aek.common.core.base.BaseController;
import com.aek.common.core.exception.ExceptionFactory;
import com.aek.common.core.util.DateUtil;
import com.aek.common.core.util.DateUtil.DATE_PATTERN;
import com.aek.ebey.sys.model.bo.TenantImportVo;
import com.aek.ebey.sys.model.bo.UploadRate;
import com.aek.ebey.sys.service.ExcelDealService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;

/**
 * <p>
 * excel文件导入导出 前端控制器
 * </p>
 *
 * @author aek
 * @since 2017-05-06
 */
@Controller
@RequestMapping("/zbzxsys/excel")
@Api(value = "SysExcelController", description = "excel文件导入导出管理")
public class SysExcelController extends BaseController {
	private static final Logger logger = LogManager.getLogger(SysExcelController.class);

	@Autowired
	private ExcelDealService excelDealService;

	/**
	 * 机构导入
	 * 
	 * @param file
	 * @return
	 * @throws IOException
	 *             异常抛出 全局拦截
	 */
	/*@PreAuthorize("isAuthenticated()")
	@ResponseBody
	@PostMapping(value = "/import/tenant")
	@ApiOperation(value = "导入机构(excel)", httpMethod = "POST")
	@ApiResponse(code = 200, message = "OK", response = Result.class)
	public Result<UploadRate> excelImportTenant(@RequestParam("file") MultipartFile file) throws Exception {
		InputStream is = null;
		try {
			is = file.getInputStream();
			Workbook workbook = WorkbookFactory.create(is); // 兼容2003 /2007 版本
			return response(this.excelDealService.importTenant(workbook));
		} catch (Exception e) {
			logger.error("excel importTenant data error method way SysExcelController#excelImportTenant ", e);
			throw e;
		} finally {
			try {
				if (is != null) {
					is.close();
				}
			} catch (IOException e) {
				logger.error(e);
			}
		}
	}*/
	
	/**
	 * 机构导入
	 * 
	 * @param file
	 * @return
	 * @throws IOException
	 *             异常抛出 全局拦截
	 */
	@PreAuthorize("isAuthenticated()")
	@ResponseBody
	@PostMapping(value = "/import/tenant")
	@ApiOperation(value = "导入机构(excel)", httpMethod = "POST")
	@ApiResponse(code = 200, message = "OK", response = Result.class)
	public Result<TenantImportVo> excelImportTenant(@RequestParam("file") MultipartFile file) throws Exception {
		InputStream is = null;
		try {
			is = file.getInputStream();
			//兼容2003 /2007 版本
			Workbook workbook = WorkbookFactory.create(is);   
			return response(this.excelDealService.multiThreadImportTenant(workbook));
		} catch (IllegalArgumentException e){
			throw ExceptionFactory.create("EXCEL_05");
		} catch (RuntimeException e) {
			logger.error("excel importTenant data error method way SysExcelController#excelImportTenant ", e);
			throw ExceptionFactory.create("500");
		}
		finally {
			try {
				if (is != null) {
					is.close();
				}
			} catch (IOException e) {
				logger.error(e);
			}
		}
	}

	@PreAuthorize("isAuthenticated()")
	@ResponseBody
	@GetMapping(value = "/import/tenant/rate")
	@ApiOperation(value = "导入机构(excel)处理进度", httpMethod = "GET")
	@ApiResponse(code = 200, message = "OK", response = Result.class)
	public Result<UploadRate> getExcelImportTenantRate(@RequestParam(value = "importRateRedisKey", required = true) String importRateRedisKey){
		return response(excelDealService.getExcelImportTenantRate(importRateRedisKey));
	}
	
	/**
	 * 导出机构导入excel模板
	 * 
	 * @param file
	 * @return
	 * @throws Exception
	 */
	@GetMapping(value = "/export/templt/tenant")
	@ApiOperation(value = "获取导入机构excel模板", httpMethod = "GET")
	@ApiResponse(code = 200, message = "OK", response = Result.class)
	public void excelExportTenantTemplt(HttpServletResponse response) throws Exception {
		HSSFWorkbook workbook = this.excelDealService.getExportTenantTemplt();
		OutputStream out = null;
		try {
			out = response.getOutputStream();
			String fileName = "医疗机构导入模板.xls";
			String headStr = "attachment; filename=\"" + fileName + "\"";
			response.setContentType("APPLICATION/OCTET-STREAM");
			response.setHeader("Content-Disposition", new String(headStr.getBytes("UTF-8"), "iso-8859-1"));
			workbook.write(out);
		} catch (Exception e) {
			logger.error("excel exportTenantTemplt error method way SysExcelController#excelExportTenantTemplt", e);
			throw e;
		} finally {
			try {
				if (out != null) {
					out.close();
				}
			} catch (IOException e) {
				logger.error(e);
			}
		}
	}

	/**
	 * 获取导入输出错误excel
	 * 
	 * @param file
	 * @return
	 * @throws Exception
	 */
	@GetMapping(value = "/export/error/tenant/")
	@ApiOperation(value = "获取导入输出错误excel", httpMethod = "GET")
	@ApiResponse(code = 200, message = "OK", response = Result.class)
	public void excelExportTenantErrorFile(@RequestParam(value = "errorTenantExcelBoRedisKey", required = true) String errorTenantExcelBoRedisKey, HttpServletResponse response)
			throws Exception {
		HSSFWorkbook workbook = this.excelDealService.getExportTenantError(errorTenantExcelBoRedisKey);
		OutputStream out = null;
		try {
			out = response.getOutputStream();
			String fileName = "导入失败数据_" + DateUtil.format(new Date(), DATE_PATTERN.YYYYMMDD) + ".xls";
			String headStr = "attachment; filename=\"" + fileName + "\"";
			response.setContentType("APPLICATION/OCTET-STREAM");
			response.setHeader("Content-Disposition", new String(headStr.getBytes("UTF-8"), "iso-8859-1"));
			workbook.write(out);
		} catch (Exception e) {
			logger.error("excel exportTenantTemplt error method way SysExcelController#excelExportTenantErrorFile", e);
			throw e;
		} finally {
			try {
				if (out != null) {
					out.close();
				}
			} catch (IOException e) {
				logger.error(e);
			}
		}
	}

}
