package cn.emay.modules.wx.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import cn.emay.framework.common.utils.RoletoJson;
import cn.emay.framework.core.common.controller.BaseController;
import cn.emay.framework.core.common.exception.BusinessException;
import cn.emay.framework.core.common.hibernate.qbc.CriteriaQuery;
import cn.emay.framework.core.common.model.json.AjaxJson;
import cn.emay.framework.core.common.model.json.DataGrid;
import cn.emay.framework.core.constant.Globals;
import cn.emay.framework.core.extend.hqlsearch.HqlGenerateUtil;
import cn.emay.framework.tag.core.easyui.TagUtil;
import cn.emay.modules.sys.service.SystemService;
import cn.emay.modules.wx.entity.WxSurvey;
import cn.emay.modules.wx.entity.WxSurveyMain;
import cn.emay.modules.wx.entity.WxSurveyOption;
import cn.emay.modules.wx.service.WxSurveyService;


/**
 * 调查问卷
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/weixinSurveyController")
public class WeixinSurveyController extends BaseController{

	
	
	@Autowired
	private WxSurveyService wxSurveyService;
	
	@Autowired
	private SystemService systemService;
	
	
	/**
	 * 调查文件主题（分组）列表
	 * @return
	 */
	@RequestMapping(params = "weixinSurvey")
	public ModelAndView weixinSurvey(HttpServletRequest request){
		List<WxSurveyMain>surveyMainList=wxSurveyService.findByQueryString("from WeixinSurveyMain");
		request.setAttribute("surveyMainReplace", RoletoJson.listToReplaceStr(surveyMainList, "surveyTitle", "id"));
		return new ModelAndView("modules/weixin/survey/surveyList");
	}
	
	
	/**
	 * 调查问卷新增
	 * @return
	 */
	@RequestMapping(params = "goadd")
	public ModelAndView weixinSurveyMainAdd(WxSurvey wxSurvey,HttpServletRequest request){
		if(StringUtils.isNotBlank(wxSurvey.getId())){
			WxSurvey weixinSurveypage=wxSurveyService.get(WxSurvey.class, wxSurvey.getId());
			request.setAttribute("weixinSurveypage", weixinSurveypage);
		}
		List<WxSurveyMain>surveyMainList=wxSurveyService.findByQueryString("from WeixinSurveyMain");
		request.setAttribute("surveyMainList", surveyMainList);
		return new ModelAndView("modules/weixin/survey/surveyForm");
	}
	
	
	@RequestMapping(params = "surveyOptionList")
	public ModelAndView surveyOptionList(WxSurvey wxSurvey,HttpServletRequest request){
		List<WxSurveyOption>surveyOptionList=wxSurveyService.findByProperty(WxSurveyOption.class, "surveyId", wxSurvey.getId());
		request.setAttribute("surveyOptionList", surveyOptionList);
		return new ModelAndView("modules/weixin/survey/surveyOption");
	}
	

	/**
	 * easyui AJAX请求数据
	 * 
	 * @param request
	 * @param response
	 * @param dataGrid
	 * @param user
	 */

	@RequestMapping(params = "datagrid")
	public void datagrid(WxSurvey weixinSurvey,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(WxSurvey.class, dataGrid);
		//查询条件组装器
		HqlGenerateUtil.installHql(cq, weixinSurvey, request.getParameterMap());
		try{
		//自定义追加查询条件
		}catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
		cq.add();
		wxSurveyService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}
	
	
	/**
	 * 删除添加调研主题
	 * 
	 * @return
	 */
	@RequestMapping(params = "doDel")
	@ResponseBody
	public AjaxJson doDel(WxSurvey wxSurvey, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		wxSurvey = systemService.getEntity(WxSurvey.class, wxSurvey.getId());
		String message = "调研问卷删除成功";
		try{
			wxSurveyService.delete(wxSurvey);
			systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
		}catch(Exception e){
			e.printStackTrace();
			message = "调研问卷删除失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	
	
	
	/**
	 * 调查问卷保存
	 * @param weixinSurvey
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "save")
	@ResponseBody
	public AjaxJson save(WxSurvey wxSurvey, HttpServletRequest request){
		String message = "调研问卷保存成功";
		AjaxJson j = new AjaxJson();
		try{
			if(StringUtils.isBlank(wxSurvey.getId())){
				wxSurveyService.saveMain(wxSurvey);
				systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
			}else{
				wxSurveyService.updateMain(wxSurvey);
				message = "调研问卷更新成功";
				systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
			}
			
		}catch (Exception e) {
			e.printStackTrace();
			message = "调查问卷保存失败";
		}
		j.setMsg(message);
		return j;
	}
	
}
