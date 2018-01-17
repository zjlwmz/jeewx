package cn.emay.modules.wx.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import cn.emay.framework.common.poi.excel.annotation.Excel;
import cn.emay.framework.core.common.entity.IdEntity;

/**
 * 文本消息模板实体
 * @author zjlWm
 * @date 2015-11-22
 */
@Entity
@Table(name = "wx_texttemplate")
public class TextTemplate extends IdEntity {

	private static final long serialVersionUID = 1L;
	
	
	@Excel(exportName="关键字")
	private String templateName;
	
	@Excel(exportName="内容")
	private String content;
	
	@Excel(exportName="搜索次数")
	private Integer searchtimes=0;	//点击次数
	
	
	@Excel(exportName="解决次数")
	private Integer solvenum=0;	//解决次数
	
	@Excel(exportName="未解决次数")
	private Integer notresolvednum=0;	//未解决次数
	
	@Excel(exportName="解决率")
	private String solutionRate;	//解决率
	
	@Excel(exportName="创建时间")
	private String createDate;
	
	
	private String wechatId;

	

	@Column(name = "wechat_id")
	public String getWechatId() {
		return wechatId;
	}

	public void setWechatId(String wechatId) {
		this.wechatId = wechatId;
	}

	@Column(name = "templatename")
	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	@Column(name = "content")
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Column(name = "create_date")
	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	
	@Column(name = "solvenum")
	public Integer getSolvenum() {
		return solvenum;
	}

	

	public void setSolvenum(Integer solvenum) {
		this.solvenum = solvenum;
	}

	@Column(name = "notresolvednum")
	public Integer getNotresolvednum() {
		return notresolvednum;
	}

	public void setNotresolvednum(Integer notresolvednum) {
		this.notresolvednum = notresolvednum;
	}

	
	@Column(name = "searchtimes")
	public Integer getSearchtimes() {
		return searchtimes;
	}

	public void setSearchtimes(Integer searchtimes) {
		this.searchtimes = searchtimes;
	}

	@Transient
	public String getSolutionRate() {
		return solutionRate;
	}

	public void setSolutionRate(String solutionRate) {
		this.solutionRate = solutionRate;
	}
	
	
	
	
	
}
