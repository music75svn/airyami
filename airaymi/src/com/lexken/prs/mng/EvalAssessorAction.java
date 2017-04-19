/*************************************************************************
* CLASS 명      : EvalAssessorAction
* 작 업 자      : 김효은
* 작 업 일      : 2013년 12월 11일 
* 기    능      : 개인별 평가자 선정
* ---------------------------- 변 경 이 력 --------------------------------
* 번호   작 업 자      작   업   일        변 경 내 용              비고
* ----  ---------  -----------------  -------------------------    --------
*   1    김효은      2013년 12월 11일         최 초 작 업 
**************************************************************************/
package com.lexken.prs.mng;
    
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

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

public class EvalAssessorAction extends CommonService {

    private static final long serialVersionUID = 1L;
    
    // Logger
    private final Log logger = LogFactory.getLog(getClass());
    
    /**
     * 개인별 평가자 선정 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap evalAssessorList(SearchMap searchMap) {
    	// 최상위 평가조직 조회
    	HashMap topScDept = (HashMap)getDetail("prs.mng.evalMng.getTopDeptInfo", searchMap);

    	// 파라미터가 null로 들어올 때 디폴트값 셋팅.
    	if (topScDept == null) {
    		topScDept = new HashMap();
    		topScDept.put("DEPT_CD", "");
    		topScDept.put("DEPT_KOR_NM", "");
    	}
    	
    	// 파라미터가 null로 들어올 때 디폴트값 셋팅.
    	String findDeptCd =  StaticUtil.nullToDefault((String)searchMap.getString("findDeptCd"), (String)topScDept.get("DEPT_CD"));	// 조직코드가 없으면 전사조직코드를 셋팅.
    	String findUpDeptName =  StaticUtil.nullToDefault((String)searchMap.getString("findUpDeptName"), (String)topScDept.get("DEPT_KOR_NM"));// 조직명이 없으면 전사조직명을 셋팅.
    	
    	// 디폴트 조회조건 설정
    	searchMap.put("findDeptCd", findDeptCd);	// 검색버튼 조회시 조직트리에 기존 선택한 조직이 표시되도록 설정.(findSearchCodeId에 넣어주면 우선 적용됨. 트리와 검색조건을 별도로 사용할 경우에 한함.)
    	
    	searchMap.addList("deptTree", getList("prs.mng.evalMng.getDeptList", searchMap)); //인사조직
    	
    	searchMap.put("findEvalGrpType", "02");
    	searchMap.addList("evalGrpList", getList("bsc.module.commModule.getEvalGrpList", searchMap));//평가군 조회
    	searchMap.addList("mngClosing", getDetail("prs.mng.evalGrpAssessor.getClosingMng", searchMap)); //개인평가일정
        return searchMap;
    }
    
    /**
     * 개인별 평가자 선정 데이터 조회(xml)
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap evalAssessorList_xml(SearchMap searchMap) {
        
        searchMap.addList("list", getList("prs.mng.evalAssessor.getList", searchMap));

        return searchMap;
    }
    
    /**
     * 개인별 평가자  리스트 조회
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap evalAssessor_ajax(SearchMap searchMap) {
    	
    	searchMap.addList("userList", getList("prs.mng.evalAssessor.getEvalAssessor", searchMap));
        
        return searchMap;
    }
    
    
    /**
     * 개인별 평가자 선정 상세화면
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap evalAssessorModify(SearchMap searchMap) {
        searchMap.addList("membersTable", getDetail("prs.mng.planMng.getFindList", searchMap));//목록리스트
        searchMap.addList("deptTree", getList("prs.mng.evalMng.getDeptList", searchMap)); //인사조직
    	searchMap.addList("userList", getList("prs.mng.evalAssessor.getUseList", searchMap));	//오른편 지정된 대상자
    	searchMap.addList("userList1", getList("prs.mng.evalAssessor.getUseList1", searchMap));	//오른편 지정된 대상자
    	searchMap.addList("mngEvalYn", getDetail("prs.mng.evalResult.getMngEvalClosing", searchMap));//마감여부(개인평가)
        
        return searchMap;
    }
    
    
    /**
     * 개인별 평가자 선정 등록/수정/삭제
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap evalAssessorProcess(SearchMap searchMap) {
        HashMap returnMap = new HashMap();
        String stMode 		= searchMap.getString("mode");
        String stModeFirst 	= searchMap.getString("mode1");
        
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
        }else if("ADDFIRST".equals(stModeFirst)) {
        	searchMap = insertFirstDB(searchMap);
        }else if("ASSESSOR".equals(stMode)) {
        	searchMap = insertEvalAssessor(searchMap);
        }else if("ASSESSORFIRST".equals(stMode)) {
        	searchMap = insertEvalAssessorFirst(searchMap);
        }
        
        /**********************************
         * Return
         **********************************/
        return searchMap;
    }
    
    /**
     * 개인별 평가자 선정 등록 1차평가자
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap insertFirstDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
            
        	//1차평가자
        	String empns1Org    = searchMap.getString("empns1");
        	String[] empns1 	= searchMap.getString("empns1").split("\\|", 0);
            String evalMemb1 	= searchMap.getString("evalMemb1");
            String evalMembSeq1 = searchMap.getString("evalMembSeq1");
            String userId1 		= searchMap.getString("userId1");
            String empns1Yn = "N";
            
    		setStartTransaction();
    		
    		//1차평가자
    		
    		//평가대상자가 한명이라도 지정된 경우 Y
    		if(!"".equals(empns1Org) && empns1Org != null){
    			empns1Yn = "Y";
    		} 
    		
    		searchMap.put("empns1Yn", empns1Yn);
        	searchMap.put("itemArray", empns1);
        	searchMap.put("evalMemb1", evalMemb1);
        	searchMap.put("evalMembSeq1", evalMembSeq1);
        	searchMap.put("userId1", userId1);
        	searchMap.put("evalDegreeIdFirst", "10");
        	returnMap = deleteData("prs.mng.evalAssessor.deleteGradeDataFirst", searchMap, true);//평가항목별등급삭제
        	returnMap = deleteData("prs.mng.evalAssessor.deleteMngDataFirst", searchMap, true);//개인업적삭제
        	returnMap = deleteData("prs.mng.evalAssessor.deleteAssessorMappingDataFirst", searchMap, true);//대상자별 매핑 삭제
        	
        	if(!"".equals(empns1Org) && empns1Org != null) {
     	        for (int i = 0; i < empns1.length; i++) {
     	        	searchMap.put("empn1", empns1[i]);
     	        	searchMap.put("userId1", userId1);
     	        	int evalAsser = getInt("prs.mng.evalAssessor.getEvalAsserFirst", searchMap); //전체 평가자 유무 조회
     	        	if(evalAsser == 0){
     	        		returnMap = deleteData("prs.mng.evalAssessor.deleteAssessorDataFirst", searchMap, true);//개인평가평가자 삭제
     	        	}
     	        	returnMap = insertData("prs.mng.evalAssessor.updateEvalAssessorFirst", searchMap); //개인평가 평가자 추가
     	        	
     	        	int evalMappingAsser = getInt("prs.mng.evalAssessor.getEvalMappingAsserFirst", searchMap); //매핑된 평가자 조회
     	        	if(evalMappingAsser == 0){
     	        		returnMap = insertData("prs.mng.evalAssessor.updateEvalMappingFirst", searchMap); //대상자별 평가자 매핑추가
     	        	}
     	        }
        	}
    		
        	
        	/******************************************
             * 권한설정
             ******************************************/
	        returnMap = insertAdmin(searchMap);
        	
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
     * 개인별 평가자 선정 등록 2차평가자
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap insertDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
            
        	//2차평가자
        	String empnsOrg		= searchMap.getString("empns");
            String[] empns 		= searchMap.getString("empns").split("\\|", 0);
            String evalMemb 	= searchMap.getString("evalMemb");
            String evalMembSeq 	= searchMap.getString("evalMembSeq");
            String userId 		= searchMap.getString("userId");
            String empnsYn	    = "N";
            
    		setStartTransaction();
    		
    		//2차평가자
    		
    		//평가대상자가 한명이라도 지정된 경우 Y
    		if(!"".equals(empnsOrg) && empnsOrg != null){
    			empnsYn = "Y";
    		} 
    		
    		searchMap.put("empnsYn", empnsYn);
    		
        	searchMap.put("empnsCnt", empns.length);
        	searchMap.put("itemArray", empns);
        	searchMap.put("evalMemb", evalMemb);
        	searchMap.put("evalMembSeq", evalMembSeq);
        	searchMap.put("userId", userId);
        	searchMap.put("evalDegreeIdSecond", "20");
        	returnMap = deleteData("prs.mng.evalAssessor.deleteGradeData", searchMap, true);//평가항목별등급삭제
        	returnMap = deleteData("prs.mng.evalAssessor.deleteMngData", searchMap, true);//개인업적삭제
        	returnMap = deleteData("prs.mng.evalAssessor.deleteAssessorMappingData", searchMap, true);//대상자별 매핑 삭제
        	
        	if(!"".equals(empnsOrg) && null != empnsOrg) {
     	        for (int i = 0; i < empns.length; i++) {
     	        	searchMap.put("empn", empns[i]);
     	        	searchMap.put("userId", userId);
     	        	int evalAsser = getInt("prs.mng.evalAssessor.getEvalAsser", searchMap); //전체 평가자 유무 조회
     	        	if(evalAsser == 0){
     	        		returnMap = deleteData("prs.mng.evalAssessor.deleteAssessorData", searchMap, true);//개인평가평가자 삭제
     	        	}
     	        	returnMap = insertData("prs.mng.evalAssessor.updateEvalAssessor", searchMap); //개인평가 평가자 추가
     	        	
     	        	int evalMappingAsser = getInt("prs.mng.evalAssessor.getEvalMappingAsser", searchMap); //매핑된 평가자 조회
     	        	if(evalMappingAsser == 0){
     	        		returnMap = insertData("prs.mng.evalAssessor.updateEvalMapping", searchMap); //대상자별 평가자 매핑추가
     	        	}
     	        }
        	}
        	
        	/******************************************
             * 권한설정
             ******************************************/
	        returnMap = insertAdmin(searchMap);
        	
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
     * 지표권한등록
     * 04 : KPI담당자(실적입력자)
     * 05 : 실적승인자
     * @param
     * @return String
     * @throws
     */
    public HashMap insertAdmin(SearchMap searchMap) throws Exception{
        HashMap returnMap = new HashMap();

        try {
        	 returnMap = updateData("prs.mng.evalAssessor.deleteAdminData", searchMap, true);//권한삭제
        	 returnMap = insertData("prs.mng.evalAssessor.insertAdminData", searchMap);//권한등록

        } catch (Exception e) {
        	logger.error(e.toString());
        	returnMap.put("ErrorNumber", ErrorMessages.FAILURE_PROCESS_CODE);
			returnMap.put("ErrorMessage", ErrorMessages.FAILURE_PROCESS_MESSAGE);
			throw e;
        } finally {
        }
        return returnMap;
    }
    
    /**
     * 개인별 1차평가자 일괄등록
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap insertEvalAssessorFirst(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        SearchMap retutnMap2 = null;

        try {
	        setStartTransaction();
	        
	        returnMap = insertData("prs.mng.evalAssessor.insertEvalAssessorFirst", searchMap);
	        returnMap = insertAdmin(searchMap);
	        
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
     * 개인별 2차평가자 일괄등록
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap insertEvalAssessor(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        SearchMap retutnMap2 = null;

        try {
	        setStartTransaction();
	        
	        returnMap = insertData("prs.mng.evalAssessor.insertEvalAssessor", searchMap);
	        returnMap = insertAdmin(searchMap);
	        
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
     * 개인별대상자 관리 엑셀다운로드
     * @param      
     * @return String  
     * @throws 
     */  
    public SearchMap evalAssessorListExcel(SearchMap searchMap) {
        String excelFileName = "개인별 평가자 선정";
        String excelTitle = "개인별 평가자 선정 리스트";
        
        /************************************************************************************
         * 조회조건 설정
         ************************************************************************************/
        ArrayList<ExcelVO> excelSearchInfoList = new ArrayList<ExcelVO>();
        
        excelSearchInfoList.add(new ExcelVO(StringConstants.YEAR, (String)searchMap.get("yearNm")));
        excelSearchInfoList.add(new ExcelVO("직급", StaticUtil.nullToDefault((String)searchMap.get("castTcNm"), "전체")));
        excelSearchInfoList.add(new ExcelVO("평가군", StaticUtil.nullToDefault((String)searchMap.get("evalGrpNm"), "전체")));
        
        
        /****************************************************************************************************
         * 엑셀 다운로드 설정 : '헤더명', '컬럼명', '셀정렬(left, center, right)', 'ROWSPAN COLUMN', '셀너비'
         ****************************************************************************************************/
        ArrayList<ExcelVO> excelInfoList = new ArrayList<ExcelVO>();
        excelInfoList.add(new ExcelVO("사원번호", "EVAL_MEMB_EMPN", "center"));
    	excelInfoList.add(new ExcelVO("이름", "KOR_NM", "center"));
    	excelInfoList.add(new ExcelVO("부서 명", "DEPT_FULL_NM", "left"));
    	excelInfoList.add(new ExcelVO("직급", "CAST_TC_NM", "center"));
    	excelInfoList.add(new ExcelVO("직위", "POS_TC_NM", "left"));
    	excelInfoList.add(new ExcelVO("평가군", "EVAL_GRP_NM", "left"));
    	excelInfoList.add(new ExcelVO("평가자 그룹", "ASSESSOR_GRP_NM", "left"));
    	excelInfoList.add(new ExcelVO("근무시작일", "FROM_DT", "center"));
    	excelInfoList.add(new ExcelVO("근무종료일", "TO_DT", "center"));
    	excelInfoList.add(new ExcelVO("평가차수", "EVAL_DEGREE_NM", "center"));
    	excelInfoList.add(new ExcelVO("평가자1", "ASSESSOR1_NM", "center"));
    	excelInfoList.add(new ExcelVO("평가자2", "ASSESSOR2_NM", "center"));
    	excelInfoList.add(new ExcelVO("평가자3", "ASSESSOR3_NM", "center"));
    	excelInfoList.add(new ExcelVO("평가자4", "ASSESSOR4_NM", "center"));
    	excelInfoList.add(new ExcelVO("평가자5", "ASSESSOR5_NM", "center"));
    	excelInfoList.add(new ExcelVO("평가자6", "ASSESSOR6_NM", "center"));
    	
        
        searchMap.put("excelFileName", excelFileName);
        searchMap.put("excelTitle", excelTitle);
        searchMap.put("excelSearchInfoList", excelSearchInfoList);
        searchMap.put("excelInfoList", excelInfoList);
        searchMap.put("excelDataList", (ArrayList)getList("prs.mng.evalAssessor.getList", searchMap));
        return searchMap;
    }
    
}
