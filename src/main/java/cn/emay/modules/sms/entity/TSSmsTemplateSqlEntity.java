package cn.emay.modules.sms.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import cn.emay.framework.common.poi.excel.annotation.Excel;
import cn.emay.framework.core.common.entity.IdEntity;

/**   
 * @Title: Entity
 * @Description: 消息模板_业务SQL配置表
 * @author onlineGenerator
 * @date 2014-09-17 23:44:17
 * @version V1.0   
 *
 */
@Entity
@Table(name = "t_s_sms_template_sql")
public class TSSmsTemplateSqlEntity extends IdEntity{

	private static final long serialVersionUID = 1L;
	
	
	/**创建人名称*/
	private java.lang.String createName;
	/**创建人登录名称*/
	private java.lang.String createBy;
	/**创建日期*/
	private java.util.Date createDate;
	/**更新人名称*/
	private java.lang.String updateName;
	/**更新人登录名称*/
	private java.lang.String updateBy;
	/**更新日期*/
	private java.util.Date updateDate;
	/**配置CODE*/
	@Excel(exportName="配置CODE")
	private java.lang.String code;
	/**配置名称*/
	@Excel(exportName="配置名称")
	private java.lang.String name;
	/**业务SQLID*/
	@Excel(exportName="业务SQLID")
	private java.lang.String sqlId;
	/**消息模本ID*/
	@Excel(exportName="消息模本ID")
	private java.lang.String templateId;
	
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  创建人名称
	 */
	@Column(name ="CREATE_NAME",nullable=true,length=50)
	public java.lang.String getCreateName(){
		return this.createName;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  创建人名称
	 */
	public void setCreateName(java.lang.String createName){
		this.createName = createName;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  创建人登录名称
	 */
	@Column(name ="CREATE_BY",nullable=true,length=50)
	public java.lang.String getCreateBy(){
		return this.createBy;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  创建人登录名称
	 */
	public void setCreateBy(java.lang.String createBy){
		this.createBy = createBy;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  创建日期
	 */
	@Column(name ="CREATE_DATE",nullable=true,length=20)
	public java.util.Date getCreateDate(){
		return this.createDate;
	}

	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  创建日期
	 */
	public void setCreateDate(java.util.Date createDate){
		this.createDate = createDate;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  更新人名称
	 */
	@Column(name ="UPDATE_NAME",nullable=true,length=50)
	public java.lang.String getUpdateName(){
		return this.updateName;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  更新人名称
	 */
	public void setUpdateName(java.lang.String updateName){
		this.updateName = updateName;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  更新人登录名称
	 */
	@Column(name ="UPDATE_BY",nullable=true,length=50)
	public java.lang.String getUpdateBy(){
		return this.updateBy;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  更新人登录名称
	 */
	public void setUpdateBy(java.lang.String updateBy){
		this.updateBy = updateBy;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  更新日期
	 */
	@Column(name ="UPDATE_DATE",nullable=true,length=20)
	public java.util.Date getUpdateDate(){
		return this.updateDate;
	}

	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  更新日期
	 */
	public void setUpdateDate(java.util.Date updateDate){
		this.updateDate = updateDate;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  配置CODE
	 */
	@Column(name ="CODE",nullable=true,length=32)
	public java.lang.String getCode(){
		return this.code;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  配置CODE
	 */
	public void setCode(java.lang.String code){
		this.code = code;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  配置名称
	 */
	@Column(name ="NAME",nullable=true,length=32)
	public java.lang.String getName(){
		return this.name;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  配置名称
	 */
	public void setName(java.lang.String name){
		this.name = name;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  业务SQLID
	 */
	@Column(name ="SQL_ID",nullable=true,length=32)
	public java.lang.String getSqlId(){
		return this.sqlId;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  业务SQLID
	 */
	public void setSqlId(java.lang.String sqlId){
		this.sqlId = sqlId;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  消息模本ID
	 */
	@Column(name ="TEMPLATE_ID",nullable=true,length=32)
	public java.lang.String getTemplateId(){
		return this.templateId;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  消息模本ID
	 */
	public void setTemplateId(java.lang.String templateId){
		this.templateId = templateId;
	}
}
