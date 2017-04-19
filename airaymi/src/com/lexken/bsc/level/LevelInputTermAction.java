/*************************************************************************
* CLASS 명      : LevelInputTermAction
* 작 업 자      : 현걸욱
* 작 업 일      : 2012년 10월 22일 
* 기    능      : 난이도평가일정관리
* ---------------------------- 변 경 이 력 --------------------------------
* 번호   작 업 자      작   업   일        변 경 내 용              비고
* ----  --------  -----------------  -------------------------    --------
*   1    현걸욱      2012년 10월 22일             최 초 작 업 
**************************************************************************/
package com.lexken.bsc.level;

import java.util.ArrayList;
import java.util.HashMap;

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

public class LevelInputTermAction extends CommonService {

	private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * 난이도평가일정관리 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap levelInputTermList(SearchMap searchMap) {
    	
    	//난이도평가마감구분 마감'Y'
		searchMap.addList("evalCloseYn", getStr("bsc.level.levelInputTerm.getEvalCloseYn", searchMap));
		//난이도평가제출구분 제출'Y'
		searchMap.addList("evalSubmitYn", getStr("bsc.level.levelInputTerm.getEvalSubmitYn", searchMap));
    	
        return searchMap;
    }
    
    /**
     * 난이도평가일정관리 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap levelInputTermList_xml(SearchMap searchMap) {
        
        searchMap.addList("list", getList("bsc.level.levelInputTerm.getList", searchMap));

        return searchMap;
    }
    
    /**
     * 난이도평가일정관리 상세화면
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap levelInputTermModify(SearchMap searchMap) {
    	String stMode = searchMap.getString("stMode");
    	
    	//난이도평가마감구분 마감'Y' 한개의 평가단이라도 마감이 되어있으면 임계치 수정 불가
		searchMap.addList("evalCloseYn", getStr("bsc.level.levelInputTerm.getEvalCloseYn", searchMap));
		//난이도평가제출구분 제출'Y'
		searchMap.addList("evalSubmitYn", getStr("bsc.level.levelInputTerm.getEvalSubmitYn", searchMap));
        
    	if("MOD".equals(stMode)) {
    		
    		searchMap.addList("detail", getDetail("bsc.level.levelInputTerm.getDetail", searchMap));
    	}
        
        return searchMap;
    }
    
    /**
     * 난이도평가일정관리 등록/수정/삭제
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap levelInputTermProcess(SearchMap searchMap) {
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
     * KPI입력일정관리 등록
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap insertDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
        	setStartTransaction();
        	
        	/**********************************
	         * 해당 월 등록여부 확인
	         **********************************/
	        int cnt = getInt("bsc.level.levelInputTerm.getCount", searchMap);
	        
	        if(0 < cnt) {
	            returnMap.put("ErrorNumber", ErrorMessages.FAILURE_DUP2_CODE);
	            returnMap.put("ErrorMessage", ErrorMessages.format(ErrorMessages.FAILURE_DUP2_MESSAGE, "입력 년도"));
	        } else {
	        	returnMap = insertData("bsc.level.levelInputTerm.insertData", searchMap);
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
     * KPI입력일정관리 수정
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap updateDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
	        setStartTransaction();
	        
	        returnMap = updateData("bsc.level.levelInputTerm.updateData", searchMap);
	        
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
     * KPI입력일정관리 삭제 
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap deleteDB(SearchMap searchMap) {
        
        HashMap returnMap = new HashMap(); 
        
	    try {
	        String[] years = searchMap.getString("years").split("\\|", 0);
	        
	        setStartTransaction();
	        
	        for (int i = 0; i < years.length; i++) {
	            searchMap.put("year", years[i]);
	            returnMap = updateData("bsc.level.levelInputTerm.deleteData", searchMap);
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
