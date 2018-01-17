package cn.emay.modules.wx.dto.im;

/**
 * 我的信息（如果layim.config已经配置了mine，则该返回的信息无效）
 */
public class MyImMineDto {

	/**
	 * 我的昵称
	 */
	private String username;
	
	/**
	 * 我的ID
	 */
	private String id;
	
	/**
	 * 在线状态 online：在线、hide：隐身
	 */
	private String status;
	
	/**
	 * 我的签名
	 */
	private String sign;
	
	/**
	 * 我的头像
	 */
	private String avatar;

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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	
	
	
	
}
