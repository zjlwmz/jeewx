package cn.emay.modules.wx.service;

import cn.emay.framework.core.common.service.CommonService;
import cn.emay.modules.wx.entity.ReceiveMessage;


/**
 * 接收消息service接口
 * @author lenovo
 *
 */
public interface ReceiveMessageService extends CommonService{

	public void save(ReceiveMessage receiveMessage );

}
