/*************************************************************************
* CLASS 명      : SystemAggAction
* 작 업 자      : 김윤기
* 작 업 일      : 2012년 7월 17일 
* 기    능      : 데이터집계관리
* ---------------------------- 변 경 이 력 --------------------------------
* 번호  작 업 자     작     업     일        변 경 내 용                 비고
* ----  --------  -----------------  -------------------------    --------
*   1    김윤기      2012년 7월 17일             최 초 작 업 
**************************************************************************/
package com.lexken.bsc.system;
    
import java.util.HashMap;

import com.lexken.framework.common.ErrorMessages;
import com.lexken.framework.common.Paging;
import com.lexken.framework.common.SearchMap;
import com.lexken.framework.common.ValidationChk;
import com.lexken.framework.struts2.IsBoxActionSupport;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lexken.framework.core.CommonService;
import com.lexken.framework.login.LoginManager;
import com.lexken.framework.util.StaticUtil;

public class SystemAggAction extends CommonService {

    private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * 데이터집계관리 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap systemAggList(SearchMap searchMap) {

        return searchMap;
    }
    
    /**
     * 데이터집계관리 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap systemAggList_xml(SearchMap searchMap) {
        
        searchMap.addList("list", getList("bsc.system.systemAgg.getList", searchMap));

        return searchMap;
    }
    
    /**
     * 데이터집계관리 프로시저 실행
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap systemAggProcess(SearchMap searchMap) {
        HashMap returnMap = new HashMap();
        String stMode = searchMap.getString("mode");
        
        /**********************************
         * 프로시저 실행
         **********************************/
        if("CALL".equals(stMode)) {
            searchMap = callDB(searchMap);
        }        
        
        /**********************************
         * Return
         **********************************/
        return searchMap;
    }
    
    /**
     * 데이터집계관리 프로시저 실행
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap callDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
        	setStartTransaction();
        	
        	if("SP_BSC_TOTAL_PROC_MONTH_KGS".equals(searchMap.get("procId"))){
        		returnMap = insertData("bsc.system.systemAgg.callSpBscTotalProc", searchMap);
        	}else if("SP_BSC_TOTAL_PROC_KGS_SHORTLY".equals(searchMap.get("procId"))){
        		returnMap = insertData("bsc.system.systemAgg.callSpBscTotalProcShortly", searchMap);
        	}else if("SP_APPROVE_TARGET_ALL".equals(searchMap.get("procId"))){
        		returnMap = insertData("bsc.system.systemAgg.callSpApproveTargetAll", searchMap);
        	}else if("SP_APPROVE_ACTUAL_ALL".equals(searchMap.get("procId"))){
        		returnMap = insertData("bsc.system.systemAgg.callSpApproveActualAll", searchMap);
        	}
        	else{
        		//..
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
