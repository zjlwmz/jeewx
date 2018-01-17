package cn.emay.modules.wx.web;

import java.util.Date;
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

import cn.emay.framework.common.utils.MyBeanUtils;
import cn.emay.framework.common.utils.StringUtil;
import cn.emay.framework.common.utils.StringUtils;
import cn.emay.framework.core.common.exception.BusinessException;
import cn.emay.framework.core.common.hibernate.qbc.CriteriaQuery;
import cn.emay.framework.core.common.model.json.AjaxJson;
import cn.emay.framework.core.common.model.json.DataGrid;
import cn.emay.framework.core.constant.Globals;
import cn.emay.framework.core.extend.hqlsearch.HqlGenerateUtil;
import cn.emay.framework.tag.core.easyui.TagUtil;
import cn.emay.modules.sys.service.SystemService;
import cn.emay.modules.wx.entity.NewsItem;
import cn.emay.modules.wx.entity.NewsTemplate;
import cn.emay.modules.wx.entity.WxWechat;
import cn.emay.modules.wx.service.NewsItemService;
import cn.emay.modules.wx.service.NewsTemplateService;
import cn.emay.modules.wx.service.WxCacheService;

/**
 * 图文消息
 * 
 */
@Controller
@RequestMapping("/newsTemplateController")
public class NewsTemplateController {

	private static final Logger logger = Logger.getLogger(NewsTemplateController.class);
	
	/**
	 * 图文消息模板接口servie接口
	 */
	@Autowired
	private NewsTemplateService newsTemplateService;

	@Autowired
	private NewsItemService newsItemService;

	@Autowired
	private SystemService systemService;

	/**
	 * 微信缓存数据接口
	 */
	@Autowired
	private WxCacheService wxCacheService;

	private String message;
	
	
	
	@ModelAttribute("newsTemplate")
	public NewsTemplate get(@RequestParam(required=false) String id) {
		if (StringUtils.isNotBlank(id)){
			return newsTemplateService.get(id);
		}else{
			return new NewsTemplate();
		}
	}
	

	/**
	 * 列表展示
	 * 
	 * @return
	 */
	@RequestMapping(params = "list")
	public ModelAndView list() {
		return new ModelAndView("modules/weixin/newstemplate/newsTemplateList");
	}

	
	/**
	 * 查询数据
	 * @param newsTemplate
	 * @param request
	 * @param response
	 * @param dataGrid
	 */
	@RequestMapping(params = "datagrid")
	@ResponseBody
	public void datagrid(NewsTemplate newsTemplate, HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(NewsTemplate.class, dataGrid);
		HqlGenerateUtil.installHql(cq, newsTemplate, request.getParameterMap());
		WxWechat wxWechat=wxCacheService.getWxWechat();
		cq.eq("wechatId", wxWechat.getId());
		try {
			// 自定义追加查询条件
		} catch (Exception e) {
			logger.error("微信粉丝easyuiAJAX", e);
			throw new BusinessException(e.getMessage());
		}
		cq.add();
		newsTemplateService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}

	/**
	 * 删除信息模板
	 * 
	 * @param newsTemplate
	 * @param req
	 * @return
	 */
	@RequestMapping(params = "del")
	@ResponseBody
	public AjaxJson del(NewsTemplate newsTemplate, HttpServletRequest req) {
		AjaxJson ajaxJson = new AjaxJson();
		newsTemplate = this.newsTemplateService.getEntity(NewsTemplate.class, newsTemplate.getId());
		newsTemplateService.delete(newsTemplate);
		message = "删除信息数据成功！";
		systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
		ajaxJson.setMsg(this.message);
		return ajaxJson;
	}

	/**
	 * 批量删除图文消息
	 * 
	 * @return
	 */
	@RequestMapping(params = "doBatchDel")
	@ResponseBody
	public AjaxJson doBatchDel(String ids, HttpServletRequest request) {
		AjaxJson ajaxJson = new AjaxJson();
		message = "删除信息数据成功";
		int succeed = 0;
		int error = 0;
		try {
			for (String id : ids.split(",")) {
				NewsTemplate newsTemplate = this.newsTemplateService.getEntity(NewsTemplate.class, id);
				this.newsTemplateService.delete(newsTemplate);
				succeed += 1;
				systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
			}
		} catch (Exception e) {
			e.printStackTrace();
			error += 1;
			message = "删除信息数据失败";
			throw new BusinessException(e.getMessage());
		}
		message = "删除信息数据成功" + succeed + "条，失败" + error + "条";
		ajaxJson.setMsg(message);
		return ajaxJson;
	}

