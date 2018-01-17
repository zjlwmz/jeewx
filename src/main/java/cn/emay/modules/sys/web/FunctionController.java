package cn.emay.modules.sys.web;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import cn.emay.framework.common.utils.MutiLangUtil;
import cn.emay.framework.common.utils.NumberComparator;
import cn.emay.framework.common.utils.StringUtil;
import cn.emay.framework.common.utils.oConvertUtils;
import cn.emay.framework.core.common.controller.BaseController;
import cn.emay.framework.core.common.hibernate.qbc.CriteriaQuery;
import cn.emay.framework.core.common.model.json.AjaxJson;
import cn.emay.framework.core.common.model.json.ComboTree;
import cn.emay.framework.core.common.model.json.DataGrid;
import cn.emay.framework.core.common.model.json.TreeGrid;
import cn.emay.framework.core.constant.Globals;
import cn.emay.framework.core.extend.hqlsearch.HqlGenerateUtil;
import cn.emay.framework.tag.core.easyui.TagUtil;
import cn.emay.framework.tag.vo.datatable.SortDirection;
import cn.emay.framework.tag.vo.easyui.ComboTreeModel;
import cn.emay.framework.tag.vo.easyui.TreeGridModel;
import cn.emay.modules.sys.entity.DataRule;
import cn.emay.modules.sys.entity.Function;
import cn.emay.modules.sys.entity.Icon;
import cn.emay.modules.sys.entity.Operation;
import cn.emay.modules.sys.entity.RoleFunction;
import cn.emay.modules.sys.service.FunctionService;
import cn.emay.modules.sys.service.RoleFunctionService;
import cn.emay.modules.sys.service.SystemService;
import cn.emay.modules.sys.service.UserService;

/**
 * 菜单权限处理类
 * 
 * @author 张代浩
 * 
 */
@Controller
@RequestMapping("/functionController")
public class FunctionController extends BaseController {
	
	private static final Logger logger = Logger.getLogger(FunctionController.class);
	
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
	 * 菜单service接口
	 */
	@Autowired
	private FunctionService functionService;
	
	@Autowired
	private RoleFunctionService roleFunctionService;
	
	@ModelAttribute("function")
	public Function get(@RequestParam(required=false) String id) {
		if (StringUtils.isNotBlank(id)){
			return systemService.get(Function.class, id);
		}else{
			return new Function();
		}
	}
	
	

	/**
	 * 权限列表页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "function")
	public ModelAndView function() {
		return new ModelAndView("system/function/functionList");
	}

	/**
	 * 操作列表页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "operation")
	public ModelAndView operation(HttpServletRequest request, String functionId) {
		request.setAttribute("functionId", functionId);
		return new ModelAndView("system/operation/operationList");
	}

	/**
	 * 数据规则列表页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "dataRule")
	public ModelAndView operationData(HttpServletRequest request, String functionId) {
		request.setAttribute("functionId", functionId);
		return new ModelAndView("system/dataRule/ruleDataList");
	}

	/**
	 * easyuiAJAX请求数据
	 * 
	 * @param request
	 * @param response
	 * @param dataGrid
	 */

	@RequestMapping(params = "datagrid")
	public void datagrid(HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(Function.class, dataGrid);
		systemService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}

	/**
	 * easyuiAJAX请求数据
	 * 
	 * @param request
	 * @param response
	 * @param dataGrid
	 */

	@RequestMapping(params = "opdategrid")
	public void opdategrid(HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(Operation.class, dataGrid);
		String functionId = oConvertUtils.getString(request.getParameter("functionId"));
		cq.eq("parentFunction.id", functionId);
		cq.add();
		systemService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}

