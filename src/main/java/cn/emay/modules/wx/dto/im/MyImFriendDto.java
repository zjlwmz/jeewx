package cn.emay.modules.wx.dto.im;

import java.util.List;


/**
 * 好友列表
 * @author lenovo
 *
 */
public class MyImFriendDto {

	/**
	 * 好友分组名
	 */
	private String groupname;
	
	/**
	 * 分组ID
	 */
	private String id;
	
	/**
	 * 在线数量，可以不传
	 */
	private String online;
	
	
	private List<MyImFriendUserInfoDto>list;

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

	public String getOnline() {
		return online;
	}

	public void setOnline(String online) {
		this.online = online;
	}

	public List<MyImFriendUserInfoDto> getList() {
		return list;
	}

	public void setList(List<MyImFriendUserInfoDto> list) {
		this.list = list;
	}
	
	
	
	
	
}
