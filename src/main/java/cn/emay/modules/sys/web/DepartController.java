package cn.emay.modules.sys.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import cn.emay.framework.common.mapper.JsonMapper;
import cn.emay.framework.common.poi.excel.ExcelImportUtil;
import cn.emay.framework.common.poi.excel.entity.params.ExportParams;
import cn.emay.framework.common.poi.excel.entity.params.ImportParams;
import cn.emay.framework.common.poi.excel.entity.vo.NormalExcelConstants;
import cn.emay.framework.common.utils.ExceptionUtil;
import cn.emay.framework.common.utils.MutiLangUtil;
import cn.emay.framework.common.utils.MyBeanUtils;
import cn.emay.framework.common.utils.ResourceUtil;
import cn.emay.framework.common.utils.StringUtil;
import cn.emay.framework.common.utils.YouBianCodeUtil;
import cn.emay.framework.common.utils.oConvertUtils;
import cn.emay.framework.core.common.controller.BaseController;
import cn.emay.framework.core.common.dao.jdbc.JdbcDao;
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
import cn.emay.modules.sys.entity.Depart;
import cn.emay.modules.sys.entity.User;
import cn.emay.modules.sys.entity.UserOrg;
import cn.emay.modules.sys.service.DepartService;
import cn.emay.modules.sys.service.SystemService;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

/**
 * 部门信息处理类
 * 
 * @author 张代浩
 * 
 */
@Controller
@RequestMapping("/departController")
public class DepartController extends BaseController {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(DepartController.class);


	@Autowired
	private SystemService systemService;
	
	@Autowired
	private DepartService departService;


	
	@ModelAttribute("depart")
	public Depart get(@RequestParam(required=false) String id) {
		if (StringUtils.isNotBlank(id)){
			return departService.get(id);
		}else{
			return new Depart();
		}
	}
	
	/**
	 * 部门列表页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "depart")
	public ModelAndView depart() {
		return new ModelAndView("system/depart/departList");
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
		CriteriaQuery cq = new CriteriaQuery(Depart.class, dataGrid);
		systemService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}

	/**
	 * 删除部门：
	 * <ul>
	 * 组织机构下存在子机构时
	 * <li>不允许删除 组织机构</li>
	 * </ul>
	 * <ul>
	 * 组织机构下存在用户时
	 * <li>不允许删除 组织机构</li>
	 * </ul>
	 * <ul>
	 * 组织机构下 不存在子机构 且 不存在用户时
	 * <li>删除 组织机构-角色 信息</li>
	 * <li>删除 组织机构 信息</li>
	 * </ul>
	 * 
	 * @return 删除的结果信息
	 */
	@RequestMapping(params = "del")
	@ResponseBody
	public AjaxJson del(Depart depart, HttpServletRequest request) {
		String message = null;
		AjaxJson ajaxJson = new AjaxJson();
		message = MutiLangUtil.paramDelSuccess("common.department");
		if (depart.getDeparts().size() == 0) {
			Long userCount = systemService.getCountForJdbc("select count(1) from sys_user_org where org_id='" + depart.getId() + "'");
			if (userCount == 0) { // 组织机构下没有用户时，该组织机构才允许删除。
				systemService.executeSql("delete from sys_role_org where org_id=?", depart.getId());
				systemService.delete(depart);
				systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
			}
		} else {
			message = MutiLangUtil.paramDelFail("common.department");
		}
		ajaxJson.setMsg(message);
		return ajaxJson;
	}

	public void upEntity(Depart depart) {
		List<User> users = systemService.findByProperty(User.class, "depart.id", depart.getId());
		if (users.size() > 0) {
			for (User tsUser : users) {
				systemService.delete(tsUser);
			}
		}
	}

	/**
	 * 添加部门
	 * @param depart
	 * @return
	 */
	@RequestMapping(params = "save")
	@ResponseBody
	public AjaxJson save(Depart depart, HttpServletRequest request) {
		String message = null;
//		// 设置上级部门
//		String pid = request.getParameter("depart.id");
//		if (pid.equals("")) {
//			depart.setDepart(null);
//		}
		AjaxJson ajaxJson = new AjaxJson();
		if (StringUtils.isNotBlank(depart.getId())) {
			message = MutiLangUtil.paramUpdSuccess("common.department");
			departService.saveOrUpdate(depart);
			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
		} else {
			message = MutiLangUtil.paramAddSuccess("common.department");
			departService.save(depart);
			systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
		}
		ajaxJson.setMsg(message);
		return ajaxJson;
	}

