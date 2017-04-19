/*************************************************************************
* CLASS 명  	: CommonService
* 작 업 자  	: 하윤식
* 작 업 일  	: 2012년 3월 25일 
* 기    능  	: 공통 Service
* ---------------------------- 변 경 이 력 --------------------------------
* 번호  작 업 자     작     업     일        변 경 내 용                 비고
* ----  --------  -----------------  -------------------------    --------
*   1    하윤식       2012년 5월 15일 		  최 초 작 업 
**************************************************************************/
package com.lexken.framework.core;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpSession;

import com.lexken.framework.common.MsgDic;
import com.lexken.framework.common.MyAppSqlConfig;
import com.lexken.framework.common.Paging;
import com.lexken.framework.common.SearchMap;

import com.lexken.framework.common.ErrorMessages;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ibatis.sqlmap.client.SqlMapClient;

public class CommonService {

	// Logger
	private final Log logger = LogFactory.getLog(getClass());
	
	// CommonDao
	private CommonDao commonDao = new CommonDao();
	
	// SqlMap생성
    SqlMapClient sqlMap = MyAppSqlConfig.getSqlMapInstance();
    
    boolean isOk = true;

	/**
	 * 목록조회 
	 * @param SearchMap
	 * @return Paging   
	 */
	public List getList(String mapStmtName, SearchMap searchMap) {
    
        /**********************************
         * List 조회
         **********************************/
        List list = null;  
        try {
        	list = (List)commonDao.getList(mapStmtName, searchMap, sqlMap);
        } catch (SQLException e) {
            logger.error(e.toString());
            e.printStackTrace();
        }   
        return list;      
    }
	
	/**
	 * 문자열조회 
	 * @param SearchMap
	 * @return Paging   
	 */
	public String getStr(String mapStmtName, SearchMap searchMap) {
		
        /**********************************
         * List 조회
         **********************************/
        String data = "";
        try {
        	data = (String)commonDao.getStr(mapStmtName, searchMap, sqlMap);
        } catch (SQLException e) {
            logger.error(e.toString());
            e.printStackTrace();
        }   
        return data;      
    }
	
	/**
	 * 쿼리실행 성공여부
	 * @param SearchMap
	 * @return 성공 : T, 실패 : F 반환   
	 */
	public String getQuerySuccess(String mapStmtName, SearchMap searchMap) {
		
        /**********************************
         * List 조회
         **********************************/
        String data = "";
        String isOk = "F";
        
        try {
        	data = (String)commonDao.getStr(mapStmtName, searchMap, sqlMap);
        	isOk = "T";
        } catch (SQLException e) {
            logger.error(e.toString());
            e.printStackTrace();
            isOk = "F";
        }   
        return isOk;
    }
	
	/**
	 * 건수조회 
	 * @param SearchMap
	 * @return Paging   
	 */
	public int getInt(String mapStmtName, SearchMap searchMap) {
		
        /**********************************
         * List 조회
         **********************************/
        int data = 0;
        try {
        	data = (int)commonDao.getInt(mapStmtName, searchMap, sqlMap);
        } catch (SQLException e) {
            logger.error(e.toString());
            e.printStackTrace();
        }   
        return data;      
    }
	
	/**
	 * 목록조회 (페이징) 
	 * @param SearchMap
	 * @return Paging   
	 */
	public Paging getPageList(String mapStmtName, SearchMap searchMap) {
		
        /**********************************
         * List 조회
         **********************************/
        Paging paging = null;
        try {
            paging = (Paging)commonDao.getPageList(mapStmtName, searchMap, sqlMap);
        } catch (SQLException e) {
            logger.error(e.toString());
            e.printStackTrace();
        }   
        return paging;      
    }
	
	/**
     * 상세조회
     * @param SearchMap
     * @return HashMap   
     */
    public HashMap getDetail(String mapStmtName, SearchMap searchMap) {
    	
        /**********************************
         * 정의
         **********************************/
    	HashMap detailInfoMap = new HashMap();

        try {
        	detailInfoMap = commonDao.getDetail(mapStmtName, searchMap, sqlMap);
        } catch (SQLException e) {
            logger.error("SQL 트랜잭션 오류 : " + e.toString());
            e.printStackTrace();
            detailInfoMap = null;
        }
        
        return detailInfoMap;       
    }
    
    /**
	 * 등록 
	 * @param SearchMap
	 * @return HashMap  
	 */
	public HashMap insertData(String mapStmtName, SearchMap searchMap) {
		
		return insertData(mapStmtName, searchMap, false);
	}
	
	/**
	 * 등록2
	 * @param SearchMap
	 * @return HashMap  
	 */
	public HashMap insertData(String mapStmtName, SearchMap searchMap, boolean pass) {
		
		return insertData(mapStmtName, searchMap, pass, false);
	}
	
