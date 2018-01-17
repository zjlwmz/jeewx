package cn.emay.modules.wx.service;

import java.util.List;

import cn.emay.framework.core.common.service.CommonService;
import cn.emay.modules.wx.entity.WxOnlineRecord;


/**
 * 在线客服记录单接口服务
 * @author lenovo
 *
 */
public interface WxOnlineRecordService extends CommonService{

	public void save(WxOnlineRecord wxOnlineRecord);
	
	
	/**
	 * 未结束对话列表
	 */
	public List<WxOnlineRecord>findNotFinishedWxOnlineRecord();
	
	
	/**
	 *  等待答复记录 
	 */
	public List<WxOnlineRecord>finWxOnlineRecordByReplyStatus(Integer replyStatus);
	
	/**
	 * 创建一个等接入的在线对话
	 * @param wxOnlineRecord
	 */
	public void createWxOnlineRecord(WxOnlineRecord wxOnlineRecord);
	
	/**
	 * 开始在线对话
	 */
	public void startWxOnlineRecord(WxOnlineRecord wxOnlineRecord);
	
	
	
	
	/**
	 * 获取正在对话中的在线客服记录单
	 */
	public WxOnlineRecord getCurrentWxOnlineRecord(String openid);
}
