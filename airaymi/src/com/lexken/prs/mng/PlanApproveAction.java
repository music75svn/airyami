/*************************************************************************
* CLASS 명      : PlanApproveAction
* 작 업 자      : 김효은
* 작 업 일      : 2013년 12월 6일 
* 기    능      : 성과계획서 확인
* ---------------------------- 변 경 이 력 --------------------------------
* 번호   작 업 자      작   업   일        변 경 내 용              비고
* ----  ---------  -----------------  -------------------------    --------
*   1    김효은      2013년 12월 6일         최 초 작 업 
**************************************************************************/
package com.lexken.prs.mng;
    
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lexken.framework.common.ErrorMessages;
import com.lexken.framework.common.ExcelVO;
import com.lexken.framework.common.SearchMap;
import com.lexken.framework.common.StringConstants;
import com.lexken.framework.core.CommonService;
import com.lexken.framework.login.LoginVO;
import com.lexken.framework.util.StaticUtil;

public class PlanApproveAction extends CommonService {

    private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * 성과계획서 확인리스트 관리 조회
     * @param      
     * @return String  
     * @throws 
     */
 public SearchMap evalMngList(SearchMap searchMap) {
    	
    	searchMap.put("findDeptCd", "0000");
    	
    	// 최상위 평가조직 조회
    	HashMap topScDept = (HashMap)getDetail("prs.mng.planApprove.getTopDeptInfo", searchMap);

    	// 파라미터가 null로 들어올 때 디폴트값 셋팅.
    	if (topScDept == null) {
    		topScDept = new HashMap();
    		topScDept.put("DEPT_CD", "");
    	}
    	
    	// 파라미터가 null로 들어올 때 디폴트값 셋팅.
    	String findDeptCd =  StaticUtil.nullToDefault((String)searchMap.getString("findDeptCd"), (String)topScDept.get("DEPT_CD"));	// 조직코드가 없으면 전사조직코드를 셋팅.
    	
    	// 디폴트 조회조건 설정
    	searchMap.put("findDeptCd", findDeptCd);	// 검색버튼 조회시 조직트리에 기존 선택한 조직이 표시되도록 설정.(findSearchCodeId에 넣어주면 우선 적용됨. 트리와 검색조건을 별도로 사용할 경우에 한함.)
    	
    	searchMap.addList("deptTree", getList("prs.mng.evalMng.getDeptList", searchMap)); //인사조직
    	searchMap.addList("closingPlan", getDetail("prs.mng.planMng.getClosingPlan", searchMap));//일정마감
    	
    	return searchMap;
    }
    
