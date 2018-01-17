package cn.emay.modules.wx.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.emay.framework.common.utils.MyBeanUtils;
import cn.emay.framework.common.utils.StringUtils;
import cn.emay.framework.core.common.exception.BusinessException;
import cn.emay.framework.core.common.service.impl.CommonServiceImpl;
import cn.emay.modules.wx.entity.Templatemessage;
import cn.emay.modules.wx.entity.TemplatemessageItem;
import cn.emay.modules.wx.repository.TemplatemessageDao;
import cn.emay.modules.wx.service.TemplatemessageService;

/**
 * 模板消息service接口实现
 * 
 * @author lenovo
 * 
 */
@Service
@Transactional(readOnly = true)
public class TemplatemessageServiceImpl extends CommonServiceImpl implements TemplatemessageService {

	/**
	 * 模板消息DAO接口
	 */
	@Autowired
	private TemplatemessageDao templatemessageDao;

	@Override
	public Templatemessage get(String templatemessageId) {
		return templatemessageDao.get(Templatemessage.class, templatemessageId);
	}

	@Override
	public List<Templatemessage> findTemplatemessage(String wechatId) {
		return templatemessageDao.findByQueryString(" from Templatemessage  where wechatId='" + wechatId + "'");
	}

	@Transactional(readOnly = false)
	@Override
	public void delMain(Templatemessage templatemessage) {
		// 删除主表信息
		this.delete(templatemessage);
		// ===================================================================================
		// 获取参数
		Object id0 = templatemessage.getId();
		// ===================================================================================
		// 删除-模板消息明细
		String hql0 = "from TemplatemessageItem where 1 = 1 AND templatemessageId = ? ";
		List<TemplatemessageItem> templatemessageItemOldList = templatemessageDao.findHql(hql0, id0);
		templatemessageDao.deleteAllEntitie(templatemessageItemOldList);
	}

	@Transactional(readOnly = false)
	@Override
	public void addMain(Templatemessage templatemessage, List<TemplatemessageItem> templatemessageItemList) {
		// 保存主信息
		templatemessageDao.save(templatemessage);

		/** 保存-模板消息明细 */
		for (TemplatemessageItem templatemessageItem : templatemessageItemList) {
			// 外键设置
			templatemessageItem.setTemplatemessageId(templatemessage.getId());
			this.save(templatemessageItem);
		}
	}

	@Transactional(readOnly = false)
	@Override
	public void updateMain(Templatemessage templatemessage, List<TemplatemessageItem> templatemessageItemList) {
		// 保存主表信息
		templatemessage.setUpdateDate(new Date());
		this.saveOrUpdate(templatemessage);
		// ===================================================================================
		// 获取参数
		Object id0 = templatemessage.getId();
		// ===================================================================================
		// 1.查询出数据库的明细数据-模板消息明细
		String hql0 = "from TemplatemessageItem where 1 = 1 AND templatemessageId = ? ";
		List<TemplatemessageItem> templatemessageItemOldList = this.findHql(hql0, id0);
		// 2.筛选更新明细数据-模板消息明细
		for (TemplatemessageItem oldE : templatemessageItemOldList) {
			boolean isUpdate = false;
			for (TemplatemessageItem sendE : templatemessageItemList) {
				// 需要更新的明细数据-模板消息明细
				if (oldE.getId().equals(sendE.getId())) {
					try {
						MyBeanUtils.copyBeanNotNull2Bean(sendE, oldE);
						this.saveOrUpdate(oldE);
					} catch (Exception e) {
						e.printStackTrace();
						throw new BusinessException(e.getMessage());
					}
					isUpdate = true;
					break;
				}
			}
			if (!isUpdate) {
				// 如果数据库存在的明细，前台没有传递过来则是删除-模板消息明细
				super.delete(oldE);
			}

		}
		// 3.持久化新增的数据-模板消息明细
		for (TemplatemessageItem templatemessageItem : templatemessageItemList) {

			if (StringUtils.isEmpty(templatemessageItem.getId())) {
				// 外键设置
				templatemessageItem.setTemplatemessageId(templatemessage.getId());
				this.save(templatemessageItem);
			}
		}
	}

}
