package cn.emay.modules.sys.web;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import cn.emay.framework.common.utils.IconImageUtil;
import cn.emay.framework.common.utils.MutiLangUtil;
import cn.emay.framework.common.utils.StringUtil;
import cn.emay.framework.common.utils.oConvertUtils;
import cn.emay.framework.core.common.controller.BaseController;
import cn.emay.framework.core.common.hibernate.qbc.CriteriaQuery;
import cn.emay.framework.core.common.model.common.UploadFile;
import cn.emay.framework.core.common.model.json.AjaxJson;
import cn.emay.framework.core.common.model.json.DataGrid;
import cn.emay.framework.core.constant.Globals;
import cn.emay.framework.tag.core.easyui.TagUtil;
import cn.emay.modules.sys.entity.Function;
import cn.emay.modules.sys.entity.Icon;
import cn.emay.modules.sys.entity.Operation;
import cn.emay.modules.sys.service.SystemService;

/**
 * 图标信息处理类
 * 
 * @author 张代浩
 * 
 */
// @Scope("prototype")
@Controller
@RequestMapping("/iconController")
public class IconController extends BaseController {
	private SystemService systemService;

	@Autowired
	public void setSystemService(SystemService systemService) {
		this.systemService = systemService;
	}

	/**
	 * 图标列表页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "icon")
	public ModelAndView icon() {
		return new ModelAndView("system/icon/iconList");
	}

	/**
	 * easyuiAJAX请求数据
	 * 
	 * @param request
	 * @param response
	 * @param dataGrid
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(params = "datagrid")
	public void datagrid(Icon icon, HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(Icon.class, dataGrid);
		cn.emay.framework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, icon);
		cq.add();
		this.systemService.getDataGridReturn(cq, true);
		IconImageUtil.convertDataGrid(dataGrid, request);// 先把数据库的byte存成图片到临时目录，再给每个Icon设置目录路径

		List<Icon> list = dataGrid.getResults();
		for (Icon Icon : list) {
			Icon.setIconName(MutiLangUtil.doMutiLang(Icon.getIconName(), ""));
		}

		TagUtil.datagrid(response, dataGrid);
	}

	/**
	 * 上传图标
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "saveOrUpdateIcon", method = RequestMethod.POST)
	@ResponseBody
	public AjaxJson saveOrUpdateIcon(HttpServletRequest request) throws Exception {
		String message = null;
		AjaxJson j = new AjaxJson();
		Icon icon = new Icon();
		Short iconType = oConvertUtils.getShort(request.getParameter("iconType"));
		String iconName = oConvertUtils.getString(request.getParameter("iconName"));
		String id = request.getParameter("id");
		icon.setId(id);
		icon.setIconName(iconName);
		icon.setIconType(iconType);
		// uploadFile.setBasePath("images/accordion");
		UploadFile uploadFile = new UploadFile(request, icon);
		uploadFile.setCusPath("static/accordion/images");
		uploadFile.setExtend("extend");
		uploadFile.setTitleField("iconclas");
		uploadFile.setRealPath("iconPath");
		uploadFile.setObject(icon);
		uploadFile.setByteField("iconContent");
		uploadFile.setRename(false);
		systemService.uploadFile(uploadFile);
		// 图标的css样式
		String css = "." + icon.getIconClas() + "{background:url('../images/" + icon.getIconClas() + "." + icon.getExtend() + "') no-repeat}";
		write(request, css);
		message = MutiLangUtil.paramAddSuccess("common.icon");
		j.setMsg(message);
		return j;
	}

	/**
	 * 没有上传文件时更新信息
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("deprecation")
	@RequestMapping(params = "update", method = RequestMethod.POST)
	@ResponseBody
	public AjaxJson update(HttpServletRequest request) throws Exception {
		String message = null;
		AjaxJson j = new AjaxJson();
		Short iconType = oConvertUtils.getShort(request.getParameter("iconType"));
		String iconName = java.net.URLDecoder.decode(oConvertUtils.getString(request.getParameter("iconName")));
		String id = request.getParameter("id");
		Icon icon = new Icon();
		if (StringUtil.isNotEmpty(id)) {
			icon = systemService.get(Icon.class, id);
			icon.setId(id);
		}
		icon.setIconName(iconName);
		icon.setIconType(iconType);
		systemService.saveOrUpdate(icon);
		// 图标的css样式
		String css = "." + icon.getIconClas() + "{background:url('../images/" + icon.getIconClas() + "." + icon.getExtend() + "') no-repeat}";
		write(request, css);
		message = "更新成功";
		j.setMsg(message);
		return j;
	}

	/**
	 * 添加图标样式
	 * 
	 * @param request
	 * @param css
	 */
	protected void write(HttpServletRequest request, String css) {
		try {
			String path = request.getSession().getServletContext().getRealPath("/static/accordion/css/icons.css");
			File file = new File(path);
			if (!file.exists()) {
				file.createNewFile();
			}
			FileWriter out = new FileWriter(file, true);
			out.write("\r\n");
			out.write(css);
			out.close();
		} catch (Exception e) {
		}
	}

