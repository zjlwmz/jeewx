package cn.emay.modules.wx.service.impl;


import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.emay.framework.common.utils.DateUtils;
import cn.emay.framework.common.utils.StringUtils;
import cn.emay.framework.core.common.service.impl.CommonServiceImpl;
import cn.emay.modules.wx.entity.WxOnlineRecord;
import cn.emay.modules.wx.repository.WxOnlineRecordDao;
import cn.emay.modules.wx.service.WxOnlineRecordService;


/**
 * 在线客服记录单接口服务实现
 * @author lenovo
 *
 */
@Service
@Transactional(readOnly=true)
public class WxOnlineRecordServiceImpl extends CommonServiceImpl implements WxOnlineRecordService {

	
	
	
	
	/**
	 * 在线客服记录单DAO接口
	 */
	@Autowired
	private WxOnlineRecordDao wxOnlineRecordDao;
	
	@Transactional(readOnly=false)
	@Override
	public void save(WxOnlineRecord wxOnlineRecord) {
		if(StringUtils.isNotBlank(wxOnlineRecord.getId())){
			wxOnlineRecordDao.updateEntitie(wxOnlineRecord);
		}else{
			wxOnlineRecordDao.save(wxOnlineRecord);
		}
	}
	
	
	/**
	 * 等待分派的在线服务记录
	 */
	@Override
	public List<WxOnlineRecord> findNotFinishedWxOnlineRecord() {
		Date current=new Date();
		long oldTime=current.getTime()-2*24*60*60*1000;
		Date pastDate=new Date(oldTime);
		String foratTime=DateUtils.formatDateTime(pastDate);
		return wxOnlineRecordDao.findByQueryString("from WxOnlineRecord where status<2  and DATE_FORMAT(endDate,'%Y-%m-%d %T')>'"+foratTime+"'");
	}


	@Transactional(readOnly=false)
	@Override
	public synchronized void startWxOnlineRecord(WxOnlineRecord wxOnlineRecord) {
		wxOnlineRecord.setStatus(1);
		wxOnlineRecord.setEndDate(new Date());
		wxOnlineRecordDao.updateEntitie(wxOnlineRecord);
	}

	
	@Transactional(readOnly=false)
	@Override
	public synchronized void createWxOnlineRecord(WxOnlineRecord wxOnlineRecord) {
		Date current=new Date();
		long oldTime=current.getTime()-1*60*60*1000;//一小时
		Date pastDate=new Date(oldTime);
		String foratTime=DateUtils.formatDateTime(pastDate);
		List<WxOnlineRecord>wxOnlineRecordList=wxOnlineRecordDao.findByQueryString("from WxOnlineRecord where status=0 and openid='"+wxOnlineRecord.getOpenid()+"' and DATE_FORMAT(endDate,'%Y-%m-%d %T')>'"+foratTime+"'");
		if(wxOnlineRecordList.size()==0){
			wxOnlineRecordDao.save(wxOnlineRecord);
		}
		
	}


	@Override
	public List<WxOnlineRecord> finWxOnlineRecordByReplyStatus(Integer replyStatus) {
		return wxOnlineRecordDao.findByQueryString("from WxOnlineRecord where status=1 and replyStatus="+replyStatus);
	}


	@Override
	public WxOnlineRecord getCurrentWxOnlineRecord(String openid) {
		Date current=new Date();
		long oldTime=current.getTime()-2*24*60*60*1000;
		Date pastDate=new Date(oldTime);
		String foratTime=DateUtils.formatDateTime(pastDate);
		List<WxOnlineRecord>wxOnlineRecordList=wxOnlineRecordDao.findByQueryString("from WxOnlineRecord where status=1 and openid='"+openid+"' and DATE_FORMAT(endDate,'%Y-%m-%d %T')>'"+foratTime+"'");
		if(wxOnlineRecordList.size()>0){
			return wxOnlineRecordList.get(0);
		}
		return null;
	}


	


}
