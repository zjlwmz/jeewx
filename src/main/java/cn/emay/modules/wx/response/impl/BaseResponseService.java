package cn.emay.modules.wx.response.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.emay.modules.sys.service.ParamsService;



/**
 * 针对不同类型的相应接口
 * @author lenovo
 *
 */
@Component
public class BaseResponseService {

	/**
	 * 参数service接口
	 */
	@Autowired
	private ParamsService paramsService;
	
	/**
	 * 欢迎语
	 * 
	 * @return
	 */
	public String getMainMenu() {
		/**
		 * 默认key值
		 */
		String wxCusKeyDefault=paramsService.findParamsByName("wx.cus.key.default");
		
		return wxCusKeyDefault;
	}
	
	
}
