package cn.emay.modules.wx.im.response;

public class SendBase {
	/**
	 * 消息来源用户名
	 */
	private String username;
	
	/**
	 * 消息来源用户头像
	 */
	private String avatar;
	
	/**
	 * 聊天窗口来源ID（如果是私聊，则是用户id，如果是群聊，则是群组id）
	 */
	private String id;
	
	
	/**
	 * 服务端动态时间戳
	 */
	private String timestamp;


	public String getUsername() {
		return username;
	}


	public void setUsername(String username) {
		this.username = username;
	}


	public String getAvatar() {
		return avatar;
	}


	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getTimestamp() {
		return timestamp;
	}


	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	
	
	
}
