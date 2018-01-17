package cn.emay.modules.wx.dto.members;

/**
 * 群信息
 * @author lenovo
 *
 */
public class MembersOwnerDto {

	/**
	 * 群主昵称
	 */
	private String username;
	
	/**
	 * 群主ID
	 */
	private String id;
	
	/**
	 * 群主头像
	 */
	private String avatar;
	
	/**
	 * 群员签名
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
