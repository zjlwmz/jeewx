package cn.emay.modules.chat.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import cn.emay.framework.core.common.entity.IdEntity;


/**
 * 聊天群组
 * @author lenovo
 *
 */
@Entity
@Table(name = "chat_group")
public class ChatGroup extends IdEntity{

	private static final long serialVersionUID = 1L;
	
	/**
	 * 群组名称
	 */
	private String name;
	
	/**
	 * 群组头像
	 */
	private String avatar;
	
	/**
	 * 创建时间
	 */
	private Date createDate;

	
	
	
	
	
	
	@Column(name="name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	
	@Column(name="avatar")
	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	@Column(name="create_date")
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}


}
