/*************************************************************************
* CLASS 명      : DirEvalMemberAction
* 작 업 자      : 방승현
* 작 업 일      : 2013년 06월 24일 
* 기    능      : 임원평가-평가대상자 선정
* ---------------------------- 변 경 이 력 --------------------------------
* 번호   작 업 자      작   업   일        변 경 내 용              비고
* ----  ---------  -----------------  -------------------------    --------
*   1    방 승 현      2013년 06월 25일    최 초 작 업 
**************************************************************************/
package com.lexken.prs.dir;

import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lexken.framework.common.ErrorMessages;
import com.lexken.framework.common.SearchMap;
import com.lexken.framework.core.CommonService;
import com.lexken.framework.util.StaticUtil;

public class DirEvalMemberAction extends CommonService {
	private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
//    private final EvalGrpAssessorAction action = new EvalGrpAssessorAction();
    
    /**
     * 평가대상자 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap dirEvalMemberList(SearchMap searchMap) {
    	
    	// 최상위 평가조직 조회
    	HashMap topScDept = (HashMap)getDetail("prs.dir.dirEvalMember.getTopDeptInfo", searchMap);

    	// 파라미터가 null로 들어올 때 디폴트값 셋팅.
    	if (topScDept == null) {
    		topScDept = new HashMap();
    		topScDept.put("DEPT_CD", "");
    		topScDept.put("DEPT_KOR_NM", "");
    	}
    	
    	// 파라미터가 null로 들어올 때 디폴트값 셋팅.
    	String findDeptCd =  StaticUtil.nullToDefault((String)searchMap.getString("findDeptCd"), (String)topScDept.get("DEPT_CD"));	// 조직코드가 없으면 전사조직코드를 셋팅.
    	String findUpDeptName =  StaticUtil.nullToDefault((String)searchMap.getString("findUpDeptName"), (String)topScDept.get("DEPT_KOR_NM")) ; ;	// 조직명이 없으면 전사조직명을 셋팅.
    	
    	// 디폴트 조회조건 설정
    	searchMap.put("findDeptCd", findDeptCd);	// 검색버튼 조회시 조직트리에 기존 선택한 조직이 표시되도록 설정.(findSearchCodeId에 넣어주면 우선 적용됨. 트리와 검색조건을 별도로 사용할 경우에 한함.)
    	searchMap.put("findUpDeptName", findUpDeptName);
    	
    	searchMap.addList("evalYn", getDetail("prs.dir.dirFinalEval.getList", searchMap));// 마감여부
    	
    	return searchMap;
    }
    
    /**
     * 평가대상자 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap dirEvalMemberList_xml(SearchMap searchMap) {
        
        searchMap.addList("list", getList("prs.dir.dirEvalMember.getList", searchMap));

        return searchMap;
    }
    
    /**
     * 평가대상자선정 등록/수정/삭제
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap dirEvalMemberProcess(SearchMap searchMap) {
        HashMap returnMap = new HashMap();
        String stMode = searchMap.getString("mode");
        
        /**********************************
         * 등록/수정/삭제
         **********************************/
        if("MOD".equals(stMode)) {        	
        	searchMap = insertDB(searchMap);
        } else if("GET".equals(stMode)) {
            searchMap = insertInsaInfo(searchMap);
        }
        
         return searchMap;
    }
    
    /**
     * 평가대상자 선정
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap insertDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
        	setStartTransaction();
            String[] empns = searchMap.getStringArray("empns");
            
            for(int i=0; i<empns.length; i++){
            	if(null != empns[i]){
            		searchMap.put("empn", empns[i]);
            		
            		String evalYn = searchMap.getString("evalYn" + (i + 1));
            		
            		searchMap.put("evalYn", evalYn);
            		
            		String empn = empns[i];
            		
        			if(!"".equals(empn) ){	
        			
        				returnMap = updateData("prs.dir.dirEvalMember.updateEmpMemberData", searchMap);
        				
        			}
            	}
            }
            insertadminDirEvalInfo(searchMap); //권한 부여 작업
            
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
     * 임원평가대상자 가져오기
     * @param      
     * @return String  
     * @throws 
     */ 
    public SearchMap insertInsaInfo(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        SearchMap retutnMap2 = null;

        try {
	        setStartTransaction();
	        
	        returnMap = deleteData("prs.dir.dirEvalMember.deleteEmpMemberInfo", searchMap, true); //평가 대상자 삭제
	        returnMap = insertData("prs.dir.dirEvalMember.insertEmpMemberInfo", searchMap);
	        returnMap = updateData("prs.dir.dirEvalMember.updateEvalyn", searchMap);
	        
	        insertadminDirEvalInfo(searchMap); //권한 부여 작업
	        
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
     * 임원 평가대상자 권한 등록
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap insertadminDirEvalInfo(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
        	setStartTransaction();
        
        	returnMap = deleteData("prs.dir.dirEvalMember.deleteAdminData", searchMap, true);
        	returnMap = insertData("prs.dir.dirEvalMember.insertAdminData", searchMap);
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
}
