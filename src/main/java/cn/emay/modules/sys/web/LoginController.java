package cn.emay.modules.sys.web;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import cn.emay.framework.common.utils.CacheUtils;
import cn.emay.framework.common.utils.ContextHolderUtils;
import cn.emay.framework.common.utils.IpUtil;
import cn.emay.framework.common.utils.MenuUtils;
import cn.emay.framework.common.utils.SysThemesUtil;
import cn.emay.framework.common.utils.oConvertUtils;
import cn.emay.framework.core.common.controller.BaseController;
import cn.emay.framework.core.common.model.json.AjaxJson;
import cn.emay.framework.core.constant.Globals;
import cn.emay.framework.core.enums.SysThemesEnum;
import cn.emay.modules.sys.entity.Client;
import cn.emay.modules.sys.entity.Config;
import cn.emay.modules.sys.entity.Depart;
import cn.emay.modules.sys.entity.Function;
import cn.emay.modules.sys.entity.Role;
import cn.emay.modules.sys.entity.RoleUser;
import cn.emay.modules.sys.entity.User;
import cn.emay.modules.sys.manager.ClientManager;
import cn.emay.modules.sys.service.MutiLangService;
import cn.emay.modules.sys.service.ParamsService;
import cn.emay.modules.sys.service.SystemService;
import cn.emay.modules.sys.service.UserService;
import cn.emay.modules.sys.utils.UserCacheUtils;

import com.google.common.collect.Maps;

/**
 * 登陆初始化控制器
 * 
 * @author 张代浩
 * 
 */
@Controller
@RequestMapping("/loginController")
public class LoginController extends BaseController {

	@SuppressWarnings("unused")
	private Logger log = Logger.getLogger(LoginController.class);

	/**
	 * 系统service接口
	 */
	@Autowired
	private SystemService systemService;

	/**
	 * 用户service接口
	 */
	@Autowired
	private UserService userService;

	/**
	 * 多语言service接口
	 */
	@Autowired
	private MutiLangService mutiLangService;
	
	@Autowired
	private ParamsService paramsService;

	/**
	 * 初始化
	 * 
	 * @return
	 */
	@RequestMapping(params = "goPwdInit")
	public String goPwdInit() {
		return "login/pwd_init";
	}

	/**
	 * 检查用户名称
	 * 
	 * @param user
	 * @param req
	 * @return
	 */
	@RequestMapping(params = "checkuser")
	@ResponseBody
	public AjaxJson checkuser(User user, HttpServletRequest req) {
		HttpSession session = req.getSession();
		AjaxJson ajaxJson = new AjaxJson();
		// 语言选择
		if (req.getParameter("langCode") != null) {
			req.getSession().setAttribute("lang", req.getParameter("langCode"));
		} else {
			req.getSession().setAttribute("lang", "zh-cn");
		}
		// 验证码
		String randCode = req.getParameter("randCode");
		if (StringUtils.isEmpty(randCode)) {
			ajaxJson.setMsg(mutiLangService.getLang("common.enter.verifycode"));
			ajaxJson.setSuccess(false);
		} else if (!randCode.equalsIgnoreCase(String.valueOf(session.getAttribute("randCode")))) {
			ajaxJson.setMsg(mutiLangService.getLang("common.verifycode.error"));
			ajaxJson.setSuccess(false);
		} else {
			// 用户登录验证逻辑
			User u = userService.checkUserExits(user);
			if (u == null) {
				ajaxJson.setMsg(mutiLangService.getLang("common.username.or.password.error"));
				ajaxJson.setSuccess(false);
				return ajaxJson;
			}
			if (u != null && u.getStatus() != 0) {
				// 处理用户有多个组织机构的情况，以弹出框的形式让用户选择
				Map<String, Object> attrMap = new HashMap<String, Object>();
				ajaxJson.setAttributes(attrMap);

				String orgId = req.getParameter("orgId");
				if (StringUtils.isEmpty(orgId)) {
					// 没有传组织机构参数，则获取当前用户的组织机构
					Long orgNum = userService.getOrgNum(u.getId());
					if (orgNum > 1) {
						attrMap.put("orgNum", orgNum);
						attrMap.put("user", u);
					} else {
						Map<String, Object> userOrgMap = userService.getUserOrgId(u.getId());
						saveLoginSuccessInfo(req, u, (String) userOrgMap.get("orgId"));
					}
				} else {
					attrMap.put("orgNum", 1);
					saveLoginSuccessInfo(req, u, orgId);
				}
			} else {
				ajaxJson.setMsg(mutiLangService.getLang("common.username.or.password.error"));
				ajaxJson.setSuccess(false);
			}
		}
		return ajaxJson;
	}

