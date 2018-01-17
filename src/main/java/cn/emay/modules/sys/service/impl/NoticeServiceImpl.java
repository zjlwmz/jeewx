package cn.emay.modules.sys.service.impl;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import org.springframework.stereotype.Service;

import cn.emay.framework.core.common.service.impl.CommonServiceImpl;
import cn.emay.modules.sys.entity.Notice;
import cn.emay.modules.sys.entity.TSNoticeAuthorityRole;
import cn.emay.modules.sys.entity.TSNoticeAuthorityUser;
import cn.emay.modules.sys.entity.TSNoticeReadUser;
import cn.emay.modules.sys.entity.User;
import cn.emay.modules.sys.service.NoticeService;

/**
 * 通知公告Service接口实现类
 * @author  alax
 *
 */
@Service("noticeService")
public class NoticeServiceImpl extends CommonServiceImpl implements NoticeService{
	
	/**
	 * 
	 * @param noticeTilte 标题
	 * @param noticeContent 内容
	 * @param noticeType 类型
	 * @param noticeLevel 级别
	 * @param noticeTerm 期限
	 * @param createUser 创建者
	 * @return
	 */
	public String addNotice(String noticeTitle, String noticeContent,
			String noticeType, String noticeLevel, Date noticeTerm,
			String createUser) {
		String noticeId=null;
		Notice notice = new Notice();
		notice.setNoticeTitle(noticeTitle);
		notice.setNoticeContent(noticeContent);
		notice.setNoticeType(noticeType);
		notice.setNoticeLevel(noticeLevel);
		notice.setNoticeTerm(noticeTerm);
		notice.setCreateUser(createUser);
		notice.setCreateTime(new Date());
		this.save(notice);
		noticeId = notice.getId();
		return noticeId;
	}

    /**
     * 追加通告授权用户
     */
	public void addNoticeAuthorityUser(String noticeId, String userid) {
		if(noticeId != null && userid!=null){
			TSNoticeAuthorityUser entity = new  TSNoticeAuthorityUser();
			entity.setNoticeId(noticeId);
			User user = new User();
			user.setId(userid);
			entity.setUser(user);
			this.saveOrUpdate(entity);
		}
	}
	
	public <T> void delete(T entity) {

		Notice notice = (Notice)entity;
		super.deleteAllEntitie(super.findByProperty(TSNoticeReadUser.class, "noticeId", notice.getId()));
		super.deleteAllEntitie(super.findByProperty(TSNoticeAuthorityUser.class, "noticeId", notice.getId()));
		super.deleteAllEntitie(super.findByProperty(TSNoticeAuthorityRole.class, "noticeId", notice.getId()));
		super.delete(notice);
 		//执行删除操作配置的sql增强
		this.doDelSql(notice);

 	}
 	
 	public <T> Serializable save(T entity) {
 		Serializable t = super.save(entity);
 		//执行新增操作配置的sql增强
 		this.doAddSql((Notice)entity);
 		return t;
 	}
 	
 	public <T> void saveOrUpdate(T entity) {
 		super.saveOrUpdate(entity);
 		//执行更新操作配置的sql增强
 		this.doUpdateSql((Notice)entity);
 	}
 	
 	/**
	 * 默认按钮-sql增强-新增操作
	 * @param id
	 * @return
	 */
 	public boolean doAddSql(Notice t){
	 	return true;
 	}
 	/**
	 * 默认按钮-sql增强-更新操作
	 * @param id
	 * @return
	 */
 	public boolean doUpdateSql(Notice t){
	 	return true;
 	}
 	/**
	 * 默认按钮-sql增强-删除操作
	 * @param id
	 * @return
	 */
 	public boolean doDelSql(Notice t){
	 	return true;
 	}
 	
 	/**
	 * 替换sql中的变量
	 * @param sql
	 * @return
	 */
 	public String replaceVal(String sql,Notice t){
 		sql  = sql.replace("#{id}",String.valueOf(t.getId()));
 		sql  = sql.replace("#{notice_title}",String.valueOf(t.getNoticeTitle()));
 		sql  = sql.replace("#{notice_content}",String.valueOf(t.getNoticeContent()));
 		sql  = sql.replace("#{notice_type}",String.valueOf(t.getNoticeType()));
 		sql  = sql.replace("#{notice_level}",String.valueOf(t.getNoticeLevel()));
 		sql  = sql.replace("#{notice_term}",String.valueOf(t.getNoticeTerm()));
 		sql  = sql.replace("#{create_user}",String.valueOf(t.getCreateUser()));
 		sql  = sql.replace("#{create_time}",String.valueOf(t.getCreateTime()));
 		sql  = sql.replace("#{UUID}",UUID.randomUUID().toString());
 		return sql;
 	}
}
