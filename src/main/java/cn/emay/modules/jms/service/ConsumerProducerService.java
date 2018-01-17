package cn.emay.modules.jms.service;

import java.io.Serializable;

import javax.jms.Destination;


/**
 * 
 * @Title 客服消息接口
 * @author zjlwm
 * @date 2017-2-12 下午5:25:13
 *
 */
public interface ConsumerProducerService {

	/**
	 * 发送普通的纯文本消息
	 * @param destination
	 * @param message
	 */
	public void sendMessage(Destination destination, String message);
	
	/**
	 * 发送一个ObjectMessage
	 * @param destination
	 * @param obj
	 */
	public void sendMessage(Destination destination, Serializable obj);
	
}
