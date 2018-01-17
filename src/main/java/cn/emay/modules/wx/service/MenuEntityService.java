package cn.emay.modules.wx.service;

import java.util.List;

import cn.emay.framework.core.common.service.CommonService;
import cn.emay.modules.wx.entity.MenuEntity;


/**
 * 微信菜单service接口
 * @author lenovo
 *
 */
public interface MenuEntityService  extends CommonService{

	public MenuEntity get(String id);
	
	public long getMenuKey(String menuKey);
	
	public MenuEntity findMenuEntityByMenuKey(String menuKey);
	
	public List<MenuEntity>findMenuEntity(String accountId);
	
	public List<MenuEntity>findChildMenuEntity(String accountId);
}
