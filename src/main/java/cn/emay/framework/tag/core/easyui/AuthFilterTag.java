package cn.emay.framework.tag.core.easyui;

import java.io.IOException;
import java.util.Set;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.springframework.beans.factory.annotation.Autowired;

import cn.emay.framework.common.utils.SpringContextHolder;
import cn.emay.framework.common.utils.ResourceUtil;
import cn.emay.framework.common.utils.oConvertUtils;
import cn.emay.framework.core.constant.Globals;
import cn.emay.modules.sys.entity.Operation;
import cn.emay.modules.sys.service.SystemService;

/**
 * 
 * @Title:AuthFilterTag
 * @description:列表按钮权限过滤
 * @author 赵俊夫
 * @date Aug 24, 2013 7:46:57 PM
 * @version V1.0
 */
public class AuthFilterTag extends TagSupport {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** 列表容器的ID */
	protected String name;
	@Autowired
	private SystemService systemService;

	public int doStartTag() throws JspException {
		return super.doStartTag();
	}

	public int doEndTag() throws JspException {
		JspWriter out = null;
		try {
			out = this.pageContext.getOut();
			out.print(end().toString());
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return EVAL_PAGE;

	}

	protected Object end() {
		StringBuilder out = new StringBuilder();
		getAuthFilter(out);
		return out;
	}

	/**
	 * 获取隐藏按钮的JS代码
	 * 
	 * @param out
	 */
	@SuppressWarnings("unchecked")
	protected void getAuthFilter(StringBuilder out) {
		out.append("<script type=\"text/javascript\">");
		out.append("$(document).ready(function(){");

		if (ResourceUtil.getSessionUserName().getUserName().equals("admin") || !Globals.BUTTON_AUTHORITY_CHECK) {
		} else {
			Set<String> operationCodes = (Set<String>) super.pageContext.getRequest().getAttribute(Globals.OPERATIONCODES);
			if (null != operationCodes) {
				for (String MyoperationCode : operationCodes) {
					if (oConvertUtils.isEmpty(MyoperationCode))
						break;
					systemService = SpringContextHolder.getApplicationContext().getBean(SystemService.class);
					Operation operation = systemService.getEntity(Operation.class, MyoperationCode);
					if (operation.getOperationcode().startsWith(".") || operation.getOperationcode().startsWith("#")) {
						if (operation.getOperationType().intValue() == Globals.OPERATION_TYPE_HIDE) {
							// out.append("$(\""+name+"\").find(\"#"+operation.getOperationcode().replaceAll(" ",
							// "")+"\").hide();");
							out.append("$(\"" + operation.getOperationcode().replaceAll(" ", "") + "\").hide();");
						} else {
							// out.append("$(\""+name+"\").find(\"#"+operation.getOperationcode().replaceAll(" ",
							// "")+"\").find(\":input\").attr(\"disabled\",\"disabled\");");
							out.append("$(\"" + operation.getOperationcode().replaceAll(" ", "") + "\").attr(\"disabled\",\"disabled\");");
							out.append("$(\"" + operation.getOperationcode().replaceAll(" ", "") + "\").find(\":input\").attr(\"disabled\",\"disabled\");");
						}
					}
				}
			}

		}

		out.append("});");
		out.append("</script>");
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
