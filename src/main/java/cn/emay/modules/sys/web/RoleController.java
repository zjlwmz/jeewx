package cn.emay.modules.sys.web;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
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
import cn.emay.framework.common.utils.MutiLangUtil;
import cn.emay.framework.common.utils.MyBeanUtils;
import cn.emay.framework.common.utils.NumberComparator;
import cn.emay.framework.common.utils.ResourceUtil;
import cn.emay.framework.common.utils.SetListSort;
import cn.emay.framework.common.utils.StringUtil;
import cn.emay.framework.common.utils.oConvertUtils;
import cn.emay.framework.core.common.controller.BaseController;
import cn.emay.framework.core.common.hibernate.qbc.CriteriaQuery;
import cn.emay.framework.core.common.model.json.AjaxJson;
import cn.emay.framework.core.common.model.json.ComboTree;
import cn.emay.framework.core.common.model.json.DataGrid;
import cn.emay.framework.core.common.model.json.TreeGrid;
import cn.emay.framework.core.common.model.json.ValidForm;
import cn.emay.framework.core.constant.Globals;
import cn.emay.framework.tag.core.easyui.TagUtil;
import cn.emay.framework.tag.vo.easyui.ComboTreeModel;
import cn.emay.framework.tag.vo.easyui.TreeGridModel;
import cn.emay.modules.sys.entity.DataRule;
import cn.emay.modules.sys.entity.Depart;
import cn.emay.modules.sys.entity.Function;
import cn.emay.modules.sys.entity.Operation;
import cn.emay.modules.sys.entity.Role;
import cn.emay.modules.sys.entity.RoleFunction;
import cn.emay.modules.sys.entity.RoleOrg;
import cn.emay.modules.sys.entity.RoleUser;
import cn.emay.modules.sys.entity.User;
import cn.emay.modules.sys.service.SystemService;
import cn.emay.modules.sys.service.UserService;

/**
 * 角色处理类
 * 
 * @author 张代浩
 * 
 */
