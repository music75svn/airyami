package com.lexken.framework.common;

import java.io.Reader;

import com.ibatis.common.resources.Resources;
import com.ibatis.sqlmap.client.SqlMapClient;
import com.ibatis.sqlmap.client.SqlMapClientBuilder;

public class MyAppSmsSqlConfig {
	private static final SqlMapClient sqlMap;

	static {
		try {
			String resource = "sqlmap-sms-config.xml";
			Reader configReader = Resources.getResourceAsReader(resource);
			sqlMap = SqlMapClientBuilder.buildSqlMapClient(configReader);
		} catch(Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Error initializing MyAppSmsSqlConfig class. Cause : " + e);
		}
	}

	public static SqlMapClient getSqlMapInstance() {
		return sqlMap;
	}

}

