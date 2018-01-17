package cn.emay.modules.sys.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.emay.framework.core.common.service.impl.CommonServiceImpl;
import cn.emay.modules.sys.entity.Attachment;
import cn.emay.modules.sys.service.DeclareService;


@Service("declareService")
@Transactional
public class DeclareServiceImpl extends CommonServiceImpl implements DeclareService {

	public List<Attachment> getAttachmentsByCode(String businessKey,String description)
	{
		String hql="from Attachment t where t.businessKey='"+businessKey+"' and t.description='"+description+"'";
		return commonDao.findByQueryString(hql);
	}
	
}
