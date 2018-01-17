package cn.emay.modules.wx.im.request;

/**
 * Im接收文本消息
 * @author lenovo
 *
 */
public class ReceiveTextMessage {
	/**
	 * 文本消息类型
	 */
	private String type;
	
	
	/**
	 * 文本内容
	 */
	private String content;
	
	
	/**
	 * 接收人
	 */
	private String toUser;
	
	/**
	 * 发送人
	 */
	private String fromUser;
	

	/**
	 * 0  微信端
	 * 1 管理系统
	 */
	private String client;
	

	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}


	public String getContent() {
		return content;
	}


	public void setContent(String content) {
		this.content = content;
	}

	public String getToUser() {
		return toUser;
	}


	public void setToUser(String toUser) {
		this.toUser = toUser;
	}


	public String getFromUser() {
		return fromUser;
	}


	public void setFromUser(String fromUser) {
		this.fromUser = fromUser;
	}


	public String getClient() {
		return client;
	}


	public void setClient(String client) {
		this.client = client;
	}


	
	
	
}
