package cn.emay.modules.sys.entity;
// default package

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;


/**
 * 
 * @Title 文档下载,新闻,法规表
 * @author zjlwm
 * @date 2017-2-20 上午11:48:02
 *
 */
@Entity
@Table(name = "sys_document")
@PrimaryKeyJoinColumn(name = "id")
public class Document extends Attachment implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String documentTitle;//文档标题
	
	private byte[] pictureIndex;//焦点图导航
	
	private Short documentState;//状态：0未发布，1已发布
	
	private Short showHome;//是否首页显示
	
	private Type type;//文档分类
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "typeid")
	public Type getType() {
		return type;
	}
	
	public void setType(Type type) {
		this.type = type;
	}
	
	@Column(name = "documenttitle", length = 100)
	public String getDocumentTitle() {
		return documentTitle;
	}
	
	public void setDocumentTitle(String documentTitle) {
		this.documentTitle = documentTitle;
	}
	
	@Column(name = "pictureindex",length=3000)
	public byte[] getPictureIndex() {
		return pictureIndex;
	}
	
	public void setPictureIndex(byte[] pictureIndex) {
		this.pictureIndex = pictureIndex;
	}
	
	@Column(name = "documentstate")
	public Short getDocumentState() {
		return documentState;
	}
	
	public void setDocumentState(Short documentState) {
		this.documentState = documentState;
	}
	
	@Column(name = "showhome")
	public Short getShowHome() {
		return showHome;
	}
	
	public void setShowHome(Short showHome) {
		this.showHome = showHome;
	}
}