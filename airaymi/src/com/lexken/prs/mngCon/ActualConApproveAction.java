/*************************************************************************
* CLASS 명      : ActualConApproveAction
* 작 업 자      : 김효은
* 작 업 일      : 2014년 4월 4일 
* 기    능      : 직원개인업무성과 실적확인
* ---------------------------- 변 경 이 력 --------------------------------
* 번호   작 업 자      작   업   일        변 경 내 용              비고
* ----  ---------  -----------------  -------------------------    --------
*   1    김효은      2014년 4월 4일         최 초 작 업 
**************************************************************************/
package com.lexken.prs.mngCon;
    
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

public class ActualConApproveAction extends CommonService {

    private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    
    /**
     * 직원개인업무성과 실적확인  조회
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
    		topScDept.put("DEPT_KOR_NM", "");
    	}
    	
    	// 파라미터가 null로 들어올 때 디폴트값 셋팅.
    	String findDeptCd =  StaticUtil.nullToDefault((String)searchMap.getString("findDeptCd"), (String)topScDept.get("DEPT_CD"));	// 조직코드가 없으면 전사조직코드를 셋팅.
    	String findUpDeptName =  StaticUtil.nullToDefault((String)searchMap.getString("findUpDeptName"), (String)topScDept.get("DEPT_KOR_NM")) ; ;	// 조직명이 없으면 전사조직명을 셋팅.
    	
    	// 디폴트 조회조건 설정
    	searchMap.put("findDeptCd", findDeptCd);	// 검색버튼 조회시 조직트리에 기존 선택한 조직이 표시되도록 설정.(findSearchCodeId에 넣어주면 우선 적용됨. 트리와 검색조건을 별도로 사용할 경우에 한함.)
    	searchMap.put("findUpDeptName", findUpDeptName);
    	
    	searchMap.addList("deptTree", getList("prs.mng.evalMng.getDeptList", searchMap)); //인사조직
    	//searchMap.addList("actInTermList", getList("prs.mng.actInputTerm.getList", searchMap));
    	if("".equals(StaticUtil.nullToBlank((String)searchMap.getString("mon")))) {
    		searchMap.put("mon",  (String)searchMap.getString("findMon"));
    	}
    	//searchMap.addList("actInTerm", getDetail("prs.mng.actInputTerm.getDetail", searchMap));
    	
    	return searchMap;
    }
    
    /**
     * 직원개인업무성과 실적확인 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap evalMngList_xml(SearchMap searchMap) {
    	LoginVO loginVO = (LoginVO)searchMap.get("loginVO");
    	if(loginVO.chkAuthGrp("01")) searchMap.put("isAdmin", "Y");
    	searchMap.put("findMon", "12");
    	searchMap.addList("list", getList("prs.mngCon.actualConApprove.getActualList", searchMap));
    	
    	return searchMap;
    }
    
    /**
     * 직원개인업무성과 실적확인 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap actualConApproveList(SearchMap searchMap) {
    	searchMap.put("mon", "12");
    	searchMap.addList("membersTable", getDetail("prs.mngCon.planMngCon.getFindList", searchMap));//목록리스트
    	/**********************************
         * 첨부파일
         **********************************/
    	searchMap.addList("fileList", getList("prs.mngCon.actualMngCon.getFileList", searchMap));
    	
