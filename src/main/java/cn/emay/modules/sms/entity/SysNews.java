package cn.emay.modules.sms.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import cn.emay.framework.common.poi.excel.annotation.Excel;
import cn.emay.framework.core.common.entity.IdEntity;

/**
 * @Title: Entity
 * @Description: 消息发送记录表
 * @author onlineGenerator
 * @date 2014-09-18 00:01:53
 * @version V1.0
 * 
 */
@Entity
@Table(name = "sys_news")
public class SysNews extends IdEntity {

	private static final long serialVersionUID = 1L;

	/** 创建人名称 */
	private String createName;
	
	/** 创建人登录名称 */
	private String createBy;
	
	/** 创建日期 */
	private Date createDate;
	
	/** 更新人名称 */
	private String updateName;
	
	/** 更新人登录名称 */
	private String updateBy;
	
	/** 更新日期 */
	private Date updateDate;
	
	/** 消息标题 */
	@Excel(exportName = "消息标题")
	private String esTitle;
	
	/** 消息类型 */
	@Excel(exportName = "消息类型")
	private String esType;
	
	/** 发送人 */
	@Excel(exportName = "发送人")
	private String esSender;
	
	/** 接收人 */
	@Excel(exportName = "接收人")
	private String esReceiver;
	
	/** 内容 */
	@Excel(exportName = "内容")
	private String esContent;
	
	/** 发送时间 */
	@Excel(exportName = "发送时间")
	private Date esSendtime;
	
	/** 发送状态 */
	@Excel(exportName = "发送状态")
	private String esStatus;
	
	/** 备注 */
	@Excel(exportName = "备注")
	private String remark;

	
	/**
	 * 方法: 取得String
	 * @return: String 创建人名称
	 */
	@Column(name = "create_name", nullable = true, length = 50)
	public String getCreateName() {
		return this.createName;
	}

	/**
	 * 方法: 设置String
	 * 
	 * @param: String 创建人名称
	 */
	public void setCreateName(String createName) {
		this.createName = createName;
	}

	/**
	 * 方法: 取得String
	 * 
	 * @return: String 创建人登录名称
	 */
	@Column(name = "create_by", nullable = true, length = 50)
	public String getCreateBy() {
		return this.createBy;
	}

	/**
	 * 方法: 设置String
	 * 
	 * @param: String 创建人登录名称
	 */
	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	/**
	 * 方法: 取得Date
	 * 
	 * @return: Date 创建日期
	 */
	@Column(name = "create_date", nullable = true, length = 20)
	public Date getCreateDate() {
		return this.createDate;
	}

	/**
	 * 方法: 设置Date
	 * 
	 * @param: Date 创建日期
	 */
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	/**
	 * 方法: 取得String
	 * 
	 * @return: String 更新人名称
	 */
	@Column(name = "update_name", nullable = true, length = 50)
	public String getUpdateName() {
		return this.updateName;
	}

	/**
	 * 方法: 设置String
	 * 
	 * @param: String 更新人名称
	 */
	public void setUpdateName(String updateName) {
		this.updateName = updateName;
	}

	/**
	 * 方法: 取得String
	 * 
	 * @return: String 更新人登录名称
	 */
	@Column(name = "update_by", nullable = true, length = 50)
	public String getUpdateBy() {
		return this.updateBy;
	}

	/**
	 * 方法: 设置String
	 * 
	 * @param: String 更新人登录名称
	 */
	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}

	/**
	 * 方法: 取得Date
	 * 
	 * @return: Date 更新日期
	 */
	@Column(name = "update_date", nullable = true, length = 20)
	public Date getUpdateDate() {
		return this.updateDate;
	}

	/**
	 * 方法: 设置Date
	 * 
	 * @param: Date 更新日期
	 */
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	/**
	 * 方法: 取得String
	 * 
	 * @return: String 消息标题
	 */
	@Column(name = "es_title", nullable = true, length = 32)
	public String getEsTitle() {
		return this.esTitle;
	}

	/**
	 * 方法: 设置String
	 * 
	 * @param: String 消息标题
	 */
	public void setEsTitle(String esTitle) {
		this.esTitle = esTitle;
	}

	/**
	 * 方法: 取得String
	 * 
	 * @return: String 消息类型
	 */
	@Column(name = "es_type", nullable = true, length = 1)
	public String getEsType() {
		return this.esType;
	}

	/**
	 * 方法: 设置String
	 * 
	 * @param: String 消息类型
	 */
	public void setEsType(String esType) {
		this.esType = esType;
	}

	/**
	 * 方法: 取得String
	 * 
	 * @return: String 发送人
	 */
	@Column(name = "es_sender", nullable = true, length = 50)
	public String getEsSender() {
		return this.esSender;
	}

	/**
	 * 方法: 设置String
	 * 
	 * @param: String 发送人
	 */
	public void setEsSender(String esSender) {
		this.esSender = esSender;
	}

	/**
	 * 方法: 取得String
	 * 
	 * @return: String 接收人
	 */
	@Column(name = "es_receiver", nullable = true, length = 50)
	public String getEsReceiver() {
		return this.esReceiver;
	}

	/**
	 * 方法: 设置String
	 * 
	 * @param: String 接收人
	 */
	public void setEsReceiver(String esReceiver) {
		this.esReceiver = esReceiver;
	}

	/**
	 * 方法: 取得String
	 * 
	 * @return: String 内容
	 */
	@Column(name = "es_content", nullable = true, length = 1000)
	public String getEsContent() {
		return this.esContent;
	}

	/**
	 * 方法: 设置String
	 * 
	 * @param: String 内容
	 */
	public void setEsContent(String esContent) {
		this.esContent = esContent;
	}

	/**
	 * 方法: 取得Date
	 * 
	 * @return: Date 发送时间
	 */
	@Column(name = "es_sendtime", nullable = true, length = 32)
	public Date getEsSendtime() {
		return this.esSendtime;
	}

	/**
	 * 方法: 设置Date
	 * 
	 * @param: Date 发送时间
	 */
	public void setEsSendtime(Date esSendtime) {
		this.esSendtime = esSendtime;
	}

	/**
	 * 方法: 取得String
	 * 
	 * @return: String 发送状态
	 */
	@Column(name = "es_status", nullable = true, length = 1)
	public String getEsStatus() {
		return this.esStatus;
	}

	/**
	 * 方法: 设置String
	 * 
	 * @param: String 发送状态
	 */
	public void setEsStatus(String esStatus) {
		this.esStatus = esStatus;
	}

	/**
	 * 方法: 取得String
	 * 
	 * @return: String 备注
	 */
	@Column(name = "remark", nullable = true, length = 1)
	public String getRemark() {
		return this.remark;
	}

	/**
	 * 方法: 设置String
	 * 
	 * @param: String 备注
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}
}