	/**
	 * 删除权限
	 * 
	 * @param function
	 * @return
	 */
	@RequestMapping(params = "del")
	@ResponseBody
	public AjaxJson del(Function function, HttpServletRequest request) {
		String message = null;
		AjaxJson ajaxJson = new AjaxJson();
		message = MutiLangUtil.paramDelSuccess("common.menu");
		systemService.updateBySqlString("delete from sys_role_function where functionid='" + function.getId() + "'");

		try {
			systemService.delete(function);
		} catch (Exception e) {
			logger.error("删除权限", e);
			message = MutiLangUtil.getMutiLangInstance().getLang("common.menu.del.fail");
		}

		systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);


		ajaxJson.setMsg(message);
		return ajaxJson;
	}

	/**
	 * 删除操作 删除操作时同步更新已分配此操作的的 角色功能记录中的oeration 字段。
	 * 
	 * @param operation
	 * @return
	 */
	@RequestMapping(params = "delop")
	@ResponseBody
	public AjaxJson delop(Operation operation, HttpServletRequest request) {
		String message = null;
		AjaxJson ajaxJson = new AjaxJson();
		operation = systemService.getEntity(Operation.class, operation.getId());
		message = MutiLangUtil.paramDelSuccess("common.operation");
		userService.delete(operation);
		// ---author:jg_xugj----start-----date:20151211--------for：#779
		// 【菜单问题】当删了t_s_operation表中记录时， t_s_role_function 表中operation 字段应该同步更新。
		String operationId = operation.getId();
		List<RoleFunction> roleFunctions =roleFunctionService.findRoleFunctionByOperation(operationId);
		for (RoleFunction roleFunction : roleFunctions) {
			String newOper = roleFunction.getOperation().replace(operationId + ",", "");
			if (roleFunction.getOperation().length() == newOper.length()) {
				newOper = roleFunction.getOperation().replace(operationId, "");
			}
			roleFunction.setOperation(newOper);
			userService.updateEntitie(roleFunction);
		}
		// ---author:jg_xugj----start-----date:20151211--------for：#779
		// 【菜单问题】当删了t_s_operation表中记录时， t_s_role_function 表中operation 字段应该同步更新。

		systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);

		ajaxJson.setMsg(message);

		return ajaxJson;
	}

	/**
	 * 递归更新子菜单的FunctionLevel
	 * 
	 * @param subFunction
	 * @param parent
	 */
	private void updateSubFunction(List<Function> subFunction, Function parent) {
		if (subFunction.size() == 0) {// 没有子菜单是结束
			return;
		} else {
			for (Function function : subFunction) {
				function.setFunctionLevel(Short.valueOf(parent.getFunctionLevel() + 1 + ""));
				systemService.saveOrUpdate(function);
				List<Function> subFunction1 =functionService.findFunctionByParent(function.getId());
				updateSubFunction(subFunction1, function);
			}
		}
	}

	/**
	 * 权限录入
	 * 
	 * @param function
	 * @return
	 */
	@RequestMapping(params = "saveFunction")
	@ResponseBody
	public AjaxJson saveFunction(Function function, HttpServletRequest request) {
		String message = "权限录入";
		AjaxJson ajaxJson = new AjaxJson();
		try{
			function.setFunctionUrl(function.getFunctionUrl().trim());
			String functionOrder = function.getFunctionOrder();
			if (StringUtils.isEmpty(functionOrder)) {
				function.setFunctionOrder("0");
			}
			Function parent=function.getParentFunction();
			if(StringUtils.isBlank(parent.getId())){
				function.setParentFunction(null);
			}else{
				/**
				 * 菜单等级
				 */
				Short functionLevel=parent.getFunctionLevel();
				if(null==functionLevel){
					parent=functionService.get(parent.getId());
					functionLevel=parent.getFunctionLevel();
				}
				functionLevel++;
				function.setFunctionLevel(functionLevel);
			}
			
			if (StringUtils.isNotBlank(function.getId())) {
				functionService.update(function);
				systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);

				List<Function> subFunction = functionService.findByProperty(Function.class, "parentFunction.id", function.getId());
				updateSubFunction(subFunction, function);

				systemService.flushRoleFunciton(function.getId(), function);

			} else {
				if (function.getFunctionLevel().equals(Globals.Function_Leave_ONE)) {
					function.setFunctionOrder(function.getFunctionOrder());
				} else {
					function.setFunctionOrder(function.getFunctionOrder());
				}
				systemService.save(function);
				systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
			}
		}catch (Exception e) {
			logger.error("保存异常", e);
			message="保存异常";
			ajaxJson.setSuccess(false);
		}
		

		ajaxJson.setMsg(message);
		return ajaxJson;
	}

	/**
	 * 操作录入
	 * 
	 * @param operation
	 * @return
	 */
	@RequestMapping(params = "saveop")
	@ResponseBody
	public AjaxJson saveop(Operation operation, HttpServletRequest request) {
		String message = null;
		String pid = request.getParameter("Function.id");
		if (pid.equals("")) {
			operation.setFunction(null);
		}
		AjaxJson j = new AjaxJson();
		if (StringUtil.isNotEmpty(operation.getId())) {
			message = MutiLangUtil.paramUpdSuccess("common.operation");
			userService.saveOrUpdate(operation);
			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
		} else {
			message = MutiLangUtil.paramAddSuccess("common.operation");
			userService.save(operation);
			systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
		}

		j.setMsg(message);
		return j;
	}

	/**
	 * 权限列表页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "addorupdate")
	public ModelAndView addorupdate(Function function, HttpServletRequest req) {
		List<Function> fuinctionlist = systemService.getList(Function.class);
		req.setAttribute("flist", fuinctionlist);
		List<Icon> iconlist = systemService.findByQueryString("from Icon where iconType != 3");
		req.setAttribute("iconlist", iconlist);
		List<Icon> iconDeskList = systemService.findByQueryString("from Icon where iconType = 3");
		req.setAttribute("iconDeskList", iconDeskList);
		if (function != null) {
			req.setAttribute("function", function);
			if (function.getParentFunction() != null && function.getParentFunction().getId() != null) {
				function.setFunctionLevel((short) 1);
				function.setParentFunction((Function) systemService.getEntity(Function.class, function.getParentFunction().getId()));
				req.setAttribute("function", function);
			}
		}
		return new ModelAndView("system/function/function");
	}

	/**
	 * 操作列表页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "addorupdateop")
	public ModelAndView addorupdateop(Operation operation, HttpServletRequest req) {
		List<Icon> iconlist = systemService.getList(Icon.class);
		req.setAttribute("iconlist", iconlist);
		if (operation.getId() != null) {
			operation = systemService.getEntity(Operation.class, operation.getId());
			req.setAttribute("operation", operation);
		}
		String functionId = oConvertUtils.getString(req.getParameter("functionId"));
		req.setAttribute("functionId", functionId);
		return new ModelAndView("system/operation/operation");
	}

	/**
	 * 权限列表
	 */
	@RequestMapping(params = "functionGrid")
	@ResponseBody
	public List<TreeGrid> functionGrid(HttpServletRequest request, TreeGrid treegrid) {
		CriteriaQuery cq = new CriteriaQuery(Function.class);
		String selfId = request.getParameter("selfId");
		if (selfId != null) {
			cq.notEq("id", selfId);
		}
		if (treegrid.getId() != null) {
			cq.eq("parentFunction.id", treegrid.getId());
		}
		if (treegrid.getId() == null) {
			cq.isNull("parentFunction");
		}
		cq.addOrder("functionOrder", SortDirection.asc);
		cq.add();

		// 获取装载数据权限的条件HQL
		cq = HqlGenerateUtil.getDataAuthorConditionHql(cq, new Function());
		cq.add();

		List<Function> functionList = systemService.getListByCriteriaQuery(cq, false);

		Collections.sort(functionList, new NumberComparator());

		List<TreeGrid> treeGrids = new ArrayList<TreeGrid>();
		TreeGridModel treeGridModel = new TreeGridModel();
		treeGridModel.setIcon("icon_iconPath");
		treeGridModel.setTextField("functionName");
		treeGridModel.setParentText("parentFunction_functionName");
		treeGridModel.setParentId("parentFunction_id");
		treeGridModel.setSrc("functionUrl");
		treeGridModel.setIdField("id");
		treeGridModel.setChildList("Functions");
		// 添加排序字段
		treeGridModel.setOrder("functionOrder");

		treeGridModel.setFunctionType("functionType");

		treeGrids = systemService.treegrid(functionList, treeGridModel);

		MutiLangUtil.setMutiTree(treeGrids);
		return treeGrids;
	}

	/**
	 * 权限列表
	 */
	@RequestMapping(params = "functionList")
	@ResponseBody
	public void functionList(HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(Function.class, dataGrid);
		String id = oConvertUtils.getString(request.getParameter("id"));
		cq.isNull("parentFunction");
		if (id != null) {
			cq.eq("parentFunction.id", id);
		}
		cq.add();
		this.systemService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}

	/**
	 * 父级权限下拉菜单
	 */
	@RequestMapping(params = "setPFunction")
	@ResponseBody
	public List<ComboTree> setPFunction(HttpServletRequest request, ComboTree comboTree) {
		CriteriaQuery cq = new CriteriaQuery(Function.class);
		if (null != request.getParameter("selfId")) {
			cq.notEq("id", request.getParameter("selfId"));
		}
		if (comboTree.getId() != null) {
			cq.eq("parentFunction.id", comboTree.getId());
		}
		if (comboTree.getId() == null) {
			cq.isNull("parentFunction");
		}
		cq.add();
		List<Function> functionList = systemService.getListByCriteriaQuery(cq, false);
		List<ComboTree> comboTrees = new ArrayList<ComboTree>();
		ComboTreeModel comboTreeModel = new ComboTreeModel("id", "functionName", "Functions");
		comboTrees = systemService.ComboTree(functionList, comboTreeModel, null, false);
		MutiLangUtil.setMutiTree(comboTrees);
		return comboTrees;
	}

	/**
	 * 菜单模糊检索功能
	 * 
	 * @return
	 */
	@RequestMapping(params = "searchApp")
	public ModelAndView searchApp(Function function, HttpServletRequest req) {

		String name = req.getParameter("name");
		String menuListMap = "";
		// String menuListMap =
		// "<div class='appListContainer ui-sortable' customacceptdrop='0' index='0' _olddisplay='block' style='width: auto; height: auto; margin-left: 10px; margin-top: 10px; display: block;'>";
		CriteriaQuery cq = new CriteriaQuery(Function.class);

		cq.notEq("functionLevel", Short.valueOf("0"));
		if (name == null || "".equals(name)) {
			cq.isNull("parentFunction");
		} else {
			String name1 = "%" + name + "%";
			cq.like("functionName", name1);
		}
		cq.add();
		List<Function> functionList = systemService.getListByCriteriaQuery(cq, false);
		if (functionList.size() > 0 && functionList != null) {
			for (int i = 0; i < functionList.size(); i++) {
				// menuListMap = menuListMap +
				// "<div id='menuList_area'  class='menuList_area'>";
				String icon = "";
				if (!"".equals(functionList.get(i).getIconDesk()) && functionList.get(i).getIconDesk() != null) {
					icon = functionList.get(i).getIconDesk().getIconPath();
				} else {
					icon = "static/sliding/icon/default.png";
				}
				menuListMap = menuListMap + "<div type='" + i + 1 + "' class='menuSearch_Info' id='" + functionList.get(i).getId() + "' title='" + functionList.get(i).getFunctionName() + "' url='" + functionList.get(i).getFunctionUrl() + "' icon='" + icon + "' style='float:left;left: 0px; top: 0px;margin-left: 30px;margin-top: 20px;'>" + "<div ><img alt='" + functionList.get(i).getFunctionName() + "' src='" + icon + "'></div>" + "<div class='appButton_appName_inner1' style='color:#333333;'>" + functionList.get(i).getFunctionName() + "</div>" + "<div class='appButton_appName_inner_right1'></div>" +
				// "</div>" +
						"</div>";
			}
		} else {
			menuListMap = menuListMap + "很遗憾，在系统中没有检索到与“" + name + "”相关的信息！";
		}
		// menuListMap = menuListMap + "</div>";
		req.setAttribute("menuListMap", menuListMap);
		return new ModelAndView("system/function/menuAppList");
	}

	/**
	 * 
	 * addorupdaterule 数据规则权限的编辑和新增
	 * 
	 * @Title: addorupdaterule
	 * @Description: TODO
	 * @param @param operation
	 * @param @param req
	 * @param @return 设定文件
	 * @return ModelAndView 返回类型
	 * @throws
	 */
	@RequestMapping(params = "addorupdaterule")
	public ModelAndView addorupdaterule(DataRule operation, HttpServletRequest req) {
		List<Icon> iconlist = systemService.getList(Icon.class);
		req.setAttribute("iconlist", iconlist);
		if (operation.getId() != null) {
			operation = systemService.getEntity(DataRule.class, operation.getId());
			req.setAttribute("operation", operation);
		}
		String functionId = oConvertUtils.getString(req.getParameter("functionId"));
		req.setAttribute("functionId", functionId);
		return new ModelAndView("system/dataRule/ruleData");
	}

	/**
	 * 
	 * opdategrid 数据规则的列表界面
	 * 
	 * @Title: opdategrid
	 * @Description: TODO
	 * @param @param request
	 * @param @param response
	 * @param @param dataGrid 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	@RequestMapping(params = "ruledategrid")
	public void ruledategrid(HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(DataRule.class, dataGrid);
		String functionId = oConvertUtils.getString(request.getParameter("functionId"));
		cq.eq("function.id", functionId);
		cq.add();
		this.systemService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}

	/**
	 * 
	 * delrule 删除数据权限规则
	 * 
	 * @Title: delrule
	 * @Description: TODO
	 * @param @param operation
	 * @param @param request
	 * @param @return 设定文件
	 * @return AjaxJson 返回类型
	 * @throws
	 */
	@RequestMapping(params = "delrule")
	@ResponseBody
	public AjaxJson delrule(DataRule operation, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		operation = systemService.getEntity(DataRule.class, operation.getId());
		message = MutiLangUtil.paramDelSuccess("common.operation");
		userService.delete(operation);
		systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);

		j.setMsg(message);

		return j;
	}

	/**
	 * 
	 * saverule保存规则权限值
	 * 
	 * @Title: saverule
	 * @Description: TODO
	 * @param @param operation
	 * @param @param request
	 * @param @return 设定文件
	 * @return AjaxJson 返回类型
	 * @throws
	 */
	@RequestMapping(params = "saverule")
	@ResponseBody
	public AjaxJson saverule(DataRule operation, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		if (StringUtil.isNotEmpty(operation.getId())) {
			message = MutiLangUtil.paramUpdSuccess("common.operation");
			userService.saveOrUpdate(operation);
			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
		} else {
			if (justHaveDataRule(operation) == 0) {
				message = MutiLangUtil.paramAddSuccess("common.operation");
				userService.save(operation);
				systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
			} else {

				message = "操作 字段规则已存在";
			}
		}
		j.setMsg(message);
		return j;
	}

	public int justHaveDataRule(DataRule dataRule) {
		String sql = "SELECT id FROM sys_data_rule WHERE functionId='" + dataRule.getFunction().getId() + "' AND rule_column='" + dataRule.getRuleColumn() + "' AND rule_conditions='" + dataRule.getRuleConditions() + "'";

		List<String> hasOperList = this.systemService.findListbySql(sql);
		return hasOperList.size();
	}
}
