package cn.emay.modules.wx.repository.impl;

import org.springframework.stereotype.Repository;

import cn.emay.framework.core.common.dao.impl.GenericBaseCommonDao;
import cn.emay.modules.wx.entity.Templatemessage;
import cn.emay.modules.wx.repository.TemplatemessageDao;


/**
 * 模板消息DAO接口实现
 * @author lenovo
 *
 */
@Repository
public class TemplatemessageDaoImpl extends GenericBaseCommonDao<Templatemessage, String> implements TemplatemessageDao{

}
