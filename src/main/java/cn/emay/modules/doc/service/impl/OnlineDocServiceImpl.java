package cn.emay.modules.doc.service.impl;
import java.io.Serializable;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.emay.framework.core.common.service.impl.CommonServiceImpl;
import cn.emay.modules.doc.entity.OnlineDocEntity;
import cn.emay.modules.doc.service.OnlineDocService;

@Service("onlineDocService")
@Transactional
public class OnlineDocServiceImpl extends CommonServiceImpl implements OnlineDocService {

	
 	public <T> void delete(T entity) {
 		super.delete(entity);
 		//执行删除操作配置的sql增强
		this.doDelSql((OnlineDocEntity)entity);
 	}
 	
 	public <T> Serializable save(T entity) {
 		Serializable t = super.save(entity);
 		//执行新增操作配置的sql增强
 		this.doAddSql((OnlineDocEntity)entity);
 		return t;
 	}
 	
 	public <T> void saveOrUpdate(T entity) {
 		super.saveOrUpdate(entity);
 		//执行更新操作配置的sql增强
 		this.doUpdateSql((OnlineDocEntity)entity);
 	}
 	
 	/**
	 * 默认按钮-sql增强-新增操作
	 * @param id
	 * @return
	 */
 	public boolean doAddSql(OnlineDocEntity t){
	 	return true;
 	}
 	/**
	 * 默认按钮-sql增强-更新操作
	 * @param id
	 * @return
	 */
 	public boolean doUpdateSql(OnlineDocEntity t){
	 	return true;
 	}
 	/**
	 * 默认按钮-sql增强-删除操作
	 * @param id
	 * @return
	 */
 	public boolean doDelSql(OnlineDocEntity t){
	 	return true;
 	}
 	
 	/**
	 * 替换sql中的变量
	 * @param sql
	 * @return
	 */
 	public String replaceVal(String sql,OnlineDocEntity t){
 		sql  = sql.replace("#{id}",String.valueOf(t.getId()));
 		sql  = sql.replace("#{create_name}",String.valueOf(t.getCreateName()));
 		sql  = sql.replace("#{create_by}",String.valueOf(t.getCreateBy()));
 		sql  = sql.replace("#{create_date}",String.valueOf(t.getCreateDate()));
 		sql  = sql.replace("#{update_name}",String.valueOf(t.getUpdateName()));
 		sql  = sql.replace("#{update_by}",String.valueOf(t.getUpdateBy()));
 		sql  = sql.replace("#{update_date}",String.valueOf(t.getUpdateDate()));
 		sql  = sql.replace("#{sys_org_code}",String.valueOf(t.getSysOrgCode()));
 		sql  = sql.replace("#{sys_company_code}",String.valueOf(t.getSysCompanyCode()));
 		sql  = sql.replace("#{bpm_status}",String.valueOf(t.getBpmStatus()));
 		sql  = sql.replace("#{old_name}",String.valueOf(t.getOldName()));
 		sql  = sql.replace("#{new_name}",String.valueOf(t.getNewName()));
 		sql  = sql.replace("#{description}",String.valueOf(t.getDescription()));
 		sql  = sql.replace("#{treeNode}",String.valueOf(t.getTreeNode()));
 		sql  = sql.replace("#{path}",String.valueOf(t.getPath()));
 		sql  = sql.replace("#{UUID}",UUID.randomUUID().toString());
 		return sql;
 	}
}