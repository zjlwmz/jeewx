package cn.emay.modules.wx.entity;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import cn.emay.framework.core.common.entity.IdEntity;

/**   
 * @Title: Entity
 * @Description: 模板消息
 * @author onlineGenerator
 * @date 2015-01-08 09:56:27
 * @version V1.0   
 *
 */
@Entity
@Table(name = "wx_templatemessage")
public class Templatemessage extends IdEntity{

	private static final long serialVersionUID = 1L;
	
	
	/**创建人名称*/
	private String createName;
	
	
	/**创建日期*/
	private Date createDate;
	
	
	/**修改人名称*/
	private String updateName;
	
	
	/**修改日期*/
	private Date updateDate;
	
	
	/**模板id*/
	private String templateId;
	
	
	/**模板名称*/
	private String templateName;
	
	
	/**微信帐户id*/
	private String wechatId;
	
	
	/**详情Url*/
	private String detailUrl;
	
	
	/**标题颜色*/
	private String topcolor;
	
	
	public Templatemessage(){
		
	}
	

	@PrePersist
	public void prePersist(){
		this.createDate=new Date();
		this.updateDate=this.createDate;
	}
	
	@PreUpdate
	public void preUpdate(){
		this.updateDate=new Date();
	}


	
	@Column(name="create_name")
	public String getCreateName() {
		return createName;
	}



	public void setCreateName(String createName) {
		this.createName = createName;
	}


	@Column(name="create_date")
	public Date getCreateDate() {
		return createDate;
	}



	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}


	@Column(name="update_name")
	public String getUpdateName() {
		return updateName;
	}



	public void setUpdateName(String updateName) {
		this.updateName = updateName;
	}


	@Column(name="update_date")
	public Date getUpdateDate() {
		return updateDate;
	}



	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}


	@Column(name="template_id")
	public String getTemplateId() {
		return templateId;
	}



	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}


	@Column(name="template_name")
	public String getTemplateName() {
		return templateName;
	}



	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}



	


	@Column(name="wechat_id")
	public String getWechatId() {
		return wechatId;
	}


	public void setWechatId(String wechatId) {
		this.wechatId = wechatId;
	}

	@Column(name="detail_url")
	public String getDetailUrl() {
		return detailUrl;
	}



	public void setDetailUrl(String detailUrl) {
		this.detailUrl = detailUrl;
	}


	@Column(name="topcolor")
	public String getTopcolor() {
		return topcolor;
	}



	public void setTopcolor(String topcolor) {
		this.topcolor = topcolor;
	}
	
	

	
}
