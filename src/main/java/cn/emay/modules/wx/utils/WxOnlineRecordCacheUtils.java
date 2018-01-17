package cn.emay.modules.wx.utils;

import java.util.HashMap;

import cn.emay.framework.common.utils.CacheUtils;
import cn.emay.modules.wx.entity.WxOnlineRecord;

/**
 * 微信客服对话缓存工具类
 * @author lenovo
 *
 */
public class WxOnlineRecordCacheUtils {

	private static final String ONLINERECORD_CACHE_NAME = "onlineRecordCache";
	
	
	private static final String ONLINERECORD_CACHE_KEY = "onlineRecordCacheKey";
	
	/**
	 * 获取对话缓存
	 * @param key 用户ID
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static WxOnlineRecord getWxOnlineRecord(String key){
		HashMap<String, WxOnlineRecord> onlineRecordMap;
		if (CacheUtils.get(ONLINERECORD_CACHE_NAME, ONLINERECORD_CACHE_KEY) == null) {
			onlineRecordMap = new HashMap<String, WxOnlineRecord>();
		} else {
			onlineRecordMap = (HashMap<String, WxOnlineRecord>) CacheUtils.get(ONLINERECORD_CACHE_NAME, ONLINERECORD_CACHE_KEY);
		}
		return onlineRecordMap.get(key);
	}
	
	
	/**
	 * 存储一个对话缓存
	 * @param wxOnlineRecord
	 */
	@SuppressWarnings("unchecked")
	public static void putWxOnlineRecord(WxOnlineRecord wxOnlineRecord){
		HashMap<String, WxOnlineRecord> onlineRecordMap;
		if (CacheUtils.get(ONLINERECORD_CACHE_NAME, ONLINERECORD_CACHE_KEY) == null) {
			onlineRecordMap = new HashMap<String, WxOnlineRecord>();
		} else {
			onlineRecordMap = (HashMap<String, WxOnlineRecord>) CacheUtils.get(ONLINERECORD_CACHE_NAME, ONLINERECORD_CACHE_KEY);
		}
		onlineRecordMap.put(wxOnlineRecord.getOpenid(), wxOnlineRecord);
		onlineRecordMap.put(wxOnlineRecord.getUserId(), wxOnlineRecord);
		CacheUtils.put(ONLINERECORD_CACHE_NAME, ONLINERECORD_CACHE_KEY, onlineRecordMap);
	}
	
	
	/**
	 * 删除一个对话缓存
	 * @param key
	 */
	@SuppressWarnings("unchecked")
	public static void removeWxOnlineRecord(String key){
		HashMap<String, WxOnlineRecord> onlineRecordMap;
		if (CacheUtils.get(ONLINERECORD_CACHE_NAME, ONLINERECORD_CACHE_KEY) == null) {
			onlineRecordMap = new HashMap<String, WxOnlineRecord>();
		} else {
			onlineRecordMap = (HashMap<String, WxOnlineRecord>) CacheUtils.get(ONLINERECORD_CACHE_NAME, ONLINERECORD_CACHE_KEY);
		}
		
		WxOnlineRecord wxOnlineRecord=getWxOnlineRecord(key);
		if(null!=wxOnlineRecord){
			onlineRecordMap.remove(wxOnlineRecord.getOpenid());
			onlineRecordMap.remove(wxOnlineRecord.getUserId());
		}
		CacheUtils.put(ONLINERECORD_CACHE_NAME, ONLINERECORD_CACHE_KEY, onlineRecordMap);
	}
}
