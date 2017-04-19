/*************************************************************************
* CLASS 명      : ClosingYearAction
* 작 업 자      : 정철수
* 작 업 일      : 2012년 6월 8일 
* 기    능      : 년마감관리
* ---------------------------- 변 경 이 력 --------------------------------
* 번호  작 업 자     작     업     일        변 경 내 용                 비고
* ----  --------  -----------------  -------------------------    --------
*   1    정철수      년마감관리            최 초 작 업 
**************************************************************************/
package com.lexken.bsc.system;
    
import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lexken.framework.common.ErrorMessages;
import com.lexken.framework.common.SearchMap;
import com.lexken.framework.core.CommonService;

public class ClosingYearAction extends CommonService {

    private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * 년마감관리 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap closingYearList(SearchMap searchMap) {

        return searchMap;
    }
    
    /**
     * 년마감관리 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap closingYearList_xml(SearchMap searchMap) {
        
        searchMap.addList("list", getList("bsc.system.closingYear.getList", searchMap));

        return searchMap;
    }
    
    /**
     * 년마감관리 등록/수정/삭제
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap closingYearProcess(SearchMap searchMap) {
        HashMap returnMap = new HashMap();
        String stMode = searchMap.getString("mode");
        
        /**********************************
         * 등록/수정/삭제
         **********************************/
        if("ADD".equals(stMode)) {
            searchMap = insertDB(searchMap);
        } else if("MOD".equals(stMode)) {
            searchMap = updateDB(searchMap);
        } 
        
        /**********************************
         * Return
         **********************************/
        return searchMap;
    }
    
    /**
     * 년마감관리 등록
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap insertDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
	        setStartTransaction();
	        
	        returnMap = insertData("bsc.system.closingYear.insertData", searchMap);
        
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
     * 년마감관리 수정
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap updateDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
	        setStartTransaction();
	        
	        int existCnt = getInt("bsc.system.closingYear.getListCnt", searchMap);
	        
	        if(existCnt > 0){
	        	returnMap = updateData("bsc.system.closingYear.updateData", searchMap);
	        } else {
	        	returnMap = insertData("bsc.system.closingYear.insertData", searchMap);
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
    
}
