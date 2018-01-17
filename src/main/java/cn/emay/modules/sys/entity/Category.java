package cn.emay.modules.sys.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import cn.emay.framework.core.common.entity.IdEntity;

/**
 * 
 * @Title 分类管理
 * @author zjlwm
 * @date 2017-2-20 上午11:46:42
 *
 */
@Entity
@Table(name = "sys_category", schema = "")
@DynamicUpdate(true)
@DynamicInsert(true)
public class Category extends IdEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/** 类型名称 */
	private String name;
	
	/** 类型编码 */
	private String code;
	
	/** 分类图标 */
	private Icon icon;
	
	/** 创建人名称 */
	private String createName;
	
	/** 创建人登录名称 */
	private String createBy;
	
	/** 创建日期 */
	private Date createDate;
	
	/** 更新人名称 */
	private String updateName;
	
	/** 更新人登录名称 */
	private String updateBy;
	
	/** 更新日期 */
	private Date updateDate;
	
	/** 组织 */
	private String sysOrgCode;
	
	/** 公司 */
	private String sysCompanyCode;
	
	
	/** 上级 */
	private Category parent;
	
	

	private List<Category> list;


	/**
	 * 方法: 取得String
	 * 
	 * @return: String 创建人名称
	 */
	@Column(name = "create_name", nullable = true, length = 50)
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
	 * 方法: 取得String
	 * 
	 * @return: String 创建人登录名称
	 */
	@Column(name = "create_by", nullable = true, length = 50)
	public String getCreateBy() {
		return this.createBy;
	}

	/**
	 * 方法: 设置String
	 * 
	 * @param: String 创建人登录名称
	 */
	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	/**
	 * 方法: 取得Date
	 * 
	 * @return: Date 创建日期
	 */
	@Column(name = "create_date", nullable = true)
	public Date getCreateDate() {
		return this.createDate;
	}

	/**
	 * 方法: 设置Date
	 * 
	 * @param: Date 创建日期
	 */
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	/**
	 * 方法: 取得String
	 * 
	 * @return: String 更新人名称
	 */
	@Column(name = "update_name", nullable = true, length = 50)
	public String getUpdateName() {
		return this.updateName;
	}

	/**
	 * 方法: 设置String
	 * 
	 * @param: String 更新人名称
	 */
	public void setUpdateName(String updateName) {
		this.updateName = updateName;
	}

	/**
	 * 方法: 取得String
	 * 
	 * @return: String 更新人登录名称
	 */
	@Column(name = "update_by", nullable = true, length = 50)
	public String getUpdateBy() {
		return this.updateBy;
	}

	/**
	 * 方法: 设置String
	 * 
	 * @param: String 更新人登录名称
	 */
	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}

	/**
	 * 方法: 取得Date
	 * 
	 * @return: Date 更新日期
	 */
	@Column(name = "update_date", nullable = true)
	public Date getUpdateDate() {
		return this.updateDate;
	}

	/**
	 * 方法: 设置Date
	 * 
	 * @param: Date 更新日期
	 */
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	/**
	 * 方法: 取得String
	 * 
	 * @return: String 类型名称
	 */
	@Column(name = "name", nullable = true, length = 32)
	public String getName() {
		return this.name;
	}

	/**
	 * 方法: 设置String
	 * 
	 * @param: String 类型名称
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 方法: 取得String
	 * 
	 * @return: String 类型编码
	 */
	@Column(name = "code", nullable = true, length = 32)
	public String getCode() {
		return this.code;
	}

	/**
	 * 方法: 设置String
	 * 
	 * @param: String 类型编码
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * 方法: 取得TSCategoryEntity
	 * 
	 * @return: TSCategoryEntity 上级code
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "parent_code",referencedColumnName = "code")
	public Category getParent() {
		return this.parent;
	}

	/**
	 * 方法: 设置TSCategoryEntity
	 * 
	 * @param: TSCategoryEntity 上级
	 */
	public void setParent(Category parent) {
		this.parent = parent;
	}

	@Column(name = "sys_org_code", nullable = true, length = 15)
	public String getSysOrgCode() {
		return sysOrgCode;
	}

	public void setSysOrgCode(String sysOrgCode) {
		this.sysOrgCode = sysOrgCode;
	}

	@Column(name = "sys_company_code", nullable = true, length = 15)
	public String getSysCompanyCode() {
		return sysCompanyCode;
	}

	public void setSysCompanyCode(String sysCompanyCode) {
		this.sysCompanyCode = sysCompanyCode;
	}

	@OneToMany(cascade = CascadeType.REMOVE, mappedBy = "parent")
	public List<Category> getList() {
		return list;
	}

	public void setList(List<Category> list) {
		this.list = list;
	}

	@ManyToOne()
	@JoinColumn(name = "icon_id")
	public Icon getIcon() {
		return icon;
	}

	public void setIcon(Icon icon) {
		this.icon = icon;
	}
}
