package cn.emay.modules.wx.repository.impl;

import org.springframework.stereotype.Repository;

import cn.emay.framework.core.common.dao.impl.GenericBaseCommonDao;
import cn.emay.modules.wx.entity.WxLocation;
import cn.emay.modules.wx.repository.WxLocationDao;

/**
 * 位置上报DAO实现
 * @author lenovo
 *
 */
@Repository
public class WxLocationDaoImpl extends GenericBaseCommonDao<WxLocation,String> implements WxLocationDao{

}
