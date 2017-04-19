/*************************************************************************
* CLASS 명  	: CommonDao
* 작 업 자  	: 하윤식
* 작 업 일  	: 2012년 3월 25일 
* 기    능  	: 코드관리
* ---------------------------- 변 경 이 력 --------------------------------
* 번호  작 업 자     작     업     일        변 경 내 용                 비고
* ----  --------  -----------------  -------------------------    --------
*   1    ha       2012년 3월 25일 		  최 초 작 업 
**************************************************************************/
package com.lexken.framework.core;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ibatis.sqlmap.client.SqlMapClient;
import com.lexken.framework.common.Paging;
//import com.lexken.framework.common.SearchMap;
import com.lexken.framework.common.SearchMap;

public class CommonDao {

	// Logger 
	private final Log logger = LogFactory.getLog(getClass());
	
	/**
	 * 목록조회 
	 * @param SearchMap, SqlMapClient
	 * @return Paging      
	 * @throws SQLException   
	 */
	public List getList(String mapStmtName, SearchMap searchMap, SqlMapClient sqlMap) throws SQLException {
		
		/**********************************
		 * List조회
		 **********************************/
		List list  = (List)sqlMap.queryForList(mapStmtName, searchMap);	
		
		return list;
	}	
	
	/**
	 * 건수조회 
	 * @param SearchMap, SqlMapClient
	 * @return Paging      
	 * @throws SQLException   
	 */
	public int getInt(String mapStmtName, SearchMap searchMap, SqlMapClient sqlMap) throws SQLException {

		/**********************************
         * 선언
         **********************************/
        int data = 0;
		
		/**********************************
		 * Total Count 조회
		 **********************************/
        data = ((Integer)sqlMap.queryForObject(mapStmtName, searchMap)).intValue();
		
		return data;
	}	
	
	/**
	 * 문자열조회 
	 * @param SearchMap, SqlMapClient
	 * @return Paging      
	 * @throws SQLException   
	 */
	public String getStr(String mapStmtName, SearchMap searchMap, SqlMapClient sqlMap) throws SQLException {

		/**********************************
         * 선언
         **********************************/
        String data = "";
		
		/**********************************
		 * 문자열 조회
		 **********************************/
        data = ((String)sqlMap.queryForObject(mapStmtName, searchMap));
		
		return data;
	}	
	
	/**
	 * 목록조회 (페이징)
	 * @param SearchMap, SqlMapClient
	 * @return Paging      
	 * @throws SQLException   
	 */
	public Paging getPageList(String mapStmtName, SearchMap searchMap, SqlMapClient sqlMap) throws SQLException {
		
	    /**********************************
         * 선언
         **********************************/
        int     totalRow = 0;
		
		/**********************************
		 * Total Count 조회
		 **********************************/
		totalRow = ((Integer)sqlMap.queryForObject(mapStmtName+"Count", searchMap)).intValue();
		
		/**********************************
		 * Page객체 생성
		 **********************************/		
		Paging pg = new Paging(totalRow, searchMap.getInt("beginRow"), searchMap.getInt("rowPerPage"));
		searchMap.put("pg", pg);

		/**********************************
		 * List조회
		 **********************************/
		List list  = (List)sqlMap.queryForList(mapStmtName, searchMap);	
		pg.setVoList(list);
		
		return pg;
	}	
	
	
	/**
	 * 상세조회
	 * @param SearchMap, SqlMapClient
	 * @return HashMap  
	 * @throws SQLException  
	 */
	public HashMap getDetail(String mapStmtName, SearchMap searchMap, SqlMapClient sqlMap) throws SQLException {
		/**********************************
		 * 정의
		 **********************************/
		HashMap detailInfoMap = new HashMap();
		
		/**********************************
		 * 관리자 상세
		 **********************************/
		detailInfoMap  = (HashMap)sqlMap.queryForObject(mapStmtName, searchMap);
		
		return detailInfoMap;
	}
	
	
	/**
	 * 데이터 등록
	 * @param SearchMap, SqlMapClient
	 * @return int  
	 * @throws SQLException    
	 */
	public int insertData(String mapStmtName, SearchMap searchMap, SqlMapClient sqlMap) throws SQLException {
		Object key = sqlMap.insert(mapStmtName, searchMap);
		return 1;
	}	
	
	
	/**
	 * 데이터 수정
	 * @param SearchMap, SqlMapClient
	 * @return int  
	 * @throws SQLException  
	 */
	public int updateData(String mapStmtName, SearchMap searchMap, SqlMapClient sqlMap) throws SQLException {
		int rows = (Integer) sqlMap.update(mapStmtName, searchMap);
		return rows;
	}	

	
	/**
	 * 데이터 삭제
	 * @param SearchMap, SqlMapClient
	 * @return int  
	 * @throws SQLException  
	 */
	public int deleteData(String mapStmtName, SearchMap searchMap, SqlMapClient sqlMap) throws SQLException {
		int rows = (Integer) sqlMap.delete(mapStmtName, searchMap);
		return rows;
	}	
	
}
