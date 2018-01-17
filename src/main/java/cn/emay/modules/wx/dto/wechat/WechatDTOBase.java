package cn.emay.modules.wx.dto.wechat;

/**
 * 聊天记录DTO
 * @author lenovo
 *
 */
public class WechatDTOBase {

	
	/**
	 * 聊天类型
	 * 0 朋友、1群组
	 */
	private String  type;
	
	/**
	 * 消息类型
	 */
	private String messageType;
	
	/**
	 * 好友头像
	 */
	private String avatar;
	
	
	/**
	 * 用户名称
	 */
	private String userName;
	
	
	/**
	 * 是否我发送的消息，如果为true，则会显示在右方
	 */
	private boolean mine;
	
	
	
	/**
	 * 聊天文本内容
	 */
	private String content;
	
	
	/**
	 * 图片
	 */
	private String picUrl;
	
	/**
	 * 语音路径
	 */
	private String voiceUrl;
	
	
	/**
	 * 视频
	 */
	private String videoUrl;
	
	
	/**
	 * 短视频
	 */
	private String shortVideoUrl;
	
	
	/**
	 * 时间
	 */
	private String timestamp;
	
	
	
	
	
	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}


	public String getMessageType() {
		return messageType;
	}


	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}


	public String getContent() {
		return content;
	}


	public void setContent(String content) {
		this.content = content;
	}


	public String getPicUrl() {
		return picUrl;
	}


	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}


	public String getVoiceUrl() {
		return voiceUrl;
	}


	public void setVoiceUrl(String voiceUrl) {
		this.voiceUrl = voiceUrl;
	}


	public String getVideoUrl() {
		return videoUrl;
	}


	public void setVideoUrl(String videoUrl) {
		this.videoUrl = videoUrl;
	}


	public String getShortVideoUrl() {
		return shortVideoUrl;
	}


	public void setShortVideoUrl(String shortVideoUrl) {
		this.shortVideoUrl = shortVideoUrl;
	}


	public String getTimestamp() {
		return timestamp;
	}


	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}


	public boolean isMine() {
		return mine;
	}


	public void setMine(boolean mine) {
		this.mine = mine;
	}


	public String getAvatar() {
		return avatar;
	}


	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}


	public String getUserName() {
		return userName;
	}


	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	
	
	
	
	
}
