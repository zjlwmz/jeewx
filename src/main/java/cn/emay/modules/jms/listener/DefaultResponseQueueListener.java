package cn.emay.modules.jms.listener;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.springframework.stereotype.Component;

/**
 * 
 * @Title defaultResponseQueue对应的监听器
 * @author zjlwm
 * @date 2017-2-12 下午7:00:51
 *
 */
@Component
public class DefaultResponseQueueListener implements MessageListener {

	public void onMessage(Message message) {
		if (message instanceof TextMessage) {
			TextMessage textMessage = (TextMessage) message;
			try {
				System.out.println("DefaultResponseQueueListener接收到发送到defaultResponseQueue的一个文本消息，内容是：" + textMessage.getText());
			} catch (JMSException e) {
				e.printStackTrace();
			}
		}
	}

}
