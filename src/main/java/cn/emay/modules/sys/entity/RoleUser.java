package cn.emay.modules.sys.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import cn.emay.framework.core.common.entity.IdEntity;

/**
 * 
 * @Title 
 * @author zjlwm
 * @date 2017-2-20 上午11:50:51
 *
 */
@Entity
@Table(name = "sys_role_user")
public class RoleUser extends IdEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	private User user;
	
	private Role role;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "userid")
	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "roleid")
	public Role getRole() {
		return this.role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

}