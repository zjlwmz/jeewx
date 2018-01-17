package cn.emay.framework.common.utils;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

/**
 * Cache工具类
 * 
 * @author zjlWm
 * @version 2015-5-14 cacheName在ehcache.xml中配置
 */
public class CacheUtils {

	private static CacheManager cacheManager = ((CacheManager) SpringContextHolder.getBean("ehcacheManagerFactory"));
	

	/**
	 * 微信缓存
	 */
	private static final String WX_CACHE = "wxCache";

	public static Object get(String key) {
		return get(WX_CACHE, key);
	}

	public static void put(String key, Object value) {
		put(WX_CACHE, key, value);
	}

	public static void remove(String key) {
		remove(WX_CACHE, key);
	}

	public static Object get(String cacheName, String key) {
		Element element = getCache(cacheName).get(key);
		return element == null ? null : element.getObjectValue();
	}

	public static Object get(String cacheName, Object key) {
		Cache cache = getCache(cacheName);
		if (cache != null) {
			Element element = cache.get(key);
			if (element != null) {
				return element.getObjectValue();
			}
		}
		return null;
	}

	public static void put(String cacheName, String key, Object value) {
		Element element = new Element(key, value);
		getCache(cacheName).put(element);
	}

	public static void put(String cacheName, Object key, Object value) {
		Cache cache = getCache(cacheName);
		if (cache != null) {
			cache.put(new Element(key, value));
		}
	}

	public static void remove(String cacheName, String key) {
		getCache(cacheName).remove(key);
	}

	/**
	 * 获得一个Cache，没有则创建一个。
	 * 
	 * @param cacheName
	 * @return
	 */
	private static Cache getCache(String cacheName) {
		Cache cache = cacheManager.getCache(cacheName);
		if (cache == null) {
			cacheManager.addCache(cacheName);
			cache = cacheManager.getCache(cacheName);
			cache.getCacheConfiguration().setEternal(true);
		}
		return cache;
	}

	public static CacheManager getCacheManager() {
		return cacheManager;
	}

}
