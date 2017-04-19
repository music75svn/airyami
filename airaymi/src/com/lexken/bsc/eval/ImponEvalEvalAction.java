/*************************************************************************
* CLASS 명      : ImponEvalEvalAction
* 작 업 자      : 김윤기
* 작 업 일      : 2012년 7월 26일 
* 기    능      : 비계량평가실시
* ---------------------------- 변 경 이 력 --------------------------------
* 번호  작 업 자     작     업     일        변 경 내 용                 비고
* ----  --------  -----------------  -------------------------    --------
*   1    김윤기      2012년 7월 26일             최 초 작 업 
**************************************************************************/
package com.lexken.bsc.eval;
    
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

public class ImponEvalEvalAction extends CommonService {

    private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * 비계량평가실시 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap imponEvalEvalList(SearchMap searchMap) {

    	String findCycle = (String)searchMap.get("findCycle");
    	searchMap.addList("evalCycle", getList("bsc.eval.imponEvalGrp.getEvalCycle", searchMap));

    	//디폴트 조회조건 설정(findCycle)
    	if("".equals(StaticUtil.nullToBlank(findCycle))) {
    		searchMap.put("findCycle", searchMap.getDefaultValue("evalCycle", "CODE_ID", 0));
    	}
    	searchMap.addList("evalEmpList", getList("bsc.eval.imponEvalEval.getEvalEmpList", searchMap));
    	
    	//디폴트 조회조건 설정(findEvalEmpId)
    	if("".equals(StaticUtil.nullToBlank(findCycle))) {
    		searchMap.put("findEvalEmpId", searchMap.getDefaultValue("evalEmpList", "EVAL_EMP_ID", 0));
    	}
    	
    	searchMap.addList("evalable", getDetail("bsc.eval.imponEvalGrp.getEvalable", searchMap));
    	searchMap.addList("allEvaled", getDetail("bsc.eval.imponEvalEval.getAllEvaled", searchMap));
    	searchMap.addList("isSubmitted", getStr("bsc.eval.imponEvalEval.isSubmitted", searchMap));
    	
        return searchMap;
    }
    
    /**
     * 비계량평가실시 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap imponEvalEvalList_xml(SearchMap searchMap) {
    	
        searchMap.addList("list", getList("bsc.eval.imponEvalEval.getList", searchMap));
        return searchMap;
        
    }
    
    /**
     * 비계량평가실시 상세화면
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap imponEvalEvalModify(SearchMap searchMap) {
    	String stMode = searchMap.getString("mode");
        
    	if("MOD".equals(stMode)) {
    		searchMap.addList("gradeList", getList("bsc.eval.imponEvalEval.getDetail", searchMap));
    		searchMap.addList("suggeList", getDetail("bsc.eval.imponEvalEval.getSuggests", searchMap));
    	}
        
        return searchMap;
    }
    
    /**
     * 비계량평가실시 등록/수정/삭제
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap imponEvalEvalProcess(SearchMap searchMap) {
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
        } else if("SUBMIT".equals(stMode)) {
        	searchMap.put("status", "Y");
            searchMap = submitDB(searchMap);
        } 
        
        /**********************************
         * Return
         **********************************/
        return searchMap;
    }
    
    /**
     * 비계량평가실시 등록
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap insertDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
        	setStartTransaction();
        
        	returnMap = insertData("bsc.eval.imponEvalEval.insertData", searchMap);
        
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
     * 비계량평가실시 평가
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap updateDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
	        setStartTransaction();

	        int index = StaticUtil.toNumber(searchMap.getString("index"));
	        String itemCd  = null;
	        String suggest = null;
	        String grade   = null;
	        
	        
	        if(index>0){
	            returnMap = deleteData("bsc.eval.imponEvalEval.deleteData", searchMap, true);
	            returnMap = deleteData("bsc.eval.imponEvalEval.deleteSuggest", searchMap, true);
	        	for(int i=1; i<=index; i++){
	        		itemCd   = searchMap.getString("itemCds_"+i);
	        		suggest = searchMap.getString("suggests_"+i);
	        		grade	 = searchMap.getString("grades_"+i);
	        		searchMap.put("itemCd", itemCd);
	        		searchMap.put("suggest", suggest);
        			searchMap.put("grade", grade.split("_")[0]);
        			searchMap.put("gradeScore", grade.split("_")[1]);
        			
        			returnMap = insertData("bsc.eval.imponEvalEval.updateData", searchMap);
        			returnMap = insertData("bsc.eval.imponEvalEval.updateSuggest", searchMap);
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
     * 비계량평가실시 평가완료
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap submitDB(SearchMap searchMap) {
    	HashMap returnMap    = new HashMap();    
    	
    	try {
    		setStartTransaction();
    		
    		returnMap = updateData("bsc.eval.imponEvalEval.submitData", searchMap);
    		
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
     * 비계량평가실시 삭제 
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap deleteDB(SearchMap searchMap) {
        
        HashMap returnMap = new HashMap(); 
	    try {
	        
	        setStartTransaction();
	        
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
