package cn.emay.modules.sys.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import cn.emay.framework.common.utils.MutiLangUtil;
import cn.emay.framework.common.utils.MyBeanUtils;
import cn.emay.framework.common.utils.StringUtil;
import cn.emay.framework.core.common.controller.BaseController;
import cn.emay.framework.core.common.hibernate.qbc.CriteriaQuery;
import cn.emay.framework.core.common.model.json.AjaxJson;
import cn.emay.framework.core.common.model.json.DataGrid;
import cn.emay.framework.core.constant.Globals;
import cn.emay.framework.core.extend.hqlsearch.HqlGenerateUtil;
import cn.emay.framework.tag.core.easyui.TagUtil;
import cn.emay.modules.sys.entity.MutiLang;
import cn.emay.modules.sys.service.MutiLangService;
import cn.emay.modules.sys.service.SystemService;

/**
 * @Title: Controller
 * @Description: 多语言
 * @author Rocky
 * @date 2014-06-28 00:09:31
 * @version V1.0
 * 
 */
@Controller
@RequestMapping("/mutiLangController")
public class MutiLangController extends BaseController {

	@Autowired
	private MutiLangService mutiLangService;
	
	@Autowired
	private SystemService systemService;

	/**
	 * 多语言列表 页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "mutiLang")
	public ModelAndView mutiLang(HttpServletRequest request) {
		return new ModelAndView("system/mutilang/mutiLangList");
	}

	/**
	 * easyui AJAX请求数据
	 * 
	 * @param request
	 * @param response
	 * @param dataGrid
	 */

	@RequestMapping(params = "datagrid")
	public void datagrid(MutiLang mutiLang, HttpServletRequest request,
			HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(MutiLang.class, dataGrid);
		// 查询条件组装器
		HqlGenerateUtil.installHql(cq, mutiLang, request.getParameterMap());
		mutiLangService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}

	/**
	 * 删除多语言
	 * 
	 * @return
	 */
	@RequestMapping(params = "del")
	@ResponseBody
	public AjaxJson del(MutiLang mutiLang, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		mutiLang = systemService.getEntity(MutiLang.class, mutiLang.getId());
		message = MutiLangUtil.paramDelSuccess("common.language");
		mutiLangService.delete(mutiLang);
		mutiLangService.initAllMutiLang();
		systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
		j.setMsg(message);
		return j;
	}

	/**
	 * 添加多语言
	 * 
	 * @param mutiLang
	 * @return
	 */
	@RequestMapping(params = "save")
	@ResponseBody
	public AjaxJson save(MutiLang mutiLang, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		if (StringUtil.isNotEmpty(mutiLang.getId())) {
			message = MutiLangUtil.paramUpdSuccess("common.language");
			MutiLang t = mutiLangService.get(MutiLang.class, mutiLang.getId());
			try {
				MyBeanUtils.copyBeanNotNull2Bean(mutiLang, t);
				mutiLangService.saveOrUpdate(t);
				mutiLangService.initAllMutiLang();
				systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
			} catch (Exception e) {
				e.printStackTrace();
				message = MutiLangUtil.paramUpdFail("common.language");
			}
		} else {

			if(MutiLangUtil.existLangKey( mutiLang.getLangKey(),mutiLang.getLangCode()))
			{
				message = mutiLangService.getLang("common.langkey.exist");
			}

			if(StringUtil.isEmpty(message))
			{
				mutiLangService.save(mutiLang);
				message = MutiLangUtil.paramAddSuccess("common.language");
				systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
			}
		}
		
		mutiLangService.putMutiLang(mutiLang);
		j.setMsg(message);
		return j;
	}

	/**
	 * 多语言列表页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "addorupdate")
	public ModelAndView addorupdate(MutiLang mutiLang,
			HttpServletRequest req) {
		if (StringUtil.isNotEmpty(mutiLang.getId())) {
			mutiLang = mutiLangService.getEntity(MutiLang.class, mutiLang.getId());
			req.setAttribute("mutiLangPage", mutiLang);
			mutiLangService.putMutiLang(mutiLang);
		}
		return new ModelAndView("system/mutilang/mutiLang");
	}
	
	
	/**
	 * 刷新前端缓存
	 * @param request
	 */
	@RequestMapping(params = "refreshCach")
	@ResponseBody
	public AjaxJson refreshCach(HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		try {
			mutiLangService.refleshMutiLangCach();
			message = mutiLangService.getLang("common.refresh.success");
		} catch (Exception e) {
			message = mutiLangService.getLang("common.refresh.fail");
		}
		j.setMsg(message);
		return j;
	}
}
