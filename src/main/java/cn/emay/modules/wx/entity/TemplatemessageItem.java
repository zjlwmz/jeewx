package cn.emay.modules.wx.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import cn.emay.framework.common.poi.excel.annotation.Excel;
import cn.emay.framework.core.common.entity.IdEntity;

/**   
 * @Title: Entity
 * @Description: 模板消息明细
 * @author onlineGenerator
 * @date 2015-01-08 09:56:27
 * @version V1.0   
 *
 */
@Entity
@Table(name = "wx_templatemessage_item")
public class TemplatemessageItem extends IdEntity{

	private static final long serialVersionUID = 1L;
	
	
	/**创建人名称*/
	private String createName;
	
	
	/**创建日期*/
	private Date createDate;
	
	
	/**修改人名称*/
	private String updateName;
	
	
	/**修改日期*/
	private Date updateDate;
	
	
	/**模板消息Id*/
	private String templatemessageId;
	
	
	/**模板项key*/
	@Excel(exportName="模板项key")
	private String itemKey;
	
	
	/**模板项标题*/
	@Excel(exportName="模板项标题")
	private String itemTitle;
	
	
	/**模板项颜色*/
	@Excel(exportName="模板项颜色")
	private String itemColor;


	
	public TemplatemessageItem(){
		super();
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
	
	@Column(name="templatemessage_id")
	public String getTemplatemessageId() {
		return templatemessageId;
	}


	public void setTemplatemessageId(String templatemessageId) {
		this.templatemessageId = templatemessageId;
	}


	@Column(name="item_key")
	public String getItemKey() {
		return itemKey;
	}


	public void setItemKey(String itemKey) {
		this.itemKey = itemKey;
	}

	@Column(name="item_title")
	public String getItemTitle() {
		return itemTitle;
	}


	public void setItemTitle(String itemTitle) {
		this.itemTitle = itemTitle;
	}

	
	@Column(name="item_color")
	public String getItemColor() {
		return itemColor;
	}


	public void setItemColor(String itemColor) {
		this.itemColor = itemColor;
	}
	
	
	
	
	
}