	/**
	 * 恢复图标（将数据库图标数据写入图标存放的路径下）
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "repair")
	@ResponseBody
	public AjaxJson repair(HttpServletRequest request) throws Exception {
		AjaxJson json = new AjaxJson();
		List<Icon> icons = systemService.loadAll(Icon.class);
		String rootpath = request.getSession().getServletContext().getRealPath("/");
		String csspath = request.getSession().getServletContext().getRealPath("/static/accordion/css/icons.css");
		// 清空CSS文件内容
		clearFile(csspath);
		for (Icon c : icons) {
			File file = new File(rootpath + c.getIconPath());
			if (!file.exists()) {
				byte[] content = c.getIconContent();
				if (content != null) {
					BufferedImage imag = ImageIO.read(new ByteArrayInputStream(content));
					ImageIO.write(imag, "PNG", file);// 输出到 png 文件
				}
			}
			String css = "." + c.getIconClas() + "{background:url('../images/" + c.getIconClas() + "." + c.getExtend() + "') no-repeat}";
			write(request, css);
		}
		json.setMsg(MutiLangUtil.paramAddSuccess("common.icon.style"));
		json.setSuccess(true);
		return json;
	}

	/**
	 * 清空文件内容
	 * 
	 * @param path
	 */
	protected void clearFile(String path) {
		try {
			FileOutputStream fos = new FileOutputStream(new File(path));
			fos.write("".getBytes());
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 删除图标
	 * 
	 * @param icon
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "del")
	@ResponseBody
	public AjaxJson del(Icon icon, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();

		icon = systemService.getEntity(Icon.class, icon.getId());

		boolean isPermit = isPermitDel(icon);

		if (isPermit) {
			systemService.delete(icon);

			message = MutiLangUtil.paramDelSuccess("common.icon");

			systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);

			j.setMsg(message);

			return j;
		}

		message = MutiLangUtil.paramDelFail("common.icon,common.icon.isusing");
		j.setMsg(message);

		return j;
	}

	/**
	 * 检查是否允许删除该图标。
	 * 
	 * @param icon
	 *            图标。
	 * @return true允许；false不允许；
	 */
	private boolean isPermitDel(Icon icon) {
		List<Function> functions = systemService.findByProperty(Function.class, "Icon.id", icon.getId());
		if (functions == null || functions.isEmpty()) {
			return true;
		}
		return false;
	}

	public void upEntity(Icon icon) {
		List<Function> functions = systemService.findByProperty(Function.class, "Icon.id", icon.getId());
		if (functions.size() > 0) {
			for (Function function : functions) {
				function.setIcon(null);
				systemService.saveOrUpdate(function);
			}
		}
		List<Operation> operations = systemService.findByProperty(Operation.class, "Icon.id", icon.getId());
		if (operations.size() > 0) {
			for (Operation Operation : operations) {
				Operation.setIcon(null);
				systemService.saveOrUpdate(Operation);
			}
		}
	}

	/**
	 * 图标页面跳转
	 * 
	 * @param icon
	 * @param req
	 * @return
	 */
	@RequestMapping(params = "addorupdate")
	public ModelAndView addorupdate(Icon icon, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(icon.getId())) {
			icon = systemService.getEntity(Icon.class, icon.getId());
			req.setAttribute("icon", icon);
		}
		return new ModelAndView("system/icon/icons");
	}
}
