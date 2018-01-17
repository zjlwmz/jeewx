package cn.emay.modules.wx.im.bean.message;

/**
 * 
 * @Title 语音消息
 * @author zjlwm
 * @date 2017-2-21 下午3:49:17
 *
 */
public class IVoiceMessage extends IMessage{

	/**
	 * 语音地址
	 */
	private String content; 
	
	
	/**
	 * 语音时间
	 */
	private String mediaTime;
	
	
	/**
	 * 宽度
	 */
	private String width;
	
	
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

	public String getMediaTime() {
		return mediaTime;
	}

	public void setMediaTime(String mediaTime) {
		this.mediaTime = mediaTime;
	}

	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	
	
	
}
