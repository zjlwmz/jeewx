package cn.emay.modules.wx.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.emay.framework.core.common.hibernate.qbc.CriteriaQuery;
import cn.emay.framework.core.common.model.json.DataGrid;
import cn.emay.framework.core.common.service.impl.CommonServiceImpl;
import cn.emay.modules.wx.repository.WxChatLogsDao;
import cn.emay.modules.wx.service.WxChatLogsService;


/**
 * 聊天记录servie接口实现
 * @author lenovo
 *
 */
@Service
@Transactional(readOnly=true)
public class WxChatLogsServiceImpl extends CommonServiceImpl implements WxChatLogsService{

	@Autowired
	private WxChatLogsDao wxChatLogsDao ;
	
	@Override
	public DataGrid findWxChatPage(CriteriaQuery criteriaQuery) {
		wxChatLogsDao.getDataGridReturn(criteriaQuery, true);
		return criteriaQuery.getDataGrid();
	}

}
