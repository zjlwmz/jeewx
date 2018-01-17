package cn.emay.modules.wx.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.emay.framework.core.common.service.impl.CommonServiceImpl;
import cn.emay.modules.wx.entity.WxSurvey;
import cn.emay.modules.wx.entity.WxSurveyOption;
import cn.emay.modules.wx.service.WxSurveyService;

@Service("wxSurveyService")
@Transactional
public class WxSurveyServiceImpl extends CommonServiceImpl implements WxSurveyService {

	@Override
	public void saveMain(WxSurvey wxSurvey) {

		save(wxSurvey);
		List<WxSurveyOption> surveyOptionList = wxSurvey.getSurveyOptionList();
		for (WxSurveyOption surveyOption : surveyOptionList) {
			surveyOption.setSurveyId(wxSurvey.getId());
			save(surveyOption);
		}
	}

	@Override
	public void updateMain(WxSurvey wxSurvey) {
		/**
		 * 保存主表信息
		 */
		saveOrUpdate(wxSurvey);
		/**
		 * 删除从表信息
		 */
		this.commonDao.deleteAllEntitie(findByProperty(WxSurveyOption.class, "surveyId", wxSurvey.getId()));
		List<WxSurveyOption> surveyOptionList = wxSurvey.getSurveyOptionList();
		for (WxSurveyOption surveyOption : surveyOptionList) {
			surveyOption.setSurveyId(wxSurvey.getId());
			save(surveyOption);
		}
	}

}
