package cn.emay;

import java.text.MessageFormat;

public class FormatTest {

	public static final String MYSQL_SQL = "select * from ( {0}) sel_tab00 limit {1},{2}";         //mysql
	
	public static void main(String[] args) {
		String sql="SELECT * FROM  sys_notice t WHERE t.notice_level = '1'  OR (t.notice_level = '2' AND EXISTS (SELECT 1 FROM sys_notice_authority_role r,sys_role_user ru WHERE r.role_id = ru.roleid AND t.id = r.notice_id AND ru.userid = '8a8ab0b246dc81120146dc8181950052')) OR (t.notice_level = '3' AND EXISTS (SELECT 1 FROM sys_notice_authority_user u WHERE t.id = u.notice_id AND u.user_id = '8a8ab0b246dc81120146dc8181950052')) ORDER BY t.create_time DESC";
		String beginNum="0";
		String rows="1000";
		sql =MessageFormat.format(MYSQL_SQL, sql,beginNum,rows);
		System.out.println("sql"+sql);
	}
}
