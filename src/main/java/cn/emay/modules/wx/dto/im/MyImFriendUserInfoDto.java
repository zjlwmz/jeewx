package cn.emay.modules.wx.dto.im;

/**
 * 分组下的好友信息
 * @author lenovo
 *
 */
public class MyImFriendUserInfoDto {

	/**
	 * 好友昵称
	 */
	private String username;
	
	/**
	 * 好友ID
	 */
	private String id;
	
	/**
	 * 好友头像
	 */
	private String avatar;
	
	/**
	 * 好友签名
	 */
	private String sign;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}
	
	
	
}
