package cn.emay.modules.wx.web;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import cn.emay.framework.common.utils.SignatureUtils;
import cn.emay.modules.wx.entity.WxWechat;
import cn.emay.modules.wx.service.WechatReceiveService;
import cn.emay.modules.wx.service.WxWechatService;


/**
 * 微信公众号对接控制器
 * @author zjlWm
 * @date 2015-11-28
 */
@Controller
@RequestMapping("/wechatController")
public class WechatController {
	
	private static final Logger logger = LoggerFactory.getLogger(WechatController.class);
	
	
	/**
	 * 微信公众号后台service接口
	 */
	@Autowired
	private WxWechatService wxWechatService;
	
	/**
	 * 微信前端service接口
	 */
	@Autowired
	private WechatReceiveService wechatReceiveService;
	
	
	/**
	 * 公众账号验证 GET接口
	 * @param request
	 * @param response
	 * @param signature
	 * @param timestamp
	 * @param nonce
	 * @param echostr
	 */
	@RequestMapping(params="wechat", method = RequestMethod.GET)
	public void wechatGet(HttpServletRequest request,HttpServletResponse response,
			@RequestParam(value = "signature") String signature,
			@RequestParam(value = "timestamp") String timestamp,
			@RequestParam(value = "nonce") String nonce,
			@RequestParam(value = "echostr") String echostr) {

		WxWechat wxWechat = wxWechatService.findWxWechat();
		if (SignatureUtils.checkSignature(wxWechat.getToken(), signature,timestamp, nonce)) {
			try {
				response.getWriter().print(echostr);
			} catch (IOException e) {
				logger.debug("公众账号验证 GET接口",e);
			}
		}
	}

	@RequestMapping(params="wechat", method = RequestMethod.POST)
	public void wechatPost(HttpServletResponse response,HttpServletRequest request) throws IOException {
		String respMessage = wechatReceiveService.coreService(request);
		logger.info("提交到腾讯微信接口信息："+respMessage);
		if(respMessage!=null && !respMessage.equals("")){
			PrintWriter out = response.getWriter();
			out.print(respMessage);
			out.close();
		}
	}
}
