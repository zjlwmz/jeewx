package cn.emay.modules.sys.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import cn.emay.framework.common.poi.excel.annotation.Excel;
import cn.emay.framework.core.common.entity.IdEntity;


/**
 * 
 * @Title 数据日志
 * @author zjlwm
 * @date 2017-2-20 上午11:47:19
 *
 */
@Entity
@Table(name = "sys_data_log", schema = "")
@SuppressWarnings("serial")
public class DatalogEntity extends IdEntity implements Serializable {
	
	/**创建人名称*/
	private String createName;
	
	/**创建人登录名称*/
	private String createBy;
	
	/**创建日期*/
	private Date createDate;
	
	/**更新人名称*/
	private String updateName;
	
	/**更新人登录名称*/
	private String updateBy;
	
	/**更新日期*/
	private Date updateDate;
	
	/**所属部门*/
	private String sysOrgCode;
	
	/**所属公司*/
	private String sysCompanyCode;
	
	/**表名*/
	@Excel(exportName="表名")
	private String tableName;
	
	/**数据ID*/
	@Excel(exportName="数据ID")
	private String dataId;
	
	/**数据内容*/
	@Excel(exportName="数据内容")
	private String dataContent;
	
	/**版本号*/
	@Excel(exportName="版本号")
	private Integer versionNumber;

	/**
	 *方法: 取得String
	 *@return: String  创建人名称
	 */
	@Column(name ="CREATE_NAME",nullable=true,length=50)
	public String getCreateName(){
		return this.createName;
	}

	/**
	 *方法: 设置String
	 *@param: String  创建人名称
	 */
	public void setCreateName(String createName){
		this.createName = createName;
	}
	/**
	 *方法: 取得String
	 *@return: String  创建人登录名称
	 */
	@Column(name ="CREATE_BY",nullable=true,length=50)
	public String getCreateBy(){
		return this.createBy;
	}

	/**
	 *方法: 设置String
	 *@param: String  创建人登录名称
	 */
	public void setCreateBy(String createBy){
		this.createBy = createBy;
	}
	/**
	 *方法: 取得Date
	 *@return: Date  创建日期
	 */
	@Column(name ="CREATE_DATE",nullable=true,length=20)
	public Date getCreateDate(){
		return this.createDate;
	}

	/**
	 *方法: 设置Date
	 *@param: Date  创建日期
	 */
	public void setCreateDate(Date createDate){
		this.createDate = createDate;
	}
	/**
	 *方法: 取得String
	 *@return: String  更新人名称
	 */
	@Column(name ="UPDATE_NAME",nullable=true,length=50)
	public String getUpdateName(){
		return this.updateName;
	}

	/**
	 *方法: 设置String
	 *@param: String  更新人名称
	 */
	public void setUpdateName(String updateName){
		this.updateName = updateName;
	}
	/**
	 *方法: 取得String
	 *@return: String  更新人登录名称
	 */
	@Column(name ="UPDATE_BY",nullable=true,length=50)
	public String getUpdateBy(){
		return this.updateBy;
	}

	/**
	 *方法: 设置String
	 *@param: String  更新人登录名称
	 */
	public void setUpdateBy(String updateBy){
		this.updateBy = updateBy;
	}
	/**
	 *方法: 取得Date
	 *@return: Date  更新日期
	 */
	@Column(name ="UPDATE_DATE",nullable=true,length=20)
	public Date getUpdateDate(){
		return this.updateDate;
	}

	/**
	 *方法: 设置Date
	 *@param: Date  更新日期
	 */
	public void setUpdateDate(Date updateDate){
		this.updateDate = updateDate;
	}
	/**
	 *方法: 取得String
	 *@return: String  所属部门
	 */
	@Column(name ="SYS_ORG_CODE",nullable=true,length=50)
	public String getSysOrgCode(){
		return this.sysOrgCode;
	}

	/**
	 *方法: 设置String
	 *@param: String  所属部门
	 */
	public void setSysOrgCode(String sysOrgCode){
		this.sysOrgCode = sysOrgCode;
	}
	/**
	 *方法: 取得String
	 *@return: String  所属公司
	 */
	@Column(name ="SYS_COMPANY_CODE",nullable=true,length=50)
	public String getSysCompanyCode(){
		return this.sysCompanyCode;
	}

	/**
	 *方法: 设置String
	 *@param: String  所属公司
	 */
	public void setSysCompanyCode(String sysCompanyCode){
		this.sysCompanyCode = sysCompanyCode;
	}
	/**
	 *方法: 取得String
	 *@return: String  表名
	 */
	@Column(name ="TABLE_NAME",nullable=true,length=32)
	public String getTableName(){
		return this.tableName;
	}

	/**
	 *方法: 设置String
	 *@param: String  表名
	 */
	public void setTableName(String tableName){
		this.tableName = tableName;
	}
	/**
	 *方法: 取得String
	 *@return: String  数据ID
	 */
	@Column(name ="DATA_ID",nullable=true,length=32)
	public String getDataId(){
		return this.dataId;
	}

	/**
	 *方法: 设置String
	 *@param: String  数据ID
	 */
	public void setDataId(String dataId){
		this.dataId = dataId;
	}
	/**
	 *方法: 取得String
	 *@return: String  数据内容
	 */
	@Column(name ="DATA_CONTENT",nullable=true,length=32)
	public String getDataContent(){
		return this.dataContent;
	}

	/**
	 *方法: 设置String
	 *@param: String  数据内容
	 */
	public void setDataContent(String dataContent){
		this.dataContent = dataContent;
	}
	/**
	 *方法: 取得Integer
	 *@return: Integer  版本号
	 */
	@Column(name ="VERSION_NUMBER",nullable=true,length=4)
	public Integer getVersionNumber(){
		return this.versionNumber;
	}

	/**
	 *方法: 设置Integer
	 *@param: Integer  版本号
	 */
	public void setVersionNumber(Integer versionNumber){
		this.versionNumber = versionNumber;
	}
}
