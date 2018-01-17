package cn.emay.modules.sys.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import cn.emay.framework.common.utils.oConvertUtils;
import cn.emay.framework.core.common.entity.IdEntity;

/**
 * 
 * @Title 菜单权限表
 * @author zjlwm
 * @date 2017-2-20 上午11:48:34
 *
 */
@Entity
@Table(name = "sys_function")
@org.hibernate.annotations.Proxy(lazy = false)
public class Function extends IdEntity implements Serializable, Comparable<Function> {
	private static final long serialVersionUID = 1L;

	private Function parentFunction;// 父菜单

	private String functionName;// 菜单名称

	private Short functionLevel;// 菜单等级

	private String functionUrl;// 菜单地址

	private Short functionIframe;// 菜单地址打开方式

	private String functionOrder;// 菜单排序

	private Short functionType;// 菜单类型

	private Icon icon = new Icon();// 菜单图标

	private Icon iconDesk;// 云桌面菜单图标

	private List<Function> functions = new ArrayList<Function>();

	/** 创建时间 */
	private Date createDate;

	/** 创建人ID */
	private String createBy;

	/** 创建人名称 */
	private String createName;

	/** 修改时间 */
	private Date updateDate;

	/** 修改人 */
	private String updateBy;

	/** 修改人名称 */
	private String updateName;

	/**
	 * 方法: 取得Date
	 * 
	 * @return: Date 创建时间
	 */
	@Column(name = "create_date", nullable = true)
	public Date getCreateDate() {
		return this.createDate;
	}

	/**
	 * 方法: 设置Date
	 * 
	 * @param: Date 创建时间
	 */
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	/**
	 * 方法: 取得String
	 * 
	 * @return: String 创建人ID
	 */
	@Column(name = "create_by", nullable = true, length = 32)
	public String getCreateBy() {
		return this.createBy;
	}

	/**
	 * 方法: 设置String
	 * 
	 * @param: String 创建人ID
	 */
	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	/**
	 * 方法: 取得String
	 * 
	 * @return: String 创建人名称
	 */
	@Column(name = "create_name", nullable = true, length = 32)
	public String getCreateName() {
		return this.createName;
	}

	/**
	 * 方法: 设置String
	 * 
	 * @param: String 创建人名称
	 */
	public void setCreateName(String createName) {
		this.createName = createName;
	}

	/**
	 * 方法: 取得Date
	 * 
	 * @return: Date 修改时间
	 */
	@Column(name = "update_date", nullable = true)
	public Date getUpdateDate() {
		return this.updateDate;
	}

	/**
	 * 方法: 设置Date
	 * 
	 * @param: Date 修改时间
	 */
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	/**
	 * 方法: 取得String
	 * 
	 * @return: String 修改人ID
	 */
	@Column(name = "update_by", nullable = true, length = 32)
	public String getUpdateBy() {
		return this.updateBy;
	}

	/**
	 * 方法: 设置String
	 * 
	 * @param: String 修改人ID
	 */
	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}

	/**
	 * 方法: 取得String
	 * 
	 * @return: String 修改人名称
	 */
	@Column(name = "update_name", nullable = true, length = 32)
	public String getUpdateName() {
		return this.updateName;
	}

	/**
	 * 方法: 设置String
	 * 
	 * @param: String 修改人名称
	 */
	public void setUpdateName(String updateName) {
		this.updateName = updateName;
	}

	public boolean hasSubFunction(Map<Integer, List<Function>> map) {
		if (map.containsKey(this.getFunctionLevel() + 1)) {
			return hasSubFunction(map.get(this.getFunctionLevel() + 1));
		}
		return false;
	}

	public boolean hasSubFunction(List<Function> functions) {
		for (Function f : functions) {
			if (f.getParentFunction().getId().equals(this.getId())) {
				return true;
			}
		}
		return false;
	}

	/*
	 * public void setSubFunctionSize(int subFunctionSize) {
	 * this.subFunctionSize = subFunctionSize; }
	 */

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "desk_iconid")
	public Icon getIconDesk() {
		return iconDesk;
	}

	public void setIconDesk(Icon iconDesk) {
		this.iconDesk = iconDesk;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "iconid")
	public Icon getIcon() {
		return icon;
	}

	public void setIcon(Icon icon) {
		this.icon = icon;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "parentfunctionid")
	public Function getParentFunction() {
		return this.parentFunction;
	}

	public void setParentFunction(Function parentFunction) {
		this.parentFunction = parentFunction;
	}

	@Column(name = "functionname", nullable = false, length = 50)
	public String getFunctionName() {
		return this.functionName;
	}

	public void setFunctionName(String functionName) {
		this.functionName = functionName;
	}

	@Column(name = "functionlevel")
	public Short getFunctionLevel() {
		return this.functionLevel;
	}

	public void setFunctionLevel(Short functionLevel) {
		this.functionLevel = functionLevel;
	}

	@Column(name = "functiontype")
	public Short getFunctionType() {
		return this.functionType;
	}

	public void setFunctionType(Short functionType) {
		this.functionType = functionType;
	}

	@Column(name = "functionurl", length = 100)
	public String getFunctionUrl() {
		return this.functionUrl;
	}

	public void setFunctionUrl(String functionUrl) {
		this.functionUrl = functionUrl;
	}

	@Column(name = "functionorder")
	public String getFunctionOrder() {
		return functionOrder;
	}

	public void setFunctionOrder(String functionOrder) {
		this.functionOrder = functionOrder;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "parentFunction")
	public List<Function> getFunctions() {
		return functions;
	}

	public void setFunctions(List<Function> functions) {
		this.functions = functions;
	}

	@Column(name = "functioniframe")
	public Short getFunctionIframe() {
		return functionIframe;
	}

	public void setFunctionIframe(Short functionIframe) {
		this.functionIframe = functionIframe;
	}

	/**
	 * 菜单排序比较器
	 */
	public int compareTo(Function c1) {
		if (c1.getFunctionOrder() != null && this.getFunctionOrder() != null) {
			int c1order = oConvertUtils.getInt(c1.getFunctionOrder().substring(c1.getFunctionOrder().indexOf("fun") + 3));
			int c2order = oConvertUtils.getInt(this.getFunctionOrder().substring(this.getFunctionOrder().indexOf("fun")) + 3);
			if (c1order > c2order) {
				return 1;
			} else {
				return -1;
			}
		} else {
			return 1;
		}
	}

}