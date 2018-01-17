package cn.emay.modules.chat.dto;

import java.util.Map;

/**
 * 上传图片DTO
 * @author lenovo
 *
 */
public class MessageImageDto {

	/**
	 * 0表示成功，其它表示失败
	 */
	private String code;
	
	/**
	 * 失败信息
	 */
	private String msg;
	
	/**
	 * "data": {
		    "src": "http://cdn.xxx.com/upload/images/a.jpg" //图片url
		  }
	 */
	private Map<String,String>data;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Map<String, String> getData() {
		return data;
	}

	public void setData(Map<String, String> data) {
		this.data = data;
	}
	
	
	
	
}