    /**
	 * 등록작업 
	 * pass 값 true시 이전실행된 dml문 체크하지 않음
	 * @param SearchMap
	 * @return HashMap  
	 */
	public HashMap insertData(String mapStmtName, SearchMap searchMap, boolean pass, boolean force) {
		HashMap returnMap = new HashMap();
		
		//이전 실행된 dml 성공여부 체크 
		if(!force) {
			if(!checkPreProcess(searchMap)) {
				returnMap.put("ErrorNumber", ErrorMessages.FAILURE_PROCESS_CODE);
				returnMap.put("ErrorMessage", ErrorMessages.FAILURE_PROCESS_MESSAGE);
				return returnMap;
			}
		}
				
		int 	resultValue		= 0;
		int 	intDataCnt 		= 0;
		int     chkVal          = 1;

		try {
			resultValue = commonDao.insertData(mapStmtName, searchMap, sqlMap);
			
			if(0 < resultValue) {
				returnMap.put("ErrorNumber", 1);
				returnMap.put("ErrorMessage", ErrorMessages.SUCCESS_SAVE_MESSAGE);
			} else if(0 == resultValue && pass){
				returnMap.put("ErrorNumber", 1);
				returnMap.put("ErrorMessage", ErrorMessages.SUCCESS_EDIT_MESSAGE);
			} else {
				returnMap.put("ErrorNumber", ErrorMessages.FAILURE_PROCESS_CODE);
				returnMap.put("ErrorMessage", ErrorMessages.FAILURE_PROCESS_MESSAGE);
				chkVal = -1;
			}

		} catch (SQLException e) {
			logger.error("SQL 오류 : " + e.toString());
			isOk = false;
			returnMap.put("ErrorNumber", -1);
			returnMap.put("ErrorMessage", ErrorMessages.FAILURE_PROCESS_MESSAGE);
			chkVal = -1;
			
			/**********************************
			 * 메시지 처리
			 **********************************/
	        MsgDic.getErrorMsg(returnMap ,e.getErrorCode() ,e.toString() );
		} 
		
		searchMap.put("chkVal", new Integer(chkVal));
		return returnMap;		
	}
	
	/**
	 * 수정 
	 * @param SearchMap
	 * @return HashMap  
	 */
	public HashMap updateData(String mapStmtName, SearchMap searchMap) {
		
		return updateData(mapStmtName, searchMap, false);
	}
	
	/**
	 * 수정2
	 * @param SearchMap
	 * @return HashMap  
	 */
	public HashMap updateData(String mapStmtName, SearchMap searchMap, boolean pass) {
		
		return updateData(mapStmtName, searchMap, pass, false);
	}
	
	/**
	 * 수정
	 * pass 값 true 시 0 row updated 시에도 success 반환  
	 * @param SearchMap
	 * @return HashMap  
	 */
	public HashMap updateData(String mapStmtName, SearchMap searchMap, boolean pass, boolean force) {
		HashMap returnMap 		= new HashMap();
		
		//이전 실행된 sql문 성공여부 체크
		if(!force) {
			if(!checkPreProcess(searchMap)) {
				returnMap.put("ErrorNumber", ErrorMessages.FAILURE_PROCESS_CODE);
				returnMap.put("ErrorMessage", ErrorMessages.FAILURE_PROCESS_MESSAGE);
				return returnMap;
			}
		}
		
		int 	resultValue		= 0;
		int     chkVal          = 1;

		try {
			resultValue = commonDao.updateData(mapStmtName, searchMap, sqlMap);
			
			if(0 < resultValue) {
				returnMap.put("ErrorNumber", 1);
				returnMap.put("ErrorMessage", ErrorMessages.SUCCESS_EDIT_MESSAGE);
			} else if(0 == resultValue && pass){
				returnMap.put("ErrorNumber", 1);
				returnMap.put("ErrorMessage", ErrorMessages.SUCCESS_EDIT_MESSAGE);
			} else {
				returnMap.put("ErrorNumber", ErrorMessages.FAILURE_PROCESS_CODE);
				returnMap.put("ErrorMessage", ErrorMessages.FAILURE_PROCESS_MESSAGE);
				chkVal = -1;
			}

		} catch (SQLException e) {
			logger.error("SQL 오류 : " + e.toString());
			isOk = false;
			returnMap.put("ErrorNumber", -1);
			returnMap.put("ErrorMessage", ErrorMessages.FAILURE_PROCESS_MESSAGE);
			chkVal = -1;

			/**********************************
			 * 메시지 처리
			 **********************************/
	        MsgDic.getErrorMsg(returnMap ,e.getErrorCode() ,e.toString() );
		}
		
		searchMap.put("chkVal", new Integer(chkVal));
		return returnMap;
	}
	
