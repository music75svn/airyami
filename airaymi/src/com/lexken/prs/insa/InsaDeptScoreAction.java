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
package com.lexken.prs.insa;

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

public class InsaDeptScoreAction extends CommonService {

    private static final long serialVersionUID = 1L;

    // Logger
    private final Log logger = LogFactory.getLog(getClass());

    /**
     * 조직업적평가결과 조회
     * @param
     * @return String
     * @throws
     */
    public SearchMap insaDeptScoreList(SearchMap searchMap) {
    	InsaAction insaDept = new InsaAction();
    	
    	searchMap.put("exceptDirector", "Y");

        return insaDept.insaList(searchMap);
    }

    /**
     * 조직업적평가결과 데이터 조회(xml)
     * @param
     * @return String
     * @throws
     */
    public SearchMap insaDeptScoreList_xml(SearchMap searchMap) {

        searchMap.addList("list", getList("prs.insa.insaDeptScore.getList", searchMap));

        return searchMap;
    }

    /**
     * 엑셀업로드 팝업
     * @param
     * @return String
     * @throws
     */
    public SearchMap popInsaDeptScoreExcelUpload(SearchMap searchMap) {

        return searchMap;
    }

    /**
     * 조직업적평가결과 엑셀 업로드
     * @param
     * @return String
     * @throws
     */
    public SearchMap insaDeptScoreProcess(SearchMap searchMap) {
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
    		
    		returnMap = insertData("prs.insa.insaDeptScore.getBscScore", searchMap);
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
    		
    		returnMap = insertData("prs.insa.insaDeptScore.getBscMonScore", searchMap);
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
        		String[] strDeptNm = (String[]) excelDataList.get(2);
        		String[] strGrade = (String[]) excelDataList.get(3);
        		String[] strScore = (String[]) excelDataList.get(4);

                for(int i=0; i<strYear.length; i++){
                	if(null != strYear[i]){
                		searchMap.put("year", strYear[i]);
                		searchMap.put("deptCd", strDeptCd[i]);
                		searchMap.put("deptNm", strDeptNm[i]);
                		searchMap.put("grade", strGrade[i]);
                		searchMap.put("score", strScore[i]);

                		returnMap = insertData("prs.insa.insaDeptScore.insertData", searchMap);
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
    public SearchMap insaDeptScoreListExcel(SearchMap searchMap) {
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
    	excelInfoList.add(new ExcelVO("부서명", "DEPT_KOR_NM", "left", "100"));
    	excelInfoList.add(new ExcelVO("등급", "GRADE", "center", "100"));
    	excelInfoList.add(new ExcelVO("점수", "SCORE", "right", "100"));

    	searchMap.put("excelFileName", excelFileName);
    	searchMap.put("excelTitle", excelTitle);
    	searchMap.put("excelSearchInfoList", excelSearchInfoList);
    	searchMap.put("excelInfoList", excelInfoList);
    	searchMap.put("excelDataList", (ArrayList)getList("prs.insa.insaDeptScore.getList", searchMap));

        return searchMap;
    }
}