	@RequestMapping(params = "add")
	public ModelAndView add(Depart depart, Model model) {
		Depart departNew=new Depart();
		model.addAttribute("depart",departNew);
		model.addAttribute("parent", depart);
		return new ModelAndView("system/depart/depart");
	}

	/**
	 * 部门列表页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "update")
	public ModelAndView update(String id, Model model) {
		List<Depart> departList = systemService.getList(Depart.class);
		model.addAttribute("departList", departList);
		if (StringUtils.isNotBlank(id)) {
			Depart depart = systemService.getEntity(Depart.class, id);
			Depart parent=depart.getDepart();
			model.addAttribute("parent", parent);
			model.addAttribute("depart", depart);
		}
		return new ModelAndView("system/depart/depart");
	}

	/**
	 * 父级权限列表
	 * 
	 * @param request
	 * @param comboTree
	 * @return
	 */
	@RequestMapping(params = "setPFunction")
	@ResponseBody
	public List<ComboTree> setPFunction(HttpServletRequest request, ComboTree comboTree) {
		CriteriaQuery cq = new CriteriaQuery(Depart.class);
		if (null != request.getParameter("selfId")) {
			cq.notEq("id", request.getParameter("selfId"));
		}
		if (comboTree.getId() != null) {
			cq.eq("depart.id", comboTree.getId());
		}
		if (comboTree.getId() == null) {
			cq.isNull("depart");
		}
		cq.add();
		List<Depart> departsList = systemService.getListByCriteriaQuery(cq, false);
		List<ComboTree> comboTrees = new ArrayList<ComboTree>();
		ComboTreeModel comboTreeModel = new ComboTreeModel("id", "departname", "departs");
		comboTrees = systemService.ComboTree(departsList, comboTreeModel, null, true);
		return comboTrees;

	}

	/**
	 * 部门列表，树形展示
	 * 
	 * @param request
	 * @param response
	 * @param treegrid
	 * @return
	 */
	@RequestMapping(params = "departgrid")
	@ResponseBody
	public Object departgrid(String departname, HttpServletRequest request, HttpServletResponse response, TreeGrid treegrid) {
		departService.getSession().clear();
		CriteriaQuery cq = new CriteriaQuery(Depart.class);
		Depart depart=new Depart();
		if ("yes".equals(request.getParameter("isSearch"))) {
			treegrid.setId(null);
			depart.setId(null);
		}
		
		if(null!=departname){
			HqlGenerateUtil.installHql(cq, depart);
		}
		
		if (null!=treegrid.getId()) {
			cq.eq("depart.id", treegrid.getId());
		}
		
		if (null==treegrid.getId()) {
			cq.isNull("depart");
		}
		
		cq.add();
		
		List<TreeGrid> departList = null;
		
		departList = departService.getListByCriteriaQuery(cq, false);
		if (departList.size() == 0 && depart.getDepartname() != null) {
			cq = new CriteriaQuery(Depart.class);
			Depart parDepart = new Depart();
			depart.setDepart(parDepart);
			HqlGenerateUtil.installHql(cq, depart);
			departList = departService.getListByCriteriaQuery(cq, false);
		}
		
		List<TreeGrid> treeGrids = new ArrayList<TreeGrid>();
		TreeGridModel treeGridModel = new TreeGridModel();
		treeGridModel.setTextField("departname");
		treeGridModel.setParentText("depart_departname");
		treeGridModel.setParentId("depart_id");
		treeGridModel.setSrc("description");
		treeGridModel.setIdField("id");
		treeGridModel.setChildList("departs");
		
		Map<String, Object> fieldMap = new HashMap<String, Object>();
		fieldMap.put("orgCode", "orgCode");
		fieldMap.put("orgType", "orgType");
		fieldMap.put("mobile", "mobile");
		fieldMap.put("fax", "fax");
		fieldMap.put("address", "address");
		treeGridModel.setFieldMap(fieldMap);
		treeGrids = systemService.treegrid(departList, treeGridModel);

		JSONArray jsonArray = new JSONArray();
		for (TreeGrid treeGrid : treeGrids) {
			jsonArray.add(JSON.parse(treeGrid.toJson()));
		}
		return jsonArray;
	}

	// ----
	/**
	 * 方法描述: 查看成员列表 作 者： yiming.zhang 日 期： Dec 4, 2013-8:53:39 PM
	 * 
	 * @param request
	 * @param departid
	 * @return 返回类型： ModelAndView
	 */
	@RequestMapping(params = "userList")
	public ModelAndView userList(String departid,Model model) {
		model.addAttribute("departid", departid);
		return new ModelAndView("system/depart/departUserList");
	}

