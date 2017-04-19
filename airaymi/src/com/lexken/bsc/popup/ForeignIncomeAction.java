/*************************************************************************
* CLASS 명      : foreignIncome
* 작 업 자      : 안요한
* 작 업 일      : 2013년 08월 13일
* 기    능      : 해외사업 수입 실적
* ---------------------------- 변 경 이 력 --------------------------------
* 번호  작 업 자     작     업     일        변 경 내 용                 비고
* ----  --------  -----------------  -------------------------    --------
*   1    안요한      해외사업 수입            최 초 작 업
**************************************************************************/
package com.lexken.bsc.popup;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import com.lexken.framework.common.ErrorMessages;
import com.lexken.framework.common.ExcelVO;
import com.lexken.framework.common.Paging;
import com.lexken.framework.common.SearchMap;
import com.lexken.framework.common.ValidationChk;
import com.lexken.framework.struts2.IsBoxActionSupport;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lexken.framework.core.CommonService;
import com.lexken.framework.login.LoginManager;
import com.lexken.framework.util.StaticUtil;

public class ForeignIncomeAction extends CommonService {

    private static final long serialVersionUID = 1L;

    // Logger
    private final Log logger = LogFactory.getLog(getClass());

    /**
     * 해외사업 수입 실적 조회
     * @param
     * @return String
     * @throws
     */
    public SearchMap foreignIncomeList(SearchMap searchMap) {

    	if ("".equals(StaticUtil.nullToBlank((String)searchMap.get("findDeptId")))) {
			searchMap.put("findDeptId", getStr("bsc.popup.foreignIncome.getScDeptMapping", searchMap));
		}

    	searchMap.addList("itemInfo", getList("bsc.popup.foreignIncome.getItem", searchMap));

        searchMap.addList("deptInfo", getList("bsc.popup.foreignIncome.getDeptInfo", searchMap));

        return searchMap;
    }

    /**
     * 해외사업 수입 실적 데이터 조회(xml)
     * @param
     * @return String
     * @throws
     */
    public SearchMap foreignIncomeList_xml(SearchMap searchMap) {

        searchMap.addList("list", getList("bsc.popup.foreignIncome.getList", searchMap));

        return searchMap;
    }

    /**
     * 엑셀다운로드
     * @param
     * @return String
     * @throws
     */
    public SearchMap excel(SearchMap searchMap) {
    	String excelFileName = "해외사업수입";
    	String excelTitle = "해외사업수입";

    	/************************************************************************************
    	 * 조회조건 설정
    	 ************************************************************************************/
    	ArrayList<ExcelVO> excelSearchInfoList = new ArrayList<ExcelVO>();

    	excelSearchInfoList.add(new ExcelVO("년도", (String)searchMap.get("yearNm")));
    	excelSearchInfoList.add(new ExcelVO("월", (String)searchMap.get("monNm")));
    	excelSearchInfoList.add(new ExcelVO("부서", (String)searchMap.get("deptNm")));
    	excelSearchInfoList.add(new ExcelVO("항목", (String)searchMap.get("itemNm")));

    	/****************************************************************************************************
    	 * 엑셀 다운로드 설정 : '헤더명', '컬럼명', '셀정렬(left, center, right)', 'ROWSPAN COLUMN', '셀너비'
    	 ****************************************************************************************************/
    	ArrayList<ExcelVO> excelInfoList = new ArrayList<ExcelVO>();
    	excelInfoList.add(new ExcelVO("년도","SUMM_YY", "center"));
    	excelInfoList.add(new ExcelVO("월", "SUMM_MM", "center"));
    	excelInfoList.add(new ExcelVO("부서명", "DEPT_NM", "left"));
    	excelInfoList.add(new ExcelVO("검사종류", "INSP_KIND_TC_NM", "center"));
    	excelInfoList.add(new ExcelVO("통계분류명", "STIC_LAG_TC_NM", "center"));
    	excelInfoList.add(new ExcelVO("처리건수", "TY_QTY", "center"));
    	excelInfoList.add(new ExcelVO("실적금액", "TY_AMT", "center"));


    	searchMap.put("excelFileName", excelFileName);
    	searchMap.put("excelTitle", excelTitle);
    	searchMap.put("excelSearchInfoList", excelSearchInfoList);
    	searchMap.put("excelInfoList", excelInfoList);

    	searchMap.put("excelDataList", (ArrayList)getList("bsc.popup.foreignIncome.getList", searchMap));

    	return searchMap;
    }
}
