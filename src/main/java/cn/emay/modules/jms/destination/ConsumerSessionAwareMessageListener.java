package cn.emay.modules.jms.destination;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.springframework.jms.listener.SessionAwareMessageListener;

/**
 * 
 * @Title 可以获取session的MessageListener
 * @author zjlwm
 * @date 2017-2-12 下午3:33:06
 *
 */
public class ConsumerSessionAwareMessageListener implements SessionAwareMessageListener<TextMessage> {

	private Destination destination;

	public void onMessage(TextMessage message, Session session) throws JMSException {
		System.out.println("收到一条消息");
		System.out.println("消息内容是：" + message.getText());
		MessageProducer producer = session.createProducer(destination);
		Message textMessage = session.createTextMessage("ConsumerSessionAwareMessageListener。。。");
		producer.send(textMessage);
	}

	public Destination getDestination() {
		return destination;
	}

	public void setDestination(Destination destination) {
		this.destination = destination;
	}

}
