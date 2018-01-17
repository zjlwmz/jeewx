package cn.emay.modules.wx.repository.impl;

import org.springframework.stereotype.Repository;

import cn.emay.framework.core.common.dao.impl.GenericBaseCommonDao;
import cn.emay.modules.wx.entity.MenuEntity;
import cn.emay.modules.wx.repository.MenuEntityDao;

/**
 * 微信菜单DAO接口实现
 * @author lenovo
 *
 */
@Repository
public class MenuEntityDaoImpl extends GenericBaseCommonDao<MenuEntity, String> implements MenuEntityDao{

}
