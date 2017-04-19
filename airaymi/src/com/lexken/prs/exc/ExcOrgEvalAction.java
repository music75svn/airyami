/*************************************************************************
* CLASS 명      : ExcOrgEvalAction
* 작 업 자      : 안요한
* 작 업 일      : 2013년 07월 19일 
* 기    능      : 부설기관장 별도평가
* ---------------------------- 변 경 이 력 --------------------------------
* 번호   작 업 자      작   업   일        변 경 내 용              비고
* ----  ---------  -----------------  -------------------------    --------
*   1    안요한      2013년 07월 19일         최 초 작 업 
**************************************************************************/
package com.lexken.prs.exc;
    
import java.util.ArrayList;
import java.util.HashMap;

import com.lexken.framework.common.ErrorMessages;
import com.lexken.framework.common.ExcelVO;
import com.lexken.framework.common.Paging;
import com.lexken.framework.common.SearchMap;
import com.lexken.framework.common.StringConstants;
import com.lexken.framework.common.ValidationChk;
import com.lexken.framework.struts2.IsBoxActionSupport;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lexken.framework.core.CommonService;
import com.lexken.framework.login.LoginManager;
import com.lexken.framework.util.StaticUtil;

public class ExcOrgEvalAction extends CommonService {

    private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * 부설기관장 별도평가 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap excOrgEvalList(SearchMap searchMap) {
    	
    	/**********************************
         * 확정 조회
         **********************************/
    	searchMap.addList("confirmYn", getDetail("prs.exc.excOrgEval.getConfirmYn", searchMap));
    	
    	return searchMap;
    	
    }
    
    /**
     * 부설기관장 별도평가 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap excOrgEvalList_xml(SearchMap searchMap) {
    	
        searchMap.addList("list", getList("prs.exc.excOrgEval.getList", searchMap));
        
        return searchMap;
    }
    
    /**
     * 부설기관장 별도평가 수정
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap excOrgEvalProcess(SearchMap searchMap) {
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
        if("MOD".equals(stMode)) {
            searchMap = updateDB(searchMap);
        } else if ("CONF".equals(stMode)) {
        	searchMap = updateSubmitDB(searchMap);
        }
        
        /**********************************
         * Return
         **********************************/
        return searchMap;
    }
    
    /**
     * 부설기관장 별도평가 수정
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap updateDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
	        	setStartTransaction();
	        	
	        	String[] excGrpIds = searchMap.getStringArray("excGrpId");
	        	String[] empns = searchMap.getStringArray("empn");
	        	String[] effScores = searchMap.getStringArray("effScore");
	        	
	        	String[] totalScores = searchMap.getString("totalScores").split("\\|", 0);
	        	String[] grades = searchMap.getString("grades").split("\\|", 0);
	        	
	        	for(int i=0; i<excGrpIds.length; i++){
	        		
	        		searchMap.put("totalScore", totalScores[i]);
	        		searchMap.put("excGrpId", excGrpIds[i]);
	        		searchMap.put("empn", empns[i]);
	        		searchMap.put("effScore", effScores[i]);
	        		searchMap.put("grade", grades[i]);
	        		
	        		returnMap = insertData("prs.exc.excOrgEval.insertData", searchMap);
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
     * 부설기관 별도평가 확정하기
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap updateSubmitDB(SearchMap searchMap) {
    	HashMap returnMap    = new HashMap();
    	String excOrg = (String) searchMap.get("excOrgEvalYn");
    	
    	try {
    		setStartTransaction();
    		
    		if("Y".equals(excOrg)) {
    		
	    		String[] excGrpIds = searchMap.getStringArray("excGrpId");
	        	String[] empns = searchMap.getStringArray("empn");
	        	String[] effScores = searchMap.getStringArray("effScore");
	        	
	        	String[] totalScores = searchMap.getString("totalScores").split("\\|", 0);
	        	String[] grades = searchMap.getString("grades").split("\\|", 0);
	        	
	        	for(int i=0; i<excGrpIds.length; i++){
	        		
	        		searchMap.put("totalScore", totalScores[i]);
	        		searchMap.put("excGrpId", excGrpIds[i]);
	        		searchMap.put("empn", empns[i]);
	        		searchMap.put("effScore", effScores[i]);
	        		searchMap.put("grade", grades[i]);
	        		
	        		returnMap = insertData("prs.exc.excOrgEval.insertData", searchMap);
	        	}
    		}
    		
    		returnMap = updateData("prs.exc.excOrgEval.insertExcOrgEvalYnData", searchMap); //확정여부 수정
    	} catch (Exception e) {
    		setRollBackTransaction();
    		logger.error(e.toString());
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
