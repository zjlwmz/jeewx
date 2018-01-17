package cn.emay.modules.sys.service;

import cn.emay.framework.core.common.service.CommonService;
import cn.emay.modules.sys.entity.MutiLang;


/**
 * 多语言service接口
 * @author lenovo
 *
 */
public interface MutiLangService extends CommonService{

	public void initAllMutiLang();
	
	public String getLang(String lang_key);
	
	public String getLang(String lang_key, String args);
	
	public void refleshMutiLangCach();
	
	/**
	 * 更新缓存，插入缓存
	 */
	public void putMutiLang(MutiLang mutiLang);
	
	/**
	 * 更新缓存，插入缓存
	 */
	public void putMutiLang(String langKey,String langCode,String langContext);

}
