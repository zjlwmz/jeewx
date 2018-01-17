package cn.emay.modules.wx.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import cn.emay.framework.core.common.entity.IdEntity;


/**
 * 推送消息
 * @author zjlWm
 * @version 2015-4-22
 */
@Entity
@Table(name = "wx_push_message")
public class PushMessage extends IdEntity{

	private static final long serialVersionUID = 1L;
	
	
	private String type;
	
	private String text;
	
	private Date createDate;
	
	private Date startDate;
	
	private Date endDate;

	private String status="0";	//0未发送、1已发送
	
	

	@Column(name="type")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Column(name="text")
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	@Column(name="create_date")
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	@Column(name="start_date")
	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	
	@Column(name="end_date")
	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
}
