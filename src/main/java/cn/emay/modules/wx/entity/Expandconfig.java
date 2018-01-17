package cn.emay.modules.wx.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import cn.emay.framework.core.common.entity.IdEntity;

/**   
 * @Title: Entity
 * @Description: 扩展接口管理
 * @author onlineGenerator
 * @date 2014-06-04 23:21:57
 * @version V1.0   
 *
 */
@Entity
@Table(name = "wx_expandconfig")
public class Expandconfig extends IdEntity{
	
	private static final long serialVersionUID = 1L;
	
	
	/**关键字*/
	private String keyword;
	/**类长名*/
	private String classname;
	/**微信公众帐号*/
	private String accountid;
	/**功能名称*/
	private String name;
	/**功能描述*/
	private String content;
	
	
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  关键字
	 */
	@Column(name ="KEYWORD",nullable=false,length=100)
	public java.lang.String getKeyword(){
		return this.keyword;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  关键字
	 */
	public void setKeyword(java.lang.String keyword){
		this.keyword = keyword;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  类长名
	 */
	@Column(name ="CLASSNAME",nullable=false,length=100)
	public java.lang.String getClassname(){
		return this.classname;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  类长名
	 */
	public void setClassname(java.lang.String classname){
		this.classname = classname;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  微信公众帐号
	 */
	@Column(name ="ACCOUNTID",nullable=true,length=200)
	public java.lang.String getAccountid(){
		return this.accountid;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  微信公众帐号
	 */
	public void setAccountid(java.lang.String accountid){
		this.accountid = accountid;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  功能名称
	 */
	@Column(name ="NAME",nullable=true,length=100)
	public java.lang.String getName(){
		return this.name;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  功能名称
	 */
	public void setName(java.lang.String name){
		this.name = name;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  功能描述
	 */
	@Column(name ="CONTENT",nullable=true,length=300)
	public java.lang.String getContent(){
		return this.content;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  功能描述
	 */
	public void setContent(java.lang.String content){
		this.content = content;
	}
}
