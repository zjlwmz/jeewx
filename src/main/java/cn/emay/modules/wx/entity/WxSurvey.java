package cn.emay.modules.wx.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import cn.emay.framework.core.common.entity.IdEntity;

/**
 * 调查问题
 * @author zjlWm
 * @date 2015-05-28
 *
 */
@Entity
@Table(name="wx_survey")
public class WxSurvey extends IdEntity{

	private static final long serialVersionUID = 3692971553918906851L;

	/**
	 * 调查问卷
	 */
	@Column(name="mai_id")
	private String mainId;
	
	/**
	 * 调研题目
	 */
	@Column(name="survey_title")
	private String surveyTitle;
	
	/**
	 * 调研类型
	 * 1单选、2多选、3填空、4下拉
	 */
	@Column(name="survey_type")
	private String surveyType;
	
	/**
	 * 排序
	 */
	@Column(name="survey_sort")
	private Integer surveySort;
	
	/**
	 * 参与调研人数
	 */
	@Column(name="survey_count")
	private String surveyCount;
	
	/**
	 *调研描述
	 */
	@Column(name="survey_description")
	private String surveyDescription;
	
	
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

	/**
	 * 选项列表
	 */
	public List<WxSurveyOption>surveyOptionList=new ArrayList<WxSurveyOption>();
	
	
	public String getMainId() {
		return mainId;
	}

	public void setMainId(String mainId) {
		this.mainId = mainId;
	}

	public String getSurveyTitle() {
		return surveyTitle;
	}

	public void setSurveyTitle(String surveyTitle) {
		this.surveyTitle = surveyTitle;
	}

	public String getSurveyType() {
		return surveyType;
	}

	public void setSurveyType(String surveyType) {
		this.surveyType = surveyType;
	}

	public String getSurveyCount() {
		return surveyCount;
	}

	public void setSurveyCount(String surveyCount) {
		this.surveyCount = surveyCount;
	}

	public String getSurveyDescription() {
		return surveyDescription;
	}

	public void setSurveyDescription(String surveyDescription) {
		this.surveyDescription = surveyDescription;
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

	public Integer getSurveySort() {
		return surveySort;
	}

	public void setSurveySort(Integer surveySort) {
		this.surveySort = surveySort;
	}

	
	
	@Transient
	public List<WxSurveyOption> getSurveyOptionList() {
		return surveyOptionList;
	}
	
	
}
