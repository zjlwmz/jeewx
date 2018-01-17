package cn.emay.modules.wx.repository.impl;

import org.springframework.stereotype.Repository;

import cn.emay.framework.core.common.dao.impl.GenericBaseCommonDao;
import cn.emay.modules.wx.entity.WxFansSession;
import cn.emay.modules.wx.repository.WxFansSessionDao;

/**
 * 微信会话DAO实现
 * @author zjlwm
 *
 */
@Repository
public class WxFansSessionDaoImpl extends GenericBaseCommonDao<WxFansSession, String> implements WxFansSessionDao {

}
