package cn.emay.modules.sys.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.criterion.Property;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import cn.emay.framework.common.poi.excel.ExcelImportUtil;
import cn.emay.framework.common.poi.excel.entity.params.ExportParams;
import cn.emay.framework.common.poi.excel.entity.params.ImportParams;
import cn.emay.framework.common.poi.excel.entity.vo.NormalExcelConstants;
import cn.emay.framework.common.utils.ExceptionUtil;
import cn.emay.framework.common.utils.MenuUtils;
import cn.emay.framework.common.utils.MyBeanUtils;
import cn.emay.framework.common.utils.PasswordUtil;
import cn.emay.framework.common.utils.ResourceUtil;
import cn.emay.framework.common.utils.RoletoJson;
import cn.emay.framework.common.utils.StringUtil;
import cn.emay.framework.common.utils.SysThemesUtil;
import cn.emay.framework.common.utils.oConvertUtils;
import cn.emay.framework.core.common.controller.BaseController;
import cn.emay.framework.core.common.hibernate.qbc.CriteriaQuery;
import cn.emay.framework.core.common.model.common.UploadFile;
import cn.emay.framework.core.common.model.json.AjaxJson;
import cn.emay.framework.core.common.model.json.ComboBox;
import cn.emay.framework.core.common.model.json.DataGrid;
import cn.emay.framework.core.common.model.json.ValidForm;
import cn.emay.framework.core.constant.Globals;
import cn.emay.framework.core.enums.SysThemesEnum;
import cn.emay.framework.tag.core.easyui.TagUtil;
import cn.emay.framework.tag.vo.datatable.DataTableReturn;
import cn.emay.framework.tag.vo.datatable.DataTables;
import cn.emay.modules.sys.entity.Depart;
import cn.emay.modules.sys.entity.Function;
import cn.emay.modules.sys.entity.Role;
import cn.emay.modules.sys.entity.RoleFunction;
import cn.emay.modules.sys.entity.RoleUser;
import cn.emay.modules.sys.entity.User;
import cn.emay.modules.sys.entity.UserOrg;
import cn.emay.modules.sys.manager.ClientManager;
import cn.emay.modules.sys.service.DepartService;
import cn.emay.modules.sys.service.RoleUserService;
import cn.emay.modules.sys.service.SystemService;
import cn.emay.modules.sys.service.UserService;

/**
 * 用户控制器
 * 
 * @Description: TODO(用户管理处理类)
 * @author 张代浩
 * 
 */

@Controller
@RequestMapping("/userController")
public class UserController extends BaseController {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(UserController.class);

	/**
	 * 用户service接口
	 */
	@Autowired
	private UserService userService;

	/**
	 * 系统service接口
	 */
	@Autowired
	private SystemService systemService;

	/**
	 * 
	 * 角色用户Service接口
	 * 
	 */
	@Autowired
	private RoleUserService roleUserService;

	
	/**
	 * 部门service接口
	 */
	@Autowired
	private DepartService departService;
	
