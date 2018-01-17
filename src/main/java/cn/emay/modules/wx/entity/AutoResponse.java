package cn.emay.modules.wx.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import cn.emay.framework.core.common.entity.IdEntity;

/**
 * 自动回复信息表-关键字
 * @author zjlWm
 * @date 2015-11-23
 */
@Entity
@Table(name = "wx_autoresponse")
public class AutoResponse extends IdEntity {
	private static final long serialVersionUID = 1L;
	
	private String keyWord;			//关键字
	private String resContent;		//关联模板id
	private String templateName;	//关联模板名称
	private Date createTime;		//创建时间
	private String msgType;			//信息类型
	private String wechatId;		//微信账号id

	
	
	@Column(name = "wechat_id",length=100)
	public String getWechatId() {
		return wechatId;
	}

	public void setWechatId(String wechatId) {
		this.wechatId = wechatId;
	}

	@Column(name = "keyword")
	public String getKeyWord() {
		return keyWord;
	}

	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}

	@Column(name = "rescontent")
	public String getResContent() {
		return resContent;
	}

	public void setResContent(String resContent) {
		this.resContent = resContent;
	}

	@Column(name = "create_time")
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Column(name = "msgtype")
	public String getMsgType() {
		return msgType;
	}

	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}

	@Column(name = "template_name")
	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

}
