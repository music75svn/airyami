/*************************************************************************
* CLASS 명      : EvalGrpAssessorAction
* 작 업 자      : 박선혜
* 작 업 일      : 2013년 06월 04일 
* 기    능      : 개인업적 평가자 선정
* ---------------------------- 변 경 이 력 --------------------------------
* 번호   작 업 자      작   업   일        변 경 내 용              비고
* ----  ---------  -----------------  -------------------------    --------
*   1    박선혜      2013년 06월 04일         최 초 작 업 
**************************************************************************/
package com.lexken.prs.mng;
    
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
import com.lexken.framework.login.LoginVO;
import com.lexken.framework.util.StaticUtil;

public class EvalGrpAssessorAction extends CommonService {

    private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * 간부 개인업적 평가자 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap evalGrpAssessorList(SearchMap searchMap) {
    	

    	searchMap.addList("mngClosing", getDetail("prs.mng.evalGrpAssessor.getClosingMng", searchMap));
    	
    	return searchMap;
    }

    /**
     * 간부 개인업적 평가자 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap evalGrpAssessorList_xml(SearchMap searchMap) {
        
        searchMap.addList("list", getList("prs.mng.evalGrpAssessor.getList", searchMap));

        return searchMap;
    }
    
    /**
     * 간부 개인업적 평가자 상세화면
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap evalGrpAssessorModify(SearchMap searchMap) {
		searchMap.addList("evalGrpUseDetail", getDetail("prs.mng.evalGrpAssessor.getDetail", searchMap)); 
		searchMap.addList("deptTree", getList("prs.mng.evalMng.getDeptList", searchMap)); //인사조직    	
    	searchMap.addList("userList", getList("prs.mng.evalGrpAssessor.getUseList", searchMap));	
    	searchMap.addList("mngEvalYn", getDetail("prs.mng.evalResult.getMngEvalClosing", searchMap));//마감여부(개인평가)
        
        return searchMap;
    }
   
    /**
     * 간부 개인업적 평가자 등록/수정/삭제
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap evalGrpAssessorProcess(SearchMap searchMap) {
        HashMap returnMap = new HashMap();
        String stMode = searchMap.getString("mode");

        /**********************************
         * 등록/삭제
         **********************************/
        if("ADD".equals(stMode)) {
            searchMap = insertAssessorDB(searchMap);
        }
        
        /**********************************
         * Return
         **********************************/
        return searchMap;
    }
    
    /**
     * 간부 별도평가군 대상자관리 등록
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap insertAssessorDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        setStartTransaction();
        
        String empned    = searchMap.getString("empns");
        String evalGrpId    = searchMap.getString("evalGrpId");
        String userId = searchMap.getString("findUserId");
        String[] empns = searchMap.getString("empns").split("\\|", 0);
        
    	searchMap.put("evalGrpId", evalGrpId);
    	searchMap.put("userId", userId);
    	returnMap = deleteData("prs.mng.evalGrpAssessor.deleteGrpAssessorData", searchMap, true); //기존 평가군별 평가자 삭제
    	
        if(!"".equals(empned)) {
	        for (int i = 0; i < empns.length; i++) {
	        	searchMap.put("empn", empns[i]);
	            returnMap = insertData("prs.mng.evalGrpAssessor.insertGrpAssessorData", searchMap);//대상자별 평가자 추가
	        }
        }
        
        setEndTransaction();
        
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
