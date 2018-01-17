package cn.emay.framework.common.poi.excel.entity.params;

import java.io.Serializable;
import org.apache.poi.ss.usermodel.CellStyle;

public class ExcelTemplateParams implements Serializable {
	private static final long serialVersionUID = 1L;
	private String name;
	private CellStyle cellStyle;
	private short height;

	public ExcelTemplateParams() {
	}

	public ExcelTemplateParams(String name, CellStyle cellStyle, short height) {
		this.name = name;
		this.cellStyle = cellStyle;
		this.height = height;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public CellStyle getCellStyle() {
		return this.cellStyle;
	}

	public void setCellStyle(CellStyle cellStyle) {
		this.cellStyle = cellStyle;
	}

	public short getHeight() {
		return this.height;
	}

	public void setHeight(short height) {
		this.height = height;
	}
}