	/**
	 * 菜单列表
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "menu")
	public void menu(HttpServletRequest request, HttpServletResponse response) {
//		SetListSort sort = new SetListSort();
		User u = ResourceUtil.getSessionUserName();
		// 登陆者的权限
		Set<Function> loginActionlist = new HashSet<Function>();// 已有权限菜单
		List<RoleUser> rUsers = systemService.findByProperty(RoleUser.class, "user.id", u.getId());
		for (RoleUser ru : rUsers) {
			Role role = ru.getRole();
			List<RoleFunction> roleFunctionList = systemService.findByProperty(RoleFunction.class, "role.id", role.getId());
			if (roleFunctionList.size() > 0) {
				for (RoleFunction roleFunction : roleFunctionList) {
					Function function = (Function) roleFunction.getFunction();
					loginActionlist.add(function);
				}
			}
		}
		List<Function> bigActionlist = new ArrayList<Function>();// 一级权限菜单
		List<Function> smailActionlist = new ArrayList<Function>();// 二级权限菜单
		if (loginActionlist.size() > 0) {
			for (Function function : loginActionlist) {
				if (function.getFunctionLevel() == 0) {
					bigActionlist.add(function);
				} else if (function.getFunctionLevel() == 1) {
					smailActionlist.add(function);
				}
			}
		}
		// 菜单栏排序
		Collections.sort(bigActionlist);
		Collections.sort(smailActionlist);
		/*
		Collections.sort(bigActionlist, sort);
		Collections.sort(smailActionlist, sort);
		*/
		String logString = MenuUtils.getMenu(bigActionlist, smailActionlist);
		// request.setAttribute("loginMenu",logString);
		try {
			response.getWriter().write(logString);
			response.getWriter().flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				response.getWriter().close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 用户列表页面跳转[跳转到标签和手工结合的html页面]
	 * 
	 * @return
	 */
	@RequestMapping(params = "userDemo")
	public String userDemo(HttpServletRequest request) {
		// 给部门查询条件中的下拉框准备数据
		List<Depart> departList = systemService.getList(Depart.class);
		request.setAttribute("departsReplace", RoletoJson.listToReplaceStr(departList, "departname", "id"));
		return "system/user/userList2";
	}

	/**
	 * 用户列表页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "user")
	public String user(HttpServletRequest request) {
		// 给部门查询条件中的下拉框准备数据
		List<Depart> departList = systemService.getList(Depart.class);
		request.setAttribute("departsReplace", RoletoJson.listToReplaceStr(departList, "departname", "id"));
		departList.clear();
		return "system/user/userList";
	}

	/**
	 * 用户信息
	 * 
	 * @return
	 */
	@RequestMapping(params = "userinfo")
	public String userinfo(HttpServletRequest request) {
		User user = ResourceUtil.getSessionUserName();
		request.setAttribute("user", user);
		return "system/user/userinfo";
	}

	/**
	 * 修改密码
	 * 
	 * @return
	 */
	@RequestMapping(params = "changepassword")
	public String changepassword(HttpServletRequest request) {
		User user = ResourceUtil.getSessionUserName();
		request.setAttribute("user", user);
		return "system/user/changepassword";
	}

	/**
	 * 修改密码
	 * 
	 * @return
	 */
	@RequestMapping(params = "savenewpwd")
	@ResponseBody
	public AjaxJson savenewpwd(HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		User user = ResourceUtil.getSessionUserName();
		String password = oConvertUtils.getString(request.getParameter("password"));
		String newpassword = oConvertUtils.getString(request.getParameter("newpassword"));
		String pString = PasswordUtil.encrypt(user.getUserName(), password, PasswordUtil.getStaticSalt());
		if (!pString.equals(user.getPassword())) {
			j.setMsg("原密码不正确");
			j.setSuccess(false);
		} else {
			try {
				user.setPassword(PasswordUtil.encrypt(user.getUserName(), newpassword, PasswordUtil.getStaticSalt()));
			} catch (Exception e) {
				e.printStackTrace();
			}
			systemService.updateEntitie(user);
			j.setMsg("修改成功");

		}
		return j;
	}

	/**
	 * 
	 * 修改用户密码
	 * 
	 * @author Chj
	 */

	@RequestMapping(params = "changepasswordforuser")
	public ModelAndView changepasswordforuser(User user, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(user.getId())) {
			user = systemService.getEntity(User.class, user.getId());
			req.setAttribute("user", user);
			idandname(req, user);
			// System.out.println(user.getPassword()+"-----"+user.getRealName());
		}
		return new ModelAndView("system/user/adminchangepwd");
	}

	@RequestMapping(params = "savenewpwdforuser")
	@ResponseBody
	public AjaxJson savenewpwdforuser(HttpServletRequest req) {
		String message = null;
		AjaxJson j = new AjaxJson();
		String id = oConvertUtils.getString(req.getParameter("id"));
		String password = oConvertUtils.getString(req.getParameter("password"));
		if (StringUtil.isNotEmpty(id)) {
			User users = systemService.getEntity(User.class, id);
			// System.out.println(users.getUserName());
			users.setPassword(PasswordUtil.encrypt(users.getUserName(), password, PasswordUtil.getStaticSalt()));
			users.setStatus(Globals.User_Normal);
			users.setActivitiSync(users.getActivitiSync());
			systemService.updateEntitie(users);
			message = "用户: " + users.getUserName() + "密码重置成功";
			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
		}

		j.setMsg(message);

		return j;
	}

	/**
	 * 锁定账户
	 * 
	 * 
	 * @author pu.chen
	 */
	@RequestMapping(params = "lock")
	@ResponseBody
	public AjaxJson lock(String id, HttpServletRequest req) {
		AjaxJson j = new AjaxJson();
		String message = null;
		User user = systemService.getEntity(User.class, id);
		if ("admin".equals(user.getUserName())) {
			message = "超级管理员[admin]不可操作";
			j.setMsg(message);
			return j;
		}
		String lockValue = req.getParameter("lockvalue");

		user.setStatus(new Short(lockValue));
		try {
			userService.updateEntitie(user);
			if ("0".equals(lockValue)) {
				message = "用户：" + user.getUserName() + "锁定成功!";
			} else if ("1".equals(lockValue)) {
				message = "用户：" + user.getUserName() + "激活成功!";
			}
			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
		} catch (Exception e) {
			message = "操作失败!";
		}
		j.setMsg(message);
		return j;
	}

