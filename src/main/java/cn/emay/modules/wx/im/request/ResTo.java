package cn.emay.modules.wx.im.request;


/**
 * 
 * @Title 包含对方的信息
 * @author zjlwm
 * @date 2017-2-13 上午9:23:50
 *
 */
public class ResTo {

	/**
	 * 对方的头像
	 */
	private String avatar;
	
	/**
	 * 对方的id
	 */
	private String id;
	
	/**
	 * 对方的名称
	 */
	private String name;
	
	/**
	 * 对方的签名
	 */
	private String sign;// "这些都是测试数据，实际使用请严格按照该格式返回"
	
	/**
	 * 聊天类型，一般分friend和group两种，group即群聊
	 */
	private String type;
	

	/**
	 * 对方的昵称
	 */
	private String  username;


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


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getSign() {
		return sign;
	}


	public void setSign(String sign) {
		this.sign = sign;
	}


	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}


	public String getUsername() {
		return username;
	}


	public void setUsername(String username) {
		this.username = username;
	}
			  
	
	
	
}
