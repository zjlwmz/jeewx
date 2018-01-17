package cn.emay.modules.wx.service;

import java.util.List;

import cn.emay.framework.core.common.service.CommonService;
import cn.emay.modules.wx.entity.NewsTemplate;

/**
 * 图文消息模板接口servie接口
 * @author lenovo
 *
 */
public interface NewsTemplateService extends CommonService{

	public NewsTemplate get(String templateId);
	
	public List<NewsTemplate> findNewsTemplate(String accountId);
	
	public void save(NewsTemplate newsTemplate);

}
