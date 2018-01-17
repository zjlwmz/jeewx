/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package ${packageName}.${moduleName}.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.emay.framework.core.common.service.impl.CommonServiceImpl;
import ${packageName}.${moduleName}.dao.${ClassName}Dao;
import ${packageName}.${moduleName}.entity.${ClassName};
import ${packageName}.${moduleName}.service.${ClassName}Service;


/**
 * ${functionName}Service
 * @author ${classAuthor}
 * @version ${classVersion}
 */
@Service
@Transactional(readOnly = true)
public class ${ClassName}ServiceImpl extends CommonServiceImpl implements ${ClassName}Service{

	@Autowired
	private ${ClassName}Dao ${className}Dao;
	
	public ${ClassName} get(String id) {
		return ${className}Dao.get(${ClassName}.class, id);
	}
	
	@Transactional(readOnly = false)
	public void save(${ClassName} ${className}) {
		${className}Dao.saveOrUpdate(${className});
	}
	
	@Transactional(readOnly = false)
	public void delete(String id) {
		${className}Dao.deleteEntityById(${ClassName}.class, id);
	}
	
}
