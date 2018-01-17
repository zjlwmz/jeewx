package cn.emay.modules.sms.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import cn.emay.framework.common.poi.excel.annotation.Excel;
import cn.emay.framework.core.common.entity.IdEntity;

/**   
 * @Title: Entity
 * @Description: 消息模本表
 * @author onlineGenerator
 * @date 2014-09-17 23:52:46
 * @version V1.0   
 *
 */
@Entity
@Table(name = "t_s_sms_template")
public class TSSmsTemplateEntity extends IdEntity{

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
	/**模板类型*/
	@Excel(exportName="模板类型")
	private java.lang.String templateType;
	/**模板名称*/
	@Excel(exportName="模板名称")
	private java.lang.String templateName;
	/**模板内容*/
	@Excel(exportName="模板内容")
	private java.lang.String templateContent;
	
	
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
	 *@return: java.lang.String  模板类型
	 */
	@Column(name ="TEMPLATE_TYPE",nullable=true,length=1)
	public java.lang.String getTemplateType(){
		return this.templateType;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  模板类型
	 */
	public void setTemplateType(java.lang.String templateType){
		this.templateType = templateType;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  模板名称
	 */
	@Column(name ="TEMPLATE_NAME",nullable=true,length=50)
	public java.lang.String getTemplateName(){
		return this.templateName;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  模板名称
	 */
	public void setTemplateName(java.lang.String templateName){
		this.templateName = templateName;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  模板内容
	 */
	@Column(name ="TEMPLATE_CONTENT",nullable=true,length=1000)
	public java.lang.String getTemplateContent(){
		return this.templateContent;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  模板内容
	 */
	public void setTemplateContent(java.lang.String templateContent){
		this.templateContent = templateContent;
	}
}
