package cn.emay.modules.wx.service;

import java.util.List;

import cn.emay.framework.core.common.service.CommonService;
import cn.emay.modules.wx.entity.TextTemplate;

/**
 * 文本消息模板实体service接口
 * @author lenovo
 *
 */
public interface TextTemplateService extends CommonService{
	
	public TextTemplate getTextTemplate(String wechatId,String templateName);
	
	public TextTemplate get(String textTemplateId);
	
	public List<TextTemplate>findTextTemplate(String wechatId);
}
