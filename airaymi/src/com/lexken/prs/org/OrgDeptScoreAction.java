/*************************************************************************
* CLASS 명      : OrgConnItemAction
* 작 업 자      : 지원길
* 작 업 일      : 2013년 6월 10일
* 기    능      : 조직관리평가결과
* ---------------------------- 변 경 이 력 --------------------------------
* 번호  작 업 자     작     업     일        변 경 내 용                 비고
* ----  --------  -----------------  -------------------------    --------
*   1    지원길      2013년 6월 10일            최 초 작 업
**************************************************************************/
package com.lexken.prs.org;

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

public class OrgDeptScoreAction extends CommonService {

    private static final long serialVersionUID = 1L;

    // Logger
    private final Log logger = LogFactory.getLog(getClass());

    /**
     * 조직관리평가결과 조회
     * @param
     * @return String
     * @throws
     */
    public SearchMap orgDeptScoreList(SearchMap searchMap) {
    	InsaAction insaDept = new InsaAction();
    	searchMap.addList("rptSchedule", getDetail("prs.org.orgDeptScore.getRptSchedule", searchMap)); //입력기간 조회

        return insaDept.insaList(searchMap);
    }

    /**
     * 조직관리평가결과 데이터 조회(xml)
     * @param
     * @return String
     * @throws
     */
    public SearchMap orgDeptScoreList_xml(SearchMap searchMap) {

    	searchMap.addList("rptSchedule", getDetail("prs.org.orgDeptScore.getRptSchedule", searchMap)); //입력기간 조회
        searchMap.addList("list", getList("prs.org.orgDeptScore.getList", searchMap));

        return searchMap;
    }
    
    /**
     * 엑셀업로드 팝업
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap popExcelUpload(SearchMap searchMap) {
    	
        return searchMap;
    }

    /**
     * 조직관리평가결과 엑셀 업로드
     * @param
     * @return String
     * @throws
     */
    public SearchMap orgDeptScoreProcess(SearchMap searchMap) {
        HashMap returnMap = new HashMap();
        String stMode = searchMap.getString("mode");

        /**********************************
         * 등록/수정/삭제
         **********************************/
        if("ADDEXCEL".equals(stMode)) {
            searchMap = insertExcelDB(searchMap);
        } else if("CALL".equals(stMode)) {
            searchMap = callDB(searchMap);
        }

        /**********************************
         * Return
         **********************************/
        return searchMap;
    }


    /**
     * 조직관리평가결과 등록
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
        	excelDataList = excel.execlOrgDeptScoreUpload(searchMap);
        	
        	if(null != excelDataList && 0 < excelDataList.size()) {
        		String[] strYear = (String[]) excelDataList.get(0);
        		String[] strDeptCd = (String[]) excelDataList.get(1);
        		String[] strGrade = (String[]) excelDataList.get(2);
        		String[] strScore = (String[]) excelDataList.get(3);
                
                for(int i=0; i<strYear.length; i++){
                	if(null != strYear[i]){
                		searchMap.put("year", strYear[i]);
                		searchMap.put("deptCd", strDeptCd[i]);
                		searchMap.put("grade", strGrade[i]);
                		searchMap.put("score", strScore[i]);
                		
                		returnMap = insertData("prs.org.orgDeptScore.insertData", searchMap);
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
     * 조직관리평가결과 엑셀다운로드
     * @param
     * @return String
     * @throws
     */
    public SearchMap orgDeptScoreListExcel(SearchMap searchMap) {
    	String excelFileName = "조직관리역량평가결과";
    	String excelTitle = "조직관리역량평가결과";

    	/************************************************************************************
    	 * 조회조건 설정
    	 ************************************************************************************/
    	ArrayList<ExcelVO> excelSearchInfoList = new ArrayList<ExcelVO>();
    	excelSearchInfoList.add(new ExcelVO("평가년도", (String)searchMap.get("findYear")));

    	/****************************************************************************************************
         * 엑셀 다운로드 설정 : '헤더명', '컬럼명', '셀정렬(left, center, right)', 'ROWSPAN COLUMN', '셀너비'
         ****************************************************************************************************/
    	ArrayList<ExcelVO> excelInfoList = new ArrayList<ExcelVO>();
    	excelInfoList.add(new ExcelVO("기년년도", "YEAR", "left", "100"));
    	excelInfoList.add(new ExcelVO("부서코드", "DEPT_CD", "left", "100"));
    	excelInfoList.add(new ExcelVO("부서명", "DEPT_KOR_NM", "left", "100"));
    	excelInfoList.add(new ExcelVO("등급", "GRADE", "center", "100"));
    	excelInfoList.add(new ExcelVO("점수", "SCORE", "right", "100"));

    	searchMap.put("excelFileName", excelFileName);
    	searchMap.put("excelTitle", excelTitle);
    	searchMap.put("excelSearchInfoList", excelSearchInfoList);
    	searchMap.put("excelInfoList", excelInfoList);
    	searchMap.put("excelDataList", (ArrayList)getList("prs.org.orgDeptScore.getList", searchMap));

        return searchMap;
    }
    
    /**
     * 프로시저 실행
     * @param      
     * @return String  
     * @throws 
     */
    public SearchMap callDB(SearchMap searchMap) {
        HashMap returnMap    = new HashMap();    
        
        try {
        	setStartTransaction();
        	
        	returnMap = insertData("prs.org.orgDeptScore.callSpPrsMngDeptScore", searchMap);
        
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
}
