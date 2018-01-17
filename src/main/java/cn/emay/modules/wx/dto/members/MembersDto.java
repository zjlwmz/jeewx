package cn.emay.modules.wx.dto.members;

import cn.emay.modules.wx.dto.im.ImBaseDto;

/**
 * 群组列表dto
 * @author lenovo
 *
 */
public class MembersDto extends ImBaseDto{
	
	private MembersDataDto data;

	public MembersDataDto getData() {
		return data;
	}

	public void setData(MembersDataDto data) {
		this.data = data;
	}
}
