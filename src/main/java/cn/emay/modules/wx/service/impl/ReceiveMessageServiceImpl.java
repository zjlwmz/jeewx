package cn.emay.modules.wx.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.emay.framework.core.common.service.impl.CommonServiceImpl;
import cn.emay.modules.wx.entity.ReceiveMessage;
import cn.emay.modules.wx.repository.ReceiveMessageDao;
import cn.emay.modules.wx.service.ReceiveMessageService;

/**
 * 文本接收消息service接口实现
 * @author lenovo
 *
 */
@Service
@Transactional(readOnly=true)
public class ReceiveMessageServiceImpl extends CommonServiceImpl implements ReceiveMessageService {

	@Autowired
	private ReceiveMessageDao receiveMessageDao;
	
	@Transactional
	@Override
	public void save(ReceiveMessage receiveMessage) {
		receiveMessageDao.save(receiveMessage);
	}

}
