package cn.emay.modules.sys.entity;
// default package

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import cn.emay.framework.core.common.entity.IdEntity;

/**
 * 
 * @Title 通知公告用户授权表
 * @author zjlwm
 * @date 2017-2-20 上午11:52:28
 *
 */
@Entity
@Table(name = "sys_notice_authority_user")
@SuppressWarnings("serial")
public class TSNoticeAuthorityUser extends IdEntity implements java.io.Serializable {

	private String noticeId;// 通告ID
	private User user;//用户
	
	public void setNoticeId(String noticeId) {
		this.noticeId = noticeId;
	}
	
	@Column(name ="notice_id",nullable=true)
	public String getNoticeId() {
		return noticeId;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	@ManyToOne
	@JoinColumn(name="user_id")
	public User getUser() {
		return user;
	}

	}