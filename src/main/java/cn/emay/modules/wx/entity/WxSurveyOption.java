package cn.emay.modules.wx.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import cn.emay.framework.core.common.entity.IdEntity;


/**
 * 调查问卷 问题 选项
 * @author zjlWm
 * @date 2015-05-28
 *
 */
@Entity
@Table(name="wx_survey_option")
public class WxSurveyOption extends IdEntity{

	private static final long serialVersionUID = 5456102274442810310L;

	/**
	 * 问题id
	 */
	@Column(name="survey_id")
	private String surveyId;
	
	/**
	 * 选项排序
	 */
	@Column(name="option_sort")
	private Long optionSort;
	
	/**
	 * 选项名称
	 */
	@Column(name="option_name")
	private String optionName;
	
	/**
	 * 选项值
	 */
	@Column(name="option_value")
	private String optionValue;

	public String getSurveyId() {
		return surveyId;
	}

	public void setSurveyId(String surveyId) {
		this.surveyId = surveyId;
	}

	public Long getOptionSort() {
		return optionSort;
	}

	public void setOptionSort(Long optionSort) {
		this.optionSort = optionSort;
	}

	public String getOptionName() {
		return optionName;
	}

	public void setOptionName(String optionName) {
		this.optionName = optionName;
	}

	public String getOptionValue() {
		return optionValue;
	}

	public void setOptionValue(String optionValue) {
		this.optionValue = optionValue;
	}
	
	
	
}
