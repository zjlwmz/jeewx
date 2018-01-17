package cn.emay.modules.chat.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import cn.emay.framework.core.common.entity.IdEntity;


/**
 * 好友关系对应表
 * @author lenovo
 *
 */
@Entity
@Table(name = "chat_friend")
public class ChatFriend extends IdEntity{
	
	private static final long serialVersionUID = 1L;

	
	/**
	 * 创建时间
	 */
	private Date createDate;
	
	
	/**
	 * 申请人
	 * 【微信粉丝】
	 */
	private String openid;
	
	
	/**
	 * 被申请人
	 * 【客服id】
	 */
	private String userid;
	
	
	/**
	 *  申请状态
	 */
	private Integer status=0;

	
	/**
	 * 分组id
	 * @return
	 */
	private String groupId;
	

	@Column(name="create_date")
	public Date getCreateDate() {
		return createDate;
	}


	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	@Column(name="status")
	public Integer getStatus() {
		return status;
	}


	public void setStatus(Integer status) {
		this.status = status;
	}

	@Column(name="group_id")
	public String getGroupId() {
		return groupId;
	}


	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}


	public String getOpenid() {
		return openid;
	}


	public void setOpenid(String openid) {
		this.openid = openid;
	}


	public String getUserid() {
		return userid;
	}


	public void setUserid(String userid) {
		this.userid = userid;
	}
	
	
	
	
}
