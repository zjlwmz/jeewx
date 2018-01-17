package cn.emay.modules.wx.repository.impl;

import org.springframework.stereotype.Repository;

import cn.emay.framework.core.common.dao.impl.GenericBaseCommonDao;
import cn.emay.modules.wx.entity.TextTemplate;
import cn.emay.modules.wx.repository.TextTemplateDao;


/**
 *  文本消息模DAO接口实现
 * @author lenovo
 *
 */
@Repository
public class TextTemplateDaoImpl extends GenericBaseCommonDao<TextTemplate, String> implements TextTemplateDao{

}
