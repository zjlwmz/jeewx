package cn.emay.modules.wx.dto.im;

import java.util.List;

public class MyImDataDto {

	/**
	 * 我的信息
	 */
	private MyImMineDto mine;
	
	
	/**
	 * 好友列表
	 */
	private List<MyImFriendDto> friend;
	
	
	/**
	 * 群组列表
	 */
	private List<MyImGroupDto>group;


	public MyImMineDto getMine() {
		return mine;
	}


	public void setMine(MyImMineDto mine) {
		this.mine = mine;
	}


	public List<MyImFriendDto> getFriend() {
		return friend;
	}


	public void setFriend(List<MyImFriendDto> friend) {
		this.friend = friend;
	}


	public List<MyImGroupDto> getGroup() {
		return group;
	}


	public void setGroup(List<MyImGroupDto> group) {
		this.group = group;
	}
	
	
	
}
