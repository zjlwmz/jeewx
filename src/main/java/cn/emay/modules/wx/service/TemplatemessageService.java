package cn.emay.modules.wx.service;

import java.util.List;

import cn.emay.framework.core.common.service.CommonService;
import cn.emay.modules.wx.entity.Templatemessage;
import cn.emay.modules.wx.entity.TemplatemessageItem;


/**
 * 模板消息service接口
 * @author lenovo
 *
 */
public interface TemplatemessageService extends CommonService{

	public Templatemessage get(String templatemessageId);
	
	public List<Templatemessage>findTemplatemessage(String wechatId);

	
	public void delMain (Templatemessage templatemessage);
	
	public void addMain(Templatemessage templatemessage,List<TemplatemessageItem> templatemessageItemList);
	
	
	public void updateMain(Templatemessage templatemessage,List<TemplatemessageItem> templatemessageItemList);
}
