package cn.emay.modules.jms.listener;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.springframework.stereotype.Component;

/**
 * 
 * @Title 
 * @author zjlwm
 * @date 2017-2-12 下午7:19:58
 *
 */
@Component
public class WeixinMessageListener implements MessageListener {

	public void onMessage(Message message) {
		if (message instanceof TextMessage) {
			TextMessage textMessage = (TextMessage) message;
			try {
				System.out.println("WeixinMessageListener接收到发送到defaultResponseQueue的一个文本消息，内容是：" + textMessage.getText());
			} catch (JMSException e) {
				e.printStackTrace();
			}
		}
	}
}
