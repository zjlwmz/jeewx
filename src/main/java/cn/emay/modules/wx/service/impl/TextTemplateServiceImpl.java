package cn.emay.modules.wx.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.emay.framework.core.common.service.impl.CommonServiceImpl;
import cn.emay.modules.wx.entity.TextTemplate;
import cn.emay.modules.wx.repository.TextTemplateDao;
import cn.emay.modules.wx.service.TextTemplateService;


/**
 * 文本消息模板实体service接口实现
 * @author lenovo
 *
 */
@Service
@Transactional(readOnly=true)
public class TextTemplateServiceImpl extends CommonServiceImpl implements TextTemplateService {

	@Autowired
	private TextTemplateDao textTemplateDao;
	
	@Override
	public TextTemplate getTextTemplate(String wechatId, String templateName) {
		List<TextTemplate>textTemplateList=textTemplateDao.findByQueryString("from TextTemplate where wechatId='"+wechatId+"' and templateName='"+templateName+"'");
		if(textTemplateList.size()>0){
			return textTemplateList.get(0);
		}
		return null;
	}

	@Override
	public TextTemplate get(String textTemplateId) {
		return textTemplateDao.getEntity(TextTemplate.class, textTemplateId);
	}

	@Override
	public List<TextTemplate> findTextTemplate(String wechatId) {
		return textTemplateDao.findByQueryString("from TextTemplate where wechatId='"+wechatId+"'");
	}

}
