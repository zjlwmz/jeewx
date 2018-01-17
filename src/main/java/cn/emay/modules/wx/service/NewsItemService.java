package cn.emay.modules.wx.service;

import java.util.List;

import cn.emay.framework.core.common.service.CommonService;
import cn.emay.modules.wx.entity.NewsItem;

/**
 * 图文消息模板service接口
 * @author lenovo
 *
 */
public interface NewsItemService extends CommonService{

	
	public List<NewsItem>findNewsItemByNewsTemplate(String templateId);
	
	
	public NewsItem get(String id);
	
	
	public void save(NewsItem newsItem);
	
	public NewsItem update(NewsItem newsItem);
}