@Controller
@RequestMapping("/roleController")
public class RoleController extends BaseController {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(RoleController.class);
	
	
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
	 * 角色列表页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "role")
	public ModelAndView role() {
		return new ModelAndView("system/role/roleList");
	}

	/**
	 * easyuiAJAX请求数据
	 * 
	 * @param request
	 * @param response
	 * @param dataGrid
	 */

	@RequestMapping(params = "roleGrid")
	public void roleGrid(Role role, HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(Role.class, dataGrid);
		cn.emay.framework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, role);
		cq.add();
		this.systemService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
		;
	}

	/**
	 * 删除角色
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "delRole")
	@ResponseBody
	public AjaxJson delRole(Role role, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		long count = userService.findRoleUser(role.getId());
		if (count == 0) {
			// 删除角色之前先删除角色权限关系
			delRoleFunction(role);
			systemService.executeSql("delete from sys_role_org where role_id=?", role.getId()); // 删除
			role = systemService.getEntity(Role.class, role.getId());
			userService.delete(role);
			message = "角色: " + role.getRoleName() + "被删除成功";
			systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
		} else {
			message = "角色: 仍被用户使用，请先删除关联关系";
		}
		j.setMsg(message);
		return j;
	}

	/**
	 * 检查角色
	 * 
	 * @param role
	 * @return
	 */
	@RequestMapping(params = "checkRole")
	@ResponseBody
	public ValidForm checkRole(Role role, HttpServletRequest request, HttpServletResponse response) {
		ValidForm v = new ValidForm();
		String roleCode = oConvertUtils.getString(request.getParameter("param"));
		String code = oConvertUtils.getString(request.getParameter("code"));
		List<Role> roles = systemService.findByProperty(Role.class, "roleCode", roleCode);
		if (roles.size() > 0 && !code.equals(roleCode)) {
			v.setInfo("角色编码已存在");
			v.setStatus("n");
		}
		return v;
	}

	/**
	 * 删除角色权限
	 * 
	 * @param role
	 */
	protected void delRoleFunction(Role role) {
		List<RoleFunction> roleFunctions = systemService.findByProperty(RoleFunction.class, "role.id", role.getId());
		if (roleFunctions.size() > 0) {
			for (RoleFunction tsRoleFunction : roleFunctions) {
				systemService.delete(tsRoleFunction);
			}
		}
		List<RoleUser> roleUsers = systemService.findByProperty(RoleUser.class, "role.id", role.getId());
		for (RoleUser tsRoleUser : roleUsers) {
			systemService.delete(tsRoleUser);
		}
	}

	/**
	 * 角色录入
	 * 
	 * @param role
	 * @return
	 */
	@RequestMapping(params = "saveRole")
	@ResponseBody
	public AjaxJson saveRole(Role role, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		if (StringUtil.isNotEmpty(role.getId())) {
			message = "角色: " + role.getRoleName() + "被更新成功";
			userService.saveOrUpdate(role);
			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
		} else {
			message = "角色: " + role.getRoleName() + "被添加成功";
			userService.save(role);
			systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
		}

		return j;
	}

	/**
	 * 角色列表页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "fun")
	public ModelAndView fun(HttpServletRequest request) {
		String roleId = request.getParameter("roleId");
		request.setAttribute("roleId", roleId);
		return new ModelAndView("system/role/roleSet");
	}

	/**
	 * 
	 * 角色所有用户信息列表页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "userList")
	public ModelAndView userList(HttpServletRequest request) {

		request.setAttribute("roleId", request.getParameter("roleId"));

		return new ModelAndView("system/role/roleUserList");
	}

	/**
	 * 用户列表查询
	 * 
	 * @param request
	 * @param response
	 * @param dataGrid
	 */
	@RequestMapping(params = "roleUserDatagrid")
	public void roleUserDatagrid(User user, HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(User.class, dataGrid);

		// 查询条件组装器
		String roleId = request.getParameter("roleId");
		List<RoleUser> roleUser = systemService.findByProperty(RoleUser.class, "role.id", roleId);
		

		Criterion cc = null;
		if (roleUser.size() > 0) {
			for (int i = 0; i < roleUser.size(); i++) {
				if (i == 0) {
					cc = Restrictions.eq("id", roleUser.get(i).getUser().getId());
				} else {
					cc = cq.getor(cc, Restrictions.eq("id", roleUser.get(i).getUser().getId()));
				}
			}
		} else {
			cc = Restrictions.eq("id", "-1");
		}
		cq.add(cc);
		cn.emay.framework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, user);
		this.systemService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}

	/**
	 * 获取用户列表
	 * 
	 * @param user
	 * @param request
	 * @param response
	 * @param dataGrid
	 * @return
	 */
	@RequestMapping(params = "getUserList")
	@ResponseBody
	public List<ComboTree> getUserList(User user, HttpServletRequest request, ComboTree comboTree) {
		List<ComboTree> comboTrees = new ArrayList<ComboTree>();
		String roleId = request.getParameter("roleId");
		List<User> loginActionlist = new ArrayList<User>();
		if (user != null) {

			List<RoleUser> roleUser = systemService.findByProperty(RoleUser.class, "role.id", roleId);
			if (roleUser.size() > 0) {
				for (RoleUser ru : roleUser) {
					loginActionlist.add(ru.getUser());
				}
			}
		}
		ComboTreeModel comboTreeModel = new ComboTreeModel("id", "userName", "user");
		comboTrees = systemService.ComboTree(loginActionlist, comboTreeModel, loginActionlist, false);
		return comboTrees;
	}

	/**
	 * 角色树列表页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "roleTree")
	public ModelAndView roleTree(HttpServletRequest request) {
		request.setAttribute("orgId", request.getParameter("orgId"));
		return new ModelAndView("system/role/roleTree");
	}

	/**
	 * 获取 组织机构的角色树
	 * 
	 * @param request
	 *            request
	 * @return 组织机构的角色树
	 */
	@RequestMapping(params = "getRoleTree")
	@ResponseBody
	public List<ComboTree> getRoleTree(HttpServletRequest request) {
		ComboTreeModel comboTreeModel = new ComboTreeModel("id", "roleName", "");
		String orgId = request.getParameter("orgId");
		List<Role[]> orgRoleArrList = systemService.findHql("from Role r, RoleOrg ro, Depart o WHERE r.id=ro.role.id AND ro.depart.id=o.id AND o.id=?", orgId);
		List<Role> orgRoleList = new ArrayList<Role>();
		for (Object[] roleArr : orgRoleArrList) {
			orgRoleList.add((Role) roleArr[0]);
		}

		List<Object> allRoleList = this.systemService.getList(Role.class);
		List<ComboTree> comboTrees = systemService.ComboTree(allRoleList, comboTreeModel, orgRoleList, false);

		return comboTrees;
	}

	/**
	 * 更新 组织机构的角色列表
	 * 
	 * @param request
	 *            request
	 * @return 操作结果
	 */
	@RequestMapping(params = "updateOrgRole")
	@ResponseBody
	public AjaxJson updateOrgRole(HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		try {
			String orgId = request.getParameter("orgId");
			String roleIds = request.getParameter("roleIds");
			List<String> roleIdList = extractIdListByComma(roleIds);
			systemService.executeSql("delete from sys_role_org where org_id=?", orgId);
			if (!roleIdList.isEmpty()) {
				List<RoleOrg> roleOrgList = new ArrayList<RoleOrg>();
				Depart depart = new Depart();
				depart.setId(orgId);
				for (String roleId : roleIdList) {
					Role role = new Role();
					role.setId(roleId);

					RoleOrg roleOrg = new RoleOrg();
					roleOrg.setRole(role);
					roleOrg.setDepart(depart);
					roleOrgList.add(roleOrg);
				}
				systemService.batchSave(roleOrgList);
			}
			j.setMsg("角色更新成功");
		} catch (Exception e) {
			logger.error(ExceptionUtil.getExceptionMessage(e));
			j.setMsg("角色更新失败");
		}
		return j;
	}

	/**
	 * 设置权限
	 * 
	 * @param role
	 * @param request
	 * @param comboTree
	 * @return
	 */
	@RequestMapping(params = "setAuthority")
	@ResponseBody
	public List<ComboTree> setAuthority(Role role, HttpServletRequest request, ComboTree comboTree) {
		CriteriaQuery cq = new CriteriaQuery(Function.class);
		if (comboTree.getId() != null) {
			cq.eq("parentFunction.id", comboTree.getId());
		}
		if (comboTree.getId() == null) {
			cq.isNull("parentFunction");
		}
		cq.notEq("functionLevel", Short.parseShort("-1"));
		cq.add();
		List<Function> functionList = systemService.getListByCriteriaQuery(cq, false);
		Collections.sort(functionList, new NumberComparator());
		List<ComboTree> comboTrees = new ArrayList<ComboTree>();
		String roleId = request.getParameter("roleId");
		List<Function> loginActionlist = new ArrayList<Function>();// 已有权限菜单
		role = this.systemService.get(Role.class, roleId);
		if (role != null) {
			List<RoleFunction> roleFunctionList = systemService.findByProperty(RoleFunction.class, "role.id", role.getId());
			if (roleFunctionList.size() > 0) {
				for (RoleFunction roleFunction : roleFunctionList) {
					Function function = (Function) roleFunction.getFunction();
					loginActionlist.add(function);
				}
			}
			roleFunctionList.clear();
		}
		ComboTreeModel comboTreeModel = new ComboTreeModel("id", "functionName", "functions");
		// author:xugj-----start-----date:20160516 ------- for: TASK #1071
		// 【平台】优化角色权限这块功能
		comboTrees = systemService.ComboTree(functionList, comboTreeModel, loginActionlist, true);
		MutiLangUtil.setMutiComboTree(comboTrees);
		// author:xugj-----start-----date:20160516 ------- for: TASK #1071
		// 【平台】优化角色权限这块功能

		functionList.clear();
		loginActionlist.clear();

		return comboTrees;
	}

	/**
	 * 更新权限
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "updateAuthority")
	@ResponseBody
	public AjaxJson updateAuthority(HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		try {
			String roleId = request.getParameter("roleId");
			String rolefunction = request.getParameter("rolefunctions");
			Role role = this.systemService.get(Role.class, roleId);
			List<RoleFunction> roleFunctionList = systemService.findByProperty(RoleFunction.class, "role.id", role.getId());
			Map<String, RoleFunction> map = new HashMap<String, RoleFunction>();
			for (RoleFunction functionOfRole : roleFunctionList) {
				map.put(functionOfRole.getFunction().getId(), functionOfRole);
			}
			String[] roleFunctions = rolefunction.split(",");
			Set<String> set = new HashSet<String>();
			for (String s : roleFunctions) {
				set.add(s);
			}
			updateCompare(set, role, map);
			j.setMsg("权限更新成功");
		} catch (Exception e) {
			logger.error(ExceptionUtil.getExceptionMessage(e));
			j.setMsg("权限更新失败");
		}
		return j;
	}

	/**
	 * 权限比较
	 * 
	 * @param set
	 *            最新的权限列表
	 * @param role
	 *            当前角色
	 * @param map
	 *            旧的权限列表
	 */
	private void updateCompare(Set<String> set, Role role, Map<String, RoleFunction> map) {
		List<RoleFunction> entitys = new ArrayList<RoleFunction>();
		List<RoleFunction> deleteEntitys = new ArrayList<RoleFunction>();
		for (String s : set) {
			if (map.containsKey(s)) {
				map.remove(s);
			} else {
				RoleFunction rf = new RoleFunction();
				Function f = this.systemService.get(Function.class, s);
				rf.setFunction(f);
				rf.setRole(role);
				entitys.add(rf);
			}
		}
		Collection<RoleFunction> collection = map.values();
		Iterator<RoleFunction> it = collection.iterator();
		for (; it.hasNext();) {
			deleteEntitys.add(it.next());
		}
		systemService.batchSave(entitys);
		systemService.deleteAllEntitie(deleteEntitys);

	}

	/**
	 * 角色页面跳转
	 * 
	 * @param role
	 * @param req
	 * @return
	 */
	@RequestMapping(params = "addorupdate")
	public ModelAndView addorupdate(Role role, HttpServletRequest req) {
		if (role.getId() != null) {
			role = systemService.getEntity(Role.class, role.getId());
			req.setAttribute("role", role);
		}
		return new ModelAndView("system/role/role");
	}

	/**
	 * 权限操作列表
	 * 
	 * @param treegrid
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(params = "setOperate")
	@ResponseBody
	public List<TreeGrid> setOperate(HttpServletRequest request, TreeGrid treegrid) {
		String roleId = request.getParameter("roleId");
		CriteriaQuery cq = new CriteriaQuery(Function.class);
		if (treegrid.getId() != null) {
			cq.eq("parentFunction.id", treegrid.getId());
		}
		if (treegrid.getId() == null) {
			cq.isNull("parentFunction");
		}
		cq.add();
		List<Function> functionList = systemService.getListByCriteriaQuery(cq, false);
		List<TreeGrid> treeGrids = new ArrayList<TreeGrid>();
		Collections.sort(functionList, new SetListSort());
		TreeGridModel treeGridModel = new TreeGridModel();
		treeGridModel.setRoleid(roleId);
		treeGrids = systemService.treegrid(functionList, treeGridModel);
		return treeGrids;

	}

	/**
	 * 操作录入
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "saveOperate")
	@ResponseBody
	public AjaxJson saveOperate(HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		String fop = request.getParameter("fp");
		String roleId = request.getParameter("roleId");
		// 录入操作前清空上一次的操作数据
		clearp(roleId);
		String[] fun_op = fop.split(",");
		String aa = "";
		String bb = "";
		// 只有一个被选中
		if (fun_op.length == 1) {
			bb = fun_op[0].split("_")[1];
			aa = fun_op[0].split("_")[0];
			savep(roleId, bb, aa);
		} else {
			// 至少2个被选中
			for (int i = 0; i < fun_op.length; i++) {
				String cc = fun_op[i].split("_")[0]; // 操作id
				if (i > 0 && bb.equals(fun_op[i].split("_")[1])) {
					aa += "," + cc;
					if (i == (fun_op.length - 1)) {
						savep(roleId, bb, aa);
					}
				} else if (i > 0) {
					savep(roleId, bb, aa);
					aa = fun_op[i].split("_")[0]; // 操作ID
					if (i == (fun_op.length - 1)) {
						bb = fun_op[i].split("_")[1]; // 权限id
						savep(roleId, bb, aa);
					}

				} else {
					aa = fun_op[i].split("_")[0]; // 操作ID
				}
				bb = fun_op[i].split("_")[1]; // 权限id

			}
		}

		return j;
	}

	/**
	 * 更新操作
	 * 
	 * @param roleId
	 * @param functionid
	 * @param ids
	 */
	public void savep(String roleId, String functionid, String ids) {
		CriteriaQuery cq = new CriteriaQuery(RoleFunction.class);
		cq.eq("role.id", roleId);
		cq.eq("function.id", functionid);
		cq.add();
		List<RoleFunction> rFunctions = systemService.getListByCriteriaQuery(cq, false);
		if (rFunctions.size() > 0) {
			RoleFunction roleFunction = rFunctions.get(0);
			roleFunction.setOperation(ids);
			systemService.saveOrUpdate(roleFunction);
		}
	}

	/**
	 * 清空操作
	 * 
	 * @param roleId
	 */
	public void clearp(String roleId) {
		List<RoleFunction> rFunctions = systemService.findByProperty(RoleFunction.class, "role.id", roleId);
		if (rFunctions.size() > 0) {
			for (RoleFunction tRoleFunction : rFunctions) {
				tRoleFunction.setOperation(null);
				systemService.saveOrUpdate(tRoleFunction);
			}
		}
	}

	/**
	 * 按钮权限展示
	 * 
	 * @param request
	 * @param functionId
	 * @param roleId
	 * @return
	 */
	@RequestMapping(params = "operationListForFunction")
	public ModelAndView operationListForFunction(HttpServletRequest request, String functionId, String roleId) {
		CriteriaQuery cq = new CriteriaQuery(Operation.class);
		cq.eq("function.id", functionId);

		cq.eq("status", Short.valueOf("0"));

		cq.add();
		List<Operation> operationList = this.systemService.getListByCriteriaQuery(cq, false);
		Set<String> operationCodes = systemService.getOperationCodesByRoleIdAndFunctionId(roleId, functionId);
		request.setAttribute("operationList", operationList);
		request.setAttribute("operationcodes", operationCodes);
		request.setAttribute("functionId", functionId);
		return new ModelAndView("system/role/operationListForFunction");
	}

	/**
	 * 更新按钮权限
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "updateOperation")
	@ResponseBody
	public AjaxJson updateOperation(HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		String roleId = request.getParameter("roleId");
		String functionId = request.getParameter("functionId");

		String operationcodes = null;
		try {
			operationcodes = URLDecoder.decode(request.getParameter("operationcodes"), "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		CriteriaQuery cq1 = new CriteriaQuery(RoleFunction.class);
		cq1.eq("role.id", roleId);
		cq1.eq("function.id", functionId);
		cq1.add();
		List<RoleFunction> rFunctions = systemService.getListByCriteriaQuery(cq1, false);
		if (null != rFunctions && rFunctions.size() > 0) {
			RoleFunction tsRoleFunction = rFunctions.get(0);
			tsRoleFunction.setOperation(operationcodes);
			systemService.saveOrUpdate(tsRoleFunction);
		}
		j.setMsg("按钮权限更新成功");
		return j;
	}

	// chengchuan数据规则过滤代码

	/**
	 * 按钮权限展示
	 * 
	 * @param request
	 * @param functionId
	 * @param roleId
	 * @return
	 */
	@RequestMapping(params = "dataRuleListForFunction")
	public ModelAndView dataRuleListForFunction(HttpServletRequest request, String functionId, String roleId) {
		CriteriaQuery cq = new CriteriaQuery(DataRule.class);
		cq.eq("function.id", functionId);
		cq.add();
		List<DataRule> dataRuleList = this.systemService.getListByCriteriaQuery(cq, false);
		Set<String> dataRulecodes = systemService.getOperationCodesByRoleIdAndruleDataId(roleId, functionId);
		request.setAttribute("dataRuleList", dataRuleList);
		request.setAttribute("dataRulecodes", dataRulecodes);
		request.setAttribute("functionId", functionId);
		return new ModelAndView("system/role/dataRuleListForFunction");
	}

	/**
	 * 更新按钮权限
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "updateDataRule")
	@ResponseBody
	public AjaxJson updateDataRule(HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		String roleId = request.getParameter("roleId");
		String functionId = request.getParameter("functionId");

		String dataRulecodes = null;
		try {
			dataRulecodes = URLDecoder.decode(request.getParameter("dataRulecodes"), "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		CriteriaQuery cq1 = new CriteriaQuery(RoleFunction.class);
		cq1.eq("role.id", roleId);
		cq1.eq("function.id", functionId);
		cq1.add();
		List<RoleFunction> rFunctions = systemService.getListByCriteriaQuery(cq1, false);
		if (null != rFunctions && rFunctions.size() > 0) {
			RoleFunction tsRoleFunction = rFunctions.get(0);
			tsRoleFunction.setDataRule(dataRulecodes);
			systemService.saveOrUpdate(tsRoleFunction);
		}
		j.setMsg("数据权限更新成功");
		return j;
	}

	/**
	 * 添加 用户到角色 的页面 跳转
	 * 
	 * @param req
	 *            request
	 * @return 处理结果信息
	 */
	@RequestMapping(params = "goAddUserToRole")
	public ModelAndView goAddUserToOrg(HttpServletRequest req) {
		return new ModelAndView("system/role/noCurRoleUserList");
	}

	/**
	 * 获取 除当前 角色之外的用户信息列表
	 * 
	 * @param request
	 *            request
	 * @return 处理结果信息
	 */
	@RequestMapping(params = "addUserToRoleList")
	public void addUserToOrgList(User user, HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		String roleId = request.getParameter("roleId");

		CriteriaQuery cq = new CriteriaQuery(User.class, dataGrid);
		cn.emay.framework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, user);

		// 获取 当前组织机构的用户信息
		CriteriaQuery subCq = new CriteriaQuery(RoleUser.class);
		subCq.setProjection(Property.forName("user.id"));
		subCq.eq("role.id", roleId);
		subCq.add();

		cq.add(Property.forName("id").notIn(subCq.getDetachedCriteria()));
		cq.add();

		this.systemService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}

	/**
	 * 添加 用户到角色
	 * 
	 * @param req
	 *            request
	 * @return 处理结果信息
	 */
	@RequestMapping(params = "doAddUserToRole")
	@ResponseBody
	public AjaxJson doAddUserToOrg(HttpServletRequest req) {
		String message = null;
		AjaxJson j = new AjaxJson();
		Role role = systemService.getEntity(Role.class, req.getParameter("roleId"));
		saveRoleUserList(req, role);
		message = MutiLangUtil.paramAddSuccess("common.user");
		// systemService.addLog(message, Globals.Log_Type_UPDATE,
		// Globals.Log_Leavel_INFO);
		j.setMsg(message);

		return j;
	}

	/**
	 * 保存 角色-用户 关系信息
	 * 
	 * @param request
	 *            request
	 * @param depart
	 *            depart
	 */
	private void saveRoleUserList(HttpServletRequest request, Role role) {
		String userIds = oConvertUtils.getString(request.getParameter("userIds"));

		List<RoleUser> roleUserList = new ArrayList<RoleUser>();
		List<String> userIdList = extractIdListByComma(userIds);
		for (String userId : userIdList) {
			User user = new User();
			user.setId(userId);

			RoleUser roleUser = new RoleUser();
			roleUser.setUser(user);
			roleUser.setRole(role);

			roleUserList.add(roleUser);
		}
		if (!roleUserList.isEmpty()) {
			systemService.batchSave(roleUserList);
		}
	}

	/**
	 * 导入功能跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "upload")
	public ModelAndView upload(HttpServletRequest req) {
		req.setAttribute("controller_name", "roleController");
		return new ModelAndView("common/upload/pub_excel_upload");
	}

	/**
	 * 导出excel
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "exportXls")
	public String exportXls(Role tsRole, HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid, ModelMap modelMap) {
		tsRole.setRoleName(null);
		CriteriaQuery cq = new CriteriaQuery(Role.class, dataGrid);
		cn.emay.framework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, tsRole, request.getParameterMap());
		List<Role> tsRoles = systemService.getListByCriteriaQuery(cq, false);
		modelMap.put(NormalExcelConstants.FILE_NAME, "角色表");
		modelMap.put(NormalExcelConstants.CLASS, Role.class);
		modelMap.put(NormalExcelConstants.PARAMS, new ExportParams("角色表列表", "导出人:" + ResourceUtil.getSessionUserName().getRealName(), "导出信息"));
		modelMap.put(NormalExcelConstants.DATA_LIST, tsRoles);
		return NormalExcelConstants.JEECG_EXCEL_VIEW;
	}

	/**
	 * 导出excel 使模板
	 * 
	 * @param request
	 * @param response
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(params = "exportXlsByT")
	public String exportXlsByT(Role tsRole, HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid, ModelMap modelMap) {
		modelMap.put(NormalExcelConstants.FILE_NAME, "用户表");
		modelMap.put(NormalExcelConstants.CLASS, Role.class);
		modelMap.put(NormalExcelConstants.PARAMS, new ExportParams("用户表列表", "导出人:" + ResourceUtil.getSessionUserName().getRealName(), "导出信息"));
		modelMap.put(NormalExcelConstants.DATA_LIST, new ArrayList());
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
				List<Role> tsRoles = ExcelImportUtil.importExcel(file.getInputStream(), Role.class, params);
				for (Role tsRole : tsRoles) {
					String roleCode = tsRole.getRoleCode();
					List<Role> roles = systemService.findByProperty(Role.class, "roleCode", roleCode);
					if (roles.size() != 0) {
						Role role = roles.get(0);
						MyBeanUtils.copyBeanNotNull2Bean(tsRole, role);
						systemService.saveOrUpdate(role);
					} else {
						systemService.save(tsRole);
					}
				}
				j.setMsg("文件导入成功！");
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
}
