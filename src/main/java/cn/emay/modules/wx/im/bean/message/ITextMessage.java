package cn.emay.modules.wx.im.bean.message;


/**
 * 文本消息
 * @author lenovo
 *
 */
public class ITextMessage extends IMessage {

	
	/**
	 * 消息内容
	 */
	private String content;
	
	/**
	 * 消息类型
	 */
	private String msgType="text";
	

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getMsgType() {
		return msgType;
	}

	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}

	
}
