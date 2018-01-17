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
 * @Title 权限操作表
 * @author zjlwm
 * @date 2017-2-20 上午11:49:48
 *
 */
@Entity
@Table(name = "sys_operation")
public class Operation extends IdEntity implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String operationname;
	
	private String operationcode;
	
	private String operationicon;
	
	private Short status;
	
	private Icon icon = new Icon();
	
	private Function function = new Function();
	
	private Short operationType;
	
	@Column(name = "operationtype")
	public Short getOperationType() {
		return operationType;
	}

	public void setOperationType(Short operationType) {
		this.operationType = operationType;
	}

	@Column(name = "operationname", length = 50)
	public String getOperationname() {
		return this.operationname;
	}

	public void setOperationname(String operationname) {
		this.operationname = operationname;
	}

	@Column(name = "operationcode", length = 50)
	public String getOperationcode() {
		return this.operationcode;
	}

	public void setOperationcode(String operationcode) {
		this.operationcode = operationcode;
	}

	@Column(name = "operationicon", length = 100)
	public String getOperationicon() {
		return this.operationicon;
	}

	public void setOperationicon(String operationicon) {
		this.operationicon = operationicon;
	}

	@Column(name = "status")
	public Short getStatus() {
		return this.status;
	}

	public void setStatus(Short status) {
		this.status = status;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "iconid")
	public Icon getIcon() {
		return icon;
	}

	public void setIcon(Icon icon) {
		this.icon = icon;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "functionid")
	public Function getFunction() {
		return function;
	}

	public void setFunction(Function function) {
		this.function = function;
	}
	
	@Override
    public boolean equals(Object obj) {  
        if(this == obj)  
            return false;  
        if(obj == null)  
            return false;  
        if(getClass() != obj.getClass() )  
            return false;  
        Operation other = (Operation)obj;  
        if (getId().equals(other.getId())){
        	return true; 
        }else {
        	return false;  
        }
    }  
}