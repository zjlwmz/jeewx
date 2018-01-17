package cn.emay.modules.wx.service;

import cn.emay.framework.core.common.service.CommonService;
import cn.emay.modules.wx.entity.WxSurvey;



public interface WxSurveyService extends CommonService{

	
	public void saveMain(WxSurvey wxSurvey);
	
	public void updateMain(WxSurvey wxSurvey);
}
