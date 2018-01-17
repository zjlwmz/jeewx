package cn.emay.modules.sys.service.impl;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.emay.framework.common.utils.BrowserUtils;
import cn.emay.framework.common.utils.ResourceUtil;
import cn.emay.framework.common.utils.StringUtil;
import cn.emay.framework.common.utils.StringUtils;
import cn.emay.framework.core.common.service.impl.CommonServiceImpl;
import cn.emay.modules.sys.entity.MutiLang;
import cn.emay.modules.sys.service.MutiLangService;

/**
 * 多语言service接口实现
 * @author lenovo
 *
 */
@Service("mutiLangService")
@Transactional
public class MutiLangServiceImpl extends CommonServiceImpl implements MutiLangService {

	@Autowired
	private HttpServletRequest request;

	/** 初始化语言信息，TOMCAT启动时直接加入到内存中 **/
	public void initAllMutiLang() {
		List<MutiLang> mutiLang = this.commonDao.loadAll(MutiLang.class);

		for (MutiLang MutiLang : mutiLang) {
			ResourceUtil.mutiLangMap.put(MutiLang.getLangKey() + "_" + MutiLang.getLangCode(), MutiLang.getLangContext());
		}
	}

	/**
	 * 更新缓存，插入缓存
	 */
	public void putMutiLang(String langKey, String langCode, String langContext) {
		ResourceUtil.mutiLangMap.put(langKey + "_" + langCode, langContext);
	}

	/**
	 * 更新缓存，插入缓存
	 */
	public void putMutiLang(MutiLang mutiLang) {
		ResourceUtil.mutiLangMap.put(mutiLang.getLangKey() + "_" + mutiLang.getLangCode(), mutiLang.getLangContext());
	}

	/** 取 o_muti_lang.lang_key 的值返回当前语言的值 **/
	public String getLang(String langKey) {
		String language = BrowserUtils.getBrowserLanguage(request);

		if (request.getSession().getAttribute("lang") != null) {
			language = (String) request.getSession().getAttribute("lang");
		}

		String langContext = ResourceUtil.mutiLangMap.get(langKey + "_" + language);

		if (StringUtil.isEmpty(langContext)) {
			langContext = ResourceUtil.mutiLangMap.get("common.notfind.langkey" + "_" + request.getSession().getAttribute("lang"));
			if(StringUtils.isBlank(langKey)){
				langKey="";
			}
			if ("null".equals(langContext) || langContext == null || langKey.startsWith("?")) {
				langContext = "";
			}
			langContext = langContext + langKey;
		}
		return langContext;
	}

	public String getLang(String lanKey, String langArg) {
		String langContext = StringUtil.getEmptyString();
		if (StringUtil.isEmpty(langArg)) {
			langContext = getLang(lanKey);
		} else {
			String[] argArray = langArg.split(",");
			langContext = getLang(lanKey);

			for (int i = 0; i < argArray.length; i++) {
				String langKeyArg = argArray[i].trim();
				String langKeyContext = getLang(langKeyArg);
				langContext = StringUtil.replace(langContext, "{" + i + "}", langKeyContext);
			}
		}
		return langContext;
	}

	/** 刷新多语言cach **/
	public void refleshMutiLangCach() {
		ResourceUtil.mutiLangMap.clear();
		initAllMutiLang();
	}

}