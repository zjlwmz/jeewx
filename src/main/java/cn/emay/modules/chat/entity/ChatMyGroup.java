package cn.emay.modules.chat.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import cn.emay.framework.core.common.entity.IdEntity;

/**
 * 我的聊天好友分组
 * @author lenovo
 *
 */
@Entity
@Table(name = "chat_my_group")
public class ChatMyGroup extends IdEntity{

	private static final long serialVersionUID = 1L;

	/**
	 * 分组名称
	 */
	private String name;
	
	/**
	 * 创建时间
	 */
	private Date createDate;
	
	/**
	 * 创建分组的用户id
	 */
	private String userId;
	
	
	/**
	 * 分组ID
	 */
	private String groupId;
	

	@Column(name="name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name="create_date")
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	@Column(name="user_id")
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	
	@Column(name="group_id")
	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	
	
	
}
