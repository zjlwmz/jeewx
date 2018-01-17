package cn.emay.modules.wx.response;

import weixin.popular.bean.message.EventMessage;


/**
 * 针对不同类型的相应接口
 * @author lenovo
 */
public interface ResponseService {

	public void execute(EventMessage eventMessage);
}