	/**
	 * 跳转到信息模板
	 * 
	 * @param req
	 * @return
	 */
	@RequestMapping(params = "goSuView")
	public ModelAndView goSuView(HttpServletRequest req) {
		String id = req.getParameter("id");
		req.setAttribute("id", id);
		if (StringUtil.isNotEmpty(id)) {
			NewsTemplate newsTemplate = newsTemplateService.getEntity(NewsTemplate.class, id);
			String type = newsTemplate.getType();
			req.setAttribute("type", type);
			req.setAttribute("tempateName", newsTemplate.getTemplateName());
		}
		return new ModelAndView("modules/weixin/newstemplate/newsTemplateInfo");
	}

	@RequestMapping(params = "doSave")
	@ResponseBody
	public AjaxJson doSave(NewsTemplate newsTemplate, HttpServletRequest req) {
		AjaxJson ajax = new AjaxJson();
		try{
			String id = newsTemplate.getId();
			if (StringUtil.isNotEmpty(id)) {
				newsTemplate.setUpdateDate(new Date());
			} else {
				newsTemplate.setCreateDate(new Date());
				newsTemplate.setUpdateDate(newsTemplate.getCreateDate());
				WxWechat wxWechat=wxCacheService.getWxWechat();
				newsTemplate.setWechatId(wxWechat.getId());
			}
			newsTemplateService.save(newsTemplate);
			ajax.setMsg("图文消息消息保存成功");
		}catch (Exception e) {
			ajax.setSuccess(false);
			ajax.setMsg("保存异常");
			e.printStackTrace();
		}
		return ajax;
	}

	/**
	 * 跳转到消息模板
	 * 
	 * @param req
	 * @return
	 */
	@RequestMapping(params = "goNewsItem")
	public ModelAndView goNewsItem(HttpServletRequest req) {
		String templateId = req.getParameter("templateId");
		req.setAttribute("templateId", templateId);

		if (StringUtil.isNotEmpty(templateId)) {
			NewsTemplate newsTemplate = newsTemplateService.getEntity(NewsTemplate.class, templateId);
			req.setAttribute("type", newsTemplate.getType());
		}

		String id = req.getParameter("id");
		req.setAttribute("id", id);
		if (StringUtil.isNotEmpty(id)) {
			NewsItem newsItem = newsTemplateService.getEntity(NewsItem.class, id);
			req.setAttribute("title", newsItem.getTitle());
			req.setAttribute("content", newsItem.getContent());
			req.setAttribute("author", newsItem.getAuthor());
			req.setAttribute("imagePath", newsItem.getImagePath());
			req.setAttribute("description", newsItem.getDescription());
			NewsTemplate newsTemplate=newsTemplateService.get(newsItem.getNewsTemplateId());
			req.setAttribute("templateId", newsItem.getNewsTemplateId());
			req.setAttribute("type", newsTemplate.getType());
			req.setAttribute("orders", newsItem.getOrders());
		} else {
			// 判断是否是头一条短信
			List<NewsItem> newsItemList = this.newsTemplateService.findByProperty(NewsItem.class, "newsTemplateId", templateId);
			req.setAttribute("orders", (newsItemList.size() + 1));
		}

		return new ModelAndView("modules/weixin/newstemplate/itemInfo");
	}

	@RequestMapping(params = "jumpupload")
	public ModelAndView jumpUpload(HttpServletRequest req) {
		return new ModelAndView("modules/weixin/newstemplate/upload");
	}

	/**
	 * 保存新增关键字
	 * 
	 * @param newsItem
	 * @param req
	 * @return
	 */
	@RequestMapping(params = "saveNewsTemplate")
	@ResponseBody
	public AjaxJson saveNewsTemplate(NewsItem newsItem, HttpServletRequest req) {
		AjaxJson ajaxJson = new AjaxJson();
		String id = newsItem.getId();
		if (StringUtil.isNotEmpty(id)) {

			NewsItem tempAutoResponse = this.newsTemplateService.getEntity(NewsItem.class, newsItem.getId());
			this.message = "修改关键字回复成功！";
			try {
				MyBeanUtils.copyBeanNotNull2Bean(newsItem, tempAutoResponse);
				newsItemService.saveOrUpdate(tempAutoResponse);
				systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
			} catch (Exception e) {
				e.printStackTrace();
			}

		} else {
			String templateId = req.getParameter("templateId");
			newsItem.setNewsTemplateId(templateId);
			newsItemService.save(newsItem);
		}
		return ajaxJson;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
