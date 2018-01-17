package cn.emay.modules.wx.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.emay.framework.common.utils.DateUtils;
import cn.emay.framework.core.common.service.impl.CommonServiceImpl;
import cn.emay.modules.wx.entity.WxFansSession;
import cn.emay.modules.wx.repository.WxFansSessionDao;
import cn.emay.modules.wx.service.WxFansSessionService;


/**
 * 微信会话service接口实现
 * @author lenovo
 *
 */
@Service
@Transactional(readOnly=true)
public class WxFansSessionServiceImpl extends CommonServiceImpl implements WxFansSessionService {

	@Autowired
	private WxFansSessionDao wxFansSessionDao;
	
	@Override
	public WxFansSession get(String id) {
		return wxFansSessionDao.get(WxFansSession.class, id);
	}

	@Transactional
	@Override
	public synchronized void save(WxFansSession wxFansSession) {
		if(StringUtils.isNotBlank(wxFansSession.getId())){
			wxFansSessionDao.updateEntitie(wxFansSession);
		}else{
			wxFansSessionDao.save(wxFansSession);
		}
	}


	@Override
	public WxFansSession findCurrentWxFansSessionByOpenid(String openid) {
		List<WxFansSession>wxFansSessionList=wxFansSessionDao.findByQueryString("from WxFansSession where openid ='"+openid+"' and satus=1");
		if(wxFansSessionList.size()>0){
			return wxFansSessionList.get(0);
		}else{
			return null;
		}
	}
	
	
	/**
	 * 查询在48小时以内
	 */
	public List<WxFansSession>findThePast48Hourse(){
		Date current=new Date();
		long oldTime=current.getTime()-2*24*60*60*1000;
		Date pastDate=new Date(oldTime);
		String foratTime=DateUtils.formatDateTime(pastDate);
		return wxFansSessionDao.findByQueryString("from WxFansSession where DATE_FORMAT(endSessionTime,'%Y-%m-%d %T')>'"+foratTime+"'");
	}

	/**
	 * 查询在48小时以前
	 */
	public List<WxFansSession>find48HourseAgo(){
		Date current=new Date();
		long oldTime=current.getTime()-2*24*60*60*1000;
		Date pastDate=new Date(oldTime);
		String foratTime=DateUtils.formatDateTime(pastDate);
		return wxFansSessionDao.findByQueryString("from WxFansSession where DATE_FORMAT(endSessionTime,'%Y-%m-%d %T')<='"+foratTime+"'");
	}
	
	/**
	 * 更新剩余时间
	 */
	@Override
	@Transactional
	public synchronized void  updateWxFansSessionSurplusDate(WxFansSession wxFansSession) {
		Date currentDate=new Date();
		long surplusTime=currentDate.getTime()/1000-wxFansSession.getEndSessionTime().getTime()/1000;
		wxFansSession.setSurplusDate(surplusTime);
		wxFansSessionDao.updateEntitie(wxFansSession);
	}
	
}
