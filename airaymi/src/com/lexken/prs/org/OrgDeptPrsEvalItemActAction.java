/*************************************************************************
* CLASS 명      : OrgDeptPrsEvalItemActAction
* 작 업 자      : 박선혜
* 작 업 일      : 2013년 6월 14일 
* 기    능      : 조직별평가실적등록
* ---------------------------- 변 경 이 력 --------------------------------
* 번호  작 업 자     작     업     일        변 경 내 용                 비고
* ----  --------  -----------------  -------------------------    --------
*   1    박선혜      2013년 6월 14일             최 초 작 업 
**************************************************************************/
package com.lexken.prs.org;
    
import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lexken.framework.common.ErrorMessages;
import com.lexken.framework.common.SearchMap;
import com.lexken.framework.core.CommonService;

public class OrgDeptPrsEvalItemActAction extends CommonService {

    private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * 개인별평가실적 등록 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap orgDeptPrsEvalItemActList(SearchMap searchMap) {    	
        searchMap.addList("rptSchedule", getDetail("prs.org.orgDeptEvalItemAct.getRptSchedule", searchMap)); //입력기간 조회
    	
    	return searchMap;
    }
    
    
    /**
     * 세부평가항목 산식항목 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap orgDeptPrsEvalItemActList_xml(SearchMap searchMap) {
        
        searchMap.addList("list", getList("prs.org.orgDeptPrsEvalItemAct.getList", searchMap));

        return searchMap;
    }
    
    
    /**
     * 조직트리
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap popDeptCdTree(SearchMap searchMap) {
        
    	searchMap.addList("treeList", getList("prs.org.orgDeptPrsEvalItemAct.getScDeptList", searchMap));

        return searchMap;
    }
    
    
    /**
     * 산식항목 등록/수정/삭제
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap orgDeptPrsEvalItemActProcess(SearchMap searchMap) {
        HashMap returnMap = new HashMap();
        String stMode = searchMap.getString("mode");
               
        /**********************************
         * 등록/수정/삭제
         **********************************/
        if("PRS".equals(stMode)) {
            searchMap = updatePrsDB(searchMap);
        } else if("MOD".equals(stMode)) {
        	searchMap = updateDB(searchMap);
        } else if("CALL".equals(stMode)) {
        	searchMap = updateCallDB(searchMap);
        }
        
        /**********************************
         * Return
         **********************************/
        return searchMap;
    }
    
    
    /**
     * 개인별 실적적용
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap updatePrsDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
	        setStartTransaction();
                        
            /**********************************
	         * 실적적용
	         **********************************/
	        returnMap = insertData("prs.org.orgDeptPrsEvalItemAct.callSpOrgPrsActualProc", searchMap);
	        
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
     * 개인별항목 수정
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap updateDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
	        setStartTransaction();
	        String[] deptCd			  = searchMap.getStringArray("deptCd");
	        String[] empn			  = searchMap.getStringArray("empn");
	        String[] orgEvalItemId	  = searchMap.getStringArray("orgEvalItemId");
	        String[] calTypeCol		  = searchMap.getStringArray("calTypeCol");
	        String[] itemValue		  = searchMap.getStringArray("itemValue");
	    	
	    	
            for(int i=0; i<deptCd.length; i++){
            	if(null != deptCd[i]){
            		searchMap.put("deptCd", deptCd[i]);
            		searchMap.put("empn", empn[i]);
            		searchMap.put("orgEvalItemId", orgEvalItemId[i]);
            		searchMap.put("calTypeCol", calTypeCol[i]);
            		searchMap.put("itemValue", itemValue[i]);

            		/**********************************
        	         *실적 입력 
        	         **********************************/
        	        returnMap = updateData("prs.org.orgDeptPrsEvalItemAct.updatePrsData", searchMap);
            	}
            }
            
            /**********************************
	         * 실적계산
	         **********************************/
            for(int i=0; i<deptCd.length; i++){
            	if(null != deptCd[i]){
            		searchMap.put("deptCd", deptCd[i]);
            		searchMap.put("empn", empn[i]);
            		searchMap.put("orgEvalItemId", orgEvalItemId[i]);

        	        returnMap = insertData("prs.org.orgDeptPrsEvalItemAct.callSpPrsActualProc", searchMap);
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
     * 개인별 점수계산
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap updateCallDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
	        setStartTransaction();
                        
            /**********************************
	         * 실적적용
	         **********************************/
	        returnMap = insertData("prs.org.orgDeptPrsEvalItemAct.callSpOrgPrsScoreProc", searchMap);
	        
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
