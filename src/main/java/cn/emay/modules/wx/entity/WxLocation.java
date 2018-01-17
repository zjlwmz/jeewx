package cn.emay.modules.wx.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import cn.emay.framework.core.common.entity.IdEntity;


/**
 * 微信位置提交实体
 * @author zjlWm
 * @date 2016-2-16
 */
@Entity
@Table(name = "wx_location")
public class WxLocation  extends IdEntity{

	private static final long serialVersionUID = 1L;
	
	
	/**
	 * 微信openid
	 */
	private String openid;
	
	/**
	 * 粉丝名称
	 */
	private String nickName;
	
	/**
	 * 纬度
	 */
	private Double latitude;
	
	
	/**
	 * 经度
	 */
	private Double longitude;
	
	/**
	 * 精度
	 */
	private Double precision;
	
	/**
	 * 创建时间
	 */
	private Date createDate;

	/**
	 * 是否已经展示过：0未展示；1已展示
	 */
	private String isShow;
	
	
	
	public WxLocation(){
		this.isShow="0";
	}
	
	
	@Column(name="openid")
	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	
	@Column(name="latitude")
	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	@Column(name="longitude")
	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	
	@Column(name="wx_precision")
	public Double getPrecision() {
		return precision;
	}

	public void setPrecision(Double precision) {
		this.precision = precision;
	}

	
	@Column(name="create_date")
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	@Column(name="is_show")
	public String getIsShow() {
		return isShow;
	}

	public void setIsShow(String isShow) {
		this.isShow = isShow;
	}


	@Transient
	public String getNickName() {
		return nickName;
	}

	@Transient
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	
	
	
	
	
}
