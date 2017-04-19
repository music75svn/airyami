/*************************************************************************
* CLASS 명  	: CalTypeAction
* 작 업 자  	: 정철수
* 작 업 일  	: 2012년 5월 29일 
* 기    능  	: KPI득점산식 관리
* ---------------------------- 변 경 이 력 --------------------------------
* 번호  작 업 자     작     업     일        변 경 내 용                 비고
* ----  --------  -----------------  -------------------------    --------
*   1    정철수      2012년 5월 29일  		  최 초 작 업 
**************************************************************************/
package com.lexken.bsc.base;

import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lexken.framework.common.ErrorMessages;
import com.lexken.framework.common.SearchMap;
import com.lexken.framework.common.ValidationChk;
import com.lexken.framework.core.CommonService;

public class CalTypeAction extends CommonService {
	
	private static final long serialVersionUID = 1L;
	
	// Logger
	private final Log logger = LogFactory.getLog(getClass());
	
	/**
	 * KPI득점산식 조회
	 * @param      
	 * @return String  
	 * @throws 
	 */
	public SearchMap calTypeList(SearchMap searchMap) {

		return searchMap;
	}
	
	
	/**
	 * KPI득점산식 데이터 조회(xml)
	 * @param      
	 * @return String  
	 * @throws 
	 */
	public SearchMap calTypeList_xml(SearchMap searchMap) {
		
		searchMap.addList("list", getList("bsc.base.calType.getList", searchMap));
		
		return searchMap;
	}
	
	/**
	 * KPI득점산식 상세화면
	 * @param      
	 * @return String  
	 * @throws 
	 */
	public SearchMap calTypeModify(SearchMap searchMap) {
		String stMode = searchMap.getString("mode");
        
    	if("MOD".equals(stMode)) {
    		searchMap.addList("detail", getDetail("bsc.base.calType.getDetail", searchMap));
    	}
    	
		return searchMap;
	}
	
	/**
	 * KPI득점산식 등록/수정/삭제
	 * @param      
	 * @return String  
	 * @throws 
	 */
	public SearchMap calTypeProcess(SearchMap searchMap) {
		HashMap returnMap = new HashMap();
		String stMode = searchMap.getString("mode");
		
		/**********************************
		 * 무결성 체크
		 **********************************/
		if(!"DEL".equals(stMode)) {
			returnMap = this.validChk(searchMap);
			
			if((Integer)returnMap.get("ErrorNumber") < 0 ){
				searchMap.addList("returnMap", returnMap);
				return searchMap;
			}
		}
		
		/**********************************
		 * 등록/수정/삭제
		 **********************************/
		if("ADD".equals(stMode)) {
			searchMap = insertDB(searchMap);
		} else if("MOD".equals(stMode)) {
			searchMap = updateDB(searchMap);
		} else if("DEL".equals(stMode)) {
			searchMap = deleteDB(searchMap);
		}
		
		/**********************************
		 * Return
		 **********************************/
		return searchMap;
	}
	
	/**
	 * KPI득점산식 등록
	 * @param      
	 * @return String  
	 * @throws 
	 */
	public SearchMap insertDB(SearchMap searchMap) {
		HashMap returnMap	= new HashMap();	
		
		try {
			setStartTransaction();
			
			returnMap = insertData("bsc.base.calType.insertData", searchMap);
			
		} catch (Exception e) {
        	logger.error(e.toString());
        	setRollBackTransaction();
        	returnMap.put("ErrorNumber", ErrorMessages.FAILURE_PROCESS_CODE);
			returnMap.put("ErrorMessage", ErrorMessages.FAILURE_PROCESS_MESSAGE);
        } finally {
        	setEndTransaction();
        }
		
		/**********************************
		 * Return
		 **********************************/
		searchMap.addList("returnMap", returnMap);
		return searchMap;	
	}
	
	/**
	 * KPI득점산식 수정
	 * @param      
	 * @return String  
	 * @throws 
	 */
	public SearchMap updateDB(SearchMap searchMap) {
		HashMap returnMap	= new HashMap();	
		
		try {
			setStartTransaction();
			
			returnMap = updateData("bsc.base.calType.updateData", searchMap);
		
		} catch (Exception e) {
        	logger.error(e.toString());
        	setRollBackTransaction();
        	returnMap.put("ErrorNumber", ErrorMessages.FAILURE_PROCESS_CODE);
			returnMap.put("ErrorMessage", ErrorMessages.FAILURE_PROCESS_MESSAGE);
        } finally {
        	setEndTransaction();
        }
		
		/**********************************
		 * Return
		 **********************************/
		searchMap.addList("returnMap", returnMap);
		return searchMap;	
	}
	
	/**
	 * KPI득점산식 삭제 
	 * @param      
	 * @return String  
	 * @throws 
	 */
	public SearchMap deleteDB(SearchMap searchMap) {
		
		HashMap returnMap	= new HashMap();	
		String calTypeIds = searchMap.getString("calTypeIds");
	    String[] keyArray = calTypeIds.split("\\|", 0);
 
	    try {
		    setStartTransaction();
		    
		    for (int i=0; i<keyArray.length; i++) {
		    	searchMap.put("calTypeId", keyArray[i]);
		    	returnMap = updateData("bsc.base.calType.deleteData", searchMap);
		    }
			
	    } catch (Exception e) {
        	logger.error(e.toString());
        	setRollBackTransaction();
        	returnMap.put("ErrorNumber", ErrorMessages.FAILURE_PROCESS_CODE);
			returnMap.put("ErrorMessage", ErrorMessages.FAILURE_PROCESS_MESSAGE);
        } finally {
        	setEndTransaction();
        }
	    
		/**********************************
		 * Return
		 **********************************/
		searchMap.addList("returnMap", returnMap);
		return searchMap;	
	}
	
    /**
     * 지표 매핑 개수 팝업 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap popMetricCntList(SearchMap searchMap) {
    	
        return searchMap;
    }
    
    /**
     * 지표 매핑 개수 팝업 데이터 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap popMetricCntList_xml(SearchMap searchMap) {
    	
    	searchMap.addList("list", getList("bsc.base.calType.getMetricCntList", searchMap));

        return searchMap;
    }

	/**
	 *  Validation 체크(무결성 체크)
	 * @param SearchMap
	 * @return HashMap   
	 */
	private HashMap validChk(SearchMap searchMap) {
		HashMap returnMap 		= new HashMap();
		int 	resultValue		= 0;
		
		returnMap = ValidationChk.lengthCheck(searchMap.getString("calTypeNm"), "산식패턴", 1, 300);
		if((Integer)returnMap.get("ErrorNumber") < 0 ){
			return returnMap;
		}
		
		returnMap = ValidationChk.lengthCheck(searchMap.getString("calType"), "산식패턴계산식", 1, 500);
		if((Integer)returnMap.get("ErrorNumber") < 0 ){
			return returnMap;
		}
		
		returnMap = ValidationChk.lengthCheck(searchMap.getString("content"), "비고", 0, 1000);
		if((Integer)returnMap.get("ErrorNumber") < 0 ){
			return returnMap;
		}
		
		returnMap.put("ErrorNumber",  resultValue);
		return returnMap;		
	}

	
}
