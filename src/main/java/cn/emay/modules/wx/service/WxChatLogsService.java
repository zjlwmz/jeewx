package cn.emay.modules.wx.service;

import cn.emay.framework.core.common.hibernate.qbc.CriteriaQuery;
import cn.emay.framework.core.common.model.json.DataGrid;
import cn.emay.framework.core.common.service.CommonService;


/**
 * 聊天记录servie接口
 * @author lenovo
 *
 */
public interface WxChatLogsService extends CommonService{

	/**
	 * 分页查询
	 * @return
	 */
	public DataGrid findWxChatPage(CriteriaQuery criteriaQuery);
}