    /**
     * 성과계획서 확인리스트 관리 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap evalMngList_xml(SearchMap searchMap) {
    	LoginVO loginVO = (LoginVO)searchMap.get("loginVO");
    	if(loginVO.chkAuthGrp("01")) searchMap.put("isAdmin", "Y");
    	searchMap.addList("list", getList("prs.mng.planApprove.getList", searchMap));
    	
    	return searchMap;
    }
    
    /**
     * 성과계획서 확인 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap planApproveList(SearchMap searchMap) {
    	
    	if("".equals(StaticUtil.nullToBlank((String)searchMap.getString("evalMembEmpn")))) {
    		searchMap.put("evalMembEmpn",  (String)searchMap.getString("findEmpn"));
    	}
    	if("".equals(StaticUtil.nullToBlank((String)searchMap.getString("evalMembEmpnSeq")))) {
    		searchMap.put("evalMembEmpnSeq", (String)searchMap.getString("findEmpnSeq"));
    	}
    	
    	searchMap.addList("membersTable", getDetail("prs.mng.planMng.getFindList", searchMap));//목록리스트
    	searchMap.addList("closingPlan", getDetail("prs.mng.planMng.getClosingPlan", searchMap));//일정마감
    	
    	searchMap.addList("dfflyList", getList("prs.mng.planMng.getDfflyList", searchMap));
    	
        return searchMap;
    }
    
    /**
     * 성과계획서 확인 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap planApproveList_xml(SearchMap searchMap) {
    	
    	if("".equals(StaticUtil.nullToBlank((String)searchMap.getString("evalMembEmpn")))) {
    		searchMap.put("evalMembEmpn",  (String)searchMap.getString("findEmpn"));
    	}
    	if("".equals(StaticUtil.nullToBlank((String)searchMap.getString("evalMembEmpnSeq")))) {
    		searchMap.put("evalMembEmpnSeq", (String)searchMap.getString("findEmpnSeq"));
    	}
    	
        searchMap.addList("planList", getList("prs.mng.planMng.getPlanList", searchMap));
        
        return searchMap;
    }
    
    
    
    /**
     * 성과계획서 반려사유 팝업 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap popReturnReason(SearchMap searchMap) {
    	
    	searchMap.addList("return", getDetail("prs.mng.planApprove.getReturnList", searchMap));
    	return searchMap;
    }
     
    /**
     * 성과계획서 확인리스트 등록/수정/삭제
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap planEvalMngProcess(SearchMap searchMap) {
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
    	if("LISTAPPROVE".equals(stMode)) { 
    		searchMap = planApproveDB(searchMap);
    	} else if("LISTRETURN".equals(stMode)) {
    		searchMap = returnDB(searchMap);
    	}
    	
    	/**********************************
    	 * Return
    	 **********************************/
    	return searchMap;
    }
    
    /**
     * 성과계획서 확인 등록/수정/삭제
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap planApproveProcess(SearchMap searchMap) {
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
       if("APPROVE".equals(stMode)) {
    	   searchMap = planApproveDB(searchMap);
        } else if("RETURN".equals(stMode)) {
        	searchMap = returnDB(searchMap);
	    } else if("SAVE".equals(stMode)) {
        	searchMap = saveDB(searchMap);
	    }
        
        /**********************************
         * Return
         **********************************/
        return searchMap;
    }
    
    
    /**
     * 성과계획서 확인 
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap planApproveDB(SearchMap searchMap) {
    	
    	HashMap returnMap = new HashMap(); 
    	
    	try {
    		
    		String[] evalMembEmpns = searchMap.getString("evalMembEmpns").split("\\|", 0);
    		String[] evalMembEmpnSeqs = searchMap.getString("evalMembEmpnSeqs").split("\\|", 0);
    		String[] approveDts = searchMap.getString("approveDts").split("\\|", 0);
    		String gubun = searchMap.getString("gubun");
    		String userId = searchMap.getString("userId");
    		
    		setStartTransaction();
    		
    		for (int i = 0; i < evalMembEmpns.length; i++) {
  	            	searchMap.put("evalMembEmpn", evalMembEmpns[i]);
  	            	searchMap.put("evalMembEmpnSeq", evalMembEmpnSeqs[i]);
  	            	searchMap.put("userId", userId);
            	if("01".equals(gubun)){
  	            	searchMap.put("planStatusId","03");
  	            	returnMap = updateData("prs.mng.planApprove.planApproveData", searchMap);
  	            }else if("02".equals(gubun)){
  	            	searchMap.put("planStatusId","04");
  	            	searchMap.put("finalApprove","Y");
  	            	returnMap = updateData("prs.mng.planApprove.updateData", searchMap);//계약서상태 변경
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
     * 난이도 저장
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap saveDB(SearchMap searchMap) {
    	
    	HashMap returnMap = new HashMap(); 
    	
    	try {
    		
    		String evalMembEmpn = searchMap.getString("evalMembEmpn");
    		String evalMembEmpnSeq = searchMap.getString("evalMembEmpnSeq");
    		String[] targetId = searchMap.getStringArray("targetIds");
    		String[] dffly = searchMap.getStringArray("dffly");
    		
    		setStartTransaction();
    		
    		for (int i = 0; i < targetId.length; i++) {
            	searchMap.put("evalMembEmpn", evalMembEmpn);
            	searchMap.put("evalMembEmpnSeq", evalMembEmpnSeq);
            	searchMap.put("targetId", targetId[i]);
            	searchMap.put("dffly", dffly[i]);
            	returnMap = updateData("prs.mng.planApprove.updateDfflyData", searchMap);
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
     * 성과계획서 확인 수정
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap returnDB(SearchMap searchMap) {
    	HashMap returnMap    = new HashMap();    
    	
    	try {
    		
    		String[] evalMembEmpns = searchMap.getString("evalMembEmpns").split("\\|", 0);
    		String[] evalMembEmpnSeqs = searchMap.getString("evalMembEmpnSeqs").split("\\|", 0);
    		String[] approveUserIds = searchMap.getString("approveUserIds").split("\\|", 0);
    		String userId = searchMap.getString("userId");
    		String gubun = searchMap.getString("gubun");
    		
    		setStartTransaction();
    		for (int i = 0; i < evalMembEmpns.length; i++) {
  	            	searchMap.put("evalMembEmpn", evalMembEmpns[i]);
  	            	searchMap.put("evalMembEmpnSeq", evalMembEmpnSeqs[i]);
  	            	searchMap.put("userId", userId);
  	            	searchMap.put("planStatusId","05");
  	            	searchMap.put("approveUserId",approveUserIds[i]);
  	            	returnMap = updateData("prs.mng.planApprove.updateData", searchMap);
  	            	returnMap = insertData("prs.mng.planApprove.insertHistoryData", searchMap);// 성과계획서 이력등록
  	    			returnMap = insertData("prs.mng.planApprove.insertTargetHistoryData", searchMap);// 목표관리 이력등록
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
    
    /**
     * 성과계획서 관리 엑셀다운로드
     * @param      
     * @return String  
     * @throws 
     */  
    public SearchMap evalMngListExcel(SearchMap searchMap) {
        String excelFileName = "주관부서확인";
        String excelTitle = "주관부서확인 리스트";
        
        /************************************************************************************
         * 조회조건 설정
         ************************************************************************************/
        ArrayList<ExcelVO> excelSearchInfoList = new ArrayList<ExcelVO>();
        LoginVO loginVO = (LoginVO)searchMap.get("loginVO");
    	if(loginVO.chkAuthGrp("01")) searchMap.put("isAdmin", "Y");
        
        excelSearchInfoList.add(new ExcelVO(StringConstants.YEAR, (String)searchMap.get("yearNm")));
        //excelSearchInfoList.add(new ExcelVO("이름", StaticUtil.nullToDefault((String)searchMap.get("korNm"), "전체")));
        excelSearchInfoList.add(new ExcelVO("계약서상태", StaticUtil.nullToDefault((String)searchMap.get("planStatusNm"), "전체")));
        
        
        /****************************************************************************************************
         * 엑셀 다운로드 설정 : '헤더명', '컬럼명', '셀정렬(left, center, right)', 'ROWSPAN COLUMN', '셀너비'
         ****************************************************************************************************/
        ArrayList<ExcelVO> excelInfoList = new ArrayList<ExcelVO>();
        excelInfoList.add(new ExcelVO("사원번호", "EVAL_MEMB_EMPN", "center"));
    	excelInfoList.add(new ExcelVO("이름", "KOR_NM", "center"));
    	excelInfoList.add(new ExcelVO("부서 명", "DEPT_FULL_NM", "left"));
    	excelInfoList.add(new ExcelVO("직급", "CAST_TC_NM", "center"));
    	excelInfoList.add(new ExcelVO("직위", "POS_TC_NM", "left"));
    	excelInfoList.add(new ExcelVO("직무수행기간", "WORKDATE", "center"));
    	excelInfoList.add(new ExcelVO("계획서상태", "PLAN_STATUS_NM", "center"));
        
        searchMap.put("excelFileName", excelFileName);
        searchMap.put("excelTitle", excelTitle);
        searchMap.put("excelSearchInfoList", excelSearchInfoList);
        searchMap.put("excelInfoList", excelInfoList);
        searchMap.put("excelDataList", (ArrayList)getList("prs.mng.planApprove.getList", searchMap));
        return searchMap;
    }
    

}