	/**
	 * 方法描述: 成员列表dataGrid 作 者： yiming.zhang 日 期： Dec 4, 2013-10:40:17 PM
	 * 
	 * @param user
	 * @param request
	 * @param response
	 * @param dataGrid
	 *            返回类型： void
	 */
	@RequestMapping(params = "userDatagrid")
	public void userDatagrid(String departid, HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(User.class, dataGrid);
		// 查询条件组装器
//		HqlGenerateUtil.installHql(cq, user);
		if (!StringUtil.isEmpty(departid)) {

			DetachedCriteria dc = cq.getDetachedCriteria();
			DetachedCriteria dcDepart = dc.createCriteria("userOrgList");
			dcDepart.add(Restrictions.eq("depart.id", departid));
			// 这种方式也是可以的
			// DetachedCriteria dcDepart = dc.createAlias("userOrgList",
			// "userOrg");
			// dcDepart.add(Restrictions.eq("userOrg.tsDepart.id", departid));
		}
		Short[] userstate = new Short[] { Globals.User_Normal, Globals.User_ADMIN };
		cq.in("status", userstate);
		cq.add();
		this.systemService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}

	
	
	// ----

	/**
	 * 获取机构树-combotree
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "getOrgTree")
	@ResponseBody
	public List<ComboTree> getOrgTree(HttpServletRequest request) {
		// findHql不能处理is null条件
		List<Depart> departsList =departService.findFirstParent();
		List<ComboTree> comboTrees = new ArrayList<ComboTree>();
		ComboTreeModel comboTreeModel = new ComboTreeModel("id", "departname", "departs");
		comboTrees = systemService.ComboTree(departsList, comboTreeModel, null, true);
		return comboTrees;
	}

	/**
	 * 添加 用户到组织机构 的页面 跳转
	 * 
	 * @param req
	 *            request
	 * @return 处理结果信息
	 */
	@RequestMapping(params = "goAddUserToOrg")
	public ModelAndView goAddUserToOrg(String orgId,Model model) {
		model.addAttribute("orgId", orgId);
		return new ModelAndView("system/depart/noCurDepartUserList");
	}

	/**
	 * 获取 除当前 组织之外的用户信息列表
	 * 
	 * @param request
	 *            request
	 * @return 处理结果信息
	 */
	@RequestMapping(params = "addUserToOrgList")
	public void addUserToOrgList(User user, HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		String orgId = request.getParameter("orgId");

		CriteriaQuery cq = new CriteriaQuery(User.class, dataGrid);
		HqlGenerateUtil.installHql(cq, user);

		// 获取 当前组织机构的用户信息
		CriteriaQuery subCq = new CriteriaQuery(UserOrg.class);
		subCq.setProjection(Property.forName("tsUser.id"));
		subCq.eq("depart.id", orgId);
		subCq.add();

		cq.add(Property.forName("id").notIn(subCq.getDetachedCriteria()));
		cq.add();

		systemService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}

	/**
	 * 添加 用户到组织机构
	 * 
	 * @param req
	 *            request
	 * @return 处理结果信息
	 */
	@RequestMapping(params = "doAddUserToOrg")
	@ResponseBody
	public AjaxJson doAddUserToOrg(HttpServletRequest req) {
		String message = null;
		AjaxJson ajaxJson = new AjaxJson();
		Depart depart = systemService.getEntity(Depart.class, req.getParameter("orgId"));
		saveOrgUserList(req, depart);
		message = MutiLangUtil.paramAddSuccess("common.user");
		ajaxJson.setMsg(message);

		return ajaxJson;
	}

	/**
	 * 保存 组织机构-用户 关系信息
	 * 
	 * @param request
	 *            request
	 * @param depart
	 *            depart
	 */
	private void saveOrgUserList(HttpServletRequest request, Depart depart) {
		String orgIds = oConvertUtils.getString(request.getParameter("userIds"));

		List<UserOrg> userOrgList = new ArrayList<UserOrg>();
		List<String> userIdList = extractIdListByComma(orgIds);
		for (String userId : userIdList) {
			User user = new User();
			user.setId(userId);

			UserOrg userOrg = new UserOrg();
			userOrg.setUser(user);
			userOrg.setDepart(depart);

			userOrgList.add(userOrg);
		}
		if (!userOrgList.isEmpty()) {
			systemService.batchSave(userOrgList);
		}
	}

	/**
	 * 用户选择机构列表跳转页面
	 * 
	 * @return
	 */
	@RequestMapping(params = "departSelect")
	public String departSelect(String orgIds,Model model) {
		model.addAttribute("orgIds", orgIds);
		return "system/depart/departSelect";
	}

