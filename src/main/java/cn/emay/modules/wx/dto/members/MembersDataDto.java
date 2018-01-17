package cn.emay.modules.wx.dto.members;

import java.util.List;

public class MembersDataDto {

	/**
	 * 群信息
	 */
	private MembersOwnerDto membersOwnerDto;
	
	/**
	 * 全成员信息
	 */
	private List<MembersMemberDto>list;

	public MembersOwnerDto getMembersOwnerDto() {
		return membersOwnerDto;
	}

	public void setMembersOwnerDto(MembersOwnerDto membersOwnerDto) {
		this.membersOwnerDto = membersOwnerDto;
	}

	public List<MembersMemberDto> getList() {
		return list;
	}

	public void setList(List<MembersMemberDto> list) {
		this.list = list;
	}
	
	
	
	
}
