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
 * @Title 通知公告角色授权表
 * @author zjlwm
 * @date 2017-2-20 上午11:52:19
 *
 */

@Entity
@Table(name = "sys_notice_authority_role")
@SuppressWarnings("serial")
public class TSNoticeAuthorityRole extends IdEntity implements java.io.Serializable {

	private String noticeId;// 通告ID
	private Role role;// 
	
	public void setNoticeId(String noticeId) {
		this.noticeId = noticeId;
	}
	
	@Column(name ="notice_id",nullable=true)
	public String getNoticeId() {
		return noticeId;
	}

	public void setRole(Role role) {
		this.role = role;
	}
	
	@ManyToOne
	@JoinColumn(name="role_id")
	public Role getRole() {
		return role;
	}


	}