package cn.emay.modules.chat.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.emay.framework.core.common.exception.BusinessException;
import cn.emay.framework.core.common.hibernate.qbc.CriteriaQuery;
import cn.emay.framework.core.common.model.json.AjaxJson;
import cn.emay.framework.core.common.model.json.DataGrid;
import cn.emay.framework.core.constant.Globals;
import cn.emay.framework.core.extend.hqlsearch.HqlGenerateUtil;
import cn.emay.framework.tag.core.easyui.TagUtil;
import cn.emay.modules.chat.entity.ChatGroup;
import cn.emay.modules.chat.service.ChatGroupService;
import cn.emay.modules.sys.service.SystemService;


/**
 * 聊天群组控制器
 * @author lenovo
 *
 */
@Controller
@RequestMapping("/chatGroupController")
public class ChatGroupController {

	private static final Logger logger = Logger.getLogger(ChatGroupController.class);
	
	/**
	 * 聊天群组service接口
	 */
	@Autowired
	private ChatGroupService chatGroupService;
	
	
	/**
	 * 系统服务接口
	 */
	@Autowired
	private SystemService systemService;
	
	
	
	@ModelAttribute("chatGroup")
	public ChatGroup get(@RequestParam(required=false) String id) {
		if (StringUtils.isNotBlank(id)){
			return chatGroupService.get(id);
		}else{
			return new ChatGroup();
		}
	}
	
	
	
	
	@RequestMapping(params="list")
	public String list(){
		return "modules/chat/chatgroup/chatgroupList";
	}
	
	
	/**
	 * 微信账号easyuiAJAX用户列表请求数据 
	 * @param request
	 * @param response
	 * @param dataGrid
	 */
	@RequestMapping(params = "datagrid")
	public void datagrid(ChatGroup chatGroup,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid){
		CriteriaQuery cq = new CriteriaQuery(ChatGroup.class, dataGrid);
		//查询条件组装器
		HqlGenerateUtil.installHql(cq, chatGroup, request.getParameterMap());
		try{
		//自定义追加查询条件
		}catch (Exception e) {
			logger.error("微信账号easyuiAJAX", e);
			throw new BusinessException(e.getMessage());
		}
		cq.add();
		chatGroupService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
		  
	}
	
	
	/**
	 * 聊天群组删除
	 * 
	 * @return
	 */
	@RequestMapping(params = "doDel")
	@ResponseBody
	public AjaxJson doDel(ChatGroup chatGroup, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "聊天群组删除成功";
		try{
			chatGroupService.delete(chatGroup);
			systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
		}catch(Exception e){
			e.printStackTrace();
			message = "聊天群组删除失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	
	
	/**
	 * 聊天群组保存
	 */
	@RequestMapping(params = "save")
	@ResponseBody
	public AjaxJson save(ChatGroup chatGroup,HttpServletRequest request, HttpServletResponse response){
		String message = null;
		AjaxJson ajaxjson = new AjaxJson();
		try{
			chatGroupService.saveOrUpdate(chatGroup);
			ajaxjson.setSuccess(true);
		}catch (Exception e) {
			logger.error("聊天群组保存异常", e);
			message="聊天群组保存异常";
			ajaxjson.setMsg(message);
			ajaxjson.setSuccess(false);
		}
		return ajaxjson;
	}
	
	
	
	/**
	 * 聊天群组表单页面
	 */
	@RequestMapping(params="form")
	public String form(ChatGroup chatGroup,ModelMap modelMap,HttpServletRequest request) {
		modelMap.put("chatGroup", chatGroup);
		return "modules/chat/chatgroup/chatgroupForm";
	}
	
	
}
