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
import cn.emay.modules.wx.entity.NewsTemplate;
import cn.emay.modules.wx.entity.Subscribe;
import cn.emay.modules.wx.entity.TextTemplate;
import cn.emay.modules.wx.entity.WxWechat;
import cn.emay.modules.wx.service.NewsTemplateService;
import cn.emay.modules.wx.service.SubscribeService;
import cn.emay.modules.wx.service.TextTemplateService;
import cn.emay.modules.wx.service.WxCacheService;

/**
 * @Title 关注管理控制器
 * @author zjlwm
 * @date 2017-1-15 上午11:15:37
 */
@Controller
@RequestMapping("/subscribeController")
public class SubscribeController {

	@Autowired
	private SystemService systemService;

	/**
	 * 
	 * 关注提示语service接口
	 * 
	 */
	@Autowired
	private SubscribeService subscribeService;

	@Autowired
	private WxCacheService wxCacheService;
	
	@Autowired
	private TextTemplateService textTemplateService;
	
	@Autowired
	private  NewsTemplateService newsTemplateService;

	private String message;

	@RequestMapping(params = "list")
	public ModelAndView list() {
		return new ModelAndView("modules/weixin/subscribe/subscribeList");
	}

	
	@SuppressWarnings("unchecked")
	@RequestMapping(params = "datagrid")
	@ResponseBody
	public void datagrid(Subscribe subscribe, HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(Subscribe.class, dataGrid);
		WxWechat wxWechat = wxCacheService.getWxWechat();
		cq.eq("wechatId", wxWechat.getId());
		HqlGenerateUtil.installHql(cq, subscribe);
		this.subscribeService.getDataGridReturn(cq, true);
		List<Subscribe>subscribeList= dataGrid.getResults();
		for(Subscribe sub:subscribeList){
			String templateId=sub.getTemplateId();
			String msgType=sub.getMsgType();
			if(msgType.equals("text")){
				TextTemplate textTemplate=textTemplateService.get(templateId);
				if(null!=textTemplate){
					sub.setTemplateName(textTemplate.getTemplateName());
				}
			}else if(msgType.equals("news")){
				NewsTemplate newsTemplate=newsTemplateService.get(templateId);
				if(null!=newsTemplate){
					sub.setTemplateName(newsTemplate.getTemplateName());
				}
			}
		}
		TagUtil.datagrid(response, dataGrid);
	}

	
	@RequestMapping(params = "del")
	@ResponseBody
	public AjaxJson deleteSmsGroup(Subscribe subscribe, HttpServletRequest req) {
		AjaxJson j = new AjaxJson();
		subscribe = this.subscribeService.getEntity(Subscribe.class, subscribe.getId());

		this.subscribeService.delete(subscribe);

		message = "删除信息数据成功！";
		systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
		j.setMsg(this.message);
		return j;
	}

	
	@RequestMapping(params = "jumpSuView")
	public ModelAndView jumpSuView(HttpServletRequest req) {
		String id = req.getParameter("id");
		req.setAttribute("id", id);
		WxWechat wxWechat = wxCacheService.getWxWechat();
		List<TextTemplate> textList = this.subscribeService.findByQueryString("from TextTemplate t where t.wechatId = '" + wxWechat.getId() + "'");
		List<NewsTemplate> newsList = this.subscribeService.findByQueryString("from NewsTemplate t where t.wechatId = '" + wxWechat.getId() + "'");
		req.setAttribute("textList", textList);
		req.setAttribute("newsList", newsList);
		if (StringUtil.isNotEmpty(id)) {
			Subscribe subscribe = subscribeService.getEntity(Subscribe.class, id);
			String lx = subscribe.getMsgType();
			String templateId = subscribe.getTemplateId();
			req.setAttribute("lx", lx);
			req.setAttribute("templateId", templateId);
			req.setAttribute("subscribe", subscribe);
		}
		return new ModelAndView("modules/weixin/subscribe/subscribeForm");
	}

	
	@RequestMapping(params = "su")
	@ResponseBody
	public AjaxJson su(Subscribe subscribe, HttpServletRequest req) {
		AjaxJson j = new AjaxJson();
		WxWechat wxWechat = wxCacheService.getWxWechat();
		if (null == wxWechat) {
			j.setSuccess(false);
			j.setMsg("请添加一个公众帐号。");
			return j;
		}
		String id = subscribe.getId();
		if (StringUtil.isNotEmpty(id)) {
			Subscribe tempAutoResponse = subscribeService.getEntity(Subscribe.class, subscribe.getId());
			this.message = "修改关文本模板成功！";
			try {
				MyBeanUtils.copyBeanNotNull2Bean(subscribe, tempAutoResponse);
				this.subscribeService.saveOrUpdate(tempAutoResponse);
				systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			// 判断当前公众帐号是否已经配置欢迎语
			int size = subscribeService.findByProperty(Subscribe.class, "wechatId", wxWechat.getId()).size();
			// 如果集合不为0
			if (size != 0) {
				j.setSuccess(false);
				j.setMsg("每个公众帐号只能配置一个欢迎语。");
				return j;
			}
			subscribe.setCreateDate(new Date());
			String templateId = subscribe.getTemplateId();
			String msgType = subscribe.getMsgType();
			String templateName = "";
			if ("text".equals(msgType)) {
				TextTemplate textTemplate = this.subscribeService.getEntity(TextTemplate.class, templateId);
				if (textTemplate != null) {
					templateName = textTemplate.getTemplateName();
				}
			} else if ("news".equals(msgType)) {
				NewsTemplate newsTemplate = this.subscribeService.getEntity(NewsTemplate.class, templateId);
				if (newsTemplate != null) {
					templateName = newsTemplate.getTemplateName();
				}
			}
			subscribe.setTemplateId(templateId);
			subscribe.setTemplateName(templateName);
			subscribe.setWechatId(wxWechat.getId());
			this.subscribeService.save(subscribe);
		}
		return j;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
