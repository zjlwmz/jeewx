package cn.emay.modules.wx.utils;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import cn.emay.framework.common.utils.FileUtils;
import cn.emay.framework.common.utils.PropertiesUtil;

/**
 * 表情解析工具
 * @author lenovo
 * http://www.oicqzone.com/tool/emoji/
 */
public class EmojiUtils {

	/**
	 * 微信表情集合
	 */
	private static Map<String,String>emojiMap=new HashMap<String, String>();
	
	
	/**
	 * 符号表情集合
	 */
	private static Map<String,String>symbolEmojiMap=new HashMap<String, String>();
	
	
	private static EmojiUtils emojiUtils;
	
	public static EmojiUtils getInstance(){
		if(null==emojiUtils){
			emojiUtils=new EmojiUtils();
		}
		return emojiUtils;
	}
	
	/**
	 * 微信表情
	 */
	public  void initEmoji(){
		if(emojiMap.size()==0){
			PropertiesUtil propertiesUtil = new PropertiesUtil("emoji-kin/wechat_emotion.properties");
			Properties properties=propertiesUtil.getProperties();
			Set<Object>setKey=propertiesUtil.getProperties().keySet();
			Iterator<Object> iterator=setKey.iterator();
			while (iterator.hasNext()) {
				  String emojiKey = iterator.next().toString();  
				  String emojiValue=properties.get(emojiKey).toString();
				  emojiMap.put(emojiKey, emojiValue);
			}
		}
	}
	
	
	/**
	 * 表情解析
	 * @param content
	 * @return
	 */
	public  String analysisContent(String content){
		
		/**
		 * 微信表情
		 */
		initEmoji();
		Set<String>setKey=emojiMap.keySet();
		Iterator<String> iterator=setKey.iterator();
		while (iterator.hasNext()) {
			  String emojiKey = iterator.next(); 
			  String emojiValue=emojiMap.get(emojiKey);
			  content=content.replace(emojiKey, emojiValue);
		}
		
		return content;
	}
	
	
	
	
	
	/**
	 * 表情解析
	 * @param content
	 * @return
	 */
	public String analysisSymbolContent(String domain,String wxPath,String content){
		/**
		 * 符号
		 */
		initSymbolEoji(wxPath);
		String staticPathUnified="static/emoji-kin/emojis/unified";
		Set<String>setSymbolKey=symbolEmojiMap.keySet();
		Iterator<String> iteratorSymbol=setSymbolKey.iterator();
		while (iteratorSymbol.hasNext()) {
			  String emojiKey = iteratorSymbol.next(); 
			  String emojiValue=symbolEmojiMap.get(emojiKey);
			  String imageUrl=staticPathUnified+"/"+emojiValue;
			  String imageHtml="<img src="+imageUrl+" />";
			  content=content.replace(emojiKey, imageHtml);
		}
		
		
		return content;
	}
	
	
	
	/**
	 * 符号表情
	 */
	public void initSymbolEoji(String wxPath){
		if(symbolEmojiMap.size()==0){
			String staticPathUnified="/static/emoji-kin/emojis/unified";
			String symbolEojoPath=wxPath+staticPathUnified;
			File symbolEojoFile=new File(symbolEojoPath);
			File[]childList=symbolEojoFile.listFiles();
			for(int i=0;i<childList.length;i++){
				if(childList[i].isFile()){
					String fileName=childList[i].getName();
					String key=FileUtils.getFileNameNoEx(fileName);
					symbolEmojiMap.put(key, fileName);
				}
			}
		}
	}
	
	
	
	
	
	public static void main(String[] args) {
		File f=new File("F:/1/3/emoji-master/emoji/00a9.png");
		System.out.println(FileUtils.getFileNameNoEx(f.getName()));
	}
}
