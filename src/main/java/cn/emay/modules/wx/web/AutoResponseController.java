package cn.emay.modules.wx.web;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import cn.emay.framework.common.utils.MyBeanUtils;
import cn.emay.framework.common.utils.StringUtil;
import cn.emay.framework.core.common.hibernate.qbc.CriteriaQuery;
import cn.emay.framework.core.common.model.json.AjaxJson;
import cn.emay.framework.core.common.model.json.DataGrid;
import cn.emay.framework.core.constant.Globals;
import cn.emay.framework.core.extend.hqlsearch.HqlGenerateUtil;
import cn.emay.framework.tag.core.easyui.TagUtil;
import cn.emay.modules.sys.service.SystemService;
import cn.emay.modules.wx.entity.AutoResponse;
import cn.emay.modules.wx.entity.NewsTemplate;
import cn.emay.modules.wx.entity.TextTemplate;
import cn.emay.modules.wx.entity.WxWechat;
import cn.emay.modules.wx.service.AutoResponseService;
import cn.emay.modules.wx.service.WxCacheService;


/**
 * 
 * @Title 自动回复关键字
 * @author zjlwm
 * @date 2017-1-15 上午11:36:08
 *
 */
@Controller
@RequestMapping("/autoResponseController")
public class AutoResponseController {

	
	@Autowired
	private SystemService systemService;
	
	@Autowired
	private WxCacheService wxCacheService;
	
	
	@Autowired
	private AutoResponseService autoResponseService;
	
	
	
	private String message;

	/**
	 * 转向列表
	 */
	@RequestMapping(params = "list")
	public ModelAndView list() {
		return new ModelAndView("modules/weixin/autoresponse/autoresponseList");
	}

	/**
	 * 消息自动回复
	 * 
	 * @param autoResponse
	 * @param request
	 * @param response
	 * @param dataGrid
	 */
	@RequestMapping(params = "datagrid")
	@ResponseBody
	public void datagrid(AutoResponse autoResponse, HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		WxWechat wxWechat = wxCacheService.getWxWechat();
		CriteriaQuery cq = new CriteriaQuery(AutoResponse.class, dataGrid);
		cq.eq("wechatId", wxWechat.getId());
		HqlGenerateUtil.installHql(cq, autoResponse);
		this.autoResponseService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);

	}

	/**
	 * 删除信息
	 * 
	 * @param autoResponse
	 * @param req
	 * @return
	 */
	@RequestMapping(params = "del")
	@ResponseBody
	public AjaxJson del(AutoResponse autoResponse, HttpServletRequest req) {

		AjaxJson ajaxJson = new AjaxJson();
		autoResponse = this.autoResponseService.getEntity(AutoResponse.class, autoResponse.getId());
		this.autoResponseService.delete(autoResponse);
		message = "删除信息数据成功！";
		systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
		ajaxJson.setMsg(this.message);
		return ajaxJson;

	}

	/**
	 * 自动回复消息
	 * 
	 * @param req
	 * @return
	 */
	@RequestMapping(params = "addOrUpdate")
	public ModelAndView addOrUpdate(HttpServletRequest req) {

		WxWechat wxWechat = wxCacheService.getWxWechat();
		String id = req.getParameter("id");
		req.setAttribute("id", id);
		List<TextTemplate> textList = this.autoResponseService.findByQueryString("from TextTemplate t where t.wechatId = '" + wxWechat.getId() + "'");
		List<NewsTemplate> newsList = this.autoResponseService.findByQueryString("from NewsTemplate t where t.wechatId = '" + wxWechat.getId() + "'");
		req.setAttribute("textList", textList);
		req.setAttribute("newsList", newsList);
		if (StringUtil.isNotEmpty(id)) {
			AutoResponse autoResponse = this.autoResponseService.getEntity(AutoResponse.class, id);
			String msgType = autoResponse.getMsgType();
			String resContent = autoResponse.getResContent();
			String keyWord = autoResponse.getKeyWord();
			String templateName = autoResponse.getTemplateName();
			req.setAttribute("msgType", msgType);
			req.setAttribute("resContent", resContent);
			req.setAttribute("keyWord", keyWord);
			req.setAttribute("templateName", templateName);
		}
		return new ModelAndView("modules/weixin/autoresponse/autoresponseForm");

	}

	/**
	 * 保存关键字修改
	 * 
	 * @param autoResponse
	 * @param req
	 * @return
	 */
	@RequestMapping(params = "doSave")
	@ResponseBody
	public AjaxJson doSave(AutoResponse autoResponse, HttpServletRequest req) {
		AjaxJson ajaxJson = new AjaxJson();
		WxWechat wxWechat = wxCacheService.getWxWechat();
		if(null==wxWechat){
			ajaxJson.setSuccess(false);
			ajaxJson.setMsg("请添加一个公众帐号。");
			return ajaxJson;
		}
		String templateName = "";
		
		String id = autoResponse.getId();
		if (StringUtil.isNotEmpty(id)) {
			AutoResponse tempAutoResponse = this.autoResponseService.getEntity(AutoResponse.class, autoResponse.getId());
			// 获取模板文名字
			templateName = getTempName(autoResponse.getMsgType(), autoResponse.getResContent());
			autoResponse.setTemplateName(templateName);
			this.message = "修改关键字回复成功！";
			try {
				MyBeanUtils.copyBeanNotNull2Bean(autoResponse, tempAutoResponse);
				this.autoResponseService.saveOrUpdate(tempAutoResponse);
				systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			autoResponse.setCreateTime(new Date());
			String templateId = autoResponse.getResContent();
			String msgType = autoResponse.getMsgType();
			templateName = getTempName(msgType, templateId);
			autoResponse.setTemplateName(templateName);
			autoResponse.setWechatId(wxWechat.getId());
			this.autoResponseService.save(autoResponse);
		}
		return ajaxJson;

	}

	/**
	 * 获取模板文件名字
	 * 
	 * @param msgType
	 * @param templateId
	 * @return
	 */
	private String getTempName(String msgType, String templateId) {
		String templateName = "";
		if ("text".equals(msgType)) {
			TextTemplate textTemplate = this.autoResponseService.getEntity(TextTemplate.class, templateId);
			if (textTemplate != null) {
				templateName = textTemplate.getTemplateName();
			}
		} else if ("news".equals(msgType)) {
			NewsTemplate newsTemplate = this.autoResponseService.getEntity(NewsTemplate.class, templateId);
			if (newsTemplate != null) {
				templateName = newsTemplate.getTemplateName();
			}
		}
		return templateName;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	
}
