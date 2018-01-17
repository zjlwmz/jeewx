package cn.emay.framework.common.poi.excel.entity.result;

import java.util.List;
import org.apache.poi.ss.usermodel.Workbook;

public class ExcelImportResult<T> {
	private List<T> list;
	private boolean verfiyFail;
	private Workbook workbook;

	public ExcelImportResult() {
	}

	public ExcelImportResult(List<T> list, boolean verfiyFail, Workbook workbook) {
		this.list = list;
		this.verfiyFail = verfiyFail;
		this.workbook = workbook;
	}

	public List<T> getList() {
		return this.list;
	}

	public Workbook getWorkbook() {
		return this.workbook;
	}

	public boolean isVerfiyFail() {
		return this.verfiyFail;
	}

	public void setList(List<T> list) {
		this.list = list;
	}

	public void setVerfiyFail(boolean verfiyFail) {
		this.verfiyFail = verfiyFail;
	}

	public void setWorkbook(Workbook workbook) {
		this.workbook = workbook;
	}
}