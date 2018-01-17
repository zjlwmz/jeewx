package cn.emay.modules.wx.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import cn.emay.framework.core.common.entity.IdEntity;


/**
 * 在线客服记录单
 * @author lenovo
 *
 */
@Entity
@Table(name = "wx_online_record")
public class WxOnlineRecord extends IdEntity{

	private static final long serialVersionUID = 1L;
	
	
	private String openid;
	
	/**
	 * 开始时间
	 */
	private Date startDate;
	
	/**
	 * 结束时间
	 */
	private Date endDate;
	
	/**
	 * 状态
	 * 0排队等候;1开始对话;2:对话结束
	 */
	private Integer status;
	
	
	/**
	 * 答复状态
	 * 0等待回复【等待客服人员回复】
	 * 1等待提问【等待顾客回复】
	 * 2提醒顾客回复
	 */
	private Integer replyStatus;
	
	/**
	 * 客服id
	 */
	private String userId;

	
	@Column(name="openid")
	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
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

	
	@Column(name="status")
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	
	@Column(name="user_id")
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	
	@Column(name="reply_status")
	public Integer getReplyStatus() {
		return replyStatus;
	}

	public void setReplyStatus(Integer replyStatus) {
		this.replyStatus = replyStatus;
	}
	
	
	
}
