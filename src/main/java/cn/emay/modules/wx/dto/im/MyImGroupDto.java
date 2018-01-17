package cn.emay.modules.wx.dto.im;

/**
 * 群组信息
 * @author lenovo
 *
 */
public class MyImGroupDto {

	
	/**
	 * 群组名
	 */
	private String groupname;
	
	/**
	 * 群组ID
	 */
	private String id;
	
	/**
	 * 群组头像
	 */
	private String avatar;

	public String getGroupname() {
		return groupname;
	}

	public void setGroupname(String groupname) {
		this.groupname = groupname;
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
	
	
}
