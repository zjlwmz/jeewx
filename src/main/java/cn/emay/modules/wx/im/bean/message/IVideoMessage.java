package cn.emay.modules.wx.im.bean.message;

/**
 * 
 * @Title 小视频
 * @author zjlwm
 * @date 2017-2-22 上午11:46:42
 *
 */
public class IVideoMessage extends IMessage{

	/**
	 * 截图地址
	 */
	private String screenshot;
	
	/**
	 * 消息类型
	 */
	private String msgType="video";
	
	/**
	 * 视频地址
	 */
	private String content;


	public String getMsgType() {
		return msgType;
	}

	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}

	

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getScreenshot() {
		return screenshot;
	}

	public void setScreenshot(String screenshot) {
		this.screenshot = screenshot;
	}
	
	
	
	
}
