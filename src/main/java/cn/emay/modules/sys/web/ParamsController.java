package cn.emay.modules.sys.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
import cn.emay.modules.sys.entity.Params;
import cn.emay.modules.sys.service.ParamsService;
import cn.emay.modules.sys.service.SystemService;

/**
 * @Title 参数控制器
 * @author zjlwm
 * @date 2017-2-7 下午2:49:53
 */
@Controller
@RequestMapping("/paramsController")
public class ParamsController extends BaseController {

	private Logger logger = Logger.getLogger(ParamsController.class);
	
	
	/**
	 * 参数service接口
	 */
	@Autowired
	private ParamsService paramsService;

	@Autowired
	private SystemService systemService;

	@RequestMapping(params = "list")
	public ModelAndView list(HttpServletRequest request) {
		return new ModelAndView("modules/sys/params/paramsList");
	}

	@RequestMapping(params = "datagrid")
	public void datagrid(Params params, HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(Params.class, dataGrid);
		// 查询条件组装器
		HqlGenerateUtil.installHql(cq, params, request.getParameterMap());
		paramsService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}

	/**
	 * 参数删除
	 */
	@RequestMapping(params = "del")
	@ResponseBody
	public AjaxJson del(Params params, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		params = paramsService.getEntity(Params.class, params.getId());
		message = MutiLangUtil.paramDelSuccess("common.language");
		paramsService.delete(params);
		systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
		j.setMsg(message);
		return j;
	}

	@RequestMapping(params = "form")
	public ModelAndView form(Params params, Model model) {
		if (StringUtil.isNotEmpty(params.getId())) {
			params = paramsService.getEntity(Params.class, params.getId());
			model.addAttribute("params", params);
		}
		return new ModelAndView("modules/sys/params/paramsForm");
	}

	
	/**
	 * 参数保存
	 * @param params
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "save")
	@ResponseBody
	public AjaxJson save(Params params, HttpServletRequest request) {
		String message = null;
		AjaxJson ajaxJson = new AjaxJson();
		try{
			if (StringUtil.isNotEmpty(params.getId())) {
				message = MutiLangUtil.paramUpdSuccess("common.language");
				Params t = paramsService.get(Params.class, params.getId());
				try {
					MyBeanUtils.copyBeanNotNull2Bean(params, t);
					paramsService.saveOrUpdate(t);
					systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
				} catch (Exception e) {
					e.printStackTrace();
					message = MutiLangUtil.paramUpdFail("common.language");
				}
			} else {
				long count=paramsService.findParamsByNameCount(params.getParamName());
				if(count>0){
					ajaxJson.setSuccess(false);
					ajaxJson.setMsg("参数名称已经存在");
				}else{
					paramsService.save(params);
				}
			}
		}catch (Exception e) {
			logger.error("参数保存异常", e);
			ajaxJson.setSuccess(false);
			ajaxJson.setMsg("保存失败");
		}
		return ajaxJson;
	}
}
