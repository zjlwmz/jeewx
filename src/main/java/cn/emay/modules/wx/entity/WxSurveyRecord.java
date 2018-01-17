/**
 * 
 */
package cn.emay.modules.wx.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import cn.emay.framework.core.common.entity.IdEntity;

/**
 * 调查问卷结果记录
 * @author zjlWm
 * @date 2015-07-30
 *
 */
@Entity
@Table(name="wx_survey_record")
public class WxSurveyRecord extends IdEntity{

	private static final long serialVersionUID = -4761921523120360653L;


	/**
	 * 调查问卷
	 */
	@Column(name="mai_id")
	private String mainId;
	
	
	/**
	 * 调查题目
	 */
	@Column(name="mai_id")
	private String surveyId;
	
	/**
	 * 调查答复
	 */
	@Column(name="answer")
	private String answer;
	
	/**
	 * 调查答复id
	 */
	@Column(name="answer_id")
	private String answerId;
	
	@Column(name="openid")
	private String openid;
	
	@Column(name="memberid")
	private String memberid;
	
	@Column(name="create_name")
	private String createName;
	/**创建日期*/
	@Column(name="create_date")
	private Date createDate;
	
	/**创建人id*/
	@Column(name="create_by")
	private String createBy;
	
	@Column(name="update_by")
	private String updateBy;
	
	@Column(name="update_name")
	private String updateName;
	
	/**创建日期*/
	@Column(name="update_date")
	private Date updateDate;

	public String getMainId() {
		return mainId;
	}

	public void setMainId(String mainId) {
		this.mainId = mainId;
	}

	public String getSurveyId() {
		return surveyId;
	}

	public void setSurveyId(String surveyId) {
		this.surveyId = surveyId;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public String getCreateName() {
		return createName;
	}

	public void setCreateName(String createName) {
		this.createName = createName;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public String getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}

	public String getUpdateName() {
		return updateName;
	}

	public void setUpdateName(String updateName) {
		this.updateName = updateName;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public String getAnswerId() {
		return answerId;
	}

	public void setAnswerId(String answerId) {
		this.answerId = answerId;
	}

	public String getMemberid() {
		return memberid;
	}

	public void setMemberid(String memberid) {
		this.memberid = memberid;
	}
	
	
	
	
}
