/*************************************************************************
* CLASS 명      : OrgDeptEvalItemActAction
* 작 업 자      : 박선혜
* 작 업 일      : 2013년 6월 14일 
* 기    능      : 조직별평가실적등록
* ---------------------------- 변 경 이 력 --------------------------------
* 번호  작 업 자     작     업     일        변 경 내 용                 비고
* ----  --------  -----------------  -------------------------    --------
*   1    박선혜      2013년 6월 14일             최 초 작 업 
**************************************************************************/
package com.lexken.prs.org;
    
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

public class OrgDeptEvalItemActAction extends CommonService {

    private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * 조직별평가실적 등록 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap orgDeptEvalItemActList(SearchMap searchMap) {
    	
    	
    	
    	
    	/************************************************************************************
    	 * 평가조직 트리 조회
    	 ************************************************************************************/
        searchMap.addList("treeList", getList("prs.org.orgDeptEvalItemAct.getDeptList1", searchMap));
        
        searchMap.addList("rptSchedule", getDetail("prs.org.orgDeptEvalItemAct.getRptSchedule", searchMap)); //입력기간 조회
        
    	/************************************************************************************
    	 * 디폴트 평가조직 조회
    	 ************************************************************************************/
    	String deptCd = searchMap.getString("deptCd");
    	searchMap.put("findSearchCodeId", deptCd);
    	if("".equals(deptCd)) {
    		searchMap.put("deptCd", searchMap.getDefaultValue("treeList", "CODE_ID", 0));
    		searchMap.put("deptNm", searchMap.getDefaultValue("treeList", "CODE_NM", 0));
    		searchMap.put("findSearchCodeId", searchMap.getDefaultValue("treeList", "CODE_ID", 0));
    	}        
    	
    	return searchMap;
    }
    
    
    /**
     * 세부평가항목 산식항목 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap orgDeptEvalItemActList_xml(SearchMap searchMap) {
        
    	searchMap.addList("rptSchedule", getDetail("prs.org.orgDeptEvalItemAct.getRptSchedule", searchMap)); //입력기간 조회
        searchMap.addList("list", getList("prs.org.orgDeptEvalItemAct.getList", searchMap));

        return searchMap;
    }
    
    /**
     * 산식항목 등록/수정/삭제
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap orgDeptEvalItemActProcess(SearchMap searchMap) {
        HashMap returnMap = new HashMap();
        String stMode = searchMap.getString("mode");
        
        /**********************************
         * 무결성 체크
         **********************************/
/*
        if("MOD".equals(stMode) ) {
            returnMap = this.validChk(searchMap);
            
            if((Integer)returnMap.get("ErrorNumber") < 0 ){
                searchMap.addList("returnMap", returnMap);
                return searchMap;
            }
        }
*/        
        /**********************************
         * 등록/수정/삭제
         **********************************/
        if("MOD".equals(stMode)) {
            searchMap = updateDB(searchMap);
        } else if("MOD2".equals(stMode)) {
        	searchMap = updateDB2(searchMap);
        }
        
        /**********************************
         * Return
         **********************************/
        return searchMap;
    }
    
    /**
     * 산식항목 수정
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap updateDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
	        setStartTransaction();
	        
	        String[] orgEvalItemIds   = searchMap.getStringArray("orgEvalItemIds");
	        String[] calTypeCols  	  = searchMap.getString("calTypeCols").split("\\|", 0);
	        String[] scores 		  = searchMap.getStringArray("scores");
	    	String deptCd			  = searchMap.getString("deptCd");
	    	String year 			  = searchMap.getString("year");
	    	
            for(int i=0; i<orgEvalItemIds.length; i++){
            	if(null != orgEvalItemIds[i]){
            		searchMap.put("orgEvalItemId", orgEvalItemIds[i]);
            		searchMap.put("calTypeCol", calTypeCols[i]);
            		searchMap.put("score", scores[i]);
            		
            		String orgEvalItemId = orgEvalItemIds[i];
            		String calTypeCol    = calTypeCols[i];
            		String score 	     = scores[i];
			
            		 /**********************************
        	         * 세부평가항목 수정 
        	         **********************************/
        	        returnMap = updateData("prs.org.orgDeptEvalItemAct.updateData", searchMap);
            	}
            }
            
            /**********************************
	         * 실적계산
	         **********************************/
	        returnMap = insertData("prs.org.orgDeptEvalItemAct.callSpOrgActualProc", searchMap);
	        
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
     * 산식항목 수정
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap updateDB2(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
	        setStartTransaction();
	                   
            /**********************************
	         * 실적계산
	         **********************************/
	        returnMap = insertData("prs.org.orgDeptEvalItemAct.callSpOrgActualProcAll", searchMap);
	        
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
