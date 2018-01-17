package cn.emay.modules.wx.repository.impl;

import org.springframework.stereotype.Repository;

import cn.emay.framework.core.common.dao.impl.GenericBaseCommonDao;
import cn.emay.modules.wx.entity.ReceiveMessage;
import cn.emay.modules.wx.repository.ReceiveMessageDao;

/**
 * 文本接收消息DAO实现
 * @author lenovo
 *
 */
@Repository
public class ReceiveMessageDaoImpl extends GenericBaseCommonDao<ReceiveMessage, String> implements ReceiveMessageDao{

}
