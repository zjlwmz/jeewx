package cn.emay.framework.common.poi.excel.entity;

import java.util.Comparator;

import cn.emay.framework.common.poi.excel.entity.params.ExcelExportEntity;

/**
 * 按照升序排序
 * 
 * @author jueyue
 * 
 */
public class ComparatorExcelField implements Comparator<ExcelExportEntity> {

	public int compare(ExcelExportEntity prev, ExcelExportEntity next) {
		return prev.getOrderNum() - next.getOrderNum();
	}

}
