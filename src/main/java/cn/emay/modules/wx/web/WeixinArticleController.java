package cn.emay.modules.wx.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import cn.emay.framework.common.utils.DateUtils;
import cn.emay.framework.common.utils.MyBeanUtils;
import cn.emay.framework.common.utils.MyClassLoader;
import cn.emay.framework.common.utils.StringUtil;
import cn.emay.framework.common.utils.StringUtils;
import cn.emay.framework.common.utils.oConvertUtils;
import cn.emay.framework.core.common.controller.BaseController;
import cn.emay.framework.core.common.exception.BusinessException;
import cn.emay.framework.core.common.hibernate.qbc.CriteriaQuery;
import cn.emay.framework.core.common.model.common.UploadFile;
import cn.emay.framework.core.common.model.json.AjaxJson;
import cn.emay.framework.core.common.model.json.DataGrid;
import cn.emay.framework.core.constant.Globals;
import cn.emay.framework.core.extend.hqlsearch.HqlGenerateUtil;
import cn.emay.framework.tag.core.easyui.TagUtil;
import cn.emay.modules.sys.entity.Document;
import cn.emay.modules.sys.entity.Type;
import cn.emay.modules.sys.entity.Typegroup;
import cn.emay.modules.sys.service.SystemService;
import cn.emay.modules.wx.entity.NewsItem;
import cn.emay.modules.wx.entity.NewsTemplate;
import cn.emay.modules.wx.entity.WxWechat;
import cn.emay.modules.wx.service.NewsItemService;
import cn.emay.modules.wx.service.NewsTemplateService;
import cn.emay.modules.wx.service.WxCacheService;

/**
 * 微信图文-明细页面
 * 
 * @author zjlwm
 * 
 */
@Controller
@RequestMapping("/weixinArticleController")
public class WeixinArticleController extends BaseController {

	private static final Logger logger = Logger.getLogger(WeixinArticleController.class);

	/**
	 * 微信缓存数据接口
	 */
	@Autowired
	private WxCacheService wxCacheService;

	/**
	 * 图文消息模板service接口
	 */
	@Autowired
	private NewsItemService newsItemService;
	
	/**
	 * 图文消息模板接口servie接口
	 */
	@Autowired
	private NewsTemplateService newsTemplateService;

	@Autowired
	private SystemService systemService;

	private String message;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@ModelAttribute("weixinArticle")
	public NewsItem get(@RequestParam(required = false) String id) {
		if (StringUtils.isNotBlank(id)) {
			NewsItem newsItem=newsItemService.get(id);
			System.out.println("--"+newsItem.getNewsTemplateId()+"--"+newsItem.getCreateDate());
			return newsItem;
		} else {
			return new NewsItem();
		}
	}

