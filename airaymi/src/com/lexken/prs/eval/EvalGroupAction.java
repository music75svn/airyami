/*************************************************************************
* CLASS 명      : EvalGroupAction
* 작 업 자      : 박경태
* 작 업 일      : 2012년 11월 26일 
* 기    능      : 평가군
* ---------------------------- 변 경 이 력 --------------------------------
* 번호   작 업 자      작   업   일        변 경 내 용              비고
* ----  ---------  -----------------  -------------------------    --------
*   1    박경태      2012년 11월 26일         최 초 작 업 
**************************************************************************/
package com.lexken.prs.eval;
    
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
     * 평가군 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap evalGroupList(SearchMap searchMap) {
    	
    	/**********************************
         * 마감,확정 조회
         **********************************/
    	searchMap.addList("yearCheck", getDetail("prs.eval.evalSchedule.getYearCheck", searchMap));		//해당년도 일정입력체크
    	searchMap.addList("mngClosing", getDetail("prs.mng.evalGrpAssessor.getClosingMng", searchMap)); //간부개인평가일정 체크
    	searchMap.addList("mngEvalYn", getDetail("prs.mng.evalResult.getMngEvalClosing", searchMap));//마감여부(간부개인평가)
    	return searchMap;
    }
    
    /**
     * 평가군 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap evalGroupList_xml(SearchMap searchMap) {
    	
        
        searchMap.addList("list", getList("prs.eval.evalGroup.getList", searchMap));

        return searchMap;
    }
    
    /**
     * 평가군 상세화면
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap evalGroupModify(SearchMap searchMap) {
    	String stMode = searchMap.getString("mode");
    	searchMap.addList("evalMethodList", getList("prs.eval.evalGroup.getMethodList", searchMap));
    	if("MOD".equals(stMode)) {
    		searchMap.addList("detail", getDetail("prs.eval.evalGroup.getDetail", searchMap));
    	}
        
        return searchMap;
    }
    
    /**
     * 평가자 추가화면
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap evalGrpUserModify(SearchMap searchMap) {
    	
    	/**********************************
         * 선택 평가구분 명조회
         **********************************/
    	searchMap.addList("evalDegreeDetail", getDetail("prs.eval.evalGroup.getDegreeDetail", searchMap));
    	searchMap.addList("evalDetail", getDetail("prs.eval.evalGroup.getDetail", searchMap));
    	
    	searchMap.addList("treeList", getList("bsc.module.commModule.getDeptList", searchMap));
    	searchMap.addList("userList", getList("prs.eval.evalGroup.getUserList", searchMap));
        
        return searchMap;
    }
    /**
     * 관리여부 
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap evalGroupDept(SearchMap searchMap) {
    	String stMode = searchMap.getString("mode");
    	
    	if(!"".equals(StaticUtil.nullToBlank((String)searchMap.get("evalGrpId")))) {
			searchMap.put("findEvalGrpId", (String)searchMap.get("evalGrpId"));
		}
    	
    	if("".equals(StaticUtil.nullToBlank((String)searchMap.get("year")))) {
			searchMap.put("year", (String)searchMap.get("findYear"));
		}
    	
    	if(!"".equals(StaticUtil.nullToBlank((String)searchMap.get("evalGrpNm")))) {
			searchMap.put("findEvalGrpNm", (String)searchMap.get("evalGrpNm"));
		}
    	
    	// 최상위 인사조직 조회
    	HashMap topDept = (HashMap)getDetail("prs.eval.evalGroup.getTopDeptInfo", searchMap);
    	
    	if(StaticUtil.isEmpty(topDept)) {
    		topDept = new HashMap();
    	}
    	
    	String topDeptId = (String)topDept.get("DEPT_CD");	// 조직트리구성할 때 li태그에 id를 조직코드로 만드는데 id는  처음에 숫자가 나오면 안되므로 문자를 붙여줌.(실조직인 경우 숫자가 많음.) 
    	
    	// 파라미터가 null로 들어올 때 디폴트값 셋팅.
    	String findDeptId = (String)searchMap.getString("findDeptId", topDeptId);									// 인사조직코드가 없으면 전사실조직코드를 셋팅.
    	
    	// 디폴트 조회조건 설정
    	searchMap.put("findDeptId", findDeptId);						// 검색버튼 조회시 실조직트리에 기존 선택한 조직이 표시되도록 설정.
		searchMap.put("tree2CheckboxYn", "true");
    	searchMap.addList("treeList2", getList("prs.eval.evalGroup.getDeptMappingTreeList", searchMap));	// 인사조직 미사용트리
    	
		ArrayList list = new ArrayList();
		searchMap.addList("mappingScDeptList", list);		// 성과조직트리에서 선택한 조직
		searchMap.addList("mappingDeptList", getList("prs.eval.evalGroup.getMappingDeptList", searchMap));		// 성과조직에 매핑된 실조직 리스트 조회
        
        return searchMap;
    }
    
    /**
     * 관리여부 등록/수정/삭제
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap evalGroupDeptProcess(SearchMap searchMap) {
        HashMap returnMap = new HashMap();
        String stMode = searchMap.getString("mode");
        
        /**********************************
         * 무결성 체크
         **********************************/
        if(!"DEL".equals(stMode) && !"MAPPING".equals(stMode)) {
            returnMap = this.validChk(searchMap);
            
            if((Integer)returnMap.get("ErrorNumber") < 0 ){
                searchMap.addList("returnMap", returnMap);
                return searchMap;
            }
        }
        
        /**********************************
         * 등록/수정/삭제
         **********************************/
        if("MAPPING".equals(stMode)) {
        	searchMap = updateDBScDeptMapping(searchMap);
        } 
        
        /**********************************
         * Return
         **********************************/
        return searchMap;
    }
    
    /**
     * 성과조직매핑관리 수정
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap updateDBScDeptMapping(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();
        int totCnt = 0;
        try {
        	
        	String scDeptMappingId = searchMap.getString("scDeptMappingIds");
        	String userId = searchMap.getString("findUserId");
        	String[] scDeptMappingIds = null;
        	
        	if(!"".equals(scDeptMappingId)){
        		scDeptMappingIds = searchMap.getString("scDeptMappingIds").split("\\|", 0);
        	}
        	
	        setStartTransaction();
	        
	        returnMap = deleteData("prs.eval.evalGroup.deleteScDeptMappingData", searchMap, true);
	        if( scDeptMappingIds != null) {
	        	for (int i = 0; i < scDeptMappingIds.length; i++) {
	        		searchMap.put("scDeptMappingId", scDeptMappingIds[i]);
	        		searchMap.put("userId", userId);
		            returnMap = insertData("prs.eval.evalGroup.insertScDeptMappingData", searchMap);
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
     * 평가군 등록/수정/삭제
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
        if(!"DEL".equals(stMode) && !"MODUSER".equals(stMode) ) {
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
        } else if("MODUSER".equals(stMode)) {
            searchMap = updateUserDB(searchMap);
        }
        
        /**********************************
         * Return
         **********************************/
        return searchMap;
    }
    
    /**
     * 평가군 등록
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap insertDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
        	setStartTransaction();
        
        	returnMap = insertData("prs.eval.evalGroup.insertData", searchMap);
        
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
     * 평가군 수정
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap updateDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
	        setStartTransaction();
	        
	        returnMap = updateData("prs.eval.evalGroup.updateData", searchMap);
	        
	        
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
     * 평가단-평가자 매핑 수정
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap updateUserDB(SearchMap searchMap) {
    	HashMap returnMap = new HashMap(); 
	    
	    try {
	    	String evalDegreeId  = searchMap.getString("evalDegreeId");
	    	String evalGrpId 	 = searchMap.getString("evalGrpId");
			String[] evalUserIds = searchMap.getString("evalUserIds").split("\\|", 0);
	        
	        setStartTransaction();
	        
	        searchMap.put("evalDegreeId", evalDegreeId);
	        searchMap.put("evalGrpId", evalGrpId);
	        
	        returnMap = updateData("prs.eval.evalGroup.deleteUserData", searchMap, true);
	        
	        if(!searchMap.getString("evalUserIds").equals("")){
		        if(null != evalUserIds && 0 < evalUserIds.length) {
			    	for (int i = 0; i < evalUserIds.length; i++) {
			    		searchMap.put("evalDegreeId", evalDegreeId);
			    		searchMap.put("evalGrpId", evalGrpId);
			            searchMap.put("evalUserId", evalUserIds[i]);
			            returnMap = insertData("prs.eval.evalGroup.insertUserData", searchMap);
			        }
			    }
	        }
	        
	        returnMap = updateData("prs.eval.evalGroup.deleteAdminData", searchMap, true);//권한삭제
	        
	        returnMap = insertData("prs.eval.evalGroup.insertAdminData", searchMap);//권한등록
	        
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
     * 평가군 삭제 
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap deleteDB(SearchMap searchMap) {
        
        HashMap returnMap = new HashMap(); 
	    
	    try {
	        String[] evalDegreeIds = searchMap.getString("evalDegreeIds").split("\\|", 0);
			String[] evalGrpIds = searchMap.getString("evalGrpIds").split("\\|", 0);
	        
	        setStartTransaction();
	        
	        if(null != evalDegreeIds && 0 < evalDegreeIds.length) {
		        for (int i = 0; i < evalDegreeIds.length; i++) {
		            searchMap.put("evalDegreeId", evalDegreeIds[i]);
			searchMap.put("evalGrpId", evalGrpIds[i]);
		            returnMap = updateData("prs.eval.evalGroup.deleteData", searchMap);
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
        String stMode = searchMap.getString("mode");
        
    	returnMap = ValidationChk.lengthCheck(searchMap.getString("evalGrpNm"), "평가군", 1, 50);
		if((Integer)returnMap.get("ErrorNumber") < 0) {
			return returnMap;
		}
		returnMap = ValidationChk.checkCommaPointNumber(searchMap.getString("sortOrder"), "정렬순서");
		if((Integer)returnMap.get("ErrorNumber") < 0) {
			return returnMap;
		}
    		
        returnMap.put("ErrorNumber",  resultValue);
        return returnMap;        
    }
}
