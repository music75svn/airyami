/*************************************************************************
* CLASS 명      : DeptAction
* 작 업 자      : 김상용
* 작 업 일      : 2013년 05월 08일 
* 기    능      : 인사조직
* ---------------------------- 변 경 이 력 --------------------------------
* 번호   작 업 자      작   업   일				변 경 내 용        	 비고
* ----  ---------   -----------------  	-------------------------  --------
*   1    김 상 용    2013년 05월 08일        	최 초 작 업 
**************************************************************************/
package com.lexken.prs.insa;

import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import sun.security.util.Debug;

import com.lexken.framework.common.ErrorMessages;
import com.lexken.framework.common.SearchMap;
import com.lexken.framework.common.ValidationChk;
import com.lexken.framework.core.CommonService;
import com.lexken.framework.util.StaticUtil;

public class DeptAction extends CommonService {
	private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * 인사조직 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap deptList(SearchMap searchMap) {
    	
    	// 최상위 평가조직 조회
    	HashMap topScDept = (HashMap)getDetail("prs.insa.dept.getTopDeptInfo", searchMap);


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

    	searchMap.addList("deptTree", getList("prs.insa.dept.getList", searchMap));
    	
        return searchMap;
    }
    
    /**
     * 인사조직관리 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap deptList_xml(SearchMap searchMap) {
    	
        searchMap.addList("list", getList("prs.insa.dept.getListData", searchMap));

        return searchMap;
    }
    
    /**
     * 인사조직 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap deptListData(SearchMap searchMap) {
    	searchMap.addList("detail", getDetail("prs.insa.dept.getDeptListData", searchMap));
    	
        return searchMap;
    }
    
    /**
     * 인사조직 등록/수정/삭제
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap deptProcess(SearchMap searchMap) {
        HashMap returnMap = new HashMap();
        String stMode = searchMap.getString("mode");
        String upDeptCd = searchMap.getString("upDeptCd");
        
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
        } else if("GET".equals(stMode)) {
            searchMap = insertInsaInfo(searchMap);
//        } else if("SORT_MOD".equals(stMode)) {
//            searchMap = updateMenuSortDB(searchMap);
        }
        
        /**********************************
         * Return
         **********************************/
         return searchMap;
    }
    
    /**
     * 인사조직관리 상세화면
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap deptModify(SearchMap searchMap) {
    	String mode = (String)searchMap.get("mode");
    	String upDeptCd = (String)searchMap.get("upDeptCd");
    	
    	// 신규 등록 및 미사용조직 수정시 조직트리에 상위조직 설정.
    	searchMap.put("findSearchCodeId", upDeptCd);    		
    	
    	if("ADD".equals(mode)) {
    		searchMap.put("deptCd", "");					// 신규 등록시 조직코드 초기화.
    	}
    	
    	searchMap.addList("deptTree", getList("prs.insa.dept.getList", searchMap));
    	
        searchMap.addList("detail", getDetail("prs.insa.dept.getDeptListData", searchMap));
        
        return searchMap;
    }
    
    /**
     * 인사조직가져오기 등록
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap insertInsaInfo(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
	        setStartTransaction();
	        
	        returnMap = insertData("prs.insa.dept.insertInsaInfo", searchMap);
	        
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
     * 인사조직 등록
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap insertDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
	        setStartTransaction();
	        
	        if (getInt("prs.insa.dept.getDeptCdCount", searchMap) > 0) {
	        	returnMap.put("ErrorNumber", ErrorMessages.FAILURE_CHK_DUPLICATED);
				returnMap.put("ErrorMessage", ErrorMessages.FAILURE_CHK_DUPLICATED_MESSAGE);
	        }else { 
	         
	        	returnMap = insertData("prs.insa.dept.insertData", searchMap);
	        }
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
     * 인사조직 수정
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap updateDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
	        setStartTransaction();
	        
	        returnMap = updateData("prs.insa.dept.updateData", searchMap);
	        
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
     * 인사조직 삭제 
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap deleteDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
        	String deptCds = searchMap.getString("deptCds");
	        String[] keyArray = deptCds.split("\\|", 0);
	        
	        setStartTransaction();
	        
	        for (int i=0; i<keyArray.length; i++) {
	            searchMap.put("deptCd", keyArray[i]);
	            returnMap = updateData("prs.insa.dept.deleteData", searchMap);
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
        if( "ADD".equals(searchMap.get("mode")) ){
            
        	returnMap = ValidationChk.lengthCheck(searchMap.getString("deptCd"), "조직코드", 1, 4);
            if((Integer)returnMap.get("ErrorNumber") < 0 ){
            	return returnMap;
            }
            
            returnMap = ValidationChk.lengthCheck(searchMap.getString("upDeptCd"), "상위조직코드", 1, 4);
            if((Integer)returnMap.get("ErrorNumber") < 0 ){
            	return returnMap;
            }
            
        } 
        
        returnMap.put("ErrorNumber",  resultValue);
        return returnMap;        
    }
}
