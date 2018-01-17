package cn.emay.modules.sys.web;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.emay.framework.common.utils.MutiLangUtil;
import cn.emay.framework.common.utils.MyBeanUtils;
import cn.emay.framework.common.utils.StringUtil;
import cn.emay.framework.core.common.controller.BaseController;
import cn.emay.framework.core.common.hibernate.qbc.CriteriaQuery;
import cn.emay.framework.core.common.model.json.AjaxJson;
import cn.emay.framework.core.common.model.json.ComboTree;
import cn.emay.framework.core.common.model.json.DataGrid;
import cn.emay.framework.core.common.model.json.TreeGrid;
import cn.emay.framework.core.constant.Globals;
import cn.emay.framework.tag.vo.easyui.ComboTreeModel;
import cn.emay.framework.tag.vo.easyui.TreeGridModel;
import cn.emay.modules.sys.entity.Category;
import cn.emay.modules.sys.entity.Icon;
import cn.emay.modules.sys.service.CategoryServiceI;
import cn.emay.modules.sys.service.SystemService;

/**
 * @Title: Controller
 * @Description: 分类管理
 * @author JueYue
 * @date 2014-09-16 21:50:55
 * @version V1.0
 * 
 */
@Controller
@RequestMapping("/categoryController")
public class CategoryController extends BaseController {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger
			.getLogger(CategoryController.class);

	private static final String CATEGORY_LIST = "system/category/categoryList";
	private static final String CATEGORY_ADD_OR_UPDATE = "system/category/category";

	@Autowired
	private CategoryServiceI categoryService;

	@Autowired
	private SystemService systemService;

	/**
	 * 分类管理列表 页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "category")
	public String category(HttpServletRequest request) {
		return CATEGORY_LIST;
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
	@ResponseBody
	public List<TreeGrid> datagrid(Category category,
			HttpServletRequest request, HttpServletResponse response,
			DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(Category.class, dataGrid);
		if (category.getId() == null || StringUtils.isEmpty(category.getId())) {
			cq.isNull("parent");
		} else {
			cq.eq("parent.code", category.getId());
			category.setId(null);
		}
		// 查询条件组装器
		cn.emay.framework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq,
				category, request.getParameterMap());
		List<Category> list = this.categoryService
				.getListByCriteriaQuery(cq, false);
		List<TreeGrid> treeGrids = new ArrayList<TreeGrid>();
		TreeGridModel treeGridModel = new TreeGridModel();
		treeGridModel.setIdField("code");
		treeGridModel.setSrc("id");
		treeGridModel.setTextField("name");
		treeGridModel.setIcon("icon_iconPath");
		treeGridModel.setParentText("parent_name");
		treeGridModel.setParentId("parent_code");
		treeGridModel.setChildList("list");
		treeGrids = systemService.treegrid(list, treeGridModel);
		return treeGrids;
	}

	/**
	 * 删除分类管理
	 * 
	 * @return
	 */
	@RequestMapping(params = "del")
	@ResponseBody
	public AjaxJson del(Category tSCategory, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		tSCategory = systemService.getEntity(Category.class,
				tSCategory.getId());
		j.setMsg("分类管理删除成功");
		categoryService.delete(tSCategory);
		systemService.addLog(j.getMsg(), Globals.Log_Type_DEL,
				Globals.Log_Leavel_INFO);
		return j;
	}