	/**
	 * 角色显示列表
	 * 
	 * @param response
	 *            response
	 * @param dataGrid
	 *            dataGrid
	 */
	@RequestMapping(params = "departSelectDataGrid")
	public void datagridRole(HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(Depart.class, dataGrid);
		systemService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}

	/**
	 * 导入功能跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "upload")
	public ModelAndView upload(Model model) {
		model.addAttribute("controller_name", "departController");
		return new ModelAndView("common/upload/pub_excel_upload");
	}

	/**
	 * 导出excel
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "exportXls")
	public String exportXls(Depart tsDepart, HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid, ModelMap modelMap) {
		CriteriaQuery cq = new CriteriaQuery(Depart.class, dataGrid);
		HqlGenerateUtil.installHql(cq, tsDepart, request.getParameterMap());
		cq.addOrder("orgCode", SortDirection.asc);
		List<Depart> tsDeparts = this.systemService.getListByCriteriaQuery(cq, false);

		modelMap.put(NormalExcelConstants.FILE_NAME, "组织机构表");
		modelMap.put(NormalExcelConstants.CLASS, Depart.class);
		modelMap.put(NormalExcelConstants.PARAMS, new ExportParams("组织机构表列表", "导出人:" + ResourceUtil.getSessionUserName().getRealName(), "导出信息"));
		modelMap.put(NormalExcelConstants.DATA_LIST, tsDeparts);
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
	public String exportXlsByT(Depart tsDepart, HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid, ModelMap modelMap) {
		modelMap.put(NormalExcelConstants.FILE_NAME, "组织机构表");
		modelMap.put(NormalExcelConstants.CLASS, Depart.class);
		modelMap.put(NormalExcelConstants.PARAMS, new ExportParams("组织机构表列表", "导出人:" + ResourceUtil.getSessionUserName().getRealName(), "导出信息"));
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
				List<Depart> tsDeparts = ExcelImportUtil.importExcel(file.getInputStream(), Depart.class, params);
				for (Depart tsDepart : tsDeparts) {
					String orgCode = tsDepart.getOrgCode();
					List<Depart> departs = systemService.findByProperty(Depart.class, "orgCode", orgCode);
					if (departs.size() != 0) {
						Depart depart = departs.get(0);
						MyBeanUtils.copyBeanNotNull2Bean(tsDepart, depart);
						systemService.saveOrUpdate(depart);
					} else {
						tsDepart.setOrgType(tsDepart.getOrgType().substring(0, 1));
						String orgcode = tsDepart.getOrgCode();
						String parentOrgCode = orgcode.substring(0, orgcode.length() - 3);
						Depart parentDept = (Depart) systemService.getSession().createSQLQuery("select * from sys_depart where ORG_CODE = :parentOrgCode").addEntity(Depart.class).setString("parentOrgCode", parentOrgCode).list().get(0);
						tsDepart.setDepart(parentDept);
						systemService.save(tsDepart);
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

	@RequestMapping(params = "getDepartInfo")
	@ResponseBody
	public AjaxJson getDepartInfo(HttpServletRequest request, HttpServletResponse response) {

		AjaxJson ajaxJson = new AjaxJson();

		String orgIds = request.getParameter("orgIds");

		String[] ids = new String[] {};
		if (StringUtils.isNotBlank(orgIds)) {
			orgIds = orgIds.substring(0, orgIds.length() - 1);
			ids = orgIds.split("\\,");
		}

		String parentid = request.getParameter("parentid");

		List<Depart> tSDeparts = new ArrayList<Depart>();

		StringBuffer hql = new StringBuffer(" from Depart t where 1=1 ");
		if (StringUtils.isNotBlank(parentid)) {

			Depart dePart = this.systemService.getEntity(Depart.class, parentid);

			hql.append(" and depart = ?");
			tSDeparts = this.systemService.findHql(hql.toString(), dePart);
		} else {
			hql.append(" and t.orgType = ?");
			tSDeparts = this.systemService.findHql(hql.toString(), "1");
		}
		List<Map<String, Object>> dateList = new ArrayList<Map<String, Object>>();
		if (tSDeparts.size() > 0) {
			Map<String, Object> map = null;
			String sql = null;
			Object[] params = null;
			for (Depart depart : tSDeparts) {
				map = new HashMap<String, Object>();
				map.put("id", depart.getId());
				map.put("name", depart.getDepartname());

				if (ids.length > 0) {
					for (String id : ids) {
						if (id.equals(depart.getId())) {
							map.put("checked", true);
						}
					}
				}

				if (StringUtils.isNotBlank(parentid)) {
					map.put("pId", parentid);
				} else {
					map.put("pId", "1");
				}
				// 根据id判断是否有子节点
				sql = "select count(1) from sys_depart t where t.parentdepartid = ?";
				params = new Object[] { depart.getId() };
				long count = this.systemService.getCountForJdbcParam(sql, params);
				if (count > 0) {
					map.put("isParent", true);
				}
				dateList.add(map);
			}
		}
		ajaxJson.setMsg(JsonMapper.getInstance().toJson(dateList));
		return ajaxJson;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/*
	 * *****************部门管理操作****************************
	 */

//	/**
//	 * 部门列表页面跳转
//	 * 
//	 * @return
//	 */
//	@RequestMapping(params = "depart")
//	public ModelAndView depart() {
//		return new ModelAndView("system/depart/departList");
//	}