	/**
	 * 得到角色列表
	 * 
	 * @return
	 */
	@RequestMapping(params = "role")
	@ResponseBody
	public List<ComboBox> role(HttpServletResponse response, HttpServletRequest request, ComboBox comboBox) {
		String id = request.getParameter("id");
		List<ComboBox> comboBoxs = new ArrayList<ComboBox>();
		List<Role> roles = new ArrayList<Role>();
		if (StringUtil.isNotEmpty(id)) {
			List<RoleUser> roleUser = systemService.findByProperty(RoleUser.class, "user.id", id);
			if (roleUser.size() > 0) {
				for (RoleUser ru : roleUser) {
					roles.add(ru.getRole());
				}
			}
		}
		List<Role> roleList = systemService.getList(Role.class);
		comboBoxs = TagUtil.getComboBox(roleList, roles, comboBox);

		roleList.clear();
		roles.clear();

		return comboBoxs;
	}

	/**
	 * 得到部门列表
	 * 
	 * @return
	 */
	@RequestMapping(params = "depart")
	@ResponseBody
	public List<ComboBox> depart(HttpServletResponse response, HttpServletRequest request, ComboBox comboBox) {
		String id = request.getParameter("id");
		List<ComboBox> comboBoxs = new ArrayList<ComboBox>();
		List<Depart> departs = new ArrayList<Depart>();
		if (StringUtil.isNotEmpty(id)) {
			// todo zhanggm 获取指定用户的组织机构列表
			List<Depart[]> resultList = systemService.findHql("from Depart d,UserOrg uo where d.id=uo.orgId and uo.id=?", id);
			for (Depart[] departArr : resultList) {
				departs.add(departArr[0]);
			}
		}
		List<Depart> departList = systemService.getList(Depart.class);
		comboBoxs = TagUtil.getComboBox(departList, departs, comboBox);
		return comboBoxs;
	}

	/**
	 * easyuiAJAX用户列表请求数据
	 * 
	 * @param request
	 * @param response
	 * @param dataGrid
	 */
	@RequestMapping(params = "datagrid")
	public void datagrid(User user, HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(User.class, dataGrid);
		// 查询条件组装器
		cn.emay.framework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, user);

		Short[] userstate = new Short[] { Globals.User_Normal, Globals.User_ADMIN, Globals.User_Forbidden };
		cq.in("status", userstate);

		cq.eq("deleteFlag", Globals.Delete_Normal);

		String orgIds = request.getParameter("orgIds");
		List<String> orgIdList = extractIdListByComma(orgIds);
		// 获取 当前组织机构的用户信息
		if (!CollectionUtils.isEmpty(orgIdList)) {
			CriteriaQuery subCq = new CriteriaQuery(UserOrg.class);
			subCq.setProjection(Property.forName("user.id"));
			subCq.in("depart.id", orgIdList.toArray());
			subCq.add();

			cq.add(Property.forName("id").in(subCq.getDetachedCriteria()));
		}

		cq.add();
		this.systemService.getDataGridReturn(cq, true);

		List<User> cfeList = new ArrayList<User>();
		for (Object o : dataGrid.getResults()) {
			if (o instanceof User) {
				User cfe = (User) o;
				if (cfe.getId() != null && !"".equals(cfe.getId())) {
					List<RoleUser> roleUser = systemService.findByProperty(RoleUser.class, "role.id", cfe.getId());
					if (roleUser.size() > 0) {
						String roleName = "";
						for (RoleUser ru : roleUser) {
							roleName += ru.getRole().getRoleName() + ",";
						}
						roleName = roleName.substring(0, roleName.length() - 1);
						cfe.setUserKey(roleName);
					}
				}
				cfeList.add(cfe);
			}
		}

