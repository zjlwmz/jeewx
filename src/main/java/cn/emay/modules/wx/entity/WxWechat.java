package cn.emay.modules.wx.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * 微信账号实体
 */
@Entity
@Table(name = "wx_wechat")
public class WxWechat implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private String id;
	/**
	 * 公众号名称
	 */
	private String name;
	
	private String token;
	private String encodingaeskey;
	private String accessToken;
	
	/**
	 * AppId
	 */
	private String appid;
	
	/**
	 * secret
	 */
	private String secret;
	/**
	 * 1:普通订阅号 ;2:普通服务号；3 1:普通订阅号 ;2:普通服务号；3：认证订阅号；4：认证服务号'
	 */
	private Short level;
	
	/**
	 * 原始ID
	 */
	private String original;
	
	/**
	 * 签名 签名(文字签名)
	 */
	private String signature;
	
	/**
	 * 微信公众号登录用户名
	 */
	private String username;
	
	/**
	 * 微信公众号登录密码
	 */
	private String password;
	
	private String jsapiTicket;
	private String subscribeurl;
	private String cardTicket;
	private String topad;
	private String footad;
	private String authRefreshToken;
	private Integer updatedOn;
	private Integer updatedBy;


	// Property accessors
	@Id
	@Column(name = "wechat_id", unique = true, nullable = false)
	@GeneratedValue(generator = "paymentableGenerator")
	@GenericGenerator(name = "paymentableGenerator", strategy = "uuid")
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column(name = "name", nullable = false, length = 30)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "token", nullable = false, length = 32)
	public String getToken() {
		return this.token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	@Column(name = "encodingaeskey")
	public String getEncodingaeskey() {
		return this.encodingaeskey;
	}

	public void setEncodingaeskey(String encodingaeskey) {
		this.encodingaeskey = encodingaeskey;
	}

	@Column(name = "access_token", length = 1000)
	public String getAccessToken() {
		return this.accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	@Column(name = "appid", nullable = false, length = 50)
	public String getAppid() {
		return this.appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}

	@Column(name = "secret", nullable = false, length = 50)
	public String getSecret() {
		return this.secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	@Column(name = "level", nullable = false)
	public Short getLevel() {
		return this.level;
	}

	public void setLevel(Short level) {
		this.level = level;
	}

	@Column(name = "original", length = 50)
	public String getOriginal() {
		return this.original;
	}

	public void setOriginal(String original) {
		this.original = original;
	}

	@Column(name = "signature", nullable = false, length = 100)
	public String getSignature() {
		return this.signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	@Column(name = "username", length = 30)
	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Column(name = "password", length = 32)
	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Column(name = "jsapi_ticket", length = 1000)
	public String getJsapiTicket() {
		return this.jsapiTicket;
	}

	public void setJsapiTicket(String jsapiTicket) {
		this.jsapiTicket = jsapiTicket;
	}

	@Column(name = "subscribeurl", length = 120)
	public String getSubscribeurl() {
		return this.subscribeurl;
	}

	public void setSubscribeurl(String subscribeurl) {
		this.subscribeurl = subscribeurl;
	}

	@Column(name = "card_ticket", length = 1000)
	public String getCardTicket() {
		return this.cardTicket;
	}

	public void setCardTicket(String cardTicket) {
		this.cardTicket = cardTicket;
	}

	@Column(name = "topad", length = 225)
	public String getTopad() {
		return this.topad;
	}

	public void setTopad(String topad) {
		this.topad = topad;
	}

	@Column(name = "footad", length = 225)
	public String getFootad() {
		return this.footad;
	}

	public void setFootad(String footad) {
		this.footad = footad;
	}

	@Column(name = "auth_refresh_token")
	public String getAuthRefreshToken() {
		return this.authRefreshToken;
	}

	public void setAuthRefreshToken(String authRefreshToken) {
		this.authRefreshToken = authRefreshToken;
	}

	@Column(name = "updated_on")
	public Integer getUpdatedOn() {
		return this.updatedOn;
	}

	public void setUpdatedOn(Integer updatedOn) {
		this.updatedOn = updatedOn;
	}

	@Column(name = "updated_by")
	public Integer getUpdatedBy() {
		return this.updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

}