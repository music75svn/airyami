/*************************************************************************
* CLASS 명      : InsaDeptScoreAction
* 작 업 자      : 지원길
* 작 업 일      : 2013년 6월 14일
* 기    능      : 조직업적평가결과
* ---------------------------- 변 경 이 력 --------------------------------
* 번호  작 업 자     작     업     일        변 경 내 용                 비고
* ----  --------  -----------------  -------------------------    --------
*   1    지원길      2013년 6월 14일            최 초 작 업
**************************************************************************/
package com.lexken.mem.eval;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lexken.framework.common.ErrorMessages;
import com.lexken.framework.common.ExcelUpload;
import com.lexken.framework.common.ExcelVO;
import com.lexken.framework.common.SearchMap;
import com.lexken.framework.common.ValidationChk;
import com.lexken.framework.core.CommonService;
import com.lexken.framework.util.StaticUtil;
import com.lexken.prs.insa.InsaAction;

public class MemDeptScoreAction extends CommonService {

    private static final long serialVersionUID = 1L;

    // Logger
    private final Log logger = LogFactory.getLog(getClass());

    /**
     * 조직업적평가결과 조회
     * @param
     * @return String
     * @throws
     */
    public SearchMap memDeptScoreList(SearchMap searchMap) {
    	// 최상위 평가조직 조회
    	HashMap topScDept = (HashMap)getDetail("mem.eval.memDeptScore.getTopDeptInfo", searchMap);


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
    	
    	searchMap.addList("deptTree", getList("mem.eval.memDeptScore.getDeptList", searchMap)); //미사용은 제외한 인사조직
    	
    	return searchMap;
    }

    /**
     * 조직업적평가결과 데이터 조회(xml)
     * @param
     * @return String
     * @throws
     */
    public SearchMap memDeptScoreList_xml(SearchMap searchMap) {

        searchMap.addList("list", getList("mem.eval.memDeptScore.getList", searchMap));

        return searchMap;
    }
    
    /**
     * 조직업적평가결과 조회
     * @param
     * @return String
     * @throws
     */
    public SearchMap popGradeDistri(SearchMap searchMap) {

        return searchMap;
    }

    /**
     * 조직업적평가결과 데이터 조회(xml)
     * @param
     * @return String
     * @throws
     */
    public SearchMap popGradeDistri_xml(SearchMap searchMap) {

        searchMap.addList("list", getList("mem.eval.memDeptScore.getGradeDistriList", searchMap));

        return searchMap;
    }

    /**
     * 엑셀업로드 팝업
     * @param
     * @return String
     * @throws
     */
    public SearchMap popMemDeptScoreExcelUpload(SearchMap searchMap) {

        return searchMap;
    }

    /**
     * 조직업적평가결과 엑셀 업로드
     * @param
     * @return String
     * @throws
     */
    public SearchMap memDeptScoreProcess(SearchMap searchMap) {
        HashMap returnMap = new HashMap();
        String stMode = searchMap.getString("mode");

        /**********************************
         * 등록/수정/삭제
         **********************************/
        if ("ADDEXCEL".equals(stMode)) {
            searchMap = insertExcelDB(searchMap);
        } else if ("GET".equals(stMode)) {
        	searchMap = getBscScore(searchMap);
        } else if("MONCAL".equals(stMode)) {
        	searchMap = getBscMonScore(searchMap);
        } else if("GRADEDISTRI".equals(stMode)) {
        	searchMap = insertGradeScoreDB(searchMap);
        } else if("SAVEALL".equals(stMode)) {
        	searchMap = insertScoreDB(searchMap);	
        }

        /**********************************
         * Return
         **********************************/
        return searchMap;
    }

    private SearchMap getBscScore(SearchMap searchMap) {
    	HashMap returnMap = new HashMap();
    	
    	try {
    		setStartTransaction();
    		
    		returnMap = insertData("mem.eval.memDeptScore.getBscScore", searchMap);
    	} catch (Exception e) {
    		setRollBackTransaction();
    		logger.error(e.toString());
        	returnMap.put("ErrorNumber", ErrorMessages.FAILURE_PROCESS_CODE);
			returnMap.put("ErrorMessage", ErrorMessages.FAILURE_PROCESS_MESSAGE);
        } finally {
        	setCommitTransaction();
        }
    	
    	searchMap.addList("returnMap", returnMap);
    	
    	return searchMap;
    }
    
