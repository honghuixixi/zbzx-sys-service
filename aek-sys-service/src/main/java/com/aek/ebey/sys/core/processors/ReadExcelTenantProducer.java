package com.aek.ebey.sys.core.processors;

import java.util.concurrent.BlockingQueue;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.aek.common.core.config.RedisRepository;
import com.aek.ebey.sys.model.bo.TenantExcelBo;
import com.aek.ebey.sys.model.bo.UploadRate;
import com.aek.ebey.sys.model.custom.HplTenant;
import com.alibaba.fastjson.JSON;

/**
 * 读取机构Excel数据导入数据对象放入队列中
 * 
 * @author HongHui
 * @date 2017年8月2日
 */
public class ReadExcelTenantProducer implements Runnable {	

	// 缓存操作对象
	private RedisRepository redisRepository;
	// Excel文件
	private Workbook workBook;
	// 机构数据对象队列,先进先出
	private BlockingQueue<TenantExcelBo> queue;
	// 机构数据上传进度数据放入缓存Key
	private String importRateRedisKey;
	// 上传进度
	private UploadRateData uploadRateData;

	public ReadExcelTenantProducer(BlockingQueue<TenantExcelBo> queue,String importRateRedisKey,RedisRepository redisRepository,Workbook workBook,UploadRateData uploadRateData) {
		this.queue = queue;
		this.importRateRedisKey = importRateRedisKey;
		this.redisRepository = redisRepository;
		this.workBook = workBook;
		this.uploadRateData = uploadRateData;
	}

	@Override
	public void run() {
		Sheet sheet = workBook.getSheetAt(0);
		int lastRowNum = sheet.getPhysicalNumberOfRows();  // Excel最后一行行号
		int lastCellNum = 9; // 行最后列列号
		UploadRate updateRate = new UploadRate(lastRowNum - 2);
		long expireTime = 5 * 60 * 60; // 5小时有效
		redisRepository.setExpire(importRateRedisKey, JSON.toJSONString(updateRate),expireTime);
		for (int i = 2; i < lastRowNum; i++) {
			Row row = sheet.getRow(i);
			TenantExcelBo tenantExcelBo = new TenantExcelBo();
			int emptyCellNum = 0; // 一行中空数据列的数量
			for (int j = 0; j < lastCellNum && row != null; j++) {
				Cell cell = row.getCell(j);
				if (cell == null) {
					cell = row.createCell(j);
				}
				cell.setCellType(Cell.CELL_TYPE_STRING); // 列转换为文本处理
				String cellValue = StringUtils.trimToEmpty(cell.getStringCellValue());
				if (StringUtils.isEmpty(cellValue)) {
					emptyCellNum++;
				}
				setValuesForTenantExcelBo(cellValue, j, tenantExcelBo);
			}
			if (emptyCellNum >= lastCellNum) {
				uploadRateData.totalNumDecrement();
				continue;
			}
			//将最后一个数据放入队列时，将其状态置为最后一个
			if((i+1) == lastRowNum){
				tenantExcelBo.setLastFlag(true);
			}
			try {
				queue.put(tenantExcelBo);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} 
	}

	/**
	 * 将Excel某一列的数值放入对象中
	 * @param cellValue
	 * @param cellIndex
	 * @param tenantExcelBo
	 */
	@SuppressWarnings("unused")
	private static void setValuesForTenantExcelBo(String cellValue, int cellIndex, TenantExcelBo tenantExcelBo) {
		String cellName = "";
		switch (cellIndex) {
		case 0:
			cellName = "机构名称";
			tenantExcelBo.setName(cellValue);
			break;
		case 1:
			cellName = "所在省份";
			tenantExcelBo.setProvince(cellValue);
			break;
		case 2:
			cellName = "所在城市";
			tenantExcelBo.setCity(cellValue);
			break;
		case 3:
			cellName = "所在区县";
			tenantExcelBo.setCounty(cellValue);
			break;
		case 4:
			cellName = "上级行政机构";
			tenantExcelBo.setManageTenantName(cellValue);
			break;
		case 5:
			cellName = "医疗机构代码";
			HplTenant hpl = new HplTenant();
			hpl.setOrgCode(cellValue);
			tenantExcelBo.setHplTenant(hpl);
			break;
		case 6:
			cellName = "机构管理员姓名";
			tenantExcelBo.getAdmin().setRealName(cellValue);
			break;
		case 7:
			cellName = "手机号";
			tenantExcelBo.getAdmin().setMobile(cellValue);
			break;
		case 8:
			tenantExcelBo.getAdmin().setEmail(cellValue);
			cellName = "邮箱";
			break;
		default:
			break;
		}
	}

}
