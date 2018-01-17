package cn.emay.modules.wx.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import cn.emay.framework.core.common.entity.IdEntity;

/**
 * 聊天记录
 * @author lenovo
 *
 */
@Entity
@Table(name = "wx_chat_logs")
public class WxChatLogs extends IdEntity{
	
	private static final long serialVersionUID = 1L;

	/**
	 * 聊天类型
	 * friend 朋友、group群组
	 */
	private String  type;
	
	/**
	 * 消息类型
	 */
	private String messageType;
	
	
	/**
	 * 聊天文本内容
	 */
	private String content;
	
	
	/**
	 * 接收人用户id
	 */
	private String toUserName;
	
	
	/**
	 * 发送消息人用户id
	 */
	private String fromUserName;
	
	
	/**
	 * 图片
	 */
	private String picUrl;
	
	/**
	 * 语音路径
	 */
	private String voiceUrl;
	
	
	/**
	 * 视频
	 */
	private String videoUrl;
	
	
	/**
	 * 短视频
	 */
	private String shortVideoUrl;
	
	
	/**
	 * 多媒体时间(视频、音频时间 )
	 */
	private String mediaTime="";
	
	
	
	/**
	 * 地理位置-地理位置维度
	 */
	private String locationX;
	
	/**
	 * 地理位置-地理位置经度
	 */
	private String locationY;
	
	/**
	 * 地理位置-地图缩放大小
	 */
	private String scale;
	
	/**
	 * 地理位置-地理位置信息
	 */
	private String label;

	
	/**
	 * 在线客服当次id
	 */
	private String wxOnlineServiceId;
	
	/**
	 * 是否我发送的消息，如果为true，则会显示在右方
	 */
	private boolean mine=false;
	
	/**
	 * 创建时间
	 */
	private Date createDate;
	
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	
	
	@Column(name = "message_type")
	public String getMessageType() {
		return messageType;
	}

	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}

	@Column(name = "content")
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	
	@Column(name = "to_user_name")
	public String getToUserName() {
		return toUserName;
	}

	public void setToUserName(String toUserName) {
		this.toUserName = toUserName;
	}

	@Column(name = "from_user_name")
	public String getFromUserName() {
		return fromUserName;
	}

	public void setFromUserName(String fromUserName) {
		this.fromUserName = fromUserName;
	}

	@Column(name = "pic_url")
	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}


	@Column(name = "voice_url")
	public String getVoiceUrl() {
		return voiceUrl;
	}

	public void setVoiceUrl(String voiceUrl) {
		this.voiceUrl = voiceUrl;
	}

	@Column(name = "video_url")
	public String getVideoUrl() {
		return videoUrl;
	}

	public void setVideoUrl(String videoUrl) {
		this.videoUrl = videoUrl;
	}

	@Column(name = "short_video_url")
	public String getShortVideoUrl() {
		return shortVideoUrl;
	}

	public void setShortVideoUrl(String shortVideoUrl) {
		this.shortVideoUrl = shortVideoUrl;
	}

	

	@Column(name = "location_x")
	public String getLocationX() {
		return locationX;
	}

	public void setLocationX(String locationX) {
		this.locationX = locationX;
	}

	@Column(name = "location_y")
	public String getLocationY() {
		return locationY;
	}

	public void setLocationY(String locationY) {
		this.locationY = locationY;
	}

	@Column(name = "scale")
	public String getScale() {
		return scale;
	}

	public void setScale(String scale) {
		this.scale = scale;
	}

	@Column(name = "label")
	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	
	@Column(name = "wx_online_service_id")
	public String getWxOnlineServiceId() {
		return wxOnlineServiceId;
	}

	public void setWxOnlineServiceId(String wxOnlineServiceId) {
		this.wxOnlineServiceId = wxOnlineServiceId;
	}
	
	@Column(name = "mine")
	public boolean isMine() {
		return mine;
	}

	public void setMine(boolean mine) {
		this.mine = mine;
	}

	
	@Column(name = "create_date")
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	
	@Column(name = "media_time")
	public String getMediaTime() {
		return mediaTime;
	}

	
	public void setMediaTime(String mediaTime) {
		this.mediaTime = mediaTime;
	}
	
	
	
	
	
}
