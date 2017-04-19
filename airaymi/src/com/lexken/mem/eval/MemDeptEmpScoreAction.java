/*************************************************************************
* CLASS 명     : OrgDeptPrsScoreAction
* 작 업 자      : 박선혜
* 작 업 일      : 2013년 6월 22일
* 기    능      : 개인별 조직관리역량 평가결과
* ---------------------------- 변 경 이 력 --------------------------------
* 번호  작 업 자     작     업     일        변 경 내 용                 비고
* ----  --------  -----------------  -------------------------    --------
*   1    박선혜      2013년 6월 22일            최 초 작 업
**************************************************************************/
package com.lexken.mem.eval;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lexken.framework.codeUtil.CodeUtil;
import com.lexken.framework.common.ErrorMessages;
import com.lexken.framework.common.ExcelVO;
import com.lexken.framework.common.SearchMap;
import com.lexken.framework.core.CommonService;
import com.lexken.framework.login.LoginVO;
import com.lexken.prs.insa.InsaAction;

public class MemDeptEmpScoreAction extends CommonService {

    private static final long serialVersionUID = 1L;

    // Logger
    private final Log logger = LogFactory.getLog(getClass());

    /**
     * 개인별 조직관리역량평가결과 조회
     * @param
     * @return String
     * @throws
     */
    public SearchMap memDeptEmpScoreList(SearchMap searchMap) {
    	
    	searchMap.addList("workMonList", CodeUtil.getCodeList("250"));
    	
        return searchMap;
    }

    /**
     * 개인별 조직관리평가결과 데이터 조회(xml)
     * @param
     * @return String
     * @throws
     */
    public SearchMap memDeptEmpScoreList_xml(SearchMap searchMap) {
        
    	String totCntStr = getStr("mem.eval.memDeptEmpScore.selectAllRecords", searchMap);
    	int totCnt = Integer.parseInt(totCntStr);
    	String pageStr = searchMap.getString("page");
    	int page = Integer.parseInt(pageStr);
    	String rowsStr = searchMap.getString("rows");
    	int rows = Integer.parseInt(rowsStr);
    	
    	//System.out.println("================================="+pageStr);
    	
    	searchMap.addList("page", page);
    	searchMap.addList("total", (int)(totCnt/rows)+1);
    	searchMap.addList("records", totCnt);
    	searchMap.put("startRow", (15*(page-1))+1);
    	searchMap.put("endRow", (15*page)+1);
    	
    	
        searchMap.addList("list", getList("mem.eval.memDeptEmpScore.getList", searchMap));

        return searchMap;
    }

    /**
     * 조직트리
     * @param
     * @return String
     * @throws
     */
    public SearchMap popDeptCdTree(SearchMap searchMap) {

    	searchMap.addList("treeList", getList("mem.eval.memDeptEmpScore.getScDeptList", searchMap));

        return searchMap;
    }
    
    
    /**
     * 조직별KPI관리 등록/수정/삭제
     * @param
     * @return String
     * @throws
     */
    public SearchMap memDeptEmpScoreProcess(SearchMap searchMap) {
        HashMap returnMap = new HashMap();
        String stMode = searchMap.getString("mode");

        /**********************************
         * 무결성 체크
         **********************************/
        /*
        if("ADD".equals(stMode) || "MOD".equals(stMode) || "BATCH".equals(stMode) ) {
            returnMap = this.validChk(searchMap);

            if((Integer)returnMap.get("ErrorNumber") < 0 ){
                searchMap.addList("returnMap", returnMap);
                return searchMap;
            }
        }
        */

        /**********************************
         * 등록/수정/삭제
         **********************************/
        if("ALLSAVE".equals(stMode)) {
            searchMap = allSaveDB(searchMap);
        } else if("BATCH".equals(stMode)) {
            searchMap = execDB(searchMap);
        } 

        /**********************************
         * 지표연계 속성 수정
         **********************************/
        //searchMap = metricPropertySet(searchMap);

        /**********************************
         * 조직코드 설정
         **********************************/
        //String scDeptId = searchMap.getString("scDeptId");
    	//searchMap.put("findSearchCodeId", scDeptId);

        /**********************************
         * Return
         **********************************/
        return searchMap;
    }
    
