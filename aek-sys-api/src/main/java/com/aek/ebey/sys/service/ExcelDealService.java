package com.aek.ebey.sys.service;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;

import com.aek.ebey.sys.model.bo.TenantImportVo;
import com.aek.ebey.sys.model.bo.UploadRate;

/**
 * excel处理接口
 * 
 * @author mr.han
 *
 */
public interface ExcelDealService {
	/**
	 * 获取导入机构 excel 模板 文件
	 * 
	 * @return
	 */
	public HSSFWorkbook getExportTenantTemplt();

	/**
	 * excel 批量导入机构
	 * 
	 * @param workbook
	 * @throws Exception
	 */
	/*public UploadRate importTenant(Workbook workbook) throws Exception;*/

	/**
	 * 获取excel导入后保存的错误文件，没有返回null
	 * 
	 * @param errorTenantExcelBoRedisKey
	 * @return
	 */
	public HSSFWorkbook getExportTenantError(String errorTenantExcelBoRedisKey);
	
	/**
	 * 多线程处理机构批量导入
	 * @param excelInputStream
	 * @return
	 */
	public TenantImportVo multiThreadImportTenant(Workbook workbook) throws Exception;
	
	/**
	 * 获取excel导入处理进度
	 * @param importRateRedisKey
	 * @return
	 */
	public UploadRate getExcelImportTenantRate(String importRateRedisKey);

}
