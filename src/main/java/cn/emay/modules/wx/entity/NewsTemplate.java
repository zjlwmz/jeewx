package cn.emay.modules.wx.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import cn.emay.framework.core.common.entity.IdEntity;

/**
 * 图文消息模板实体
 * @author zjlWm
 * @date 2015-11-22
 */
@Entity
@Table(name="wx_newstemplate")
public class NewsTemplate extends IdEntity {

	private static final long serialVersionUID = 1L;
	
	/**
	 * 模板名称
	 */
	private String templateName;
	
	/**
	 * 创建时间
	 */
	private Date createDate;
	
	
	/**
	 * 更新时间
	 */
	private Date updateDate;
	
	/**
	 * 类型
	 * 普通模板：common 
	 * 超链接模板：cl
	 */
	private String type;
	
	
	/**
	 * 微信账号id
	 */
	private String wechatId;
	
	
	
	
	@Column(name = "wechat_id",length=100)
	public String getWechatId() {
		return wechatId;
	}
	public void setWechatId(String wechatId) {
		this.wechatId = wechatId;
	}
	@Column(name="tempate_name")
	public String getTemplateName() {
		return templateName;
	}
	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}
	
	/**
	 * 创建时间
	 * @return
	 */
	@Column(name="create_date")
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	
	
	@Column(name="update_date")
	public Date getUpdateDate() {
		return updateDate;
	}


	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}


	@Column(name="type")
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
}
