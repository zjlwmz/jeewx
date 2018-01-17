package cn.emay.modules.wx.repository.impl;

import org.springframework.stereotype.Repository;

import cn.emay.framework.core.common.dao.impl.GenericBaseCommonDao;
import cn.emay.modules.wx.entity.NewsItem;
import cn.emay.modules.wx.repository.NewsItemDao;

/**
 * 
 * @author zjlwm
 * @date 2017-1-14 下午6:29:54
 *
 */
@Repository
public class NewsItemDaoImpl extends GenericBaseCommonDao<NewsItem, String> implements NewsItemDao {

}
