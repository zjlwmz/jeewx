package cn.emay.modules.sys.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import cn.emay.framework.core.common.entity.IdEntity;

/**
 * 
 * @Title 通用类型字典表
 * @author zjlwm
 * @date 2017-2-20 上午11:52:47
 *
 */
@Entity
@Table(name = "sys_type")
public class Type extends IdEntity implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Typegroup typegroup;//类型分组
	
	private Type type;//父类型
	
	private String typename;//类型名称
	
	private String typecode;//类型编码
	
	private List<Type> types =new ArrayList<Type>();

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "typegroupid")
	public Typegroup getTypegroup() {
		return this.typegroup;
	}

	public void setTypegroup(Typegroup typegroup) {
		this.typegroup = typegroup;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "typepid")
	public Type getType() {
		return this.type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	@Column(name = "typename", length = 50)
	public String getTypename() {
		return this.typename;
	}

	public void setTypename(String typename) {
		this.typename = typename;
	}

	@Column(name = "typecode", length = 50)
	public String getTypecode() {
		return this.typecode;
	}

	public void setTypecode(String typecode) {
		this.typecode = typecode;
	}
	
//	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "TSType")
//	public List<TPProcess> getTSProcesses() {
//		return this.TSProcesses;
//	}
//
//	public void setTSProcesses(List<TPProcess> TSProcesses) {
//		this.TSProcesses = TSProcesses;
//	}


	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "type")
	public List<Type> getTypes() {
		return this.types;
	}

	public void setTypes(List<Type> types) {
		this.types = types;
	}

}