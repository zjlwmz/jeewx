package cn.emay.modules.wx.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.emay.framework.core.common.service.impl.CommonServiceImpl;
import cn.emay.modules.wx.entity.MenuEntity;
import cn.emay.modules.wx.repository.MenuEntityDao;
import cn.emay.modules.wx.service.MenuEntityService;

/**
 * 微信菜单service接口
 * @author lenovo
 *
 */
@Service
@Transactional(readOnly=true)
public class MenuEntityServiceImpl extends CommonServiceImpl implements MenuEntityService {

	/**
	 * 微信菜单DAO接口
	 */
	@Autowired
	private MenuEntityDao menuEntityDao;
	
	
	public long getMenuKey(String menuKey){
		return menuEntityDao.getCountForJdbc("select count(*) from wx_menuentity where menukey='"+menuKey+"'");
	}
	
	
	@Override
	public MenuEntity findMenuEntityByMenuKey(String menuKey) {
		return menuEntityDao.findUniqueByProperty(MenuEntity.class, "menuKey", menuKey);
	}

	@Override
	public List<MenuEntity> findMenuEntity(String accountId) {
		return menuEntityDao.findByQueryString("from MenuEntity where accountId='"+accountId+"'");
	}

	@Override
	public List<MenuEntity> findChildMenuEntity(String accountId) {
		String hql = "from MenuEntity where fatherid is null and accountId = '" + accountId+ "'  order by  orders asc";
		return menuEntityDao.findByQueryString(hql);
	}

	@Override
	public MenuEntity get(String id) {
		return menuEntityDao.get(MenuEntity.class, id);
	}

}