    public SearchMap allSaveDB(SearchMap searchMap) {
        HashMap returnMap = new HashMap();
        LoginVO loginVO = (LoginVO)searchMap.get("loginVO");

        /**********************************
         * Parameter setting
         **********************************/
        String year = searchMap.getString("year");

        String[] empNos = searchMap.getStringArray("deptEmpNoArray");
        String[] sers = searchMap.getStringArray("deptSerArray");
        String[] startPcmtDates = searchMap.getStringArray("startPcmtDateArray");
        String[] endPcmtDates = searchMap.getStringArray("endPcmtDateArray");
        String bEmpNo = "";

        try {
        	
        	setStartTransaction();
        	
        	if(null != empNos && 0 < empNos.length) {
		        for (int i = 0; i < empNos.length; i++) {
		        	searchMap.put("year", year);
		        	searchMap.put("empNo", empNos[i]);
		            searchMap.put("ser", Integer.parseInt(sers[i]));
		            searchMap.put("startPcmtDate", startPcmtDates[i].replace(".", ""));
		            searchMap.put("endPcmtDate", endPcmtDates[i].replace(".", ""));
		            returnMap = updateData("mem.eval.memDeptEmpScore.updateDB", searchMap);
		            
		            if(!bEmpNo.equals(empNos[i])){
		            	returnMap = updateData("mem.eval.memDeptEmpScore.updateScoreDB", searchMap);
		            }
		            
		            bEmpNo = (String)empNos[i];
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
    
    public SearchMap execDB(SearchMap searchMap) {
        HashMap returnMap = new HashMap();
        LoginVO loginVO = (LoginVO)searchMap.get("loginVO");

        /**********************************
         * Parameter setting
         **********************************/
        String year = searchMap.getString("year");

        try {
        	
        	setStartTransaction();
        	
        	returnMap = insertData("mem.eval.memDeptEmpScore.execDB", searchMap);
        	

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
     * 평가결과 엑셀다운로드
     * @param
     * @return String
     * @throws
     */
    public SearchMap memDeptEmpScoreListExcel(SearchMap searchMap) {
    	String excelFileName = "개인별 조직관리평가결과";
    	String excelTitle = "개인별 조직관리평가결과 리스트";

    	/************************************************************************************
    	 * 조회조건 설정
    	 ************************************************************************************/
    	ArrayList<ExcelVO> excelSearchInfoList = new ArrayList<ExcelVO>();
    	excelSearchInfoList.add(new ExcelVO("평가년도", (String)searchMap.get("yearNm")));
    	excelSearchInfoList.add(new ExcelVO("직급", (String)searchMap.get("castTcNm")));
    	excelSearchInfoList.add(new ExcelVO("직위", (String)searchMap.get("posTcNm")));
    	excelSearchInfoList.add(new ExcelVO("이름", (String)searchMap.get("empNm")));

    	/****************************************************************************************************
         * 엑셀 다운로드 설정 : '헤더명', '컬럼명', '셀정렬(left, center, right)', 'ROWSPAN COLUMN', '셀너비'
         ****************************************************************************************************/
    	ArrayList<ExcelVO> excelInfoList = new ArrayList<ExcelVO>();
    	excelInfoList.add(new ExcelVO("평가년도",		"YEAR",				"center", "EMP_NO"));
    	excelInfoList.add(new ExcelVO("사번",		    "EMP_NO",			"center", "EMP_NO"));
    	excelInfoList.add(new ExcelVO("이름",		    "EMP_NM",			"center", "EMP_NO"));
    	excelInfoList.add(new ExcelVO("직급",		    "CAST_TC_NM",		"center", "EMP_NO"));
    	excelInfoList.add(new ExcelVO("직위",		    "POS_TC_NM",		"center", "EMP_NO"));
    	excelInfoList.add(new ExcelVO("순번",		    "SER",				"center"));
    	excelInfoList.add(new ExcelVO("발령구분",		"PCMT_TC_NM",		"center"));
    	excelInfoList.add(new ExcelVO("조직코드",		"DEPT_CD",			"center"));
    	excelInfoList.add(new ExcelVO("조직명",		"DEPT_NM",			"left"));
    	excelInfoList.add(new ExcelVO("근무시작일",	"START_PCMT_DATE",	"center"));
    	excelInfoList.add(new ExcelVO("근무종료일",	"END_PCMT_DATE",	"center"));
    	excelInfoList.add(new ExcelVO("근무월",		"WORK_MON",			"center"));
    	excelInfoList.add(new ExcelVO("조직점수",		"DEPT_SCORE",		"center"));
    	excelInfoList.add(new ExcelVO("총근무월",		"TOT_WORK_MON",		"center", "EMP_NO"));
    	excelInfoList.add(new ExcelVO("개인총점",		"DEPT_FINAL_SCORE",	"center", "EMP_NO"));

    	searchMap.put("excelFileName", excelFileName);
    	searchMap.put("excelTitle", excelTitle);
    	searchMap.put("excelSearchInfoList", excelSearchInfoList);
    	searchMap.put("excelInfoList", excelInfoList);
    	searchMap.put("excelDataList", getList("mem.eval.memDeptEmpScore.getExcelList", searchMap));

        return searchMap;
    }
}
