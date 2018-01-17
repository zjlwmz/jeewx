package cn.emay.modules.sys.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Transient;

import cn.emay.framework.common.poi.excel.annotation.Excel;
import cn.emay.framework.core.common.entity.IdEntity;

/**
 * 
 * @Title 系统用户表
 * @author zjlwm
 * @date 2017-2-20 上午11:53:09
 *
 */
@Entity
@Table(name = "sys_user")
@PrimaryKeyJoinColumn(name = "id")
public class User extends IdEntity implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Excel(exportName = "用户名")
	private String userName;// 用户名
	
	@Excel(exportName = "真实姓名")
	private String realName;// 真实姓名
	
	private String browser;// 用户使用浏览器类型
	
	@Excel(exportName = "角色编码(多个角色编码用逗号分隔，非必填)")
	private String userKey;// 用户验证唯一标示
	
	private String password;// 用户密码
	
	private Short activitiSync;// 是否同步工作流引擎
	
	/* @Excel(name = "状态") */
	private Short status;// 状态1：在线,2：离线,0：禁用

	private Short deleteFlag;// 状态: 0:不删除 1：删除

	private byte[] signature;// 签名文件

	@Excel(exportName = "组织机构编码(多个组织机构编码用逗号分隔，非必填)")
	private String departid;
	
	private List<UserOrg> userOrgList = new ArrayList<UserOrg>();
	
	private Depart currentDepart = new Depart();// 当前部门
	
	/**
	 * 头像
	 */
	private String avatar;
	
	/**
	 * 我的签名
	 */
	private String mySignature;
	
	
	
	
	private String signatureFile;// 签名文件
	
	@Excel(exportName = "手机")
	private String mobilePhone;// 手机
	
	@Excel(exportName = "办公电话")
	private String officePhone;// 办公电话
	
	@Excel(exportName = "邮箱")
	private String email;// 邮箱
	
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
	
	
	@Column(name = "signatureFile", length = 100)
	public String getSignatureFile() {
		return this.signatureFile;
	}

	public void setSignatureFile(String signatureFile) {
		this.signatureFile = signatureFile;
	}

	@Column(name = "mobilePhone", length = 30)
	public String getMobilePhone() {
		return this.mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	@Column(name = "officePhone", length = 20)
	public String getOfficePhone() {
		return this.officePhone;
	}

	public void setOfficePhone(String officePhone) {
		this.officePhone = officePhone;
	}

	@Column(name = "email", length = 50)
	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
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
	
	
	
	
	public void setDepartid(String departid) {
		this.departid = departid;
	}

	@Column(name = "departid", length = 32)
	public String getDepartid() {
		return departid;
	}

	

	@Column(name = "signature", length = 3000)
	public byte[] getSignature() {
		return signature;
	}

	public void setSignature(byte[] signature) {
		this.signature = signature;
	}

	@Column(name = "browser", length = 20)
	public String getBrowser() {
		return browser;
	}

	public void setBrowser(String browser) {
		this.browser = browser;
	}

	@Column(name = "userkey", length = 200)
	public String getUserKey() {
		return userKey;
	}

	public void setUserKey(String userKey) {
		this.userKey = userKey;
	}

	@Column(name = "status")
	public Short getStatus() {
		return this.status;
	}

	public void setStatus(Short status) {
		this.status = status;
	}

	public Short getActivitiSync() {
		return activitiSync;
	}

	@Column(name = "activitisync")
	public void setActivitiSync(Short activitiSync) {
		this.activitiSync = activitiSync;
	}

	@Column(name = "password", length = 100)
	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	@Column(name = "username", nullable = false, length = 10)
	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Column(name = "realname", length = 50)
	public String getRealName() {
		return this.realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	@Transient
	public Depart getCurrentDepart() {
		return currentDepart;
	}

	public void setCurrentDepart(Depart currentDepart) {
		this.currentDepart = currentDepart;
	}

	
	@OneToMany(mappedBy = "user")
	public List<UserOrg> getUserOrgList() {
		return userOrgList;
	}

	public void setUserOrgList(List<UserOrg> userOrgList) {
		this.userOrgList = userOrgList;
	}

	public void setDeleteFlag(Short deleteFlag) {
		this.deleteFlag = deleteFlag;
	}

	@Column(name = "delete_flag")
	public Short getDeleteFlag() {
		return deleteFlag;
	}

	
	
	@Column(name = "avatar")
	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	@Column(name = "my_signature")
	public String getMySignature() {
		return mySignature;
	}

	public void setMySignature(String mySignature) {
		this.mySignature = mySignature;
	}

}