	/**
	 * 添加分类管理
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "save")
	@ResponseBody
	public AjaxJson save(Category category, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		if (StringUtil.isNotEmpty(category.getId())) {
			j.setMsg("分类管理更新成功");
			Category t = categoryService.get(Category.class,
					category.getId());

			category.getParent().setCode("".equals(t.getParent().getCode())?null:t.getParent().getCode());

			try {
				MyBeanUtils.copyBeanNotNull2Bean(category, t);
				categoryService.saveOrUpdate(t);
				systemService.addLog(j.getMsg(), Globals.Log_Type_UPDATE,
						Globals.Log_Leavel_INFO);
			} catch (Exception e) {
				logger.error(e.getMessage(), e.fillInStackTrace());
				j.setMsg("分类管理更新失败");
			}
		} else {
			j.setMsg("分类管理添加成功");
			categoryService.saveCategory(category);
			systemService.addLog(j.getMsg(), Globals.Log_Type_INSERT,
					Globals.Log_Leavel_INFO);
		}
		return j;
	}

	/**
	 * 分类管理列表页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "addorupdate")
	public String addorupdate(ModelMap map, Category category) {
		if (StringUtil.isNotEmpty(category.getCode())) {
			category = categoryService.findUniqueByProperty(Category.class,
					"code",category.getCode());
			map.put("categoryPage", category);
		}
		map.put("iconlist", systemService.findByProperty(Icon.class,
				"iconType", (short) 1));
		if (category.getParent() != null
				&& StringUtil.isNotEmpty(category.getParent().getCode())) {
			Category parent = categoryService.findUniqueByProperty(Category.class, "code", category.getParent().getCode());
			category.setParent(parent);
			map.put("categoryPage", category);
		}
		return CATEGORY_ADD_OR_UPDATE;
	}

	@RequestMapping(params = "combotree")
	@ResponseBody
	public List<ComboTree> combotree(String selfCode, ComboTree comboTree) {
		CriteriaQuery cq = new CriteriaQuery(Category.class);
		if (StringUtils.isNotEmpty(comboTree.getId())) {
			cq.createAlias("parent", "parent");
			cq.eq("parent.code", comboTree.getId());
		} else if (StringUtils.isNotEmpty(selfCode)) {
			cq.eq("code", selfCode);
		} else {
			cq.isNull("parent");
		}
		cq.add();
		List<Category> categoryList = systemService
				.getListByCriteriaQuery(cq, false);
		List<ComboTree> comboTrees = new ArrayList<ComboTree>();
		ComboTreeModel comboTreeModel = new ComboTreeModel("code", "name", "list");
		comboTrees = systemService.ComboTree(categoryList, comboTreeModel,
				null, false);
		MutiLangUtil.setMutiTree(comboTrees);
		return comboTrees;
	}

	/**
	 * 鉴于树的问题,这里自己加载全部数据来做同步树
	 * 
	 * @param comboTree
	 * @return
	 */
	@RequestMapping(params = "tree")
	@ResponseBody
	public List<ComboTree> tree(String selfCode,ComboTree comboTree, boolean isNew) {
		CriteriaQuery cq = new CriteriaQuery(Category.class);
		if (StringUtils.isNotEmpty(comboTree.getId())) {
			cq.createAlias("parent", "parent");
			cq.eq("parent.code", comboTree.getId());
		} else if (StringUtils.isNotEmpty(selfCode)) {
			cq.eq("code", selfCode);
		} else {
			cq.isNull("parent");
		}
		cq.add();
		List<Category> categoryList = systemService
				.getListByCriteriaQuery(cq, false);
		List<ComboTree> comboTrees = new ArrayList<ComboTree>();
		for (int i = 0; i < categoryList.size(); i++) {
			comboTrees.add(categoryConvertToTree(categoryList.get(i)));
		}
		return comboTrees;
	}

	private ComboTree categoryConvertToTree(Category entity) {
		ComboTree tree = new ComboTree();
		tree.setId(entity.getCode());
		tree.setText(entity.getName());
		tree.setIconCls(entity.getIcon().getIconClas());
		if (entity.getList() != null && entity.getList().size() > 0) {
			List<ComboTree> comboTrees = new ArrayList<ComboTree>();
			for (int i = 0; i < entity.getList().size(); i++) {
				comboTrees.add(categoryConvertToTree(entity.getList().get(
						i)));
			}
			tree.setChildren(comboTrees);
		}
		return tree;
	}
}
