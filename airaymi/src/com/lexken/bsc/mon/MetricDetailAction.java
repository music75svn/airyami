/*************************************************************************
* CLASS 명      : MetricDetailAction
* 작 업 자      : 김윤기
* 작 업 일      : 2012년 8월 27일 
* 기    능      : 지표실적상세
* ---------------------------- 변 경 이 력 --------------------------------
* 번호   작 업 자      작   업   일        변 경 내 용              비고
* ----  --------  -----------------  -------------------------    --------
*   1    김윤기      2012년 8월 27일             최 초 작 업 
**************************************************************************/
package com.lexken.bsc.mon;
    
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.lexken.framework.common.ErrorMessages;
import com.lexken.framework.common.ExcelVO;
import com.lexken.framework.common.Paging;
import com.lexken.framework.common.SearchMap;
import com.lexken.framework.common.ValidationChk;
import com.lexken.framework.struts2.IsBoxActionSupport;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lexken.framework.core.CommonService;
import com.lexken.framework.login.LoginManager;
import com.lexken.framework.util.StaticUtil;

public class MetricDetailAction extends CommonService {

    private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * 지표실적상세 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap metricDetailList(SearchMap searchMap) {
        
    	//지표상세정보
    	HashMap detail = new HashMap();
    	detail = getDetail("bsc.module.commModule.getMetricInfo", searchMap);
		searchMap.put("userId", detail.get("INSERT_USER_ID"));
		searchMap.put("strategyId", detail.get("STRATEGY_ID"));
		
    	
        //성과조직명
    	searchMap.addList("scDeptNm", getStr("bsc.module.commModule.getScDeptNm", searchMap));
    	
        //전략과제명
    	searchMap.addList("strategyNm", getStr("bsc.module.commModule.getStrategyNm", searchMap));
    	
        //지표명
    	searchMap.addList("metricNm", getStr("bsc.module.commModule.getMetricNm", searchMap));

        //지표실적입력자명
    	searchMap.addList("userNm", getStr("bsc.module.commModule.getUserNm", searchMap));
    	
        //지표실적요약
    	searchMap.addList("summary", getDetail("bsc.mon.metricDetail.getSummary", searchMap));
    	
        //ACTIVITY
    	searchMap.addList("activity", getDetail("bsc.mon.metricDetail.getActivity", searchMap));
    	
        //미진사유
    	searchMap.addList("cause", getDetail("bsc.mon.metricDetail.getCause", searchMap));

        //ACTIVITY 첨부파일
    	searchMap.addList("fileList", getList("bsc.mon.metricDetail.getFileList", searchMap));

    	//실무자의견
    	searchMap.addList("coachingList", getList("bsc.mon.metricDetail.getCoaching", searchMap));
    	
        //상위자코칭
    	searchMap.addList("opinionList", getList("bsc.mon.metricDetail.getOpinion", searchMap));
    	
        return searchMap;
    }
    
    /**
     * 의견 코칭 팝업화면
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap metricDetailPop(SearchMap searchMap) {
    	String stMode = searchMap.getString("mode");
    	
    	if("OPINION".equals(searchMap.get("target"))){
    		if("MOD".equals(stMode) || "ANSMOD".equals(stMode)) {
    			searchMap.addList("content1", getDetail("bsc.mon.metricDetail.getOpinionContent", searchMap));
    		}
    	}else{
    		if("MOD".equals(stMode) || "ANSMOD".equals(stMode)) {
    			searchMap.addList("content1", getDetail("bsc.mon.metricDetail.getCoachingContent", searchMap));
    		}
    	}
        
        return searchMap;
    }
    
    /**
     * 지표실적상세 등록/삭제
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap metricDetailProcess(SearchMap searchMap) {
        HashMap returnMap = new HashMap();
        String stMode = searchMap.getString("mode");

        /**********************************
         * 등록/삭제
         **********************************/
        if("ADD".equals(stMode)) {
            searchMap = insertDB(searchMap);
        } else if("DEL".equals(stMode) || "ANSDEL".equals(stMode) ) {
            searchMap = deleteDB(searchMap);
        } 
        
        /**********************************
         * Return
         **********************************/
        return searchMap;
    }
    
    /**
     * 지표실적상세 수정/답변
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap metricDetailPopProcess(SearchMap searchMap) {
    	HashMap returnMap = new HashMap();
    	String stMode = searchMap.getString("mode");
    	
    	/**********************************
    	 * 수정/답변/댓글수정
    	 **********************************/
    	if("MOD".equals(stMode) || "REP".equals(stMode) || "ANSMOD".equals(stMode) ) {
    		searchMap = updateDB(searchMap);
    	}

    	/**********************************
    	 * Return
    	 **********************************/
    	return searchMap;
    }
    
    /**
     * 지표실적상세 등록
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap insertDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
        	setStartTransaction();
        
        	if("OPINION".equals(searchMap.get("target"))){
        		returnMap = insertData("bsc.mon.metricDetail.insertOpinion", searchMap);
        	}else{
        		returnMap = insertData("bsc.mon.metricDetail.insertCoaching", searchMap);
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
     * 지표실적상세 수정
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap updateDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
	        setStartTransaction();
	        
	        if("OPINION".equals(searchMap.get("target"))){
	        	if("MOD".equals(searchMap.get("mode"))){
	        		returnMap = updateData("bsc.mon.metricDetail.updateOpinion", searchMap);
	        	}else if("REP".equals(searchMap.get("mode"))){
	        		returnMap = updateData("bsc.mon.metricDetail.answerOpinion", searchMap);
	        	}else{
	        		returnMap = updateData("bsc.mon.metricDetail.updateOpinionAnswer", searchMap);
	        	}
	        }else{
	        	if("MOD".equals(searchMap.get("mode"))){
	        		returnMap = updateData("bsc.mon.metricDetail.updateCoaching", searchMap);
	        	}else if("REP".equals(searchMap.get("mode"))){
	        		returnMap = updateData("bsc.mon.metricDetail.answerCoaching", searchMap);
	        	}else{
	        		returnMap = updateData("bsc.mon.metricDetail.updateCoachingAnswer", searchMap);
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
     * 지표실적상세 삭제 
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap deleteDB(SearchMap searchMap) {
        
        HashMap returnMap = new HashMap(); 
	    try {

	        if("OPINION".equals(searchMap.get("target"))){
	        	if("DEL".equals(searchMap.get("mode"))){
	        		returnMap = updateData("bsc.mon.metricDetail.deleteOpinion", searchMap);
	        	}else{
	        		searchMap.put("content", "");
	        		returnMap = updateData("bsc.mon.metricDetail.updateOpinionAnswer", searchMap);
	        	}
	        }else{
	        	if("DEL".equals(searchMap.get("mode"))){
	        		returnMap = updateData("bsc.mon.metricDetail.deleteCoaching", searchMap);
	        	}else{
	        		searchMap.put("content", "");
	        		returnMap = updateData("bsc.mon.metricDetail.answerCoaching", searchMap);
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
        
        //Validation 체크 추가
        
        returnMap.put("ErrorNumber",  resultValue);
        return returnMap;        
    }

}
