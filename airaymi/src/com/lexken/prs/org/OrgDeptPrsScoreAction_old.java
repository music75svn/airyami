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
package com.lexken.prs.org;

import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lexken.framework.common.ExcelVO;
import com.lexken.framework.common.SearchMap;
import com.lexken.framework.core.CommonService;
import com.lexken.prs.insa.InsaAction;

public class OrgDeptPrsScoreAction_old extends CommonService {

    private static final long serialVersionUID = 1L;

    // Logger
    private final Log logger = LogFactory.getLog(getClass());

    /**
     * 개인별 조직관리역량평가결과 조회
     * @param
     * @return String
     * @throws
     */
    public SearchMap orgDeptPrsScoreList(SearchMap searchMap) {
    	InsaAction insaDept = new InsaAction();

        return insaDept.insaList(searchMap);
    }

    /**
     * 개인별 조직관리평가결과 데이터 조회(xml)
     * @param
     * @return String
     * @throws
     */
    public SearchMap orgDeptPrsScoreList_xml(SearchMap searchMap) {

        searchMap.addList("list", getList("prs.org.orgDeptPrsScore.getList", searchMap));

        return searchMap;
    }

    /**
     * 평가결과 엑셀다운로드
     * @param
     * @return String
     * @throws
     */
    public SearchMap orgDeptPrsScoreListExcel(SearchMap searchMap) {
    	String excelFileName = "개인별 조직관리역량평가결과";
    	String excelTitle = "개인별 조직관리역량평가결과 리스트";

    	/************************************************************************************
    	 * 조회조건 설정
    	 ************************************************************************************/
    	ArrayList<ExcelVO> excelSearchInfoList = new ArrayList<ExcelVO>();
    	excelSearchInfoList.add(new ExcelVO("평가년도", (String)searchMap.get("yearNm")));
    	excelSearchInfoList.add(new ExcelVO("이름", (String)searchMap.get("userNm")));

    	/****************************************************************************************************
         * 엑셀 다운로드 설정 : '헤더명', '컬럼명', '셀정렬(left, center, right)', 'ROWSPAN COLUMN', '셀너비'
         ****************************************************************************************************/
    	ArrayList<ExcelVO> excelInfoList = new ArrayList<ExcelVO>();
    	excelInfoList.add(new ExcelVO("사번", 				"EMPN", 				"center"));
    	excelInfoList.add(new ExcelVO("이름", 				"KOR_NM", 				"center"));
    	excelInfoList.add(new ExcelVO("부서", 				"DEPT_NM",		 		"left"));
    	excelInfoList.add(new ExcelVO("직급", 				"CAST_TC_NM", 			"center"));
    	excelInfoList.add(new ExcelVO("직위", 				"POS_TC_NM", 			"center"));
    	excelInfoList.add(new ExcelVO("현부서", 			    "CUR_DEPT_NM",		 	"left"));
    	excelInfoList.add(new ExcelVO("현직급", 			    "CUR_CAST_TC_NM", 		"center"));
    	excelInfoList.add(new ExcelVO("현직위", 			    "CUR_POS_TC_NM", 		"center"));
    	excelInfoList.add(new ExcelVO("승진여부", 			"PROMO_YN", 			"center"));
    	excelInfoList.add(new ExcelVO("조직관리역량평가점수", "SCORE", 	            "center"));
    	excelInfoList.add(new ExcelVO("등급", 				"GRADE", 				"center"));

    	searchMap.put("excelFileName", excelFileName);
    	searchMap.put("excelTitle", excelTitle);
    	searchMap.put("excelSearchInfoList", excelSearchInfoList);
    	searchMap.put("excelInfoList", excelInfoList);
    	searchMap.put("excelDataList", getList("prs.org.orgDeptPrsScore.getList", searchMap));

        return searchMap;
    }
}
