/*************************************************************************
* CLASS 명      : EvalGroupAction
* 작 업 자      : 안요한
* 작 업 일      : 2013년 06월 24일 
* 기    능      : 직원평가 조직별 평가군 관리
* ---------------------------- 변 경 이 력 --------------------------------
* 번호   작 업 자      작   업   일        변 경 내 용              비고
* ----  ---------  -----------------  -------------------------    --------
*   1    안요한      2013년 06월 24일      최 초 작 업 
**************************************************************************/
package com.lexken.prs.emp;
    
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

public class EvalGroupAction extends CommonService {

    private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * 직원평가 조직별 평가군 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap evalGroupList(SearchMap searchMap) {
    	
    	// 최상위 평가조직 조회
    	HashMap topScDept = (HashMap)getDetail("prs.insa.insa.getTopDeptInfo", searchMap);

    	// 평가군 상태 가져오기
    	searchMap.addList("evalStatus", getDetail("prs.emp.evalGroup.getEvalStatus", searchMap));

    	// 파라미터가 null로 들어올 때 디폴트값 셋팅.
    	if(topScDept==null){
    		topScDept =new HashMap();
    		topScDept.put("DEPT_CD", "");
    		topScDept.put("DEPT_KOR_NM", "");
    	}
    	
    	// 파라미터가 null로 들어올 때 디폴트값 셋팅.
    	String findDeptCd =  StaticUtil.nullToDefault((String)searchMap.getString("findDeptCd"), (String)topScDept.get("DEPT_CD"));	// 조직코드가 없으면 전사조직코드를 셋팅.
    	String findUpDeptName =  StaticUtil.nullToDefault((String)searchMap.getString("findUpDeptName"), (String)topScDept.get("DEPT_KOR_NM")) ; ;	// 조직명이 없으면 전사조직명을 셋팅.
    	
    	// 디폴트 조회조건 설정
    	searchMap.put("findDeptCd", findDeptCd);	// 검색버튼 조회시 조직트리에 기존 선택한 조직이 표시되도록 설정.(findSearchCodeId에 넣어주면 우선 적용됨. 트리와 검색조건을 별도로 사용할 경우에 한함.)
    	searchMap.put("findUpDeptName", findUpDeptName);
    	
    	searchMap.addList("deptTree", getList("prs.emp.evalGroup.getDeptList", searchMap)); //미사용은 제외한 인사조직
    	
    	searchMap.addList("getEvalGroupList", getList("prs.emp.evalGroup.getEvalGroupList", searchMap));
    	
    	return searchMap;
    }
    
    /**
     * 직원평가 조직별평가군 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap evalGroupList_xml(SearchMap searchMap) {
    	
        searchMap.addList("list", getList("prs.emp.evalGroup.getList", searchMap));
        
        
        return searchMap;
    }
    
    /**
     * 직원평가 조직별평가군 등록/수정/삭제
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap evalGroupProcess(SearchMap searchMap) {
        HashMap returnMap = new HashMap();
        String stMode = searchMap.getString("mode");
        
        /**********************************
         * 무결성 체크
         **********************************/
        if(!"DEL".equals(stMode) && !"AUTO".equals(stMode)) {
        	returnMap = this.validChk(searchMap);

        	if((Integer)returnMap.get("ErrorNumber") < 0 ){
                searchMap.addList("returnMap", returnMap);
                return searchMap;
            }
        }
       
        /**********************************
         * 등록/대상자자동등록/수정/삭제
         **********************************/
        if ("ADD".equals(stMode)) {
            searchMap = insertDB(searchMap);
        } else if ("AUTO".equals(stMode)) {
        	searchMap = insertAutoDB(searchMap);
        }
        
        /**********************************
         * Return
         **********************************/
        return searchMap;
    }
    
    /**
     * 평가조직별 평가군 자동등록
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap insertAutoDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
     try {
          setStartTransaction(); 
	        
	      /**********************************
	       * 대상자 자동등록
	       **********************************/
          
            returnMap = insertData("prs.emp.evalGroup.deleteAutoData", searchMap);
		  	returnMap = insertData("prs.emp.evalGroup.insertBranchData", searchMap);
		  	returnMap = insertData("prs.emp.evalGroup.insertHQData", searchMap);
		  	returnMap = insertData("prs.emp.evalGroup.insertAttachedData", searchMap);

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
     * 직원평가 조직별평가군 등록
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap insertDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
        	setStartTransaction();
        	
	        /**********************************
	         * 기존 직원평가조직별평가군 등록
	         **********************************/
        	
        	String[] deptCds = searchMap.getString("deptCds").split("\\|", 0);
        	String[] evalGrpIds = searchMap.getStringArray("evalGrpIds");
        	
	        for (int i = 0; i < deptCds.length; i++) {
        		searchMap.put("deptCd", deptCds[i]);
        		
        		/**********************************
    	         * 기존 직원평가조직별평가군 삭제 
    	         **********************************/
            	returnMap = deleteData("prs.emp.evalGroup.deleteData", searchMap, true);
        		/*
        		if ("-1".equals(evalGrpIds[i]))
        			searchMap.put("evalGrpId", null);
        		else
        			searchMap.put("evalGrpId", evalGrpIds[i]);
	            
	            returnMap = insertData("prs.emp.evalGroup.insertData", searchMap);
	            */
            	
            	if (!"-1".equals(evalGrpIds[i])) {
        			searchMap.put("evalGrpId", evalGrpIds[i]);
        			returnMap = insertData("prs.emp.evalGroup.insertData", searchMap);
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
