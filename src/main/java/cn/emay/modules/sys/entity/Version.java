package cn.emay.modules.sys.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import cn.emay.framework.core.common.entity.IdEntity;

/**
 * 
 * @Title 程序版本控制表
 * @author zjlwm
 * @date 2017-2-20 上午11:53:25
 *
 */
@Entity
@Table(name = "sys_version")
public class Version extends IdEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	private String versionName;//版本名称
	private String versionCode;//版本编码
	private String loginPage;//登陆入口页面
	private String versionNum;//版本号
	@Column(name = "versionname", length = 30)
	public String getVersionName() {
		return versionName;
	}
	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}
	
	@Column(name = "versioncode", length = 50)
	public String getVersionCode() {
		return versionCode;
	}
	public void setVersionCode(String versionCode) {
		this.versionCode = versionCode;
	}
	@Column(name = "loginpage", length = 100)
	public String getLoginPage() {
		return loginPage;
	}
	public void setLoginPage(String loginPage) {
		this.loginPage = loginPage;
	}
	@Column(name = "versionnum", length = 20)
	public String getVersionNum() {
		return versionNum;
	}
	public void setVersionNum(String versionNum) {
		this.versionNum = versionNum;
	}
}
