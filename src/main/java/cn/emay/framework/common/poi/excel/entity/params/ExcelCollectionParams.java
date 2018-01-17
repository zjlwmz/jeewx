package cn.emay.framework.common.poi.excel.entity.params;

import java.util.Map;

/**
 * Excel 对于的 Collection
 * 
 * @author JueYue
 * @date 2013-9-26
 * @version 1.0
 */
public class ExcelCollectionParams {
	/**
	 * 集合对应的名称
	 */
	private String excelName;
	
	/**
	 * 实体对象
	 */
	private Class<?> type;
	
	/**
	 * 这个list下面的参数集合实体对象
	 */
	private Map<String, ExcelImportEntity> excelParams;

	public Map<String, ExcelImportEntity> getExcelParams() {
		return this.excelParams;
	}


	public Class<?> getType() {
		return this.type;
	}

	public void setExcelParams(Map<String, ExcelImportEntity> excelParams) {
		this.excelParams = excelParams;
	}


	public void setType(Class<?> type) {
		this.type = type;
	}

	public String getExcelName() {
		return this.excelName;
	}

	public void setExcelName(String excelName) {
		this.excelName = excelName;
	}
}