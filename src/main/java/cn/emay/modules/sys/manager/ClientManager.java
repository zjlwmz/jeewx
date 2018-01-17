package cn.emay.modules.sys.manager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import org.apache.commons.lang3.StringUtils;

import cn.emay.framework.common.utils.CacheUtils;
import cn.emay.framework.common.utils.ContextHolderUtils;
import cn.emay.modules.sys.entity.Client;
import cn.emay.modules.sys.entity.User;

/**
 * 对在线用户的管理
 * 
 * @author JueYue
 * @date 2013-9-28
 * @version 1.0
 */
// add-begin-Author:xugj Date:20160228 for:#859
// 【jeecg分布式改造】目前用户登录session存在class级别缓存导致分布式
public class ClientManager {
	private final String CACHENAME = "eternalCache";
	private final String OnlineClientsKey = "onLineClients";
	
	/**
	 * 在线客服队列
	 */
	private final String OnlineCustomerServerClientKey="onLineCustomerServiceClients";

	private static ClientManager instance = new ClientManager();

	private ClientManager() {

	}

	public static ClientManager getInstance() {
		return instance;
	}

	
	
	/**
	 * 获取在线客服列表
	 */
	@SuppressWarnings("unchecked")
	public Collection<User> getOnlineCustomerServiceClient() {
		HashMap<String, User> onLineClients = (HashMap<String, User>) CacheUtils.get(CACHENAME, OnlineCustomerServerClientKey);
		if(null!=onLineClients){
			return onLineClients.values();
		}else{
			return new ArrayList<User>();
		}
	}
	
	/**
	 * 保存在线客服队列
	 * @param User
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public boolean addOnlineCustomerServiceClient(User User){
		HashMap<String, User> onLineClients;
		if (CacheUtils.get(CACHENAME, OnlineCustomerServerClientKey) == null) {
			onLineClients = new HashMap<String, User>();
		} else {
			onLineClients = (HashMap<String, User>) CacheUtils.get(CACHENAME, OnlineCustomerServerClientKey);
		}
		onLineClients.put(User.getId(), User);
		CacheUtils.put(CACHENAME, OnlineCustomerServerClientKey, onLineClients);
		return true;
	}
	
	
	
	@SuppressWarnings("unchecked")
	public User getOnLineClientsByUserId(String userid){
		HashMap<String, User> onLineClients=(HashMap<String, User>) CacheUtils.get(CACHENAME, OnlineCustomerServerClientKey);
		if (onLineClients != null) {
			User user=onLineClients.get(userid);
			return user;
		}
		return null;
	}
	
	
	/**
	 * 
	 * 移除已经不在线的客服队列
	 * 
	 */
	@SuppressWarnings("unchecked")
	public boolean removeOnlineCustomerServiceClient(User User){
		HashMap<String, User> onLineClients;
		if (CacheUtils.get(CACHENAME, OnlineCustomerServerClientKey) != null) {
			onLineClients = (HashMap<String, User>) CacheUtils.get(CACHENAME, OnlineCustomerServerClientKey);
			onLineClients.remove(User.getId());
			CacheUtils.put(CACHENAME, OnlineCustomerServerClientKey, onLineClients);
			return true;
		}
		return false;
	}
	/**
	 * 向ehcache缓存中增加Client对象
	 * 
	 * @author xugj
	 * */
	@SuppressWarnings("unchecked")
	private boolean addClientToCachedMap(String sessionId, Client client) {
		HashMap<String, Client> onLineClients;
		if (CacheUtils.get(CACHENAME, OnlineClientsKey) == null) {
			onLineClients = new HashMap<String, Client>();
		} else {
			onLineClients = (HashMap<String, Client>) CacheUtils.get(CACHENAME, OnlineClientsKey);
		}
		onLineClients.put(sessionId, client);
		CacheUtils.put(CACHENAME, OnlineClientsKey, onLineClients);
		return true;
	}

	
	
	/**
	 * 从缓存中的Client集合中删除 Client对象
	 * */
	@SuppressWarnings("unchecked")
	private boolean removeClientFromCachedMap(String sessionId) {
		HashMap<String, Client> onLineClients;
		if (CacheUtils.get(CACHENAME, OnlineClientsKey) != null) {
			onLineClients = (HashMap<String, Client>) CacheUtils.get(CACHENAME, OnlineClientsKey);
			onLineClients.remove(sessionId);
			CacheUtils.put(CACHENAME, OnlineClientsKey, onLineClients);
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 用户登录，向session中增加用户信息
	 * 
	 * @param sessionId
	 * @param client
	 */
	public void addClinet(String sessionId, Client client) {
		ContextHolderUtils.getSession().setAttribute(sessionId, client);
		if (client != null) {
			Client ret = new Client();
			ret.setIp(client.getIp());
			ret.setLogindatetime(client.getLogindatetime());
			ret.setUser(client.getUser());
			addClientToCachedMap(sessionId, ret);
		}
	}

	/**
	 * 用户退出登录 从Session中删除用户信息 sessionId
	 */
	public void removeClinet(String sessionId) {
		ContextHolderUtils.getSession().removeAttribute(sessionId);
		removeClientFromCachedMap(sessionId);
	}

	/**
	 * 根据sessionId 得到Client 对象
	 * 
	 * @param sessionId
	 */
	public Client getClient(String sessionId) {
		if (!StringUtils.isEmpty(sessionId) && ContextHolderUtils.getSession().getAttribute(sessionId) != null) {
			return (Client) ContextHolderUtils.getSession().getAttribute(sessionId);
		} else {
			return null;
		}
	}

	/**
	 * 得到Client 对象
	 */
	public Client getClient() {
		String sessionId = ContextHolderUtils.getSession().getId();
		if (!StringUtils.isEmpty(sessionId) && ContextHolderUtils.getSession().getAttribute(sessionId) != null) {
			return (Client) ContextHolderUtils.getSession().getAttribute(sessionId);
		} else {
			return null;
		}
	}

	/**
	 * 得到所有在线用户
	 */
	@SuppressWarnings("unchecked")
	public Collection<Client> getAllClient() {
		if (CacheUtils.get(CACHENAME, OnlineClientsKey) != null) {
			HashMap<String, Client> onLineClients = (HashMap<String, Client>) CacheUtils.get(CACHENAME, OnlineClientsKey);
			return onLineClients.values();
		} else{
			return new ArrayList<Client>();
		}
	}
}
// add-end-Author:xugj Date:20160228 for:#859
// 【jeecg分布式改造】目前用户登录session存在class级别缓存导致分布式
