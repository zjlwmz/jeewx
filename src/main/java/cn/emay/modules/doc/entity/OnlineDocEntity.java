package cn.emay.modules.doc.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import cn.emay.framework.common.poi.excel.annotation.Excel;
import cn.emay.framework.core.common.entity.IdEntity;

/**
 * @Title: Entity
 * @Description: 在线文档
 * @author onlineGenerator
 * @date 2016-03-19 15:49:59
 * @version V1.0
 * 
 */
@Entity
@Table(name = "sys_online_doc")
public class OnlineDocEntity extends IdEntity {

	private static final long serialVersionUID = 1L;

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
	
	/** 所属部门 */
	private String sysOrgCode;
	
	/** 所属公司 */
	private String sysCompanyCode;
	
	/** 流程状态 */
	private String bpmStatus;
	
	/** 文件原名 */
	private String oldName;
	
	/** 文件名 */
	private String newName;
	
	/** 描述 */
	@Excel(exportName = "描述")
	private String description;
	
	/** 分类节点 */
	@Excel(exportName = "分类节点")
	private String treeNode;
	
	/** 下载地址 */
	private String path;

	
	
	/**
	 * 方法: 取得String
	 * 
	 * @return: String 创建人名称
	 */
	@Column(name = "CREATE_NAME", nullable = true, length = 50)
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
	@Column(name = "CREATE_BY", nullable = true, length = 50)
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
	@Column(name = "CREATE_DATE", nullable = true, length = 20)
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
	@Column(name = "UPDATE_NAME", nullable = true, length = 50)
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
	@Column(name = "UPDATE_BY", nullable = true, length = 50)
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
	@Column(name = "UPDATE_DATE", nullable = true, length = 20)
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
	 * @return: String 所属部门
	 */
	@Column(name = "SYS_ORG_CODE", nullable = true, length = 50)
	public String getSysOrgCode() {
		return this.sysOrgCode;
	}

	/**
	 * 方法: 设置String
	 * 
	 * @param: String 所属部门
	 */
	public void setSysOrgCode(String sysOrgCode) {
		this.sysOrgCode = sysOrgCode;
	}

	/**
	 * 方法: 取得String
	 * 
	 * @return: String 所属公司
	 */
	@Column(name = "SYS_COMPANY_CODE", nullable = true, length = 50)
	public String getSysCompanyCode() {
		return this.sysCompanyCode;
	}

	/**
	 * 方法: 设置String
	 * 
	 * @param: String 所属公司
	 */
	public void setSysCompanyCode(String sysCompanyCode) {
		this.sysCompanyCode = sysCompanyCode;
	}

	/**
	 * 方法: 取得String
	 * 
	 * @return: String 流程状态
	 */
	@Column(name = "BPM_STATUS", nullable = true, length = 32)
	public String getBpmStatus() {
		return this.bpmStatus;
	}

	/**
	 * 方法: 设置String
	 * 
	 * @param: String 流程状态
	 */
	public void setBpmStatus(String bpmStatus) {
		this.bpmStatus = bpmStatus;
	}

	/**
	 * 方法: 取得String
	 * 
	 * @return: String 文件原名
	 */
	@Column(name = "OLD_NAME", nullable = true, length = 50)
	public String getOldName() {
		return this.oldName;
	}

	/**
	 * 方法: 设置String
	 * 
	 * @param: String 文件原名
	 */
	public void setOldName(String oldName) {
		this.oldName = oldName;
	}

	/**
	 * 方法: 取得String
	 * 
	 * @return: String 文件名
	 */
	@Column(name = "NEW_NAME", nullable = true, length = 50)
	public String getNewName() {
		return this.newName;
	}

	/**
	 * 方法: 设置String
	 * 
	 * @param: String 文件名
	 */
	public void setNewName(String newName) {
		this.newName = newName;
	}

	/**
	 * 方法: 取得String
	 * 
	 * @return: String 描述
	 */
	@Column(name = "DESCRIPTION", nullable = true, length = 200)
	public String getDescription() {
		return this.description;
	}

	/**
	 * 方法: 设置String
	 * 
	 * @param: String 描述
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * 方法: 取得String
	 * 
	 * @return: String 下载地址
	 */
	@Column(name = "PATH", nullable = true, length = 200)
	public String getPath() {
		return this.path;
	}

	/**
	 * 方法: 设置String
	 * 
	 * @param: String 下载地址
	 */
	public void setPath(String path) {
		this.path = path;
	}

	public void setTreeNode(String treeNode) {
		this.treeNode = treeNode;
	}

	@Column(name = "TREE_NODE", nullable = true, length = 200)
	public String getTreeNode() {
		return treeNode;
	}
}
