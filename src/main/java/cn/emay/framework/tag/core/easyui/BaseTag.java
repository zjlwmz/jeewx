package cn.emay.framework.tag.core.easyui;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import jodd.util.StringUtil;
import cn.emay.framework.common.utils.StringUtils;
import cn.emay.framework.common.utils.SysThemesUtil;
import cn.emay.framework.common.utils.oConvertUtils;
import cn.emay.framework.core.enums.SysThemesEnum;

/**
 * 
 * @author 张代浩
 * 
 */
public class BaseTag extends TagSupport {
	private static final long serialVersionUID = 1L;
	protected String type = "default";// 加载类型

	protected String cssTheme;
	
	/**
	 * 是否js为最小js
	 */
	protected String min;

	public String getCssTheme() {
		return cssTheme;
	}

	public void setCssTheme(String cssTheme) {
		this.cssTheme = cssTheme;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getMin() {
		return min;
	}

	public void setMin(String min) {
		this.min = min;
	}

	public int doStartTag() throws JspException {
		return EVAL_PAGE;
	}

	public int doEndTag() throws JspException {
		JspWriter out = null;
		StringBuffer sb = new StringBuffer();
		String types[] = type.split(",");
		try {
			out = this.pageContext.getOut();
			/*
			 * // update-start--Author:longjb Date:20150408 for：手动设置指定属性主题优先
			 * //if (cssTheme == null) {// Cookie[] cookies =
			 * ((HttpServletRequest) super.pageContext
			 * .getRequest()).getCookies(); for (Cookie cookie : cookies) { if
			 * (cookie == null || StringUtils.isEmpty(cookie.getName())) {
			 * continue; } if
			 * (cookie.getName().equalsIgnoreCase("JEECGCSSTHEME")) { cssTheme =
			 * cookie.getValue(); } } //}
			 * 
			 * if(cssTheme==null||"".equals(cssTheme)){ cssTheme="default"; }
			 */
			SysThemesEnum sysThemesEnum = null;
			if (StringUtil.isEmpty(cssTheme) || "null".equals(cssTheme)) {
				sysThemesEnum = SysThemesUtil.getSysTheme((HttpServletRequest) super.pageContext.getRequest());
			} else {
				sysThemesEnum = SysThemesEnum.toEnum(cssTheme);
			}

			/**
			 * 是否为最小js 
			 */
			String suffix="";
			if(StringUtils.isNotBlank(min) && min.equals("true")){
				suffix=".min";
			}
			
			
			// 插入多语言脚本
			String lang = (String) ((HttpServletRequest) this.pageContext.getRequest()).getSession().getAttribute("lang");
			String langjs = StringUtil.replace("<script type=\"text/javascript\" src=\"static/mutiLang/{0}.js\"></script>", "{0}", lang);
			sb.append(langjs);

			if (oConvertUtils.isIn("jquery-webos", types)) {
				sb.append("<script type=\"text/javascript\" src=\"static/sliding/js/jquery-1.7.1.min.js\"></script>");
			} else if (oConvertUtils.isIn("jquery", types)) {
				sb.append("<script type=\"text/javascript\" src=\"static/jquery/jquery-1.8.3.min.js\"></script>");

				sb.append("<script type=\"text/javascript\" src=\"static/jquery/jquery.cookie"+suffix+".js\" ></script>");
				sb.append("<script type=\"text/javascript\" src=\"static/jquery-plugs/storage/jquery.storageapi.min.js\" ></script>");

			}

			if (oConvertUtils.isIn("ckeditor", types)) {
				sb.append("<script type=\"text/javascript\" src=\"static/ckeditor/ckeditor.js\"></script>");
				sb.append("<script type=\"text/javascript\" src=\"static/tools/ckeditorTool.js\"></script>");
			}
			if (oConvertUtils.isIn("ckfinder", types)) {
				sb.append("<script type=\"text/javascript\" src=\"static/ckfinder/ckfinder.js\"></script>");
				sb.append("<script type=\"text/javascript\" src=\"static/tools/ckfinderTool.js\"></script>");
			}
			if (oConvertUtils.isIn("easyui", types)) {
				sb.append("<script type=\"text/javascript\" src=\"static/tools/dataformat"+suffix+".js\"></script>");

				// sb.append("<link id=\"easyuiTheme\" rel=\"stylesheet\" href=\"static/easyui/themes/"+cssTheme+"/easyui.css\" type=\"text/css\"></link>");
				sb.append(SysThemesUtil.getEasyUiTheme(sysThemesEnum));
				sb.append(SysThemesUtil.getEasyUiMainTheme(sysThemesEnum));

				sb.append(SysThemesUtil.getEasyUiIconTheme(sysThemesEnum));
				// sb.append("<link rel=\"stylesheet\" href=\"static/easyui/themes/icon.css\" type=\"text/css\"></link>");
				sb.append("<link rel=\"stylesheet\" type=\"text/css\" href=\"static/accordion/css/accordion.css\">");
				sb.append("<script type=\"text/javascript\" src=\"static/easyui/jquery.easyui.min.1.3.2.js\"></script>");
				sb.append("<script type=\"text/javascript\" src=\"static/easyui/locale/zh-cn"+suffix+".js\"></script>");
				sb.append("<script type=\"text/javascript\" src=\"static/tools/syUtil"+suffix+".js\"></script>");
				sb.append("<script type=\"text/javascript\" src=\"static/easyui/extends/datagrid-scrollview"+suffix+".js\"></script>");
			}
			if (oConvertUtils.isIn("DatePicker", types)) {
				sb.append("<script type=\"text/javascript\" src=\"static/My97DatePicker/WdatePicker.js\"></script>");
			}
			if (oConvertUtils.isIn("jqueryui", types)) {
				sb.append("<link rel=\"stylesheet\" href=\"static/jquery-ui/css/ui-lightness/jquery-ui-1.9.2.custom.min.css\" type=\"text/css\"></link>");
				sb.append("<script type=\"text/javascript\" src=\"static/jquery-ui/js/jquery-ui-1.9.2.custom.min.js\"></script>");
			}
			if (oConvertUtils.isIn("jqueryui-sortable", types)) {
				sb.append("<link rel=\"stylesheet\" href=\"static/jquery-ui/css/ui-lightness/jquery-ui-1.9.2.custom.min.css\" type=\"text/css\"></link>");
				sb.append("<script type=\"text/javascript\" src=\"static/jquery-ui/js/ui/jquery.ui.core"+suffix+".js\"></script>");
				sb.append("<script type=\"text/javascript\" src=\"static/jquery-ui/js/ui/jquery.ui.widget"+suffix+".js\"></script>");
				sb.append("<script type=\"text/javascript\" src=\"static/jquery-ui/js/ui/jquery.ui.mouse"+suffix+".js\"></script>");
				sb.append("<script type=\"text/javascript\" src=\"static/jquery-ui/js/ui/jquery.ui.sortable"+suffix+".js\"></script>");
			}
			if (oConvertUtils.isIn("prohibit", types)) {
				sb.append("<script type=\"text/javascript\" src=\"static/tools/prohibitutil"+suffix+".js\"></script>");
			}
			if (oConvertUtils.isIn("designer", types)) {
//				sb.append("<script type=\"text/javascript\" src=\"static/designer/easyui/jquery-1.7.2.min.js\"></script>");
//				sb.append("<link id=\"easyuiTheme\" rel=\"stylesheet\" href=\"static/designer/easyui/easyui.css\" type=\"text/css\"></link>");
				
//				sb.append("<link rel=\"stylesheet\" href=\"static/designer/easyui/icon.css\" type=\"text/css\"></link>");
				
//				sb.append("<script type=\"text/javascript\" src=\"static/designer/easyui/jquery.easyui.min.1.3.0.js\"></script>");

				// 加载easyui多语言
//				sb.append(StringUtil.replace("<script type=\"text/javascript\" src=\"static/designer/easyui/locale/{0}.js\"></script>", "{0}", lang));

				sb.append("<script type=\"text/javascript\" src=\"static/tools/syUtil"+suffix+".js\"></script>");
				sb.append("<script type=\'text/javascript\' src=\'static/jquery/jquery-autocomplete/lib/jquery.bgiframe.min.js\'></script>");
				sb.append("<script type=\'text/javascript\' src=\'static/jquery/jquery-autocomplete/lib/jquery.ajaxQueue"+suffix+".js\'></script>");
				sb.append("<script type=\'text/javascript\' src=\'static/jquery/jquery-autocomplete/jquery.autocomplete.min.js\'></script>");
				
				
//				sb.append("<link href=\"static/designer/designer.css\" type=\"text/css\" rel=\"stylesheet\" />");
//				sb.append("<script src=\"static/designer/draw2d/wz_jsgraphics.js\"></script>");
//				sb.append("<script src=\'static/designer/draw2d/mootools.js\'></script>");
//				sb.append("<script src=\'static/designer/draw2d/moocanvas.js\'></script>");
//				sb.append("<script src=\'static/designer/draw2d/draw2d.js\'></script>");
//				sb.append("<script src=\"static/designer/MyCanvas.js\"></script>");
//				sb.append("<script src=\"static/designer/ResizeImage.js\"></script>");
//				sb.append("<script src=\"static/designer/event/Start.js\"></script>");
//				sb.append("<script src=\"static/designer/event/End.js\"></script>");
//				sb.append("<script src=\"static/designer/connection/MyInputPort.js\"></script>");
//				sb.append("<script src=\"static/designer/connection/MyOutputPort.js\"></script>");
//				sb.append("<script src=\"static/designer/connection/DecoratedConnection.js\"></script>");
//				sb.append("<script src=\"static/designer/task/Task.js\"></script>");
//				sb.append("<script src=\"static/designer/task/UserTask.js\"></script>");
//				sb.append("<script src=\"static/designer/task/ManualTask.js\"></script>");
//				sb.append("<script src=\"static/designer/task/ServiceTask.js\"></script>");
//				sb.append("<script src=\"static/designer/gateway/ExclusiveGateway.js\"></script>");
//				sb.append("<script src=\"static/designer/gateway/ParallelGateway.js\"></script>");
//				sb.append("<script src=\"static/designer/boundaryevent/TimerBoundary.js\"></script>");
//				sb.append("<script src=\"static/designer/boundaryevent/ErrorBoundary.js\"></script>");
//				sb.append("<script src=\"static/designer/subprocess/CallActivity.js\"></script>");
//				sb.append("<script src=\"static/designer/task/ScriptTask.js\"></script>");
//				sb.append("<script src=\"static/designer/task/MailTask.js\"></script>");
//				sb.append("<script src=\"static/designer/task/ReceiveTask.js\"></script>");
//				sb.append("<script src=\"static/designer/task/BusinessRuleTask.js\"></script>");
//				sb.append("<script src=\"static/designer/designer.js\"></script>");
//				sb.append("<script src=\"static/designer/mydesigner.js\"></script>");

			}
			if (oConvertUtils.isIn("tools", types)) {

				// sb.append("<link rel=\"stylesheet\" href=\"static/tools/css/"+("metro".equals(cssTheme)?"metro/":"")+"common.css\" type=\"text/css\"></link>");
				sb.append(SysThemesUtil.getCommonTheme(sysThemesEnum));

				// sb.append("<script type=\"text/javascript\" src=\"static/lhgDialog/lhgdialog.min.js"+("metro".equals(cssTheme)?"?skin=metro":"")+"\"></script>");
				sb.append(SysThemesUtil.getLhgdialogTheme(sysThemesEnum));
				sb.append(SysThemesUtil.getBootstrapTabTheme(sysThemesEnum));
				sb.append(StringUtil.replace("<script type=\"text/javascript\" src=\"static/tools/curdtools_{0}.js\"></script>", "{0}", lang));

				sb.append("<script type=\"text/javascript\" src=\"static/tools/easyuiextend"+suffix+".js\"></script>");
				sb.append("<script type=\"text/javascript\" src=\"static/jquery-plugs/hftable/jquery-hftable"+suffix+".js\"></script>");
				sb.append("<script type=\"text/javascript\" src=\"static/tools/json2"+suffix+".js\" ></script>");
			}
			if (oConvertUtils.isIn("toptip", types)) {
				sb.append("<link rel=\"stylesheet\" href=\"static/toptip/css/css.css\" type=\"text/css\"></link>");
				sb.append("<script type=\"text/javascript\" src=\"static/toptip/manhua_msgTips.js\"></script>");
			}
			if (oConvertUtils.isIn("autocomplete", types)) {
				sb.append("<link rel=\"stylesheet\" href=\"static/jquery/jquery-autocomplete/jquery.autocomplete.css\" type=\"text/css\"></link>");
				sb.append("<script type=\"text/javascript\" src=\"static/jquery/jquery-autocomplete/jquery.autocomplete.min.js\"></script>");
			}
			if (oConvertUtils.isIn("jeasyuiextensions", types)) {
				
				
//				sb.append("<script src=\"static/jquery-extensions/release/jquery.jdirk.min.js\" type=\"text/javascript\"></script>");
//				sb.append("<link href=\"static/jquery-extensions/icons/icon-all.css\" rel=\"stylesheet\" type=\"text/css\" />");
//				sb.append("<link href=\"static/jquery-extensions/jeasyui-extensions/jeasyui.extensions.css\" rel=\"stylesheet\" type=\"text/css\" />");
//				sb.append("<script src=\"static/jquery-extensions/jeasyui-extensions/jeasyui.extensions.js\" type=\"text/javascript\"></script>");
//				sb.append("<script src=\"static/jquery-extensions/jeasyui-extensions/jeasyui.extensions.linkbutton.js\" type=\"text/javascript\"></script>");
//				sb.append("<script src=\"static/jquery-extensions/jeasyui-extensions/jeasyui.extensions.menu.js\" type=\"text/javascript\"></script>");
//				sb.append("<script src=\"static/jquery-extensions/jeasyui-extensions/jeasyui.extensions.panel.js\" type=\"text/javascript\"></script>");
//				sb.append("<script src=\"static/jquery-extensions/jeasyui-extensions/jeasyui.extensions.window.js\" type=\"text/javascript\"></script>");
//				sb.append("<script src=\"static/jquery-extensions/jeasyui-extensions/jeasyui.extensions.dialog.js\" type=\"text/javascript\"></script>");
//				sb.append("<script src=\"static/jquery-extensions/jeasyui-extensions/jeasyui.extensions.datagrid.js\" type=\"text/javascript\"></script>");
			}
			out.print(sb.toString());
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (out != null) {
				try {

					out.clearBuffer();
					sb.setLength(0);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		}
		return EVAL_PAGE;
	}

}
