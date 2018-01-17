package cn.emay.modules.wx.im.request;


/**
 * 监听发送消息 数据结构
 * @Title layim
 * @author zjlwm
 * @date 2017-2-13 上午9:22:20
 *
 */
public class ResMessage {

	/**
	 * 我的信息
	 */
	private ResMine mine;
	
	
	/**
	 * 对方的信息
	 */
	private ResTo to;


	public ResMine getMine() {
		return mine;
	}


	public void setMine(ResMine mine) {
		this.mine = mine;
	}


	public ResTo getTo() {
		return to;
	}


	public void setTo(ResTo to) {
		this.to = to;
	}
	
	
	
}
