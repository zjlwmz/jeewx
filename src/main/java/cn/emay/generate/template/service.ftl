
package ${packageName}.${moduleName}.service;

import cn.emay.framework.core.common.service.CommonService;

import ${packageName}.${moduleName}.entity.${ClassName};
/**
 * ${functionName}Service
 * @author ${classAuthor}
 * @version ${classVersion}
 */
public interface ${ClassName}Service extends CommonService {

	public ${ClassName} get(String id);
	
	public void save(${ClassName} ${className});
	
	public void delete(String id);
	
}
