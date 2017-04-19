/*************************************************************************
* CLASS 명      : MemDeptEvalUserAction
* 작 업 자      : 유연주
* 작 업 일      : 2017년 03월16일 
* 기    능      : 직원업적평가>부서별평가자관리
* ---------------------------- 변 경 이 력 --------------------------------
* 번호   작 업 자      작   업   일				변 경 내 용        	 비고
* ----  ---------   -----------------  	-------------------------  --------
*   1   유 연 주    2017년 03월16일         	최 초 작 업 
**************************************************************************/
package com.lexken.mem.base;

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
import com.lexken.framework.util.StaticUtil;

public class MemDeptEvalUserAction extends CommonService {
	private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * 평가자관리 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap memDeptEvalUserList(SearchMap searchMap) {
    	
    	// 최상위 평가조직 조회
    	HashMap topScDept = (HashMap)getDetail("mem.base.memDeptEvalUser.getTopDeptInfo", searchMap);

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
    	
    	searchMap.addList("deptTree", getList("mem.base.memDeptEvalUser.getDeptList", searchMap)); //인사조직
    	
    	return searchMap;
    }
    
    /**
     * 평가자관리 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap memDeptEvalUserList_xml(SearchMap searchMap) {
        
        searchMap.addList("list", getList("mem.base.memDeptEvalUser.getList", searchMap));

        return searchMap;
    }
    
    /**
     * 사람찾기 팝업
     * @param
     * @return String
     * @throws
     */
    public SearchMap popSearchUser(SearchMap searchMap) {

    	searchMap.addList("treeList", getList("mem.base.memDeptEvalUser.getRealDeptList", searchMap));

        return searchMap;
    }
    
    /**
     * 사람찾기 팝업
     * @param
     * @return String
     * @throws
     */
    public SearchMap popSearchUser2(SearchMap searchMap) {
    	
    	searchMap.addList("treeList", getList("mem.base.memDeptEvalUser.getRealDeptList", searchMap));
    	
    	return searchMap;
    }
    
    /**
     * 조직별 사용자 리스트 조회
     * @param
     * @return String
     * @throws
     */
    public SearchMap deptUserInfo_ajax(SearchMap searchMap) {

    	searchMap.addList("userList", getList("mem.base.memDeptEvalUser.getUserList", searchMap));

        return searchMap;
    }
    
    /**
     * 평가자관리 등록/수정/삭제
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap memDeptEvalUserProcess(SearchMap searchMap) {
        HashMap returnMap = new HashMap();
        String stMode = searchMap.getString("mode");
        
        /**********************************
         * 등록/수정/삭제
         **********************************/
        if("ADD".equals(stMode)) {        	
        	searchMap = insertDB(searchMap);
        } else if("GET".equals(stMode)){
        	searchMap = insertDeptEvalUserInfo(searchMap);
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
        	String[] deptCds = searchMap.getStringArray("deptCds");
            String[] eval1EmpNos = searchMap.getStringArray("eval1EmpNos");
            String[] eval2EmpNos = searchMap.getStringArray("eval2EmpNos");
            
            for(int i=0; i<eval1EmpNos.length; i++){
            	
            	searchMap.put("deptCd", deptCds[i]);
            	returnMap = deleteData("mem.base.memDeptEvalUser.deleteData", searchMap, true);
            	
            	if(null != eval1EmpNos[i] && !"".equals(eval1EmpNos[i])) {
            		searchMap.put("deptCd", deptCds[i]);
            		searchMap.put("eval1EmpNo", eval1EmpNos[i]);
            		searchMap.put("eval2EmpNo", eval2EmpNos[i]);
            		
            		returnMap = insertData("mem.base.memDeptEvalUser.insertData", searchMap);
            		
            	}
            }
            
            //insertDeptEvalUserAdmin(searchMap); //평가가 권한 부여
            
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
     * 평가자 가져오기
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap insertDeptEvalUserInfo(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
	        setStartTransaction();
	        
	        returnMap = deleteData("mem.base.memDeptEvalUser.deleteDeptEvalUserInfo", searchMap, true);
	        
	        returnMap = insertData("mem.base.memDeptEvalUser.insertDeptEvalUserInfo", searchMap);
	        
	        //insertDeptEvalUserAdmin(searchMap); //평가가 권한 부여
	        
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
     * 개인업적 평가자 등록
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap insertDeptEvalUserAdmin(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();        
        try {
            returnMap = deleteData("mem.base.memDeptEvalUser.deleteAdminDatas", searchMap, true);//권한삭제
            returnMap = insertData("mem.base.memDeptEvalUser.insertAdminDatas", searchMap);//권한등록    
            
        } catch (Exception e) {
        	logger.error(e.toString());
        	setRollBackTransaction();
        	returnMap.put("ErrorNumber", ErrorMessages.FAILURE_PROCESS_CODE);
			returnMap.put("ErrorMessage", ErrorMessages.FAILURE_PROCESS_MESSAGE);
        } finally {
        }
        
        /**********************************
         * Return
         **********************************/
        searchMap.addList("returnMap", returnMap);
        return searchMap;    
    } 
    
    /**
     * 평가자관리 엑셀변환다운로드
     * @param
     * @return String
     * @throws
     */
    public SearchMap memDeptEvalUserExcel(SearchMap searchMap) {
    	String excelFileName = "부서별평가자관리";
    	String excelTitle = "부서별평가자관리목록";
    	
    	/************************************************************************************
    	 * 조회조건 설정
    	 ************************************************************************************/
    	ArrayList<ExcelVO> excelSearchInfoList = new ArrayList<ExcelVO>();
    	
    	excelSearchInfoList.add(new ExcelVO(StringConstants.YEAR, (String)searchMap.get("yearNm")));
    	
    	/****************************************************************************************************
    	 * 엑셀 다운로드 설정 : '헤더명', '컬럼명', '셀정렬(left, center, right)', 'ROWSPAN COLUMN', '셀너비'
    	 ****************************************************************************************************/
    	ArrayList<ExcelVO> excelInfoList = new ArrayList<ExcelVO>();
    	excelInfoList.add(new ExcelVO("부서코드", "DEPT_CD", "center"));
    	excelInfoList.add(new ExcelVO("소속부서", "DEPT_KOR_NM", "left"));
    	excelInfoList.add(new ExcelVO("1차평가자사번", "EVAL_1_EMP_NO", "center"));
    	excelInfoList.add(new ExcelVO("1차평가자성명", "EVAL_1_EMP_NM", "center"));
    	excelInfoList.add(new ExcelVO("2차평가자사번", "EVAL_2_EMP_NO", "center"));
    	excelInfoList.add(new ExcelVO("2차평가자성명", "EVAL_2_EMP_NM", "center"));

    	searchMap.put("excelFileName", excelFileName);
    	searchMap.put("excelTitle", excelTitle);
    	searchMap.put("excelSearchInfoList", excelSearchInfoList);
    	searchMap.put("excelInfoList", excelInfoList);
    	
    	searchMap.put("excelDataList", (ArrayList)getList("mem.base.memDeptEvalUser.getList", searchMap));
    	
    	return searchMap;
    	
    }
}

