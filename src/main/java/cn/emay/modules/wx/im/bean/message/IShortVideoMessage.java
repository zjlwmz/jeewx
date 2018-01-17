package cn.emay.modules.wx.im.bean.message;


/**
 * 
 * @Title 短视频
 * @author zjlwm
 * @date 2017-2-22 上午11:47:20
 *
 */
public class IShortVideoMessage  extends IMessage{

	/**
	 * 截图地址
	 */
	private String screenshot;
	
	/**
	 * 消息类型
	 */
	private String msgType="shortvideo"; 
	
	
	/**
	 * 视频地址
	 */
	private String shortvideoUrl;

	public String getScreenshot() {
		return screenshot;
	}

	public void setScreenshot(String screenshot) {
		this.screenshot = screenshot;
	}

	public String getMsgType() {
		return msgType;
	}

	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}

	public String getShortvideoUrl() {
		return shortvideoUrl;
	}

	public void setShortvideoUrl(String shortvideoUrl) {
		this.shortvideoUrl = shortvideoUrl;
	}
	
	
	
	
}