	/**
	 * 微信单图消息列表 页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "goMessage")
	public ModelAndView goMessage(String templateId,HttpServletRequest request) {
		if (StringUtil.isNotEmpty(templateId)) {
			List<NewsItem> headerList = newsItemService.findNewsItemByNewsTemplate(templateId);
			if (headerList.size() > 0) {
				request.setAttribute("headerNews", headerList.get(0));
				if (headerList.size() > 1) {
					ArrayList<NewsItem> list = new ArrayList<NewsItem>(headerList);
					list.remove(0);
					request.setAttribute("newsList", list);
				}
			}
			NewsTemplate newsTemplate =newsTemplateService.get(templateId);
			String addtime=DateUtils.formatDate(newsTemplate.getCreateDate(), "yyyy年MM月dd日");
			request.setAttribute("addtime",addtime);
		}
		return new ModelAndView("modules/weixin/newstemplate/showmessage");
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
	public void datagrid(NewsItem weixinArticle, HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(NewsItem.class, dataGrid);
		WxWechat wxWechat = wxCacheService.getWxWechat();
		cq.eq("accountId", wxWechat.getId());
		// 查询条件组装器
		HqlGenerateUtil.installHql(cq, weixinArticle, request.getParameterMap());
		try {
			// 自定义追加查询条件
		} catch (Exception e) {
			logger.error("easyui AJAX请求数据", e);
			throw new BusinessException(e.getMessage());
		}
		cq.add();
		this.newsItemService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}

	/**
	 * 删除微信单图消息
	 * 
	 * @return
	 */
	@RequestMapping(params = "doDel")
	@ResponseBody
	public AjaxJson doDel(NewsItem weixinArticle, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		message = "微信单图消息删除成功";
		try {
			newsItemService.delete(weixinArticle);
			systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			message = "微信单图消息删除失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}

	/**
	 * 批量删除微信单图消息
	 * 
	 * @return
	 */
	@RequestMapping(params = "doBatchDel")
	@ResponseBody
	public AjaxJson doBatchDel(String ids, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		message = "微信单图消息删除成功";
		try {
			for (String id : ids.split(",")) {
				NewsItem weixinArticle = newsItemService.get(id);
				newsItemService.delete(weixinArticle);
				systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
			}
		} catch (Exception e) {
			e.printStackTrace();
			message = "微信单图消息删除失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}

	/**
	 * 添加微信单图消息
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "doAdd")
	@ResponseBody
	public AjaxJson doAdd(NewsItem weixinArticle, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		WxWechat wxWechat = wxCacheService.getWxWechat();
		message = "微信单图消息添加成功";
		try {
			String templateId = request.getParameter("templateId");
			weixinArticle.setNewsTemplateId(templateId);
			if (!"-1".equals(wxWechat.getId())) {
				newsItemService.save(weixinArticle);
			} else {
				j.setSuccess(false);
				j.setMsg("请添加一个公众帐号。");
			}
			systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			message = "微信单图消息添加失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}

	/**
	 * 更新微信单图消息
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "doUpdate")
	@ResponseBody
	public AjaxJson doUpdate(NewsItem weixinArticle, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		message = "微信单图消息更新成功";
		NewsItem t = newsItemService.get(NewsItem.class, weixinArticle.getId());
		try {
			MyBeanUtils.copyBeanNotNull2Bean(weixinArticle, t);
			newsItemService.update(t);
			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			message = "微信单图消息更新失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}

	/**
	 * 微信单图消息新增页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "goAdd")
	public ModelAndView goAdd(NewsItem weixinArticle, HttpServletRequest req) {
		String templateId = req.getParameter("templateId");
		req.setAttribute("templateId", templateId);
		if (StringUtil.isNotEmpty(weixinArticle.getId())) {
			weixinArticle = newsItemService.getEntity(NewsItem.class, weixinArticle.getId());
			req.setAttribute("weixinArticlePage", weixinArticle);
		}
		return new ModelAndView("modules/weixin/newstemplate/weixinArticle-add");
	}

	/**
	 * 微信单图消息编辑页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "goUpdate")
	public ModelAndView goUpdate(NewsItem weixinArticle, HttpServletRequest req) {
		req.setAttribute("weixinArticle", weixinArticle);
		return new ModelAndView("modules/weixin/newstemplate/weixinArticle-update");
	}

	/**
	 * 保存文件信息
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(params = "upload", method = RequestMethod.POST)
	@ResponseBody
	public AjaxJson upload(MultipartHttpServletRequest request, HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		Map<String, Object> attributes = new HashMap<String, Object>();
		Typegroup typegroup = systemService.getTypeGroup("fieltype", "文档分类");
		Type tsType = systemService.getType("files", "附件", typegroup);
		String fileKey = oConvertUtils.getString(request.getParameter("fileKey"));// 文件ID
		String documentTitle = oConvertUtils.getString(request.getParameter("documentTitle"));// 文件标题
		Document document = new Document();
		if (StringUtil.isNotEmpty(fileKey)) {
			document.setId(fileKey);
			document = systemService.getEntity(Document.class, fileKey);
			document.setDocumentTitle(documentTitle);

		}
		document.setSubclassname(MyClassLoader.getPackPath(document));
		document.setCreatedate(DateUtils.getTimestamp());
		document.setType(tsType);
		UploadFile uploadFile = new UploadFile(request, document);
		uploadFile.setCusPath("files");
		uploadFile.setSwfpath("swfpath");
		document = systemService.uploadFile(uploadFile);
		attributes.put("url", document.getRealpath());
		attributes.put("fileKey", document.getId());
		attributes.put("name", document.getAttachmenttitle());
		attributes.put("viewhref", "commonController.do?openViewFile&fileid=" + document.getId());
		attributes.put("delurl", "commonController.do?delObjFile&fileKey=" + document.getId());
		j.setMsg("文件添加成功");
		j.setAttributes(attributes);

		return j;
	}
}
