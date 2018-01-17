package cn.emay.modules.sys.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import cn.emay.framework.core.common.entity.IdEntity;

/**
 * 
 * @Title 多语言
 * @author zjlwm
 * @date 2017-2-20 上午11:49:28
 *
 */
@Entity
@Table(name = "sys_muti_lang")
@DynamicUpdate(true)
@DynamicInsert(true)
public class MutiLang extends IdEntity {

	private static final long serialVersionUID = 1L;

	/** 语言主键 */
	private String langKey;
	/** 内容 */
	private String langContext;
	/** 语言 */
	private String langCode;
	/** 创建时间 */
	private Date createDate;
	/** 创建人编号 */
	private String createBy;
	/** 创建人姓名 */
	private String createName;
	/** 更新日期 */
	private Date updateDate;
	/** 更新人编号 */
	private String updateBy;
	/** 更新人姓名 */
	private String updateName;

	/**
	 * 方法: 取得String
	 * 
	 * @return: String 语言主键
	 */
	@Column(name = "LANG_KEY", nullable = false, length = 50)
	public String getLangKey() {
		return this.langKey;
	}

	/**
	 * 方法: 设置String
	 * 
	 * @param: String 语言主键
	 */
	public void setLangKey(String langKey) {
		this.langKey = langKey;
	}

	/**
	 * 方法: 取得String
	 * 
	 * @return: String 内容
	 */
	@Column(name = "LANG_CONTEXT", nullable = false, length = 500)
	public String getLangContext() {
		return this.langContext;
	}

	/**
	 * 方法: 设置String
	 * 
	 * @param: String 内容
	 */
	public void setLangContext(String langContext) {
		this.langContext = langContext;
	}

	/**
	 * 方法: 取得String
	 * 
	 * @return: String 语言
	 */
	@Column(name = "LANG_CODE", nullable = false, length = 50)
	public String getLangCode() {
		return this.langCode;
	}

	/**
	 * 方法: 设置String
	 * 
	 * @param: String 语言
	 */
	public void setLangCode(String langCode) {
		this.langCode = langCode;
	}

	/**
	 * 方法: 取得Date
	 * 
	 * @return: Date 创建时间
	 */
	@Column(name = "CREATE_DATE", nullable = false)
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
	 * @return: String 创建人编号
	 */
	@Column(name = "CREATE_BY", nullable = false, length = 50)
	public String getCreateBy() {
		return this.createBy;
	}

	/**
	 * 方法: 设置String
	 * 
	 * @param: String 创建人编号
	 */
	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	/**
	 * 方法: 取得String
	 * 
	 * @return: String 创建人姓名
	 */
	@Column(name = "CREATE_NAME", nullable = false, length = 50)
	public String getCreateName() {
		return this.createName;
	}

	/**
	 * 方法: 设置String
	 * 
	 * @param: String 创建人姓名
	 */
	public void setCreateName(String createName) {
		this.createName = createName;
	}

	/**
	 * 方法: 取得Date
	 * 
	 * @return: Date 更新日期
	 */
	@Column(name = "UPDATE_DATE", nullable = false)
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
	 * @return: String 更新人编号
	 */
	@Column(name = "UPDATE_BY", nullable = false, length = 50)
	public String getUpdateBy() {
		return this.updateBy;
	}

	/**
	 * 方法: 设置String
	 * 
	 * @param: String 更新人编号
	 */
	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}

	/**
	 * 方法: 取得String
	 * 
	 * @return: String 更新人姓名
	 */
	@Column(name = "UPDATE_NAME", nullable = false, length = 50)
	public String getUpdateName() {
		return this.updateName;
	}

	/**
	 * 方法: 设置String
	 * 
	 * @param: String 更新人姓名
	 */
	public void setUpdateName(String updateName) {
		this.updateName = updateName;
	}
}
