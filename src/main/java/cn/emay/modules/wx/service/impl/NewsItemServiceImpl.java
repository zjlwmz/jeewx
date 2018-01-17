package cn.emay.modules.wx.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.emay.framework.core.common.service.impl.CommonServiceImpl;
import cn.emay.modules.wx.entity.NewsItem;
import cn.emay.modules.wx.repository.NewsItemDao;
import cn.emay.modules.wx.service.NewsItemService;

/**
 * 图文消息模板service接口实现
 * @author lenovo
 *
 */
@Service
@Transactional(readOnly=true)
public class NewsItemServiceImpl extends CommonServiceImpl implements NewsItemService {

	@Autowired
	private NewsItemDao newsItemDao;
	
	@Override
	public List<NewsItem> findNewsItemByNewsTemplate(String templateId) {
		List<NewsItem> headerList = newsItemDao.findByProperty(NewsItem.class, "newsTemplateId", templateId);
		return headerList;
	}

	@Override
	public NewsItem get(String id) {
		return newsItemDao.getEntity(NewsItem.class, id);
	}

	@Transactional(readOnly=false)
	@Override
	public void save(NewsItem newsItem) {
		newsItemDao.save(newsItem);
	}

	@Transactional(readOnly=false)
	@Override
	public NewsItem update(NewsItem newsItem) {
		newsItemDao.getSession().clear();
		if(StringUtils.isNotBlank(newsItem.getId())){
			newsItemDao.updateEntitie(newsItem);
		}else{
			newsItemDao.save(newsItem);
		}
		return newsItem;
	}
	
	

	
	
}
