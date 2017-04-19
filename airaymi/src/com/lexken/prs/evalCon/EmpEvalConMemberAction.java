/*************************************************************************
* CLASS 명      : EmpMemberAction
* 작 업 자      : 방승현
* 작 업 일      : 2013년 06월 24일 
* 기    능      : 직원개인평가-평가대상자 선정
* ---------------------------- 변 경 이 력 --------------------------------
* 번호   작 업 자      작   업   일        변 경 내 용              비고
* ----  ---------  -----------------  -------------------------    --------
*   1    방 승 현      2013년 06월 24일    최 초 작 업 
**************************************************************************/
package com.lexken.prs.evalCon;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lexken.framework.common.ErrorMessages;
import com.lexken.framework.common.ExcelVO;
import com.lexken.framework.common.SearchMap;
import com.lexken.framework.common.StringConstants;
import com.lexken.framework.core.CommonService;
import com.lexken.framework.util.StaticUtil;

public class EmpEvalConMemberAction extends CommonService {
	private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    //private final EvalGrpAssessorAction action = new EvalGrpAssessorAction();
    
    /**
     * 평가대상자 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap empEvalConMemberList(SearchMap searchMap) {
    	
    	// 최상위 평가조직 조회
    	HashMap topScDept = (HashMap)getDetail("prs.evalCon.empEvalConMember.getTopDeptInfo", searchMap);

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
    	
    	searchMap.addList("deptTree", getList("prs.evalCon.empEvalConMember.getDeptList", searchMap)); //인사조직
    	
    	return searchMap;
    }
    
    /**
     * 평가대상자 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap empEvalConMemberList_xml(SearchMap searchMap) {
        
        searchMap.addList("list", getList("prs.evalCon.empEvalConMember.getList", searchMap));

        return searchMap;
    }
    
    /**
     * 평가대상자 상세화면
     * @param
     * @return String
     * @throws
     */
    public SearchMap evalMngModify(SearchMap searchMap) {
    	
    	String stMode = searchMap.getString("mode");
    	
    	if("MOD".equals(stMode)) {
    		searchMap.addList("detail", getDetail("prs.evalCon.empEvalConMember.getDetail", searchMap));
    	}
    	//searchMap.addList("empnList", getList("prs.evalCon.empEvalConMember.getEmpnList", searchMap));
        searchMap.addList("evalGrpList", getList("prs.mngCon.scoreUpload.getEvalGrpId", searchMap));
        return searchMap;
    }
    
    /**
     * 사람찾기 팝업(v_deptinfo)기준
     * @param
     * @return String
     * @throws
     */
    public SearchMap popSearchUser(SearchMap searchMap) {

    	searchMap.addList("treeList", getList("bsc.module.commModule.getDeptList", searchMap));
    	searchMap.addList("empnList", getList("prs.evalCon.empEvalConMember.getEmpnList", searchMap));
        return searchMap;
    }
    
    
    /**
     * 평가대상자선정 등록/수정/삭제
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap empEvalConMemberProcess(SearchMap searchMap) {
        HashMap returnMap = new HashMap();
        String stMode = searchMap.getString("mode");
        
        /**********************************
         * 등록/수정/삭제
         **********************************/
        if("ADD".equals(stMode)) {        	
        	searchMap = insertMemberDB(searchMap);
        } else if("MOD".equals(stMode)) {
        	searchMap = updateDB(searchMap);
        } else if("DEL".equals(stMode)) {        	
        	searchMap = deleteDB(searchMap);
        } else if("SAVE".equals(stMode)) {        	
        	searchMap = insertDB(searchMap);
        } else if("GET".equals(stMode)) {
            searchMap = insertInsaInfo(searchMap);
        }
        