	/**
	 * 变更在线用户组织
	 * 
	 * @param user
	 * @param req
	 * @return
	 */
	@RequestMapping(params = "changeDefaultOrg")
	@ResponseBody
	public AjaxJson changeDefaultOrg(User user, HttpServletRequest req) {
		AjaxJson j = new AjaxJson();
		Map<String, Object> attrMap = new HashMap<String, Object>();
		String orgId = req.getParameter("orgId");
		User u = userService.checkUserExits(user);
		if (StringUtils.isNotEmpty(orgId)) {
			attrMap.put("orgNum", 1);
			saveLoginSuccessInfo(req, u, orgId);
		}
		return j;
	}

	/**
	 * 保存用户登录的信息，并将当前登录用户的组织机构赋值到用户实体中；
	 * 
	 * @param req
	 *            request
	 * @param user
	 *            当前登录用户
	 * @param orgId
	 *            组织主键
	 */
	private void saveLoginSuccessInfo(HttpServletRequest req, User user, String orgId) {
		String message = null;
		Depart currentDepart = systemService.get(Depart.class, orgId);
		user.setCurrentDepart(currentDepart);

		HttpSession session = ContextHolderUtils.getSession();
		session.setAttribute(UserCacheUtils.LOCAL_CLINET_USER, user);
		message = mutiLangService.getLang("common.user") + ": " + user.getUserName() + "[" + currentDepart.getDepartname() + "]" + mutiLangService.getLang("common.login.success");
		// 当前session为空 或者 当前session的用户信息与刚输入的用户信息一致时，则更新Client信息
		Client clientOld = ClientManager.getInstance().getClient(session.getId());
		if (clientOld == null || clientOld.getUser() == null || user.getUserName().equals(clientOld.getUser().getUserName())) {
			Client client = new Client();
			client.setIp(IpUtil.getIpAddr(req));
			client.setLogindatetime(new Date());
			client.setUser(user);
			ClientManager.getInstance().addClinet(session.getId(), client);
		} else {
			// 如果不一致，则注销session并通过session=req.getSession(true)初始化session
			ClientManager.getInstance().removeClinet(session.getId());
			session.invalidate();
			session = req.getSession(true);// session初始化
			session.setAttribute(UserCacheUtils.LOCAL_CLINET_USER, user);
			session.setAttribute("randCode", req.getParameter("randCode"));// 保存验证码
			checkuser(user, req);
		}

		// 添加登陆日志
		systemService.addLog(message, Globals.Log_Type_LOGIN, Globals.Log_Leavel_INFO);
	}

	/**
	 * 用户登录
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "login")
	public String login(ModelMap modelMap, HttpServletRequest request, HttpServletResponse response) {
		User user = UserCacheUtils.getCurrentUser();
		String roles = "";
		if (user != null) {
			List<RoleUser> rUsers = systemService.findByProperty(RoleUser.class, "user.id", user.getId());
			for (RoleUser ru : rUsers) {
				Role role = ru.getRole();
				roles += role.getRoleName() + ",";
			}
			if (roles.length() > 0) {
				roles = roles.substring(0, roles.length() - 1);
			}
			modelMap.put("roleName", roles);
			modelMap.put("userName", user.getUserName());

			modelMap.put("userid", user.getId());
			modelMap.put("currentOrgName", ClientManager.getInstance().getClient().getUser().getCurrentDepart().getDepartname());

			request.getSession().setAttribute("CKFinder_UserRole", "admin");

			SysThemesEnum sysTheme = SysThemesUtil.getSysTheme(request);
			if ("ace".equals(sysTheme.getStyle()) || "diy".equals(sysTheme.getStyle()) || "acele".equals(sysTheme.getStyle()) || "hplus".equals(sysTheme.getStyle())) {

				/**
				 * 用户功能权限
				 */
				request.setAttribute("menuMap", userService.getFunctionMap(user));
			}

			Cookie cookie = new Cookie("JEECGINDEXSTYLE", sysTheme.getStyle());
			// 设置cookie有效期为一个月
			cookie.setMaxAge(3600 * 24 * 30);
			response.addCookie(cookie);

			Cookie zIndexCookie = new Cookie("ZINDEXNUMBER", "1990");
			zIndexCookie.setMaxAge(3600 * 24);// 一天
			response.addCookie(zIndexCookie);
			
			if (roles.contains("客服")) {
				String socketUrl=paramsService.findParamsByName("wx.cus.socket.url");
				modelMap.addAttribute("showIm", true);
				modelMap.addAttribute("socketUrl", socketUrl);
			}
