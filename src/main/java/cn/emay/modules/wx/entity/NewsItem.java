package cn.emay.modules.wx.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;


/**
 * 图文消息实体
 * @author zjlWm
 * @date 2015-11-22
 */
@Entity
@Table(name="wx_newsitem")
public class NewsItem implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(generator = "paymentableGenerator")
	@GenericGenerator(name = "paymentableGenerator", strategy = "uuid")
	@Column(name ="id",nullable=false,length=32)
	private String id;
	
	@Column(name="title")
	private String title;
	
	@Column(name="author")
	private String author;
	
	@Column(name="imagepath")
	private String imagePath;
	
	@Column(name="content")
	private String content;
	
	@Column(name="description")
	private String description;
	
	@Column(name="template_id")
	private String newsTemplateId;
	
	@Column(name="orders")
	private String orders;
	
	@Column(name = "wechat_id",length=100)
	private String wechatId;
	
	/**类型：图文|外部链接*/
	@Column(name="new_type")
	private String newType;
	
	/**外部*/
	@Column(name="url")
	private String url;
	
	/**创建时间*/
	@Column(name="create_date")
	private Date createDate;

	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getNewsTemplateId() {
		return newsTemplateId;
	}

	public void setNewsTemplateId(String newsTemplateId) {
		this.newsTemplateId = newsTemplateId;
	}

	public String getOrders() {
		return orders;
	}

	public void setOrders(String orders) {
		this.orders = orders;
	}

	public String getWechatId() {
		return wechatId;
	}

	public void setWechatId(String wechatId) {
		this.wechatId = wechatId;
	}

	public String getNewType() {
		return newType;
	}

	public void setNewType(String newType) {
		this.newType = newType;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}


	
	
	
	
	
	
	
	
}