        return searchMap;
    }
    
    /**
     * 직원개인업무성과 실적확인 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap actualConApproveList_xml(SearchMap searchMap) {
        
    	searchMap.put("findMon", "12");
        searchMap.addList("list", getList("prs.mngCon.actualConApprove.getList", searchMap));

        return searchMap;
    }
    
    /**
     * 직원개인업무성과 실적반려사유 팝업 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap popReturnReason(SearchMap searchMap) {
    	searchMap.put("findMon", "12");
    	searchMap.addList("return", getDetail("prs.mngCon.actualConApprove.getReturnList", searchMap));
    	return searchMap;
    }
    
    /**
     * 직원개인업무성과 실적확인 등록/수정/삭제
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap actualConApproveProcess(SearchMap searchMap) {
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
        if("APC".equals(stMode)) {
        	searchMap = approveDB(searchMap);
        } else if("RETURN".equals(stMode)) {
        	searchMap = returnDB(searchMap);
        }
        
        /**********************************
         * Return
         **********************************/
        return searchMap;
    }
    
    /**
     * 직원개인업무성과 실적확인 등록/수정/삭제
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap actualMngApproveProcess(SearchMap searchMap) {
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
    	if("APC".equals(stMode)) {
    		searchMap = approveDB(searchMap);
    	} else if("RETURN".equals(stMode)) {
    		searchMap = returnDB(searchMap);
    	}
    	
    	/**********************************
    	 * Return
    	 **********************************/
    	return searchMap;
    }
    
    
    /**
     * 직원개인업무성과 실적확인 확인요청
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap approveDB(SearchMap searchMap) {
    	HashMap returnMap    = new HashMap();    
    	
    	try {
    		setStartTransaction();
    		
    		String[] evalMembEmpns = searchMap.getString("evalMembEmpns").split("\\|");
    		String[] evalMembEmpnSeqs = searchMap.getString("evalMembEmpnSeqs").split("\\|");
    		String gubun = searchMap.getString("gubun");
    		
    		if("01".equals(gubun)){
    		 searchMap.put("actStatusId","03");
    		}else if("02".equals(gubun)){
    		 searchMap.put("actStatusId","04");
    		}
    		
    		for(int i = 0; i < evalMembEmpns.length; i++){
    			searchMap.put("evalMembEmpn", evalMembEmpns[i]);
    			searchMap.put("evalMembEmpnSeq", evalMembEmpnSeqs[i]);
    			searchMap.put("mon", "12");
    			
    			returnMap = updateData("prs.mngCon.actualConApprove.updateStatusData", searchMap);
    			returnMap = updateData("prs.mngCon.actualConApprove.updateConEvalEmpGrade", searchMap);
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
     * 직원개인업무성과 실적확인 확인요청
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap returnDB(SearchMap searchMap) {
    	HashMap returnMap    = new HashMap();    
    	
    	try {
    		
    		String[] evalMembEmpns = searchMap.getString("evalMembEmpns").split("\\|");
    		String[] evalMembEmpnSeqs = searchMap.getString("evalMembEmpnSeqs").split("\\|");
    		searchMap.put("actStatusId", "05");
    		
    		
    		setStartTransaction();
    		
    		for(int i = 0; i < evalMembEmpns.length; i++){
    			searchMap.put("evalMembEmpn", evalMembEmpns[i]);
    			searchMap.put("evalMembEmpnSeq", evalMembEmpnSeqs[i]);
    			
    			returnMap = updateData("prs.mngCon.actualConApprove.updateStatusData", searchMap);
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
     * 직원개인업무성과 실적확인 엑셀다운로드
     * @param      
     * @return String  
     * @throws 
     */  
    public SearchMap evalMngListExcel(SearchMap searchMap) {
        String excelFileName = "주관부서 실적확인";
        String excelTitle = "주관부서 실적확인 리스트";
        
        /************************************************************************************
         * 조회조건 설정
         ************************************************************************************/
        ArrayList<ExcelVO> excelSearchInfoList = new ArrayList<ExcelVO>();
        String actStatusId = searchMap.getString("actStatusId");
        excelSearchInfoList.add(new ExcelVO(StringConstants.YEARMON, (String)searchMap.get("yearNm")));
        excelSearchInfoList.add(new ExcelVO("승인상태", (String)searchMap.get("actStatusNm")));
        
        
        /****************************************************************************************************
         * 엑셀 다운로드 설정 : '헤더명', '컬럼명', '셀정렬(left, center, right)', 'ROWSPAN COLUMN', '셀너비'
         ****************************************************************************************************/
        ArrayList<ExcelVO> excelInfoList = new ArrayList<ExcelVO>();
        excelInfoList.add(new ExcelVO("사원번호", "EVAL_MEMB_EMPN", "left"));
    	excelInfoList.add(new ExcelVO("이름", "KOR_NM", "left"));
    	excelInfoList.add(new ExcelVO("부서 명", "DEPT_FULL_NM", "left"));
    	excelInfoList.add(new ExcelVO("직급", "CAST_TC_NM", "left"));
    	excelInfoList.add(new ExcelVO("직위", "POS_TC_NM", "left"));
    	excelInfoList.add(new ExcelVO("부서장", "MANAGER_USER_NM", "left"));
    	excelInfoList.add(new ExcelVO("계획서상태", "PLAN_STATUS_NM", "left"));
    	excelInfoList.add(new ExcelVO("실적상태", "ACT_STATUS_NM", "left"));
    	
    	LoginVO loginVO = (LoginVO)searchMap.get("loginVO");
    	if(loginVO.chkAuthGrp("01")) searchMap.put("isAdmin", "Y");
    	searchMap.put("findActStatusId", actStatusId);
    	
        searchMap.put("excelFileName", excelFileName);
        searchMap.put("excelTitle", excelTitle);
        searchMap.put("excelSearchInfoList", excelSearchInfoList);
        searchMap.put("excelInfoList", excelInfoList);
        searchMap.put("excelDataList", (ArrayList)getList("prs.mngCon.actualConApprove.getActualList", searchMap));
        return searchMap;
    }
    

    /**
     * 직원개인업무성과 실적확인(상세) 엑셀다운로드
     * @param      
     * @return String  
     * @throws 
     */  
    public SearchMap actualConApproveListExcel(SearchMap searchMap) {
        String excelFileName = "주관부서 실적확인";
        String excelTitle = "주관부서 실적확인 리스트";
        
        /************************************************************************************
         * 조회조건 설정
         ************************************************************************************/
        ArrayList<ExcelVO> excelSearchInfoList = new ArrayList<ExcelVO>();
        
        excelSearchInfoList.add(new ExcelVO(StringConstants.YEAR, (String)searchMap.get("yearNm")));
    	excelSearchInfoList.add(new ExcelVO("이름", (String)searchMap.get("korNm")));
    	excelSearchInfoList.add(new ExcelVO("부서명", (String)searchMap.get("deptKorNm")));
    	excelSearchInfoList.add(new ExcelVO("직위", (String)searchMap.get("posTcNm")));
    	excelSearchInfoList.add(new ExcelVO("직급", (String)searchMap.get("castTcNm")));
    	excelSearchInfoList.add(new ExcelVO("확인자", StaticUtil.nullToDefault((String)searchMap.get("approveUserNm"), "없음")));
    	excelSearchInfoList.add(new ExcelVO("계약서상태", (String)searchMap.get("planStatusNm")));
    	excelSearchInfoList.add(new ExcelVO("최종확인일", (String)searchMap.get("finalApproveDt")));
    	excelSearchInfoList.add(new ExcelVO("승인상태", (String)searchMap.get("actualStatusNmExcel")));
        
        /****************************************************************************************************
         * 엑셀 다운로드 설정 : '헤더명', '컬럼명', '셀정렬(left, center, right)', 'ROWSPAN COLUMN', '셀너비'
         ****************************************************************************************************/
        ArrayList<ExcelVO> excelInfoList = new ArrayList<ExcelVO>();
    	excelInfoList.add(new ExcelVO("설정방향", "DIRECTION_NM", "left"));
    	excelInfoList.add(new ExcelVO("성과목표", "TARGET_NM", "left"));
    	excelInfoList.add(new ExcelVO("성과지표", "METRIC_NM", "left"));
    	excelInfoList.add(new ExcelVO("목표", "TARGET_VALUE", "left"));
    	excelInfoList.add(new ExcelVO("단위", "UNIT_NM", "left"));
    	excelInfoList.add(new ExcelVO("실적", "VALUE", "left"));
    	excelInfoList.add(new ExcelVO("점수", "SCORE", "left"));
    	excelInfoList.add(new ExcelVO("평가등급", "EVAL_GRADE", "left"));
    	
    	String evalMembEmpnExcel = searchMap.getString("evalMembEmpnExcel");
    	String evalMembEmpnSeqExcel = searchMap.getString("evalMembEmpnSeqExcel");
    	searchMap.put("evalMembEmpn", evalMembEmpnExcel);
    	searchMap.put("evalMembEmpnSeq", evalMembEmpnSeqExcel);
        
        searchMap.put("excelFileName", excelFileName);
        searchMap.put("excelTitle", excelTitle);
        searchMap.put("excelSearchInfoList", excelSearchInfoList);
        searchMap.put("excelInfoList", excelInfoList);
        searchMap.put("excelDataList", (ArrayList)getList("prs.mngCon.actualConApprove.getList", searchMap));
        
        return searchMap;
    }
    
}
