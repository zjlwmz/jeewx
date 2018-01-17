package cn.emay.modules.sys.entity;

import java.util.List;
import java.util.Map;


/**
 * 
 * @Title 在线用户对象
 * @author zjlwm
 * @date 2017-2-20 上午11:46:51
 *
 */
public class Client implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	
	private User user;

	private Map<String, Function> functions;
	private Map<Integer, List<Function>> functionMap;
	public Map<Integer, List<Function>> getFunctionMap() {
		return functionMap;
	}

	public void setFunctionMap(Map<Integer, List<Function>> functionMap) {
		this.functionMap = functionMap;
	}

	/**
	 * 用户IP
	 */
	private java.lang.String ip;
	
	
	/**
	 *登录时间
	 */
	private java.util.Date logindatetime;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}


	public Map<String, Function> getFunctions() {
		return functions;
	}

	public void setFunctions(Map<String, Function> functions) {
		this.functions = functions;
	}

	public java.lang.String getIp() {
		return ip;
	}

	public void setIp(java.lang.String ip) {
		this.ip = ip;
	}

	public java.util.Date getLogindatetime() {
		return logindatetime;
	}

	public void setLogindatetime(java.util.Date logindatetime) {
		this.logindatetime = logindatetime;
	}


}
