package cn.emay.modules.sys.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import cn.emay.framework.core.common.entity.IdEntity;

/**
 * 
 * @Title 数据字典类型
 * @author zjlwm
 * @date 2017-2-20 上午11:52:57
 *
 */
@Entity
@Table(name = "sys_typegroup")
public class Typegroup extends IdEntity implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String typegroupname;
	
	private String typegroupcode;
	
	private List<Type> types = new ArrayList<Type>();
	
	@Column(name = "typegroupname", length = 50)
	public String getTypegroupname() {
		return this.typegroupname;
	}

	public void setTypegroupname(String typegroupname) {
		this.typegroupname = typegroupname;
	}

	@Column(name = "typegroupcode", length = 50)
	public String getTypegroupcode() {
		return this.typegroupcode;
	}

	public void setTypegroupcode(String typegroupcode) {
		this.typegroupcode = typegroupcode;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "typegroup")
	public List<Type> getTypes() {
		return this.types;
	}

	public void setTypes(List<Type> types) {
		this.types = types;
	}

}