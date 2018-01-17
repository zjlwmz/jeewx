package cn.emay.modules.wx.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import cn.emay.framework.common.utils.DateUtils;
import cn.emay.framework.core.common.entity.IdEntity;

/**
 * 微信粉丝会话
 * @author zjlwm
 *
 */
@Entity
@Table(name = "wx_fans_session")
public class WxFansSession  extends IdEntity{

	private static final long serialVersionUID = 1L;
	
	
	/**
	 * 会话开始时间
	 */
	private Timestamp  startSessionTime;
	
	
	/**
	 * 结束会话时间
	 */
	private Timestamp  endSessionTime;
	
	
	/**
	 * 会话剩余时间(秒)
	 */
	private Long surplusDate;
	
	/**
	 * 微信openid
	 */
	private String openid;
	
	/**
	 * 0:未在会话中、1:在会话中
	 */
	private short satus;


	@Column(name = "start_session_time")
	public Timestamp getStartSessionTime() {
		return startSessionTime;
	}

	public void setStartSessionTime(Timestamp startSessionTime) {
		this.startSessionTime = startSessionTime;
	}

	@Column(name = "end_session_time")
	public Timestamp getEndSessionTime() {
		return endSessionTime;
	}

	public void setEndSessionTime(Timestamp endSessionTime) {
		this.endSessionTime = endSessionTime;
	}

	@Column(name = "surplus_date")
	public Long getSurplusDate() {
		return surplusDate;
	}

	public void setSurplusDate(Long surplusDate) {
		this.surplusDate = surplusDate;
	}

	@Column(name = "openid")
	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	@Column(name = "satus")
	public short getSatus() {
		return satus;
	}

	public void setSatus(short satus) {
		this.satus = satus;
	}

	@Transient
	public String getResidualTime() {
		if(null!=surplusDate){
			//48小时
			if(surplusDate>2*24*60*60){
				return "0";
			}else{
				return DateUtils.dateDiff(surplusDate*1000);
			}
			
		}
		return "";
	}

	
}
