package cn.emay.modules.wx.im.request;


/**
 * @Title 包含我发送的消息及我的信息
 * @author zjlwm
 * @date 2017-2-13 上午9:23:41
 *
 */
public class ResMine {
	/**
	 * 我的头像
	 */
	private String avatar;
	
	/**
	 * 消息类型
	 */
	private String msgType;
	
	/**
	 * 消息内容
	 */
	private String content;
	
	/**
	 * 我的id
	 */
	private String id;
	
	/**
	 * 是否我发送的消息
	 */
	private String mine;
	
	/**
	 * 我的昵称
	 */
	private String username;

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMine() {
		return mine;
	}

	public void setMine(String mine) {
		this.mine = mine;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getMsgType() {
		return msgType;
	}

	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}

	
			  
}
