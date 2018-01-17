package cn.emay.modules.sys.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import cn.emay.framework.common.utils.ResourceUtil;
import cn.emay.framework.common.utils.StringUtil;
import cn.emay.framework.core.common.controller.BaseController;
import cn.emay.framework.core.common.hibernate.qbc.CriteriaQuery;
import cn.emay.framework.core.common.model.json.AjaxJson;
import cn.emay.framework.core.common.model.json.DataGrid;
import cn.emay.framework.core.constant.Globals;
import cn.emay.framework.tag.core.easyui.TagUtil;
import cn.emay.modules.sys.entity.Config;
import cn.emay.modules.sys.service.SystemService;

/**
 * 配置信息处理类
 * 
 * @author 张代浩
 * 
 */
// @Scope("prototype")
@Controller
@RequestMapping("/configController")
public class ConfigController extends BaseController {
	/**
	 * Logger for this class
	 */
	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(ConfigController.class);
	private SystemService systemService;

	@Autowired
	public void setSystemService(SystemService systemService) {
		this.systemService = systemService;
	}

	/**
	 * 配置列表页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "config")
	public ModelAndView config() {
		return new ModelAndView("system/config/configList");
	}

	/**
	 * easyuiAjax表单请求
	 * 
	 * @param request
	 * @param response
	 * @param dataGrid
	 */
	@RequestMapping(params = "datagrid")
	public void datagrid(HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(Config.class, dataGrid);
		this.systemService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}

	/**
	 * 删除配置信息
	 * 
	 * @param config
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "del")
	@ResponseBody
	public AjaxJson del(Config config, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		config = systemService.getEntity(Config.class, config.getId());
		message = "配置信息: " + config.getName() + "被删除 成功";
		systemService.delete(config);
		systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);

		return j;
	}

	/**
	 * 添加和更新配置信息
	 * 
	 * @param config
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "save")
	@ResponseBody
	public AjaxJson save(Config tsConfig, HttpServletRequest request) {
		String message = null;
		if (StringUtil.isEmpty(tsConfig.getId())) {
			Config tsConfig2 = systemService.findUniqueByProperty(Config.class, "code", tsConfig.getCode());
			if (tsConfig2 != null) {
				message = "编码为: " + tsConfig.getCode() + "的配置信息已存在";
			} else {
				tsConfig.setUser(ResourceUtil.getSessionUserName());
				systemService.save(tsConfig);
				message = "配置信息: " + tsConfig.getName() + "被添加成功";
				systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
			}

		} else {
			message = "配置信息: " + tsConfig.getName() + "被修改成功";
			systemService.updateEntitie(tsConfig);
			systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
		}
		AjaxJson j = new AjaxJson();
		j.setMsg(message);

		return j;
	}

	/**
	 * 添加和更新配置信息页面
	 * 
	 * @param config
	 * @param req
	 * @return
	 */
	@RequestMapping(params = "addorupdate")
	public ModelAndView addorupdate(Config config, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(config.getId())) {
			config = systemService.getEntity(Config.class, config.getId());
			req.setAttribute("config", config);
		}
		return new ModelAndView("system/config/config");
	}

}
