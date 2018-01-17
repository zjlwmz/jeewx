package cn.emay.modules.wx.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import cn.emay.framework.core.common.entity.IdEntity;

/**
 * 关注提示语实体
 * @author zjlWm
 * @date 2015-11-22
 */
@Entity
@Table(name = "wx_subscribe")
public class Subscribe extends IdEntity{

	private static final long serialVersionUID = 1L;
	
	
	private String templateId;		//模板id
	private String msgType;			//消息类型
	private Date createDate;		//创建时间
	private String wechatId;		//微信账号id
	
	private String templateName;


	@Column(name="wechat_id")
	public String getWechatId() {
		return wechatId;
	}

	public void setWechatId(String wechatId) {
		this.wechatId = wechatId;
	}


	@Column(name="template_id")
	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	@Column(name="msg_type")
	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}

	public String getMsgType() {
		return msgType;
	}
	
	@Column(name="create_date")
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	@Transient
	public String getTemplateName() {
		return templateName;
	}
	
	@Transient
	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	
	
}
