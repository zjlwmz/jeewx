package cn.emay.modules.sys.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import cn.emay.framework.common.poi.excel.annotation.Excel;
import cn.emay.framework.core.common.entity.IdEntity;
/**
 * 
 * @Title 角色表 
 * @author zjlwm
 * @date 2017-2-20 上午11:50:07
 *
 */
@Entity
@Table(name = "sys_role")
public class Role extends IdEntity implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Excel(exportName = "角色名称")
	private String roleName;//角色名称
	
	@Excel(exportName = "角色编码")
	private String roleCode;//角色编码
	
	/**创建时间*/
	private Date createDate;
	
	/**创建人ID*/
	private String createBy;
	
	/**创建人名称*/
	private String createName;
	
	/**修改时间*/
	private Date updateDate;
	
	/**修改人*/
	private String updateBy;
	
	/**修改人名称*/
	private String updateName;
	
	@Column(name = "rolename", nullable = false, length = 100)
	public String getRoleName() {
		return this.roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	@Column(name = "rolecode", length = 10)
	public String getRoleCode() {
		return this.roleCode;
	}

	public void setRoleCode(String roleCode) {
		this.roleCode = roleCode;
	}
	/**
	 *方法: 取得Date
	 *@return: Date  创建时间
	 */
	@Column(name ="create_date",nullable=true)
	public Date getCreateDate(){
		return this.createDate;
	}

	/**
	 *方法: 设置Date
	 *@param: Date  创建时间
	 */
	public void setCreateDate(Date createDate){
		this.createDate = createDate;
	}
	/**
	 *方法: 取得String
	 *@return: String  创建人ID
	 */
	@Column(name ="create_by",nullable=true,length=32)
	public String getCreateBy(){
		return this.createBy;
	}

	/**
	 *方法: 设置String
	 *@param: String  创建人ID
	 */
	public void setCreateBy(String createBy){
		this.createBy = createBy;
	}
	/**
	 *方法: 取得String
	 *@return: String  创建人名称
	 */
	@Column(name ="create_name",nullable=true,length=32)
	public String getCreateName(){
		return this.createName;
	}

	/**
	 *方法: 设置String
	 *@param: String  创建人名称
	 */
	public void setCreateName(String createName){
		this.createName = createName;
	}
	/**
	 *方法: 取得Date
	 *@return: Date  修改时间
	 */
	@Column(name ="update_date",nullable=true)
	public Date getUpdateDate(){
		return this.updateDate;
	}

	/**
	 *方法: 设置Date
	 *@param: Date  修改时间
	 */
	public void setUpdateDate(Date updateDate){
		this.updateDate = updateDate;
	}
	/**
	 *方法: 取得String
	 *@return: String  修改人ID
	 */
	@Column(name ="update_by",nullable=true,length=32)
	public String getUpdateBy(){
		return this.updateBy;
	}

	/**
	 *方法: 设置String
	 *@param: String  修改人ID
	 */
	public void setUpdateBy(String updateBy){
		this.updateBy = updateBy;
	}
	/**
	 *方法: 取得String
	 *@return: String  修改人名称
	 */
	@Column(name ="update_name",nullable=true,length=32)
	public String getUpdateName(){
		return this.updateName;
	}

	/**
	 *方法: 设置String
	 *@param: String  修改人名称
	 */
	public void setUpdateName(String updateName){
		this.updateName = updateName;
	}
}