/*************************************************************************
* CLASS 명  	: CodeUtil
* 작 업 자  	: 박재현
* 작 업 일  	: 2009.07.15
* 기    능  	: CodeUtilDao
* ---------------------------- 변 경 이 력 --------------------------------
* 번호  작 업 자     작     업     일        변 경 내 용                 비고
* ----  --------  -----------------  -------------------------    --------
*   1    박재현		 2009.07.15			  최 초 작 업 
**************************************************************************/
package com.lexken.framework.codeUtil;


import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ibatis.sqlmap.client.SqlMapClient;
import com.lexken.framework.common.SearchMap;


public class CodeUtilDao {
	
	// log4j 설정
	private final Log logger = LogFactory.getLog(getClass());	
	
	private static CodeUtilDao instance = null;
	
	public static CodeUtilDao getInstance() {
		if (instance == null) {
			instance = new CodeUtilDao();
		}
		return instance;
	}

	/**
	 * 코드그룹목록
	 * @param sqlMap
	 * @return List  
	 * @throws SQLException  
	*/
	public ArrayList<CodeUtilVO> getCodeGrpList(SqlMapClient sqlMap) throws SQLException {
		ArrayList<CodeUtilVO> dataList = null;
		dataList  = (ArrayList<CodeUtilVO>)sqlMap.queryForList("CodeUtil.getCodeGrpList");
		return dataList;
	}
	
	
	/**
	 * 코드목록
	 * @param sqlMap
	 * @return List  
	 * @throws SQLException  
	*/
	public ArrayList<CodeUtilVO> getCodeList(SqlMapClient sqlMap) throws SQLException {
		ArrayList<CodeUtilVO> dataList = null;
		dataList  = (ArrayList<CodeUtilVO>)sqlMap.queryForList("CodeUtil.getCodeList");
		return dataList;
	}
	
	
	
}
