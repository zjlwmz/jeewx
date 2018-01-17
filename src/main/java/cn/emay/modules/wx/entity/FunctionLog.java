package cn.emay.modules.wx.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import cn.emay.framework.core.common.entity.IdEntity;

/**
 * 微信功能访问日志记录
 * 
 * @author zjlWm
 * @version 2015-03-22
 * 
 */
@Entity
@Table(name = "wx_function_log")
public class FunctionLog extends IdEntity{

	private static final long serialVersionUID = 1L;
	
	
	/**
	 * 功能名称
	 */
	private String functionName;

	/**
	 * 事件类型
	 */
	private String eventKey;

	/**
	 * 微信openid
	 */
	private String openid;

	/**
	 * 会员id
	 */
	private Integer memberid;

	/**
	 * 创建时间
	 */
	private Date credateDate;

	
	
	@Column(name = "function_name")
	public String getFunctionName() {
		return functionName;
	}

	public void setFunctionName(String functionName) {
		this.functionName = functionName;
	}

	@Column(name = "event_key")
	public String getEventKey() {
		return eventKey;
	}

	public void setEventKey(String eventKey) {
		this.eventKey = eventKey;
	}

	@Column(name = "openid")
	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	@Column(name = "memberid")
	public Integer getMemberid() {
		return memberid;
	}

	public void setMemberid(Integer memberid) {
		this.memberid = memberid;
	}

	@Column(name = "credate_date")
	public Date getCredateDate() {
		return credateDate;
	}

	public void setCredateDate(Date credateDate) {
		this.credateDate = credateDate;
	}
}
