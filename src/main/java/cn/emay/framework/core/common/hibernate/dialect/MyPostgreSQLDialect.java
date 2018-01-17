package cn.emay.framework.core.common.hibernate.dialect;

import org.hibernate.dialect.PostgreSQL82Dialect;

public class MyPostgreSQLDialect extends PostgreSQL82Dialect {

	
	public boolean useInputStreamToInsertBlob() {
		return true;
	}

}
