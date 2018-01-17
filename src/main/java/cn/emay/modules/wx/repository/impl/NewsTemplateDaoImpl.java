package cn.emay.modules.wx.repository.impl;

import org.springframework.stereotype.Repository;

import cn.emay.framework.core.common.dao.impl.GenericBaseCommonDao;
import cn.emay.modules.wx.entity.NewsTemplate;
import cn.emay.modules.wx.repository.NewsTemplateDao;


/**
 * 图文模板DAO接口实现
 * @author lenovo
 *
 */
@Repository
public class NewsTemplateDaoImpl extends GenericBaseCommonDao<NewsTemplate, String> implements NewsTemplateDao{

}
