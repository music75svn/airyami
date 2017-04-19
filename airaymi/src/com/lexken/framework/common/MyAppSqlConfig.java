package com.lexken.framework.common;

import java.io.Reader;
import java.nio.charset.Charset;

import com.ibatis.common.resources.Resources;
import com.ibatis.sqlmap.client.SqlMapClient;
import com.ibatis.sqlmap.client.SqlMapClientBuilder;

public class MyAppSqlConfig {
	//private static final SqlMapClient sqlMap;
	private static SqlMapClient sqlMap;
	static {
		try {
			
//			String resource = "sqlmap-config.xml";
//			Reader configReader = Resources.getResourceAsReader(resource);
			
			String resource = "sqlmap-config.xml";
			Charset charset = Charset.forName("UTF-8");
            Resources.setCharset(charset);
			Reader configReader = Resources.getResourceAsReader(resource);
			
			sqlMap = SqlMapClientBuilder.buildSqlMapClient(configReader);
			
		} catch(Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Error initializing MyAppSqlConfig class. Cause : " + e);
		}
	}

	public static SqlMapClient getSqlMapInstance() {
		//reload();
		return sqlMap;
	}
	
	public static void reload(){
		try {
//			String resource = "sqlmap-config.xml";
//			Reader configReader = Resources.getResourceAsReader(resource);

			String resource = "sqlmap-config.xml";
			Charset charset = Charset.forName("UTF-8");
            Resources.setCharset(charset);
			Reader configReader = Resources.getResourceAsReader(resource);
			
			sqlMap = SqlMapClientBuilder.buildSqlMapClient(configReader);
			
		} catch(Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Error initializing MyAppSqlConfig class. Cause : " + e);
		}
	}

}
