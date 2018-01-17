package cn.emay.modules.wx.web;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import cn.emay.framework.common.utils.StringUtils;
import cn.emay.framework.core.common.exception.BusinessException;
import cn.emay.framework.core.common.hibernate.qbc.CriteriaQuery;
import cn.emay.framework.core.common.model.json.AjaxJson;
import cn.emay.framework.core.common.model.json.DataGrid;
import cn.emay.framework.core.constant.Globals;
import cn.emay.framework.core.extend.hqlsearch.HqlGenerateUtil;
import cn.emay.framework.tag.core.easyui.TagUtil;
import cn.emay.modules.sys.service.SystemService;
import cn.emay.modules.wx.entity.TextTemplate;
import cn.emay.modules.wx.service.TextTemplateService;
import cn.emay.modules.wx.service.WxCacheService;


/**
 * 文本消息
 * 
 */
@Controller
@RequestMapping("/textTemplateController")
public class TextTemplateController {
	
	/**
	 * 文本模板消息接口
	 */
	@Autowired
	private TextTemplateService textTemplateService;
	
	
	/**
	 * 微信缓存数据接口
	 */
	@Autowired
	private WxCacheService wxCacheService;
	
	
	@Autowired
	private SystemService systemService;
	private String message;

	
	@ModelAttribute("textTemplate")
	public TextTemplate get(@RequestParam(required=false) String id) {
		if (StringUtils.isNotBlank(id)){
			return textTemplateService.get(id);
		}else{
			return new TextTemplate();
		}
	}
	
	
	
	/**
	 * 转向消息自动回复模板
	 * 
	 * @return
	 */
	@RequestMapping(params = "list")
	public ModelAndView list() {
		return new ModelAndView("modules/weixin/texttemplate/textTemplateList");
	}

	/**
	 * 查询信息列表
	 * 
	 * @param textTemplate
	 * @param request
	 * @param response
	 * @param dataGrid
	 */
	@RequestMapping(params = "datagrid")
	@ResponseBody
	public void datagrid(TextTemplate textTemplate, HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(TextTemplate.class, dataGrid);
		cq.eq("wechatId", wxCacheService.getWxWechat().getId());
		HqlGenerateUtil.installHql(cq, textTemplate);

		textTemplateService.getDataGridReturn(cq, true);

		TagUtil.datagrid(response, dataGrid);
	}

	/**
	 * 删除信息
	 * 
	 * @param textTemplate
	 * @param req
	 * @return
	 */
	@RequestMapping(params = "del")
	@ResponseBody
	public AjaxJson del(TextTemplate textTemplate, HttpServletRequest req) {
		AjaxJson j = new AjaxJson();
		textTemplateService.delete(textTemplate);
		message = "删除信息数据成功！";
		systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
		j.setMsg(this.message);
		return j;
	}

	/**
	 * 批量删除文本消息
	 * 
	 * @return
	 */
	@RequestMapping(params = "doBatchDel")
	@ResponseBody
	public AjaxJson doBatchDel(String ids, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		message = "删除信息数据成功";
		int succeed = 0;
		int error = 0;
		try {
			for (String id : ids.split(",")) {
				TextTemplate textTemplate =textTemplateService.get(id);
				textTemplateService.delete(textTemplate);
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
		j.setMsg(message);
		return j;
	}

	/**
	 * 添加或修改消息页面跳转
	 * 
	 * @param req
	 * @return
	 */
	@RequestMapping(params = "addorUpdate")
	public ModelAndView addorUpdate(TextTemplate textTemplate,HttpServletRequest req) {
		req.setAttribute("textTemplate", textTemplate);
		return new ModelAndView("modules/weixin/texttemplate/textTemplateInfo");
	}

	/**
	 * 保存文本模板修改
	 * 
	 * @param textTemplate
	 * @param req
	 * @return
	 */
	@RequestMapping(params = "doSave")
	@ResponseBody
	public AjaxJson doSave(TextTemplate textTemplate, HttpServletRequest req) {

		AjaxJson j = new AjaxJson();
		String id = textTemplate.getId();
		if (StringUtils.isNotEmpty(id)) {
			textTemplateService.updateEntitie(textTemplate);
			this.message = "修改关文本模板成功！";
			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
		} else {
			String wechatId = wxCacheService.getWxWechat().getId();
			textTemplate.setWechatId(wechatId);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			textTemplate.setCreateDate(sdf.format(new Date()));
			textTemplateService.save(textTemplate);
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