    private SearchMap getBscMonScore(SearchMap searchMap) {
    	HashMap returnMap = new HashMap();
    	
    	try {
    		setStartTransaction();
    		
    		returnMap = insertData("mem.eval.memDeptScore.getBscMonScore", searchMap);
    	} catch (Exception e) {
    		setRollBackTransaction();
    		logger.error(e.toString());
        	returnMap.put("ErrorNumber", ErrorMessages.FAILURE_PROCESS_CODE);
			returnMap.put("ErrorMessage", ErrorMessages.FAILURE_PROCESS_MESSAGE);
        } finally {
        	setCommitTransaction();
        }
    	
    	searchMap.addList("returnMap", returnMap);
    	
    	return searchMap;
    }
    
    /**
     * 조직업적평가결과 등록
     * @param
     * @return String
     * @throws
     */
    public SearchMap insertScoreDB(SearchMap searchMap) {
    	HashMap returnMap = new HashMap();
    	String[] deptCds = searchMap.getStringArray("deptCdArray");
    	String[] grades = searchMap.getStringArray("deptGradeArray");
    	String[] scores = searchMap.getStringArray("deptScoreArray");
    	String year = searchMap.getString("year");

        try {
        	setStartTransaction();

        	if(null != deptCds && 0 < deptCds.length) {
        		
                for(int i=0; i<deptCds.length; i++){
                	
            		searchMap.put("year", year);
            		searchMap.put("deptCd", deptCds[i]);
            		searchMap.put("deptGrade", grades[i]);
            		searchMap.put("deptScore", scores[i]);
            		
            		String dataYn = getStr("mem.eval.memDeptScore.getDataYn", searchMap);
            		
            		if("Y".equals(dataYn)){
            			returnMap = updateData("mem.eval.memDeptScore.updateExcelData", searchMap);
            		}else{
            			returnMap = insertData("mem.eval.memDeptScore.insertExcelData", searchMap);
            		}
                }
        	}

        } catch (Exception e) {
        	logger.error(e.toString());
        	setRollBackTransaction();
        	returnMap.put("ErrorNumber", ErrorMessages.FAILURE_PROCESS_CODE);
			returnMap.put("ErrorMessage", ErrorMessages.FAILURE_PROCESS_MESSAGE);
        } finally {
        	setCommitTransaction();
        }

        /**********************************
         * Return
         **********************************/
        searchMap.addList("returnMap", returnMap);
        return searchMap;
    }
    
    /**
     * 조직업적평가결과 등록
     * @param
     * @return String
     * @throws
     */
    public SearchMap insertGradeScoreDB(SearchMap searchMap) {
    	HashMap returnMap = new HashMap();
    	String[] grades = searchMap.getString("grades").split("\\|", 0);
    	String[] scores = searchMap.getString("scores").split("\\|", 0);
    	String year = searchMap.getString("year");

        try {
        	setStartTransaction();

        	if(null != grades && 0 < grades.length) {
        		
        		String score = "";
        		String grade = "";
        		
        		returnMap = updateData("mem.eval.memDeptScore.deleteGradeDistri", searchMap, true);
        		
                for(int i=0; i<grades.length; i++){
                	if("".equals(StaticUtil.nullToDefault(grades[i], "none"))){
                		grade = "";
                	}else{
                		grade = grades[i];
                	}
                	
                	if("".equals(StaticUtil.nullToDefault(scores[i], "none"))){
                		score = "";
                	}else{
                		score = scores[i];
                	}
                	
            		searchMap.put("year", year);
            		searchMap.put("score", score);
            		searchMap.put("grade", grade);
            		
            		returnMap = insertData("mem.eval.memDeptScore.insertGradeDistri", searchMap);
                }
        	}

        } catch (Exception e) {
        	logger.error(e.toString());
        	setRollBackTransaction();
        	returnMap.put("ErrorNumber", ErrorMessages.FAILURE_PROCESS_CODE);
			returnMap.put("ErrorMessage", ErrorMessages.FAILURE_PROCESS_MESSAGE);
        } finally {
        	setCommitTransaction();
        }

        /**********************************
         * Return
         **********************************/
        searchMap.addList("returnMap", returnMap);
        return searchMap;
    }

