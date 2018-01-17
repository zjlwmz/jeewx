package cn.emay.modules.sys.service;

import java.util.List;

import cn.emay.framework.core.common.service.CommonService;
import cn.emay.modules.sys.entity.Attachment;

/**
 * 
 * @author  张代浩
 *
 */
public interface DeclareService extends CommonService{
	
	public List<Attachment> getAttachmentsByCode(String businessKey,String description);
	
}
