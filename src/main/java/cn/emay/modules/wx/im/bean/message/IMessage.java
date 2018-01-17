package cn.emay.modules.wx.im.bean.message;

public abstract class IMessage {

	/**
	 * 消息来源用户名
	 */
	private String username;
	
	/**
	 * 群名称
	 */
	private String name;
	
	
	/**
	 * 消息来源用户头像
	 */
	private String avatar;
	
	/**
	 * 服务端动态时间戳
	 */
	private long timestamp;

	
	
	/**
	 * 聊天窗口来源ID（如果是私聊，则是用户id，如果是群聊，则是群组id）
	 */
	private String id;

	/**
	 * 消息类型
	 */
	private String msgType;
	
	
	/**
	 * 消息类别
	 */
	private String type;
	
	/**
	 * 添加类型
	 * friend   好友添加
	 * group    群添加
	 */
	private String addType;
	

	
	/**
	 * 是否我发送的消息，如果为true，则会显示在右方
	 */
	private boolean mine;
	
	/**
	 * 是否为系统消息
	 */
	private boolean system=false;
	
	
	/**
	 * 好友所在分组
	 */
	private String groupid;
	
	
	public IMessage() {
		super();
	}


	protected IMessage(String username, String avatar, long timestamp, String id, String msgType,boolean mine,boolean system) {
		super();
		this.username = username;
		this.avatar = avatar;
		this.timestamp = timestamp;
		this.id = id;
		this.msgType = msgType;
		this.mine=mine;
		this.system=system;
	}





	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	
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

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	

	public String getMsgType() {
		return msgType;
	}


	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}


	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}

	public boolean getMine() {
		return mine;
	}

	public void setMine(boolean mine) {
		this.mine = mine;
	}
	
	
	public boolean isSystem() {
		return system;
	}

	public void setSystem(boolean system) {
		this.system = system;
	}


	public String getAddType() {
		return addType;
	}


	public void setAddType(String addType) {
		this.addType = addType;
	}


	public String getGroupid() {
		return groupid;
	}


	public void setGroupid(String groupid) {
		this.groupid = groupid;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}
	
	
	
}
