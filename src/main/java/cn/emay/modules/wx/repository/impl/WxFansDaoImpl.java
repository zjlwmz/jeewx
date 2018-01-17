package cn.emay.modules.wx.repository.impl;

import org.springframework.stereotype.Repository;

import cn.emay.framework.core.common.dao.impl.GenericBaseCommonDao;
import cn.emay.framework.core.common.hibernate.qbc.CriteriaQuery;
import cn.emay.framework.core.common.hibernate.qbc.PageList;
import cn.emay.modules.wx.entity.WxFans;
import cn.emay.modules.wx.repository.WxFansDao;

/**
 * 微信粉丝DAO
 * @author zjlwm
 *
 */
@Repository
public class WxFansDaoImpl extends GenericBaseCommonDao<WxFans, String> implements WxFansDao {

	@Override
	public PageList getPageList(CriteriaQuery cqCriteriaQuery) {
		return this.getPageList(cqCriteriaQuery, true);
	}

}
