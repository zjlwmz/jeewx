package cn.emay.modules.wx.utils;

import javax.websocket.Session;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 功能说明：用来存储业务定义的sessionId和连接的对应关系
 * 		   利用业务逻辑中组装的sessionId获取有效连接后进行后续操作
 * @Title 
 * @author zjlwm
 * @date 2017-2-13 下午5:18:23
 *
 */
public class SessionUtils {

	/**
	 * session map列表
	 */
    public static Map<String, Session> clients = new ConcurrentHashMap<String, Session>();

    /**
     * session key列表
     */
    public static List<String>clientsKeyList=new ArrayList<String>();
    
    public static void put(String userCode, Session session){
        clients.put(userCode, session);
        if(!clientsKeyList.contains(userCode)){
        	 clientsKeyList.add(userCode);
        }
    }

    public static Session get(String userCode){
        return clients.get(userCode);
    }

    public static void remove(String userCode){
        clients.remove(userCode);
        for(int i=clientsKeyList.size()-1;i>0;i--){
        	String key=clientsKeyList.get(i);
        	if(key.equals(userCode)){
        		clientsKeyList.remove(i);
        	}
        }
    }

    /**
     * 判断是否有连接
     * @param relationId
     * @param userCode
     * @return
     */
    public static boolean hasConnection(String userCode) {
        return clients.containsKey(userCode);
    }

}
