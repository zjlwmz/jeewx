package cn.emay.modules.sys.entity;
// default package

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import cn.emay.framework.core.common.entity.IdEntity;

/**
 * 
 * @Title 通知公告已读用户表
 * @author zjlwm
 * @date 2017-2-20 上午11:52:37
 *
 */
@Entity
@Table(name = "sys_notice_read_user")
@SuppressWarnings("serial")
public class TSNoticeReadUser extends IdEntity implements java.io.Serializable {

	private String noticeId;// 通告ID
	private String userId;// 用户ID
	private Date createTime;  //创建时间
	
	public void setNoticeId(String noticeId) {
		this.noticeId = noticeId;
	}
	
	@Column(name ="notice_id",nullable=true)
	public String getNoticeId() {
		return noticeId;
	}

	
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	@Column(name ="user_id",nullable=true)
	public String getUserId() {
		return userId;
	}
	@Column(name ="create_time",nullable=true)
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
}