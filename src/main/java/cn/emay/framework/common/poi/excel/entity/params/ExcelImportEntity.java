/**
 * Copyright 2013-2015 JueYue (qrb.jueyue@gmail.com)
 *   
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.emay.framework.common.poi.excel.entity.params;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * excel 导入工具类,对cell类型做映射
 * 
 * @author JueYue
 * @version 1.0 2013年8月24日
 */
public class ExcelImportEntity extends ExcelBaseEntity {

	private int width;

	private int height;

	/**
	 * 图片的类型,1是文件,2是数据库
	 */
	private int exportImageType;
	/**
	 * 排序顺序
	 */
	private int orderNum;
	/**
	 * 是否支持换行
	 */
	private boolean isWrap;
	/**
	 * 是否需要合并
	 */
	private boolean needMerge;

	/**
	 * 导出日期格式
	 */
	private String exportFormat;
	/**
	 * cell 函数
	 */
	private String cellFormula;

	/**
	 * 字典map：dicCode,dicText
	 */
	private Map<String, String> dictMap;

	/**
	 * get 和convert 合并
	 */
	private Method getMethod;

	private List<Method> getMethods;

	/**
	 * 对应 Collection NAME
	 */
	private String collectionName;
	/**
	 * 保存图片的地址
	 */
	private String saveUrl;

	/**
	 * 对应exportType
	 */
	private String classType;
	/**
	 * 后缀
	 */
	private String suffix;
	/**
	 * 导入校验字段
	 */
	private boolean importField;

	private List<ExcelImportEntity> list;

	public String getClassType() {
		return classType;
	}

	public String getCollectionName() {
		return collectionName;
	}

	public List<ExcelImportEntity> getList() {
		return list;
	}

	public String getSaveUrl() {
		return saveUrl;
	}

	public void setClassType(String classType) {
		this.classType = classType;
	}

	public void setCollectionName(String collectionName) {
		this.collectionName = collectionName;
	}

	public void setList(List<ExcelImportEntity> list) {
		this.list = list;
	}

	public void setSaveUrl(String saveUrl) {
		this.saveUrl = saveUrl;
	}

	public String getSuffix() {
		return suffix;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	public boolean isImportField() {
		return importField;
	}

	public void setImportField(boolean importField) {
		this.importField = importField;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getExportImageType() {
		return exportImageType;
	}

	public void setExportImageType(int exportImageType) {
		this.exportImageType = exportImageType;
	}

	public int getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(int orderNum) {
		this.orderNum = orderNum;
	}

	public boolean isWrap() {
		return isWrap;
	}

	public void setWrap(boolean isWrap) {
		this.isWrap = isWrap;
	}

	public boolean isNeedMerge() {
		return needMerge;
	}

	public void setNeedMerge(boolean needMerge) {
		this.needMerge = needMerge;
	}

	public String getExportFormat() {
		return exportFormat;
	}

	public void setExportFormat(String exportFormat) {
		this.exportFormat = exportFormat;
	}

	public String getCellFormula() {
		return cellFormula;
	}

	public void setCellFormula(String cellFormula) {
		this.cellFormula = cellFormula;
	}

	public Map<String, String> getDictMap() {
		return dictMap;
	}

	public void setDictMap(Map<String, String> dictMap) {
		this.dictMap = dictMap;
	}

	public Method getGetMethod() {
		return getMethod;
	}

	public void setGetMethod(Method getMethod) {
		this.getMethod = getMethod;
	}

	public List<Method> getGetMethods() {
		return getMethods;
	}

	public void setGetMethods(List<Method> getMethods) {
		this.getMethods = getMethods;
	}

}
