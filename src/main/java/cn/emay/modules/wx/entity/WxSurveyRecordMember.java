package cn.emay.modules.wx.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import cn.emay.framework.common.poi.excel.annotation.Excel;


/**
 * 调查问卷会员参与者
 * @author lenovo
 *
 */
@Entity
@Table(name="wx_survey_record_member")
public class WxSurveyRecordMember {

	@Id
	@Column(name="id")
	private String id;
	
	
	@Column(name="mainId")
	private String mainId;
	
	@Excel(exportName="会员id")
	@Column(name="memberid")
	private String memberid;

	
	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getMainId() {
		return mainId;
	}


	public void setMainId(String mainId) {
		this.mainId = mainId;
	}


	public String getMemberid() {
		return memberid;
	}


	public void setMemberid(String memberid) {
		this.memberid = memberid;
	}
	
	
	
}
