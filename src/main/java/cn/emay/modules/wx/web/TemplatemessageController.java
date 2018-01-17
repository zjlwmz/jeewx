package cn.emay.modules.wx.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import cn.emay.framework.common.utils.StringUtil;
import cn.emay.framework.common.utils.StringUtils;
import cn.emay.framework.core.common.controller.BaseController;
import cn.emay.framework.core.common.exception.BusinessException;
import cn.emay.framework.core.common.hibernate.qbc.CriteriaQuery;
import cn.emay.framework.core.common.model.json.AjaxJson;
import cn.emay.framework.core.common.model.json.DataGrid;
import cn.emay.framework.core.constant.Globals;
import cn.emay.framework.core.extend.hqlsearch.HqlGenerateUtil;
import cn.emay.framework.tag.core.easyui.TagUtil;
import cn.emay.modules.sys.service.SystemService;
import cn.emay.modules.wx.entity.Templatemessage;
import cn.emay.modules.wx.entity.TemplatemessageItem;
import cn.emay.modules.wx.entity.TemplatemessagePage;
import cn.emay.modules.wx.entity.WxWechat;
import cn.emay.modules.wx.service.TemplatemessageService;
import cn.emay.modules.wx.service.WxCacheService;


/**
 * @Title 模板消息控制器
 * @author zjlwm
 * @date 2017-1-14 下午9:07:29
 *
 */
@Controller
@RequestMapping("/templatemessageController")
public class TemplatemessageController extends BaseController{
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(TemplatemessageController.class);

	@Autowired
	private TemplatemessageService templatemessageService;
	
	
	@Autowired
	private SystemService systemService;


	
	/**
	 * 微信缓存service接口
	 */
	@Autowired
	private WxCacheService wxCacheService;
	
	
	@ModelAttribute("templatemessage")
	public Templatemessage get(@RequestParam(required=false) String id) {
		if (StringUtils.isNotBlank(id)){
			return templatemessageService.get(id);
		}else{
			return new Templatemessage();
		}
	}
	
	
	
	
	/**
	 * 模板消息列表 页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "list")
	public ModelAndView templatemessage(HttpServletRequest request) {
		return new ModelAndView("modules/weixin/templatemessage/templatemessageList");
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
	public void datagrid(Templatemessage templatemessage,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(Templatemessage.class, dataGrid);
		//查询条件组装器
		HqlGenerateUtil.installHql(cq, templatemessage);
		try{
			WxWechat wxWechat=wxCacheService.getWxWechat();
			//自定义追加查询条件
			cq.eq("wechatId", wxWechat.getId());
		}catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
		cq.add();
		this.templatemessageService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}

	/**
	 * 删除模板消息
	 * 
	 * @return
	 */
	@RequestMapping(params = "doDel")
	@ResponseBody
	public AjaxJson doDel(Templatemessage templatemessage, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		templatemessage = systemService.getEntity(Templatemessage.class, templatemessage.getId());
		String message = "模板消息删除成功";
		try{
			templatemessageService.delMain(templatemessage);
			systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
		}catch(Exception e){
			e.printStackTrace();
			message = "模板消息删除失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}

	
	/**
	 * 批量删除模板消息
	 * @return
	 */
	 @RequestMapping(params = "doBatchDel")
	@ResponseBody
	public AjaxJson doBatchDel(String ids,HttpServletRequest request){
		AjaxJson j = new AjaxJson();
		String message = "模板消息删除成功";
		try{
			for(String id:ids.split(",")){
				Templatemessage templatemessage =templatemessageService.get(id);
				templatemessageService.delMain(templatemessage);
				systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
			}
		}catch(Exception e){
			e.printStackTrace();
			message = "模板消息删除失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}

	 
	/**
	 * 添加模板消息
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "doAdd")
	@ResponseBody
	public AjaxJson doAdd(Templatemessage templatemessage,TemplatemessagePage templatemessagePage, HttpServletRequest request) {
		List<TemplatemessageItem> templatemessageItemList =  templatemessagePage.getTemplatemessageItemList();
		AjaxJson ajaxJson = new AjaxJson();
		String message = "添加成功";
		try{
			WxWechat wxWechat=wxCacheService.getWxWechat();
			templatemessage.setWechatId(wxWechat.getId());
			templatemessageService.addMain(templatemessage, templatemessageItemList);
			systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
		}catch(Exception e){
			e.printStackTrace();
			message = "模板消息添加失败";
			ajaxJson.setSuccess(false);
			throw new BusinessException(e.getMessage());
		}
		ajaxJson.setMsg(message);
		return ajaxJson;
	}
	
	/**
	 * 更新模板消息
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "doUpdate")
	@ResponseBody
	public AjaxJson doUpdate(Templatemessage templatemessage,TemplatemessagePage templatemessagePage, HttpServletRequest request) {
		List<TemplatemessageItem> templatemessageItemList =  templatemessagePage.getTemplatemessageItemList();
		AjaxJson j = new AjaxJson();
		String message = "更新成功";
		try{
			templatemessageService.updateMain(templatemessage, templatemessageItemList);
			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
		}catch(Exception e){
			e.printStackTrace();
			message = "更新模板消息失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}

	
	/**
	 * 模板消息新增页面跳转
	 * @return
	 */
	@RequestMapping(params = "goAdd")
	public ModelAndView goAdd(Templatemessage templatemessage, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(templatemessage.getId())) {
			templatemessage = templatemessageService.getEntity(Templatemessage.class, templatemessage.getId());
			req.setAttribute("templatemessagePage", templatemessage);
		}
		return new ModelAndView("modules/weixin/templatemessage/templatemessage-add");
	}
	
	
	/**
	 * 模板消息编辑页面跳转
	 * @return
	 */
	@RequestMapping(params = "goUpdate")
	public ModelAndView goUpdate(Templatemessage templatemessage, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(templatemessage.getId())) {
			templatemessage = templatemessageService.getEntity(Templatemessage.class, templatemessage.getId());
			req.setAttribute("templatemessagePage", templatemessage);
		}
		return new ModelAndView("modules/weixin/templatemessage/templatemessage-update");
	}
	
	
	/**
	 * 加载明细列表[模板消息明细]
	 * @return
	 */
	@RequestMapping(params = "templatemessageItemList")
	public ModelAndView templatemessageItemList(Templatemessage templatemessage, HttpServletRequest req) {
	
		//===================================================================================
		//获取参数
		Object id0 = templatemessage.getId();
		//===================================================================================
		//查询-模板消息明细
	    String hql0 = "from TemplatemessageItem where 1 = 1 AND templatemessageId = ? ";
	    try{
	    	List<Templatemessage> templatemessageItemEntityList = systemService.findHql(hql0,id0);
			req.setAttribute("templatemessageItemList", templatemessageItemEntityList);
		}catch(Exception e){
			logger.info(e.getMessage());
		}
		return new ModelAndView("modules/weixin/templatemessage/templatemessageItemList");
	}
}
