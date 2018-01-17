package cn.emay.modules.wx.im.response;

/**
 * 发送文本消息
 * @author lenovo
 *
 */
public class SendTextMessage extends SendBase{

	/**
	 * 聊天窗口来源类型，从发送消息传递的to里面获取
	 */
	private String type;
	
	/**
	 * 是否我发送的消息，如果为true，则会显示在右方
	 */
	private String mine;
	
	/**
	 * 消息内容
	 */
	private String content;
	
	/**
	 * 是否为系统消息
	 */
	private boolean system;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getMine() {
		return mine;
	}

	public void setMine(String mine) {
		this.mine = mine;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public boolean isSystem() {
		return system;
	}

	public void setSystem(boolean system) {
		this.system = system;
	}
	
	
	
	
	
}
