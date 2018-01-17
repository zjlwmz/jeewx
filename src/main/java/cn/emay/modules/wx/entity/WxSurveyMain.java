package cn.emay.modules.wx.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import cn.emay.framework.core.common.entity.IdEntity;


/**
 * 调查问卷 问题
 * @author zjlWm
 * @date 2015-05-28
 *
 */
@Entity
@Table(name="wx_survey_main")
public class WxSurveyMain extends IdEntity{
	
	private static final long serialVersionUID = -5525336455957795431L;

	/**
	 * 调研题目
	 */
	@Column(name="survey_title")
	private String surveyTitle;
	
	/**
	 * 参与调研人数
	 */
	@Column(name="survey_count")
	private Long surveyCount;
	
	/**
	 * 调研描述
	 */
	@Column(name="survey_description")
	private String surveyDescription;
	
	/**
	 * 订单号
	 */
	@Column(name="request_no")
	private String requestNo;
	
	/**
	 * 状态
	 * 0 未开始;1进行中;2活动结束
	 */
	@Column(name="statement")
	private String statement;
	
	/**
	 * 积分
	 */
	@Column(name="integral")
	private Long integral;
	
	@Column(name="create_name")
	private String createName;
	/**创建人id*/
	@Column(name="create_by")
	private String createBy;
	@Column(name="update_name")
	private String updateName;
	/**创建日期*/
	@Column(name="create_date")
	private Date createDate;
	
	/**
	 * 开始时间
	 */
	@Column(name="begin_date")
	private Date beginDate;
	
	
	/**
	 * 开始时间
	 */
	@Column(name="end_date")
	private Date endDate;
	
	
	public String getSurveyTitle() {
		return surveyTitle;
	}
	public void setSurveyTitle(String surveyTitle) {
		this.surveyTitle = surveyTitle;
	}
	public Long getSurveyCount() {
		return surveyCount;
	}
	public void setSurveyCount(Long surveyCount) {
		this.surveyCount = surveyCount;
	}
	public String getSurveyDescription() {
		return surveyDescription;
	}
	public void setSurveyDescription(String surveyDescription) {
		this.surveyDescription = surveyDescription;
	}
	public String getStatement() {
		return statement;
	}
	public void setStatement(String statement) {
		this.statement = statement;
	}
	public Long getIntegral() {
		return integral;
	}
	public void setIntegral(Long integral) {
		this.integral = integral;
	}
	public String getCreateName() {
		return createName;
	}
	public void setCreateName(String createName) {
		this.createName = createName;
	}
	public String getCreateBy() {
		return createBy;
	}
	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}
	public String getUpdateName() {
		return updateName;
	}
	public void setUpdateName(String updateName) {
		this.updateName = updateName;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public Date getBeginDate() {
		return beginDate;
	}
	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public String getRequestNo() {
		return requestNo;
	}
	public void setRequestNo(String requestNo) {
		this.requestNo = requestNo;
	}
	
	
}
