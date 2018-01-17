package cn.emay.modules.wx.dto.im;

public class ImBaseDto {
	
	/**
	 * 0表示成功，其它表示失败
	 */
	private String code;
	
	/**
	 * 失败信息
	 */
	private String msg;

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
	
	
}