    /**
     * 조직업적평가결과 등록
     * @param
     * @return String
     * @throws
     */
    public SearchMap insertExcelDB(SearchMap searchMap) {
    	HashMap returnMap = new HashMap();
        ArrayList excelDataList = new ArrayList();

        try {
        	setStartTransaction();

        	ExcelUpload excel = ExcelUpload.getInstance();
        	excelDataList = excel.execlInsaDeptScoreUpload(searchMap);

        	if(null != excelDataList && 0 < excelDataList.size()) {
        		String[] strYear = (String[]) excelDataList.get(0);
        		String[] strDeptCd = (String[]) excelDataList.get(1);
        		//String[] strDeptNm = (String[]) excelDataList.get(2);
        		String[] strGrade = (String[]) excelDataList.get(3);
        		String[] strScore = (String[]) excelDataList.get(4);

                for(int i=0; i<strYear.length; i++){
                	if(null != strYear[i]){
                		searchMap.put("year", strYear[i]);
                		searchMap.put("deptCd", strDeptCd[i]);
                		//searchMap.put("deptNm", strDeptNm[i]);
                		searchMap.put("deptGrade", strGrade[i]);
                		searchMap.put("deptScore", strScore[i]);

                		String dataYn = getStr("mem.eval.memDeptScore.getDataYn", searchMap);
                		
                		if("Y".equals(dataYn)){
                			returnMap = updateData("mem.eval.memDeptScore.updateExcelData", searchMap);
                		}else{
                			returnMap = insertData("mem.eval.memDeptScore.insertExcelData", searchMap);
                		}
                		
                	}
                }
        	}

        } catch (Exception e) {
        	logger.error(e.toString());
        	setRollBackTransaction();
        	returnMap.put("ErrorNumber", ErrorMessages.FAILURE_PROCESS_CODE);
			returnMap.put("ErrorMessage", ErrorMessages.FAILURE_PROCESS_MESSAGE);
        } finally {
        	setCommitTransaction();
        }

        /**********************************
         * Return
         **********************************/
        searchMap.addList("returnMap", returnMap);
        return searchMap;
    }

    /**
     * 조직업적평가결과 엑셀다운로드
     * @param
     * @return String
     * @throws
     */
    public SearchMap memDeptScoreListExcel(SearchMap searchMap) {
    	String excelFileName = "조직업적평가결과";
    	String excelTitle = "조직업적평가결과";

    	/************************************************************************************
    	 * 조회조건 설정
    	 ************************************************************************************/
    	ArrayList<ExcelVO> excelSearchInfoList = new ArrayList<ExcelVO>();
    	excelSearchInfoList.add(new ExcelVO("평가년도", (String)searchMap.get("findYear")));

    	/****************************************************************************************************
         * 엑셀 다운로드 설정 : '헤더명', '컬럼명', '셀정렬(left, center, right)', 'ROWSPAN COLUMN', '셀너비'
         ****************************************************************************************************/
    	ArrayList<ExcelVO> excelInfoList = new ArrayList<ExcelVO>();
    	excelInfoList.add(new ExcelVO("평가년도", "YEAR", "left", "100"));
    	excelInfoList.add(new ExcelVO("부서코드", "DEPT_CD", "left", "100"));
    	excelInfoList.add(new ExcelVO("부서명", "PULL_DEPT_KOR_NM", "left", "100"));
    	excelInfoList.add(new ExcelVO("등급", "DEPT_GRADE", "center", "100"));
    	excelInfoList.add(new ExcelVO("점수", "DEPT_SCORE", "right", "100"));

    	searchMap.put("excelFileName", excelFileName);
    	searchMap.put("excelTitle", excelTitle);
    	searchMap.put("excelSearchInfoList", excelSearchInfoList);
    	searchMap.put("excelInfoList", excelInfoList);
    	searchMap.put("excelDataList", (ArrayList)getList("mem.eval.memDeptScore.getList", searchMap));

        return searchMap;
    }
}
