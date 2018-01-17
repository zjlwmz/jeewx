package cn.emay.modules.wx.utils;

import java.io.InputStream;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import cn.emay.modules.wx.utils.message.resp.Article;
import cn.emay.modules.wx.utils.message.resp.MusicMessageResp;
import cn.emay.modules.wx.utils.message.resp.NewsMessageResp;
import cn.emay.modules.wx.utils.message.resp.TextMessageResp;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.core.util.QuickWriter;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.io.xml.XppDriver;

public class MessageUtil {
	/**
	 * 返回消息类型：文本
	 */
	public static final String RESP_MESSAGE_TYPE_TEXT = "text";

	/**
	 * 返回消息类型：模板消息
	 */
	public static final String RESP_MESSAGE_TYPE_TEMPLATE = "template";

	/**
	 * 返回消息类型：音乐
	 */
	public static final String RESP_MESSAGE_TYPE_MUSIC = "music";

	/**
	 * 返回消息类型：图文
	 */
	public static final String RESP_MESSAGE_TYPE_NEWS = "news";
	
	/**
	 * 在线客服使用
	 * 开始会话
	 */
	public static final String RESP_MESSAGE_TYPE_SESSION="session";

	/**
	 * 请求消息类型：文本
	 */
	public static final String REQ_MESSAGE_TYPE_TEXT = "text";

	/**
	 * 请求消息类型：图片
	 */
	public static final String REQ_MESSAGE_TYPE_IMAGE = "image";

	/**
	 * 请求消息类型：链接
	 */
	public static final String REQ_MESSAGE_TYPE_LINK = "link";

	/**
	 * 请求消息类型：地理位置
	 */
	public static final String REQ_MESSAGE_TYPE_LOCATION = "location";

	/**
	 * 请求消息类型：音频
	 */
	public static final String REQ_MESSAGE_TYPE_VOICE = "voice";
	
	/**
	 * 请求消息类型 ：视频
	 */
	public static final String REQ_MESSAGE_TYPE_VIDEO="video";
	
	/**
	 * 请求消息类型 ：小视频
	 */
	public static final String REQ_MESSAGE_TYPE_SHORTVIDEO="shortvideo";

	/**
	 * 请求消息类型：推送
	 */
	public static final String REQ_MESSAGE_TYPE_EVENT = "event";

	/**
	 * 事件类型：subscribe(订阅)
	 */
	public static final String EVENT_TYPE_SUBSCRIBE = "subscribe";

	/**
	 * 事件类型：unsubscribe(取消订阅)
	 */
	public static final String EVENT_TYPE_UNSUBSCRIBE = "unsubscribe";

	/**
	 * 事件类型：CLICK(自定义菜单点击事件)
	 */
	public static final String EVENT_TYPE_CLICK = "CLICK";

	/**
	 * 事件类型：VIEW(自定义菜单点击事件)
	 */
	public static final String EVENT_TYPE_VIEW = "VIEW";

	/**
	 * 事件KEY值，qrscene_为前缀，后面为二维码的参数值(扫描带参数二维码事件)
	 */
	public static final String EVENT_KEY_QRSCENE = "qrscene_";
	
	
	/**
	 * 在模版消息发送任务完成后，微信服务器会将是否送达成功作为通知，发送到开发者中心中填写的服务器配置地址中。
	 */
	public static final String EVENT_TYPE_TEMPLATESENDJOBFINISH="TEMPLATESENDJOBFINISH";
	
	/**
	 * 群发消息任务完成后，微信服务器会将是否送达成功作为通知，发送到开发者中心中填写的服务器配置地址中。
	 */
	public static final String EVENT_TYPE_MASSSENDJOBFINISH="MASSSENDJOBFINISH";
	
	/**
	 * 用户同意上报地理位置后，每次进入公众号会话时，都会在进入时上报地理位置，上报地理位置
	 */
	public static final String EVENT_TYPE_LOCATION="LOCATION";
	
	/**
	 * 扫描带参数二维码事件
	 * 如果用户已经关注公众号，则微信会将带场景值扫描事件推送给开发者
	 */
	public static final String EVENT_TYPE_SCAN="SCAN";
	

	/**
	 * 解析微信发来的请求（XML）
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, String> parseXml(HttpServletRequest request) throws Exception {
		// 将解析结果存储在HashMap中
		Map<String, String> map = new HashMap<String, String>();

		// 从request中取得输入流
		InputStream inputStream = request.getInputStream();
		// 读取输入流
		SAXReader reader = new SAXReader();
		Document document = reader.read(inputStream);
		// 得到xml根元素
		Element root = document.getRootElement();
		// 得到根元素的所有子节点
		List<Element> elementList = root.elements();

		// 遍历所有子节点
		for (Element e : elementList)
			map.put(e.getName(), e.getText());

		// 释放资源
		inputStream.close();
		inputStream = null;

		return map;
	}

	/**
	 * 文本消息对象转换成xml
	 * 
	 * @param textMessage
	 *            文本消息对象
	 * @return xml
	 */
	public static String textMessageToXml(TextMessageResp textMessage) {
		xstream.alias("xml", textMessage.getClass());
		return xstream.toXML(textMessage);
	}

	/**
	 * 音乐消息对象转换成xml
	 * 
	 * @param musicMessage
	 *            音乐消息对象
	 * @return xml
	 */
	public static String musicMessageToXml(MusicMessageResp musicMessage) {
		xstream.alias("xml", musicMessage.getClass());
		return xstream.toXML(musicMessage);
	}

	/**
	 * 图文消息对象转换成xml
	 * 
	 * @param newsMessage
	 *            图文消息对象
	 * @return xml
	 */
	public static String newsMessageToXml(NewsMessageResp newsMessage) {
		xstream.alias("xml", newsMessage.getClass());
		xstream.alias("item", new Article().getClass());
		return xstream.toXML(newsMessage);
	}

	/**
	 * 扩展xstream，使其支持CDATA块
	 * 
	 * @date 2013-05-19
	 */
	private static XStream xstream = new XStream(new XppDriver() {
		public HierarchicalStreamWriter createWriter(Writer out) {
			return new PrettyPrintWriter(out) {
				// 对所有xml节点的转换都增加CDATA标记
				boolean cdata = true;

				@SuppressWarnings("rawtypes")
				public void startNode(String name, Class clazz) {
					super.startNode(name, clazz);
				}

				protected void writeText(QuickWriter writer, String text) {
					if (cdata) {
						writer.write("<![CDATA[");
						writer.write(text);
						writer.write("]]>");
					} else {
						writer.write(text);
					}
				}
			};
		}
	});
}