	/**
	 * easyuiAJAX请求数据
	 * 
	 * @param request
	 * @param response
	 * @param dataGrid
	 */

	@RequestMapping(params = "datagridDepart")
	public void datagridDepart(HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(Depart.class, dataGrid);
		this.systemService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}

	/**
	 * 删除部门
	 * 
	 * @return
	 */
	@RequestMapping(params = "delDepart")
	@ResponseBody
	public AjaxJson delDepart(Depart depart, HttpServletRequest request) {
		String message = null;
		AjaxJson ajaxJson = new AjaxJson();
		depart = systemService.getEntity(Depart.class, depart.getId());
		message = "部门: " + depart.getDepartname() + "被删除 成功";
		systemService.delete(depart);
		systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);

		return ajaxJson;
	}

	/**
	 * 添加部门
	 * 
	 * @param depart
	 * @return
	 */
	@RequestMapping(params = "saveDepart")
	@ResponseBody
	public AjaxJson saveDepart(Depart depart,@RequestParam(value="depart.id")String pid, HttpServletRequest request) {
		String message = null;
		AjaxJson ajaxJson = new AjaxJson();
		Depart paretDept =null;
		if (StringUtils.isNotBlank(pid)) {
			paretDept = departService.get(pid);//systemService.findUniqueByProperty(Depart.class, "id", pid);
		}
		if (StringUtils.isNotBlank(depart.getId())) {
			if(null!=paretDept){
				String localMaxCode = getMaxLocalCode(paretDept.getOrgCode());
				depart.setOrgCode(YouBianCodeUtil.getSubYouBianCode(paretDept.getOrgCode(), localMaxCode));
			}else{
				String localMaxCode = getMaxLocalCode(null);
				depart.setOrgCode(YouBianCodeUtil.getNextYouBianCode(localMaxCode));
			}
			depart.setDepart(paretDept);
			departService.save(depart);
			message = MutiLangUtil.paramUpdSuccess("common.department");
			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);

		} else {
			departService.save(depart);
			message = MutiLangUtil.paramAddSuccess("common.department");
			systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);

		}
		
		ajaxJson.setMsg(message);
		
		return ajaxJson;
	}

	private synchronized String getMaxLocalCode(String parentCode) {
		if (oConvertUtils.isEmpty(parentCode)) {
			parentCode = "";
		}
		int localCodeLength = parentCode.length() + YouBianCodeUtil.zhanweiLength;
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT org_code FROM sys_depart");

		if (ResourceUtil.getJdbcUrl().indexOf(JdbcDao.DATABSE_TYPE_SQLSERVER) != -1) {
			sb.append(" where LEN(org_code) = ").append(localCodeLength);
		} else {
			sb.append(" where LENGTH(org_code) = ").append(localCodeLength);
		}

		if (oConvertUtils.isNotEmpty(parentCode)) {
			sb.append(" and  org_code like '").append(parentCode).append("%'");
		}

		sb.append(" ORDER BY org_code DESC");
		List<Map<String, Object>> objMapList = systemService.findForJdbc(sb.toString(), 1, 1);
		String returnCode = null;
		if (objMapList != null && objMapList.size() > 0) {
			returnCode = (String) objMapList.get(0).get("org_code");
		}

		return returnCode;
	}

	/**
	 * 部门列表页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "addorupdateDepart")
	public ModelAndView addorupdateDepart(Depart depart, Model model) {
		List<Depart> departList = systemService.getList(Depart.class);
		model.addAttribute("departList", departList);
		if (depart.getId() != null) {
			model.addAttribute("depart", depart);
		}
		return new ModelAndView("system/depart/depart");
	}

}
