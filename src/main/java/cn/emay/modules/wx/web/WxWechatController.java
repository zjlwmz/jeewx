package cn.emay.modules.wx.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.emay.framework.core.common.controller.BaseController;
import cn.emay.framework.core.common.exception.BusinessException;
import cn.emay.framework.core.common.hibernate.qbc.CriteriaQuery;
import cn.emay.framework.core.common.model.json.AjaxJson;
import cn.emay.framework.core.common.model.json.DataGrid;
import cn.emay.framework.core.constant.Globals;
import cn.emay.framework.core.extend.hqlsearch.HqlGenerateUtil;
import cn.emay.framework.tag.core.easyui.TagUtil;
import cn.emay.modules.sys.service.SystemService;
import cn.emay.modules.wx.entity.WxWechat;
import cn.emay.modules.wx.service.WxWechatService;

/**
 * 微信账号控制器
 * 
 * @author zjlwm
 * 
 */
@Controller
@RequestMapping("/wxWechatController")
public class WxWechatController extends BaseController {

	private static final Logger logger = Logger.getLogger(WxWechatController.class);
	
	/**
	 * 微信账号service接口
	 */
	@Autowired
	private WxWechatService wxWechatService;
	
	
	@Autowired
	private SystemService systemService;
	
	@ModelAttribute("wxWechat")
	public WxWechat get(@RequestParam(required=false) String id) {
		if (StringUtils.isNotBlank(id)){
			return wxWechatService.get(id);
		}else{
			return new WxWechat();
		}
	}
	
	
	
	/**
	 * 微信账号easyuiAJAX用户列表请求数据 
	 * @param request
	 * @param response
	 * @param dataGrid
	 */
	@RequestMapping(params = "datagrid")
	public void datagrid(WxWechat wxWechat,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid){
		CriteriaQuery cq = new CriteriaQuery(WxWechat.class, dataGrid);
		//查询条件组装器
		HqlGenerateUtil.installHql(cq, wxWechat, request.getParameterMap());
		try{
		//自定义追加查询条件
		}catch (Exception e) {
			logger.error("微信账号easyuiAJAX", e);
			throw new BusinessException(e.getMessage());
		}
		cq.add();
		wxWechatService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
		  
	}
	
	
	/**
	 * 微信账号信息删除
	 * 
	 * @return
	 */
	@RequestMapping(params = "doDel")
	@ResponseBody
	public AjaxJson doDel(WxWechat wxWechat, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "微信账号信息删除成功";
		try{
			wxWechatService.delete(wxWechat);
			systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
		}catch(Exception e){
			e.printStackTrace();
			message = "微信账号信息删除失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	
	/**
	 * 微信账号信息保存
	 */
	@RequestMapping(params = "save")
	@ResponseBody
	public AjaxJson save(WxWechat wxWechat,HttpServletRequest request, HttpServletResponse response){
		String message = null;
		AjaxJson ajaxjson = new AjaxJson();
		try{
			wxWechatService.saveOrUpdate(wxWechat);
			ajaxjson.setSuccess(true);
		}catch (Exception e) {
			logger.error("微信账号信息保存异常", e);
			message="微信账号信息保存异常";
			ajaxjson.setMsg(message);
			ajaxjson.setSuccess(false);
		}
		return ajaxjson;
	}
	
	
	
	/**
	 * 微信账号列表
	 * @param request
	 * @return
	 */
	@RequestMapping(params="list")
	public String list(HttpServletRequest request) {
		return "modules/weixin/wechat/wxWechatList";
	}

	/**
	 * 微信账号表单页面
	 */
	@RequestMapping(params="form")
	public String form(String id,HttpServletRequest request) {
		if(StringUtils.isNotBlank(id)){
			WxWechat wxWechat=wxWechatService.get(id);
			request.setAttribute("wxWechatPage", wxWechat);
		}
		return "modules/weixin/wechat/wxWechatForm";
	}
}