         return searchMap;
    }
    
    /**
     * 평가대상자  입력
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap insertMemberDB(SearchMap searchMap) {
    	HashMap returnMap    = new HashMap();   
    	 String managerUserId = searchMap.getString("managerUserId");
        
        try {
        	setStartTransaction();
            
        	returnMap = insertData("prs.evalCon.empEvalConMember.insertData", searchMap);
        	returnMap = insertData("prs.evalCon.empEvalConMember.insertEvalGrpData", searchMap);
        	if(!"".equals(managerUserId)){
        	returnMap = insertData("prs.evalCon.empEvalConMember.insertAssessorData", searchMap);
        	}
	        searchMap.put("adminGubun", "30");
	        returnMap = deleteData("prs.evalCon.empEvalConMember.deleteAdminData", searchMap, true);
        	returnMap = insertData("prs.evalCon.empEvalConMember.insertAdminData", searchMap);
        	if(!"".equals(managerUserId)){
	        	searchMap.put("adminGubun", "31");
	        	returnMap = deleteData("prs.evalCon.empEvalConMember.deleteAdminData", searchMap, true);
	        	returnMap = insertData("prs.evalCon.empEvalConMember.insertAdminData", searchMap);
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
        	
        	int cnt = getInt("prs.evalCon.empEvalConMember.getMngCnt", searchMap);
        	
        	if(cnt>0){
        		returnMap = deleteData("prs.evalCon.empEvalConMember.deleteMngData", searchMap);
        	}
        	
            for(int i=0; i<empns.length; i++){
            	if(null != empns[i]){
            		searchMap.put("empn", empns[i]);
            		
            		//String evalYn = searchMap.getString("evalYn" + (i + 1));
            		String evalYn = searchMap.getString("evalYn" + empns[i]);
            		
            		searchMap.put("evalYn", evalYn);
            		
            		String empn = empns[i];
            		
        			if(!"".equals(empn) ){	
        				returnMap = updateData("prs.evalCon.empEvalConMember.updateEmpMemberData", searchMap);
        				returnMap = insertData("prs.evalCon.empEvalConMember.updateMngMemberData", searchMap);
        			}
            	}
            }
            
            returnMap = deleteData("prs.evalCon.empEvalConMember.deleteAdminDatas", searchMap, true);//권한삭제
            returnMap = insertData("prs.evalCon.empEvalConMember.insertAdminDatas", searchMap);//권한등록    
            /*
            insertprsEmpMemberAdmin(searchMap); //평가가 권한 부여
            */
            
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
     * 간부평가대상자 수정
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap updateDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap(); 
        String managerUserId = searchMap.getString("managerUserId");
        
        try {
            setStartTransaction();
            
            returnMap = updateData("prs.evalCon.empEvalConMember.updateData", searchMap);
            returnMap = insertData("prs.evalCon.empEvalConMember.insertEvalGrpData", searchMap);
        	if(!"".equals(managerUserId)){
        		returnMap = insertData("prs.evalCon.empEvalConMember.insertAssessorData", searchMap);
        	}            
          
        	if(!"".equals(managerUserId)){
	            searchMap.put("adminGubun", "31");
	        	returnMap = deleteData("prs.evalCon.empEvalConMember.deleteAdminData", searchMap, true);
	        	returnMap = insertData("prs.evalCon.empEvalConMember.insertAdminData", searchMap);
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
     * 개인평가 가져오기
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap insertInsaInfo(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        SearchMap retutnMap2 = null;

        try {
	        setStartTransaction();
	        
	        returnMap = insertData("prs.evalCon.empEvalConMember.callSpPrsEmpMemberProc", searchMap);
	        
	        insertprsEmpMemberAdmin(searchMap); //평가가 권한 부여
	        
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
     * 간부평가대상자 삭제 
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap deleteDB(SearchMap searchMap) {
        
        HashMap returnMap = new HashMap(); 
        String[] empn = searchMap.getString("empn").split("\\|",0);
        try {
            
            setStartTransaction();
            
                for (int i = 0; i < empn.length; i++) {
                    
                    searchMap.put("empn", empn[i]);
                    
                    returnMap = insertData("prs.evalCon.empEvalConMember.deleteData", searchMap);
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
     * 개인평가 대상자 등록
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap insertprsEmpMemberAdmin(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();        
        try {
            returnMap = deleteData("prs.evalCon.empEvalConMember.deleteAdminDatas", searchMap, true);//권한삭제
            returnMap = insertData("prs.evalCon.empEvalConMember.insertAdminDatas", searchMap);//권한등록    
            
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
    public SearchMap empEvalConMemberListExcel(SearchMap searchMap) {
    	String excelFileName = "평가대상자관리";
    	String excelTitle = "평가대상자관리목록";
    	
    	/************************************************************************************
    	 * 조회조건 설정
    	 ************************************************************************************/
    	ArrayList<ExcelVO> excelSearchInfoList = new ArrayList<ExcelVO>();
    	
    	excelSearchInfoList.add(new ExcelVO(StringConstants.YEAR, (String)searchMap.get("yearNm")));
    	
    	/****************************************************************************************************
    	 * 엑셀 다운로드 설정 : '헤더명', '컬럼명', '셀정렬(left, center, right)', 'ROWSPAN COLUMN', '셀너비'
    	 ****************************************************************************************************/
    	ArrayList<ExcelVO> excelInfoList = new ArrayList<ExcelVO>();
    	excelInfoList.add(new ExcelVO("사원번호", "EMPN", "center"));
    	excelInfoList.add(new ExcelVO("이름", "KOR_NM", "center"));
    	excelInfoList.add(new ExcelVO("부서명", "DEPT_FULL_NM", "left"));
    	excelInfoList.add(new ExcelVO("직급", "CAST_TC_NM", "center"));
    	excelInfoList.add(new ExcelVO("직위", "POS_TC_NM", "center"));
    	excelInfoList.add(new ExcelVO("직무수행시작일", "START_PCMT_DATE", "left"));
    	excelInfoList.add(new ExcelVO("직무수행종료일", "END_PCMT_DATE", "left"));
    	excelInfoList.add(new ExcelVO("평가자", "MANAGER_USER_NM", "center"));
    	excelInfoList.add(new ExcelVO("직원평가대상여부", "EVAL_YN", "center"));

    	searchMap.put("excelFileName", excelFileName);
    	searchMap.put("excelTitle", excelTitle);
    	searchMap.put("excelSearchInfoList", excelSearchInfoList);
    	searchMap.put("excelInfoList", excelInfoList);
    	
    	searchMap.put("excelDataList", (ArrayList)getList("prs.evalCon.empEvalConMember.getList", searchMap));
    	
    	return searchMap;
    	
    }
    
}
