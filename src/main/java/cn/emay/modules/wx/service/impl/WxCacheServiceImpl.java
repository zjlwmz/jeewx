package cn.emay.modules.wx.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import weixin.popular.api.TicketAPI;
import weixin.popular.api.TokenAPI;
import weixin.popular.bean.ticket.Ticket;
import weixin.popular.bean.token.Token;
import cn.emay.framework.common.mapper.JsonMapper;
import cn.emay.framework.common.utils.CacheUtils;
import cn.emay.modules.wx.entity.WxWechat;
import cn.emay.modules.wx.service.WxCacheService;
import cn.emay.modules.wx.service.WxWechatService;
/**
 * 微信token缓存实现
 * @author zjlWm
 * @date 2015-11-30
 */
@Service
public class WxCacheServiceImpl implements WxCacheService {

	/**
	 * 微信公众号接口
	 */
	@Autowired
	private  WxWechatService wxWechatService;
	
	@Override
	public String getToken(String appid, String secret) {
		String tokenStr="";
		Date currentDate=new Date();
		if(null!=CacheUtils.get(appid+"_token")){
			String tokenTimeStr=CacheUtils.get(appid+"_token").toString();//token_time
			String [] tokenTime=tokenTimeStr.split("\\u0024\\u0024#\\u0024\\u0024");//"$$#$$"
			tokenStr =tokenTime[0];
			Long time=Long.parseLong(tokenTime[1]);
			if(currentDate.getTime()-time<(1000*3600*1.8)){//1.8小时
				return tokenStr;
			}else{
				Token token=TokenAPI.token(appid, secret);
				tokenStr=token.getAccess_token();
				CacheUtils.put(appid+"_token", tokenStr+"$$#$$"+currentDate.getTime());
			}
		}else{
			Token token=TokenAPI.token(appid, secret);
			tokenStr=token.getAccess_token();
			CacheUtils.put(appid+"_token", tokenStr+"$$#$$"+currentDate.getTime());
		}
		return tokenStr;
	}

	@Override
	public String getTicket(String AccessToken) {
		Date currentDate=new Date();
		String tokenStr="";
		if(null!=CacheUtils.get("ticket_token")){
			String tokenTimeStr=CacheUtils.get("ticket_token").toString();//token_time
			String [] tokenTime=tokenTimeStr.split("\\u0024\\u0024#\\u0024\\u0024");//"$$#$$"
			tokenStr =tokenTime[0];
			Long time=Long.parseLong(tokenTime[1]);
			if(currentDate.getTime()-time<(1000*3600*1.8)){//1.8小时
				return tokenStr;
			}else{
				Ticket tickent=TicketAPI.ticketGetticket(AccessToken);
				if("0".equals(tickent.getErrcode())){
					tokenStr=tickent.getTicket();
					CacheUtils.put("ticket_token", tokenStr+"$$#$$"+currentDate.getTime());
					return tickent.getTicket();
				}
				return null;
			}
		}else{
			Ticket tickent=TicketAPI.ticketGetticket(AccessToken);
			if("0".equals(tickent.getErrcode())){
				tokenStr=tickent.getTicket();
				CacheUtils.put("ticket_token", tokenStr+"$$#$$"+currentDate.getTime());
				return tickent.getTicket();
			}
			return null;
		}
	}

	/**
	 * 
	 */
	@Override
	public WxWechat getWxWechat(String original) {
		WxWechat wxWechat=null;
		if(null!=CacheUtils.get(original)){
			String jsonWxChat=CacheUtils.get(original).toString();
			wxWechat=JsonMapper.nonDefaultMapper().fromJson(jsonWxChat, WxWechat.class);
		}else{
			wxWechat=wxWechatService.findByOriginal(original);
			CacheUtils.put(original, JsonMapper.nonDefaultMapper().toJson(wxWechat));
		}
		return wxWechat;
	}
	
	public WxWechat getWxWechat() {
		WxWechat wxWechat=null;
		if(null!=CacheUtils.get("wxwechat")){
			String jsonWxChat=CacheUtils.get("wxwechat").toString();
			wxWechat=JsonMapper.nonDefaultMapper().fromJson(jsonWxChat, WxWechat.class);
		}else{
			wxWechat=wxWechatService.findWxWechat();
			if(null!=wxWechat){
				CacheUtils.put("wxwechat", JsonMapper.nonDefaultMapper().toJson(wxWechat));
			}
		}
		return wxWechat;
	}

	@Override
	public String getToken() {
		WxWechat wxWechat=wxWechatService.findWxWechat();
		if(null!=wxWechat){
			return getToken(wxWechat.getAppid(), wxWechat.getSecret());
		}
		return null;
	}

}
