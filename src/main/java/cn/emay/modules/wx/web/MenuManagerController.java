package cn.emay.modules.wx.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import weixin.popular.api.MenuAPI;
import weixin.popular.bean.BaseResult;
import weixin.popular.bean.menu.Button;
import weixin.popular.bean.menu.MenuButtons;
import cn.emay.framework.common.mapper.JsonMapper;
import cn.emay.framework.common.utils.LogUtil;
import cn.emay.framework.common.utils.StringUtil;
import cn.emay.framework.common.utils.StringUtils;
import cn.emay.framework.core.common.hibernate.qbc.CriteriaQuery;
import cn.emay.framework.core.common.model.json.AjaxJson;
import cn.emay.framework.core.common.model.json.DataGrid;
import cn.emay.framework.core.common.model.json.TreeGrid;
import cn.emay.framework.core.constant.Globals;
import cn.emay.framework.tag.vo.datatable.SortDirection;
import cn.emay.framework.tag.vo.easyui.TreeGridModel;
import cn.emay.modules.sys.service.SystemService;
import cn.emay.modules.wx.entity.MenuEntity;
import cn.emay.modules.wx.entity.NewsTemplate;
import cn.emay.modules.wx.entity.Templatemessage;
import cn.emay.modules.wx.entity.TextTemplate;
import cn.emay.modules.wx.entity.WxWechat;
import cn.emay.modules.wx.service.MenuEntityService;
import cn.emay.modules.wx.service.NewsTemplateService;
import cn.emay.modules.wx.service.TemplatemessageService;
import cn.emay.modules.wx.service.TextTemplateService;
import cn.emay.modules.wx.service.WxCacheService;



/**
 * 微信自定义菜单
 * @author lenovo
 *
 */
@Controller
@RequestMapping("/menuManagerController")
public class MenuManagerController {
	@Autowired
	private SystemService systemService;
	
	/**
	 * 微信缓存数据接口
	 */
	@Autowired
	private WxCacheService wxCacheService;
	
	/**
	 * 微信菜单service接口
	 */
	@Autowired
	private MenuEntityService menuEntityService;
	
	/**
	 * 文本模板消息service接口
	 */
	@Autowired
	private TextTemplateService textTemplateService;
	
	/**
	 * 图文模板消息service接口
	 */
	@Autowired
	private NewsTemplateService newsTemplateService;

	
	/**
	 * 模板消息service接口
	 */
	@Autowired
	private TemplatemessageService templatemessageService;
	
	
	private String message;

	
	@ModelAttribute("menuEntity")
	public MenuEntity get(@RequestParam(required=false) String id) {
		if (StringUtils.isNotBlank(id)){
			return menuEntityService.get(id);
		}else{
			return new MenuEntity();
		}
	}
	
	@RequestMapping(params = "list")
	public ModelAndView list() {
		return new ModelAndView("modules/weixin/menu/menulist");
	}

