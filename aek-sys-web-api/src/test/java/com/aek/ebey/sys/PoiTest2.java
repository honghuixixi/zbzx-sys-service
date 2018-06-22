package com.aek.ebey.sys;

import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.hssf.usermodel.DVConstraint;
import org.apache.poi.hssf.usermodel.HSSFDataValidation;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Name;
import org.apache.poi.ss.util.CellRangeAddressList;

public class PoiTest2 {

	public static void main(String[] args) throws IOException {
		HSSFWorkbook wb = new HSSFWorkbook();// excel文件对象
		HSSFSheet sheetlist = wb.createSheet("sheetlist");// 工作表对象

		FileOutputStream out = new FileOutputStream("d:\\success.xls");
		String[] textlist = { "列表1", "列表2", "列表3", "列表4", "列表5" };

		sheetlist = setHSSFAdvanceValidation(wb,sheetlist, textlist, 0, 500, 0, 0);// 第一列的前501行都设置为选择列表形式.
		//sheetlist = setHSSFPrompt(sheetlist, "promt Title", "prompt Content", 0, 500, 1, 1);// 第二列的前501行都设置提示.

		wb.write(out);
		out.close();
	}

	/**
	 * 设置某些列的值只能输入预制的数据,显示下拉框.
	 * 
	 * @param sheet
	 *            要设置的sheet.
	 * @param textlist
	 *            下拉框显示的内容
	 * @param firstRow
	 *            开始行
	 * @param endRow
	 *            结束行
	 * @param firstCol
	 *            开始列
	 * @param endCol
	 *            结束列
	 * @return 设置好的sheet.
	 */
	public static HSSFSheet setHSSFValidation(HSSFSheet sheet, String[] textlist, int firstRow, int endRow,
			int firstCol, int endCol) {
		// 加载下拉列表内容
		DVConstraint constraint = DVConstraint.createExplicitListConstraint(textlist);
		// 设置数据有效性加载在哪个单元格上,四个参数分别是：起始行、终止行、起始列、终止列
		CellRangeAddressList regions = new CellRangeAddressList(firstRow, endRow, firstCol, endCol);
		// 数据有效性对象
		HSSFDataValidation data_validation_list = new HSSFDataValidation(regions, constraint);
		sheet.addValidationData(data_validation_list);
		return sheet;
	}
	
	/**
	 * 设置某些列的值只能输入预制的数据,显示下拉框. 避免出现数据过多报错问题
	 * 
	 * @param sheet
	 *            要设置的sheet.
	 * @param textlist
	 *            下拉框显示的内容
	 * @param firstRow
	 *            开始行
	 * @param endRow
	 *            结束行
	 * @param firstCol
	 *            开始列
	 * @param endCol
	 *            结束列
	 * @return 设置好的sheet.
	 */
	public static HSSFSheet setHSSFAdvanceValidation(HSSFWorkbook workbook,HSSFSheet sheet, String[] textlist, int firstRow, int endRow,
			int firstCol, int endCol) {
		//=======当前代码问题：当下拉框数据过多时报错：String literals in formulas can't be bigger than 255 characters ASCII========
		// 加载下拉列表内容
		//DVConstraint constraint = DVConstraint.createExplicitListConstraint(textlist);
		// 设置数据有效性加载在哪个单元格上,四个参数分别是：起始行、终止行、起始列、终止列
		//CellRangeAddressList regions = new CellRangeAddressList(firstRow, endRow, firstCol, endCol);
		// 数据有效性对象
		//HSSFDataValidation data_validation_list = new HSSFDataValidation(regions, constraint);
		//sheet.addValidationData(data_validation_list);
		//return sheet;
		//=======当前代码问题：当下拉框数据过多时报错：String literals in formulas can't be bigger than 255 characters ASCII========
		
		// 改进方案：如下代码
		String hiddenSheetName = "DropDownListHidden";//+new Date().getTime();
		// 创建隐藏域
		HSSFSheet hiddenSheet = workbook.createSheet(hiddenSheetName); 
        for (int i = 0, length = textlist.length; i < length; i++) { 
        	// 循环赋值（为了防止下拉框的行数与隐藏域的行数相对应来获取>=选中行数的数组，将隐藏域加到结束行之后）
        	hiddenSheet.createRow(endRow+i).createCell(1).setCellValue(textlist[i]);
        } 
        Name name = workbook.createName(); 
        name.setNameName(hiddenSheetName); 
        // A1:A代表隐藏域创建第?列createCell(?)时。以A1列开始A行数据获取下拉数组
        name.setRefersToFormula(hiddenSheetName + "!B"+(endRow+1)+":B" + (textlist.length + endRow));
        
        DVConstraint constraint = DVConstraint.createFormulaListConstraint(hiddenSheetName); 
        CellRangeAddressList addressList = new CellRangeAddressList(firstRow, endRow, firstCol, firstCol); 
        HSSFDataValidation validation = new HSSFDataValidation(addressList, constraint); 
        // 1隐藏、0显示
        workbook.setSheetHidden(1, true); 
        sheet.addValidationData(validation);
        return sheet;
	}

	/**
	 * 设置单元格上提示
	 * 
	 * @param sheet
	 *            要设置的sheet.
	 * @param promptTitle
	 *            标题
	 * @param promptContent
	 *            内容
	 * @param firstRow
	 *            开始行
	 * @param endRow
	 *            结束行
	 * @param firstCol
	 *            开始列
	 * @param endCol
	 *            结束列
	 * @return 设置好的sheet.
	 */
	public static HSSFSheet setHSSFPrompt(HSSFSheet sheet, String promptTitle, String promptContent, int firstRow,
			int endRow, int firstCol, int endCol) {
		// 构造constraint对象
		DVConstraint constraint = DVConstraint.createCustomFormulaConstraint("BB1");
		// 四个参数分别是：起始行、终止行、起始列、终止列
		CellRangeAddressList regions = new CellRangeAddressList(firstRow, endRow, firstCol, endCol);
		// 数据有效性对象
		// 设置输入信息提示信息

		HSSFDataValidation data_validation_view = new HSSFDataValidation(regions, constraint);
		data_validation_view.createPromptBox("下拉选择提示", "请使用下拉方式选择合适的值！");
		
		// 设置输入错误提示信息
		data_validation_view.createPromptBox(promptTitle, promptContent);
		data_validation_view.createErrorBox("选择错误提示", "你输入的值未在备选列表中，请下拉选择合适的值！");
		sheet.addValidationData(data_validation_view);
		return sheet;
	}

}