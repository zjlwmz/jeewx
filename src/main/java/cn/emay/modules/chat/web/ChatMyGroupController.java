package cn.emay.modules.chat.web;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.emay.framework.common.utils.StringUtils;
import cn.emay.framework.core.common.exception.BusinessException;
import cn.emay.framework.core.common.hibernate.qbc.CriteriaQuery;
import cn.emay.framework.core.common.model.json.AjaxJson;
import cn.emay.framework.core.common.model.json.DataGrid;
import cn.emay.framework.core.constant.Globals;
import cn.emay.framework.core.extend.hqlsearch.HqlGenerateUtil;
import cn.emay.framework.tag.core.easyui.TagUtil;
import cn.emay.modules.chat.entity.ChatMyGroup;
import cn.emay.modules.chat.service.ChatMyGroupService;
import cn.emay.modules.sys.entity.User;
import cn.emay.modules.sys.service.SystemService;
import cn.emay.modules.sys.utils.UserCacheUtils;


/**
 * 我的分组控制器
 * @author lenovo
 *
 */
@Controller
@RequestMapping("/chatMyGroupController")
public class ChatMyGroupController {

	private static final Logger logger = Logger.getLogger(ChatMyGroupController.class);
	
	
	/**
	 * 我的分组service接口
	 */
	@Autowired
	private ChatMyGroupService chatMyGroupService;
	
	/**
	 * 系统服务接口
	 */
	@Autowired
	private SystemService systemService;
	
	
	
	@ModelAttribute("chatMyGroup")
	public ChatMyGroup get(@RequestParam(required=false) String id) {
		if (StringUtils.isNotBlank(id)){
			return chatMyGroupService.get(id);
		}else{
			return new ChatMyGroup();
		}
	}
	
	
	@RequestMapping(params="list")
	public String list(){
		return "modules/chat/chatMyGroup/chatMyGroupList";
	}
	
	
	/**
	 * 微信账号easyuiAJAX用户列表请求数据 
	 * @param request
	 * @param response
	 * @param dataGrid
	 */
	@RequestMapping(params = "datagrid")
	public void datagrid(ChatMyGroup chatMyGroup,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid){
		CriteriaQuery cq = new CriteriaQuery(ChatMyGroup.class, dataGrid);
		//查询条件组装器
		HqlGenerateUtil.installHql(cq, chatMyGroup, request.getParameterMap());
		User user=UserCacheUtils.getCurrentUser();
		try{
			cq.eq("userId", user.getId());
		//自定义追加查询条件
		}catch (Exception e) {
			logger.error("微信账号easyuiAJAX", e);
			throw new BusinessException(e.getMessage());
		}
		cq.add();
		chatMyGroupService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
		  
	}
	
	
	/**
	 * 聊天分组删除
	 * 
	 * @return
	 */
	@RequestMapping(params = "doDel")
	@ResponseBody
	public AjaxJson doDel(ChatMyGroup chatMyGroup, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "聊天分组删除成功";
		try{
			chatMyGroupService.delete(chatMyGroup);
			systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
		}catch(Exception e){
			e.printStackTrace();
			message = "聊天分组删除失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	
	
	/**
	 * 聊天分组保存
	 */
	@RequestMapping(params = "save")
	@ResponseBody
	public AjaxJson save(ChatMyGroup chatMyGroup,HttpServletRequest request, HttpServletResponse response){
		String message = null;
		AjaxJson ajaxjson = new AjaxJson();
		try{
			User user=UserCacheUtils.getCurrentUser();
			if(StringUtils.isBlank(chatMyGroup.getUserId())){
				chatMyGroup.setUserId(user.getId());
				chatMyGroup.setCreateDate(new Date());
			}
			chatMyGroupService.save(chatMyGroup);
			ajaxjson.setSuccess(true);
		}catch (Exception e) {
			logger.error("聊天分组保存异常", e);
			message="聊天分组保存异常";
			ajaxjson.setMsg(message);
			ajaxjson.setSuccess(false);
		}
		return ajaxjson;
	}
	
	
	
	/**
	 * 聊天分组表单页面
	 */
	@RequestMapping(params="form")
	public String form(ChatMyGroup chatMyGroup,ModelMap modelMap,HttpServletRequest request) {
		modelMap.put("chatMyGroup", chatMyGroup);
		return "modules/chat/chatMyGroup/chatMyGroupForm";
	}
	
	
}
