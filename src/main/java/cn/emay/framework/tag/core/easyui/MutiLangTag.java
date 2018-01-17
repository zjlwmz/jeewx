package cn.emay.framework.tag.core.easyui;

import java.io.IOException;

import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.springframework.beans.factory.annotation.Autowired;

import cn.emay.framework.common.utils.SpringContextHolder;
import cn.emay.modules.sys.service.MutiLangService;

/**
 * 类描述：MutiLang标签处理类
 * 
 * @author 高留刚
 * @date： 日期：2012-12-7 时间：上午10:17:45
 * @version 1.0
 */
@SuppressWarnings({ "serial" })
public class MutiLangTag extends TagSupport {
	protected String langKey;
	protected String langArg;

	@Autowired
	private static MutiLangService mutiLangService;

	public int doStartTag() throws JspTagException {
		return EVAL_PAGE;
	}

	public int doEndTag() throws JspTagException {
		JspWriter out = null;
		try {
			out = pageContext.getOut();
			String content=end();
			out.print(content);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				out.clear();
				out.close();
			} catch (Exception e2) {
//				System.out.println("-");
//				e2.printStackTrace();
			}
		}
		return EVAL_PAGE;
	}

	public String end() {
		if (mutiLangService == null) {
			try{
				mutiLangService = SpringContextHolder.getApplicationContext().getBean(MutiLangService.class);
			}catch (Exception e) {
				System.out.println("-------------------------------");
				e.printStackTrace();
			}
		}

		String lang_context = mutiLangService.getLang(langKey, langArg);

		return lang_context;
	}

	public void setLangKey(String langKey) {
		this.langKey = langKey;
	}

	public void setLangArg(String langArg) {
		this.langArg = langArg;
	}
}
