package cn.emay.modules.wx.im.bean.message;

/**
 * 图片消息
 * @author lenovo
 *
 */
public class IImageMessage extends IMessage{

	/**
	 * 图片地址
	 */
	private String content;
	
	/**
	 * 消息类型
	 */
	private String msgType="image";
	
	
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
