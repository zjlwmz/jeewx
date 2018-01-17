package cn.emay.modules.sys.entity;

/**
 * 
 * @Title UI库常用控件参
 * @author zjlwm
 * @date 2017-2-20 上午11:48:11
 *
 */
@SuppressWarnings("serial")
public class DuplicateCheckPage   implements java.io.Serializable {

	/**
	 * 表名
	 */
	private String tableName;
	
	/**
	 * 字段名
	 */
	private String fieldName;
	
	/**
	 * 字段值
	 */
	private String fieldVlaue;
	
	/**编辑数据ID*/
	private String rowObid;

	public String getRowObid() {
		return rowObid;
	}

	public void setRowObid(String rowObid) {
		this.rowObid = rowObid;
	}

	public String getTableName() {
		return tableName;
	}

	public String getFieldName() {
		return fieldName;
	}

	public String getFieldVlaue() {
		return fieldVlaue;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public void setFieldVlaue(String fieldVlaue) {
		this.fieldVlaue = fieldVlaue;
	}

}