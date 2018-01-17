package cn.emay.modules.wx.repository.impl;

import org.springframework.stereotype.Repository;

import cn.emay.framework.core.common.dao.impl.GenericBaseCommonDao;
import cn.emay.modules.wx.entity.WxChatLogs;
import cn.emay.modules.wx.repository.WxChatLogsDao;

/**
 * 聊天记录DAO实现
 * @author lenovo
 *
 */
@Repository
public class WxChatLogsDaoImpl extends GenericBaseCommonDao<WxChatLogs, String> implements WxChatLogsDao {

}
