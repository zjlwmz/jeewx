package cn.emay.modules.wx.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import cn.emay.framework.common.utils.StringUtils;
import cn.emay.framework.core.common.entity.IdEntity;

/**
 * 文本消息存储实体类
 * 
 * @author zjlWm
 * @date 2015-1122
 */
@Entity
@Table(name = "wx_receive_message")
public class ReceiveMessage extends IdEntity {

	private static final long serialVersionUID = 1L;

	// 开发者微信号
	private String toUserName;

	// 发送方帐号（一个OpenID）
	private String fromUserName;

	// 消息创建时间 （整型）
	private Timestamp createTime;

	// 消息类型（text/image/location/link/voice/music/video）
	private String msgType;

	// 消息id，64位整型
	private String msgId;

	// 消息内容
	private String content;

	/**
	 * 图片url
	 */
	private String imageUrl;

	/**
	 * 音频url
	 */
	private String voiceUrl;

	/**
	 * 小视频url
	 */
	private String shortvideoUrl;

	/**
	 * 视频url
	 */
	private String videoUrl;

	// 是否回复
	private String response;

	// 回复内容
	private String rescontent;

	// 用户昵称
	private String nickName;

	// 微信账号Id
	private String wechatId;

	@Column(name = "to_username")
	public String getToUserName() {
		return toUserName;
	}

	public void setToUserName(String toUserName) {
		this.toUserName = toUserName;
	}

	@Column(name = "from_username")
	public String getFromUserName() {
		return fromUserName;
	}

	public void setFromUserName(String fromUserName) {
		this.fromUserName = fromUserName;
	}

	@Column(name = "msg_type")
	public String getMsgType() {
		return msgType;
	}

	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}

	@Column(name = "msg_id")
	public String getMsgId() {
		return msgId;
	}

	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}

	@Column(name = "content")
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Column(name = "response")
	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

	@Column(name = "rescontent")
	public String getRescontent() {
		return rescontent;
	}

	public void setRescontent(String rescontent) {
		this.rescontent = rescontent;
	}

	@Column(name = "createtime")
	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	@Column(name = "nick_name")
	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	@Column(name = "wechat_id")
	public String getWechatId() {
		return wechatId;
	}

	public void setWechatId(String wechatId) {
		this.wechatId = wechatId;
	}

	@Column(name = "image_url")
	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	@Column(name = "voice_url")
	public String getVoiceUrl() {
		return voiceUrl;
	}

	public void setVoiceUrl(String voiceUrl) {
		this.voiceUrl = voiceUrl;
	}

	@Column(name = "shortvideo_url")
	public String getShortvideoUrl() {
		return shortvideoUrl;
	}

	public void setShortvideoUrl(String shortvideoUrl) {
		this.shortvideoUrl = shortvideoUrl;
	}

	@Column(name = "video_url")
	public String getVideoUrl() {
		return videoUrl;
	}

	public void setVideoUrl(String videoUrl) {
		this.videoUrl = videoUrl;
	}

	
	@Transient
	public String getContentDetail(){
		if(null!=this.msgType){
			if(this.msgType.equals("text")){
				return content;
			}else if(this.msgType.equals("image")){
				if(StringUtils.isNotBlank(imageUrl)){
					this.content="<img width=\"100\" border=\"0\" src='"+imageUrl+"'>";
				}
			}else if(this.msgType.equals("voice")){
				StringBuffer buff=new StringBuffer();
				if(StringUtils.isNotBlank(voiceUrl)){
					buff.append("<audio src=\""+voiceUrl+"\" controls=\"controls\">");
					buff.append("Your browser does not support the audio element.");
					buff.append("</audio>");
					this.content=buff.toString();
				}
			}else if(this.msgType.equals("shortvideo")){
				StringBuffer buff=new StringBuffer();
				if(StringUtils.isNotBlank(shortvideoUrl)){
					buff.append("<video src=\""+shortvideoUrl+"\" controls=\"controls\">");
					buff.append("Your browser does not support the audio element.");
					buff.append("</video>");
					this.content=buff.toString();
				}
			}
			else if(this.msgType.equals("video")){
				StringBuffer buff=new StringBuffer();
				if(StringUtils.isNotBlank(videoUrl)){
					buff.append("<video src=\""+videoUrl+"\" controls=\"controls\">");
					buff.append("Your browser does not support the audio element.");
					buff.append("</video>");
					this.content=buff.toString();
				}
			}
		}
		return this.content;
	}
}
