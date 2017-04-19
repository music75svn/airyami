/*************************************************************************
* CLASS 명      : MemNonMetricEvalAction
* 작 업 자      : 유연주
* 작 업 일      : 2017년 04월17일 
* 기    능      : 비계량지표평가
* ---------------------------- 변 경 이 력 --------------------------------
* 번호   작 업 자      작   업   일        변 경 내 용              비고
* ----  ---------  -----------------  -------------------------    --------
*   1    유연주      2017년 04월17일           최 초 작 업 
**************************************************************************/
package com.lexken.mem.eval;
    
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lexken.framework.common.ErrorMessages;
import com.lexken.framework.common.SearchMap;
import com.lexken.framework.core.CommonService;
import com.lexken.framework.login.LoginVO;

public class MemNonMetricEvalAction extends CommonService {

    private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * 비계량지표평가
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap memNonMetricEvalList(SearchMap searchMap) {
    	
    	// 평가기간조회
    	searchMap.put("year", searchMap.getString("findYear"));
    	searchMap.addList("periodInfo", getDetail("mem.eval.memNonMetricEval.getPeriodInfo", searchMap));
    	
    	return searchMap;
    }
    
    /**
     * 비계량지표평가 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap memNonMetricEvalList_xml(SearchMap searchMap) {
    	
    	LoginVO loginVO = (LoginVO)searchMap.get("loginVO");
    	
    	if(!loginVO.chkAuthGrp("01")) {
    		searchMap.put("sEmpNo", loginVO.getUser_id());
    	}
    	
        searchMap.addList("list", getList("mem.eval.memNonMetricEval.getList", searchMap));

        return searchMap;
    }
    
    /**
     * 비계량지표평가화면
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap memNonMetricEvalModify(SearchMap searchMap) {
    	String stMode = searchMap.getString("mode");

    	// 평가기간조회
    	searchMap.addList("periodInfo", getDetail("mem.eval.memNonMetricEval.getPeriodInfo", searchMap));
    	
    	// 평가상태조회
    	searchMap.addList("stateInfo", getDetail("mem.eval.memNonMetricEval.getStateInfo", searchMap));
    	
        return searchMap;
    }
    
    /**
     * 비계량지표평가화면 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap memNonMetricEvalModify_xml(SearchMap searchMap) {
    	
    	searchMap.addList("list", getList("mem.eval.memNonMetricEval.getNonMetricEvalMemberList", searchMap));

        return searchMap;
    }
    
    /**
     * 비계량지표평가 저장
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap memNonMetricEvalProcess(SearchMap searchMap) {
        HashMap returnMap = new HashMap();
        String stMode = searchMap.getString("mode");
        
        /**********************************
         * 무결성 체크
         **********************************/
        if(!"CANCEL".equals(stMode) && !"COMPLETE".equals(stMode) ) {
            returnMap = this.validChk(searchMap);
            
            if((Integer)returnMap.get("ErrorNumber") < 0 ){
                searchMap.addList("returnMap", returnMap);
                return searchMap;
            }
        }
        
        /**********************************
         * 등록/수정/삭제
         **********************************/
        if("COMPLETE".equals(stMode)) {
            searchMap = complateEvalDB(searchMap);
        }else if("CANCEL".equals(stMode)) {
        	searchMap = cancelEvalDB(searchMap);
        }else if("SUBMIT".equals(stMode)) {
        	searchMap = saveEvalDB(searchMap);
        }
        
        return searchMap;
    }
    
    /**
     * 저장
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap saveEvalDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
        	setStartTransaction();
        	logger.debug("searchMap : "+searchMap);
			String[] empNos = searchMap.getStringArray("empNos");
			String[] evalScores = searchMap.getStringArray("evalScores");
			String[] metricIds = searchMap.getStringArray("metricIds");
			
			if(null != empNos && 0 < empNos.length) {
 		        for (int i = 0; i < empNos.length; i++) {
 		        	searchMap.put("empNo", empNos[i]);
 		        	searchMap.put("evalScore", evalScores[i]);
 		        	searchMap.put("metricId", metricIds[i]);
 		        	searchMap.put("evalOpinion", searchMap.getString("opinion"+empNos[i]));
 		        	
 		        	// 비계량지표평가등록
		            returnMap = insertData("mem.eval.memNonMetricEval.insertNonMetricEvalData", searchMap);
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
     * 완료
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap complateEvalDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
        	setStartTransaction();
        
        	// 저장먼저 처리
        	saveEvalDB(searchMap);
        	
	        // 비계퍙지표 평가완료처리
        	searchMap.put("evalState", "Y");
	        returnMap = updateData("mem.eval.memNonMetricEval.updateNonMetricEvalStateData", searchMap);
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
     * 완료취소
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap cancelEvalDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
        	setStartTransaction();
        
	        // 비계퍙지표평가취소처리
        	searchMap.put("evalState", "N");
	        returnMap = updateData("mem.eval.memNonMetricEval.updateNonMetricEvalStateData", searchMap);
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
        
        returnMap.put("ErrorNumber",  resultValue);
        return returnMap;        
    }
}
