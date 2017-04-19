/*************************************************************************
* CLASS 명      : MemDetailEvalMetricAction
* 작 업 자      : 유연주
* 작 업 일      : 2017년 03월 13일 
* 기    능      : 상세평가지표
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

public class MemDetailEvalMetricAction extends CommonService {

    private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * 상세평가지표 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap memDetailEvalMetricList(SearchMap searchMap) {
    	
    	searchMap.addList("evalGubunList", getList("mem.base.memDetailEvalMetric.evalGubunList", searchMap));
    	
    	return searchMap;
    }
    
    /**
     * 상세평가지표 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap memDetailEvalMetricList_xml(SearchMap searchMap) {
        
        searchMap.addList("list", getList("mem.base.memDetailEvalMetric.getList", searchMap));

        return searchMap;
    }
    
    /**
     * 상세평가지표 상세화면
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap memDetailEvalMetricModify(SearchMap searchMap) {
    	String stMode = searchMap.getString("mode");
    	if("MOD".equals(stMode)) {
    		searchMap.addList("detail", getDetail("mem.base.memDetailEvalMetric.getDetail", searchMap));
    	}
    	
        return searchMap;
    }
    
    /**
     * 상세평가지표 등록/수정/삭제
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap memDetailEvalMetricProcess(SearchMap searchMap) {
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
     * 상세평가지표 등록
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap insertDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
        	setStartTransaction();
        
        	// 상세평가지표ID 생성
        	HashMap evalGrpIdMap = getDetail("mem.base.memDetailEvalMetric.getDetailEvalMetricItemId", searchMap);
        	searchMap.put("detailEvalMetricId", evalGrpIdMap.get("DETAIL_EVAL_METRIC_ID"));
        	
	        // 상세평가지표 등록
	        returnMap = insertData("mem.base.memDetailEvalMetric.insertData", searchMap);
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
     * 상세평가지표 수정
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap updateDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
	        setStartTransaction();
	        
	        // 상세평가지표 수정
	        returnMap = updateData("mem.base.memDetailEvalMetric.updateData", searchMap);
	        
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
     * 상세평가지표 정렬순서 일괄 저장 
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap allSaveDB(SearchMap searchMap) {
        
        HashMap returnMap = new HashMap(); 
	    
	    try {
			String[] detailEvalMetricIds = searchMap.getString("detailEvalMetricIds").split("\\|", -1);
			String[] sortOrders = searchMap.getString("sortOrders").split("\\|", -1);
	        
	        setStartTransaction();
	        
	        if(null != detailEvalMetricIds && 0 < detailEvalMetricIds.length) {
		        for (int i = 0; i < detailEvalMetricIds.length -1; i++) {
		            searchMap.put("sortOrder", sortOrders[i]);
		            logger.debug("sortOrders[i] : "+sortOrders[i]);
		            searchMap.put("detailEvalMetricId", detailEvalMetricIds[i]);
		            returnMap = updateData("mem.base.memDetailEvalMetric.allSaveData", searchMap);
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
     * 상세평가지표 삭제 
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap deleteDB(SearchMap searchMap) {
        
        HashMap returnMap = new HashMap(); 
	    
	    try {
			String[] detailEvalMetricIds = searchMap.getString("detailEvalMetricIds").split("\\|", -1);
	        
	        setStartTransaction();
	        
	        if(null != detailEvalMetricIds && 0 < detailEvalMetricIds.length) {
		        for (int i = 0; i < detailEvalMetricIds.length - 1; i++) {
		        	searchMap.put("detailEvalMetricId", detailEvalMetricIds[i]);
		            returnMap = updateData("mem.base.memDetailEvalMetric.deleteData", searchMap);
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
        
    	returnMap = ValidationChk.lengthCheck(searchMap.getString("detailEvalMetricNm"), "상세평가지표", 1, 200);
		if((Integer)returnMap.get("ErrorNumber") < 0) {
			return returnMap;
		}

		returnMap = ValidationChk.checkCommaPointNumber(searchMap.getString("eval1Rate"), "1차평가자비율");
		if((Integer)returnMap.get("ErrorNumber") < 0) {
			return returnMap;
		}
		returnMap = ValidationChk.checkCommaPointNumber(searchMap.getString("eva21Rate"), "2차평가자비율");
		if((Integer)returnMap.get("ErrorNumber") < 0) {
			return returnMap;
		}
		returnMap = ValidationChk.checkCommaPointNumber(searchMap.getString("evalPeerRate"), "동료평가비율");
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
