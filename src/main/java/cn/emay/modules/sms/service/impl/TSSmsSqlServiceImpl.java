package cn.emay.modules.sms.service.impl;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.emay.framework.core.common.service.impl.CommonServiceImpl;
import cn.emay.modules.sms.entity.TSSmsSqlEntity;
import cn.emay.modules.sms.service.TSSmsSqlServiceI;

@Service("tSSmsSqlService")
@Transactional
public class TSSmsSqlServiceImpl extends CommonServiceImpl implements TSSmsSqlServiceI {

	@Resource
	private JdbcTemplate jdbcTemplate;
	@Override
	public boolean doAddSql(TSSmsSqlEntity t) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean doDelSql(TSSmsSqlEntity t) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean doUpdateSql(TSSmsSqlEntity t) {
		// TODO Auto-generated method stub
		return false;
	}
/**
 * 执行业务查询出来的sql
 */
	public Map<String, Object> getMap(String sql,Map<String, Object> map){
		return this.jdbcTemplate.queryForMap(sql, map);
		
	}
 	
}