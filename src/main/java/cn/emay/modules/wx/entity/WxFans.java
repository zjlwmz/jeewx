package cn.emay.modules.wx.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import cn.emay.framework.core.common.entity.IdEntity;

/**
 * 微信粉丝
 * @author lenovo
 *
 */
@Entity
@Table(name = "wx_fans")
public class WxFans extends IdEntity {

	private static final long serialVersionUID = 1L;
	
	
	/**
	 * 渠道Id(同时作为场景Id:scene_id)
	 */
	private Integer channelId;
	/**
	 * 微信openid
	 */
	private String openid;
	/**
	 * 是否关注
	 */
	private Boolean isFollow;
	/**
	 * 最后关注时间
	 */
	private Timestamp followtime;
	/**
	 * 最后取消关注时间
	 */
	private Timestamp unfollowtime;
	
	/**
	 * 昵称
	 */
	private String nickname;
	/**
	 * 更新时间
	 */
	private Timestamp updatedOn;
	/**
	 * 头像
	 */
	private String headimgurl;
	
	/**
	 * 多个公众号之间用户帐号互通UnionID机制
	 */
	private String unionid;

	
	/**
	 * 用户的性别，值为1时是男性，值为2时是女性，值为0时是未知
	 * 性别
	 */
	private Integer sex;
	
	
	/**
	 * 用户所在城市
	 */
	private String city;
	
	/**
	 * 用户所在国家
	 */
	private String country;
	
	
	/**
	 * 用户所在省份
	 */
	private String province;
	

	/**
	 * 用户的语言，简体中文为zh_CN
	 */
	private String language;
	
	
	/**
	 * 公众号运营者对粉丝的备注，公众号运营者可在微信公众平台用户管理界面对粉丝添加备注
	 */
	private String remark;
	
	/**
	 * 用户所在的分组ID
	 */
	private Integer groupid;
	
	
	/**
	 * 会员ID
	 */
	private String memberId;
	
	
	public WxFans(){
		super();
	}
	
	public WxFans(String id){
		super.setId(id);
	}
	
	
	
	
	@Column(name = "channel_id")
	public Integer getChannelId() {
		return this.channelId;
	}

	public void setChannelId(Integer channelId) {
		this.channelId = channelId;
	}

	@Column(name = "openid", nullable = false, length = 50)
	public String getOpenid() {
		return this.openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	@Column(name = "is_follow", nullable = false)
	public Boolean getIsFollow() {
		return this.isFollow;
	}

	public void setIsFollow(Boolean isFollow) {
		this.isFollow = isFollow;
	}

	@Column(name = "followtime", length = 19)
	public Timestamp getFollowtime() {
		return this.followtime;
	}

	public void setFollowtime(Timestamp followtime) {
		this.followtime = followtime;
	}

	@Column(name = "unfollowtime", length = 19)
	public Timestamp getUnfollowtime() {
		return this.unfollowtime;
	}

	public void setUnfollowtime(Timestamp unfollowtime) {
		this.unfollowtime = unfollowtime;
	}

	@Column(name = "nickname", length = 50)
	public String getNickname() {
		return this.nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	@Column(name = "updated_on", length = 19)
	public Timestamp getUpdatedOn() {
		return this.updatedOn;
	}

	public void setUpdatedOn(Timestamp updatedOn) {
		this.updatedOn = updatedOn;
	}

	@Column(name = "headimgurl")
	public String getHeadimgurl() {
		return this.headimgurl;
	}

	public void setHeadimgurl(String headimgurl) {
		this.headimgurl = headimgurl;
	}

	@Column(name = "unionid")
	public String getUnionid() {
		return unionid;
	}

	public void setUnionid(String unionid) {
		this.unionid = unionid;
	}

	@Column(name = "sex")
	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	@Column(name = "city")
	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	@Column(name = "country")
	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	@Column(name = "province")
	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	@Column(name = "language")
	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	@Column(name = "remark")
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Column(name = "groupid")
	public Integer getGroupid() {
		return groupid;
	}

	public void setGroupid(Integer groupid) {
		this.groupid = groupid;
	}

	@Column(name = "member_id")
	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	
	
	
	
	
}