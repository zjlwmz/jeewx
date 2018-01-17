package cn.emay.modules.wx.response.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;

import weixin.popular.api.MessageAPI;
import weixin.popular.bean.message.EventMessage;
import weixin.popular.bean.message.message.Message;
import weixin.popular.bean.message.message.TextMessage;
import cn.emay.framework.common.utils.ResourceUtil;
import cn.emay.framework.common.utils.StringUtils;
import cn.emay.modules.wx.entity.FunctionLog;
import cn.emay.modules.wx.response.ResponseService;
import cn.emay.modules.wx.service.FunctionLogService;
import cn.emay.modules.wx.service.WxCacheService;
import cn.emay.modules.wx.utils.MessageUtil;
import cn.emay.modules.wx.utils.message.resp.TextMessageResp;


/**
 * 针对地理位置消息处理接口
 * @author lenovo
 *
 */
@Component
public class DoLocationResponseService extends BaseResponseService implements ResponseService{

	private static final Logger logger = LoggerFactory.getLogger(DoLocationResponseService.class);
	
	
	/**
	 * 微信缓存数据接口
	 */
	@Autowired
	private WxCacheService wxCacheService;

	
	
	/**
	 * 功能日志Dao接口
	 */
	@Autowired
	private FunctionLogService functionLogService;
	
	
	
	
	@Override
	public void execute(EventMessage eventMessage) {
		String fromUserName = eventMessage.getFromUserName();
		// 公众帐号
		String toUserName =eventMessage.getToUserName();
		// 默认回复此文本消息
		TextMessageResp textMessage = new TextMessageResp();
		textMessage.setToUserName(fromUserName);
		textMessage.setFromUserName(toUserName);
		textMessage.setCreateTime(new Date().getTime());
		textMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_TEXT);
		textMessage.setContent(getMainMenu());
		eventMessage.getLocation_X();//
		Double Location_X = Double.parseDouble(eventMessage.getLocation_X());
		Double Location_Y = Double.parseDouble(eventMessage.getLocation_Y());
		String Scale = eventMessage.getScale();
		doMyMapEvent2(toUserName, fromUserName, Location_X, Location_Y, Scale);
	}
	
	
	@SuppressWarnings("unchecked")
	public void doMyMapEvent2(String toUserName, String fromUserName, Double x, Double y, String sacle) {
		try {
			String locationCity = testGetRequest(y, x);
			if (StringUtils.isNotBlank(locationCity)) {
				Map<String, Object> result = getStroebyMap(x, y, locationCity);
				Double dis = Double.parseDouble(result.get("dis").toString());
				double d = dis / 1000;
				/**
				 * 查询到门店数据
				 */
				if (null != result.get("mloc")) {
					Map<String, String> mloc = (Map<String, String>) result.get("mloc");
					String MLOC_REGNM = (mloc.get("MLOC_REGNM") == null ? "" : mloc.get("MLOC_REGNM")).toString();
					String MLOC_ADDR1 = mloc.get("MLOC_ADDR1");
					String MLOC_TELNO = mloc.get("MLOC_TELNO");
					String MLOC_GRNPF = mloc.get("MLOC_GRNPF");
					double mloc_x = Double.parseDouble(MLOC_GRNPF.split(",")[0]);
					double mloc_y = Double.parseDouble(MLOC_GRNPF.split(",")[1]);
					
					
					
					List<weixin.popular.bean.message.message.NewsMessage.Article> articles=new ArrayList<weixin.popular.bean.message.message.NewsMessage.Article>();
					weixin.popular.bean.message.message.NewsMessage.Article article=new weixin.popular.bean.message.message.NewsMessage.Article(MLOC_REGNM, "地址：" + MLOC_ADDR1 + "\n" + "电话：" + MLOC_TELNO + "\n" + "距离：" + d + "公里", "http://api.map.baidu.com/marker?location=" + mloc_y + "," + mloc_x + "&title=" + MLOC_REGNM + "&content=" + MLOC_ADDR1 + "&output=html", "http://api.map.baidu.com/staticimage?width=400&height=300&center=" + mloc_x + "," + mloc_y + "&zoom=" + 15 + "&markers=" + mloc_x + "," + mloc_y + "&markerStyles=l");
					articles.add(article);
					
					/**
					 * 需要推送的客服消息
					 */
					Message message=new weixin.popular.bean.message.message.NewsMessage(fromUserName,articles);
					String accessToken = wxCacheService.getToken();
					MessageAPI.messageCustomSend(accessToken, message);
				}else{
					TextMessage sendTextMessage=new TextMessage(fromUserName, "抱歉，您周边未查询到专营店！");
					sendTextMessage.getText().setContent(getMainMenu());
					String accessToken = wxCacheService.getToken();
					MessageAPI.messageCustomSend(accessToken, sendTextMessage);
				}
				try {
					FunctionLog functionLog = new FunctionLog();
					functionLog.setFunctionName("附近店铺");
					functionLog.setEventKey("LOCATION");
					functionLog.setOpenid(fromUserName);
					functionLog.setCredateDate(new Date());
					functionLogService.save(functionLog);
				} catch (Exception e) {
					logger.info("附近店铺查询异常", e);
				}
			}
		} catch (Exception e) {
			logger.info("微信请求doMyMapEvent", e);
		}
	}

	
	
	@SuppressWarnings("unchecked")
	public static String testGetRequest(double x, double y) throws IllegalStateException, IOException {
		String locationCity = null;
		HttpClient client = new HttpClient();
		StringBuilder sb = new StringBuilder();
		InputStream ins = null;
		// Create a method instance.
		GetMethod method = new GetMethod("http://api.map.baidu.com/geocoder/v2/?ak=" + ResourceUtil.getConfigByName("ak") + "&location=" + x + "," + y + "&output=json&pois=0");
		// Provide custom retry handler is necessary
		method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(3, false));
		try {
			// Execute the method.
			int statusCode = client.executeMethod(method);
			if (statusCode == HttpStatus.SC_OK) {
				ins = method.getResponseBodyAsStream();
				byte[] b = new byte[1024];
				int r_len = 0;
				while ((r_len = ins.read(b)) > 0) {
					sb.append(new String(b, 0, r_len, method.getResponseCharSet()));
				}
			} else {
				System.err.println("Response Code: " + statusCode);
			}
		} catch (HttpException e) {
			System.err.println("Fatal protocol violation: " + e.getMessage());
		} catch (IOException e) {
			System.err.println("Fatal transport error: " + e.getMessage());
		} finally {
			method.releaseConnection();
			if (ins != null) {
				ins.close();
			}
		}
		if (StringUtils.isNotBlank(sb.toString())) {
			JSONObject json = JSONObject.parseObject(sb.toString());
			if (json.getInteger("status") == 0) {
				Map<String, Object> result = (Map<String, Object>) json.get("result");
				Map<String, Object> addressComponent = (Map<String, Object>) result.get("addressComponent");
				locationCity = addressComponent.get("city").toString();
			}
		}
		return locationCity;
	}
	
	
	/**
	 * 获取最近店铺
	 * 
	 * @param response
	 * @param lon
	 * @param lat
	 * @param cityname
	 */
	public Map<String, Object> getStroebyMap(Double lon, Double lat, String cityname) {
		Map<String, Object> result = new HashMap<String, Object>();
		return result;
	}
	
}
