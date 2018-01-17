package cn.emay.modules.sys.utils;

import java.util.Date;

import javax.servlet.http.HttpSession;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.InvalidSessionException;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;

import cn.emay.framework.common.utils.ContextHolderUtils;
import cn.emay.framework.common.utils.ResourceUtil;
import cn.emay.modules.sys.entity.Client;
import cn.emay.modules.sys.entity.User;
import cn.emay.modules.sys.manager.ClientManager;

/**
 * 用户缓存工具
 * @author lenovo
 *
 */
public class UserCacheUtils {

	public static final String LOCAL_CLINET_USER = "LOCAL_CLINET_USER";
	
	/**
	 * 用户当前用户对象
	 * @return
	 */
	public final static  User getCurrentUser(){
		HttpSession session = ContextHolderUtils.getSession();
		if(ClientManager.getInstance().getClient(session.getId())!=null){
			return ClientManager.getInstance().getClient(session.getId()).getUser();
		}else{
			User u = (User) session.getAttribute(ResourceUtil.LOCAL_CLINET_USER);
			Client client = new Client();
	        client.setIp("");
	        client.setLogindatetime(new Date());
	        client.setUser(u);
	        ClientManager.getInstance().addClinet(session.getId(), client);
		}
		return null;
	}
	
	
	public static Session getSession(){
		try{
			Subject subject = SecurityUtils.getSubject();
			Session session = subject.getSession(false);
			if (session == null){
				session = subject.getSession();
			}
			if (session != null){
				return session;
			}
		}catch (InvalidSessionException e){
			
		}
		return null;
	}
}
