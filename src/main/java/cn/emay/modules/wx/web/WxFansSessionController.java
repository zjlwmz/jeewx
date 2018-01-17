package cn.emay.modules.wx.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.emay.framework.core.common.exception.BusinessException;
import cn.emay.framework.core.common.hibernate.qbc.CriteriaQuery;
import cn.emay.framework.core.common.model.json.DataGrid;
import cn.emay.framework.core.extend.hqlsearch.HqlGenerateUtil;
import cn.emay.framework.tag.core.easyui.TagUtil;
import cn.emay.modules.wx.entity.WxFansSession;
import cn.emay.modules.wx.service.WxFansSessionService;
import cn.emay.modules.wx.support.WxFansSessionStatus;


/**
 * 微信会话
 */
@Controller
@RequestMapping("/wxFansSessionController")
public class WxFansSessionController {
	
	private static final Logger logger = Logger.getLogger(WxFansSessionController.class);
	
	/**
	 * 微信会话service接口
	 */
	@Autowired
	private WxFansSessionService wxFansSessionService;
	
	/**
	 * 微信会话easyuiAJAX用户列表请求数据 
	 * @param request
	 * @param response
	 * @param dataGrid
	 */
	@RequestMapping(params = "datagrid")
	public void datagrid(WxFansSession wxFansSession,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid){
		CriteriaQuery cq = new CriteriaQuery(WxFansSession.class, dataGrid);
		//查询条件组装器
		HqlGenerateUtil.installHql(cq, wxFansSession, request.getParameterMap());
		try{
			cq.eq("satus", WxFansSessionStatus.insession);
		//自定义追加查询条件
		}catch (Exception e) {
			logger.error("微信粉丝easyuiAJAX", e);
			throw new BusinessException(e.getMessage());
		}
		cq.add();
		wxFansSessionService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
		  
	}
	
	
	/**
	 * 微信粉丝列表
	 * @param request
	 * @return
	 */
	@RequestMapping(params="list")
	public String list(HttpServletRequest request) {
		return "modules/weixin/wxFansSessionList";
	}
	
}