	@RequestMapping(params = "getSubMenu")
	public void getSubMenu(HttpServletRequest request, HttpServletResponse response) {
		WxWechat wxWechat=wxCacheService.getWxWechat();
		String accountid = wxWechat.getId();
		String resMsg = "";
		List<MenuEntity> textList =menuEntityService.findMenuEntity(accountid);
		resMsg=JsonMapper.getInstance().toJson(textList);
		try {
			response.setCharacterEncoding("utf-8");
			PrintWriter writer = response.getWriter();
			writer.write(resMsg);
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@RequestMapping(params = "gettemplate")
	public void gettemplate(HttpServletRequest request, HttpServletResponse response) {
		WxWechat wxWechat=wxCacheService.getWxWechat();
		String accountid = wxWechat.getId();
		String msgType = request.getParameter("msgType");
		String resMsg = "";
		/**
		 * 文本消息
		 */
		if ("text".equals(msgType)) {
			List<TextTemplate> textList =textTemplateService.findTextTemplate(accountid);
			resMsg =JsonMapper.getInstance().toJson(textList);
		} else if ("news".equals(msgType)) {
			List<NewsTemplate> newsList =newsTemplateService.findNewsTemplate(accountid);
			resMsg =JsonMapper.getInstance().toJson(newsList);
		} else if ("template".equals(msgType)) {
			List<Templatemessage> templatemessageList = templatemessageService.findTemplatemessage(accountid);
			resMsg =JsonMapper.getInstance().toJson(templatemessageList);
		} else if ("expand".equals(msgType)) {

		}
		try {
			response.setCharacterEncoding("utf-8");
			PrintWriter writer = response.getWriter();
			writer.write(resMsg);
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@RequestMapping(params = "datagrid")
	@ResponseBody
	public List<TreeGrid> datagrid(TreeGrid treegrid, HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		WxWechat wxWechat=wxCacheService.getWxWechat();
		String accountid = wxWechat.getId();
		CriteriaQuery cq = new CriteriaQuery(MenuEntity.class);
		cq.eq("accountId",accountid);
		if (treegrid.getId() != null) {
			cq.eq("menuEntity.id", treegrid.getId());
		} else {

			cq.isNull("menuEntity");
		}

		cq.addOrder("orders", SortDirection.asc);
		cq.add();

		List<MenuEntity> menuList = systemService.getListByCriteriaQuery(cq, false);
		List<TreeGrid> treeGrids = new ArrayList<TreeGrid>();
		TreeGridModel treeGridModel = new TreeGridModel();
		// treeGridModel.setIcon("orders");
		treeGridModel.setTextField("name");
		treeGridModel.setParentText("url");
		treeGridModel.setOrder("orders");
		treeGridModel.setSrc("type");
		treeGridModel.setIdField("id");
		treeGridModel.setChildList("menuList");
		// 添加排序字段
		treeGrids = systemService.treegrid(menuList, treeGridModel);
		return treeGrids;
	}

	@RequestMapping(params = "jumpSuView")
	public ModelAndView jumpSuView(MenuEntity menuEntity, HttpServletRequest req) {
		LogUtil.info("...menuEntity.getId()..." + menuEntity.getId());
		if (StringUtil.isNotEmpty(menuEntity.getId())) {
			if (menuEntity.getMenuEntity() != null && menuEntity.getMenuEntity().getId() != null) {
				req.setAttribute("fatherId", menuEntity.getMenuEntity().getId());
				req.setAttribute("fatherName", menuEntity.getMenuEntity().getName());
			}
			req.setAttribute("name", menuEntity.getName());
			req.setAttribute("type", menuEntity.getType());
			req.setAttribute("menuKey", menuEntity.getMenuKey());
			req.setAttribute("url", menuEntity.getUrl());
			req.setAttribute("orders", menuEntity.getOrders());
			req.setAttribute("templateId", menuEntity.getTemplateId());
			req.setAttribute("msgType", menuEntity.getMsgType());
		}
		String fatherId = req.getParameter("fatherId");
		if (StringUtil.isNotEmpty(fatherId)) {
			MenuEntity fatherMenuEntity = menuEntityService.get(fatherId);
			req.setAttribute("fatherId", fatherId);
			req.setAttribute("fatherName", fatherMenuEntity.getName());
			LogUtil.info(".....fatherName...." + fatherMenuEntity.getName());
		}
		return new ModelAndView("modules/weixin/menu/menuinfo");
	}

	@RequestMapping(params = "su")
	@ResponseBody
	public AjaxJson su(MenuEntity menuEntity, HttpServletRequest req, String fatherName) {
		AjaxJson j = new AjaxJson();
		if(StringUtils.isNotBlank(menuEntity.getId())){
			this.message ="菜单更新成功";
			menuEntityService.updateEntitie(menuEntity);
			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
		}else{
			if(menuEntityService.getMenuKey(menuEntity.getMenuKey())>0){
				this.message ="菜单标识已经存在了！";
				j.setMsg(message);
				j.setSuccess(false);
			}else{
				this.message = "添加" + menuEntity.getName() + "的信息成功！";
				if (StringUtil.isNotEmpty(fatherName)) {
					MenuEntity tempMenu =menuEntityService.get(fatherName);
					menuEntity.setMenuEntity(tempMenu);
				}
				menuEntity.setAccountId(wxCacheService.getWxWechat().getId());
				menuEntityService.save(menuEntity);
				systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
				j.setMsg(message);
			}
			
		}
		return j;
	}

	@RequestMapping(params = "jumpselect")
	public ModelAndView jumpselect() {
		return new ModelAndView("");
	}

	@RequestMapping(params = "del")
	@ResponseBody
	public AjaxJson del(MenuEntity menuEntity, HttpServletRequest req) {
		AjaxJson j = new AjaxJson();
		menuEntityService.delete(menuEntity);
		message = "删除" + menuEntity.getName() + "菜单信息数据";
		systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
		j.setMsg(this.message);
		return j;
	}

	@RequestMapping(params = "sameMenu")
	@ResponseBody
	public AjaxJson sameMenu(MenuEntity menuEntity, HttpServletRequest req) {
		WxWechat wxWechat=wxCacheService.getWxWechat();
		String accountid = wxWechat.getId();
		AjaxJson j = new AjaxJson();
		List<MenuEntity> menuList =menuEntityService.findChildMenuEntity(accountid);
		LogUtil.info(".....一级菜单的个数是....." + menuList.size());
		MenuButtons menuButtons=new MenuButtons();
		Button firstArr[] = new Button[menuList.size()];
		for (int a = 0; a < menuList.size(); a++) {
			MenuEntity entity = menuList.get(a);
			String hqls = "from MenuEntity where fatherid = '" + entity.getId() + "' and accountId = '" + accountid + "'  order by  orders asc";
			List<MenuEntity> childList = this.systemService.findByQueryString(hqls);
			/**
			 * 一级菜单
			 */
			if (childList.size() == 0) {
				Button button=new Button();
				button.setName(entity.getName());
				button.setType(entity.getType());
				if ("view".equals(entity.getType())) {
					button.setUrl(entity.getUrl());
					firstArr[a] = button;
				} else if ("click".equals(entity.getType())) {
					button.setKey(entity.getMenuKey());
					firstArr[a] = button;
				}

			} else {
				/**
				 * 一级菜单
				 */
				Button button=new Button();
				button.setName(entity.getName());
				button.setType(entity.getType());
				List<Button> subbt1 = new ArrayList<Button>();
				for (int i = 0; i < childList.size(); i++) {
					Button subbt1_1 = new Button();
					MenuEntity children = childList.get(i);
					String type = children.getType();
					subbt1_1.setName(children.getName());
					subbt1_1.setType(children.getType());
					if ("view".equals(type)) {
						subbt1_1.setUrl(children.getUrl());
						subbt1.add(subbt1_1);
					} else if ("click".equals(type)) {
						subbt1_1.setKey(children.getMenuKey());
						subbt1.add(subbt1_1);
					}
				}
				button.setSub_button(subbt1);
				firstArr[a] = button;
			}
		}
		
		menuButtons.setButton(firstArr);
		String accessToken = wxCacheService.getToken();
		BaseResult baseResult=MenuAPI.menuCreate(accessToken, menuButtons);
		if(baseResult.getErrcode().equals("0")){
			message = "同步菜单信息数据成功！";
		}else{
			message = "同步菜单信息数据失败！错误码为：" + baseResult.getErrcode() + "错误信息为：" + baseResult.getErrmsg();
		}
		systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
		j.setMsg(this.message);
		return j;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
