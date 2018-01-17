package cn.emay.modules.wx.repository.impl;

import org.springframework.stereotype.Repository;

import cn.emay.framework.core.common.dao.impl.GenericBaseCommonDao;
import cn.emay.modules.wx.entity.WxOnlineRecord;
import cn.emay.modules.wx.repository.WxOnlineRecordDao;

/**
 * 在线客服记录单DAO接口实现
 * @author lenovo
 *
 */
@Repository
public class WxOnlineRecordDaoImpl extends GenericBaseCommonDao<WxOnlineRecord, String> implements WxOnlineRecordDao{

}
