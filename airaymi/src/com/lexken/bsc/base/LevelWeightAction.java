/*************************************************************************
* CLASS 명      : levelWeightAction
* 작 업 자      : 안요한
* 작 업 일      : 2013년 9월 3일 
* 기    능      : 난이도 가중치
* ---------------------------- 변 경 이 력 --------------------------------
* 번호  작 업 자     작     업     일        변 경 내 용                 비고
* ----  --------  -----------------  -------------------------    --------
*   1    안요한      2013년 9월 3일             최 초 작 업 
**************************************************************************/
package com.lexken.bsc.base;
    
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lexken.framework.common.ErrorMessages;
import com.lexken.framework.common.ExcelVO;
import com.lexken.framework.common.SearchMap;
import com.lexken.framework.common.StringConstants;
import com.lexken.framework.common.ValidationChk;
import com.lexken.framework.core.CommonService;
import com.lexken.framework.util.HtmlHelper;
import com.lexken.framework.util.StaticUtil;
import com.lexken.framework.login.LoginVO;

public class LevelWeightAction extends CommonService {

    private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * 난이도 평가 설정 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap levelWeightList(SearchMap searchMap) {
    	
    	/**********************************
         * 평가구간대 조회
         **********************************/
        searchMap.addList("evalSectionList", getList("bsc.base.levelWeight.evalSectionList", searchMap));
        
        /**********************************
         * 난이도평점 조회
         **********************************/
        searchMap.addList("levelList", getList("bsc.base.levelWeight.levelList", searchMap));
    	
        return searchMap;
    }
    
    /**
     * 난이도 가중치 설정 등록/수정/삭제
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap levelWeightProcess(SearchMap searchMap) {
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
        } 
        
        /**********************************
         * Return
         **********************************/
        return searchMap;
    }
    
    /**
     * 난이도 가중치 등록
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap insertDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
        	setStartTransaction();
        	
        	/**********************************
             * 구간대
             **********************************/
            String[] evalSectionIds = searchMap.getStringArray("evalSectionIds");
            String[] scores = searchMap.getStringArray("scores");
            String[] fromValues = searchMap.getStringArray("fromValues");
            String[] toValues = searchMap.getStringArray("toValues");
            String[] conversionScores = searchMap.getStringArray("conversionScores");
        	
            /**********************************
             * 구간대 삭제
             **********************************/
	        returnMap = updateData("bsc.base.levelWeight.deleteEvalSection", searchMap, true);

	        /**********************************
	         * 구간대 등록
	         **********************************/
	        		if(null != evalSectionIds && 0 < evalSectionIds.length) {
				        for (int i = 0; i < evalSectionIds.length; i++) {
				            searchMap.put("evalSectionId", evalSectionIds[i]);
				            searchMap.put("conversionScore", conversionScores[i]);
				            searchMap.put("fromValue", fromValues[i]);
				            searchMap.put("toValue", toValues[i]);
				            returnMap = insertData("bsc.base.levelWeight.insertEvalSection", searchMap);
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
        
        returnMap.put("ErrorNumber",  resultValue);
        return returnMap;        
    }
    
}
