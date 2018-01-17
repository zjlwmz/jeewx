package cn.emay.modules.sys.entity;

import java.io.Serializable;

import javax.persistence.Column;
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
 * @date 2017-2-20 上午11:50:21
 *
 */
@Entity
@Table(name = "sys_role_function")
public class RoleFunction extends IdEntity implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Function function;
	
	private Role role;
	
	private String operation;
	
	private String dataRule;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "functionid")
	public Function getFunction() {
		return this.function;
	}

	public void setFunction(Function function) {
		this.function = function;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "roleid")
	public Role getRole() {
		return this.role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	@Column(name = "operation", length = 100)
	public String getOperation() {
		return this.operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}
	@Column(name = "datarule", length = 100)
	public String getDataRule() {
		return dataRule;
	}

	public void setDataRule(String dataRule) {
		this.dataRule = dataRule;
	}

}