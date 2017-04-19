/*************************************************************************
* CLASS 명      : MemJobEvalAction
* 작 업 자      : 유연주
* 작 업 일      : 2017년 03월 13일 
* 기    능      : 업무수행평가항목
* ---------------------------- 변 경 이 력 --------------------------------
* 번호   작 업 자      작   업   일        변 경 내 용              비고
* ----  ---------  -----------------  -------------------------    --------
*   1    유연주      2017년 03월 13일          최 초 작 업 
**************************************************************************/
package com.lexken.mem.base;
    
import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lexken.framework.common.ErrorMessages;
import com.lexken.framework.common.SearchMap;
import com.lexken.framework.common.ValidationChk;
import com.lexken.framework.core.CommonService;

public class MemJobEvalAction extends CommonService {

    private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * 업무수행평가항목 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap memJobEvalList(SearchMap searchMap) {
    	
    	return searchMap;
    }
    
    /**
     * 업무수행평가항목 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap memJobEvalList_xml(SearchMap searchMap) {
    	
        
        searchMap.addList("list", getList("mem.base.memJobEval.getList", searchMap));

        return searchMap;
    }
    
    /**
     * 업무수행평가항목 상세화면
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap memJobEvalModify(SearchMap searchMap) {
    	String stMode = searchMap.getString("mode");
    	if("MOD".equals(stMode)) {
    		searchMap.addList("detail", getDetail("mem.base.memJobEval.getDetail", searchMap));
    	}
    	
        return searchMap;
    }
    
    /**
     * 업무수행평가항목 등록/수정/삭제
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap memJobEvalProcess(SearchMap searchMap) {
        HashMap returnMap = new HashMap();
        String stMode = searchMap.getString("mode");
        
        /**********************************
         * 무결성 체크
         **********************************/
        if(!"DEL".equals(stMode) && !"SAVE".equals(stMode) && !"MODUSER".equals(stMode) ) {
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
        } else if("SAVE".equals(stMode)) {
            searchMap = allSaveDB(searchMap);
        }
        
        /**********************************
         * Return
         **********************************/
        return searchMap;
    }
    
    /**
     * 업무수행평가항목 등록
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap insertDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
        	setStartTransaction();
        
        	// 업무수행평가항목ID 생성
        	HashMap evalGrpIdMap = getDetail("mem.base.memJobEval.getJobEvalItemId", searchMap);
        	searchMap.put("jobEvalItemId", evalGrpIdMap.get("JOB_EVAL_ITEM_ID"));
        	
	        // 업무수행평가항목 등록
	        returnMap = insertData("mem.base.memJobEval.insertData", searchMap);
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
     * 업무수행평가항목 수정
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap updateDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
	        setStartTransaction();
	        
	        // 업무수행평가항목 수정
	        returnMap = updateData("mem.base.memJobEval.updateData", searchMap);
	        
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
     * 업무수행평가항목 정렬순서 일괄 저장 
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap allSaveDB(SearchMap searchMap) {
        
        HashMap returnMap = new HashMap(); 
	    
	    try {
			String[] jobEvalItemIds = searchMap.getString("jobEvalItemIds").split("\\|", -1);
			String[] sortOrders = searchMap.getString("sortOrders").split("\\|", -1);
	        
	        setStartTransaction();
	        
	        if(null != jobEvalItemIds && 0 < jobEvalItemIds.length) {
		        for (int i = 0; i < jobEvalItemIds.length - 1; i++) {
		            searchMap.put("sortOrder", sortOrders[i]);
		            logger.debug("sortOrders[i] : "+sortOrders[i]);
		            searchMap.put("jobEvalItemId", jobEvalItemIds[i]);
		            returnMap = updateData("mem.base.memJobEval.allSaveData", searchMap);
		        }
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
     * 업무수행평가항목 삭제 
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap deleteDB(SearchMap searchMap) {
        
        HashMap returnMap = new HashMap(); 
	    
	    try {
			String[] jobEvalItemIds = searchMap.getString("jobEvalItemIds").split("\\|", -1);
	        
	        setStartTransaction();
	        
	        if(null != jobEvalItemIds && 0 < jobEvalItemIds.length) {
		        for (int i = 0; i < jobEvalItemIds.length - 1; i++) {
		        	searchMap.put("jobEvalItemId", jobEvalItemIds[i]);
		            returnMap = updateData("mem.base.memJobEval.deleteData", searchMap);
		        }
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
     *  Validation 체크(무결성 체크)
     * @param SearchMap
     * @return HashMap
     */
    private HashMap validChk(SearchMap searchMap) {
        HashMap returnMap         = new HashMap();
        int     resultValue        = 0;
        String stMode = searchMap.getString("mode");
        
    	returnMap = ValidationChk.lengthCheck(searchMap.getString("jobEvalItemNm"), "업무수행평가항목", 1, 200);
		if((Integer)returnMap.get("ErrorNumber") < 0) {
			return returnMap;
		}
	   	returnMap = ValidationChk.lengthCheck(searchMap.getString("content"), "설명", 0, 4000);
		if((Integer)returnMap.get("ErrorNumber") < 0) {
			return returnMap;
		}
    	returnMap = ValidationChk.lengthCheck(searchMap.getString("sortOrder"), "정렬순서", 0, 5);
		if((Integer)returnMap.get("ErrorNumber") < 0) {
			return returnMap;
		}
        returnMap.put("ErrorNumber",  resultValue);
        return returnMap;        
    }
}