/*
			if (roles.contains("客服")) {
				 return "modules/weixin/wxonline/myIm";
//				return "redirect:/webImController.do?myIm";
			} else {
				return sysTheme.getIndexPath();
			}
			*/
			 return sysTheme.getIndexPath();
		} else {
			return "login/login3";
		}

	}

	/**
	 * 退出系统
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "logout")
	public ModelAndView logout(HttpServletRequest request) {
		HttpSession session = ContextHolderUtils.getSession();
		User user = UserCacheUtils.getCurrentUser();
		systemService.addLog("用户" + user.getUserName() + "已退出", Globals.Log_Type_EXIT, Globals.Log_Leavel_INFO);
		ClientManager.getInstance().removeClinet(session.getId());
		session.invalidate();
		ModelAndView modelAndView = new ModelAndView(new RedirectView("loginController.do?login"));
		return modelAndView;
	}

	/**
	 * 菜单跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "left")
	public ModelAndView left(HttpServletRequest request) {
		User user = UserCacheUtils.getCurrentUser();
		HttpSession session = ContextHolderUtils.getSession();
		ModelAndView modelAndView = new ModelAndView();
		// 登陆者的权限
		if (user.getId() == null) {
			session.removeAttribute(Globals.USER_SESSION);
			modelAndView.setView(new RedirectView("loginController.do?login"));
		} else {
			List<Config> configs = userService.loadAll(Config.class);
			for (Config config : configs) {
				request.setAttribute(config.getCode(), config.getContents());
			}
			modelAndView.setViewName("main/left");
			request.setAttribute("menuMap", userService.getFunctionMap(user));
		}
		return modelAndView;
	}

	/**
	 * 首页跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "home")
	public ModelAndView home(HttpServletRequest request) {
		SysThemesEnum sysTheme = SysThemesUtil.getSysTheme(request);
		// ACE ACE2 DIY时需要在home.jsp头部引入依赖的js及css文件
		if ("ace".equals(sysTheme.getStyle()) || "diy".equals(sysTheme.getStyle()) || "acele".equals(sysTheme.getStyle())) {
			request.setAttribute("show", "1");
		} else {// default及shortcut不需要引入依赖文件，所有需要屏蔽
			request.setAttribute("show", "0");
		}
		return new ModelAndView("main/home");
	}

	/**
	 * ACE首页跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "acehome")
	public ModelAndView acehome(HttpServletRequest request) {

		SysThemesEnum sysTheme = SysThemesUtil.getSysTheme(request);
		// ACE ACE2 DIY时需要在home.jsp头部引入依赖的js及css文件
		if ("ace".equals(sysTheme.getStyle()) || "diy".equals(sysTheme.getStyle()) || "acele".equals(sysTheme.getStyle())) {
			request.setAttribute("show", "1");
		} else {// default及shortcut不需要引入依赖文件，所有需要屏蔽
			request.setAttribute("show", "0");
		}

		return new ModelAndView("main/acehome");
	}

	/**
	 * HPLUS首页跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "hplushome")
	public ModelAndView hplushome(HttpServletRequest request) {

		// SysThemesEnum sysTheme = SysThemesUtil.getSysTheme(request);
		// ACE ACE2 DIY时需要在home.jsp头部引入依赖的js及css文件
		/*
		 * if("ace".equals(sysTheme.getStyle())||"diy".equals(sysTheme.getStyle()
		 * )||"acele".equals(sysTheme.getStyle())){ request.setAttribute("show",
		 * "1"); } else {//default及shortcut不需要引入依赖文件，所有需要屏蔽
		 * request.setAttribute("show", "0"); }
		 */
		return new ModelAndView("main/hplushome");
		// return new ModelAndView("modules/weixin/wxonline/myIm");
	}

	/**
	 * 无权限页面提示跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "noAuth")
	public ModelAndView noAuth(HttpServletRequest request) {
		return new ModelAndView("common/noAuth");
	}

	/**
	 * @Title: top
	 * @Description: bootstrap头部菜单请求
	 * @param request
	 * @return ModelAndView
	 * @throws
	 */
	@RequestMapping(params = "top")
	public ModelAndView top(HttpServletRequest request) {
		User user = UserCacheUtils.getCurrentUser();
		HttpSession session = ContextHolderUtils.getSession();
		// 登陆者的权限
		if (user.getId() == null) {
			session.removeAttribute(Globals.USER_SESSION);
			return new ModelAndView(new RedirectView("loginController.do?login"));
		}
		request.setAttribute("menuMap", userService.getFunctionMap(user));
		List<Config> configs = userService.loadAll(Config.class);
		for (Config config : configs) {
			request.setAttribute(config.getCode(), config.getContents());
		}
		return new ModelAndView("main/bootstrap_top");
	}

	/**
	 * @Title: top
	 * @author gaofeng
	 * @Description: shortcut头部菜单请求
	 * @param request
	 * @return ModelAndView
	 * @throws
	 */
	@RequestMapping(params = "shortcut_top")
	public ModelAndView shortcut_top(HttpServletRequest request) {
		User user = UserCacheUtils.getCurrentUser();
		HttpSession session = ContextHolderUtils.getSession();
		// 登陆者的权限
		if (user.getId() == null) {
			session.removeAttribute(Globals.USER_SESSION);
			return new ModelAndView(new RedirectView("loginController.do?login"));
		}
		request.setAttribute("menuMap", userService.getFunctionMap(user));
		List<Config> configs = userService.loadAll(Config.class);
		for (Config config : configs) {
			request.setAttribute(config.getCode(), config.getContents());
		}
		return new ModelAndView("main/shortcut_top");
	}

	/**
	 * @Title: top
	 * @author:gaofeng
	 * @Description: shortcut头部菜单一级菜单列表，并将其用ajax传到页面，实现动态控制一级菜单列表
	 * @return AjaxJson
	 * @throws
	 */
	@RequestMapping(params = "primaryMenu")
	@ResponseBody
	public String getPrimaryMenu() {
		List<Function> primaryMenu = userService.getFunctionMap(UserCacheUtils.getCurrentUser()).get(0);
		String floor = "";

		if (primaryMenu == null) {
			return floor;
		}

		for (Function function : primaryMenu) {
			if (function.getFunctionLevel() == 0) {
				String lang_key = function.getFunctionName();
				String lang_context = mutiLangService.getLang(lang_key);
				lang_context = lang_context.trim();

				if ("业务申请".equals(lang_context)) {

					String ss = "<div style='width:67px;position: absolute;top:40px;text-align:center;color:#909090;font-size:12px;'><span style='letter-spacing:-1px;'>" + lang_context + "</span></div>";
					floor += " <li style='position: relative;'><img class='imag1' src='static/login/images/ywsq.png' /> " + " <img class='imag2' src='static/login/images/ywsq-up.png' style='display: none;' />" + ss + " </li> ";
				} else if ("个人办公".equals(lang_context)) {

					String ss = "<div style='width:67px;position: absolute;top:40px;text-align:center;color:#909090;font-size:12px;'><span style='letter-spacing:-1px;'>" + lang_context + "</span></div>";
					floor += " <li style='position: relative;'><img class='imag1' src='static/login/images/grbg.png' /> " + " <img class='imag2' src='static/login/images/grbg-up.png' style='display: none;' />" + ss + " </li> ";
				} else if ("流程管理".equals(lang_context)) {

					String ss = "<div style='width:67px;position: absolute;top:40px;text-align:center;color:#909090;font-size:12px;'><span style='letter-spacing:-1px;'>" + lang_context + "</span></div>";
					floor += " <li style='position: relative;'><img class='imag1' src='static/login/images/lcsj.png' /> " + " <img class='imag2' src='static/login/images/lcsj-up.png' style='display: none;' />" + ss + " </li> ";
				} else if ("Online 开发".equals(lang_context)) {

					floor += " <li><img class='imag1' src='static/login/images/online.png' /> " + " <img class='imag2' src='static/login/images/online_up.png' style='display: none;' />" + " </li> ";
				} else if ("自定义表单".equals(lang_context)) {

					String ss = "<div style='width:67px;position: absolute;top:40px;text-align:center;color:#909090;font-size:12px;'><span style='letter-spacing:-1px;'>" + lang_context + "</span></div>";
					floor += " <li style='position: relative;'><img class='imag1' src='static/login/images/zdybd.png' /> " + " <img class='imag2' src='static/login/images/zdybd-up.png' style='display: none;' />" + ss + " </li> ";
				} else if ("系统监控".equals(lang_context)) {

					floor += " <li><img class='imag1' src='static/login/images/xtjk.png' /> " + " <img class='imag2' src='static/login/images/xtjk_up.png' style='display: none;' />" + " </li> ";
				} else if ("统计报表".equals(lang_context)) {

					floor += " <li><img class='imag1' src='static/login/images/tjbb.png' /> " + " <img class='imag2' src='static/login/images/tjbb_up.png' style='display: none;' />" + " </li> ";
				} else if ("消息中间件".equals(lang_context)) {
					String ss = "<div style='width:67px;position: absolute;top:40px;text-align:center;color:#909090;font-size:12px;'><span style='letter-spacing:-1px;'>" + lang_context + "</span></div>";
					floor += " <li style='position: relative;'><img class='imag1' src='static/login/images/msg.png' /> " + " <img class='imag2' src='static/login/images/msg_up.png' style='display: none;' />" + ss + " </li> ";
				} else if ("系统管理".equals(lang_context)) {

					floor += " <li><img class='imag1' src='static/login/images/xtgl.png' /> " + " <img class='imag2' src='static/login/images/xtgl_up.png' style='display: none;' />" + " </li> ";
				} else if ("常用示例".equals(lang_context)) {

					floor += " <li><img class='imag1' src='static/login/images/cysl.png' /> " + " <img class='imag2' src='static/login/images/cysl_up.png' style='display: none;' />" + " </li> ";
				} else if (lang_context.contains("消息推送")) {

					String s = "<div style='width:67px;position: absolute;top:40px;text-align:center;color:#909090;font-size:12px;'>消息推送</div>";
					floor += " <li style='position: relative;'><img class='imag1' src='static/login/images/msg.png' /> " + " <img class='imag2' src='static/login/images/msg_up.png' style='display: none;' />" + s + "</li> ";
				} else {
					// 其他的为默认通用的图片模式
					String s = "";
					if (lang_context.length() >= 5 && lang_context.length() < 7) {
						s = "<div style='width:67px;position: absolute;top:40px;text-align:center;color:#909090;font-size:12px;'><span style='letter-spacing:-1px;'>" + lang_context + "</span></div>";
					} else if (lang_context.length() < 5) {
						s = "<div style='width:67px;position: absolute;top:40px;text-align:center;color:#909090;font-size:12px;'>" + lang_context + "</div>";
					} else if (lang_context.length() >= 7) {
						s = "<div style='width:67px;position: absolute;top:40px;text-align:center;color:#909090;font-size:12px;'><span style='letter-spacing:-1px;'>" + lang_context.substring(0, 6) + "</span></div>";
					}
					floor += " <li style='position: relative;'><img class='imag1' src='static/login/images/default.png' /> " + " <img class='imag2' src='static/login/images/default_up.png' style='display: none;' />" + s + "</li> ";
				}
			}
		}

		return floor;
	}

	/**
	 * @Title: top
	 * @author:wangkun
	 * @Description: shortcut头部菜单二级菜单列表，并将其用ajax传到页面，实现动态控制二级菜单列表
	 * @return AjaxJson
	 * @throws
	 */
	@RequestMapping(params = "primaryMenuDiy")
	@ResponseBody
	public String getPrimaryMenuDiy() {
		// 取二级菜单
		List<Function> primaryMenu = userService.getFunctionMap(UserCacheUtils.getCurrentUser()).get(1);
		String floor = "";
		if (primaryMenu == null) {
			return floor;
		}
		String menuString = "user.manage role.manage department.manage menu.manage";
		for (Function function : primaryMenu) {
			if (menuString.contains(function.getFunctionName())) {
				if (function.getFunctionLevel() == 1) {

					String lang_key = function.getFunctionName();
					String lang_context = mutiLangService.getLang(lang_key);
					if ("申请".equals(lang_key)) {
						lang_context = "申请";
						String s = "";
						s = "<div style='width:67px;position: absolute;top:47px;text-align:center;color:#000000;font-size:12px;'>" + lang_context + "</div>";
						floor += " <li><img class='imag1' src='static/login/images/head_icon1.png' /> " + " <img class='imag2' src='static/login/images/head_icon1.png' style='display: none;' />" + s + " </li> ";
					} else if ("Online 开发".equals(lang_context)) {

						floor += " <li><img class='imag1' src='static/login/images/online.png' /> " + " <img class='imag2' src='static/login/images/online_up.png' style='display: none;' />" + " </li> ";
					} else if ("统计查询".equals(lang_context)) {

						floor += " <li><img class='imag1' src='static/login/images/guanli.png' /> " + " <img class='imag2' src='static/login/images/guanli_up.png' style='display: none;' />" + " </li> ";
					} else if ("系统管理".equals(lang_context)) {

						floor += " <li><img class='imag1' src='static/login/images/xtgl.png' /> " + " <img class='imag2' src='static/login/images/xtgl_up.png' style='display: none;' />" + " </li> ";
					} else if ("常用示例".equals(lang_context)) {

						floor += " <li><img class='imag1' src='static/login/images/cysl.png' /> " + " <img class='imag2' src='static/login/images/cysl_up.png' style='display: none;' />" + " </li> ";
					} else if ("系统监控".equals(lang_context)) {

						floor += " <li><img class='imag1' src='static/login/images/xtjk.png' /> " + " <img class='imag2' src='static/login/images/xtjk_up.png' style='display: none;' />" + " </li> ";
					} else if (lang_context.contains("消息推送")) {
						String s = "<div style='width:67px;position: absolute;top:40px;text-align:center;color:#909090;font-size:12px;'>消息推送</div>";
						floor += " <li style='position: relative;'><img class='imag1' src='static/login/images/msg.png' /> " + " <img class='imag2' src='static/login/images/msg_up.png' style='display: none;' />" + s + "</li> ";
					} else {
						// 其他的为默认通用的图片模式
						String s = "";
						if (lang_context.length() >= 5 && lang_context.length() < 7) {
							s = "<div style='width:67px;position: absolute;top:40px;text-align:center;color:#000000;font-size:12px;'><span style='letter-spacing:-1px;'>" + lang_context + "</span></div>";
						} else if (lang_context.length() < 5) {
							s = "<div style='width:67px;position: absolute;top:40px;text-align:center;color:#000000;font-size:12px;'>" + lang_context + "</div>";
						} else if (lang_context.length() >= 7) {
							s = "<div style='width:67px;position: absolute;top:40px;text-align:center;color:#000000;font-size:12px;'><span style='letter-spacing:-1px;'>" + lang_context.substring(0, 6) + "</span></div>";
						}
						floor += " <li style='position: relative;'><img class='imag1' src='static/login/images/head_icon2.png' /> " + " <img class='imag2' src='static/login/images/default_up.png' style='display: none;' />" + s + "</li> ";
					}
				}
			}
		}

		return floor;
	}

	/**
	 * 云桌面返回：用户权限菜单
	 */
	@RequestMapping(params = "getPrimaryMenuForWebos")
	@ResponseBody
	public AjaxJson getPrimaryMenuForWebos() {
		AjaxJson j = new AjaxJson();
		// 将菜单加载到Session，用户只在登录的时候加载一次
		Object getPrimaryMenuForWebos = ContextHolderUtils.getSession().getAttribute("getPrimaryMenuForWebos");
		if (oConvertUtils.isNotEmpty(getPrimaryMenuForWebos)) {
			j.setMsg(getPrimaryMenuForWebos.toString());
		} else {
			String PMenu = MenuUtils.getWebosMenu(userService.getFunctionMap(UserCacheUtils.getCurrentUser()));
			ContextHolderUtils.getSession().setAttribute("getPrimaryMenuForWebos", PMenu);
			j.setMsg(PMenu);
		}
		return j;
	}

	@RequestMapping(value = "timeout")
	public String timeout() {
		return "login/timeout";
	}

	/**
	 * 另一套登录界面
	 * 
	 * @return
	 */
	@RequestMapping(params = "login2")
	public String login2() {
		return "login/login2";
	}

	/**
	 * ACE登录界面
	 * 
	 * @return
	 */
	@RequestMapping(params = "login3")
	public String login3() {
		return "login/login3";
	}

	/**
	 * 是否是验证码登录
	 * 
	 * @param useruame
	 *            用户名
	 * @param isFail
	 *            计数加1
	 * @param clean
	 *            计数清零
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static boolean isValidateCodeLogin(String useruame, boolean isFail, boolean clean) {
		Map<String, Integer> loginFailMap = (Map<String, Integer>) CacheUtils.get("loginFailMap");
		if (loginFailMap == null) {
			loginFailMap = Maps.newHashMap();
			CacheUtils.put("loginFailMap", loginFailMap);
		}
		Integer loginFailNum = loginFailMap.get(useruame);
		if (loginFailNum == null) {
			loginFailNum = 0;
		}
		if (isFail) {
			loginFailNum++;
			loginFailMap.put(useruame, loginFailNum);
		}
		if (clean) {
			loginFailMap.remove(useruame);
		}
		return loginFailNum >= 3;
	}
}