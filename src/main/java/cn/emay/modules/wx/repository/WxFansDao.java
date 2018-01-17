package cn.emay.modules.wx.repository;

import cn.emay.framework.core.common.dao.IGenericBaseCommonDao;
import cn.emay.framework.core.common.hibernate.qbc.CriteriaQuery;
import cn.emay.framework.core.common.hibernate.qbc.PageList;


/**
 * 粉丝DAO接口
 * 
 * @author zjlwm
 * 
 */
public interface WxFansDao extends IGenericBaseCommonDao{
	
	public PageList getPageList(final CriteriaQuery cqCriteriaQuery);
}
