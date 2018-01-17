package cn.emay.modules.wx.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import cn.emay.modules.chat.service.ChatMyGroupService;
import cn.emay.modules.sys.entity.User;
import cn.emay.modules.sys.utils.UserCacheUtils;


/**
 * 负责 客服人员与访客对话数据的传输
 * webIm控制器
 * @author lenovo
 *
 */
@Controller
@RequestMapping("/webImController")
public class WebImController {

	/**
	 * 我的分组service接口
	 */
	@Autowired
	private ChatMyGroupService chatMyGroupService;
	
	
	/**
	 * pc
	 * 我的客服
	 * @return
	 */
	@RequestMapping(params="myIm")
	public ModelAndView myIm(HttpServletRequest request, HttpServletResponse response){
		User user=UserCacheUtils.getCurrentUser();
		if(null!=user){
			request.setAttribute("userid", user.getId());
			/**
			 * 创建默认分组
			 */
			chatMyGroupService.createDefaultChatMyGroupByUserId(user.getId());
		}
		return new ModelAndView("modules/weixin/wxonline/myIm");
	}
	
	
	
	/**
	 * 微信端
	 * 我的客服
	 * @return
	 */
	@RequestMapping(params="mobile")
	public ModelAndView mobile(HttpServletRequest request, HttpServletResponse response){
		User user=UserCacheUtils.getCurrentUser();
		if(null!=user){
			request.setAttribute("userid", user.getId());
			chatMyGroupService.createDefaultChatMyGroupByUserId(user.getId());
		}
		return new ModelAndView("modules/weixin/wxonline/mobile");
	}
}
