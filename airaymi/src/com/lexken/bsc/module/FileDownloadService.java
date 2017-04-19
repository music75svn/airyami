/*************************************************************************
* CLASS 명  	: FileDownloadService
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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ibatis.sqlmap.client.SqlMapClient;
import com.lexken.framework.common.MyAppSqlConfig;
import com.lexken.framework.common.SearchMap;

public class FileDownloadService {

	// Logger
	private final Log logger = LogFactory.getLog(getClass());
	
	// CommonModuleDao
	private FileDownloadDao fileDownloadDao = FileDownloadDao.getInstance();

	// Instance
	private static FileDownloadService instance = null;
	public static FileDownloadService getInstance() {
		if (instance == null) {
			instance = new FileDownloadService();
		}
		return instance;
	}
	
	/**
	 * 파일 다운로드 path 경로 조회
	 * @param searchMap
	 * @return String    
	*/
	public String getFiledownloadPath(SearchMap searchMap) {
		/**********************************
		 * 정의
		 **********************************/
		String data = "";
		
		/**********************************
		 * sqlMap생성
		 **********************************/
		SqlMapClient sqlMap = MyAppSqlConfig.getSqlMapInstance();

		try {
			data = (String)fileDownloadDao.getFiledownloadPath(searchMap, sqlMap);
			logger.debug("intDataCnt : " + data); 
			
		} catch (SQLException e) {
			e.printStackTrace();
		} 	
		
		return data;		
	}
 

}
