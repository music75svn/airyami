/*************************************************************************
* CLASS 명  	: FileDownloadDao
* 작 업 자  	: 하윤식
* 작 업 일  	: 2012.08.31
* 기    능  	: 파일다운로드
* ---------------------------- 변 경 이 력 --------------------------------
* 번호  작 업 자     작   업   일         변 경 내 용                 비고
* ----  --------  -----------------  -------------------------    --------
*  1    하윤식		 2012.08.31  		  파일다운로드
**************************************************************************/
package com.lexken.bsc.module;


import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ibatis.sqlmap.client.SqlMapClient;
import com.lexken.framework.common.SearchMap;


public class FileDownloadDao {
	
	// log4j 설정
	private final Log logger = LogFactory.getLog(getClass());	
	
	private static FileDownloadDao instance = null;
	
	public static FileDownloadDao getInstance() {
		if (instance == null) {
			instance = new FileDownloadDao();
		}
		return instance;
	}

	/**
	 * 파일 다운로드 path 경로 조회
	 * @param searchMap
	 * @return LoginVO    
	*/
	public String getFiledownloadPath(SearchMap searchMap, SqlMapClient sqlMap)  throws SQLException {
		String     data = "";
		data = (String)sqlMap.queryForObject("FileDownload.getFiledownloadPath", searchMap);
        
        return data;
	}
	
	 
	
	
}