		TagUtil.datagrid(response, dataGrid);
	}

	/**
	 * 用户信息录入和更新
	 * 
	 * @param user
	 * @param req
	 * @return
	 */
	@RequestMapping(params = "del")
	@ResponseBody
	public AjaxJson del(User user, HttpServletRequest req) {
		String message = null;
		AjaxJson ajaxJson = new AjaxJson();
		if ("admin".equals(user.getUserName())) {
			message = "超级管理员[admin]不可删除";
			ajaxJson.setMsg(message);
			return ajaxJson;
		}
		user = userService.getEntity(User.class, user.getId());
		if (!user.getStatus().equals(Globals.User_ADMIN)) {
			userService.delete(user);
			message = "用户：" + user.getUserName() + "删除成功";
		} else {
			message = "超级管理员不可删除";
		}
		ajaxJson.setMsg(message);
		return ajaxJson;
	}

	public void delRoleUser(User user) {
		// 同步删除用户角色关联表
		List<RoleUser> roleUserList = systemService.findByProperty(RoleUser.class, "user.id", user.getId());
		if (roleUserList.size() >= 1) {
			for (RoleUser tRoleUser : roleUserList) {
				systemService.delete(tRoleUser);
			}
		}
	}

	/**
	 * 检查用户名
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "checkUser")
	@ResponseBody
	public ValidForm checkUser(HttpServletRequest request) {
		ValidForm v = new ValidForm();
		String userName = oConvertUtils.getString(request.getParameter("param"));
		String code = oConvertUtils.getString(request.getParameter("code"));
		List<User> roles = systemService.findByProperty(User.class, "userName", userName);
		if (roles.size() > 0 && !code.equals(userName)) {
			v.setInfo("用户名已存在");
			v.setStatus("n");
		}
		return v;
	}

	/**
	 * 用户录入
	 * 
	 * @param user
	 * @param req
	 * @return
	 */

	@RequestMapping(params = "saveUser")
	@ResponseBody
	public AjaxJson saveUser(HttpServletRequest req, User user) {
		String message = null;
		AjaxJson ajaxJson = new AjaxJson();
		// 得到用户的角色
		String roleid = oConvertUtils.getString(req.getParameter("roleid"));
		String password = oConvertUtils.getString(req.getParameter("password"));
		if (StringUtil.isNotEmpty(user.getId())) {
			User users = systemService.getEntity(User.class, user.getId());
			users.setEmail(user.getEmail());
			users.setOfficePhone(user.getOfficePhone());
			users.setMobilePhone(user.getMobilePhone());
			users.setAvatar(user.getAvatar());

			systemService.executeSql("delete from sys_user_org where user_id=?", user.getId());
			saveUserOrgList(req, user);
			
			

			users.setRealName(user.getRealName());
			users.setStatus(Globals.User_Normal);
			users.setActivitiSync(user.getActivitiSync());
			systemService.updateEntitie(users);
			List<RoleUser> ru = systemService.findByProperty(RoleUser.class, "user.id", user.getId());
			systemService.deleteAllEntitie(ru);
			message = "用户: " + users.getUserName() + "更新成功";
			if (StringUtil.isNotEmpty(roleid)) {
				saveRoleUser(users, roleid);
			}
			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
		} else {
			User users = systemService.findUniqueByProperty(User.class, "userName", user.getUserName());
			if (users != null) {
				message = "用户: " + users.getUserName() + "已经存在";
			} else {
				user.setPassword(PasswordUtil.encrypt(user.getUserName(), password, PasswordUtil.getStaticSalt()));

				user.setStatus(Globals.User_Normal);
				user.setDeleteFlag(Globals.Delete_Normal);
				systemService.save(user);
				
				
				//保存多个组织机构
				saveUserOrgList(req, user);
				message = "用户: " + user.getUserName() + "添加成功";
				if (StringUtil.isNotEmpty(roleid)) {
					saveRoleUser(user, roleid);
				}
				systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
			}

		}
		ajaxJson.setMsg(message);

		return ajaxJson;
	}

	/**
	 * 保存 用户-组织机构 关系信息
	 * 
	 * @param request
	 *            request
	 * @param user
	 *            user
	 */
	private void saveUserOrgList(HttpServletRequest request, User user) {
		String orgIds = oConvertUtils.getString(request.getParameter("orgIds"));

		List<UserOrg> userOrgList = new ArrayList<UserOrg>();
		List<String> orgIdList = extractIdListByComma(orgIds);
		for (String orgId : orgIdList) {
			Depart depart = new Depart();
			depart.setId(orgId);

			UserOrg userOrg = new UserOrg();
			userOrg.setUser(user);
			userOrg.setDepart(depart);

			userOrgList.add(userOrg);
		}
		if (!userOrgList.isEmpty()) {
			systemService.batchSave(userOrgList);
		}
	}

	protected void saveRoleUser(User user, String roleidstr) {
		String[] roleids = roleidstr.split(",");
		for (int i = 0; i < roleids.length; i++) {
			RoleUser rUser = new RoleUser();
			Role role = systemService.getEntity(Role.class, roleids[i]);
			rUser.setRole(role);
			rUser.setUser(user);
			systemService.save(rUser);

		}
	}

	/**
	 * 用户选择角色跳转页面
	 * 
	 * @return
	 */
	@RequestMapping(params = "roles")
	public ModelAndView roles(HttpServletRequest request) {
		// --author：zhoujf-----start----date:20150531--------for:
		// 编辑用户，选择角色,弹出的角色列表页面，默认没选中
		ModelAndView mv = new ModelAndView("system/user/users");
		String ids = oConvertUtils.getString(request.getParameter("ids"));
		mv.addObject("ids", ids);
		return mv;
		// --author：zhoujf-----end------date:20150531--------for:
		// 编辑用户，选择角色,弹出的角色列表页面，默认没选中
	}

	/**
	 * 角色显示列表
	 * 
	 * @param request
	 * @param response
	 * @param dataGrid
	 */
	@RequestMapping(params = "datagridRole")
	public void datagridRole(Role tsRole, HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(Role.class, dataGrid);
		// 查询条件组装器
		cn.emay.framework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, tsRole);
		this.systemService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}

	/**
	 * easyuiAJAX请求数据： 用户选择角色列表
	 * 
	 * @param request
	 * @param response
	 * @param dataGrid
	 * @param user
	 */
	@RequestMapping(params = "addorupdate")
	public ModelAndView addorupdate(String id, HttpServletRequest req) {
		Depart tsDepart = new Depart();
		if (StringUtil.isNotEmpty(id)) {
			User user = systemService.getEntity(User.class, id);
			req.setAttribute("user", user);
			idandname(req, user);
			getOrgInfos(req, user);
		}
		req.setAttribute("tsDepart", tsDepart);
		return new ModelAndView("system/user/user");
	}

	/**
	 * 用户的登录后的组织机构选择页面
	 * 
	 * @param request
	 *            request
	 * @return 用户选择组织机构页面
	 */
	@RequestMapping(params = "userOrgSelect")
	public ModelAndView userOrgSelect(HttpServletRequest request) {
		List<Depart> orgList = new ArrayList<Depart>();
		String userId = oConvertUtils.getString(request.getParameter("userId"));

		List<Object[]> orgArrList = systemService.findHql("from Depart d,UserOrg uo where d.id=uo.depart.id and uo.user.id=?", userId);
		for (Object[] departs : orgArrList) {
			orgList.add((Depart) departs[0]);
		}
		request.setAttribute("orgList", orgList);

		User user = systemService.getEntity(User.class, userId);
		request.setAttribute("user", user);

		return new ModelAndView("system/user/userOrgSelect");
	}

	public void idandname(HttpServletRequest req, User user) {
		List<RoleUser> roleUsers = systemService.findByProperty(RoleUser.class, "user.id", user.getId());
		String roleId = "";
		String roleName = "";
		if (roleUsers.size() > 0) {
			for (RoleUser tRoleUser : roleUsers) {
				roleId += tRoleUser.getRole().getId() + ",";
				roleName += tRoleUser.getRole().getRoleName() + ",";
			}
		}
		req.setAttribute("id", roleId);
		req.setAttribute("roleName", roleName);

	}

	public void getOrgInfos(HttpServletRequest req, User user) {
		List<UserOrg> tSUserOrgs = systemService.findByProperty(UserOrg.class, "user.id", user.getId());
		String orgIds = "";
		String departname = "";
		if (tSUserOrgs.size() > 0) {
			for (UserOrg tSUserOrg : tSUserOrgs) {
				orgIds += tSUserOrg.getDepart().getId() + ",";
				departname += tSUserOrg.getDepart().getDepartname() + ",";
			}
		}
		req.setAttribute("orgIds", orgIds);
		req.setAttribute("departname", departname);

	}

	/**
	 * 根据部门和角色选择用户跳转页面
	 */
	@RequestMapping(params = "choose")
	public String choose(HttpServletRequest request) {
		List<Role> roles = systemService.loadAll(Role.class);
		request.setAttribute("roleList", roles);
		return "system/membership/checkuser";
	}

	/**
	 * 部门和角色选择用户的panel跳转页面
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "chooseUser")
	public String chooseUser(HttpServletRequest request) {
		String departid = request.getParameter("departid");
		String roleid = request.getParameter("roleid");
		request.setAttribute("roleid", roleid);
		request.setAttribute("departid", departid);
		return "system/membership/userlist";
	}

	/**
	 * 部门和角色选择用户的用户显示列表
	 * 
	 * @param request
	 * @param response
	 * @param dataGrid
	 */
	@RequestMapping(params = "datagridUser")
	public void datagridUser(HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		String departid = request.getParameter("departid");
		String roleid = request.getParameter("roleid");
		CriteriaQuery cq = new CriteriaQuery(User.class, dataGrid);
		if (departid.length() > 0) {
			cq.eq("TDepart.departid", oConvertUtils.getInt(departid, 0));
			cq.add();
		}
		String userid = "";
		if (roleid.length() > 0) {
			List<RoleUser> roleUsers = systemService.findByProperty(RoleUser.class, "role.roleid", oConvertUtils.getInt(roleid, 0));
			if (roleUsers.size() > 0) {
				for (RoleUser tRoleUser : roleUsers) {
					userid += tRoleUser.getUser().getId() + ",";
				}
			}
			cq.in("userid", oConvertUtils.getInts(userid.split(",")));
			cq.add();
		}
		this.systemService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}

	/**
	 * 根据部门和角色选择用户跳转页面
	 */
	@RequestMapping(params = "roleDepart")
	public String roleDepart(HttpServletRequest request) {
		List<Role> roles = systemService.loadAll(Role.class);
		request.setAttribute("roleList", roles);
		return "system/membership/roledepart";
	}

	/**
	 * 部门和角色选择用户的panel跳转页面
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "chooseDepart")
	public ModelAndView chooseDepart(HttpServletRequest request) {
		String nodeid = request.getParameter("nodeid");
		ModelAndView modelAndView = null;
		if (nodeid.equals("role")) {
			modelAndView = new ModelAndView("system/membership/users");
		} else {
			modelAndView = new ModelAndView("system/membership/departList");
		}
		return modelAndView;
	}

	/**
	 * 部门和角色选择用户的用户显示列表
	 * 
	 * @param request
	 * @param response
	 * @param dataGrid
	 */
	@RequestMapping(params = "datagridDepart")
	public void datagridDepart(HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(Depart.class, dataGrid);
		systemService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}

	/**
	 * 测试
	 * 
	 * @param user
	 * @param req
	 * @return
	 */
	@RequestMapping(params = "test")
	public void test(HttpServletRequest request, HttpServletResponse response) {
		// String jString = request.getParameter("_dt_json");
		DataTables dataTables = new DataTables(request);
		CriteriaQuery cq = new CriteriaQuery(User.class, dataTables);
		String username = request.getParameter("userName");
		if (username != null) {
			cq.like("userName", username);
			cq.add();
		}
		DataTableReturn dataTableReturn = systemService.getDataTableReturn(cq, true);
		TagUtil.datatable(response, dataTableReturn, "id,userName,mobilePhone,TSDepart_departname");
	}

	/**
	 * 用户列表页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "index")
	public String index() {
		return "bootstrap/main";
	}

	/**
	 * 用户列表页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "main")
	public String main() {
		return "bootstrap/test";
	}

	/**
	 * 测试
	 * 
	 * @return
	 */
	@RequestMapping(params = "testpage")
	public String testpage(HttpServletRequest request) {
		return "test/test";
	}

	/**
	 * 设置签名跳转页面
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "addsign")
	public ModelAndView addsign(HttpServletRequest request) {
		String id = request.getParameter("id");
		request.setAttribute("id", id);
		return new ModelAndView("system/user/usersign");
	}

	/**
	 * 用户录入
	 * 
	 * @param user
	 * @param req
	 * @return
	 */

	@RequestMapping(params = "savesign", method = RequestMethod.POST)
	@ResponseBody
	public AjaxJson savesign(HttpServletRequest req) {
		String message = null;
		UploadFile uploadFile = new UploadFile(req);
		String id = uploadFile.get("id");
		User user = systemService.getEntity(User.class, id);
		uploadFile.setRealPath("signatureFile");
		uploadFile.setCusPath("signature");
		uploadFile.setByteField("signature");
		uploadFile.setBasePath("resources");
		uploadFile.setRename(false);
		uploadFile.setObject(user);
		AjaxJson j = new AjaxJson();
		message = user.getUserName() + "设置签名成功";
		systemService.uploadFile(uploadFile);
		systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
		j.setMsg(message);

		return j;
	}

	/**
	 * 测试组合查询功能
	 * 
	 * @param user
	 * @param request
	 * @param response
	 * @param dataGrid
	 */
	@RequestMapping(params = "testSearch")
	public void testSearch(User user, HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(User.class, dataGrid);
		if (user.getUserName() != null) {
			cq.like("userName", user.getUserName());
		}
		if (user.getRealName() != null) {
			cq.like("realName", user.getRealName());
		}
		cq.add();
		this.systemService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}

	@RequestMapping(params = "changestyle")
	public String changeStyle(HttpServletRequest request) {
		User user = ResourceUtil.getSessionUserName();
		if (user == null) {
			return "login/login";
		}
		// String indexStyle = "shortcut";
		// String cssTheme="";
		// Cookie[] cookies = request.getCookies();
		// for (Cookie cookie : cookies) {
		// if(cookie==null || StringUtils.isEmpty(cookie.getName())){
		// continue;
		// }
		// if(cookie.getName().equalsIgnoreCase("JEECGINDEXSTYLE")){
		// indexStyle = cookie.getValue();
		// }
		// if(cookie.getName().equalsIgnoreCase("JEECGCSSTHEME")){
		// cssTheme = cookie.getValue();
		// }
		// }
		SysThemesEnum sysThemesEnum = SysThemesUtil.getSysTheme(request);
		request.setAttribute("indexStyle", sysThemesEnum.getStyle());
		// request.setAttribute("cssTheme", cssTheme);
		return "system/user/changestyle";
	}

	/**
	 * @Title: saveStyle
	 * @Description: 修改首页样式
	 * @param request
	 * @return AjaxJson
	 * @throws
	 */
	@RequestMapping(params = "savestyle")
	@ResponseBody
	public AjaxJson saveStyle(HttpServletRequest request, HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		j.setSuccess(Boolean.FALSE);
		User user = ResourceUtil.getSessionUserName();
		if (user != null) {
			String indexStyle = request.getParameter("indexStyle");
			// String cssTheme = request.getParameter("cssTheme");

			// if(StringUtils.isNotEmpty(cssTheme)){
			// Cookie cookie4css = new Cookie("JEECGCSSTHEME", cssTheme);
			// cookie4css.setMaxAge(3600*24*30);
			// response.addCookie(cookie4css);
			// logger.info("cssTheme:"+cssTheme);
			// }else if("ace".equals(indexStyle)){
			// Cookie cookie4css = new Cookie("JEECGCSSTHEME", "metro");
			// cookie4css.setMaxAge(3600*24*30);
			// response.addCookie(cookie4css);
			// logger.info("cssTheme:metro");

			// }else {
			// Cookie cookie4css = new Cookie("JEECGCSSTHEME", "");
			// cookie4css.setMaxAge(3600*24*30);
			// response.addCookie(cookie4css);
			// logger.info("cssTheme:default");
			// }

			if (StringUtils.isNotEmpty(indexStyle)) {
				Cookie cookie = new Cookie("JEECGINDEXSTYLE", indexStyle);
				// 设置cookie有效期为一个月
				cookie.setMaxAge(3600 * 24 * 30);
				response.addCookie(cookie);
				logger.debug(" ----- 首页样式: indexStyle ----- " + indexStyle);
				j.setSuccess(Boolean.TRUE);
				j.setMsg("样式修改成功，请刷新页面");
			}

			try {
				ClientManager.getInstance().getClient().getFunctions().clear();
			} catch (Exception e) {
			}

		} else {
			j.setMsg("请登录后再操作");
		}
		return j;
	}

	/**
	 * 导入功能跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "upload")
	public ModelAndView upload(HttpServletRequest req) {
		req.setAttribute("controller_name", "userController");
		return new ModelAndView("common/upload/pub_excel_upload");
	}

	/**
	 * 导出excel
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "exportXls")
	public String exportXls(User tsUser, HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid, ModelMap modelMap) {
		CriteriaQuery cq = new CriteriaQuery(User.class, dataGrid);
		cn.emay.framework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, tsUser, request.getParameterMap());
		List<User> tsUsers = this.userService.getListByCriteriaQuery(cq, false);
		// 导出的时候处理一下组织机构编码和角色编码
		for (int i = 0; i < tsUsers.size(); i++) {
			User user = tsUsers.get(i);
			// 托管
			systemService.getSession().evict(user);
			String id = user.getId();
			List<Role> roles = roleUserService.findRoleByUser(id);
			String roleCodes = "";
			for (Role role : roles) {
				roleCodes += role.getRoleCode() + ",";
			}
			if (StringUtils.isNotBlank(roleCodes)) {
				user.setUserKey(roleCodes.substring(0, roleCodes.length() - 1));
			} else {
				user.setUserKey(roleCodes);
			}

			/**
			 * 用户部门列表
			 */
			List<Depart> departs =departService.findUserDepart(id);
			String departCodes = "";
			for (Depart depart : departs) {
				departCodes += depart.getOrgCode() + ",";
			}
			user.setDepartid(departCodes.substring(0, departCodes.length() - 1));
		}
		modelMap.put(NormalExcelConstants.FILE_NAME, "用户表");
		modelMap.put(NormalExcelConstants.CLASS, User.class);
		modelMap.put(NormalExcelConstants.PARAMS, new ExportParams("用户表列表", "导出人:" + ResourceUtil.getSessionUserName().getRealName(), "导出信息"));
		modelMap.put(NormalExcelConstants.DATA_LIST, tsUsers);
		return NormalExcelConstants.JEECG_EXCEL_VIEW;
	}

	/**
	 * 导出excel 使模板
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "exportXlsByT")
	public String exportXlsByT(User tsUser, HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid, ModelMap modelMap) {
		modelMap.put(NormalExcelConstants.FILE_NAME, "用户表");
		modelMap.put(NormalExcelConstants.CLASS, User.class);
		modelMap.put(NormalExcelConstants.PARAMS, new ExportParams("用户表列表", "导出人:" + ResourceUtil.getSessionUserName().getRealName(), "导出信息"));
		modelMap.put(NormalExcelConstants.DATA_LIST, new ArrayList<Object>());
		return NormalExcelConstants.JEECG_EXCEL_VIEW;
	}

	@RequestMapping(params = "importExcel", method = RequestMethod.POST)
	@ResponseBody
	public AjaxJson importExcel(HttpServletRequest request, HttpServletResponse response) {
		AjaxJson j = new AjaxJson();

		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
		for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
			MultipartFile file = entity.getValue();// 获取上传文件对象
			ImportParams params = new ImportParams();
			params.setTitleRows(2);
			params.setHeadRows(1);
			params.setNeedSave(true);
			try {
				List<User> tsUsers = ExcelImportUtil.importExcel(file.getInputStream(), User.class, params);
				for (User tsUser : tsUsers) {
					tsUser.setStatus(new Short("1"));
					String username = tsUser.getUserName();
					String roleCodes = tsUser.getUserKey();
					String deptCodes = tsUser.getDepartid();

					if (username == null || username.equals("")) {
						j.setMsg("用户名为必填字段，导入失败");
					} else if ((roleCodes == null || roleCodes.equals("")) || (deptCodes == null || deptCodes.equals(""))) {
						List<User> users = systemService.findByProperty(User.class, "userName", username);
						if (users.size() != 0) {
							// 用户存在更新
							User user = users.get(0);
							MyBeanUtils.copyBeanNotNull2Bean(tsUser, user);
							user.setDepartid(null);
							systemService.saveOrUpdate(user);
						} else {
							tsUser.setDepartid(null);
							systemService.save(tsUser);
						}
					} else {
						String[] roles = roleCodes.split(",");
						String[] depts = deptCodes.split(",");
						boolean flag = true;
						// 判断组织机构编码和角色编码是否存在，如果不存在，也不能导入
						for (String roleCode : roles) {
							List<Role> roleList = systemService.findByProperty(Role.class, "roleCode", roleCode);
							if (roleList.size() == 0) {
								flag = false;
							}
						}

						for (String deptCode : depts) {
							List<Depart> departList = systemService.findByProperty(Depart.class, "orgCode", deptCode);
							if (departList.size() == 0) {
								flag = false;
							}
						}

						if (flag) {
							// 判断用户是否存在
							List<User> users = systemService.findByProperty(User.class, "userName", username);
							if (users.size() != 0) {
								// 用户存在更新
								User user = users.get(0);
								MyBeanUtils.copyBeanNotNull2Bean(tsUser, user);
								user.setDepartid(null);
								systemService.saveOrUpdate(user);

								String id = user.getId();
								systemService.executeSql("delete from sys_role_user where userid='" + id + "'");
								for (String roleCode : roles) {
									// 根据角色编码得到roleid
									List<Role> roleList = systemService.findByProperty(Role.class, "roleCode", roleCode);
									RoleUser tsRoleUser = new RoleUser();
									tsRoleUser.setUser(user);
									tsRoleUser.setRole(roleList.get(0));
									systemService.save(tsRoleUser);
								}

								systemService.executeSql("delete from sys_user_org where user_id='" + id + "'");
								for (String orgCode : depts) {
									// 根据角色编码得到roleid
									List<Depart> departList = systemService.findByProperty(Depart.class, "orgCode", orgCode);
									UserOrg tsUserOrg = new UserOrg();
									tsUserOrg.setDepart(departList.get(0));
									tsUserOrg.setUser(user);
									systemService.save(tsUserOrg);
								}
							} else {
								// 不存在则保存
								// TSUser user = users.get(0);
								tsUser.setDepartid(null);
								systemService.save(tsUser);
								for (String roleCode : roles) {
									// 根据角色编码得到roleid
									List<Role> roleList = systemService.findByProperty(Role.class, "roleCode", roleCode);
									RoleUser tsRoleUser = new RoleUser();
									tsRoleUser.setUser(tsUser);
									tsRoleUser.setRole(roleList.get(0));
									systemService.save(tsRoleUser);
								}

								for (String orgCode : depts) {
									// 根据角色编码得到roleid
									List<Depart> departList = systemService.findByProperty(Depart.class, "orgCode", orgCode);
									UserOrg tsUserOrg = new UserOrg();
									tsUserOrg.setDepart(departList.get(0));
									tsUserOrg.setUser(tsUser);
									systemService.save(tsUserOrg);
								}
							}
							j.setMsg("文件导入成功！");
						} else {
							j.setMsg("组织机构编码和角色编码不能匹配");
						}
					}
				}
			} catch (Exception e) {
				j.setMsg("文件导入失败！");
				logger.error(ExceptionUtil.getExceptionMessage(e));
			} finally {
				try {
					file.getInputStream().close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return j;
	}

	/**
	 * 选择用户跳转页面
	 * 
	 * @return
	 */
	@RequestMapping(params = "userSelect")
	public String userSelect() {
		return "system/user/userSelect";
	}
}