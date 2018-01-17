package cn.emay.modules.wx.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import cn.emay.framework.core.common.hibernate.qbc.CriteriaQuery;
import cn.emay.framework.core.common.model.json.DataGrid;
import cn.emay.framework.core.extend.hqlsearch.HqlGenerateUtil;
import cn.emay.framework.tag.core.easyui.TagUtil;
import cn.emay.modules.wx.entity.WxFans;
import cn.emay.modules.wx.entity.WxLocation;
import cn.emay.modules.wx.service.WxFansService;
import cn.emay.modules.wx.service.WxLocationService;

/**
 * @Title 微信位置控制器
 * @author zjlwm
 * @date 2017-2-2 下午10:51:39
 */
@Controller
@RequestMapping("/wxLocationController")
public class WxLocationController {

	/**
	 * 微信位置上报service接口
	 */
	@Autowired
	private WxLocationService wxLocationService;
	
	/**
	 * 微信粉丝service接口
	 */
	@Autowired
	private WxFansService wxFansService;
	
	@RequestMapping(params = "list")
	public ModelAndView list() {
		return new ModelAndView("modules/weixin/wxlocation/wxlocationList");
	}
	
	
	@SuppressWarnings("unchecked")
	@RequestMapping(params = "datagrid")
	@ResponseBody
	public void datagrid(WxLocation wxLocation, HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {

		CriteriaQuery cq = new CriteriaQuery(WxLocation.class, dataGrid);
		
		//默认排序
		if(dataGrid.getSort()==null){
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("createDate", "desc");
			cq.setOrder(map);
		}
		// cq.add();
		HqlGenerateUtil.installHql(cq, wxLocation);
		wxLocationService.getDataGridReturn(cq, true);
		List<WxLocation>wxLocationList=dataGrid.getResults();
		for(WxLocation location:wxLocationList){
			String openid=location.getOpenid();
			WxFans wxFans=wxFansService.findWxFansByOpenid(openid);
			if(null!=wxFans){
				String nickName=wxFans.getNickname();
				location.setNickName(nickName);
			}
		}
		TagUtil.datagrid(response, dataGrid);
	}
	
	
	
}
