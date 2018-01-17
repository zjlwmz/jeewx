package cn.emay.modules.wx.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.emay.framework.core.common.service.impl.CommonServiceImpl;
import cn.emay.modules.wx.entity.NewsTemplate;
import cn.emay.modules.wx.repository.NewsTemplateDao;
import cn.emay.modules.wx.service.NewsTemplateService;


/**
 * 图文消息模板接口servie接口实现
 * @author lenovo
 *
 */
@Service
@Transactional(readOnly=true)
public class NewsTemplateServiceImpl extends CommonServiceImpl implements NewsTemplateService {

	/**
	 *  图文消息模板DAO接口
	 */
	@Autowired
	private NewsTemplateDao newsTemplateDao;
	
	
	@Override
	public NewsTemplate get(String templateId) {
		return newsTemplateDao.get(NewsTemplate.class, templateId);
	}

	@Override
	public List<NewsTemplate> findNewsTemplate(String accountId) {
		return newsTemplateDao.findByQueryString("from NewsTemplate where accountId='"+accountId+"'");
	}

	
	@Transactional(readOnly=false)
	public void save(NewsTemplate newsTemplate) {
		if(StringUtils.isNotBlank(newsTemplate.getId())){
			newsTemplateDao.updateEntitie(newsTemplate);
		}else{
			newsTemplateDao.save(newsTemplate);
		}
	}

}
