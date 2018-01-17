package cn.emay.modules.wx.web;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import weixin.popular.api.MessageAPI;
import weixin.popular.bean.BaseResult;
import weixin.popular.bean.message.message.Message;
import weixin.popular.bean.message.message.TextMessage;
import cn.emay.framework.common.utils.MyBeanUtils;
import cn.emay.framework.core.common.hibernate.qbc.CriteriaQuery;
import cn.emay.framework.core.common.model.json.AjaxJson;
import cn.emay.framework.core.common.model.json.DataGrid;
import cn.emay.framework.core.constant.Globals;
import cn.emay.framework.core.extend.hqlsearch.HqlGenerateUtil;
import cn.emay.framework.tag.core.easyui.TagUtil;
import cn.emay.modules.sys.service.SystemService;
import cn.emay.modules.wx.entity.ReceiveMessage;
import cn.emay.modules.wx.entity.WxWechat;
import cn.emay.modules.wx.service.WxCacheService;

/**
 * @Title 接收消息控制器
 * @author zjlwm
 * @date 2017-2-2 下午2:02:10
 */
@Controller
@RequestMapping("/receiveMessageController")
public class ReceiveMessageController {

	@Autowired
	private SystemService systemService;

	private String message;

	@Autowired
	private WxCacheService wxCacheService;

	@RequestMapping(params = "list")
	public ModelAndView jumpList() {
		return new ModelAndView("modules/weixin/receivetext/receivetextlist");
	}

	@RequestMapping(params = "datagrid")
	@ResponseBody
	public void datagrid(ReceiveMessage receiveMessage, HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {

		CriteriaQuery cq = new CriteriaQuery(ReceiveMessage.class, dataGrid);
		WxWechat wxWechat = wxCacheService.getWxWechat();
		if (wxWechat != null) {
			String wechatId = wxWechat.getId();
			cq.eq("wechatId", wechatId);
		}
		
		//默认排序
		if(dataGrid.getSort()==null){
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("createTime", "desc");
			cq.setOrder(map);
		}
		// cq.add();
		HqlGenerateUtil.installHql(cq, receiveMessage);
		systemService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}

	@RequestMapping(params = "del")
	@ResponseBody
	public AjaxJson deleteSmsGroup(ReceiveMessage receiveMessage, HttpServletRequest req) {
		AjaxJson ajaxJson = new AjaxJson();
		receiveMessage = systemService.getEntity(ReceiveMessage.class, receiveMessage.getId());

		systemService.delete(receiveMessage);

		message = "删除信息数据成功！";
		systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
		ajaxJson.setMsg(this.message);
		return ajaxJson;
	}

	
	/**
	 * 消息回复页面
	 * @param req
	 * @return
	 */
	@RequestMapping(params = "jumpsendmessage")
	public ModelAndView responseMessage(String id,Model model) {
		ReceiveMessage receiveMessage = systemService.getEntity(ReceiveMessage.class, id);
		if(null!=receiveMessage){
			receiveMessage.getContentDetail();
		}
		model.addAttribute("receiveMessage", receiveMessage);
		return new ModelAndView("modules/weixin/receivetext/messageinfo");
	}

	@RequestMapping(params = "update")
	@ResponseBody
	public AjaxJson updateAndSave(ReceiveMessage receiveMessage, HttpServletRequest req) {
		AjaxJson  ajaxJson = new AjaxJson();
		ReceiveMessage tempTextMessage = systemService.getEntity(ReceiveMessage.class, receiveMessage.getId());
		this.message = "回复信息成功！";
		try {
			MyBeanUtils.copyBeanNotNull2Bean(receiveMessage, tempTextMessage);
			tempTextMessage.setResponse("1");
			this.systemService.saveOrUpdate(tempTextMessage);

			String resContent = tempTextMessage.getRescontent();
			String openId = tempTextMessage.getFromUserName();

			String accessToken = wxCacheService.getToken();
			Message messageText = new TextMessage(openId, resContent);
			BaseResult baseResult = MessageAPI.messageCustomSend(accessToken, messageText);
			if (baseResult.isSuccess()) {
				this.message = "回复信息成功！";
				ajaxJson.setSuccess(true);
			} else {
				this.message = "回复信息失败！";
				ajaxJson.setSuccess(false);
			}

			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
		} catch (Exception e) {
			this.message = "回复信息失败！";
			ajaxJson.setSuccess(false);
			e.printStackTrace();
		}
		ajaxJson.setMsg(message);
		return ajaxJson;
	}

}
