package cn.emay.modules.wx.response.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import weixin.popular.bean.message.EventMessage;
import cn.emay.modules.wx.entity.WxLocation;
import cn.emay.modules.wx.response.ResponseService;
import cn.emay.modules.wx.service.WxLocationService;

/**
 * 微信位置信息上报处理接口
 * @author lenovo
 *
 */
@Component
public class DoWxLocationService extends BaseResponseService implements ResponseService{

	/**
	 * 微信位置服务上报
	 */
	@Autowired
	private WxLocationService wxLocationService;
	
	@Override
	public void execute(EventMessage eventMessage) {
		String Latitude=eventMessage.getLatitude();
		String Longitude=eventMessage.getLongitude();
	    String Precision=eventMessage.getPrecision();
		String fromUserName=eventMessage.getFromUserName();
		WxLocation wxLocation=new WxLocation();
		wxLocation.setLatitude(Double.parseDouble(Latitude));
		wxLocation.setLongitude(Double.parseDouble(Longitude));
		wxLocation.setPrecision(Double.parseDouble(Precision));
		wxLocation.setCreateDate(new Date());
		wxLocation.setOpenid(fromUserName);
		wxLocationService.save(wxLocation);		
	}

}
