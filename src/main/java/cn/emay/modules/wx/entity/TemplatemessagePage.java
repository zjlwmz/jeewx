package cn.emay.modules.wx.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TemplatemessagePage implements Serializable{

	private static final long serialVersionUID = -4243268347856091289L;
	/**保存-模板消息明细*/
	private List<TemplatemessageItem> templatemessageItemList = new ArrayList<TemplatemessageItem>();
	

	/**主键*/
	private String id;
	/**创建人名称*/
	private String createName;
	/**创建日期*/
	private Date createDate;
	/**修改人名称*/
	private String updateName;
	/**修改日期*/
	private Date updateDate;
	/**模板id*/
	private String templateId;
	/**模板名称*/
	private String templateName;
	/**微信帐户id*/
	private String accountid;
	/**详情Url*/
	private String detailUrl;
	/**标题颜色*/
	private String topcolor;
	
	/**
	 *方法: 取得String
	 *@return: String  主键
	 */
	public String getId(){
		return this.id;
	}

	/**
	 *方法: 设置String
	 *@param: String  主键
	 */
	public void setId(String id){
		this.id = id;
	}
	/**
	 *方法: 取得String
	 *@return: String  创建人名称
	 */
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
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  创建日期
	 */
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
	 *方法: 取得String
	 *@return: String  修改人名称
	 */
	public String getUpdateName(){
		return this.updateName;
	}

	/**
	 *方法: 设置String
	 *@param: String  修改人名称
	 */
	public void setUpdateName(String updateName){
		this.updateName = updateName;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  修改日期
	 */
	public java.util.Date getUpdateDate(){
		return this.updateDate;
	}

	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  修改日期
	 */
	public void setUpdateDate(java.util.Date updateDate){
		this.updateDate = updateDate;
	}
	/**
	 *方法: 取得String
	 *@return: String  模板id
	 */
	public String getTemplateId(){
		return this.templateId;
	}

	/**
	 *方法: 设置String
	 *@param: String  模板id
	 */
	public void setTemplateId(String templateId){
		this.templateId = templateId;
	}
	/**
	 *方法: 取得String
	 *@return: String  模板名称
	 */
	public String getTemplateName(){
		return this.templateName;
	}

	/**
	 *方法: 设置String
	 *@param: String  模板名称
	 */
	public void setTemplateName(String templateName){
		this.templateName = templateName;
	}
	/**
	 *方法: 取得String
	 *@return: String  微信帐户id
	 */
	public String getAccountid(){
		return this.accountid;
	}

	/**
	 *方法: 设置String
	 *@param: String  微信帐户id
	 */
	public void setAccountid(String accountid){
		this.accountid = accountid;
	}
	/**
	 *方法: 取得String
	 *@return: String  详情Url
	 */
	public String getDetailUrl(){
		return this.detailUrl;
	}

	/**
	 *方法: 设置String
	 *@param: String  详情Url
	 */
	public void setDetailUrl(String detailUrl){
		this.detailUrl = detailUrl;
	}
	/**
	 *方法: 取得String
	 *@return: String  标题颜色
	 */
	public String getTopcolor(){
		return this.topcolor;
	}

	/**
	 *方法: 设置String
	 *@param: String  标题颜色
	 */
	public void setTopcolor(String topcolor){
		this.topcolor = topcolor;
	}
	
	
	public List<TemplatemessageItem> getTemplatemessageItemList() {
		return templatemessageItemList;
	}
	public void setTemplatemessageItemList(List<TemplatemessageItem> templatemessageItemList) {
		this.templatemessageItemList = templatemessageItemList;
	}
}
