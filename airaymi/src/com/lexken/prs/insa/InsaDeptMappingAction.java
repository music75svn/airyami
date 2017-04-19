/*************************************************************************
* CLASS 명      : insaMappingModify
* 작 업 자      : 안요한 
* 작 업 일      : 2013년 05월 27일 
* 기    능      : 인사조직매핑관리    
* ---------------------------- 변 경 이 력 --------------------------------
* 번호    작 업 자    작     업     일        변 경 내 용                비고
* ----  --------  -----------------  -------------------------  --------
*  1    안요한      2013년 05월 27일             최 초 작 업 
**************************************************************************/
package com.lexken.prs.insa;
    
import java.awt.List;
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

public class InsaDeptMappingAction extends CommonService {

    private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * 인사조직매핑관리 상세화면
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap insaMappingModify(SearchMap searchMap) {
    	String stMode = searchMap.getString("mode");
    	
    	// 최상위 인사조직 조회
    	HashMap topScDept = (HashMap)getDetail("prs.insa.insaDeptMapping.getTopDeptInfo", searchMap);
    	HashMap topDept = (HashMap)getDetail("prs.insa.insaDeptMapping.getTopDeptInfo", searchMap);
    	
    	if(StaticUtil.isEmpty(topDept)) {
    		topDept = new HashMap();
    	}
    	
    	if(StaticUtil.isEmpty(topScDept)) {
    		topScDept = new HashMap();
    	}
    	
    	String topDeptId = (String)topDept.get("DEPT_CD");	// 조직트리구성할 때 li태그에 id를 조직코드로 만드는데 id는  처음에 숫자가 나오면 안되므로 문자를 붙여줌.(실조직인 경우 숫자가 많음.) 
    	
    	// 파라미터가 null로 들어올 때 디폴트값 셋팅.
    	String findInsaDeptId = (String)searchMap.getString("findInsaDeptId", (String)topScDept.get("DEPT_CD"));	// 인사조직코드가 없으면 전사성과조직코드를 셋팅.
    	String findDeptId = (String)searchMap.getString("findDeptId", topDeptId);									// 인사조직코드가 없으면 전사실조직코드를 셋팅.
    	
    	// 디폴트 조회조건 설정
    	searchMap.put("findInsaDeptId", findInsaDeptId);					// 검색버튼 조회시 성과조직트리에 기존 선택한 조직이 표시되도록 설정.(findSearchCodeId에 넣어주면 우선 적용됨. 트리와 검색조건을 별도로 사용할 경우에 한함.)
    	searchMap.put("findDeptId", findDeptId);						// 검색버튼 조회시 실조직트리에 기존 선택한 조직이 표시되도록 설정.
    	
		searchMap.put("treeCheckboxYn", "false");
		searchMap.put("tree2CheckboxYn", "true");
        
    	searchMap.addList("treeList", getList("prs.insa.insaDeptMapping.getInsaDeptMappingTreeList", searchMap));	// 인사조직 사용트리
    	searchMap.addList("treeList2", getList("prs.insa.insaDeptMapping.getDeptMappingTreeList", searchMap));	// 인사조직 미사용트리
    	
    		ArrayList list = new ArrayList();
    		HashMap dept = new HashMap();
    		dept.put("SC_DEPT_CD", findInsaDeptId);
    		list.add(dept);
    		searchMap.addList("mappingScDeptList", list);		// 성과조직트리에서 선택한 조직
    		searchMap.addList("mappingDeptList", getList("prs.insa.insaDeptMapping.getMappingDeptList", searchMap));		// 성과조직에 매핑된 실조직 리스트 조회
        
        return searchMap;
    }
    
    /**
     * 성과조직매핑관리 등록/수정/삭제
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap insaMappingProcess(SearchMap searchMap) {
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
        	String[] scDeptMappingIds = null;
        	if(!"".equals(scDeptMappingId)){
        		scDeptMappingIds = searchMap.getString("scDeptMappingIds").split("\\|", 0);
        	}
        	
	        setStartTransaction();
	        
	        returnMap = deleteData("prs.insa.insaDeptMapping.deleteScDeptMappingData", searchMap, true);
	        if( scDeptMappingIds != null) {
		        for (int i = 0; i < scDeptMappingIds.length; i++) {
	        		searchMap.put("scDeptMappingId", scDeptMappingIds[i]);
	        		int cnt = getInt("prs.insa.insaDeptMapping.selectDeptMappingData", searchMap);
	        		
	        		totCnt += cnt;
	        	}
		        
		        if(0 < totCnt) {
		        	setRollBackTransaction();
		        	returnMap.put("ErrorNumber", ErrorMessages.FAILURE_DUP2_CODE);
		            returnMap.put("ErrorMessage", ErrorMessages.format(ErrorMessages.FAILURE_DUP2_MESSAGE, "인사조직의"));
		        }else{
		        	for (int i = 0; i < scDeptMappingIds.length; i++) {
		        		searchMap.put("scDeptMappingId", scDeptMappingIds[i]);
			            returnMap = insertData("prs.insa.insaDeptMapping.insertScDeptMappingData", searchMap);
			        }
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
        
        //Validation 체크 추가
        
        returnMap.put("ErrorNumber",  resultValue);
        return returnMap;        
    }
    
}