	/**
	 * 삭제
	 * @param SearchMap
	 * @return HashMap  
	 */
	public HashMap deleteData(String mapStmtName, SearchMap searchMap) {
		
		return deleteData(mapStmtName, searchMap, false);
	}
	
	/**
	 * 삭제2
	 * @param SearchMap
	 * @return HashMap  
	 */
	public HashMap deleteData(String mapStmtName, SearchMap searchMap, boolean pass) {
		
		return deleteData(mapStmtName, searchMap, pass, false);
	}
	
	/**
	 * 삭제
	 * pass 값 true 시 0 row deleted 시에도 success 반환
	 * @param SearchMap
	 * @return HashMap  
	 */
	public HashMap deleteData(String mapStmtName, SearchMap searchMap, boolean pass, boolean force) {
		HashMap returnMap 		= new HashMap();
		
		//이전 실행된 sql문 성공여부 체크 
		if(!force) {
			if(!checkPreProcess(searchMap)) {
				returnMap.put("ErrorNumber", ErrorMessages.FAILURE_PROCESS_CODE);
				returnMap.put("ErrorMessage", ErrorMessages.FAILURE_PROCESS_MESSAGE);
				return returnMap;
			}
		}
		
		int	resultValue	= 0;
		int chkVal      = 1;

		try {
			resultValue = commonDao.deleteData(mapStmtName, searchMap, sqlMap);
			
			if(0 < resultValue) {
				returnMap.put("ErrorNumber", 1);
				returnMap.put("ErrorMessage", ErrorMessages.SUCCESS_EDIT_MESSAGE);
			} else if(0 == resultValue && pass){
				returnMap.put("ErrorNumber", 1);
				returnMap.put("ErrorMessage", ErrorMessages.SUCCESS_EDIT_MESSAGE);
			} else {
				returnMap.put("ErrorNumber", ErrorMessages.FAILURE_PROCESS_CODE);
				returnMap.put("ErrorMessage", ErrorMessages.FAILURE_PROCESS_MESSAGE);
				chkVal = -1;
			}

		} catch (SQLException e) {
			logger.error("SQL 오류 : " + e.toString());
			isOk = false;
			returnMap.put("ErrorNumber", -1);
			returnMap.put("ErrorMessage", ErrorMessages.FAILURE_PROCESS_MESSAGE);
			chkVal = -1;
			
			/**********************************
			 * 메시지 처리
			 **********************************/
	        MsgDic.getErrorMsg(returnMap ,e.getErrorCode() ,e.toString() );
		}  
		
		searchMap.put("chkVal", new Integer(chkVal));
		return returnMap;		
	}
	 
	/**
	 * 트랜잭션 시작
	 * @param
	 * @return
	 */
	public void setStartTransaction() {
		try {
			sqlMap.startTransaction();
		} catch (SQLException e) {
			logger.error("SQL 오류 : " + e.toString());
		}
	}

	/**
	 * 트랜잭션 종료
	 * @param
	 * @return
	 */
	public void setEndTransaction() {
		try {
			if(isOk) {
				sqlMap.commitTransaction();
			} else {
				sqlMap.getCurrentConnection().rollback();
			}
		} catch (SQLException e) {
			logger.error("SQL 오류 : " + e.toString());
		} finally {
            try {
            	sqlMap.endTransaction();
            } catch (SQLException e) {
            	logger.error("SQL 오류 : " + e.toString());
            }
        }
	}
	
	/**
	 * 트랜잭션 커밋
	 * @param
	 * @return
	 */
	public void setCommitTransaction() {
		try {
			sqlMap.commitTransaction();
		} catch (SQLException e) {
			logger.error("SQL 오류 : " + e.toString());
		} finally {
            try {
            	sqlMap.endTransaction();
            } catch (SQLException e) {
            	logger.error("SQL 오류 : " + e.toString());
            }
        }
	}
	
	/**
	 * 트랜잭션 Rollback
	 * @param
	 * @return
	 */
	public void setRollBackTransaction() {
		try {
			 sqlMap.getCurrentConnection().rollback();
		} catch (SQLException e) {
			logger.error("SQL 오류 : " + e.toString());
		}
	}
	
	/**
	 * 이전 실행된 sql문 성공여부 체크
	 * @param SearchMap
	 * @return HashMap  
	 */
	public boolean checkPreProcess(SearchMap searchMap) {
		boolean isOk = true;
		Integer chkVal = (Integer)searchMap.get("chkVal");
		if(null != chkVal) {
			if(chkVal.intValue() < 0){
				logger.debug("===> 이전 실행된 sql문 오류");
				isOk = false;
			} 
		}
		return isOk;
	}
	
}
